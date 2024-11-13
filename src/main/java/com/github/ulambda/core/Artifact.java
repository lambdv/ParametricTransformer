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
        assert Artifacts.levelCheck(level, rarity);
        assert rarity >= 1 && rarity <= 5;
        this.type = type;
        this.level = level;
        this.rarity = rarity;
        this.mainStatType = mainStatType;
        this.set = set;
        substats = new SubStats(this, 
            Stat.None,
            RollQuality.AVG, 
            Stat.None, 
            RollQuality.AVG, 
            Stat.None, 
            RollQuality.AVG, 
            Stat.None, 
            RollQuality.AVG);
    }

    public void set(SubStats substats){
        this.substats = substats;
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

class Flower extends Artifact{
    public Flower(int level, int rarity, ArtifactSetBonus set){
        super(ArtifactType.FLOWER, level, rarity, Stat.FlatHP, set);

    }
}

class Feather extends Artifact{
    public Feather(int level, int rarity, ArtifactSetBonus set){
        super(ArtifactType.FEATHER, level, rarity, Stat.FlatATK, set);
    }
}

class Sands extends Artifact{
    public Sands(int level, int rarity, Stat mainStatType, ArtifactSetBonus set){
        super(ArtifactType.SANDS, level, rarity, mainStatType, set);
        assert mainStatType.equals(Stat.HPPercent) ||
            mainStatType.equals(Stat.DEFPercent) ||
            mainStatType.equals(Stat.ATKPercent) ||
            mainStatType.equals(Stat.EnergyRecharge) ||
            mainStatType.equals(Stat.ElementalMastery);
    }
}

class Goblet extends Artifact{
    public Goblet(int level, int rarity, Stat mainStatType, ArtifactSetBonus set){
        super(ArtifactType.GOBLET, level, rarity, mainStatType, set);
        assert mainStatType.equals(Stat.HPPercent) ||
            mainStatType.equals(Stat.DEFPercent) ||
            mainStatType.equals(Stat.ATKPercent) ||
            mainStatType.equals(Stat.ElementalMastery) ||
            mainStatType.equals(Stat.PhysicalDMGBonus) ||
            mainStatType.equals(Stat.PyroDMGBonus) ||
            mainStatType.equals(Stat.CryoDMGBonus) ||
            mainStatType.equals(Stat.GeoDMGBonus) ||
            mainStatType.equals(Stat.DendroDMGBonus) ||
            mainStatType.equals(Stat.ElectroDMGBonus) ||
            mainStatType.equals(Stat.HydroDMGBonus) ||
            mainStatType.equals(Stat.AnemoDMGBonus);
            mainStatType.equals(Stat.HealingBonus);
    }
}

class Circlet extends Artifact{
    public Circlet(int level, int rarity, Stat mainStatType, ArtifactSetBonus set){
        super(ArtifactType.CIRCLET, level, rarity, mainStatType, set);
        assert mainStatType.equals(Stat.CritRate) ||
            mainStatType.equals(Stat.CritDMG) ||
            mainStatType.equals(Stat.HPPercent) ||
            mainStatType.equals(Stat.DEFPercent) ||
            mainStatType.equals(Stat.ATKPercent) ||
            mainStatType.equals(Stat.ElementalMastery) ||
            mainStatType.equals(Stat.HealingBonus);
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
        if(mainStatValues.equals(null)) throw new RuntimeException("mainStatValues is null");
        if (type.equals(Stat.None)) return 0;
        if(!Artifacts.levelCheck(level, rarity)) throw new IllegalArgumentException("Invalid level for rarity");
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return mainStatValues.getJSONObject(rarity+"star").getJSONArray(type.toString()).getDouble(level);
    }
    public static double getSubStatValue(int rarity, Stat type){
        if(subStatValues.equals(null)) throw new RuntimeException("subStatValues is null");
        if (type.equals(Stat.None)) return 0;
        if(rarity < 1 || rarity > 5) throw new IllegalArgumentException("Rarity must be between 1 and 5");
        return subStatValues.getJSONObject(rarity+"star").getDouble(type.toString());
    }

    public static boolean levelCheck(int level, int rarity){
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


class SubStats implements StatTable{
    final Artifact parent;
    int rarity;
    int numRolls;
    Stat subStat1Type = Stat.None;
    double subStat1Amount = 0;
    Stat subStat2Type = Stat.None;
    double subStat2Amount = 0;
    Stat subStat3Type = Stat.None;
    double subStat3Amount = 0;
    Stat subStat4Type = Stat.None;
    double subStat4Amount = 0;
    

    public Map<Stat, Double> stats(){
        return Map.of(
            subStat1Type, subStat1Amount,
            subStat2Type, subStat2Amount,
            subStat3Type, subStat3Amount,
            subStat4Type, subStat4Amount
        );
    }

    /**
     * 
        Rarity	Max LV	Base # of
        Sub Stats	# of Unlocks	# of Upgrades
        1 Star	+4	0	1	0
        2 Stars	+4	0-1	1	0
        3 Stars	+12	1-2	2-3	0-1
        4 Stars	+16	2-3	1-2	2-3
        5 Stars	+20	3-4	0-1	4-5
     */


    public SubStats(Artifact parent, Stat subStat1Type, RollQuality subStat1RollQuality, Stat subStat2Type, RollQuality subStat2RollQuality, Stat subStat3Type, RollQuality subStat3RollQuality, Stat subStat4Type, RollQuality subStat4RollQuality){
        this.parent = parent;
        this.rarity = parent.rarity;
        numRolls = 0;

        assert subStat1Type != parent.mainStatType;
        assert subStat2Type != parent.mainStatType;
        assert subStat3Type != parent.mainStatType;
        assert subStat4Type != parent.mainStatType;

        this.subStat1Type = subStat1Type;
        this.subStat2Type = subStat2Type;
        this.subStat3Type = subStat3Type;
        this.subStat4Type = subStat4Type;
        subStat1Amount = Artifacts.getSubStatValue(rarity, subStat1Type) * subStat1RollQuality.multiplier;
        subStat2Amount = Artifacts.getSubStatValue(rarity, subStat2Type) * subStat2RollQuality.multiplier;
        subStat3Amount = Artifacts.getSubStatValue(rarity, subStat3Type) * subStat3RollQuality.multiplier;
        subStat4Amount = Artifacts.getSubStatValue(rarity, subStat4Type) * subStat4RollQuality.multiplier;
    }

    public static SubStats of(Artifact parent){
        assert parent.rarity == 1 || parent.rarity == 2;
        return new SubStats(parent, Stat.FlatHP, RollQuality.AVG, Stat.FlatATK, RollQuality.AVG, Stat.FlatDEF, RollQuality.AVG, Stat.None, RollQuality.AVG);
    }


    public static SubStats of(Artifact parent, Stat stat, RollQuality quality){
        assert parent.rarity == 2 || parent.rarity == 3;
        return null;
    }

    public static SubStats of(Artifact parent, Stat stat, RollQuality quality, Stat stat2, RollQuality quality2){
        assert parent.rarity == 3 || parent.rarity == 4;
        return null;
    }

    public static SubStats of(Artifact parent, Stat stat, RollQuality quality, Stat stat2, RollQuality quality2, Stat stat3, RollQuality quality3){
        assert parent.rarity == 4 || parent.rarity == 5;
        return null;
    }

    public static SubStats of(Artifact parent, Stat stat, RollQuality quality, Stat stat2, RollQuality quality2, Stat stat3, RollQuality quality3, Stat stat4, RollQuality quality4){
        assert parent.rarity == 5;
        return null;
    }




    public void rollSubStat(Stat stat, RollQuality quality){
        if(stat.equals(Stat.None)) throw new IllegalArgumentException("Stat cannot be None");
        if(numRolls < 10) throw new IllegalArgumentException("Too many rolls");
        double base = Artifacts.getSubStatValue(rarity, stat);
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

    public void rollSubStat(int i, RollQuality quality){
        assert i >= 1 && i <= 4;
        assert numRolls < 10;
        double base = Artifacts.getSubStatValue(rarity, switch(i){
            case 1 -> subStat1Type;
            case 2 -> subStat2Type;
            case 3 -> subStat3Type;
            case 4 -> subStat4Type;
            default -> throw new IllegalArgumentException("Invalid substat index");
        });
        switch(i){
            case 1 -> subStat1Amount += base * quality.multiplier;
            case 2 -> subStat2Amount += base * quality.multiplier;
            case 3 -> subStat3Amount += base * quality.multiplier;
            case 4 -> subStat4Amount += base * quality.multiplier;
        }
        numRolls++;
    }

}


record ArtifactSetBonus(Map<Stat, Double> twoPiece, Map<Stat,Double> fourPiece){}

class ArtifactTable{
    Artifact flower;
    Artifact feather;
    Artifact sands;
    Artifact goblet;
    Artifact circlet;
    
    SubStats flowerSubs;
    SubStats featherSubs;
    SubStats sandsSubs;
    SubStats gobletSubs;
    SubStats circletSubs;
    
    
}