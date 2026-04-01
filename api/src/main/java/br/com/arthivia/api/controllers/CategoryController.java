package br.com.arthivia.api.controllers;

import br.com.arthivia.api.models.dtos.CategoryDto;
import br.com.arthivia.api.services.CategoryService;
import br.com.arthivia.api.util.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createCategory(@RequestParam @Valid String name) {
        var result = categoryService.createCategory(name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @PutMapping("/update")
    public ResponseEntity<SuccessResponse> updateCategory(@RequestParam @Valid Long id, @RequestParam @Valid String name) {
        var result = categoryService.updateCategory(id, name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<SuccessResponse> deleteCategory(@RequestParam @Valid Long id) {
        var result = categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        var result = categoryService.getAllCategories();
        return ResponseEntity.ok(result);
    }
}
