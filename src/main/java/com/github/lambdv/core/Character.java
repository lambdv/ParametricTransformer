package com.github.lambdv.core;
import java.util.*;
import java.util.Optional;
import java.util.Set;

import com.github.lambdv.core.Stat;
import com.github.lambdv.core.Weapon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Representation for an in-game Character's stat table.
 * @note Character objects are a generalization of a builder pattern where the stat table is built up and compiled into an immutable instance.
 * @note Character's base stats are immutable and are set at construction time.
 * @note Character's stats are built by adding fluid stats and equipping artifacts and weapons.
 */
public class Character implements MutableStatTable{
    public String name;
    public Stat ascensionStatType;
    private final Map<Stat, Double> baseStats; 
    private Map<Stat, Double> fluidStats; 
    private Optional<Weapon> weapon = Optional.empty();
    // private Optional<Artifact> flower = Optional.empty();
    // private Optional<Artifact> feather = Optional.empty();
    // private Optional<Artifact> sands = Optional.empty();
    // private Optional<Artifact> goblet = Optional.empty();
    // private Optional<Artifact> circlet = Optional.empty();
 
    public Character(String name, double baseHP, double baseATK, double baseDEF, Stat ascensionStatType, double ascensionStatAmount){
        this.name = name;
        this.ascensionStatType = ascensionStatType;
        baseStats = new HashMap<>(Map.of(
            Stat.BaseHP, baseHP,
            Stat.BaseATK, baseATK,
            Stat.BaseDEF, baseDEF,
            Stat.EnergyRecharge, 1.00,
            Stat.CritRate, 0.05,
            Stat.CritDMG, 0.50
        ));
        baseStats.merge(ascensionStatType, ascensionStatAmount, Double::sum);
        fluidStats = new HashMap<>();
    }

    public Character (Characters.CharacterKey key){
        this(key.name(), key.baseHP(), key.baseATK(), key.baseDEF(), key.ascensionStatType(), key.ascensionStatAmount());
    }

    public double addStat(Stat type, double amount){
        return fluidStats.merge(type, amount, Double::sum);
    }

    public double getStat(Stat type){
        return baseStats.getOrDefault(type, 0.0) 
            + fluidStats.getOrDefault(type, 0.0)
            + weapon.map(w -> w.getStat(type)).orElse(0.0);
            // + flower.map(f -> f.getStat(type)).orElse(0.0)
            // + feather.map(f -> f.getStat(type)).orElse(0.0)
            // + sands.map(s -> s.getStat(type)).orElse(0.0)
            // + goblet.map(g -> g.getStat(type)).orElse(0.0)
            // + circlet.map(c -> c.getStat(type)).orElse(0.0);
    }

    public Map<Stat, Double> stats(){
        return Arrays.stream(Stat.values())
            .collect(HashMap::new, 
                (map, stat) -> map.put(stat, getStat(stat)), 
                HashMap::putAll
            );
    }

    @Override public String toString(){
        return name
            + "\nBase Stats: " + baseStats
            + "\nFluid Stats: " + fluidStats
            + "\nWeapon: " + weapon;
    }

}