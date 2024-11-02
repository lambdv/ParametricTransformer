package com.github.ulambda.core;
import java.util.Map;

/**
 * Artifact object represents an artifact in the game.
 * @note Artifacts are equippable to a character.
 * @note Artifacts are mutable stat tables that are built up and compiled into an immutable instance.
 * @note Artifacts are often built by using optimizers to maximize the character's rotation damage output.
 */
public class Artifact implements Equippable, MutableStatTable{
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
