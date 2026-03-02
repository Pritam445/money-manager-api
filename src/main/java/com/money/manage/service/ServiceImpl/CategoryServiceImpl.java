package com.money.manage.service.ServiceImpl;

import com.money.manage.dto.CategoryDTO;
import com.money.manage.entity.CategoryEntity;
import com.money.manage.entity.ProfileEntity;
import com.money.manage.repository.CategoryRepository;
import com.money.manage.service.CategoryService;
import com.money.manage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile) {

        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .type(categoryDTO.getType())
                .profile(profile)
                .build();
    }

    private CategoryDTO toDto(CategoryEntity entity){

        return  CategoryDTO.builder()
                .id(entity.getId())
                .profileId(entity.getProfile().getId())
                .name(entity.getName())
                .type(entity.getType())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(),profile.getId())){
            throw new RuntimeException("Category with this name already exists");

        }

        CategoryEntity newCategory = toEntity(categoryDTO,profile);
        newCategory = categoryRepository.save(newCategory);

        return toDto(newCategory);

    }

    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());

        return categories.stream().map(this::toDto).toList();
    }

    @Override
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {

        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type, profile.getId());

        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO dto, String categoryId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));

        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory.setIcon(dto.getType());
        existingCategory = categoryRepository.save(existingCategory);

        return toDto(existingCategory);
    }


}
