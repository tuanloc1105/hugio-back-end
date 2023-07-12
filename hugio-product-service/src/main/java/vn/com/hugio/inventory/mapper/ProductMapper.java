package vn.com.hugio.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.com.hugio.inventory.dto.CategoryDto;
import vn.com.hugio.inventory.dto.ProductDetailDto;
import vn.com.hugio.inventory.dto.ProductDto;
import vn.com.hugio.inventory.entity.Product;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductDto productEntityToProductDto(Product product) {
        ProductDto dto = this.modelMapper.map(product, ProductDto.class);

        List<CategoryDto> categoryDtos = product.getProductCategories()
                .stream()
                .map(category -> modelMapper.map(category.getCategory(), CategoryDto.class))
                .toList();
        dto.setCategoryDtos(categoryDtos);

        List<ProductDetailDto> productDetailDtos = product.getProductDetails()
                .stream()
                .map(productDetail -> modelMapper.map(productDetail, ProductDetailDto.class))
                .toList();
        dto.setProductDetailDtos(productDetailDtos);

        return dto;
    }
}