package com.example.product;

import com.example.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerMockMvcTest {
    @Autowired MockMvc mvc;
    @Autowired ProductRepository repository;

    @BeforeEach
    void resetStock() {
        repository.findById(1L).ifPresent(p -> { p.setStock(100); repository.save(p); });
    }

    @Test
    void listsSeededProducts() throws Exception {
        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name", is("Mouse")));
    }

    @Test
    void reservesAndReleasesStock() throws Exception {
        mvc.perform(post("/products/1/reserve").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":3}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.stock", is(97)));
        mvc.perform(post("/products/1/release").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":3}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.stock", is(100)));
    }

    @Test
    void rejectsInvalidQuantityAndInsufficientStock() throws Exception {
        mvc.perform(post("/products/1/reserve").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":0}"))
                .andExpect(status().isBadRequest());
        repository.findById(1L).ifPresent(p -> { p.setStock(1); repository.save(p); });
        mvc.perform(post("/products/1/reserve").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":2}"))
                .andExpect(status().isConflict());
    }

    @Test
    void returnsStructuredValidationError() throws Exception {
        mvc.perform(post("/products/1/reserve").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("quantity")));
    }

    @Test
    void healthIsUp() throws Exception {
        mvc.perform(get("/health")).andExpect(status().isOk())
                .andExpect(jsonPath("$.service", is("product")))
                .andExpect(jsonPath("$.status", is("UP")));
    }
}
