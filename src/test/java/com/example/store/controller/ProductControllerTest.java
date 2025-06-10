package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepo;

    @MockitoBean
    private ProductMapper productMapper;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setDescription("Sample Product");

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Sample Product");
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productRepo.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        String productJson =
                """
            {
                "id": 1,
                "description": "Sample Product"
            }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Sample Product"));

        verify(productRepo, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(any(Product.class));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productRepo.findAll()).thenReturn(Arrays.asList(product));
        when(productMapper.toDtoList(anyList())).thenReturn(Arrays.asList(productDTO));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Sample Product"));

        verify(productRepo, times(1)).findAll();
        verify(productMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testGetProductById() throws Exception {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDTO);

        mockMvc.perform(get("/products?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Sample Product"));

        verify(productRepo, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(any(Product.class));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productRepo.findById(999L)).thenReturn(Optional.empty());
        when(productMapper.toDto(any(Product.class))).thenReturn(new ProductDTO());

        mockMvc.perform(get("/products?id=999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(productRepo, times(1)).findById(999L);
        verify(productMapper, times(1)).toDto(any(Product.class));
    }
}
