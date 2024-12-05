package com.github.lambdv.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Utility class that provides optimization algorithms for a given character and rotation.
 */
public class Optimizer {
    public static ArtifactBuilder optimialArtifactSubStatDistrbution(final Character c, final Rotation r, double energyRechargeRequirements
        //Map<Stat, Double> statRequirements
    ) throws IllegalArgumentException{
        var bob = ArtifactBuilder.KQMC(c.flower(),c.feather(),c.sands(),c.goblet(),c.circlet());
        var target = BuffedStatTable.of(c.build(), ()->bob.substats()); 
        try{
            while (target.get(Stat.EnergyRecharge) < energyRechargeRequirements)
                bob.roll(Stat.EnergyRecharge, Artifacts.RollQuality.AVG);
        } 
        catch (AssertionError e){
            throw new IllegalArgumentException("Energy Recharge requirements cannot be met with substats alone");
        }

        Set<Stat> possibleSubsToRoll = ArtifactBuilder.possibleSubStats().collect(Collectors.toSet());

        while (bob.numRollsLeft() > 0 && !possibleSubsToRoll.isEmpty())
            bob.roll(
                possibleSubsToRoll.stream()
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
                    .reduce((x,y) -> x.getValue() > y.getValue() ? x : y)
                    .get().getKey(), 
                Artifacts.RollQuality.AVG
            );
        return bob;
    }

    public static ArtifactBuilder optimal5StarArtifactMainStats(final Character c, final Rotation r, double energyRechargeRequirements
        //flower rarity 
        //flower level
        //feather rarity
        //feather level
        //sands rarity
        //sands level
        //sands main stat
        //goblet rarity
        //goblet level
        //goblet main stat
        //circlet rarity
        //circlet level
        //circlet main stat
        //stat requirements
    ){
        boolean needERSands = c.get(Stat.EnergyRecharge) < energyRechargeRequirements;
        boolean ERSandsIsntEnough = c.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge) < energyRechargeRequirements;
        if(needERSands && ERSandsIsntEnough)
            throw new IllegalArgumentException("Energy Recharge requirements cannot be met with mainstats alone");
        
        Character copy = c.clone();
        copy.unequipAllArtifacts();
        c.substats = new HashMap<>();
        Flower bestFlower = new Flower(5, 20);
        Feather bestFeather = new Feather(5, 20);
        Sands bestSands = new Sands(1, 0, Stat.ATKPercent);
        Goblet bestGoblet = new Goblet(1, 0, Stat.ATKPercent);
        Circlet bestCirclet = new Circlet(1, 0, Stat.ATKPercent);

        copy.equip(bestFlower);
        copy.equip(bestFeather);

        double bestComboDPR = 0;
        
        for(Stat sandsMainStat : needERSands ? List.of(Stat.EnergyRecharge) : Sands.allowlist()){
            for(Stat gobletMainStat : Goblet.allowlist()){
                for(Stat circletMainStat : Circlet.allowlist()){
                    copy.equip(new Sands(5, 20, sandsMainStat));
                    copy.equip(new Goblet(5, 20, gobletMainStat));
                    copy.equip(new Circlet(5, 20, circletMainStat));
                    double thisComboDPR = r.compute(copy);
                    if(thisComboDPR > bestComboDPR){
                        bestComboDPR = thisComboDPR;
                        bestSands = copy.sands().get();
                        bestGoblet = copy.goblet().get();
                        bestCirclet = copy.circlet().get();
                    }
                }
            }
        }
        copy.equip(bestSands);
        copy.equip(bestGoblet);
        copy.equip(bestCirclet);
        return new ArtifactBuilder(copy.flower().get(), copy.feather().get(), copy.sands().get(), copy.goblet().get(), copy.circlet().get());
    }


}   



record KQMSArtifactOptimizer (Rotation r, double energyRechargeRequirements) implements StatTableVisitor<ArtifactBuilder> {

    public ArtifactBuilder visitCharacter(Character c){
        Character copy = c.clone();
        copy.substats = new HashMap<>();
        copy.unequipAllArtifacts();
        copy.equip(new Flower(5, 20));
        copy.equip(new Feather(5, 20));
        Sands bestSands = new Sands(1, 0, Stat.ATKPercent);
        Goblet bestGoblet = new Goblet(1, 0, Stat.ATKPercent);
        Circlet bestCirclet = new Circlet(1, 0, Stat.ATKPercent);
        ArtifactBuilder bestSubs = new ArtifactBuilder();

        double base = r.compute(copy);

        List<Stat> useableStats = List.of(
            Stat.HPPercent,
            Stat.DEFPercent,
            Stat.ATKPercent,
            Stat.ElementalMastery,
            Stat.PhysicalDMGBonus,
            Stat.PyroDMGBonus,
            Stat.CryoDMGBonus,
            Stat.GeoDMGBonus,
            Stat.DendroDMGBonus,
            Stat.ElectroDMGBonus,
            Stat.DendroDMGBonus,
            Stat.ElectroDMGBonus,
            Stat.HydroDMGBonus,
            Stat.AnemoDMGBonus,
            Stat.HealingBonus,
            Stat.EnergyRecharge,
            Stat.CritDMG,
            Stat.CritRate)
            .stream()
            .filter(s -> r.compute(copy, StatTable.of(s, 1)) > base)
            .collect(Collectors.toList());

        var sandsList = Sands.allowlist().stream().filter(useableStats::contains).collect(Collectors.toList());
        var gobletList = Goblet.allowlist().stream().filter(useableStats::contains).collect(Collectors.toList());
        var circletList = Circlet.allowlist().stream().filter(useableStats::contains).collect(Collectors.toList());

        double bestComboDPR = 0;
        for(Stat sandsMainStat : sandsList){
            for(Stat gobletMainStat : gobletList){
                for(Stat circletMainStat : circletList){
                    copy.equip(new Sands(5, 20, sandsMainStat));
                    copy.equip(new Goblet(5, 20, gobletMainStat));
                    copy.equip(new Circlet(5, 20, circletMainStat));
                    var bob2 = Optimizer.optimialArtifactSubStatDistrbution(copy, r, energyRechargeRequirements);
                    copy.substats = bob2.stats();
                    
                    double thisComboDPR = r.compute(copy);

                    if(thisComboDPR > bestComboDPR){
                        bestComboDPR = thisComboDPR;
                        bestSands = copy.sands().get();
                        bestGoblet = copy.goblet().get();
                        bestCirclet = copy.circlet().get();
                        bestSubs = bob2;
                    }
                }
            }
        }
        c.equip(new Flower(5, 20));
        c.equip(new Feather(5, 20));
        c.equip(bestSands);
        c.equip(bestGoblet);
        c.equip(bestCirclet);
        c.substats = bestSubs.stats();

        

        return bestSubs;
    }


    // public ArtifactBuilder visitCharacter(Character c){
    //     c.substats = new HashMap<>();
        
    //     ArtifactBuilder mains = Optimizer.optimal5StarArtifactMainStats(c, r, energyRechargeRequirements);
        
    //     c.equip(mains.flower().get());
    //     c.equip(mains.feather().get());
    //     c.equip(mains.sands().get());
    //     c.equip(mains.goblet().get());
    //     c.equip(mains.circlet().get());

    //     if(c.get(Stat.EnergyRecharge) >= energyRechargeRequirements){
    //         ArtifactBuilder subs = Optimizer.optimialArtifactSubStatDistrbution(c, r, energyRechargeRequirements);
    //         c.substats = subs.stats();
    //         return subs;
    //     }

    //     ArtifactBuilder subs = new ArtifactBuilder();
    //     Optional<ArtifactBuilder> erSands = Optional.empty();
    //     Optional<ArtifactBuilder> anySands = Optional.empty();
    //     double DPRwithERSands = 0;
    //     double DPRwithOutERSands = 0;

    //     try{
    //         erSands = Optional.of(Optimizer.optimialArtifactSubStatDistrbution(c.equip(new Sands(5, 20, Stat.EnergyRecharge)), r, energyRechargeRequirements));
    //         final var ferSands = erSands.get().substats();
    //         DPRwithERSands = r.compute(c, ()->ferSands);
    //     }
    //     catch(Throwable e){ throw new IllegalArgumentException("Energy Recharge Requirements cannot be met with both er sands artifact substats"); } //if er sands and subs are not enough then just throw the exception

    //     try{
    //         anySands = Optional.of(Optimizer.optimialArtifactSubStatDistrbution(c.equip(mains.sands().get()), r, energyRechargeRequirements));
    //         final var fanySands = anySands.get().substats();
    //         DPRwithOutERSands = r.compute(c, ()->fanySands);
    //     }
    //     catch(IllegalArgumentException e){}//this means an er sands is needed, ignore the anySands case (DPRwithOutERSands will be 0)

    //     if(DPRwithERSands >= DPRwithOutERSands){
    //         subs = erSands.get();
    //         c.equip(new Sands(5, 20, Stat.EnergyRecharge)); 
    //     } 
    //     else {
    //         subs = anySands.get();
    //         c.equip(mains.sands().get());
    //     }
    //     c.substats = subs.stats();
    //     return subs;
    // }

    public ArtifactBuilder visitWeapon(Weapon w){
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public ArtifactBuilder visitArtifact(Artifact a){
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public ArtifactBuilder visitStatTable(StatTable s){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}