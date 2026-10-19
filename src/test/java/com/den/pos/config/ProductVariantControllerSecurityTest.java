package com.den.pos.config;

import com.den.pos.controller.ProductVariantController;
import com.den.pos.security.CustomUserDetailsService;
import com.den.pos.security.JwtService;
import com.den.pos.service.ProductVariantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductVariantController.class)
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "cors.allowed-origins=http://localhost",
        "cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS"
})
class ProductVariantControllerSecurityTest {

    private static final UUID PRODUCT_ID = UUID.fromString("00000000-0000-7000-8000-000000000001");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductVariantService variantService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(authorities = "products:read")
    void allowsListingWhenReadAuthorityIsPresent() throws Exception {
        when(variantService.findByProduct(PRODUCT_ID)).thenReturn(List.of());

        mockMvc.perform(get("/products/{productId}/variants", PRODUCT_ID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "categories:read")
    void deniesListingWhenReadAuthorityIsMissing() throws Exception {
        mockMvc.perform(get("/products/{productId}/variants", PRODUCT_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "products:read")
    void deniesCreateWhenCreateAuthorityIsMissing() throws Exception {
        mockMvc.perform(post("/products/{productId}/variants", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sku":"TEE-CLSC-S-BLK","attributes":{"size":"S"}}
                                """))
                .andExpect(status().isForbidden());
    }
}
