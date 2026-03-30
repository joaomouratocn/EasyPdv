package br.com.arthivia.api.util;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime now, HttpStatus value, String message) {
}
