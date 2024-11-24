package com.github.lambdv.core;
import java.util.*;
import java.util.function.Function;

/**
 * External object that specifiy a sequence of damage instances.
 * @note Character and Rotation "know about each other" by having a reference to each other.
 * @note Rotations hold dynamic instructions/formulas that are executed to output a Damage Per Rotation (DPR) value.
 * @note Rotations or Damage Instances are needed by a character to calculate damage and give a target to optimize stats and gearing for.
 */
public class Rotation {
    StatTable target;
    Map<String, DamageInstance> instances;
    
    public double compute(StatTable target){
        return instances.values().stream()
            .mapToDouble(d->d.apply(target))
            .sum();
    }

    public double compute(){
        return compute(target);
    }
}


interface DamageInstance extends Function<StatTable, Double>{
    Double apply(StatTable target);
} 

class DamageFormulas {
    public static DamageInstance DefaultATKFormula(double motionValue, Map<Stat, Double> buffs){  
        return (c) -> {
            //base
            var totalATK = (c.getStat(Stat.BaseATK) * (1+c.getStat(Stat.ATKPercent)) + c.getStat(Stat.FlatATK));
            var baseDMG = (motionValue * totalATK);
            var baseDMGMultiplier = 1;
            var additiveBaseDMGBonus = 0;

            //dmg multipliers
            var avgCritMultiplier = (1 + (Math.min(c.getStat(Stat.CritRate), 1.0) * c.getStat(Stat.CritDMG)));
            var totalDMGBonusMultiplier = (1 + c.getStat(Stat.DMGBonus));
            var amplifierMultiuplier = 1;

            //enemy multipliers
            var defMultiplier = 1;
            var resMultiplier = 1;

            return ((baseDMG * baseDMGMultiplier) + additiveBaseDMGBonus)
                * avgCritMultiplier
                * totalDMGBonusMultiplier
                * defMultiplier
                * resMultiplier
                * amplifierMultiuplier;
        };
    }
}