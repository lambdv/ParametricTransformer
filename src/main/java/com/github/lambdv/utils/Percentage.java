package com.github.lambdv.utils;

public class Percentage {
    public static double parse(String str){
        if(str.contains("%")){
            str.replaceAll("%", "");  
            return Double.parseDouble(str) / 100; 
        }
        return Double.parseDouble(str);
    }
}
