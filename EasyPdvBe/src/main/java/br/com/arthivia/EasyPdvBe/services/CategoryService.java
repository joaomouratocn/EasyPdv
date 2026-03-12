package br.com.arthivia.EasyPdvBe.services;

import br.com.arthivia.EasyPdvBe.model.dtos.CategoryDto;
import br.com.arthivia.EasyPdvBe.model.entities.CategoryEntity;
import br.com.arthivia.EasyPdvBe.repository.CategoryRepository;
import br.com.arthivia.EasyPdvBe.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public String insertCategory(String name) {
        categoryRepository.findByName(name).ifPresent(category -> {
            throw new RuntimeException("Categoria já existe.");
        });

        categoryRepository.save(new CategoryEntity(Util.normalizeUpper(name)));

        return "Categoria inserida com sucesso.";
    }

    public String updateCategory(Integer id, String name) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        category.setName(Util.normalizeUpper(name));
        categoryRepository.save(category);

        return "Categoria atualizada com sucesso.";
    }

    public String deleteCategory(Integer id) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        categoryRepository.delete(category);

        return "Categoria deletada com sucesso.";
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryEntity::toCategoryDto).sorted(Comparator.comparing(CategoryDto::name)).toList();
    }

}
