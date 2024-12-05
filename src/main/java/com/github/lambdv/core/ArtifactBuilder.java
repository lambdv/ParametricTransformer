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
    private Optional<Flower> flower;
    private Optional<Feather> feather;
    private Optional<Sands> sands;
    private Optional<Goblet> goblet;
    private Optional<Circlet> circlet;
    record Roll(int rarity, Artifacts.RollQuality quality){}
    private Map<Stat, List<Roll>> substatRolls; //current number of substat rolls
    private Map<Stat, Integer> substatConstraints; //number of substat rolls for each stat type that are left



    public ArtifactBuilder(Optional<Flower> flower, Optional<Feather> feather, Optional<Sands> sands, Optional<Goblet> goblet, Optional<Circlet> circlet){
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

    public ArtifactBuilder(Flower flower, Feather feather, Sands sands, Goblet goblet, Circlet circlet){
        this(Optional.of(flower), Optional.of(feather), Optional.of(sands), Optional.of(goblet), Optional.of(circlet));
    }

    public ArtifactBuilder(){
        this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Factory method to provide an ArtifactBuilder with KQMC standard guidelines
     * 20 fixed subs: 2 for each substat
     * 20 fluid/distrubted substats left for you or an optimizer to roll
     * @return
     */
    public static ArtifactBuilder KQMC(Optional<Flower> flower, Optional<Feather> feather, Optional<Sands> sands, Optional<Goblet> goblet, Optional<Circlet> circlet){
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
            Optional.of(new Flower(ArtifactSet.empty(), 5, 20)),
            Optional.of(new Feather(ArtifactSet.empty(), 5, 20)),
            Optional.of(new Sands(ArtifactSet.empty(), 5, 20, sandsStat)),
            Optional.of(new Goblet(ArtifactSet.empty(), 5, 20, gobletStat)),
            Optional.of(new Circlet(ArtifactSet.empty(), 5, 20, circletStat))
        );
    }

 

    public Map<Stat, Double> mainStats(){
        return artifacts()
            .collect(Collectors.toMap(Artifact::statType, Artifact::statValue, Double::sum));
    }

    public Map<Stat, Double> substats(){
        return substatRolls.entrySet().stream()
            .collect(Collectors.toMap( //return map<Stat, Double> from map<Stat, List<Roll>>
                e -> e.getKey(), //keys stay the same
                e -> e.getValue().stream() //values are computed from the list of rolls
                        .mapToDouble(r -> Artifacts.getSubStatValue(r.rarity, e.getKey()) * r.quality.multiplier) 
                        .sum()));
    }

    public Map<Stat, Double> stats(){
        return Stream.concat(mainStats().entrySet().stream(), substats().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
    }

    Stream<Artifact> artifacts(){
        return Stream.of(flower, feather, sands, goblet, circlet)
            .filter(Optional::isPresent)
            .map(Optional::get);
    }

    /**
     * current number of substat rolls across all 5 artifacts
     * @return
     */
    public int numRolls(){
        return substatRolls.values().stream().mapToInt(List::size).sum();
    }

    /**
     * current number of substat rolls for a given substat
     * @param substat
     * @return
     */
    public int numRolls(Stat substat){
        return substatRolls.get(substat).size();
    }

    /**
     * current number of substat rolls for each substat
     * @return
     */
    public Map<Stat, Integer> rolls() {
        return substatRolls.entrySet().stream()
            .collect(Collectors.toMap( //return map<Stat, Double> from map<Stat, List<Roll>>
                e -> e.getKey(), //keys stay the same
                e -> e.getValue().stream().mapToInt(r -> 1) .sum()));
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
     * number of rolls left to still distrubute for a given substat
     * @param substat
     * @return
     */
    public int substatConstraints(Stat substat){
        return substatConstraints.get(substat);
    }

    /**
     * number of rolls left to still distrubute for a given substat
     * @param substat
     * @return
     */
    public int numRollsLeft(Stat substat){
        return substatConstraints.get(substat);
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
        Roll roll = new Roll(5, quality);
        substatRolls.merge(substat, new ArrayList<>(List.of(roll)), (l1, l2)->{l1.add(roll); return l1;});
        //substatRolls.merge(substat, 1, Integer::sum);
        //substatMultipliers.merge(substat, quality.multiplier, Double::sum);
    }

    /**
     * Commit num roll into a substat
     * @param substat
     * @param multiplier
     */
    public void roll(Stat substat, Artifacts.RollQuality quality, int num){
        assert numRollsLeft() > 0 : "No more rolls left to distribute";
        assert substatConstraints.get(substat) > 0 : "No more rolls left for " + substat;
        substatConstraints.merge(substat, -1, Integer::sum);
        Roll roll = new Roll(5, quality);
        substatRolls.merge(substat, new ArrayList<>(List.of(roll)), (l1, l2)->{
            for(int i = 0; i < num; i++) 
                l1.add(roll); 
        return l1;});
        //substatRolls.merge(substat, 1, Integer::sum);
        //substatMultipliers.merge(substat, quality.multiplier, Double::sum);
    }

    public void unRoll(Stat substat){
        //assert numRolls(substat) <= 0 : substat.toString() + " has no rolls";
        assert !substatRolls.get(substat).isEmpty();
        substatConstraints.merge(substat, 1, Integer::sum);
        substatRolls.get(substat).removeLast();
    }

    // public void unRoll(Stat substat, Artifacts.RollQuality quality){
    //     assert numRolls(substat) <= 0 : substat.toString() + " has no rolls";
    //     substatConstraints.merge(substat, 1, Integer::sum);
    //     substatRolls.get(substat).remove(substatRolls.get(substat).stream()
    //         .filter(r -> r.quality.equals(quality))
    //         .findFirst()
    //         .orElseThrow(() -> new IllegalStateException(substat.toString() + " has no rolls of quality " + quality)));
    // }

    public Optional<Flower> flower(){ return flower; }
    public Optional<Feather> feather(){ return feather; }
    public Optional<Sands> sands(){ return sands; }
    public Optional<Goblet> goblet(){ return goblet; }
    public Optional<Circlet> circlet(){ return circlet; }

    public ArtifactBuilder equip(Artifact artifact){ 
        switch (artifact) {
            case Flower flower -> this.flower = Optional.of(flower);
            case Feather feather -> this.feather = Optional.of(feather);
            case Sands sands -> this.sands = Optional.of(sands);
            case Goblet goblet -> this.goblet = Optional.of(goblet);
            case Circlet circlet -> this.circlet = Optional.of(circlet);
        } return this;
    }

    public void unequipFlower(){ this.flower = Optional.empty(); }
    public void unequipFeather(){ this.feather = Optional.empty(); }
    public void unequipSands(){ this.sands = Optional.empty(); }
    public void unequipGoblet(){ this.goblet = Optional.empty(); }
    public void unequipCirclet(){ this.circlet = Optional.empty(); }
    public void unequipAll(){
        flower = Optional.empty();
        feather = Optional.empty();
        sands = Optional.empty();
        goblet = Optional.empty();
        circlet = Optional.empty();
    }


    public void clear(){
        flower = Optional.empty();
        feather = Optional.empty();
        sands = Optional.empty();
        goblet = Optional.empty();
        circlet = Optional.empty();
        substatRolls.clear();
        substatConstraints.clear();
    }
    
    
    public static Stream<Stat> possibleSubStats(){
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

    public String toString(){
        return artifacts()
            .map(a -> a.statType().toString())
            .collect(Collectors.joining(", ", "Main Stats: ", ""))
            + "\nRolls: " + rolls()
            + "\nNumRolls: " + numRolls()
            //+ "\nRolls Left: " + numRollsLeft()
            //+ "\nRolls Left for each substat: " + substatConstraints
        ;
    }
}

