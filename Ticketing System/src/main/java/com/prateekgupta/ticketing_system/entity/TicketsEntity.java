package com.prateekgupta.ticketing_system.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@ToString
public class TicketsEntity {
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
    private Priority priority;
    private String paymentMode;
    private int userId;
}
