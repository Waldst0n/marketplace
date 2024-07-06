package com.waldstonsantana.marketplace.services;

import com.waldstonsantana.marketplace.dtos.ProductDTO;
import com.waldstonsantana.marketplace.models.Product;
import com.waldstonsantana.marketplace.repositories.ProductRepository;
import com.waldstonsantana.marketplace.services.exceptions.DataBaseException;
import com.waldstonsantana.marketplace.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new ProductDTO(product);

    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String name, Pageable pageable) {
        Page<Product> products = repository.searchByName(name,pageable);

        return  products.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();

        copyDtoToEntity(dto,entity);

        entity = repository.save(entity);

        return  new ProductDTO(entity);

    }

    @Transactional
    public ProductDTO update(Long id,  ProductDTO dto) {
       try {

        Product entity = repository.getReferenceById(id);
        copyDtoToEntity(dto,entity);

        entity = repository.save(entity);

        return  new ProductDTO(entity);
       }
       catch (EntityNotFoundException e) {
           throw  new ResourceNotFoundException("Recurso não encontrado");
       }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw  new ResourceNotFoundException("Recurso não encontrado!");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw  new DataBaseException("Falha de integridade referencial");
        }

        repository.deleteById(id);

    }




    private void copyDtoToEntity( ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
    }



}
