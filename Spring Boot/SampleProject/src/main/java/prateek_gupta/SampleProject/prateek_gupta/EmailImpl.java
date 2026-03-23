package prateek_gupta.SampleProject.prateek_gupta;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
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

    private Session mailSender;

    @Autowired
    AWS aws;

    public EmailImpl(
            String smtpServer, String smtpPort, String smtpUsername, String smtpPassword,
            String sendGridEnabled, String sendGridServer, String sendGridPort,
            String sendGridUsername, String sendGridPassword
    ) {
        Properties properties = new Properties();

        boolean sendGrid = Boolean.parseBoolean(sendGridEnabled);

        if (sendGrid) {

            properties.put("mail.smtp.host", sendGridServer);
            properties.put("mail.smtp.port", sendGridPort);

        } else {

            properties.put("mail.smtp.host", smtpServer);
            properties.put("mail.smtp.port", smtpPort);
        }

        // Common configs
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Authentication
        String username = sendGrid ? sendGridUsername : smtpUsername;
        String password = sendGrid ? sendGridPassword : smtpPassword;

        this.mailSender = Session.getInstance(
                properties, new javax.mail.Authenticator() {
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
            MimeMessage message = new MimeMessage(mailSender);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject(subject);

            String plainContent = getPlainContent(content);
            Object[] htmlContentAndInlineAttachments = getHtmlContentAndInlineAttachments(content);
            String htmlContent = htmlContentAndInlineAttachments[0].toString();
            JSONArray inlineAttachment = (JSONArray) htmlContentAndInlineAttachments[1];

            // 🔥 multipart/related (root)
            MimeMultipart relatedMultipart = new MimeMultipart("related");

            // 🔥 multipart/alternative (text + html)
            MimeMultipart alternativeMultipart = new MimeMultipart("alternative");

            // Plain text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(plainContent);

            // HTML part
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            // Add to alternative
            alternativeMultipart.addBodyPart(textPart);
            alternativeMultipart.addBodyPart(htmlPart);

            // Wrap alternative inside a body part
            MimeBodyPart alternativeWrapper = new MimeBodyPart();
            alternativeWrapper.setContent(alternativeMultipart);

            // Add to related
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
                            fileContent =
                                    HttpClient.newHttpClient()
                                    .send(
                                            HttpRequest.newBuilder(new URI(fileUrl)).GET().build(),
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    ).body();

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null && fileContent.length != 0) {

                            MimeBodyPart inlinePart = new MimeBodyPart();

                            DataSource ds = new ByteArrayDataSource(fileContent, contentType);

                            inlinePart.setDataHandler(new DataHandler(ds));
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
            /*if (attachments != null) {
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
                                    .send(
                                            java.net.http.HttpRequest.newBuilder(new URI(fileUrl)).GET().build(),
                                            java.net.http.HttpResponse.BodyHandlers.ofByteArray()
                                    ).body();

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null) {
                            DataSource dataSource =
                                    new ByteArrayDataSource(fileContent, contentType);

                            helper.addAttachment(fileName,dataSource);

                        } else {
                            failedAttachments.add(fileName);
                        }
                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }*/

            // Set final content
            message.setContent(relatedMultipart);
            message.saveChanges();
            Transport.send(message);
        } catch (Exception e) {
            ServiceException.logException(e);
        }
        return failedAttachments;
    }

    public Object[] getHtmlContentAndInlineAttachments(String content) {
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
