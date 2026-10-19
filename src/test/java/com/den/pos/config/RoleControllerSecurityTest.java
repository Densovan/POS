package com.den.pos.config;

import com.den.pos.controller.RoleController;
import com.den.pos.security.CustomUserDetailsService;
import com.den.pos.security.JwtService;
import com.den.pos.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "cors.allowed-origins=http://localhost",
        "cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS"
})
class RoleControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(authorities = "roles:read")
    void allowsAccessWhenAuthorityIsPresent() throws Exception {
        when(roleService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "roles:something-else")
    void deniesAccessWhenAuthorityIsMissing() throws Exception {
        mockMvc.perform(get("/roles"))
                .andExpect(status().isForbidden());
    }
}
