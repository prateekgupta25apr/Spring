package com.prateekgupta.DocumentGenerator.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tabular_content_master")
public class TabularContentMaster {
    @Id
    @Column(name = "tabular_content_id")
    Integer tabularContentId;

    @Column(name = "header_value")
    String headerValue;

    @Column(name = "normal_value")
    String normalValue;

    public Integer getTabularContentId() {
        return tabularContentId;
    }

    public void setTabularContentId(Integer tabularContentId) {
        this.tabularContentId = tabularContentId;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getNormalValue() {
        return normalValue;
    }

    public void setNormalValue(String normalValue) {
        this.normalValue = normalValue;
    }

    @Override
    public String toString() {
        return "TabularContentMaster{" +
                "tabularContentId=" + tabularContentId +
                ", headerValue='" + headerValue + '\'' +
                ", normalValue='" + normalValue + '\'' +
                '}';
    }
}
