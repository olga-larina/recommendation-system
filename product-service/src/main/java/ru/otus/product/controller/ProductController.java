package ru.otus.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.common.dto.ProductDto;
import ru.otus.product.service.ProductService;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // .....product?productCode=12345&productCode=67890
    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(required = false) List<String> productCode) {
        if (productCode == null || productCode.isEmpty()) {
            return ResponseEntity.ok(productService.getProducts());
        }
        return ResponseEntity.ok(productService.getProducts(productCode));
    }

}