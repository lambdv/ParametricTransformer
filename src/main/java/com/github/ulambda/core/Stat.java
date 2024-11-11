package com.github.ulambda.core;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.ulambda.utils.StandardUtils;

/**
 * Stat enum represents types for stats
 * @Note all texture references to stats should reflect the enum type
 * @Note uses pastal case 
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
    public static Map<String, Stat> dictionary = new HashMap<>(Map.of(
        "cr", Stat.CritRate,
        "cd", Stat.CritDMG,
        "em", Stat.ElementalMastery,
        "er", Stat.EnergyRecharge,
        "hp", Stat.HPPercent,
        "atk", Stat.ATKPercent,
        "def", Stat.DEFPercent,
        "hb", Stat.HealingBonus,
        "n", Stat.None,
        "elementaldmg", Stat.ElementalDMGBonus
    ));

    {Arrays.stream(Stat.values())
        .forEach(stat -> dictionary.put(StandardUtils.flattenName(stat.toString()), stat)
    );}

    public static Stat parseStat(String typeName){
        return Optional.ofNullable(dictionary.get(
            StandardUtils.flattenName(typeName)
        ))
        .orElseThrow(()->new IllegalArgumentException("Invalid stat type"));
    }
}