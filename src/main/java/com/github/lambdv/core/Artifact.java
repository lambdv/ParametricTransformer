package com.github.lambdv.core;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONObject;

import com.github.lambdv.core.Artifacts;
import com.github.lambdv.utils.AssetManager;

/**
 * Object that represents an artifact piece.
 */
public sealed interface Artifact extends StatTable permits Flower, Feather, Sands, Goblet, Circlet {
    ArtifactSet set();
    int rarity();
    int level();
    Stat statType();
    //default Map<Stat, Double> substats() {return Map.of();}
    default double statValue(){ 
        return Artifacts.getMainStatValue(rarity(), level(), statType()); 
    }
    default Map<Stat, Double> stats() {
        return Map.of(statType(), statValue());
    }
}

/**
 * Object that represents a flower artifact piece and specifies the rarity and level.
 * mainstat is always assumed to be flat HP.
 */
record Flower(ArtifactSet set, int rarity, int level) implements Artifact{
    public Flower{ assert Artifacts.checkCorrectLevelForRarity(level, rarity); }
    public Flower(int rarity , int level){ this(ArtifactSet.empty(), rarity, level); }
    public Stat statType(){return Stat.FlatHP;}
}

/**
 * Object that represents a feather artifact piece and specifies the rarity and level.
 * mainstat is always assumed to be flat ATK.
 */
record Feather(ArtifactSet set, int rarity, int level) implements Artifact{
    public Feather{ assert Artifacts.checkCorrectLevelForRarity(level, rarity); }
    public Feather(int rarity , int level){ this(ArtifactSet.empty(), rarity, level); }
    public Stat statType(){return Stat.FlatATK;}
}

/**
 * Object that represents a sands artifact piece and specifies the rarity, level, and stat type.
 */
record Sands(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact{
    public Sands{
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if(!allowlist().contains(statType)) throw new IllegalArgumentException(statType + "is an invalid stat type for " + this.getClass());
    }
    public Sands(int rarity , int level, Stat statType){
        this(ArtifactSet.empty(), rarity, level, statType);
    }
    public static List<Stat> allowlist(){
        return List.of(
            Stat.HPPercent,
            Stat.DEFPercent,
            Stat.ATKPercent,
            Stat.EnergyRecharge,
            Stat.ElementalMastery
        );
    }
}

/**
 * Object that represents a goblet artifact piece and specifies the rarity, level, and stat type.
 */
record Goblet(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact{
    public Goblet{
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if(!allowlist().contains(statType)) throw new IllegalArgumentException(statType + "is an invalid stat type for " + this.getClass());
    }
    public Goblet(int rarity , int level, Stat statType){
        this(ArtifactSet.empty(), rarity, level, statType);
    }
    public static List<Stat> allowlist(){
        return List.of(
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
            Stat.HealingBonus
        );
    }
}

/**
 * Object that represents a circlet artifact piece and specifies the rarity, level, and stat type.
 */
record Circlet(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact {
    public Circlet {
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if (!allowlist().contains(statType)) throw new IllegalArgumentException(statType + " is an invalid stat type for " + this.getClass());
    }

    public Circlet(int rarity , int level, Stat statType){
        this(ArtifactSet.empty(), rarity, level, statType);
    }

    public static List<Stat> allowlist() {
        return List.of(
            Stat.CritRate,
            Stat.CritDMG,
            Stat.HPPercent,
            Stat.DEFPercent,
            Stat.ATKPercent,
            Stat.ElementalMastery,
            Stat.HealingBonus
        );
    }
}