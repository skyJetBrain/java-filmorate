package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUserTest() throws Exception {
        String json = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"email@email.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    void createUserTestEmptyName() throws Exception {
        String json = "{\n" +
                "  \"login\": \"common\",\n" +
                "  \"name\": \"\",\n" +
                "  \"email\": \"friend@common.ru\",\n" +
                "  \"birthday\": \"2000-08-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

    @Test
    void createUserTestFailLogin() throws Exception {
        String json = "{\n" +
                "  \"login\": \"dolore ullamco\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"email.ru\",\n" +
                "  \"birthday\": \"2446-08-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void createUserTestFailEmail() throws Exception {
        String json = "{\n" +
                "  \"login\": \"dolore ullamco\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"email.ru\",\n" +
                "  \"birthday\": \"2446-08-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void createUserTestFailBirthday() throws Exception {
        String json = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"\",\n" +
                "  \"email\": \"email@email.ru\",\n" +
                "  \"birthday\": \"2446-08-20\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void updateUserTest() throws Exception {
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"login\": \"doloreUpdate\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void updateUserTestUnknown() throws Exception {
        String json = "{\n" +
                "  \"id\": -1,\n" +
                "  \"login\": \"doloreUpdate\",\n" +
                "  \"name\": \"est adipisicing\",\n" +
                "  \"email\": \"mail@yandex.ru\",\n" +
                "  \"birthday\": \"1976-09-20\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }



    @Test
    void geUsersList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }



}