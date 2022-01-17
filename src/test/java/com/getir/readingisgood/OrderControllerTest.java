package com.getir.readingisgood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPersistOrder() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testorder@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("testorder@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        doRequestWithToken("/api/books", objectMapper.writeValueAsString(BookDTO.builder()
                .id(4L)
                .isbn("5")
                .name("Testbook")
                .unitPrice(BigDecimal.valueOf(20.2))
                .stockCount(80)
                .build()), status().isOk(), token);

        doRequestWithToken("/api/orders", objectMapper.writeValueAsString(OrderDTO.builder()
                .customerId(3L)
                .bookId(4L)
                .price(BigDecimal.valueOf(20.2))
                .quantity(3)
                .build()), status().isOk(), token);

    }

    @Test
    public void shouldReturnError_invalidTokenDuringOrderPersist() throws Exception {

        doRequestWithToken("/api/orders", objectMapper.writeValueAsString(OrderDTO.builder()
                .customerId(3L)
                .bookId(1L)
                .price(BigDecimal.valueOf(20.2))
                .quantity(3)
                .build()), status().is4xxClientError(), "invalidToken");

    }

    private void doRequest(String urlTemplate, String content, ResultMatcher resultMatcher) throws Exception {
        mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }

    private void doRequestWithToken(String urlTemplate, String content, ResultMatcher resultMatcher, String token) throws Exception {
        mockMvc.perform(post(urlTemplate)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }


}
