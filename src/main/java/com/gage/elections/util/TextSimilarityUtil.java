package com.gage.elections.util;

import java.util.*;

public class TextSimilarityUtil {
    private static final Set<String> STOPWORDS = Set.of("y","o","el","la","los","las","de","en","por","para","con","un","una","es","que","se","al");

    public static double similarity(String a, String b) {
        Map<String, Integer> va = tokens(a);
        Map<String, Integer> vb = tokens(b);
        return cosine(va, vb);
    }
    private static Map<String,Integer> tokens(String s) {
        if (s == null) return Collections.emptyMap();
        String[] parts = s.toLowerCase().replaceAll("[^a-záéíóúñ0-9 ]", " ").split("\s+");
        Map<String,Integer> map = new HashMap<>();
        for (String p : parts) {
            if (p.isBlank() || STOPWORDS.contains(p)) continue;
            map.put(p, map.getOrDefault(p, 0) + 1);
        }
        return map;
    }
    private static double cosine(Map<String,Integer> a, Map<String,Integer> b) {
        Set<String> keys = new HashSet<>();
        keys.addAll(a.keySet()); keys.addAll(b.keySet());
        double dot=0, na=0, nb=0;
        for(String k:keys){
            int va=a.getOrDefault(k,0), vb=b.getOrDefault(k,0);
            dot+=va*vb; na+=va*va; nb+=vb*vb;
        }
        if(na==0||nb==0) return 0;
        return dot / (Math.sqrt(na)*Math.sqrt(nb));
    }
}
