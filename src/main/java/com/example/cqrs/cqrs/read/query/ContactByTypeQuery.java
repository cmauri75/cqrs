package com.example.cqrs.cqrs.read.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactByTypeQuery {
    private String userId;
    private String contactType;
}