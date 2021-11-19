package com.prateekgupta.mappings.OneToOne.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table
public class BasicDetails {
    @Id
    private int basicDetailsId;
    private String name;
    private String email;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn
    private AddressDetails address;
}
