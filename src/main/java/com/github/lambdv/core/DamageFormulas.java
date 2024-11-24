package com.github.lambdv.core;
import java.util.Map;

/**
 * 
 */
public class DamageFormulas {
    public static double AvgCritMultiplier(double critRate, double critDMG){
        return (1 + ((Math.max(0.0, Math.min(1.0, critRate))) * critDMG));
    }

    public static double DefMultiplier(int characterLevel, Enemy enemy, double defReduction, double defIgnore){
        return (characterLevel + 100) / ((characterLevel + 100.0) + (enemy.level() + 100.0) * (1.0-Math.min(defReduction, 0.9)) * (1.0-defIgnore)); 
    }

    public static double ResMultiplier(double res){
        return 1 - (res / (res + 1000));
    }



    public static Double FullDamageFormula(
        double instances, 
        double totalATK,
        double motionValue,
        double baseDMGMultiplier,
        double additiveBaseDMGBonus,
        double avgCritMultiplier,
        double totalDMGBonus,
        double dmgReductionTarget,
        double defMultiplier,
        double resMultiplier,
        double amplifierMultiuplier,
        Map<Stat, Double> buffs
    ){  
        return instances * ((((totalATK * motionValue) * baseDMGMultiplier) + additiveBaseDMGBonus)
                * avgCritMultiplier
                * (1+totalDMGBonus-dmgReductionTarget)
                * defMultiplier
                * resMultiplier
                * amplifierMultiuplier);
    }




    /**
     * 
     * @param motionValue
     * @param buffs
     * @return
     */
    public static DamageInstance DefaultATKFormula(int instances, double motionValue, Map<Stat, Double> buffs){  
        return (c) -> FullDamageFormula(
            instances,
            c.get(Stat.BaseATK) * (1+c.get(Stat.ATKPercent)) + c.get(Stat.FlatATK),
            motionValue,
            1.0,
            0.0,
            AvgCritMultiplier(c.get(Stat.CritRate), c.get(Stat.CritDMG)),
            c.get(Stat.DMGBonus),
            0.0,
            1.0,
            1.0,
            1.0,
            buffs
        );
    }


    private DamageFormulas(){}
}