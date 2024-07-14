package dpscalc.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class StatTable {
    private Map<Stat.type, Double> stats = new HashMap<>();

    public StatTable(){
        Arrays.stream(Stat.type.values())
            .forEach(s -> stats.put(s, 0.0));
    }

    public StatTable(Map<Stat.type, Double> stats){
        Arrays.stream(Stat.type.values())
            .forEach(s -> stats.put(s, stats.containsKey(s) ? stats.get(s) : 0));
    }

    public Map<Stat.type, Double> getMap(){
        return Collections.unmodifiableMap(this.stats);
    }

    public void add(Stat.type key, double value){
        stats.merge(key, value, (v1, v2) -> v1 + v2);
    }

    public double get(Stat.type key){
        return stats.get(key);
    }

    public StatTable merge(StatTable other){
        StatTable res = new StatTable();

        for(Stat.type s : Stat.type.values()){
            double v1 = 0;
            double v2 = 0;
            if(this.stats.containsKey(s)) v1 = this.get(s);
            if(other.stats.containsKey(s)) v2 = other.get(s);
            res.add(s, v1+v2);
        }
        
        return res;
    }
}
