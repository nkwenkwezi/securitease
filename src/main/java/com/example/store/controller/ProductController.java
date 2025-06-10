package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@RequestBody Product product) {
        log.info("Creating product: {}", product);
        return productMapper.toDto(productRepo.save(product));
    }

    @GetMapping(name = "id")
    public List<ProductDTO> getAllProducts() {
        return productMapper.toDtoList(productRepo.findAll());
    }

    @GetMapping(params = "id")
    public ProductDTO getProduct(@RequestParam Long id) {
        return productMapper.toDto(productRepo.findById(id).orElse(new Product()));
    }
}
