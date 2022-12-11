package sqb.uz.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sqb.uz.task.dto.ResponseDTO;
import sqb.uz.task.dto.search.Between;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.search.Sorting;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void searchByParam() throws Exception{
        Pagination pagination = Pagination.builder()
                .tableName("product")
                .page(0)
                .size(20)
                .sorting(new Sorting("id", "asc"))
                .filters(List.of(
                        Filter.builder()
                                .fieldName("name")
                                .value("O")
                                .typeValue("string")
                                .isBetween(false)
                                .build(),
                        Filter.builder()
                                .fieldName("amount")
                                .typeValue("int")
                                .isBetween(true)
                                .between(Between.builder()
                                        .low("20")
                                        .high("60")
                                        .build())
                                .build()
                ))
                .build();

        ObjectMapper jsonMapper = new ObjectMapper();

        String content = jsonMapper.writeValueAsString(pagination);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/by-param")
                .contentType("application/json")
                .content(content);

        String response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        ResponseDTO<List<Map<String, Object>>> res = jsonMapper.readValue(
                response,
                new TypeReference<ResponseDTO<List<Map<String, Object>>>>(){}
        );

        assertNotNull(res);
        assertEquals(true, res.getSuccess());
        assertNotNull(res.getData());
    }
}
