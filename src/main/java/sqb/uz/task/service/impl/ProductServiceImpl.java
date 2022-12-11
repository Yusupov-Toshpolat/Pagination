package sqb.uz.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.dto.ResponseDTO;
import sqb.uz.task.helper.AppMessages;
import sqb.uz.task.mapper.ProductMapper;
import sqb.uz.task.model.Product;
import sqb.uz.task.repository.ProductRepository;
import sqb.uz.task.repository.impl.ProductRepositoryImpl;
import sqb.uz.task.repository.template.ProductJdbcTemplate;
import sqb.uz.task.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static sqb.uz.task.helper.AppCode.*;
import static sqb.uz.task.helper.AppCode.NOT_FOUND;
import static sqb.uz.task.helper.AppCode.OK;
import static sqb.uz.task.helper.AppMessages.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepositoryImpl repository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductJdbcTemplate jdbcTemplate;

    public ResponseDTO<List<Map<String, Object>>> byParam(Pagination pagination){
        try {
            return new ResponseDTO<>(OK, true, SUCCESS, jdbcTemplate.search(pagination));
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseDTO<ProductDTO> add(ProductDTO productDTO) {
        ProductDTO res = null;
        try {
            res = productMapper.toDto(
                    productRepository.save(
                            productMapper.toEntity(
                                    productDTO
                            )));
        }catch (Exception e){
            log.error(e.getMessage());
            log.error("Sho'tta");
            return ResponseDTO.<ProductDTO>builder()
                    .code(ERROR)
                    .success(false)
                    .message(NOT_SAVED)
                    .build();
        }
        return ResponseDTO.<ProductDTO>builder()
                .code(OK)
                .success(true)
                .message(SAVED)
                .data(res)
                .build();
    }

    @Override
    public ResponseDTO<ProductDTO> findById(Long id) {
        ProductDTO productDTO = null;
        try {
            productDTO = productMapper.toDto(
                    productRepository.findById(id).get()
            );
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDTO<>(NOT_FOUND, false, DATA_NOT_FOUND, null);
        }
        return new ResponseDTO<>(OK, true, SUCCESS, productDTO);
    }

    @Override
    public ResponseDTO<ProductDTO> update(ProductDTO productDTO) {
        if (productDTO.getId() == null)
            return new ResponseDTO<>(ERROR, false, ID_IS_NULL, null);
        Optional<Product> productOptional = productRepository.findById(productDTO.getId());
        if (productOptional.isEmpty())
            return new ResponseDTO<>(NOT_FOUND, false, DATA_NOT_FOUND, null);
        ProductDTO res = null;
        try {
            res = productMapper.toDto(
                    productRepository.save(
                            mapperForUpdate(productOptional.get(), productDTO)
                    )
            );
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDTO<>(ERROR, false, ERROR_THE_CHANGE, null);
        }
        return new ResponseDTO<>(OK, true, CHANGED, res);
    }

    @Override
    public ResponseDTO<ProductDTO> deleteById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty())
            return new ResponseDTO<>(NOT_FOUND, false, DATA_NOT_FOUND, null);
        try {
            productRepository.deleteById(id);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDTO<>(DATABASE_ERROR, false, ERROR_THE_DELETING, null);
        }
        return new ResponseDTO<>(OK, true, DELETE, productMapper.toDto(productOptional.get()));
    }

    @Override
    public ResponseDTO<Page<ProductDTO>> pagination(Pagination pagination) {
        try {
            Page<Product> data = repository.searchByParams(pagination);

            return new ResponseDTO<>(OK, true, SUCCESS, data.map( p -> productMapper.toDto(p)));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDTO<>(ERROR, false, SEARCH_ERROR, null);
        }
    }

//    @Override
//    public ResponseDTO<List<Product>> search() {
//        try {
//            return new ResponseDTO<>(OK, true, SUCCESS, repository.search());
//        }catch (Exception e){
//            log.error(e.getMessage());
//            return new ResponseDTO<>(ERROR, false, SEARCH_ERROR, null);
//        }
//    }

    private Product mapperForUpdate(Product old, ProductDTO newProduct){
        if (newProduct.getName() != null && !newProduct.getName().equals(old.getName()))
            old.setName(newProduct.getName());
        if (newProduct.getDescription() != null && !newProduct.getDescription().equals(old.getDescription()))
            old.setDescription(newProduct.getDescription());
        if (newProduct.getPrice() != null && old.getPrice() == null || Double.compare(old.getPrice(), newProduct.getPrice()) != 0)
            old.setPrice(newProduct.getPrice());
        if (newProduct.getStatus() != null && old.getStatus() != newProduct.getStatus())
            old.setStatus(newProduct.getStatus());
        if (newProduct.getDateCreated() != null && old.getDateCreated() == null || !old.getDateCreated().isEqual(newProduct.getDateCreated()))
            old.setDateCreated(newProduct.getDateCreated());
        if (newProduct.getAmount() != null && old.getAmount() == null || Integer.compare(old.getAmount(), newProduct.getAmount()) != 0)
            old.setAmount(newProduct.getAmount());

        return old;
    }
}