package br.com.arthivia.EasyPdvBe.model.dtos;

public record CategoryDto(
        Integer id,
        String name
) {
    public CategoryDto {
        if (id == null) {
            id = 0;
        }
    }
}
