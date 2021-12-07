package com.prateekgupta.mappings.OneToMany.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class BasicContactDetails {
    @Id
    private int basicDetailsId;
    private String name;
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ContactDetails> contactDetails;

    @Override
    public String toString() {
        return "BasicContactDetails{" +
                "basicDetailsId=" + basicDetailsId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactDetails=" + contactDetails +
                '}';
    }
}
