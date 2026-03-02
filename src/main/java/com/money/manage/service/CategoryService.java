package com.money.manage.service;

import com.money.manage.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getCategoriesForCurrentUser();

    List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type);

    CategoryDTO updateCategory(CategoryDTO dto, String categoryId);

}
