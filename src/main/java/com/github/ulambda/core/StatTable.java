package com.github.ulambda.core;

import java.util.Map;

public interface StatTable{
    Map<Stat, Double> stats();
    public default double getStat(Stat type){
        return stats().getOrDefault(type, 0.0);
    }
    public default double addStat(Stat type, double amount){
        return stats().merge(type, amount, Double::sum);
    }
}
