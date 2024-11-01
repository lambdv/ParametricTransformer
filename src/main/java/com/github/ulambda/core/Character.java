package com.github.ulambda.core;
import java.util.*;
import java.util.Optional;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import com.github.ulambda.core.Stat;
import com.github.ulambda.core.Weapon;

/**
 * Instance representation of a character's total stats.
 */
public class Character implements StatTable{
    private final Map<Stat, Double> baseStats; 
    private Map<Stat, Double> fluidStats; 
    private Optional<Weapon> weapon = Optional.empty();
    private Optional<Artifact> flower = Optional.empty();
    private Optional<Artifact> feather = Optional.empty();
    private Optional<Artifact> sands = Optional.empty();
    private Optional<Artifact> goblet = Optional.empty();
    private Optional<Artifact> circlet = Optional.empty();
 
    public Character(double baseHP, double baseATK, double baseDEF, Stat ascensionStatType, double ascensionStatAmount){
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

    public double addStat(Stat type, double amount){
        return fluidStats.merge(type, amount, Double::sum);
    }

    public double getStat(Stat type){
        return baseStats.getOrDefault(type, 0.0) 
            + fluidStats.getOrDefault(type, 0.0)
            + weapon.map(w -> w.getStat(type)).orElse(0.0)
            + flower.map(f -> f.getStat(type)).orElse(0.0)
            + feather.map(f -> f.getStat(type)).orElse(0.0)
            + sands.map(s -> s.getStat(type)).orElse(0.0)
            + goblet.map(g -> g.getStat(type)).orElse(0.0)
            + circlet.map(c -> c.getStat(type)).orElse(0.0);
    }

    public Map<Stat, Double> stats(){
        return Arrays.stream(Stat.values())
            .collect(
                HashMap::new, 
                (map, stat) -> map.put(stat, getStat(stat)), 
                HashMap::putAll
            );
    }


}