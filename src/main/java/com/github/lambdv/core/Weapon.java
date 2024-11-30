package com.github.lambdv.core;

import java.io.Serializable;
import java.util.Map;

/**
 * Weapon object represents a weapon in the game.
 */
public record Weapon(
    String name, 
    int rarity, 
    int level, 
    int refinement, 
    double baseATK, 
    Stat mainStatType, 
    double mainStatAmount
) implements StatTable {
    public Map<Stat, Double> stats(){
        return Map.of(
            Stat.BaseATK, baseATK,
            mainStatType, mainStatAmount
        );
    }
    @Override public String toString(){ 
        return "Name" + ": " + name
            + ", Rarity: " + rarity
            + ", Level: " + level
            + ", Refinement: " + refinement
            + ", BaseATK: " + baseATK
            + ", MainStatType: " + mainStatType
            + ", MainStatAmount: " + mainStatAmount;
    }
}