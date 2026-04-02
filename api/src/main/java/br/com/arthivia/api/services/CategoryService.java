package br.com.arthivia.api.services;

import br.com.arthivia.api.models.dtos.CategoryDto;
import br.com.arthivia.api.models.entities.CategoryEntity;
import br.com.arthivia.api.repositories.CategoryRepository;
import br.com.arthivia.api.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public String createCategory(String name) {
        categoryRepository.findByName(name).ifPresent(c -> {
                throw new RuntimeException("The category with name " + c.getName() + "' already exists. if you not found contact the support.");
        });

        var category = new CategoryEntity();
        category.setName(Util.normalizeText(name));
        category.setActive(true);
        categoryRepository.save(category);
        return "Category created successfully.";
    }

    public String updateCategory(UUID id, String name) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id '" + id + "' not found."));

        category.setName(Util.normalizeText(name));
        categoryRepository.save(category);
        return "Category updated successfully.";
    }

    public String deleteCategory(UUID id) {
        categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category with id '" + id + "' not found."));
        categoryRepository.disableCategory(id);
        return "Category deleted successfully.";
    }

    public List<CategoryDto> getAllCategories() {
        var categories = categoryRepository.findAllByActiveTrue();
        return categories.stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .toList();
    }
}
