package com.github.lambdv.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * Visitor object that provides operations/algorithms to optimize a character's stats and gear distribution for a given rotation.
 */
public interface StatTableVisitor<T> {
    T visitCharacter(Character c);
    T visitWeapon(Weapon w);
    T visitArtifact(Artifact a);
    T visitStatTable(StatTable s);
}

class CloneVisitor implements StatTableVisitor<StatTable> {
    public Character visitCharacter(Character c){

        var clone = new Character(c.name, 
            c.baseStats().getOrDefault(Stat.BaseHP, 0.0), 
            c.baseStats().getOrDefault(Stat.BaseATK, 0.0),
            c.baseStats().getOrDefault(Stat.BaseDEF, 0.0),
            c.ascensionStatType, c.ascensionStatAmount
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
        clone.setSubstats(c.substats());
        clone.setArtifactSet2Piece(c.artifactSet2Piece());
        clone.setArtifactSet4Piece(c.artifactSet4Piece());


        assert Arrays.stream(Stat.values())
            .allMatch(s -> c.get(s) == clone.get(s)) 
            : "Character stats not equal";
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

