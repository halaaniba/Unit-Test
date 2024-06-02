package com.example.Unit.Test.service;


import com.example.Unit.Test.entities.PersonalUser;
import com.example.Unit.Test.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public PersonalUser setUserActivationStatus(Long userId, boolean isActive) {
        Optional<PersonalUser> user = userRepository.findById(userId);
        if (!user.isPresent()) return null;
        user.get().setActive(isActive);
        return userRepository.save(user.get());
    }
}
