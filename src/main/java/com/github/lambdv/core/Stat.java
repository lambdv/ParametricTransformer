package com.github.lambdv.core;
import com.github.lambdv.utils.StandardUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



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
    None,
    
    //hidden stats
    ReactionBonus,
    DefReduction,
    DefIgnore,
    PyroResistanceReduction,
    HydroResistanceReduction,
    ElectroResistanceReduction,
    CryoResistanceReduction,
    AnemoResistanceReduction,
    GeoResistanceReduction,
    DendroResistanceReduction,
    PhysicalResistanceReduction,   
    ;

    public static Stat parseStat(String typeName){
        return StatAdaptor.parseStat(typeName);
    }

    private class StatAdaptor{
        public static Map<String, Stat> dictionary = new HashMap<>();
        static {
            Arrays.stream(Stat.values())
                .forEach(stat -> dictionary.put(StandardUtils.flattenName(stat.toString()), stat)
            );
            dictionary.put("cr", Stat.CritRate);
            dictionary.put("cd", Stat.CritDMG);
            dictionary.put("em", Stat.ElementalMastery);
            dictionary.put("er", Stat.EnergyRecharge);
            dictionary.put("hp", Stat.HPPercent);
            dictionary.put("atk", Stat.ATKPercent);
            dictionary.put("def", Stat.DEFPercent);
            dictionary.put("hb", Stat.HealingBonus);
            dictionary.put("n", Stat.None);
            dictionary.put("elementaldmg", Stat.ElementalDMGBonus);
            dictionary.put("physicaldmg", Stat.PhysicalDMGBonus);
            dictionary.put("Physical DMG%", Stat.FlatHP);
        }
        public static Stat parseStat(String typeName){
            return Optional.ofNullable(
                dictionary.get(
                    StandardUtils.flattenName(typeName)
                )
            )
            .orElseThrow(()->new IllegalArgumentException("Invalid stat type: " + typeName));
        }
    }

    public boolean isElementalDMGBonus(){
        return switch(this){
            case PyroDMGBonus, CryoDMGBonus, GeoDMGBonus, DendroDMGBonus, ElectroDMGBonus, HydroDMGBonus, AnemoDMGBonus -> true;
            default -> false;
        };
    }
};

