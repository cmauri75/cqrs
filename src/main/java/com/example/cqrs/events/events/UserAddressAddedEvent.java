package com.example.cqrs.events.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAddressAddedEvent extends Event {
    private String city;
    private String state;
    private String postCode;
}