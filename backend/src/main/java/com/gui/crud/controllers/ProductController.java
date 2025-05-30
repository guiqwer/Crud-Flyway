package com.gui.crud.controllers;

import com.gui.crud.dtos.ProductDto;
import com.gui.crud.model.Product;
import com.gui.crud.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired //instanciar objeto fazendo todas as injeções de depedências
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products); //mostrar os produtos
    }

    @GetMapping("/{id}") //
    public ResponseEntity getProductById(@PathVariable(value = "id") long id) {
        Optional<Product> productOptional = productRepository.findById(id); // Usar Optional para evitar
        // NulLPointerException ou seja posso filtrar depois (com um if) pra caso o Optinal venha sem ou com o Produto.
        if(productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(productOptional.get());
    }

    @PostMapping
    public ResponseEntity saveProduct(@RequestBody ProductDto productDto) {
        var Product = new Product();
        BeanUtils.copyProperties(productDto, Product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(Product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductById(@PathVariable(value = "id") Long id) {
        Optional <Product> productOptional = productRepository.findById(id);

        if(productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        productRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
    }


    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional <Product>  productOptional = productRepository.findById(id);

        if(productOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        var Product = productOptional.get();
        BeanUtils.copyProperties(productDto, Product);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(Product));
    }
}
