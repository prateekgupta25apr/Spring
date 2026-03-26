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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
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
    public JSONObject getEmailContent(String messageId, String filePath, boolean fetchFileUrl) {
        JSONObject response = new JSONObject();
        try{
            if (StringUtils.isEmpty(messageId)&&StringUtils.isEmpty(filePath))
                throw new ServiceException("Invalid message id and file path");

            InputStream emailContent=null;
            if (StringUtils.isNotBlank(messageId)){
                byte[] emailContentBytes= aws.getFileContentInBytes("emails/"+messageId);
                emailContent = new ByteArrayInputStream(emailContentBytes);
            }

            if (StringUtils.isNotBlank(filePath)){
                emailContent = new FileInputStream(filePath);
            }

            MimeMessage message = new MimeMessage(session, emailContent);
            response.put("sender",((InternetAddress)message.getFrom()[0]).getAddress());
            response.put("subject",message.getSubject());

            Object content = message.getContent();

            JSONArray attachments = new JSONArray();

            if (content instanceof String) {
                response.put("text_body", content);
            } else if (content instanceof Multipart) {
                parseMultipart((Multipart) content,attachments,response,fetchFileUrl);
            }

            // Adding attachments
            response.put("attachments",attachments);

        }catch(Exception e){
            ServiceException.logException(e);
        }
        return response;
    }

    private void parseMultipart(Multipart multipart,
                                JSONArray attachments,
                                JSONObject emailDetails,
                                boolean fetchFileUrl) throws Exception {

        for (int i = 0; i < multipart.getCount(); i++) {

            BodyPart part = multipart.getBodyPart(i);

            if (part.isMimeType("multipart/*")) {
                parseMultipart((Multipart) part.getContent(),
                        attachments, emailDetails, fetchFileUrl);
                continue;
            }

            String contentType = part.getContentType().split(";")[0];
            String disposition = part.getDisposition();

            // Setting HTML body
            if (part.isMimeType("text/html") && disposition == null)
                emailDetails.put("html_body", part.getContent().toString());


            // Setting Plain text body
            else if (part.isMimeType("text/plain") && disposition == null)
                emailDetails.put("text_body", part.getContent().toString());


            // Attachments / Inline
            else if (MimeBodyPart.ATTACHMENT.equalsIgnoreCase(disposition)
                    || MimeBodyPart.INLINE.equalsIgnoreCase(disposition)
                    || part.getFileName() != null) {

                JSONObject attachment = new JSONObject();

                String fileName = part.getFileName();

                attachment.put("filename", fileName);
                attachment.put("mime_type", contentType);

                if (MimeBodyPart.INLINE.equalsIgnoreCase(disposition)) {
                    attachment.put("content-type", "Inline");
                    attachment.put("content_id",
                            part.getHeader("Content-ID") != null ?
                                    part.getHeader("Content-ID")[0] : null);
                } else {
                    attachment.put("content-type", "Attachment");
                }

                // Get binary data
                byte[] fileBytes = part.getInputStream().readAllBytes();

                // Uploading file
                if (fetchFileUrl) {

                    String newFileName = aws.updateFileName(fileName,"emails_attachment/");

                    aws.uploadFile(fileBytes, newFileName, contentType);

                    String url = aws.generatePreSignedUrl(newFileName,"GET");

                    attachment.put("file_url", url);
                }

                attachments.add(attachment);
            }
        }
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
            Object[] htmlContentAndInlineAttachments =
                    getHtmlContentAndInlineAttachments(content);
            String htmlContent = htmlContentAndInlineAttachments[0].toString();
            JSONArray inlineAttachment = (JSONArray) htmlContentAndInlineAttachments[1];

            // Creating multipart/related content
            MimeMultipart relatedMultipart = new MimeMultipart("related");

            // Creating multipart/alternative content for plain and HTML text
            MimeBodyPart alternativeWrapper = wrapPlainAndHTMLContent
                    (plainContent, htmlContent);

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
                        byte[] fileContent=null;

                        // Fetching file based on file name
                        if (!fileUrl.contains("https://"))
                            fileContent = aws.getFileContentInBytes(fileUrl);
                            // Fetching file based on pre-signed url
                        else {
                            HttpResponse<byte[]> response = HttpClient.newHttpClient()
                                    .send(HttpRequest.newBuilder(
                                            new URI(fileUrl)).GET().build(),
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    );
                            if (response.statusCode() == 200)
                                fileContent = response.body();
                        }

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null) {

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
                        byte[] fileContent=null;

                        // Fetching file based on file name
                        if (StringUtils.isNotBlank(fileKey))
                            fileContent=aws.getFileContentInBytes(fileKey);
                        // Fetching file based on pre-signed url
                        else{
                            HttpResponse<byte[]> response =
                                    HttpClient.newHttpClient()
                                    .send(HttpRequest.newBuilder(
                                            new URI(fileUrl)).GET().build(),
                                            HttpResponse.BodyHandlers.ofByteArray()
                                    );
                            if (response.statusCode() == 200)
                                fileContent = response.body();
                        }

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

    @Override
    public void processEmail(String messageId, String filePath, String toEmail) {
        try{
            if (StringUtils.isEmpty(messageId) && StringUtils.isEmpty(filePath))
                throw new ServiceException("Invalid message id and file path");

            InputStream emailContent=null;
            if (StringUtils.isNotBlank(messageId)){
                byte[] emailContentBytes= aws.getFileContentInBytes(
                        "emails/"+messageId);
                emailContent = new ByteArrayInputStream(emailContentBytes);
            }

            if (StringUtils.isNotBlank(filePath)){
                emailContent = new FileInputStream(filePath);
            }

            if (StringUtils.isBlank(toEmail))
                toEmail="prateek.gupta25apr@gmail.com";

            MimeMessage receivedMessage = new MimeMessage(session, emailContent);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(((InternetAddress)receivedMessage.getFrom()[0]));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("Email Received");

            String textBody = "Received an email from "+
                    ((InternetAddress)receivedMessage.getFrom()[0]).getAddress()
            +"with subject "+receivedMessage.getSubject();
            String htmlBody = "<p>Received an email from <b>"+
                    ((InternetAddress)receivedMessage.getFrom()[0]).getAddress()
            +"</b> with subject <b>"+receivedMessage.getSubject()+"</b></p>";

            String[] texts=extractTexts((Multipart) receivedMessage.getContent());
            textBody +=texts[0];
            htmlBody +=texts[1];

            // Creating multipart/related content
            MimeMultipart relatedMultipart = new MimeMultipart("related");

            // Creating multipart/alternative content for plain and HTML text
            MimeBodyPart alternativeWrapper = wrapPlainAndHTMLContent(textBody, htmlBody);

            // Adding plain and HTML content to multipart/related part
            relatedMultipart.addBodyPart(alternativeWrapper);

            updateAttachments((Multipart) receivedMessage.getContent(),relatedMultipart);

            // Adding multipart/related data to email
            message.setContent(relatedMultipart);

            // Saving all the changes
            message.saveChanges();

            // Sending email
            Transport.send(message);
        }catch(Exception e){
            ServiceException.logException(e);
        }
    }

    String[] extractTexts(Multipart receivedMultipart) throws Exception {
        StringBuilder textBody = new StringBuilder();
        StringBuilder htmlBody = new StringBuilder();
        for (int i = 0; i < receivedMultipart.getCount(); i++) {

            BodyPart part = receivedMultipart.getBodyPart(i);

            if (part.isMimeType("multipart/*")) {
                String[] response= extractTexts((Multipart) part.getContent());
                textBody.append(response[0]);
                htmlBody.append(response[1]);
                continue;
            }

            String disposition = part.getDisposition();

            // Setting HTML body
            if (part.isMimeType("text/html") && disposition == null)
                htmlBody = new StringBuilder(part.getContent().toString());


            // Setting Plain text body
            else if (part.isMimeType("text/plain") && disposition == null)
                textBody = new StringBuilder(part.getContent().toString());
        }
        return new String[]{textBody.toString(), htmlBody.toString()};
    }

    private void updateAttachments(
            Multipart receivedMultipart,Multipart multipart) throws Exception {

        for (int i = 0; i < receivedMultipart.getCount(); i++) {

            BodyPart part = receivedMultipart.getBodyPart(i);

            if (part.isMimeType("multipart/*")) {
                updateAttachments((Multipart) part.getContent(),multipart);
                continue;
            }

            String contentType = part.getContentType().split(";")[0];
            String disposition = part.getDisposition();


            // Attachments / Inline
            if (MimeBodyPart.ATTACHMENT.equalsIgnoreCase(disposition)
                    || MimeBodyPart.INLINE.equalsIgnoreCase(disposition)
                    || part.getFileName() != null) {

                // Get binary data
                byte[] fileContent = part.getInputStream().readAllBytes();

                DataSource dataSource = new ByteArrayDataSource(
                        fileContent, contentType);

                String fileName = part.getFileName();

                if (MimeBodyPart.INLINE.equalsIgnoreCase(disposition)) {
                    MimeBodyPart inlinePart = new MimeBodyPart();
                    String cid=part.getHeader("Content-ID") != null ?
                            part.getHeader("Content-ID")[0] : "";

                    inlinePart.setDataHandler(new DataHandler(dataSource));
                    // CID already have "<" and ">"
                    inlinePart.setHeader("Content-ID", cid );
                    inlinePart.setDisposition(MimeBodyPart.INLINE);
                    inlinePart.setFileName(fileName);

                    multipart.addBodyPart(inlinePart);
                } else {

                    MimeBodyPart normalPart = new MimeBodyPart();

                    normalPart.setDataHandler(new DataHandler(dataSource));
                    normalPart.setDisposition(MimeBodyPart.ATTACHMENT);
                    normalPart.setFileName(fileName);

                    multipart.addBodyPart(normalPart);
                }
            }
        }
    }
}
