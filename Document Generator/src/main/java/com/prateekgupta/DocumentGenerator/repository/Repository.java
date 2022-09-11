package com.prateekgupta.DocumentGenerator.repository;

import com.prateekgupta.DocumentGenerator.entities.DocumentMaster;
import com.prateekgupta.DocumentGenerator.entities.HTMLContentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Repository extends JpaRepository<DocumentMaster,Integer> {
    @Query("select hcm from DocumentMaster dm, HTMLContentMaster hcm, DocumentHTMLContentMapping dhm  where dm.documentId=dhm.documentMaster.documentId and dhm.htmlContentMaster.htmlContentId=hcm.htmlContentId and dm.documentId=:documentId")
    HTMLContentMaster getHTMLContent(@Param("documentId") Integer documentId);
}
