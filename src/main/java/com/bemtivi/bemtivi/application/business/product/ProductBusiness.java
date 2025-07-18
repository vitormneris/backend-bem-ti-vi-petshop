package com.bemtivi.bemtivi.application.business.product;

import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.product.Product;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.jpa.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.mappers.ProductPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.jpa.CategoryRepository;
import com.bemtivi.bemtivi.persistence.repositories.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductBusiness {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UploadBusiness uploadManager;
    private final ProductPersistenceMapper mapper;

    public PageResponse<Product> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                productRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Product findById(String id) {
        return mapper.mapToDomain(productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        ));
    }

    public Product insert(Product product, MultipartFile file) {
        ProductEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        try {
            product.setId(null);
            product.setRate(0d);
            product.setActivationStatus(activationStatus);
            product.getCategories().forEach(category -> categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
            ));
            ProductEntity productEntity = mapper.mapToEntity(product);
            productEntity.setPathImage(uploadManager.uploadObject(file));
            saved = productRepository.save(productEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Product update(String id, Product productNew, MultipartFile file) {
        ProductEntity productOld = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        productOld.setName(productNew.getName() == null ? productOld.getName() : productNew.getName());
        productOld.setPrice(productNew.getPrice() == null ? productOld.getPrice() : productNew.getPrice());
        productOld.setDescription(productNew.getDescription() == null ? productOld.getDescription() : productNew.getDescription());
        if (file != null) productOld.setPathImage(uploadManager.uploadObject(file));
        if (productNew.getCategories() != null) {
            productNew.getCategories().forEach(category -> categoryRepository.findById(category.getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0004)
            ));
            productOld.setCategories( mapper.mapToSetCategoryEntity(productNew.getCategories()) );
        }
        ProductEntity updated;
        try {
            updated = productRepository.save(productOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        product.getActivationStatus().setIsActive(false);
        product.getActivationStatus().setDeactivationDate(Instant.now());
        productRepository.save(product);
    }

    public void delete(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
        );
        productRepository.delete(product);
    }
}
