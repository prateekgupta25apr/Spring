package com.prateekgupta.ticketing_system.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
@Getter
@Setter
@ToString
public class MappingEntity {
    @Id
    @GenericGenerator(name = "autoincrement",strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private long contactNumber;
    private String ticketTitle;
    private String ticketDescription;
    private TicketsEntity.Priority priority;
    private int userId;
}
