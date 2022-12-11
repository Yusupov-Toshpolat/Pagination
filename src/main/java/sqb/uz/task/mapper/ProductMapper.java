package sqb.uz.task.mapper;

import org.mapstruct.Mapper;
import sqb.uz.task.dto.ProductDTO;
import sqb.uz.task.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO productDTO);
}
