package com.prateekgupta.ticketing_system.WithMapping.entity;

import com.prateekgupta.ticketing_system.WithoutMapping.entity.TicketsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@ToString
public class TicketsEntityMapping {
    public enum Priority{LOW,MEDIUM,HIGH}
    public enum PaymentMode{ONLINE,CARD,CASH}
    @Id
    @GenericGenerator(name = "autoincrement",strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    private int id;
    private String productName;
    private int productModelNumber;
    private String productId;
    private String ticketTitle;
    private String ticketDescription;
    private LocalDate date;
    private String orderId;
    private TicketsEntity.Priority priority;
    private String paymentMode;
    @ManyToOne(cascade = CascadeType.ALL)
    private UsersEntityMapping user;
}
