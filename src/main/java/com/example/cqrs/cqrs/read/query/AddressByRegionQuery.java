package com.example.cqrs.cqrs.read.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressByRegionQuery {
    private String userId;
    private String state;
}