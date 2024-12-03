package com.github.lambdv.core;

import java.util.HashMap;

/**
 * Visitor object that provides operations/algorithms to optimize a character's stats and gear distribution for a given rotation.
 */
public interface Visitor<T> {
    T visitCharacter(Character c);
    T visitWeapon(Weapon w);
    T visitArtifact(Artifact a);
    T visitStatTable(StatTable s);
}

class CloneVisitor implements Visitor<StatTable> {
    public Character visitCharacter(Character c){
        var clone = new Character(c.name, 
            c.baseStats().getOrDefault(Stat.BaseHP, 0.0), 
            c.baseStats().getOrDefault(Stat.BaseATK, 0.0),
            c.baseStats().getOrDefault(Stat.BaseDEF, 0.0),
            c.ascensionStatType,
            c.baseStats().getOrDefault(c.ascensionStatType, 0.0)
        );
        c.weapon().ifPresent(w -> clone.equip(this.visitWeapon(w)));
        c.flower().ifPresent(f -> clone.equip(this.visitArtifact(f)));
        c.feather().ifPresent(f -> clone.equip(this.visitArtifact(f)));
        c.sands().ifPresent(s -> clone.equip(this.visitArtifact(s)));
        c.goblet().ifPresent(g -> clone.equip(this.visitArtifact(g)));
        c.circlet().ifPresent(ci -> clone.equip(this.visitArtifact(ci)));
        c.fluidStats().forEach((k,v) -> clone.add(k,v));
        c.dynamicFluidStats().forEach((k,v) -> 
            v.forEach(f -> clone.add(k,f))
        );
        c.substats().forEach((k,v) -> clone.add(k,v));
        return clone;
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
    public Artifact visitArtifact(Artifact a){
        return switch(a) {
            case Flower f -> new Flower(f.rarity(), f.level());
            case Feather f -> new Feather(f.rarity(), f.level());
            case Sands s -> new Sands(s.rarity(), s.level(), s.statType());
            case Goblet g -> new Goblet(g.rarity(), g.level(), g.statType());
            case Circlet c -> new Circlet(c.rarity(), c.level(), c.statType());
        };
    }

    public StatTable visitStatTable(StatTable s){
        return () -> new HashMap<>(s.stats());
    }
}

interface OptimizerVisitor<T> extends Visitor<T> {}

class KQMCArtifactMainAndSubs implements OptimizerVisitor<ArtifactBuilder> {
    private final Rotation r;
    private final double energyRechargeRequirements;

    public KQMCArtifactMainAndSubs(Rotation r, double energyRechargeRequirements){
        this.r = r;
        this.energyRechargeRequirements = energyRechargeRequirements;
    }

    public ArtifactBuilder visitCharacter(Character c){
        ArtifactBuilder mains = Optimizer.optimal5StarArtifactMainStats(c, r, energyRechargeRequirements);
        c.equip(mains.flower().get());
        c.equip(mains.feather().get());
        c.equip(mains.sands().get());
        c.equip(mains.goblet().get());
        c.equip(mains.circlet().get());
        var subs = Optimizer.optimialArtifactSubStatDistrbution(c, r, energyRechargeRequirements);
        c.substats = subs.stats();

        subs.equip(c.flower().get());
        subs.equip(c.feather().get());
        subs.equip(c.sands().get());
        subs.equip(c.goblet().get());
        subs.equip(c.circlet().get());

        return subs;
    }

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