package com.prateekgupta.DocumentGenerator.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "html_content_master")
public class HTMLContentMaster {
    @Id
    @Column(name = "html_content_id")
    Integer htmlContentId;

    @Column(name = "html_content")
    String htmlContent;

    public Integer getHtmlContentId() {
        return htmlContentId;
    }

    public void setHtmlContentId(Integer htmlContentId) {
        this.htmlContentId = htmlContentId;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    @Override
    public String toString() {
        return "HTMLContentMaster{" +
                "htmlContentId=" + htmlContentId +
                ", htmlContent='" + htmlContent + '\'' +
                '}';
    }
}
