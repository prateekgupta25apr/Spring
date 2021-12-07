package com.prateekgupta.mappings.OneToOne.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BasicAddressDetails {
    @Id
    private int basicDetailsId;
    private String name;
    private String email;
    @OneToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "address")
    private AddressDetails address;

    @Override
    public String toString() {
        return "BasicAddressDetails{" +
                "basicDetailsId=" + basicDetailsId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }
}
