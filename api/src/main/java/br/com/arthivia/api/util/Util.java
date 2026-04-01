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
}
