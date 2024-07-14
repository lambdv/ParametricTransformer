package dpscalc.core;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

public class Character {
    private StatTable baseStats;
    private StatTable fluidStats;

    // private Optional<Integer> weapon = Optional.empty();
    // private Optional<Integer> flower = Optional.empty();
    // private Optional<Integer> feather = Optional.empty();
    // private Optional<Integer> sands = Optional.empty();
    // private Optional<Integer> goblet = Optional.empty();
    // private Optional<Integer> circlet = Optional.empty();

    private Set<Equippable<?>> equipment = new HashSet<>();

    //talent levels
    int autoATKTL = 9;
    int skillTL = 9;
    int burstTL = 9;

    //constellation
    int constellation = 0;

    //constraints


    public Character(double baseHP, double baseATK, double baseDEF, Stat.type ascensionStatType, double ascensionStatValue){
        baseStats = new StatTable();
        fluidStats = new StatTable();

        baseStats.add(Stat.type.BASEHP, baseHP);
        baseStats.add(Stat.type.BASEATK, baseATK);
        baseStats.add(Stat.type.BASEDEF, baseDEF);
        baseStats.add(ascensionStatType, ascensionStatValue);
        baseStats.add(Stat.type.ENERGYRECHARGE, 1.00);
        baseStats.add(Stat.type.CRITRATE, 0.05);
        baseStats.add(Stat.type.CRITDMG, 0.5);
    }

    public void addStat(){

    }

    public double getStat(Stat.type stat){

        return 0;
    }

}
