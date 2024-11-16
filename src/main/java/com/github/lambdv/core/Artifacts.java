package com.github.lambdv.core;
import org.json.JSONObject;

import com.github.lambdv.utils.AssetManager;
/**
 * Utility class for getting artifact stat values
 */
public class Artifacts{
    private static final JSONObject mainStatValues;
    private static final JSONObject subStatValues;
    private Artifacts(){}
    static {
        try{
            mainStatValues = AssetManager.getJSONResource("artifactMainStats.json");
            subStatValues = AssetManager.getJSONResource("artifactSubStats.json");
        }
        catch(Throwable t){throw new RuntimeException(t);}
    }
    public static double getMainStatValue(int rarity, int level, Stat type){
        if(mainStatValues.equals(null)) throw new RuntimeException("mainStatValues is null");
        if (type.equals(Stat.None)) return 0;
        if(!Artifacts.checkCorrectLevelForRarity(level, rarity)) throw new IllegalArgumentException("Invalid level for rarity");
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return mainStatValues.getJSONObject(rarity+"star").getJSONArray(type.toString()).getDouble(level);
    }
    public static double getSubStatValue(int rarity, Stat type){
        if(subStatValues.equals(null)) throw new RuntimeException("subStatValues is null");
        if (type.equals(Stat.None)) return 0;
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return subStatValues.getJSONObject(rarity+"star").getDouble(type.toString());
    }

    public static boolean checkCorrectLevelForRarity(int level, int rarity){
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


    enum RollQuality { 
        MAX(1),
        HIGH(0.9),
        MID(0.8),
        LOW(0.7),
        AVG((1+0.9+0.8+0.7)/4); //this is what KQMC uses
        //4-star artifacts if used have a x0.8 stat modifier, and a penalty of -2 distributed substats per 4-star artifact
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
    
}