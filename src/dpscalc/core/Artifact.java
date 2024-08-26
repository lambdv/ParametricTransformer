package dpscalc.core;
import java.util.Map;

public class Artifact implements Equippable{
    public static enum type {FLOWER, FEATHER, SANDS, GOBLET, CIRCLET };
    String set;
    Artifact.type type;
    int rarity;
    int level;
    double mainStat;
    private class SubStats{
        //int numRolls;
        Stat substat1;
        Stat substat2;
        Stat substat3;
        Stat substat4;
    }
    SubStats substats;
    public Artifact(String set, Artifact.type type, int rarity, int level, double mainStat){
        this.set = set;
        this.type = type;
        this.rarity = rarity;
        this.level = level;
        this.mainStat = mainStat;
    }
    public Map<Stat.type, Double> stats(){
        return Map.of(Stat.type.HPPercent, mainStat);
    }
}