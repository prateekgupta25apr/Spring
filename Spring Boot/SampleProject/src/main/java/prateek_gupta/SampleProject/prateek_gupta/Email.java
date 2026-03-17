package prateek_gupta.SampleProject.prateek_gupta;

import net.sf.json.JSONArray;

public interface Email {
    JSONArray send(
            String fromEmail, String toEmail, String subject,
            String content, JSONArray attachments);
}
