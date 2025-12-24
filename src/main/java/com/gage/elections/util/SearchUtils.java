package com.gage.elections.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.regex.Pattern;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtils {

    private static final int MAX_QUERY_LENGTH = 50;
    private static final int MIN_QUERY_LENGTH = 3;

    private static final Pattern LUCENE_SPECIAL_CHARACTERS =
            Pattern.compile("[+\\-&|!(){}\\[\\]^\"~*?:/\\\\]");

    public static String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String sanitized = input.length() > MAX_QUERY_LENGTH
                ? input.substring(0, MAX_QUERY_LENGTH)
                : input;

        sanitized = LUCENE_SPECIAL_CHARACTERS.matcher(sanitized).replaceAll(" ");

        return sanitized.replaceAll("\\s+", " ").trim();
    }

    public static boolean isValid(String sanitizedQuery) {
        if (sanitizedQuery == null || sanitizedQuery.isBlank()) {
            return false;
        }

        return Arrays.stream(sanitizedQuery.split("\\s+"))
                .anyMatch(token -> token.length() >= MIN_QUERY_LENGTH);
    }
}
