package com.modelos.usuarios.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UsersApiIntegrationTest {

    @Autowired
        private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = webAppContextSetup(webApplicationContext).build();
        }

    @Test
    void shouldReportLivenessAndReadiness() throws Exception {
        mockMvc.perform(get("/health/live"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("alive"));

        mockMvc.perform(get("/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ready"))
                .andExpect(jsonPath("$.persistence").value("in-memory"));
    }

    @Test
    void shouldExecuteCrudFlow() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "nome": "Carlos",
                                  "dtNascimento": "1992-03-14",
                                  "status": true,
                                  "telefones": ["11911112222", "1122223333"]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Carlos"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Carlos"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos"));

        mockMvc.perform(put("/usuarios/1")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 200,
                                  "nome": "Carlos Silva",
                                  "dtNascimento": "1992-03-14",
                                  "status": false,
                                  "telefones": ["11900001111"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Carlos Silva"))
                .andExpect(jsonPath("$.status").value(false));

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409ForDuplicateId() throws Exception {
        String payload = """
                {
                  "id": 1,
                  "nome": "Patricia",
                  "dtNascimento": "1995-08-10",
                  "status": true,
                  "telefones": ["11977776666"]
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Usuario com id 1 ja existe."));
    }

    @Test
    void shouldReturn404ForUnknownUser() throws Exception {
        String payload = """
                {
                  "id": 999,
                  "nome": "Inexistente",
                  "dtNascimento": "2000-01-01",
                  "status": false,
                  "telefones": []
                }
                """;

        mockMvc.perform(get("/usuarios/999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/usuarios/999")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/usuarios/999"))
                .andExpect(status().isNotFound());
    }
}
