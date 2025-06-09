package prateek_gupta.SampleProject.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

