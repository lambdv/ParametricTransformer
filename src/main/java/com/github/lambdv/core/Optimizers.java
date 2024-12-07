package com.github.lambdv.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

interface Optimizer<T> extends StatTableVisitor<T> {
    T visitCharacter(Character c);
    T visitWeapon(Weapon w);
    T visitArtifact(Artifact a);
    T visitStatTable(StatTable s);
}

/**
 * Utility class that provides optimization algorithms for a given character and rotation.
 */
public class Optimizers {
    public static StatTableVisitor<ArtifactBuilder> KQMSArtifactOptimizer(Rotation r, double energyRechargeRequirements){
        return new KQMSArtifactOptimizer(r, energyRechargeRequirements);
    }
}   


record KQMSArtifactOptimizer(Rotation r, double energyRechargeRequirements) implements StatTableVisitor<ArtifactBuilder> {
    public ArtifactBuilder visitCharacter(Character c){
        var bob = OptimizationAlgorithms.optimal5StarArtifactMainStats(c, r, energyRechargeRequirements, x -> {});
        c.equip(bob);
        return bob;


        // if(c.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge) + 
        // (Artifacts.getSubStatValue(5, Stat.EnergyRecharge) * Artifacts.RollQuality.AVG.multiplier * 8) < energyRechargeRequirements) 
        //     throw new IllegalArgumentException("Energy Recharge requirements cannot be met");

        // Optional<ArtifactBuilder> bob = Optional.empty();
        // c.unequipAllArtifacts();
        // c.clearSubstats();
        // c.equip(new Flower(5, 20));
        // c.equip(new Feather(5, 20));
        // double bestComboDPR = 0;


        // var base = r.compute(c);

        // var sandsList = new ArrayList<Stat>(Sands.allowlist().stream().filter(s -> r.compute(c, StatTable.of(s, 1)) > base).toList());
        // sandsList.add(Stat.EnergyRecharge);
        // var gobletList = Goblet.allowlist().stream().filter(s -> r.compute(c, StatTable.of(s, 1)) > base).toList();
        // var circletList = Circlet.allowlist().stream().filter(s -> r.compute(c, StatTable.of(s, 1)) > base).toList();

        // for (Stat sandsMainStat : sandsList) {
        //     for (Stat gobletMainStat : gobletList) {
        //         for (Stat circletMainStat : circletList) {
        //             Optional<ArtifactBuilder> bob2 = Optional.empty();
        //             try {
        //                 c.equip(new Sands(5, 20, sandsMainStat));
        //                 c.equip(new Goblet(5, 20, gobletMainStat));
        //                 c.equip(new Circlet(5, 20, circletMainStat));
        //                 c.clearSubstats();
        //                 bob2 = Optional.of(OptimizationAlgorithms.greedyOptimialSubStatDistrbution(c, r, energyRechargeRequirements));
        //                 c.setSubstats(bob2.get().substats());
        //                 if (c.get(Stat.EnergyRecharge) < energyRechargeRequirements) continue;
        //                 double thisComboDPR = r.compute(c);
        //                 if (thisComboDPR > bestComboDPR) {
        //                     bestComboDPR = thisComboDPR;
        //                     bob = bob2;
        //                 }
        //             } catch (IllegalArgumentException e) {continue;}
        //         }
        //     }
        // }
        // c.equip(bob.get().sands().get());
        // c.equip(bob.get().goblet().get());
        // c.equip(bob.get().circlet().get());
        // c.setSubstats(bob.get().substats());
        // return bob.get();
    }

    public ArtifactBuilder visitWeapon(Weapon w){ throw new UnsupportedOperationException("Not yet implemented");}
    public ArtifactBuilder visitArtifact(Artifact a){ throw new UnsupportedOperationException("Not yet implemented"); }
    public ArtifactBuilder visitStatTable(StatTable s){ throw new UnsupportedOperationException("Not yet implemented"); }
}


class OptimizationAlgorithms {
    public static ArtifactBuilder gradientOptimialSubStatDistrbution(final Character c, final Rotation r, double energyRechargeRequirements) throws IllegalArgumentException{
        var bob = ArtifactBuilder.KQMC(c.flower(),c.feather(),c.sands(),c.goblet(),c.circlet());
        var target = new BuffedStatTable(c.build(), ()->bob.substats()); 
        try{
            while (target.get(Stat.EnergyRecharge) < energyRechargeRequirements)
                bob.roll(Stat.EnergyRecharge, Artifacts.RollQuality.AVG);
        } catch (AssertionError e){ throw new IllegalArgumentException("Energy Recharge requirements cannot be met with substats alone");}

        Set<Stat> possibleSubsToRoll = ArtifactBuilder.possibleSubStats().collect(Collectors.toSet());

        while (bob.numRollsLeft() > 0 && !possibleSubsToRoll.isEmpty()){
            var bestSub = possibleSubsToRoll.stream()
                .filter(s->bob.numRollsLeft(s) > 0)
                .map(s -> {
                    try{ 
                        bob.roll(s, Artifacts.RollQuality.AVG);
                        var dpr = r.compute(target);
                        return Map.entry(s, dpr); 
                    }
                    finally{ bob.unRoll(s); }
                })
                .map(e-> {
                    if(e.getValue() == 0) possibleSubsToRoll.remove(e.getKey());
                    return e;
                })
                .reduce((x,y) -> x.getValue() >= y.getValue() ? x : y)
                .get().getKey();
            //System.out.println(bestSub);
            bob.roll(bestSub, Artifacts.RollQuality.AVG);
        }
        return bob;
    }

    public static ArtifactBuilder greedyOptimialSubStatDistrbution(final Character c, final Rotation r, double energyRechargeRequirements) throws IllegalArgumentException{
        var bob = ArtifactBuilder.KQMC(c.flower(),c.feather(),c.sands(),c.goblet(),c.circlet());
        var target = new BuffedStatTable(c.build(), ()->bob.substats()); 
        try{
            while (target.get(Stat.EnergyRecharge) < energyRechargeRequirements && bob.numRollsLeft(Stat.EnergyRecharge) > 0)
                bob.roll(Stat.EnergyRecharge, Artifacts.RollQuality.AVG);
        } catch (AssertionError e){ throw new IllegalArgumentException("Energy Recharge requirements cannot be met with substats alone");}

        assert target.get(Stat.EnergyRecharge) >= energyRechargeRequirements : "Energy Recharge requirements cannot be met with substats alone";

        Set<Stat> possibleSubsToRoll = ArtifactBuilder.possibleSubStats().collect(Collectors.toSet());

        while (bob.numRollsLeft() > 0 && !possibleSubsToRoll.isEmpty()){
            var bestSub = possibleSubsToRoll.stream()
                .filter(s->bob.numRollsLeft(s) > 0)
                .map(s -> {
                    try{ 
                        bob.roll(s, Artifacts.RollQuality.AVG);
                        return Map.entry(s, r.compute(target)); 
                    }
                    finally{ bob.unRoll(s); }
                })
                .map(e-> {
                    if(e.getValue() == 0) possibleSubsToRoll.remove(e.getKey());
                    return e;
                })
                .reduce((x,y) -> x.getValue() >= y.getValue() ? x : y)
                .get().getKey();
            var rollsToFill = Math.min(bob.numRollsLeft(), bob.numRollsLeft(bestSub));
            //System.out.println(bestSub);
            bob.roll(bestSub, Artifacts.RollQuality.AVG, rollsToFill);
        }
        return bob;
    }

    
    public static ArtifactBuilder optimal5StarArtifactMainStats(final Character c, final Rotation r, double energyRechargeRequirements, Consumer<Character> callback){

        Optional<ArtifactBuilder> bob = Optional.empty();
        Character copy = c.clone();


        copy.unequipAllArtifacts();
        copy.clearSubstats();

       

        double erWithErSands = copy.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge);
        double erWithErSandsAndSubs = erWithErSands + (Artifacts.getSubStatValue(5, Stat.EnergyRecharge) * Artifacts.RollQuality.AVG.multiplier * 8);
        if(erWithErSandsAndSubs < energyRechargeRequirements) 
            throw new IllegalArgumentException("Energy Recharge requirements cannot be met with mainstats alone");
        
        copy.equip(new Flower(5, 20));
        copy.equip(new Feather(5, 20));
        double bestComboDPR = 0;

        var base = r.compute(copy);

        var sandsList = erWithErSands < energyRechargeRequirements ? List.of(Stat.EnergyRecharge) : new ArrayList<Stat>(Sands.allowlist().stream().filter(s -> r.compute(copy, StatTable.of(s, 1)) > base).toList());
        if(!sandsList.contains(Stat.EnergyRecharge))
            sandsList.add(Stat.EnergyRecharge);
        var gobletList = Goblet.allowlist().stream().filter(s -> r.compute(copy, StatTable.of(s, 1)) > base).toList();
        var circletList = Circlet.allowlist().stream().filter(s -> r.compute(copy, StatTable.of(s, 1)) > base).toList();

        for (Stat sandsMainStat : sandsList) {
            for (Stat gobletMainStat : gobletList) {
                for (Stat circletMainStat : circletList) {
                    Optional<ArtifactBuilder> bob2 = Optional.empty();
                    try {
                        copy.equip(new Sands(5, 20, sandsMainStat));
                        copy.equip(new Goblet(5, 20, gobletMainStat));
                        copy.equip(new Circlet(5, 20, circletMainStat));
                        copy.clearSubstats();
                        bob2 = Optional.of(OptimizationAlgorithms.greedyOptimialSubStatDistrbution(copy, r, energyRechargeRequirements));
                        copy.setSubstats(bob2.get().substats());
                        if (copy.get(Stat.EnergyRecharge) < energyRechargeRequirements) continue;
                        double thisComboDPR = r.compute(copy);
                        if (thisComboDPR > bestComboDPR) {
                            bestComboDPR = thisComboDPR;
                            bob = bob2;
                        }
                    } catch (IllegalArgumentException e) {continue;}
                }
            }
        }
        System.out.println(copy.equip(bob.get()).stats());
        return bob.get();
        

        // boolean needERSands = c.get(Stat.EnergyRecharge) < energyRechargeRequirements;
        // boolean ERSandsIsntEnough = c.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge) < energyRechargeRequirements;
        // if(needERSands && ERSandsIsntEnough)
        //     throw new IllegalArgumentException("Energy Recharge requirements cannot be met with mainstats alone");
        
        // Character copy = c.clone();
        // copy.unequipAllArtifacts();
        // //c.substats = new HashMap<>();
        // c.clearSubstats();
        // Flower bestFlower = new Flower(5, 20);
        // Feather bestFeather = new Feather(5, 20);
        // Sands bestSands = new Sands(1, 0, Stat.ATKPercent);
        // Goblet bestGoblet = new Goblet(1, 0, Stat.ATKPercent);
        // Circlet bestCirclet = new Circlet(1, 0, Stat.ATKPercent);

        // copy.equip(bestFlower);
        // copy.equip(bestFeather);

        // double bestComboDPR = 0;
        
        // for(Stat sandsMainStat : needERSands ? List.of(Stat.EnergyRecharge) : Sands.allowlist()){
        //     for(Stat gobletMainStat : Goblet.allowlist()){
        //         for(Stat circletMainStat : Circlet.allowlist()){
        //             copy.equip(new Sands(5, 20, sandsMainStat));
        //             copy.equip(new Goblet(5, 20, gobletMainStat));
        //             copy.equip(new Circlet(5, 20, circletMainStat));


                   
        //             double thisComboDPR = r.compute(copy);
        //             if(thisComboDPR > bestComboDPR){
        //                 bestComboDPR = thisComboDPR;
        //                 bestSands = copy.sands().get();
        //                 bestGoblet = copy.goblet().get();
        //                 bestCirclet = copy.circlet().get();
        //             }
        //         }
        //     }
        // }
        // copy.equip(bestSands);
        // copy.equip(bestGoblet);
        // copy.equip(bestCirclet);
        // return new ArtifactBuilder(copy.flower().get(), copy.feather().get(), copy.sands().get(), copy.goblet().get(), copy.circlet().get());
    }
}