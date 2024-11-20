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

public interface Artifact extends Equippable{
    ArtifactSet set();
    int rarity();
    int level();
    Stat statType();
    default StatTable substats(){return () -> Map.of();}
    default double statValue(){ 
        return Artifacts.getMainStatValue(rarity(), level(), statType()); 
    }
    default Map<Stat, Double> stats() {
        return Map.of(statType(), statValue());
    }
}

record Flower(ArtifactSet set, int rarity, int level) implements Artifact{
    public Flower{assert Artifacts.checkCorrectLevelForRarity(level, rarity);}
    public Stat statType(){return Stat.FlatHP;}
}
record Feather(ArtifactSet set, int rarity, int level) implements Artifact{
    public Feather{assert Artifacts.checkCorrectLevelForRarity(level, rarity);}
    public Stat statType(){return Stat.FlatATK;}
}

record Sands(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact{
    public Sands{
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if(!allowlist().contains(statType)) 
            throw new IllegalArgumentException(statType + "is an invalid stat type for " + this.getClass());
    }
    private static List<Stat> allowlist(){
        return List.of(
            Stat.HPPercent,
            Stat.DEFPercent,
            Stat.ATKPercent,
            Stat.EnergyRecharge,
            Stat.ElementalMastery
        );
    }
}

record Goblet(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact{
    public Goblet{
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if(!allowlist().contains(statType)) 
            throw new IllegalArgumentException(statType + "is an invalid stat type for " + this.getClass());
    }
    private static List<Stat> allowlist(){
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
            Stat.HydroDMGBonus,
            Stat.AnemoDMGBonus,
            Stat.HealingBonus
        );
    }
}
record Circlet(ArtifactSet set, int rarity, int level, Stat statType) implements Artifact {
    public Circlet {
        assert Artifacts.checkCorrectLevelForRarity(level, rarity);
        if (!allowlist().contains(statType))
            throw new IllegalArgumentException(statType + " is an invalid stat type for " + this.getClass());
    }
    private static List<Stat> allowlist() {
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


record ArtifactSet(
    String setName,
    Map<Stat, Double> twoPiece, Map<Stat,Double> fourPiece
){
    public static ArtifactSet empty(){
        return new ArtifactSet("", Map.of(), Map.of());
    }
}

class ArtifactSubStats implements StatTable{
    
    Map<Stat, Double> substats;

    public Map<Stat, Double> stats() {
        return Collections.unmodifiableMap(substats);
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