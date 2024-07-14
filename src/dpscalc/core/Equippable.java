package dpscalc.core;

public interface Equippable<T extends Stat> {
    T mainStat();
}

record Weapon<T extends Stat>(String name, int rarity, int level, int refinement, double baseATK, T mainStat) implements Equippable<T>{
    Weapon {

    }
    //passive stats    
}

class Artifact<T extends Stat> implements Equippable<T>{
    
    public enum type {FLOWER, FEATHER, SANDS, GOBLET, CIRCLET };

    String set;
    Artifact.type type;
    int rarity;
    int level;
    T mainStat;

    private class SubStats{
        //int numRolls;
        Stat substat1;
        Stat substat2;
        Stat substat3;
        Stat substat4;
    }

    SubStats substats;

    public Artifact(String set, Artifact.type type, int rarity, int level, T mainStat){
        this.set = set;
        this.type = type;
        this.rarity = rarity;
        this.level = level;
        this.mainStat = mainStat;
    }

    public T mainStat(){
        return mainStat;
    }
}
