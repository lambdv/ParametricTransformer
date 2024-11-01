package com.github.ulambda.core;
import java.util.Map;

public class Artifact implements Equippable{
    public static enum type { FLOWER, FEATHER, SANDS, GOBLET, CIRCLET };
    String set;
    Artifact.type type;
    int rarity;
    int level;
    Stat mainStatType;
    double mainStatAmount;
    SubStats substats;
    
    public class SubStats{
        int numRolls;
        Stat subStat1Type;
        double subStat1Amount;
        Stat subStat2Type;
        double subStat2Amount;
        Stat subStat3Type;
        double subStat3Amount;
        Stat subStat4Type;
        double subStat4Amount;
    }
    
    public Map<Stat, Double> stats(){
        return Map.of(
            mainStatType, mainStatAmount,
            substats.subStat1Type, substats.subStat1Amount,
            substats.subStat2Type, substats.subStat2Amount,
            substats.subStat3Type, substats.subStat3Amount,
            substats.subStat4Type, substats.subStat4Amount
        );
    }
}
