package com.example.Unit.Test;

import com.example.Unit.Test.controllers.UserController;
import com.example.Unit.Test.entities.PersonalUser;
import com.example.Unit.Test.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PersonalUserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void userControllerLoads() {
        assertThat(userController).isNotNull();
    }

    private @Nullable PersonalUser getUserFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/user/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        try {
            String userJSON = result.getResponse().getContentAsString();
            PersonalUser personalUser = objectMapper.readValue(userJSON, PersonalUser.class);

            assertThat(personalUser).isNotNull();
            assertThat(personalUser.getId()).isNotNull();
            return personalUser;

        } catch (Exception e) {
            return null;
        }
    }

    private MvcResult createAUserRequest(PersonalUser personalUser) throws Exception {
        if (personalUser == null) return null;
        String userJSON = objectMapper.writeValueAsString(personalUser);

        return this.mockMvc.perform(post("/user/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    private void createAUserRequest() throws Exception {
        PersonalUser personalUser = new PersonalUser();
        personalUser.setId(personalUser.getId());
        personalUser.setActive(true);
        personalUser.setName("Hala");
        personalUser.setSurname("Aniba");
        personalUser.setAge(38);

        createAUserRequest(personalUser);
    }

    private @NotNull PersonalUser createAUser(PersonalUser personalUser) throws Exception {
        MvcResult result = createAUserRequest(personalUser);
        PersonalUser personalUserFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), PersonalUser.class);

        assertThat(personalUserFromResponse).isNotNull();
        assertThat(personalUserFromResponse.getId()).isNotNull();

        return personalUserFromResponse;
    }

    @org.jetbrains.annotations.NotNull
    private PersonalUser createAUser() throws Exception {
        PersonalUser personalUser = new PersonalUser();
        personalUser.setId(personalUser.getId());
        personalUser.setActive(true);
        personalUser.setName("Hala");
        personalUser.setSurname("Aniba");
        personalUser.setAge(38);

        return createAUser(personalUser);
    }

    @Test
    void createAUserTest() throws Exception {

        PersonalUser personalUserFromResponse = createAUser();
    }

    @Test
    void readUsersList() throws Exception {
        createAUserRequest();

        MvcResult result = this.mockMvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<PersonalUser> usersFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        System.out.println("Users in database are: " + usersFromResponse.size());
        assertThat(usersFromResponse.size()).isNotZero();
    }

    @Test
    void readSingleUser() throws Exception {
        PersonalUser personalUser = createAUser();
        PersonalUser personalUserFromResponse = getUserFromId(personalUser.getId());
        assertThat(personalUserFromResponse.getId()).isEqualTo(personalUser.getId());

    }

    @Test
    void updateUser() throws Exception {
        PersonalUser personalUser = createAUser();

        String newName = "Marta";
        personalUser.setName(newName);
        String userJSON = objectMapper.writeValueAsString(personalUser);


        MvcResult resultOne = this.mockMvc.perform(put("/user/" + personalUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        PersonalUser userFromResponse = objectMapper.readValue(resultOne.getResponse().getContentAsString(), PersonalUser.class);

        assertThat(userFromResponse.getId()).isEqualTo(personalUser.getId());
        assertThat(userFromResponse.getName()).isEqualTo(newName);

        PersonalUser userFromResponseGet = getUserFromId(personalUser.getId());
        assertThat(userFromResponseGet.getId()).isEqualTo(personalUser.getId());
        assertThat(userFromResponseGet.getName()).isEqualTo(newName);
    }

    @Test
    void deleteUser() throws Exception {
        PersonalUser personalUser = createAUser();
        assertThat(personalUser.getId()).isNotNull();

        this.mockMvc.perform(delete("/user/" + personalUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        PersonalUser userFromResponseGet = getUserFromId(personalUser.getId());
        assertThat(userFromResponseGet).isNull();
    }

    @Test
    void activeUser() throws Exception {
        PersonalUser personalUser = createAUser();
        assertThat(personalUser.getId()).isNotNull();


        MvcResult result = this.mockMvc.perform(put("/user/" + personalUser.getId() + "/activation?activated=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        PersonalUser userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), PersonalUser.class);
        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isEqualTo(personalUser.getId());
        assertThat(userFromResponse.isActive()).isEqualTo(true);

        PersonalUser userFromResponseGet = getUserFromId(personalUser.getId());
        assertThat(userFromResponseGet).isNotNull();
        assertThat(userFromResponseGet.getId()).isEqualTo(personalUser.getId());
        assertThat(userFromResponseGet.isActive()).isEqualTo(true);
    }
}
