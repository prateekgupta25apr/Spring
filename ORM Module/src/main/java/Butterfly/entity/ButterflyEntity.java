package Butterfly.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "butterfly_details")
public class ButterflyEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "speciesName")
    private String speciesName;
    @Column(name = "wingSize")
    private int wingSize;
    @Column(name = "wingColor")
    private String wingColor;
    @Column(name = "age")
    private int age;
    @Column(name = "origin")
    private String origin;
    @Column(name = "antennaeSize")
    private int antennaeSize;
}
