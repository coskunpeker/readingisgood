package com.getir.readingisgood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldPersistCustomer() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser@gmail.com")
                .password("testpassword").build()), status().isOk());
    }

    @Test
    public void shouldReturnError_invalidEMail() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testcustomer@")
                .password("testpassword").build()), status().isBadRequest());
    }

    @Test
    public void shouldReturnError_duplicateCustomerCreation() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser1@gmail.com")
                .password("testpassword").build()), status().isOk());

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser1@gmail.com")
                .password("testpassword").build()), status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnError_blankEMail() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .password("testpassword").build()), status().isBadRequest());
    }

    @Test
    public void shouldReturnError_blankPassword() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testcustomer@gmail.com").build()), status().isBadRequest());
    }

    @Test
    public void shouldReturnError_emailMaxLengthExceeded() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email(String.join("@", "testt".repeat(11), "gmail.com"))
                .password("testpassword").build()), status().isBadRequest());
    }

    @Test
    public void shouldReturnError_passwordMaxLengthExceeded() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testcustomer@gmail.com")
                .password(String.join("@", "testpassword".repeat(11), "gmail.com")).build()), status().isBadRequest());
    }

    @Test
    public void shouldReturnToken() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testcustomer@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("testcustomer@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(token).isNotNull();

    }

    @Test
    public void shouldReturnTokenError_usernameMismatch() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser2@gmail.com")
                .password("testpassword").build()), status().isOk());

        doRequest("/api/customers/login", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser4@gmail.com")
                .password("testpassword").build()), status().is4xxClientError());
    }


    @Test
    public void shouldReturnTokenError_passwordMismatch() throws Exception {
        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser3@gmail.com")
                .password("testpassword").build()), status().isOk());

        doRequest("/api/customers/login", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("testuser3@gmail.com")
                .password("testpassword1").build()), status().is4xxClientError());
    }

    private void doRequest(String urlTemplate, String content, ResultMatcher resultMatcher) throws Exception {
        mockMvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(resultMatcher);
    }

}
