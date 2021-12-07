package com.prateekgupta.mappings.OneToOne.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class AddressDetails {
    @Id
    private int id;
    private String flatNo;
    private String streetName;
    private String city;
    private String state;
    private String country;
    @OneToOne(mappedBy = "address")
    private BasicAddressDetails BasicAddressDetails;

    @Override
    public String toString() {
        return "AddressDetails{" +
                "id=" + id +
                ", flatNo='" + flatNo + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
