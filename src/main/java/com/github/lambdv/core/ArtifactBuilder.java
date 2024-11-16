package com.github.lambdv.core;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO: make KQM builder allow you to specific rarity of each artifact piece
//TODO: method build up/roll into substatRolls
//TODO: method to compile artifact builder into a stat table or map
//TODO: method to compile artifact builder into 5 artifacts that can exist ingame


/**
 * Builder pattern to construct substats for given artifact pieces
 */
public class ArtifactBuilder implements MutableStatTable{
    Flower flower;
    Feather feather;
    Sands sands;
    Goblet goblet;
    Circlet circlet;

    private Stream<Artifact> artifacts(){return Stream.of(flower, feather, sands, goblet, circlet);}

    //mock substats
    Map<Stat, Integer> substatRolls; //current number of substat rolls
    Map<Stat, Integer> substatConstraints; //number of substat rolls for each stat type that are left

    public Map<Stat, Double> stats(){
        return Map.of(
            flower.statType(), flower.statValue(),
            feather.statType(), feather.statValue(),
            sands.statType(), sands.statValue(),
            goblet.statType(), goblet.statValue(),
            circlet.statType(), circlet.statValue()
            //substats
        );
    }

    public ArtifactBuilder(Flower flower, Feather feather, Sands sands, Goblet goblet, Circlet circlet){
        this.flower = flower;
        this.feather = feather;
        this.sands = sands;
        this.goblet = goblet;
        this.circlet = circlet;
        substatRolls = new HashMap<>();        
        substatConstraints = possibleSubStats().collect(Collectors.toMap(Function.identity(), 
            stat -> artifacts()
                .filter(a->!a.statType().equals(stat))
                .mapToInt(Artifacts::maxRollsFor)
                .sum()
        ));
    }

    /**
     * Factory method to provide an ArtifactBuilder with KQMC standard guidelines
     * 20 fixed subs: 2 for each substat
     * 20 fluid/distrubted substats left for you or an optimizer to roll
     * @return
     */
    public static ArtifactBuilder KQMC(Stat sandsStat, Stat gobletStat, Stat circletStat){
        ArtifactBuilder builder = new ArtifactBuilder(
            new Flower(ArtifactSet.empty(), 5, 20),
            new Feather(ArtifactSet.empty(), 5, 20),
            new Sands(ArtifactSet.empty(), 5, 20, sandsStat),
            new Goblet(ArtifactSet.empty(), 5, 20, gobletStat),
            new Circlet(ArtifactSet.empty(), 5, 20, circletStat)
        ){
            @Override public int maxRolls(){return 40;}
        };

        builder.substatRolls = new HashMap<>();
        possibleSubStats().forEach(stat->builder.substatRolls.put(stat, 2));

        var mainstats = List.of(Stat.FlatHP, Stat.FlatATK, sandsStat, gobletStat, circletStat);
        int maxNumRollsPerArtifact = 6; //Each substat type can have maximum 2 distributed substat rolls per artifact with a main stat that is of a different stat from it
        builder.substatConstraints = possibleSubStats().collect(Collectors.toMap(Function.identity(), stat->
            maxNumRollsPerArtifact * (int) mainstats.stream().filter(ms -> !ms.equals(stat)).count()
        ));
        return builder;
    }

    /**
     * current number of substat rolls across all 5 artifacts
     * @return
     */
    public int numRolls(){
        return substatRolls.values().stream().reduce(0, Integer::sum);
    }

    /**
     * maximum number of rolls possible across all artifacts
     * @note override this method if its used to determine the number of rolls you want to constrain
     * @return
     */
    public int maxRolls(){
        return artifacts()
            .mapToInt(Artifacts::maxRollsFor)
            .sum();
    }

    /**
     * number of rolls left to still distrubute
     * @return
     */
    public int numRollsLeft(){
        return maxRolls() - numRolls();
    }
    

    private static Stream<Stat> possibleSubStats(){
        return List.of(
            Stat.HPPercent, 
            Stat.FlatHP,
            Stat.ATKPercent,
            Stat.FlatATK,
            Stat.DEFPercent,
            Stat.FlatDEF,
            Stat.ElementalMastery,
            Stat.CritRate,
            Stat.CritDMG,
            Stat.EnergyRecharge
        ).stream();
    }
}

