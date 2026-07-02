package com.modelos.usuarios.api;

import com.modelos.usuarios.adapters.inbound.http.controllers.HealthController;
import com.modelos.usuarios.application.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthControllerTest {

    @Test
    void shouldReturn503WhenUserServiceIsUnavailable() throws Exception {
        @SuppressWarnings("unchecked")
        ObjectProvider<UserService> userServiceProvider = mock(ObjectProvider.class);
        when(userServiceProvider.getIfAvailable()).thenReturn(null);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HealthController(userServiceProvider)).build();

        mockMvc.perform(get("/health/ready"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.detail").value("User service unavailable."));
    }
}
