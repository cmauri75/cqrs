package com.example.cqrs.cqrs.write.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Contact {
    private String type;
    private String detail;
}