package vn.com.hugio.product.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.com.hugio.product.dto.CategoryDto;
import vn.com.hugio.product.entity.Category;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public CategoryDto categoryEntityToDto(Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }

}
