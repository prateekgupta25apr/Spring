package com.prateekgupta.DocumentGenerator.entities;

import javax.persistence.*;

@Entity
@Table(name = "document_html_content_mapping")
public class DocumentHTMLContentMapping {
    @Id
    @Column(name = "document_html_content_mapping_id")
    Integer documentHTMLContentMappingId;

    @ManyToOne
    @JoinColumn(name = "document_id")
    DocumentMaster documentMaster;

    @ManyToOne
    @JoinColumn(name = "html_content_id")
    HTMLContentMaster htmlContentMaster;
}
