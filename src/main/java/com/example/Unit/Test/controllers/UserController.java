package com.example.Unit.Test.controllers;
import com.example.Unit.Test.entities.PersonalUser;
import com.example.Unit.Test.repositories.UserRepository;
import com.example.Unit.Test.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public @ResponseBody PersonalUser create(@RequestBody PersonalUser personalUser) {
        return userRepository.save(personalUser);
    }

    @GetMapping("/")
    public @ResponseBody List<PersonalUser> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody PersonalUser getSingleUserById(@PathVariable Long id) {
        Optional<PersonalUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public @ResponseBody PersonalUser update(@PathVariable Long id, @RequestBody @NonNull PersonalUser personalUser) {
        personalUser.setId(id);
        personalUser.setName(personalUser.getName());
        personalUser.setSurname(personalUser.getSurname());
        personalUser.setAge(personalUser.getAge());
        return userRepository.save(personalUser);
    }

    @PutMapping("/{id}/activation")
    public @ResponseBody PersonalUser setUserActive(@PathVariable Long id, @RequestParam("activated") boolean activated) {
        return userService.setUserActivationStatus(id, activated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}
