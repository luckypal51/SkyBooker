package com.skybooker.skybooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketSearch {
    private String origin;
    private String destination;
    private LocalDate date;
}
