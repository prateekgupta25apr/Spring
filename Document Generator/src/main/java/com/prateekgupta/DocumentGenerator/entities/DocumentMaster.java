package com.prateekgupta.DocumentGenerator.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document_master")
public class DocumentMaster {
    @Id
    @Column(name = "document_id")
    Integer documentId;

    @Column(name = "document_title")
    String documentTitle;
}
