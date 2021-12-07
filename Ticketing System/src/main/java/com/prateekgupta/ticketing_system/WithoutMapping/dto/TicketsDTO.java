package com.prateekgupta.ticketing_system.WithoutMapping.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class TicketsDTO {
    private int id;
    private String productName;
    private int productModelNumber;
    private String productId;
    private String ticketTitle;
    private String ticketDescription;
    @JsonFormat(pattern ="dd/MM/yyyy")
    private LocalDate date;
    private String orderId;
    private String priority;
    private String paymentMode;
    private int userId;
}
