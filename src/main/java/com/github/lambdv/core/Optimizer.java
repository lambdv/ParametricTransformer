package com.github.lambdv.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that provides optimization algorithms for a given character and rotation.
 * @throws IllegalArgumentException if the energy recharge requirements cannot be met with substats alone
 */
public class Optimizer {
    public static StatTable optimialArtifactSubStatDistrbution(final Character c, final Rotation r, double energyRechargeRequirements) throws IllegalArgumentException{
        var bob = ArtifactBuilder.KQMC(c.flower().get(), c.feather().get(), c.sands().get(), c.goblet().get(), c.circlet().get());
        var target = new BuffedStatTable(c.build(), ()->bob.substats()); 

        try{
            while (target.get(Stat.EnergyRecharge) < energyRechargeRequirements)
                bob.roll(Stat.EnergyRecharge, Artifacts.RollQuality.AVG);
        }
        catch(AssertionError e){
            throw new IllegalArgumentException("Energy Recharge requirements cannot be met with substats alone");
        }

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
        return ()->bob.substats();
    }

    public static ArtifactBuilder optimal5StarArtifactMainStats(final Character c, final Rotation r, double energyRechargeRequirements){
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
        
        for(Stat sandsMainStat : Sands.allowlist()){
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
        //return new ArtifactBuilder(copy.flower().get(), copy.feather().get(), copy.sands().get(), copy.goblet().get(), copy.circlet().get());
        return new ArtifactBuilder();
    }

    public static ArtifactBuilder optimalArtifacts(final Character c, final Rotation r, double energyRechargeRequirements){
        
        
        throw new UnsupportedOperationException("Not yet implemented");
    }

}   





/**
 * Visitor object that provides operations/algorithms to optimize a character's stats and gear distribution for a given rotation.
 */
interface Visitor<T> {
    T visitCharacter(Character c);
    T visitWeapon(Weapon w);
    T visitArtifact(Artifact a);
}

class CloneVisitor implements Visitor<StatTable> {
    public Character visitCharacter(Character c){
        return c.clone();
    }
    public Weapon visitWeapon(Weapon w){
        if(Weapons.isCached(w)) 
            return Weapons.of(w.name());
        return new Weapon(
            w.name(),
            w.rarity(),
            w.level(),
            w.refinement(),
            w.baseATK(),
            w.mainStatType(),
            w.mainStatAmount()
        );
    }
    public Character visitArtifact(Artifact a){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

class KQMCArtifactMainAndSubs implements Visitor<ArtifactBuilder> {
    private final Rotation r;
    private final double energyRechargeRequirements;

    public KQMCArtifactMainAndSubs(Rotation r, double energyRechargeRequirements){
        this.r = r;
        this.energyRechargeRequirements = energyRechargeRequirements;
    }

    public ArtifactBuilder visitCharacter(Character c){
        var subs = Optimizer.optimialArtifactSubStatDistrbution(c, r, energyRechargeRequirements);
        var mains = Optimizer.optimal5StarArtifactMainStats(c, r, energyRechargeRequirements);
        c.add(subs);
        c.add(mains);
        if(1 != 100) throw new AssertionError("This is a test");
        return mains;
    }

    public ArtifactBuilder visitWeapon(Weapon w){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArtifactBuilder visitArtifact(Artifact a){
        throw new UnsupportedOperationException("Not yet implemented");
    }
}