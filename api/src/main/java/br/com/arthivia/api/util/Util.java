package br.com.arthivia.api.util;

import java.text.Normalizer;

public class Util {
    public static String normalizeText(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        String withoutAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return withoutAccents.toUpperCase();
    }

    public static boolean isCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos ou se é uma sequência repetida conhecida
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo do 1º Dígito Verificador
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }

            int num = 11 - (soma % 11);
            int digito1 = (num > 9) ? 0 : num;

            // Cálculo do 2º Dígito Verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }

            num = 11 - (soma % 11);
            int digito2 = (num > 9) ? 0 : num;

            // Verifica se os dígitos calculados conferem com os digitados
            return (digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10)));

        } catch (Exception e) {
            return false;
        }
    }
}
