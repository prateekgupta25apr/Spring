package com.prateekgupta.DocumentGenerator.entities;

import javax.persistence.*;

@Entity
@Table(name = "document_tabular_content_mapping")
public class DocumentTabularContentMapping {

    @Id
    @Column(name = "document_tabular_content_mapping_id")
    Integer documentTabularContentMappingId;

    @ManyToOne
    @JoinColumn(name = "document_id")
    DocumentMaster documentMaster;

    @ManyToOne
    @JoinColumn(name = "tabular_content_id")
    TabularContentMaster tabularContentMaster;

    public Integer getDocumentTabularContentMappingId() {
        return documentTabularContentMappingId;
    }

    public void setDocumentTabularContentMappingId(Integer documentTabularContentMappingId) {
        this.documentTabularContentMappingId = documentTabularContentMappingId;
    }

    public DocumentMaster getDocumentMaster() {
        return documentMaster;
    }

    public void setDocumentMaster(DocumentMaster documentMaster) {
        this.documentMaster = documentMaster;
    }

    public TabularContentMaster getTabularContentMaster() {
        return tabularContentMaster;
    }

    public void setTabularContentMaster(TabularContentMaster tabularContentMaster) {
        this.tabularContentMaster = tabularContentMaster;
    }
}
