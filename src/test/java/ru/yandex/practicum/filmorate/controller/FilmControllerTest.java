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
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createFilmTest() throws Exception {
        String json = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"description\": \"adipisicing\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": \"100\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void createFilmTestFailName() throws Exception {
        String json = "{\n" +
                "  \"name\": \"\",\n" +
                "  \"description\": \"Description\",\n" +
                "  \"releaseDate\": \"1900-03-25\",\n" +
                "  \"duration\": \"200\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmTestFailDescription() throws Exception {
        String json = "{\n" +
                "  \"name\": \"Film name\",\n" +
                "  \"description\": \"Пятеро друзей ( комик-группа «Шарло»), " +
                "приезжают в город Бризуль. Здесь они хотят разыскать " +
                "господина Огюста Куглова, который задолжал им деньги," +
                "а именно 20 миллионов. о Куглов, который за время" +
                " «своего отсутствия», стал кандидатом Коломбани.\",\n" +
                "  \"releaseDate\": \"1900-03-25\",\n" +
                "  \"duration\": \"200\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmTestFailDuration() throws Exception {
        String json = "{\n" +
                "  \"name\": \"Name\",\n" +
                "  \"description\": \"Description\",\n" +
                "  \"releaseDate\": \"1980-03-25\",\n" +
                "  \"duration\": \"-200\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmTestFailReleaseDate() throws Exception {
        String json = "{\n" +
                "  \"name\": \"Name\",\n" +
                "  \"description\": \"Description\",\n" +
                "  \"releaseDate\": \"1890-03-25\",\n" +
                "  \"duration\": \"200\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
               .contentType(MediaType.APPLICATION_JSON)
               .content(json))
               .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void updateFilmTest() throws Exception {
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Film Updated\",\n" +
                "  \"description\": \"New film update description\",\n" +
                "  \"releaseDate\": \"1989-04-17\",\n" +
                "  \"rate\": 4,\n" +
                "  \"duration\": \"190\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void updateFilmTestUnknown() throws Exception {
        String json = "{\n" +
                "  \"id\": -1,\n" +
                "  \"name\": \"Film Updated\",\n" +
                "  \"description\": \"New film update description\",\n" +
                "  \"releaseDate\": \"1989-04-17\",\n" +
                "  \"rate\": 4,\n" +
                "  \"duration\": \"190\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void geFilmsList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}