package com.example.cqrs.cqrs.write.command;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateUserCommand {
    private String userId;
    private String firstName;
    private String lastName;
}