package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface Email {
    JSONObject getEmailContent(String messageId,String filePath,boolean fetchFileUrl);

    JSONArray send(
            String fromEmail, String toEmail, String subject,
            String content, JSONArray attachments);

    void processEmail(String messageId,String filePath,String toEmail);

    String getPlainContent(String content);

    Object[] getHtmlContentAndInlineAttachments(String content);
}
