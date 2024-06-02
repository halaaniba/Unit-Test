package com.example.Unit.Test;

import com.example.Unit.Test.entities.PersonalUser;
import com.example.Unit.Test.repositories.UserRepository;
import com.example.Unit.Test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonalUserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkUserActivating() throws Exception {
        PersonalUser personalUser = new PersonalUser();
        personalUser.setId(personalUser.getId());
        personalUser.setActive(true);
        personalUser.setName("Hala");
        personalUser.setSurname("Aniba");
        personalUser.setAge(23);

        PersonalUser userFromDB = userRepository.save(personalUser);
        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getId()).isNotNull();

        PersonalUser userFromService = userService.setUserActivationStatus(personalUser.getId(), true);
        assertThat(userFromService).isNotNull();
        assertThat(userFromService.getId()).isNotNull();
        assertThat(userFromService.isActive()).isTrue();

        PersonalUser userFromFind = userRepository.findById(userFromDB.getId()).get();
        assertThat(userFromFind).isNotNull();
        assertThat(userFromFind.getId()).isNotNull();
        assertThat(userFromFind.getId()).isEqualTo(userFromDB.getId());
        assertThat(userFromService.isActive()).isTrue();

    }
}
