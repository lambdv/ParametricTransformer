package dpscalc.core;
import java.util.*;
import java.util.Optional;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Instance representation of a character's total stats.
 */
public class Character {
    private final Map<Stat.type, Double> baseStats; 
    private Map<Stat.type, Double> fluidStats; 

    private Optional<Weapon> weapon = Optional.empty();
    private Optional<Artifact> flower = Optional.empty();
    private Optional<Artifact> feather = Optional.empty();
    private Optional<Artifact> sands = Optional.empty();
    private Optional<Artifact> goblet = Optional.empty();
    private Optional<Artifact> circlet = Optional.empty();

    private Runnable observers = () -> {};

    public Character(double baseHP, double baseATK, double baseDEF, Stat ascensionStat){
        baseStats = new HashMap<>();
        fluidStats = new HashMap<>();
        
        baseStats.put(Stat.type.BaseHP, baseHP);
        baseStats.put(Stat.type.BaseATK, baseATK);
        baseStats.put(Stat.type.BaseDEF, baseDEF);
        baseStats.put(ascensionStat.type(), ascensionStat.amount());
        baseStats.put(Stat.type.EnergyRecharge, 1.00);
        baseStats.put(Stat.type.CritRate, 0.05);
        baseStats.put(Stat.type.CritDMG, 0.5);
    }

    public void addStat(Stat.type type, double amount){
        fluidStats.merge(type, amount, Double::sum);
        notifyObservers();
    }

    public double getStat(Stat.type stat){
        double fromBase = baseStats.getOrDefault(stat, 0.0);
        double fromFluid = fluidStats.getOrDefault(stat, 0.0);
        return fromBase + fromFluid;
    }

    public void addObserver(Runnable observer){
        this.observers = () -> {
            observer.run();
            this.observers.run();
        };
    }

    private void notifyObservers(){
        this.observers.run();
    }
}

interface Characters {
    public static Character of(String name){
        throw new UnsupportedOperationException("Not implemented");
    }
}