package com.prateekgupta.mappings.OneToMany.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ContactDetails {
    @Id
    private int id;
    private long contactNumber;
    @ManyToOne(cascade = CascadeType.ALL)
    private BasicContactDetails BasicContactDetails;

    @Override
    public String toString() {
        return "ContactDetails{" +
                "id=" + id +
                ", contactNumber=" + contactNumber +
                '}';
    }
}
