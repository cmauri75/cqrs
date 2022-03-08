package com.example.cqrs.crud.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private String type;
    private String detail;
}