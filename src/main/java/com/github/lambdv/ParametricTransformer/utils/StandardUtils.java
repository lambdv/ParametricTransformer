package com.github.lambdv.ParametricTransformer.utils;

public class StandardUtils {
    public static String flattenName(String name){
        return name.toLowerCase()
            .replaceAll("\\s", "")
            .replaceAll(" ", "")
            .replaceAll("[^a-zA-Z0-9]", "");
    }
}
