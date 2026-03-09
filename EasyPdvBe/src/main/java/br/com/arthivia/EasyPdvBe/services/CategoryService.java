package br.com.arthivia.EasyPdvBe.services;

import br.com.arthivia.EasyPdvBe.model.dtos.CategoryDto;
import br.com.arthivia.EasyPdvBe.model.entities.CategoryEntity;
import br.com.arthivia.EasyPdvBe.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public String insertCategory(String name) {
        categoryRepository.findByName(name).ifPresent(category -> {
            throw new RuntimeException("Categoria já existe.");
        });

        categoryRepository.save(new CategoryEntity(name));

        return "Categoria inserida com sucesso.";
    }

    public String updateCategory(Integer id, String name) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        category.setName(name.toUpperCase());
        categoryRepository.save(category);

        return "Categoria atualizada com sucesso.";
    }

    public String deleteCategory(Integer id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        categoryRepository.delete(category);

        return "Categoria deletada com sucesso.";
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryEntity::toCategoryDto).toList();
    }

}
