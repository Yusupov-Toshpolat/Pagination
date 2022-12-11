package sqb.uz.task.rest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sqb.uz.task.dto.search.Filter;
import sqb.uz.task.dto.search.Pagination;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.dto.ResponseDTO;
import sqb.uz.task.model.Product;
import sqb.uz.task.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/add")
    public ResponseDTO<ProductDTO> add(@RequestBody ProductDTO productDTO){
        return productService.add(productDTO);
    }

    @GetMapping("/{id}")
    public ResponseDTO<ProductDTO> findById(@PathVariable Long id){
        return productService.findById(id);
    }

    @PatchMapping("/update")
    public ResponseDTO<ProductDTO> update(@RequestBody ProductDTO productDTO){
        return productService.update(productDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDTO<ProductDTO> deleteById(@PathVariable Long id){
        return productService.deleteById(id);
    }

    @GetMapping("/by-param")
    public ResponseDTO<List<Map<String, Object>>> byParam(@RequestBody Pagination pagination){
        return productService.byParam(pagination);
    }
}
