package com.github.ulambda.core;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Top level type for data structures that stores stat-amount pairs.
 */
public interface StatTable{
    Map<Stat, Double> stats();
    public default double getStat(Stat type){
        return stats().getOrDefault(type, 0.0);
    }
}
/**
 * StatTables where values can be modified after creation.
 */
interface MutableStatTable extends StatTable{
    public default double addStat(Stat type, double amount){
        return stats().merge(type, amount, Double::sum);
    }
    public default double setStat(Stat type, double amount){
        return stats().put(type, amount);
    }
}

/**
 * StatTables that can be equipped to a character.
 */
interface Equippable extends StatTable{}

class StatTables{
    public static Map<Stat, Double> merge(Stream<Map<Stat, Double>> statTableMaps){
        return statTableMaps
            .<Map.Entry<Stat, Double>>mapMulti((map, c)->map.forEach((k, v)->c.accept(Map.entry(k, v))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
    }
}