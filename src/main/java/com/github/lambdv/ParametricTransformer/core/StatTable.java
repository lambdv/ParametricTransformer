package com.github.lambdv.ParametricTransformer.core;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Top level type for data structures that stores stat-amount pairs.
 * @note stats() may or many not return an immutable or defensive cloned map. you should assume you cannot directly modify the map.
 * @note StatTables are not inherently immutable, its just that you cannot directly specify what stat-amouns to add or set.
 */
public interface StatTable extends Serializable{
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
    public default double get(Stat type){
        return stats().getOrDefault(type, 0.0);
    }
    
    /**
     * Compile the stat table into an immutable stat table.
     * @return
     */
    public default StatTable build(){
        var stats = stats(); //defensive copy
        return () -> stats;
    }

    /**
     * Get stream of stat-amount pairs from the stat table.
     * @return
     */
    public default Stream<Map.Entry<Stat, Double>> stream(){
        return stats().entrySet().stream();
    }

    /**
     * Get the string representation of the stat table.
     * @return
     */
    public default String ToString(){
        return stats().toString();
    }

    public static StatTable empty(){ return () -> Map.of(); }
    public static StatTable of(){ return () -> Map.of(); }
    public static StatTable of(Stat s0, double v0){ return () -> Map.of(s0, v0); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1){ return () -> Map.of(s0, v0, s1, v1); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2){ return () -> Map.of(s0, v0, s1, v1, s2, v2); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4, Stat s5, double v5){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4, s5, v5); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4, Stat s5, double v5, Stat s6, double v6){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4, s5, v5, s6, v6); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4, Stat s5, double v5, Stat s6, double v6, Stat s7, double v7){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4, s5, v5, s6, v6, s7, v7); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4, Stat s5, double v5, Stat s6, double v6, Stat s7, double v7, Stat s8, double v8){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4, s5, v5, s6, v6, s7, v7, s8, v8); }
    public static StatTable of(Stat s0, double v0, Stat s1, double v1, Stat s2, double v2, Stat s3, double v3, Stat s4, double v4, Stat s5, double v5, Stat s6, double v6, Stat s7, double v7, Stat s8, double v8, Stat s9, double v9){ return () -> Map.of(s0, v0, s1, v1, s2, v2, s3, v3, s4, v4, s5, v5, s6, v6, s7, v7, s8, v8, s9, v9); }
    
    /**
     * Make a new stat table that is the sum of all stat tables.
     * @param <T>
     * @param statTables
     * @return
     */
    @SuppressWarnings("unchecked") public static <T extends StatTable> StatTable of(T... statTables) {
        Map<Stat, Double> mergedStats = Arrays.stream(statTables)
            .flatMap(map -> map.stats().entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
        return () -> mergedStats;
    }

    public static <T extends Map<Stat, Double>> StatTable of(Stream<T> statTablesMap) {
        return () -> statTablesMap
            .<Map.Entry<Stat, Double>>mapMulti((map, c) -> map.forEach((k, v) -> c.accept(Map.entry(k, v))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum, HashMap::new));
    }
}

interface DynamicStatTable extends StatTable {
    public Map<Stat, List<Function<StatTable, Double>>> dynamicStats();
    public default Map<Stat, Double> stats(){
        return dynamicStats().entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey, 
                e -> e.getValue().stream()
                    .mapToDouble(f -> f.apply(this))
                    .sum()
                ));
    }
    default double get(Stat type){
        return dynamicStats().getOrDefault(type, Collections.emptyList()).stream()
            .mapToDouble(f -> f.apply(this))
            .sum();
    }
}
interface MutableStatTable extends StatTable{
    /**
     * Add the amount value of a stat type stored in the stat table.
     * @param type
     * @param value
     */
    public default MutableStatTable add(Stat type, double value){
        stats().merge(type, value, Double::sum); return this;
    }

    /**
     * Set the amount value of a stat type stored in the stat table.
     * @param type
     * @param value
     */
    public default MutableStatTable set(Stat type, double value){
        stats().put(type, value); return this;
    }

    /**
     * Remove the amount value of a stat type stored in the stat table.
     * @param type
     */
    public default MutableStatTable remove(Stat type){
        stats().remove(type); return this;
    }
}

/**
 * concrete decorator pattern for adding buffs over a base stat table.
 */
record BuffedStatTable(StatTable base, StatTable... buffs) implements StatTable{
    public BuffedStatTable(StatTable base, StatTable buffs){
        this(base, new StatTable[]{buffs});
    }
    public Map<Stat, Double> stats(){ 
        return StatTable.of(Stream.concat(Stream.of(base.stats()), Arrays.stream(buffs).map(s->s.stats()))).stats();
    }
    @Override public double get(Stat type){
        return base.get(type) + Arrays.stream(buffs).mapToDouble(b->b.get(type)).sum();
    }
} 

// record ConcreteImmutableStatTable(Map<Stat, Double> stats) implements StatTable{}
// record ConcreteMutableStatTable(Map<Stat, Double> stats) implements MutableStatTable{
//     public ConcreteMutableStatTable{
//         assert stats != null;
//         try{ this.add(Stat.ATKPercent, 1); }
//         catch(Throwable t){ throw new IllegalArgumentException("stats must be a mutable map"); }
//         finally{ stats.remove(Stat.ATKPercent); }
//     }
//     public ConcreteMutableStatTable(){ this(new HashMap<>()); }
//     public ConcreteMutableStatTable(StatTable statTable){ this(new HashMap<>(statTable.stats())); }
// }

// /**
//  * Utility class for merging multiple stat tables into one.
//  */
// class StatTables {
//     /**
//      * Make a new stat table that is the sum of all stat tables.
//      * @param <T>
//      * @param statTablesMap
//      * @return
//      */
//     @SafeVarargs public static <T extends Map<Stat, Double>> StatTable merge(T... statTablesMap) {
//         return () -> Arrays.stream(statTablesMap)
//             .<Map.Entry<Stat, Double>>mapMulti((map, c) -> map.forEach((k, v) -> c.accept(Map.entry(k, v))))
//             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
//     }
//     /**
//      * Make a new stat table that is the sum of all stat tables.
//      * @param <T>
//      * @param statTables
//      * @return
//      */
//     @SuppressWarnings("unchecked") public static <T extends StatTable> StatTable merge(T... statTables) {
//         Map<Stat, Double> mergedStats = Arrays.stream(statTables)
//             .flatMap(map -> map.stats().entrySet().stream())
//             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum));
//         return () -> mergedStats;
//     }

//     public static <T extends Map<Stat, Double>> StatTable merge(Stream<T> statTablesMap) {
//         return () -> statTablesMap
//             .<Map.Entry<Stat, Double>>mapMulti((map, c) -> map.forEach((k, v) -> c.accept(Map.entry(k, v))))
//             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Double::sum, HashMap::new));
//     }

//     private StatTables() {}
// }