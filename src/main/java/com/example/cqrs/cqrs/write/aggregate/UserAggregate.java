package com.example.cqrs.cqrs.write.aggregate;

import com.example.cqrs.cqrs.write.command.CreateUserCommand;
import com.example.cqrs.cqrs.write.command.UpdateUserCommand;
import com.example.cqrs.crud.model.User;
import com.example.cqrs.cqrs.write.repository.UserWriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserAggregate {
    private UserWriteRepository writeRepository;

    public User handleCreateUserCommand(CreateUserCommand command) {
        User user = User.builder().userid(command.getUserId()).firstName(command.getFirstName()).lastName(command.getLastName()).build();
        writeRepository.addUser(user.getUserid(), user);
        return user;
    }

    public User handleUpdateUserCommand(UpdateUserCommand command) {
        User user = writeRepository.getUser(command.getUserId());
        user.setAddresses(command.getAddresses());
        user.setContacts(command.getContacts());
        writeRepository.addUser(user.getUserid(), user);
        return user;
    }
}