package org.prateekgupta.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@javax.persistence.Entity
@Getter
@Setter
@ToString
@Table
public class MobileEntity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    private int id;
    private String brandName;
    private int modelNumber;
    private String modelName;
    private String type;
    private int ram;
    private int rom;
    private int price;
    private String availability;
}
