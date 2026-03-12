package br.com.arthivia.EasyPdvBe.controller;

import br.com.arthivia.EasyPdvBe.model.SuccessResponse;
import br.com.arthivia.EasyPdvBe.model.dtos.CategoryDto;
import br.com.arthivia.EasyPdvBe.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/insert")
    public ResponseEntity<SuccessResponse> insertCategory(@RequestParam @Valid String name) {
        var result = categoryService.insertCategory(name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @PostMapping("/update")
    public ResponseEntity<SuccessResponse> updateCategory(@RequestParam @Valid Integer id, @RequestParam String name) {
        var result = categoryService.updateCategory(id, name);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable @Valid Integer id) {
        var result =  categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
