package com.example.product;

import com.example.product.dto.ProductRequest;
import com.example.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @Autowired
    private ProductService productService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

    }
    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest=getProductRequest();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
        ).andExpect(status().isCreated());
        Assertions.assertEquals(1, productService.gellAllProducts().size());
    }

    @Test
    void shouldGetAllProducts() throws Exception {
       MvcResult result= mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/product"))
        .andReturn();
        result.getResponse().getContentAsString().contentEquals(objectMapper.writeValueAsString(getProductRequest()));
        Assertions.assertEquals(1, productService.gellAllProducts().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder().name("IPhone 13").description("IPhone 13").price(12600).build();
    }

}
