package com.github.lambdv.core;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Utility class that provides optimization algorithms for a given character and rotation.
 * @throws IllegalArgumentException if the energy recharge requirements cannot be met with substats alone
 */
public class Optimizer {
    public static ArtifactBuilder optimialArtifactSubStatDistrbution(final Character c, final Rotation r, double energyRechargeRequirements) throws IllegalArgumentException{

        var bob = ArtifactBuilder.KQMC(c.flower().get(), c.feather().get(), c.sands().get(), c.goblet().get(), c.circlet().get());
        var target = BuffedStatTable.of(c.build(), ()->bob.substats()); 

        while (target.get(Stat.EnergyRecharge) < energyRechargeRequirements)
            bob.roll(Stat.EnergyRecharge, Artifacts.RollQuality.AVG);

        var substattypes = ArtifactBuilder.possibleSubStats().toList();

        while (bob.numRollsLeft() > 0 && !substattypes.isEmpty()){
            Stat bestNextStatToRollInto = substattypes.stream()
                .filter(s->bob.numRollsLeft(s) > 0) //all the stats you can still roll into
                .<Map.Entry<Stat,Double>>map(s-> { 
                    bob.roll(s, Artifacts.RollQuality.AVG);
                    try{ return Map.entry(s, r.compute(target)); }
                    finally{ bob.unRoll(s); }
                })
                // .map(e-> {
                //     if(e.getValue() == 0) substattypes.remove(e.getKey());
                //     return e;
                // })
                .reduce((x,y) -> x.getValue() > y.getValue() ? x : y)
                .get().getKey();
            bob.roll(bestNextStatToRollInto, Artifacts.RollQuality.AVG);
        }
        return bob;
    }

    public static ArtifactBuilder optimal5StarArtifactMainStats(final Character c, final Rotation r, double energyRechargeRequirements){
        boolean needERSands = c.get(Stat.EnergyRecharge) < energyRechargeRequirements;
        boolean ERSandsIsntEnough = c.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge) < energyRechargeRequirements;
        if(needERSands && ERSandsIsntEnough)
            throw new IllegalArgumentException("Energy Recharge requirements cannot be met with substats alone");
        

        Character copy = c.clone();
        copy.unequipAllArtifacts();
        Flower bestFlower = new Flower(5, 20);
        Feather bestFeather = new Feather(5, 20);
        Sands bestSands = new Sands(5, 20, Stat.EnergyRecharge);
        Goblet bestGoblet = new Goblet(5, 20, Stat.ATKPercent);
        Circlet bestCirclet = new Circlet(5, 20, Stat.ATKPercent);

        copy.equip(bestFlower);
        copy.equip(bestFeather);

        double bestComboDPR = 0;
        
        for(Stat sandsMainStat : needERSands? List.of(Stat.EnergyRecharge): Sands.allowlist()){
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

    public static ArtifactBuilder optimalArtifacts(final Character c, final Rotation r, double energyRechargeRequirements){
        
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

}   



