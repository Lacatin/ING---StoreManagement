package com.example.INGStoreManagement.configuration;

import com.example.INGStoreManagement.dto.ProductDto;
import com.example.INGStoreManagement.util.ProductUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RBACTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void givenUserRole_whenPostPutDelete_thenForbidden() throws Exception {
        ProductDto productDto = ProductUtils.getRandomProductDtoWithoutId();
        String jsonBody = toJson(productDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminRole_whenPostPutDelete_thenAuthenticationSucceeds() throws Exception {
        ProductDto productDto = ProductUtils.getRandomProductDtoWithoutId();
        String jsonBody = toJson(productDto);

        mockMvc.perform(post("/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/product/" + UUID.randomUUID())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/product/" + UUID.randomUUID())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
