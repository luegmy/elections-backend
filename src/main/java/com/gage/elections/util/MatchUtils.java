package com.gage.elections.util;

import java.text.Normalizer;

public class MatchUtils {

    public static String normalize(String text) {
        if (text == null) return null;
        return Normalizer
                .normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }

}
