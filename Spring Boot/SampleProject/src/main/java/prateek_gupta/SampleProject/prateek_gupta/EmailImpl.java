package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

public class EmailImpl implements Email {

    private final JavaMailSender mailSender;

    @Autowired
    AWS aws;

    public EmailImpl(
            String smtpServer,String smtpPort,String smtpUsername,String smtpPassword,
            String sendGridEnabled,String sendGridServer,String sendGridPort,
            String sendGridUsername, String sendGridPassword
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        boolean sendGrid = Boolean.parseBoolean(sendGridEnabled);

        if (sendGrid) {

            mailSender.setHost(sendGridServer);
            mailSender.setPort(Integer.parseInt(sendGridPort));
            mailSender.setUsername(sendGridUsername);
            mailSender.setPassword(sendGridPassword);

        } else {

            mailSender.setHost(smtpServer);
            mailSender.setPort(Integer.parseInt(smtpPort));
            mailSender.setUsername(smtpUsername);
            mailSender.setPassword(smtpPassword);
        }

        Properties javaMailProps = mailSender.getJavaMailProperties();

        javaMailProps.put("mail.smtp.auth", "true");
        javaMailProps.put("mail.smtp.starttls.enable", "true");
        javaMailProps.put("mail.smtp.connectiontimeout", "5000");
        javaMailProps.put("mail.smtp.timeout", "5000");
        javaMailProps.put("mail.smtp.writetimeout", "5000");

        this.mailSender = mailSender;
    }

    @Override
    public JSONArray send(
            String fromEmail, String toEmail, String subject,
            String content, JSONArray attachments) {

        JSONArray failedAttachments = new JSONArray();

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String plainContent = getPlainContent(content);
            //JSONArray inlineAttachment = getInlineAttachment(content);
            String htmlContent = getHtmlContent(content);

            helper.setText(plainContent, htmlContent);
            JSONObject attachment;


            if (attachments != null) {
                for (Object attachmentObj : attachments) {
                    attachment=JSONObject.fromObject(attachmentObj.toString());
                    String fileUrl = attachment.has("file_url")?
                            attachment.getString("file_url"):"";
                    String fileName = attachment.getString("file_name");
                    try {
                        byte[] fileContent =null;

                        if (!fileUrl.contains("https://"))
                            fileContent=aws.getFileContentInBytes(fileName);

                        String contentType = aws.getFileDetails(fileName).contentType();

                        if (fileContent != null) {
                            DataSource dataSource =
                                    new ByteArrayDataSource(fileContent, contentType);

                            helper.addAttachment(
                                    fileName,
                                    dataSource
                            );

                        } else {
                            failedAttachments.add(fileName);
                        }

                    } catch (Exception e) {
                        failedAttachments.add(fileName);
                    }
                }
            }


            mailSender.send(message);

        } catch (Exception e) {
            ServiceException.logException(e);
        }

        return failedAttachments;
    }

    private String getHtmlContent(String content) {
        return content;
    }

    private String getPlainContent(String content) {
        return content;
    }

}
