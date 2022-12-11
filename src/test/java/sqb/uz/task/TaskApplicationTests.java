package sqb.uz.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.dto.ResponseDTO;
import sqb.uz.task.model.Product;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@SpringBootTest
@AutoConfigureMockMvc
class TaskApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("A")
	void addProduct() throws Exception {
		ObjectMapper jsonMapper = new ObjectMapper();

		Product product = new Product();
		product.setName("Appal");
		product.setDescription("Green sour apple");
		product.setPrice(8700D);
		product.setStatus(true);
		product.setAmount(35);
		product.setDateCreated(LocalDate.of(2022, 12, 11));

		String content = jsonMapper.writeValueAsString(product);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/product/add")
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

		ResponseDTO<ProductDTO> res = jsonMapper.readValue(response, new TypeReference<ResponseDTO<ProductDTO>>(){});

		assertNotNull(res);
		assertEquals(true, res.getSuccess());
		assertNotNull(res.getData());

	}

	@Test
	@DisplayName("B")
	void findProductById() throws Exception {

		ObjectMapper jsonMapper = new ObjectMapper();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/product/"+45)
				.contentType("application/json");

		String response = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertNotNull(response);

		ResponseDTO<ProductDTO> res = jsonMapper.readValue(response, new TypeReference<ResponseDTO<ProductDTO>>(){});

		assertNotNull(res);
		assertEquals(true, res.getSuccess());
		assertNotNull(res.getData());
	}

	@Test
	@DisplayName("C")
	void updateProduct() throws Exception{
		ObjectMapper jsonMapper = new ObjectMapper();

		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(45L);
		productDTO.setStatus(false);
		productDTO.setAmount(0);

		String content = jsonMapper.writeValueAsString(productDTO);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/product/update")
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

		ResponseDTO<ProductDTO> res = jsonMapper.readValue(response, new TypeReference<ResponseDTO<ProductDTO>>(){});

		assertNotNull(res);
		assertEquals(true, res.getSuccess());
		assertNotNull(res.getData());
	}

	@Test
	@DisplayName("D")
	void deleteProductById() throws Exception{
		ObjectMapper jsonMapper = new ObjectMapper();

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/product/delete/"+45)
				.contentType("application/json");

		String response = mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertNotNull(response);

		ResponseDTO<ProductDTO> res = jsonMapper.readValue(response, new TypeReference<ResponseDTO<ProductDTO>>(){});

		assertNotNull(res);
		assertEquals(true, res.getSuccess());
		assertNotNull(res.getData());
	}
}
