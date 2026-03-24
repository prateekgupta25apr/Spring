package prateek_gupta.SampleProject.prateek_gupta;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class EmailImpl implements Email {

    private final Session session;

    @Autowired
    AWS aws;

    public EmailImpl(
            String smtpServer, String smtpPort, String smtpUsername, String smtpPassword,
            String sendGridEnabled, String sendGridServer, String sendGridPort,
            String sendGridUsername, String sendGridPassword
    ) {
        Properties properties = new Properties();

        boolean sendGrid = Boolean.parseBoolean(sendGridEnabled);
        String username;
        String password;
        if (sendGrid) {
            username = sendGridUsername;
            password = sendGridPassword;
            properties.put("mail.smtp.host", sendGridServer);
            properties.put("mail.smtp.port", sendGridPort);

        } else {
            username=smtpUsername;
            password=smtpPassword;
            properties.put("mail.smtp.host", smtpServer);
            properties.put("mail.smtp.port", smtpPort);
        }

        // Common configs
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        this.session = Session.getInstance(
                properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    @Override
    public JSONArray send(
            String fromEmail, String toEmail, String subject,
            String content, JSONArray attachments) {

        JSONArray failedAttachments = new JSONArray();

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);

            String plainContent = getPlainContent(content);
            Object[] htmlContentAndInlineAttachments = getHtmlContentAndInlineAttachments(content);
            String htmlContent = htmlContentAndInlineAttachments[0].toString();
            JSONArray inlineAttachment = (JSONArray) htmlContentAndInlineAttachments[1];

            // Creating multipart/related content
            MimeMultipart relatedMultipart = new MimeMultipart("related");

            // Creating multipart/alternative content for plain and HTML text
            MimeBodyPart alternativeWrapper = wrapPlainAndHTMLContent(plainContent, htmlContent);

            // Adding plain and HTML content to multipart/related part
            relatedMultipart.addBodyPart(alternativeWrapper);

            JSONObject attachment;

            // Processing Inline Attachments
            if (inlineAttachment != null) {
                for (Object attachmentObj : inlineAttachment) {
                    attachment = JSONObject.fromObject(attachmentObj.toString());
                    String fileUrl = attachment.has("file_url") ?
                            attachment.getString("file_url") : "";
                    String fileName = attachment.getString("file_name");
                    String cid = attachment.getString("cid");
                    try {
                        byte[] fileContent;

                        // Fetching file based on file name
                        if (!fileUrl.contains("https://"))
                            fileContent = aws.getFileContentInBytes(fileUrl);
                            // Fetching file based on pre-signed url
                        else
                            fileContent = HttpClient.newHttpClient()
                                    .send(HttpRequest.newBuilder(new URI(fileUrl)).GET().build(),
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    ).body();

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null && fileContent.length != 0) {

                            MimeBodyPart inlinePart = new MimeBodyPart();

                            DataSource dataSource = new ByteArrayDataSource(
                                    fileContent, contentType);

                            inlinePart.setDataHandler(new DataHandler(dataSource));
                            inlinePart.setHeader("Content-ID", "<" + cid + ">");
                            inlinePart.setDisposition(MimeBodyPart.INLINE);
                            inlinePart.setFileName(fileName);

                            relatedMultipart.addBodyPart(inlinePart);

                        } else {
                            failedAttachments.add(fileName);
                        }
                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }

            // Processing Normal Attachments
            if (attachments != null) {
                for (Object attachmentObj : attachments) {
                    attachment=JSONObject.fromObject(attachmentObj.toString());
                    String fileUrl = attachment.has("file_url")?
                            attachment.getString("file_url"):"";
                    String fileKey = attachment.has("file_key")?
                            attachment.getString("file_key"):"";
                    String fileName = attachment.getString("file_name");
                    try {
                        byte[] fileContent;

                        // Fetching file based on file name
                        if (StringUtils.isNotBlank(fileKey))
                            fileContent=aws.getFileContentInBytes(fileKey);
                        // Fetching file based on pre-signed url
                        else
                            fileContent = java.net.http.HttpClient.newHttpClient()
                                    .send(HttpRequest.newBuilder(new URI(fileUrl)).GET().build(),
                                          HttpResponse.BodyHandlers.ofByteArray()
                                    ).body();

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null) {
                            DataSource dataSource =
                                    new ByteArrayDataSource(fileContent, contentType);

                            MimeBodyPart normalPart = new MimeBodyPart();

                            normalPart.setDataHandler(new DataHandler(dataSource));
                            normalPart.setDisposition(MimeBodyPart.ATTACHMENT);
                            normalPart.setFileName(fileName);

                            relatedMultipart.addBodyPart(normalPart);

                        } else {
                            failedAttachments.add(fileName);
                        }
                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }

            // Adding multipart/related data to email
            message.setContent(relatedMultipart);

            // Saving all the changes
            message.saveChanges();

            // Sending email
            Transport.send(message);
        } catch (Exception e) {
            ServiceException.logException(e);
        }
        return failedAttachments;
    }

    @Nonnull
    MimeBodyPart wrapPlainAndHTMLContent(
            String plainContent, String htmlContent) throws MessagingException {
        MimeMultipart alternativeMultipart = new MimeMultipart("alternative");

        // Creating Plain text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(plainContent);

        // Creating HTML text part
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

        // Setting HTML and Plain text parts
        alternativeMultipart.addBodyPart(textPart);
        alternativeMultipart.addBodyPart(htmlPart);

        // Converting MimeMultipart class object to MimeBodyPart,
        // so we can add to multipart/related content
        MimeBodyPart alternativeWrapper = new MimeBodyPart();
        alternativeWrapper.setContent(alternativeMultipart);
        return alternativeWrapper;
    }

    public Object[] getHtmlContentAndInlineAttachments(
            String content) {
        JSONArray inlineAttachments = new JSONArray();
        Document html = Jsoup.parse(content);
        html.select("img").forEach(tag -> {
            // Getting image tag message
            String imageUrl = tag.attr("src");
            String fileName = aws.extractFileName(imageUrl, true);
            String fileKey = aws.updateFileName(fileName);
            int dotIndex = fileKey.lastIndexOf('.');
            String cid = (dotIndex == -1) ? fileKey : fileKey.substring(0, dotIndex);
            String ext = (dotIndex == -1) ? "" : fileKey.substring(dotIndex);
            tag.attr("src", "cid:" + cid);

            JSONObject inlineAttachment = new JSONObject();
            inlineAttachment.put("file_key", fileKey);
            inlineAttachment.put("file_url", imageUrl);
            inlineAttachment.put("cid", cid);
            inlineAttachment.put("ext", ext);
            inlineAttachment.put("file_name", fileName);
            inlineAttachments.add(inlineAttachment);
        });
        return new Object[]{html.html(), inlineAttachments};
    }

    public String getPlainContent(String content) {
        Document html = Jsoup.parse(content);
        html.select("img").forEach(tag -> {
            // Getting image tag message
            String altMessage = tag.attr("alt");
            if (altMessage.isEmpty())
                altMessage = "image";

            // Creating new P tag
            Element pTag = html.createElement("p");

            // Setting image tag message in P tag
            pTag.text("[" + altMessage + "]");

            // Replacing image tag with P tag
            tag.replaceWith(pTag);
        });
        return html.text();
    }

}
