package br.com.arthivia.api.models.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CustomerDto(
    UUID id,
    @NonNull
    @NotBlank
    String name,
    @NonNull
    @NotBlank
    String cpf,
    @NonNull
    @NotBlank
    String phone
){}
