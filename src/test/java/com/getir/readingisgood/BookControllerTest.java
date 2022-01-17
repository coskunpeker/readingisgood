package com.getir.readingisgood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.StockDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPersistBook() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testbook@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("testbook@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        doPostRequestWithToken("/api/books", objectMapper.writeValueAsString(BookDTO.builder()
                .isbn("1")
                .name("Testbook")
                .unitPrice(BigDecimal.valueOf(20.2))
                .stockCount(80)
                .build()), status().isOk(), token);

    }


    @Test
    public void shouldUpdateBookStock() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("teststockupdate@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("teststockupdate@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        doPostRequestWithToken("/api/books", objectMapper.writeValueAsString(BookDTO.builder()
                .id(3L)
                .isbn("3")
                .name("Testbook")
                .unitPrice(BigDecimal.valueOf(20.2))
                .stockCount(80)
                .build()), status().isOk(), token);

        doPatchRequestWithToken("/api/books", objectMapper.writeValueAsString(StockDTO.builder()
                .bookId(3L)
                .stockCount(50)
                .build()), status().isOk(), token);

    }

    @Test
    public void shouldReturnError_negativeStock() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("teststocknegativestockupdate@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("teststocknegativestockupdate@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        doPostRequestWithToken("/api/books", objectMapper.writeValueAsString(BookDTO.builder()
                .isbn("2")
                .name("Testbook")
                .unitPrice(BigDecimal.valueOf(20.2))
                .stockCount(80)
                .build()), status().isOk(), token);

        doPatchRequestWithToken("/api/books", objectMapper.writeValueAsString(StockDTO.builder()
                .bookId(1L)
                .stockCount(-100)
                .build()), status().isUnprocessableEntity(), token);

    }

    private void doRequest(String urlTemplate, String content, ResultMatcher resultMatcher) throws Exception {
        mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }

    private void doPostRequestWithToken(String urlTemplate, String content, ResultMatcher resultMatcher, String token) throws Exception {
        mockMvc.perform(post(urlTemplate)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }

    private void doPatchRequestWithToken(String urlTemplate, String content, ResultMatcher resultMatcher, String token) throws Exception {
        mockMvc.perform(patch(urlTemplate)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }

}
