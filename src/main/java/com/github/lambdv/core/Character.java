package com.github.lambdv.core;
import java.lang.reflect.Array;
import java.util.*;
import com.github.lambdv.utils.DeepClone;

import com.github.lambdv.core.Stat;
import com.github.lambdv.core.Weapon;


/**
 * Object representation of a Character's total stat table
 * @note is a composite of an immutable base stats, a weapon, artifacts and a mutable fluid stat table
 * @note is a generalization of the Builder pattern for constructing characters through Mutation
 */
public class Character implements StatTable{
    private static final long serialVersionUID = 1L;

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

    Map<Stat, Double> substats;
    Map<Stat, Double> artifactSet2Piece;
    Map<Stat, Double> artifactSet4Piece;
    
    public Character(String name, double baseHP, double baseATK, double baseDEF, Stat ascensionStatType, double ascensionStatAmount){
        this.name = name;
        this.ascensionStatType = ascensionStatType;
        baseStats = new HashMap<>();
        baseStats.put(Stat.BaseHP, baseHP);
        baseStats.put(Stat.BaseATK, baseATK);
        baseStats.put(Stat.BaseDEF, baseDEF);
        baseStats.put(Stat.EnergyRecharge, 1.00);
        baseStats.put(Stat.CritRate, 0.05);
        baseStats.put(Stat.CritDMG, 0.50);
        baseStats.merge(ascensionStatType, ascensionStatAmount, Double::sum);
        fluidStats = new HashMap<>();
        substats = new HashMap<>();
        artifactSet2Piece = new HashMap<>();
        artifactSet4Piece = new HashMap<>();
    }

    public Character add(Stat type, double amount){
        fluidStats.merge(type, amount, Double::sum); return this;
    }

    public Character add(StatTable stats){
        stats.stats().forEach(this::add); return this;
    }
    
    @Override public double get(Stat type){
        return baseStats.getOrDefault(type, 0.0) 
            + fluidStats.getOrDefault(type, 0.0)
            + weapon.map(w -> w.get(type)).orElse(0.0)
            + flower.map(f -> f.get(type)).orElse(0.0)
            + feather.map(f -> f.get(type)).orElse(0.0)
            + sands.map(s -> s.get(type)).orElse(0.0)
            + goblet.map(g -> g.get(type)).orElse(0.0)
            + circlet.map(c -> c.get(type)).orElse(0.0)
            + substats.getOrDefault(type, 0.0)
            + artifactSet2Piece.getOrDefault(type, 0.0)
            + artifactSet4Piece.getOrDefault(type, 0.0)
        ;
    }

    public Character equip(Weapon weapon){
        this.weapon = Optional.of(weapon); return this;
    }

    public Character equip(Artifact artifact){
        switch (artifact) {
            case Flower flower -> this.flower = Optional.of(flower);
            case Feather feather -> this.feather = Optional.of(feather);
            case Sands sands -> this.sands = Optional.of(sands);
            case Goblet goblet -> this.goblet = Optional.of(goblet);
            case Circlet circlet -> this.circlet = Optional.of(circlet);
        } return this;
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
    public void unequipAllArtifacts(){
        unequipFlower();
        unequipFeather();
        unequipSands();
        unequipGoblet();
        unequipCirclet();
    }


    public Map<Stat, Double> stats(){
        // return Arrays.stream(Stat.values())
        //     .filter(stat -> get(stat) != 0.0)
        //     .map(stat -> Map.entry(stat, get(stat)))
        //     .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);

        return StatTables.merge(
            baseStats, 
            fluidStats, 
            substats,
            artifactSet2Piece,
            artifactSet4Piece,
            weapon.map(Weapon::stats).orElse(Map.of()), 
            flower.map(Flower::stats).orElse(Map.of()), 
            feather.map(Feather::stats).orElse(Map.of()), 
            sands.map(Sands::stats).orElse(Map.of()), goblet.map(Goblet::stats).orElse(Map.of()), 
            circlet.map(Circlet::stats).orElse(Map.of()))
            .stats();
}

    @Override public String toString(){
        return name
            + "\nBase Stats: " + baseStats
            + "\nFluid Stats: " + fluidStats
            + "\nWeapon: " + weapon;
    }

    public Character clone(){
        Character clone = new Character(name, baseStats.get(Stat.BaseHP), baseStats.get(Stat.BaseATK), baseStats.get(Stat.BaseDEF), ascensionStatType, baseStats.get(ascensionStatType));

        this.weapon.ifPresent(w -> clone.equip(w));
        this.flower.ifPresent(f -> clone.equip(f));
        this.feather.ifPresent(f -> clone.equip(f));
        this.sands.ifPresent(s -> clone.equip(s));
        this.goblet.ifPresent(g -> clone.equip(g));
        this.circlet.ifPresent(c -> clone.equip(c));
        Arrays.stream(Stat.values()).forEach(stat -> clone.add(stat, get(stat)));

        return clone;
    }
}

