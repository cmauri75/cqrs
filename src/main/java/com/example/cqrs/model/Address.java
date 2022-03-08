package com.example.cqrs.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    private String city;
    private String state;
    private String postcode;
}