package com.money.manage.controller;

import com.money.manage.dto.CategoryDTO;
import com.money.manage.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){

        try {

            CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {

        List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUser();

        return ResponseEntity.ok(categories);

    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTypeForCurrentUser(@PathVariable String type){

        try{
            List<CategoryDTO>  list = categoryService.getCategoriesByTypeForCurrentUser(type);

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable String categoryId ,@RequestBody CategoryDTO categoryDTO) {

        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.ok(updatedCategory);

    }
}
