package sqb.uz.task.service;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.dto.ResponseDTO;
import sqb.uz.task.model.Product;

import java.util.List;

public interface ProductService {
    ResponseDTO<ProductDTO> add(ProductDTO productDTO);
    ResponseDTO<ProductDTO> findById(Long id);
    ResponseDTO<ProductDTO> update(ProductDTO productDTO);
    ResponseDTO<ProductDTO> deleteById(Long id);
    ResponseDTO<Page<ProductDTO>> pagination(Pagination pagination);
//    ResponseDTO<List<Product>> search();
}
