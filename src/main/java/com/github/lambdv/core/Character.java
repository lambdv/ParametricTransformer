package com.github.lambdv.core;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public final double ascensionStatAmount;
    //composite stat tables
    private final Map<Stat, Double> baseStats; 
    private Map<Stat, Double> fluidStats; 
    private Map<Stat, List<Function<StatTable, Double>>> dynamicFluidStats;
    private Optional<Weapon> weapon = Optional.empty();
    private Optional<Flower> flower = Optional.empty();
    private Optional<Feather> feather = Optional.empty();
    private Optional<Sands> sands = Optional.empty();
    private Optional<Goblet> goblet = Optional.empty();
    private Optional<Circlet> circlet = Optional.empty();
    private Map<Stat, Double> substats;
    private Map<Stat, Double> artifactSet2Piece;
    private Map<Stat, Double> artifactSet4Piece;



    public Character(String name, double baseHP, double baseATK, double baseDEF, Stat ascensionStatType, double ascensionStatAmount){
        this.name = name;
        this.ascensionStatType = ascensionStatType;
        baseStats = new HashMap<>(Map.of(
            Stat.BaseHP, baseHP,
            Stat.BaseATK, baseATK,
            Stat.BaseDEF, baseDEF,
            Stat.CritRate, 0.05,
            Stat.CritDMG, 0.5,
            Stat.EnergyRecharge, 1.00
        ));
        ///baseStats.merge(ascensionStatType, ascensionStatAmount, Double::sum);
        this.ascensionStatAmount = ascensionStatAmount;
        fluidStats = new HashMap<>();
        dynamicFluidStats = new HashMap<>();

        substats = new HashMap<>();
        artifactSet2Piece = new HashMap<>();
        artifactSet4Piece = new HashMap<>();
    }

    /**
     * Adds static stat value pairs to character's fluid stat pool
     * @param type
     * @param amount
     * @return
     */
    public Character add(Stat type, double amount){
        fluidStats.merge(type, amount, Double::sum); return this;
    }

    /**
     * Adds multiple static stat value pairs to character's fluid stat pool
     * @param stats
     * @return
     */
    public Character add(StatTable stats){
        stats.stats().forEach(this::add); return this;
    }

    /**
     * Adds dynamic stat value pairs to character's dynafluid stat pool
     * @param type
     * @param dynamicAmount
     * @return
     */
    public Character add(Stat type, Function<StatTable, Double> dynamicAmount){
        dynamicFluidStats.merge(type, List.of(dynamicAmount), (l1, l2) -> {
            List<Function<StatTable, Double>> l = new ArrayList<>(l1);
            l.addAll(l2); return l;
        }); return this;
    }

    /**
     * Getter for the sum value of a stat type from a character
     * @note multiple get calls on a character may be expensive: build first for inital expensive computation and then log(n) cost on gets
     */
    @Override public double get(Stat type){
        return baseStats.getOrDefault(type, 0.0) 
            + (ascensionStatType==type?ascensionStatAmount:0.0)
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
            + getDynamic(type);
    }

    /**
     * Getter for the sum value of a dynamic stat type from a character
     * @param type
     * @return
     */
    private double getDynamic(Stat type){
        if(!dynamicFluidStats.containsKey(type)) 
            return 0.0;
        return dynamicFluidStats.get(type).stream()
            .map(f -> f.apply(this))
            .reduce(0.0, Double::sum);
    }

    /**
     * Sets character's weapon field to the given weapon
     * @param weapon
     * @return this
     */
    public Character equip(Weapon weapon){
        this.weapon = Optional.of(weapon); return this;
    }

    /**
     * Sets character's artifact field to the given artifact
     * @param artifact
     * @return this
     */
    public Character equip(Artifact artifact){
        switch (artifact) {
            case Flower flower -> this.flower = Optional.of(flower);
            case Feather feather -> this.feather = Optional.of(feather);
            case Sands sands -> this.sands = Optional.of(sands);
            case Goblet goblet -> this.goblet = Optional.of(goblet);
            case Circlet circlet -> this.circlet = Optional.of(circlet);
        } return this;
    }

    public Character equip(ArtifactBuilder artifacts){
        artifacts.flower().ifPresent(this::equip);
        artifacts.feather().ifPresent(this::equip);
        artifacts.sands().ifPresent(this::equip);
        artifacts.goblet().ifPresent(this::equip);
        artifacts.circlet().ifPresent(this::equip);
        this.setSubstats(artifacts.substats());
        return this;
    }


    public void unequipWeapon(){weapon = Optional.empty();}
    public void unequipFlower(){flower = Optional.empty();}
    public void unequipFeather(){feather = Optional.empty();}
    public void unequipSands(){sands = Optional.empty();}
    public void unequipGoblet(){goblet = Optional.empty();}
    public void unequipCirclet(){circlet = Optional.empty();}
    public void unequipAllArtifacts(){
        flower = Optional.empty();
        feather = Optional.empty();
        sands = Optional.empty();
        goblet = Optional.empty();
        circlet = Optional.empty();
    }
    
    public Optional<Weapon> weapon(){return weapon;}
    public Optional<Flower> flower(){return flower;}
    public Optional<Feather> feather(){return feather;}
    public Optional<Sands> sands(){return sands;}
    public Optional<Goblet> goblet(){return goblet;}
    public Optional<Circlet> circlet(){return circlet;}

    /**
     * Builds a map the sum of all stat-value pairs from a character
     */
    public Map<Stat, Double> stats(){
        return Arrays.stream(Stat.values())
            .filter(s -> get(s) != 0.0)
            .map(s -> Map.entry(s, get(s)))
            .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    @Override public String toString(){
        return name
            + "\nBase Stats: " + baseStats
            + "\nFluid Stats: " + fluidStats
            + "\nWeapon: " + weapon
            + "\nFlower: " + flower
            + "\nFeather: " + feather
            + "\nSands: " + sands
            + "\nGoblet: " + goblet
            + "\nCirclet: " + circlet
            + "\nSubstats: " + substats
            + "\nArtifact Set 2 Piece: " + artifactSet2Piece
            + "\nArtifact Set 4 Piece: " + artifactSet4Piece
            + "\nDynamic Fluid Stats: " + dynamicFluidStats
        ;
    }

    public <T> T accept(StatTableVisitor<T> visitor){
        return visitor.visitCharacter(this);
    }

    public <T> T accept(Supplier<StatTableVisitor<T>> visitor){
        return this.accept(visitor.get());
    }

    public <T> T optimize(StatTableVisitor<T> visitor){
        return visitor.visitCharacter(this);
    }

    public <T> T optimize(Supplier<StatTableVisitor<T>> visitor){
        return this.optimize(visitor.get());
    }

    /**
     * Creates a deep copy of the character
     */
    public Character clone(){
        var c = this;
        var clone = new Character(c.name, 
            c.baseStats().getOrDefault(Stat.BaseHP, 0.0), 
            c.baseStats().getOrDefault(Stat.BaseATK, 0.0),
            c.baseStats().getOrDefault(Stat.BaseDEF, 0.0),
            c.ascensionStatType, c.ascensionStatAmount
        );
        clone.fluidStats = new HashMap<>(c.fluidStats);
        clone.dynamicFluidStats = new HashMap<>(c.dynamicFluidStats);
        //alicing risk but all fields should be immutable
        clone.weapon = c.weapon;
        clone.flower = c.flower;
        clone.feather = c.feather;
        clone.sands = c.sands;
        clone.goblet = c.goblet;
        clone.circlet = c.circlet;
        clone.substats = new HashMap<>(c.substats);
        clone.artifactSet2Piece = new HashMap<>(c.artifactSet2Piece);
        clone.artifactSet4Piece = new HashMap<>(c.artifactSet4Piece);
        assert Arrays.stream(Stat.values())
            .allMatch(s -> c.get(s) == clone.get(s)) 
            : "Character stats not equal";
        return clone;
    }

    public Map<Stat, Double> baseStats(){
        return Collections.unmodifiableMap(baseStats);
    }
    public Map<Stat, Double> fluidStats(){
        return Collections.unmodifiableMap(fluidStats);
    }
    public Map<Stat, List<Function<StatTable, Double>>> dynamicFluidStats(){
        return Collections.unmodifiableMap(dynamicFluidStats);
    }
    public Map<Stat, Double> substats(){
        return Collections.unmodifiableMap(substats);
    }

    public Character setSubstats(Map<Stat, Double> substats){
        this.substats = new HashMap<>(substats); 
        return this;
    }

    public Character clearSubstats(){
        substats = new HashMap<>(); return this;
    }

    public Map<Stat, Double> artifactSet2Piece(){
        return Collections.unmodifiableMap(artifactSet2Piece);
    }

    public Map<Stat, Double> artifactSet4Piece(){
        return Collections.unmodifiableMap(artifactSet4Piece);
    }

    public Character setArtifactSet2Piece(Map<Stat, Double> artifactSet2Piece){
        this.artifactSet2Piece = artifactSet2Piece; return this;
    }

    public Character setArtifactSet4Piece(Map<Stat, Double> artifactSet4Piece){
        this.artifactSet4Piece = artifactSet4Piece; return this;
    }
}

