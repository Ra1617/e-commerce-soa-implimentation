package com.example.order;

import com.example.order.controller.OrderController;
import com.example.order.exception.ErrorHandler;
import com.example.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Verifies invalid card input is rejected before orchestration and is not echoed. */
@WebMvcTest(controllers = OrderController.class)
class OrderValidationTest {
    @Autowired MockMvc mvc;
    @MockBean OrderService service;

    @Test
    void invalidCardReturnsStructuredBadRequestWithoutCardNumber() throws Exception {
        mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(
                        "{\"customerName\":\"Asha\",\"items\":[{\"productId\":1,\"quantity\":1}],\"payment\":{\"method\":\"CARD\",\"cardNumber\":\"1234\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("payment.cardNumber: must be exactly 16 digits")))
                .andExpect(jsonPath("$.message", not(containsString("1234"))));
    }
}
