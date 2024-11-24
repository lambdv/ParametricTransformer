package com.github.lambdv.core;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Top level type for data structures that stores stat-amount pairs.
 * @note stats() may or many not return an immutable or defensive cloned map. you should assume you cannot directly modify the map.
 * @note StatTables are not inherently immutable, its just that you cannot directly specify what stat-amouns to add or set.
 */
public interface StatTable{
    /**
     * Map of stat-amount pairs.
     * @return
     */
    Map<Stat, Double> stats();

    /**
     * Get the amount value of a stat type stored in the stat table.
     * will return 0.0 if the stat type is not found in the table.
     * @param type
     * @return
     */
    public default double getStat(Stat type){
        return stats().getOrDefault(type, 0.0);
    }

    public default double get(Stat type){
        return getStat(type);
    }
    
    /**
     * Compile the stat table into an immutable stat table.
     * @return
     */
    public default StatTable build(){
        return () -> Collections.unmodifiableMap(stats());
    }

    /**
     * Get stream of stat-amount pairs from the stat table.
     * @return
     */
    public default Stream<Map.Entry<Stat, Double>> stream(){
        return stats().entrySet().stream();
    }

}
// /**
//  * StatTables where values can be directly modified after creation.
//  * @note contract for a mutable stat table is that stats() should return a mutable map to allow add and set methods to directly modify the map.
//  */
// interface MutableStatTable extends StatTable{
//     /**
//      * Method to add a stat to the table.
//      * @param type
//      * @param amount
//      * @return
//      */
//     public default double addStat(Stat type, double amount){
//         return stats().merge(type, amount, Double::sum);
//     }
//     /**
//      * Method to set a stat to the table.
//      * @param type
//      * @param amount
//      * @return
//      */
//     public default double setStat(Stat type, double amount){
//         return stats().put(type, amount);
//     }
// }

/**
 * StatTables that can be equipped to a character.
 */
interface Equippable extends StatTable{}

/**
 * Utility class for merging multiple stat tables into one.
 */
class StatTables {
    private StatTables() {}
    @SafeVarargs 
    public static <T extends Map<Stat, Double>> StatTable merge(T... statTablesMap) {
        return () -> Arrays.stream(statTablesMap)
            .<Map.Entry<Stat, Double>>mapMulti((map, c) -> map.forEach((k, v) -> c.accept(Map.entry(k, v))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
    }
    @SuppressWarnings("unchecked")
    public static <T extends StatTable> StatTable merge(T... statTables) {
        return merge(Arrays.stream(statTables)
                    .map(StatTable::stats)
                    .map(m->(Map<Stat, Double>)m)
                    .toArray(Map[]::new)
        );
    }
}