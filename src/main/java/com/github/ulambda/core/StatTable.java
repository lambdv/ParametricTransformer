package com.github.ulambda.core;
import java.util.Map;
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
 * StatTables where values cannot be modified after creation.
 */
interface ImmutableStatTable extends StatTable{}

/**
 * StatTables that can be equipped to a character.
 */
interface Equippable extends StatTable{}
