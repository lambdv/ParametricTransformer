package com.github.lambdv.core;
import java.util.Random;

import org.json.JSONObject;

import com.github.lambdv.utils.AssetManager;
/**
 * Utility class for getting artifact stat values
 */
public class Artifacts{
    private static final JSONObject mainStatValues;
    private static final JSONObject subStatValues;
    static {
        try{
            mainStatValues = AssetManager.getJSONResource("artifactMainStats.json");
            subStatValues = AssetManager.getJSONResource("artifactSubStats.json");
        } 
        catch(Throwable t){
            throw new Error("Artifacts util class could not initialize main and substat values from json db: " + t);
        }
    }
    public static double getMainStatValue(int rarity, int level, Stat type){
        assert type != null;
        assert type != Stat.None;
        if(!Artifacts.checkCorrectLevelForRarity(level, rarity)) 
            throw new IllegalArgumentException("Invalid level for rarity");
        if(rarity < 1 || rarity > 5) 
            throw new IllegalArgumentException("Rarity must be between 1 and 5");
        
        String typeAsString = type.isElementalDMGBonus() ? "ElementalDMGPercent" : type.toString();
        return mainStatValues.getJSONObject(rarity+"star").getJSONArray(typeAsString).getDouble(level);
    }
    public static double getSubStatValue(int rarity, Stat type){
        assert type != null;
        assert type != Stat.None;
        if(rarity < 1 || rarity > 5) 
            throw new IllegalArgumentException("Rarity must be between 1 and 5");

        return subStatValues.getJSONObject(rarity+"star").getDouble(type.toString());
    }

    public static boolean checkCorrectLevelForRarity(int level, int rarity) throws IllegalArgumentException{
        if (level < 0) return false;
        return switch (rarity){
            case 1 -> level <= 4;
            case 2 -> level <= 4;
            case 3 -> level <= 12;
            case 4 -> level <= 16;
            case 5 -> level <= 20;
            default -> false;
        };
    }

    public static void substatTypeIsAllowListed(Artifact artifact) throws IllegalArgumentException{
        if(artifact instanceof Flower || artifact instanceof Feather) return;

        if (artifact instanceof Sands s && Sands.allowlist().contains(s.statType())) return;
        if (artifact instanceof Goblet g && Goblet.allowlist().contains(g.statType())) return;
        if (artifact instanceof Circlet c && Circlet.allowlist().contains(c.statType())) return;

        throw new IllegalArgumentException(artifact.statType() + " is an invalid stat type for " + artifact.getClass());
    }

    enum RollQuality { 
        MAX(1),
        HIGH(0.9),
        MID(0.8),
        LOW(0.7),
        AVG((1+0.9+0.8+0.7)/4); //this is what KQMC uses
        double multiplier;
        RollQuality(double multiplier){ this.multiplier = multiplier; }
    };

    /**
     * maximum number of rolls possible for a given artifact
     * @note max assumes starts with all subs it can start with. for worse case sernario -1
     * @param artifact
     * @return
     */
    public static int maxRollsFor(Artifact artifact) {
        return artifact.rarity()-1 + artifact.level()/4;
    }
    private Artifacts(){}
}