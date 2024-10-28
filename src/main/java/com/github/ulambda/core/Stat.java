package com.github.ulambda.core;
import java.util.HashMap;
import java.util.Map;

/**
 * Stat enum represents types for stats
 */
public enum Stat {
    BaseHP, 
    FlatHP, 
    HPPercent,
    BaseATK, 
    FlatATK, 
    ATKPercent,
    BaseDEF, 
    FlatDEF, 
    DEFPercent,
    ElementalMastery, 
    CritRate, 
    CritDMG,
    EnergyRecharge,
    DMGBonus, 
    ElementalDMGBonus,
    PyroDMGBonus, 
    CryoDMGBonus, 
    GeoDMGBonus, 
    DendroDMGBonus, 
    ElectroDMGBonus, 
    HydroDMGBonus, 
    AnemoDMGBonus, 
    PhysicalDMGBonus,
    NormalATKDMGBonus, 
    ChargeATKDMGBonus, 
    PlungeATKDMGBonus,
    SkillDMGBonus, 
    BurstDMGBonus, 
    HealingBonus,
    None;

    public static Stat parseStat(String typeName){
        return StatAdaptor.parseStat(typeName);
    }
};

class StatAdaptor{
    private static Map<String, Stat> dictionary = Map.of(
        "cr", Stat.CritRate,
        "cd", Stat.CritDMG,
        "em", Stat.ElementalMastery,
        "er", Stat.EnergyRecharge,
        "hp", Stat.HPPercent,
        "atk", Stat.ATKPercent,
        "def", Stat.DEFPercent,
        "hb", Stat.HealingBonus,
        "n", Stat.None
    );

    private static Map<String, Stat> symbols = new HashMap<>();
    {
        for(Stat stat : Stat.values())
            symbols.put(stat.toString().toLowerCase().replaceAll("\\s", "").replaceAll("%", ""), stat);
    }

    public static Stat parseStat(String typeName){
        typeName = typeName.toLowerCase()
            .replaceAll("\\s", "")
            .replaceAll("%", "");
        if(dictionary.containsKey(typeName))
            return dictionary.get(typeName);

        if(symbols.containsKey(typeName))
            return symbols.get(typeName);

        throw new IllegalArgumentException("Invalid stat type");
    }
}