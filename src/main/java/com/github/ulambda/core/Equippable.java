package com.github.ulambda.core;
import java.util.Map;

public interface Equippable{
    Map<Stat, Double> stats();
    default double getStat(Stat type){
        return stats().getOrDefault(type, 0.0);
    }
}
