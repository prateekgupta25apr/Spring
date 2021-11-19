package com.prateekgupta.mappings.OneToOne.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BasicDetails {
    @Id
    private int basicDetailsId;
    private String name;
    private String email;
    @OneToOne
    private AddressDetails address;
}
