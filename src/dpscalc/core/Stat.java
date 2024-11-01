package dpscalc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public sealed interface Stat {
    double amount();
    Stat.type type();

    private Stat operate(Stat other, BiFunction<Double, Double, Double> operation) {
        if(other == null) 
            throw new NullPointerException();//other cannot be null 
        if(this.getClass() != other.getClass()) 
            throw new IllegalArgumentException("Cannot preform operations between two different types of stat"); //this and other must be the same stat
        double res = operation.apply(this.amount(), other.amount());
        try { //use reflection to call constructor of child class
            return (Stat) (this.getClass().getConstructor(double.class).newInstance(res)); 
        } 
        catch (Exception e) { 
            throw new RuntimeException("Failed to call constructor"); 
        }
    }

    default Stat add(Stat other){
        return this.operate(other, (x,y) -> x + y);
    }

    public enum type {
        BaseHP, FlatHP, HPPercent,
        BaseATK, FlatATK, ATKPercent,
        BaseDEF, FlatDEF, DEFPercent,
        ElementalMastery, CritRate, CritDMG,
        EnergyRecharge,DMGBonus, ElementalDMGBonus,
        PyroDMGBonus, CryoDMGBonus, GeoDMGBonus, DendroDMGBonus, 
        ElectroDMGBonus, HydroDMGBonus, AnemoDMGBonus, PhysicalDMGBonus,
        NormalATKDMGBonus, ChargeATKDMGBonus, PlungeATKDMGBonus,
        SkillDMGBonus, BurstDMGBonus, HealingBonus;
    };

    static final Map<String, Stat.type> dictionary = new HashMap<>();

    private static void initDict(){
        if(dictionary.isEmpty()){
            for(Stat.type type : Stat.type.values())
                dictionary.put(type.toString().toLowerCase(), type);
            dictionary.put("cr", Stat.type.CritRate);
            dictionary.put("cd", Stat.type.CritDMG);
            dictionary.put("em", Stat.type.ElementalMastery);
            dictionary.put("er", Stat.type.EnergyRecharge);
            dictionary.put("hp", Stat.type.HPPercent);
            dictionary.put("atk", Stat.type.ATKPercent);
            dictionary.put("def", Stat.type.DEFPercent);
            dictionary.put("hb", Stat.type.HealingBonus);
            dictionary.put("n", null);
        }
    }

    public static Stat.type parseStatType(String typeName){
        typeName = typeName.toLowerCase();
        typeName = typeName.replaceAll("\\s", "");
        typeName = typeName.replaceAll("%", "");

        initDict();

        if(dictionary.containsKey(typeName))
            return dictionary.get(typeName);
        else
            throw new IllegalArgumentException("Invalid stat type");
    }

    record BaseHP(double amount)            implements Stat{ public type type(){ return type.BaseHP; } }
    record FlatHP(double amount)            implements Stat{ public type type(){ return type.FlatHP; } }
    record HPPercent(double amount)         implements Stat{ public type type(){ return type.HPPercent; } }
    record BaseATK(double amount)           implements Stat{ public type type(){ return type.BaseATK; } }
    record FlatATK(double amount)           implements Stat{ public type type(){ return type.FlatATK; } }
    record ATKPercent(double amount)        implements Stat{ public type type(){ return type.ATKPercent; } }
    record BaseDEF(double amount)           implements Stat{ public type type(){ return type.BaseDEF; } }
    record FlatDEF(double amount)           implements Stat{ public type type(){ return type.FlatDEF; } }
    record DEFPercent(double amount)        implements Stat{ public type type(){ return type.DEFPercent; } }
    record ElementalMastery(double amount)  implements Stat{ public type type(){ return type.ElementalMastery; } }
    record CritRate(double amount)          implements Stat{ public type type(){ return type.CritRate; } }
    record CritDMG(double amount)           implements Stat{ public type type(){ return type.CritDMG; } }
    record EnergyRecharge(double amount)    implements Stat{ public type type(){ return type.EnergyRecharge; } }
    record DMGBonus(double amount)          implements Stat{ public type type(){ return type.DMGBonus; } }
    record ElementalDMGBonus(double amount) implements Stat{ public type type(){ return type.ElementalDMGBonus; } }
    record PyroDMGBonus(double amount)      implements Stat{ public type type(){ return type.PyroDMGBonus; } }
    record CryoDMGBonus(double amount)      implements Stat{ public type type(){ return type.CryoDMGBonus; } }
    record GeoDMGBonus(double amount)       implements Stat{ public type type(){ return type.GeoDMGBonus; } }
    record DendroDMGBonus(double amount)    implements Stat{ public type type(){ return type.DendroDMGBonus; } }
    record ElectroDMGBonus(double amount)   implements Stat{ public type type(){ return type.ElectroDMGBonus; } }
    record HydroDMGBonus(double amount)     implements Stat{ public type type(){ return type.HydroDMGBonus; } }
    record AnemoDMGBonus(double amount)     implements Stat{ public type type(){ return type.AnemoDMGBonus; } }
    record PhysicalDMGBonus(double amount)  implements Stat{ public type type(){ return type.PhysicalDMGBonus; } }
    record NormalATKDMGBonus(double amount) implements Stat{ public type type(){ return type.NormalATKDMGBonus; } }
    record ChargeATKDMGBonus(double amount) implements Stat{ public type type(){ return type.ChargeATKDMGBonus; } }
    record PlungeATKDMGBonus(double amount) implements Stat{ public type type(){ return type.PlungeATKDMGBonus; } }
    record SkillDMGBonus(double amount)     implements Stat{ public type type(){ return type.SkillDMGBonus; } }
    record BurstDMGBonus(double amount)     implements Stat{ public type type(){ return type.BurstDMGBonus; } }
    record HealingBonus(double amount)      implements Stat{ public type type(){ return type.HealingBonus; } }
}
