package com.example.siteDiscoBackend.Controllers;


import com.example.siteDiscoBackend.Product.Product;
import com.example.siteDiscoBackend.Product.ProductRepository;
import com.example.siteDiscoBackend.Product.ProductRequestDTO;
import com.example.siteDiscoBackend.Product.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody ProductRequestDTO data){
        Product productData = new Product(data);
        repository.save(productData);

        return ResponseEntity.status(HttpStatus.CREATED).body("Product Saved Sucessfully!!");
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        List<ProductResponseDTO> list = repository.findAll().stream().map(ProductResponseDTO::new).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(value="id") UUID id){
        Optional<Product> productFound = repository.findById(id);

        if(productFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(productFound);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value="id") UUID id,
                                              @RequestBody @Valid ProductRequestDTO productRequest){
        Optional<Product> productFound = repository.findById(id);

        if(productFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
        }

        var product = productFound.get();
        BeanUtils.copyProperties(productRequest, product);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(product));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id){
        Optional<Product> productFound = repository.findById(id);

        if(productFound.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
        }

        repository.delete(productFound.get());

        return ResponseEntity.status(HttpStatus.OK).body("Product Deleted Sucessfully!");
    }
}