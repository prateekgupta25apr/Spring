package com.prateekgupta.ticketing_system.WithoutMapping.dto;

import com.prateekgupta.ticketing_system.WithoutMapping.entity.TicketsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MappingDTO {
    private int ticketId;
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
