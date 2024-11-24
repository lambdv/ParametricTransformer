package com.github.lambdv.core;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builder pattern to construct substats for given artifact pieces
 */
public class ArtifactBuilder implements StatTable{
    Flower flower;
    Feather feather;
    Sands sands;
    Goblet goblet;
    Circlet circlet;

    private record Roll(int rarity, Artifacts.RollQuality quality){} 

    Map<Stat, Integer> substatRolls; //current number of substat rolls
    Map<Stat, Integer> substatConstraints; //number of substat rolls for each stat type that are left
    Map<Stat, Double> substatMultipliers; //multipliers for each substat roll

    public Map<Stat, Double> stats(){
        var stats = substats();
        stats.merge(Stat.FlatHP, flower.statValue(), Double::sum);
        stats.merge(Stat.FlatATK, feather.statValue(), Double::sum);
        stats.merge(sands.statType(), sands.statValue(), Double::sum);
        stats.merge(goblet.statType(), goblet.statValue(), Double::sum);
        stats.merge(circlet.statType(), circlet.statValue(), Double::sum);
        return stats;
    }

    /**
     * Compute a map of stats from substat specification
     * @return
     */
    public Map<Stat, Double> substats(){
        return substatRolls.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                    /**TODO: ASSUMS THAT EACH ARTIFACT IS FROM 5 STAR */
                    System.err.println("Warning: substat value is based on 5 star artifact");
                    var artifactSubStatBaseValue = Artifacts.getSubStatValue(5, e.getKey());
                    var totalMultiperSum = substatMultipliers.get(e.getKey());
                    return (artifactSubStatBaseValue * (totalMultiperSum)); 
                }    
            ));
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
        substatMultipliers = new HashMap<>(); 
    }

    /**
     * Factory method to provide an ArtifactBuilder with KQMC standard guidelines
     * 20 fixed subs: 2 for each substat
     * 20 fluid/distrubted substats left for you or an optimizer to roll
     * @return
     */
    public static ArtifactBuilder KQMC(Flower flower, Feather feather, Sands sands, Goblet goblet, Circlet circlet){
        ArtifactBuilder builder = new ArtifactBuilder(flower, feather, sands, goblet, circlet){ 
            @Override public int maxRolls(){ 
                int penalty = (int) artifacts().filter(art->art.rarity() == 4).count()*2;
                int numArts = (int) artifacts().count();
                return super.maxRolls() - numArts - penalty; 
            } 
        };
        //possibleSubStats().forEach(stat->builder.substatRolls.put(stat, 2));
        possibleSubStats().forEach(stat-> {
            builder.roll(stat, Artifacts.RollQuality.AVG);
            builder.roll(stat, Artifacts.RollQuality.AVG);
        });
        builder.substatConstraints = possibleSubStats().collect(Collectors.toMap(Function.identity(), 
            stat -> builder.artifacts()
                .filter(art->!art.statType().equals(stat))
                .mapToInt(s->2)
                .sum()
        ));
        return builder;
    }


    public static ArtifactBuilder KQMC(Stat sandsStat, Stat gobletStat, Stat circletStat){
        return KQMC(
            new Flower(ArtifactSet.empty(), 5, 20),
            new Feather(ArtifactSet.empty(), 5, 20),
            new Sands(ArtifactSet.empty(), 5, 20, sandsStat),
            new Goblet(ArtifactSet.empty(), 5, 20, gobletStat),
            new Circlet(ArtifactSet.empty(), 5, 20, circletStat)
        );
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

    /**
     * Commit a roll into a substat
     * @param substat
     * @param multiplier
     */
    public void roll(Stat substat, Artifacts.RollQuality quality){
        assert numRollsLeft() > 0 : "No more rolls left to distribute";
        assert substatConstraints.get(substat) > 0 : "No more rolls left for " + substat;
        substatConstraints.merge(substat, -1, Integer::sum);
        substatRolls.merge(substat, 1, Integer::sum);
        substatMultipliers.merge(substat, quality.multiplier, Double::sum);
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

    Stream<Artifact> artifacts(){return Stream.of(flower, feather, sands, goblet, circlet);}
}

