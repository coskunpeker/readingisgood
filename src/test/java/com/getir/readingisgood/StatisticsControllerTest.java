package com.getir.readingisgood;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.dto.BookDTO;
import com.getir.readingisgood.dto.CustomerDTO;
import com.getir.readingisgood.dto.OrderDTO;
import com.getir.readingisgood.dto.StatisticsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldQueryMonthlyStatistics() throws Exception {

        doRequest("/api/customers", objectMapper.writeValueAsString(CustomerDTO.builder()
                .email("teststatistics@gmail.com")
                .password("testpassword").build()), status().isOk());

        String token = mockMvc.perform(post("/api/customers/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(CustomerDTO.builder()
                                .email("teststatistics@gmail.com")
                                .password("testpassword")
                                .build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        prepareDatabase(token);

        String result = mockMvc.perform(get("/api/statistics/monthly")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<StatisticsDTO> statisticsDTOList = objectMapper.readValue(result, new TypeReference<List<StatisticsDTO>>() {
        });

        assertThat(statisticsDTOList.size()).isEqualTo(1);

    }

    public void prepareDatabase(String token) throws Exception {

        doRequestWithToken("/api/books", objectMapper.writeValueAsString(BookDTO.builder()
                .isbn("4")
                .name("Testbook")
                .unitPrice(BigDecimal.valueOf(20.2))
                .stockCount(80)
                .build()), status().isOk(), token);


        OrderDTO firstOrder = OrderDTO.builder()
                .customerId(3L)
                .bookId(1L)
                .price(BigDecimal.valueOf(20.2))
                .quantity(3)
                .build();

        OrderDTO secondOrder = OrderDTO.builder()
                .customerId(3L)
                .bookId(1L)
                .price(BigDecimal.valueOf(20.2))
                .quantity(3)
                .build();

        OrderDTO thirdOrder = OrderDTO.builder()
                .customerId(3L)
                .bookId(1L)
                .price(BigDecimal.valueOf(20.2))
                .quantity(3)
                .build();

        Stream.of(firstOrder, secondOrder, thirdOrder)
                .forEach(order -> {
                    try {
                        doRequestWithToken("/api/orders", objectMapper.writeValueAsString(order), status().isOk(), token);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });


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


