package br.com.arthivia.EasyPdvBe.controller;

import br.com.arthivia.EasyPdvBe.model.dtos.CategoryDto;
import br.com.arthivia.EasyPdvBe.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/insert")
    public String insertCategory(@RequestParam String name) {
        return categoryService.insertCategory(name);
    }

    @PostMapping("/update")
    public String updateCategory(@RequestParam Integer id, @RequestParam String name) {
        return categoryService.updateCategory(id, name);
    }

    @DeleteMapping("/delete/")
    public String deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
