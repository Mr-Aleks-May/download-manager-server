package com.mralexmay.projects.download_manager.server.api.user.converter.mapper;

import com.mralexmay.projects.download_manager.server.api.user.model.Category;
import com.mralexmay.projects.download_manager.server.api.user.converter.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDto fromEntityToDto(Category category) {
        return new CategoryDto()
                .setCategoryCSID(category.getCategoryCSID())
                .setName(category.getName())
                .setPluginPSID(category.getPluginPSID());
    }

    public Category fromDtoToEntity(CategoryDto categoryDto) {
        return new Category()
                .setCategoryCSID(categoryDto.getCategoryCSID())
                .setName(categoryDto.getName())
                .setPluginPSID(categoryDto.getPluginPSID());
    }
}
