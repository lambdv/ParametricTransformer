package com.github.ulambda.core;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.github.ulambda.utils.AssetManager;

/**
 * Artifact object represents an artifact in the game.
 * @note Artifacts are equippable to a character.
 * @note Artifacts are mutable stat tables that are built up and compiled into an immutable instance.
 * @note Artifacts are often built by using optimizers to maximize the character's rotation damage output.
 */
public class Artifact implements Equippable, MutableStatTable{
    public static enum ArtifactType { FLOWER, FEATHER, SANDS, GOBLET, CIRCLET };
    ArtifactSetBonus set;
    Artifact.ArtifactType type;
    int rarity;
    int level;
    Stat mainStatType;
    //double mainStatAmount;
    SubStats substats;


    public Artifact(Artifact.ArtifactType type, int level, int rarity, Stat mainStatType, ArtifactSetBonus set){
        this.type = type;
        this.level = level;
        this.rarity = rarity;
        this.mainStatType = mainStatType;
    }

    public double mainStatAmount(){
        return Artifacts.getMainStatValue(rarity, level, mainStatType);
    }
    
    public Map<Stat, Double> stats(){
        return Map.of(
            mainStatType, mainStatAmount(),
            substats.subStat1Type, substats.subStat1Amount,
            substats.subStat2Type, substats.subStat2Amount,
            substats.subStat3Type, substats.subStat3Amount,
            substats.subStat4Type, substats.subStat4Amount
        );
    }
}

/**
 * way to get main stat values for a given rarity and type
 */
class Artifacts{
    private static final JSONObject mainStatValues;
    private static final JSONObject subStatValues;
    static {
        try{
            mainStatValues = AssetManager.getJSONResource("artifactMainStats.json");
            subStatValues = AssetManager.getJSONResource("artifactSubStats.json");
        }
        catch(Throwable t){throw new RuntimeException(t);}
    }
    public static double getMainStatValue(int rarity, int level, Stat type){
        if(level < 0 || level > 20) throw new IllegalArgumentException("Level must be between 0 and 20");
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return mainStatValues.getJSONObject(rarity+"star").getJSONArray(type.toString()).getDouble(level);
    }
    public static double getSubStatValue(int rarity, Stat type){
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return subStatValues.getJSONObject(rarity+"star").getDouble(type.toString());
    }
}

enum RollQuality { 
    MAX(1),
    HIGH(0.9),
    MID(0.8),
    LOW(0.7),
    AVG((1 + 0.9 + 0.8 + 0.7)/4); //this is what KQMC uses
    double multiplier;
    RollQuality(double multiplier){ this.multiplier = multiplier; }
};


class SubStats{
    int rarity;
    int numRolls;
    Stat subStat1Type;
    double subStat1Amount;
    Stat subStat2Type;
    double subStat2Amount;
    Stat subStat3Type;
    double subStat3Amount;
    Stat subStat4Type;
    double subStat4Amount;

    public SubStats(int rarity, Stat subStat1Type, RollQuality subStat1RollQuality, Stat subStat2Type, RollQuality subStat2RollQuality, Stat subStat3Type, RollQuality subStat3RollQuality, Stat subStat4Type, RollQuality subStat4RollQuality){
        this.rarity = rarity;
        numRolls = 0;
        this.subStat1Type = subStat1Type;
        this.subStat2Type = subStat2Type;
        this.subStat3Type = subStat3Type;
        this.subStat4Type = subStat4Type;
        subStat1Amount = Artifacts.getSubStatValue(rarity, subStat1Type) * subStat1RollQuality.multiplier;
        subStat2Amount = Artifacts.getSubStatValue(rarity, subStat2Type) * subStat2RollQuality.multiplier;
        subStat3Amount = Artifacts.getSubStatValue(rarity, subStat3Type) * subStat3RollQuality.multiplier;
        subStat4Amount = Artifacts.getSubStatValue(rarity, subStat4Type) * subStat4RollQuality.multiplier;
    }

    public void rollSubStat(Stat stat, RollQuality quality){
        if(stat.equals(Stat.None)) throw new IllegalArgumentException("Stat cannot be None");
        if(numRolls < 10) 
            throw new IllegalArgumentException("Too many rolls");
        double base = 7.77; //get this from the table
        if(subStat1Type.equals(stat))
            subStat1Amount += base * quality.multiplier;
        else if(subStat2Type.equals(stat))
            subStat2Amount += base * quality.multiplier;
        else if(subStat3Type.equals(stat))
            subStat3Amount += base * quality.multiplier;
        else if(subStat4Type.equals(stat))
            subStat4Amount += base * quality.multiplier;
        else throw new IllegalArgumentException("Stat not in substats");
        numRolls++;
    }

}


record ArtifactSetBonus(Map<Stat, Double> twoPiece, Map<Stat,Double> fourPiece){}