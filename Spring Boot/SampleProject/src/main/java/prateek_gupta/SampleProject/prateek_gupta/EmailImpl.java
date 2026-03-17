package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import javax.activation.DataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;

public class EmailImpl implements Email {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    AWS aws;

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
            JSONObject attachment=null;

            /*
            if (attachments != null) {
                for (Object attachmentObj : attachments) {
                    attachment=JSONObject.fromObject(attachmentObj.toString());
                    try {
                        String fileUrl = attachment.getString("fileUrl");

                        byte[] fileContent =null;

                        if (!fileUrl.contains("https://"))
                            fileContent=aws.getFile();

                        if (fileContent != null) {
                            ResponseInputStream<GetObjectResponse> temp=null;
                            DataSource dataSource =
                                    new ByteArrayDataSource(temp, attachment.getContentType());

                            helper.addAttachment(
                                    attachment.getFileName(),
                                    dataSource
                            );

                        } else {
                            failedAttachments.add(attachment.getFileName());
                        }

                    } catch (Exception e) {
                        failedAttachments.add(attachment.getFileName());
                    }
                }
            }
            */

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
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
