package com.skybooker.skybooker.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestFlightDto {
    private long id;
    private int seats;
}
