package com.github.lambdv.core;
import java.util.*;

import com.github.lambdv.core.Stat;
import com.github.lambdv.core.Weapon;

/**
 * Object representation of a Character's total stat table
 * @note is a composite of an immutable base stats, a weapon, artifacts and a mutable fluid stat table
 * @note is a generalization of the Builder pattern for constructing characters through Mutation
 * 
 */
public class Character implements StatTable{
    //character identity details
    public int level = 90;
    public String name;
    public Stat ascensionStatType;
    //composite stat tables
    private final Map<Stat, Double> baseStats; 
    private Map<Stat, Double> fluidStats; 
    private Optional<Weapon> weapon = Optional.empty();
    private Optional<Flower> flower = Optional.empty();
    private Optional<Feather> feather = Optional.empty();
    private Optional<Sands> sands = Optional.empty();
    private Optional<Goblet> goblet = Optional.empty();
    private Optional<Circlet> circlet = Optional.empty();

    //private Map<Stat, Double> substats
    //private Map<Stat, Double> 2pcsetbonus
    //private Map<Stat, Double> 4pcsetbonus

    //public void equip(weapon)
    //public void equip(artifact)
    //public void optimize(visitor, rotation)
    
 
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

    public Character add(Stat type, double amount){
        fluidStats.merge(type, amount, Double::sum);
        return this;
    }

    public Character add(StatTable stats){
        stats.stats().forEach(this::add);
        return this;
    }
    
    @Override public double get(Stat type){
        return baseStats.getOrDefault(type, 0.0) 
            + fluidStats.getOrDefault(type, 0.0)
            + weapon.map(w -> w.get(type)).orElse(0.0)
            + flower.map(f -> f.get(type)).orElse(0.0)
            + feather.map(f -> f.get(type)).orElse(0.0)
            + sands.map(s -> s.get(type)).orElse(0.0)
            + goblet.map(g -> g.get(type)).orElse(0.0)
            + circlet.map(c -> c.get(type)).orElse(0.0);
    }
    

    public Character equip(Weapon weapon){
        this.weapon = Optional.of(weapon);
        return this;
    }

    public Character equip(Artifact artifact){
        //pattern matching
        switch (artifact) {
            case Flower flower -> this.flower = Optional.of(flower);
            case Feather feather -> this.feather = Optional.of(feather);
            case Sands sands -> this.sands = Optional.of(sands);
            case Goblet goblet -> this.goblet = Optional.of(goblet);
            case Circlet circlet -> this.circlet = Optional.of(circlet);
            default -> throw new IllegalArgumentException("Unknown artifact type: " + artifact.getClass().getName());
        }
        return this;
    }

    public Optional<Weapon> weapon(){return weapon;}
    public Optional<Flower> flower(){return flower;}
    public Optional<Feather> feather(){return feather;}
    public Optional<Sands> sands(){return sands;}
    public Optional<Goblet> goblet(){return goblet;}
    public Optional<Circlet> circlet(){return circlet;}

    public void unequipWeapon(){weapon = Optional.empty();}
    public void unequipFlower(){flower = Optional.empty();}
    public void unequipFeather(){feather = Optional.empty();}
    public void unequipSands(){sands = Optional.empty();}
    public void unequipGoblet(){goblet = Optional.empty();}
    public void unequipCirclet(){circlet = Optional.empty();}


    public Map<Stat, Double> stats(){
        return Arrays.stream(Stat.values())
            .filter(stat -> get(stat) != 0.0)
            .map(stat -> Map.entry(stat, get(stat)))
            .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
}

    @Override public String toString(){
        return name
            + "\nBase Stats: " + baseStats
            + "\nFluid Stats: " + fluidStats
            + "\nWeapon: " + weapon;
    }
}