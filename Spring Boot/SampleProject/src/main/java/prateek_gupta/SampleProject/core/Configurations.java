package prateek_gupta.SampleProject.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "configurations")
public class Configurations {

    @Id
    private Integer id;

    @Column(name = "`key`", length = 100, nullable = true)
    private String key;

    @Column(name = "`value`", length = 1000, nullable = true)
    private String value;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

