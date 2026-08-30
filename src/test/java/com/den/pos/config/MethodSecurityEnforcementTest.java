package com.den.pos.config;

import com.den.pos.controller.ProductController;
import com.den.pos.security.CustomUserDetailsService;
import com.den.pos.security.JwtService;
import com.den.pos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "cors.allowed-origins=http://localhost",
        "cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS"
})
class MethodSecurityEnforcementTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(authorities = "products:read")
    void allowsAccessWhenAuthorityIsPresent() throws Exception {
        when(productService.getById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(get("/products/{id}", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "products:something-else")
    void deniesAccessWhenAuthorityIsMissing() throws Exception {
        mockMvc.perform(get("/products/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }
}
