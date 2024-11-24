package com.github.lambdv.core;
import java.util.function.Function;

/**
 * 
 */
public interface DamageInstance extends Function<StatTable, Double>{
    Double apply(StatTable target);
} 