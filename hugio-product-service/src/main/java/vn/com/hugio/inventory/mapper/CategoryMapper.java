package vn.com.hugio.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.com.hugio.inventory.dto.CategoryDto;
import vn.com.hugio.inventory.entity.Category;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public CategoryDto categoryEntityToDto(Category category) {
        return this.modelMapper.map(category, CategoryDto.class);
    }

}
