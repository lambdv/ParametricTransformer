package dpscalc.core;
import java.nio.file.Path;
import java.util.*;

public record Weapon(String name, int rarity, int level, int refinement, double baseATK, Stat.type mainStatType, double mainStatAmount) implements Equippable {
    public Map<Stat.type, Double> stats(){
        return Map.of(mainStatType, mainStatAmount);
    }

    @Override public String toString(){
        return name + " " + rarity + " " + level + " " + refinement + " " + baseATK + " " + mainStatType + " " + mainStatAmount;
    }
}

