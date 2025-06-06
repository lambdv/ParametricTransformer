
package com.github.lambdv.ParametricTransformer.core;

/**
 * Utility class providing functions for genshin impact formulas
 */
public class Formulas{
    public static double totalATK(StatTable c){
        return (c.get(Stat.BaseATK) * (1 + c.get(Stat.ATKPercent))) + c.get(Stat.FlatATK);
    }

    public static double totalDEF(StatTable c){
        return (c.get(Stat.BaseDEF) * (1 + c.get(Stat.DEFPercent))) + c.get(Stat.FlatDEF);
    }

    public static double totalHP(StatTable c){
        return (c.get(Stat.BaseHP) * (1 + c.get(Stat.HPPercent))) + c.get(Stat.FlatHP);
    }

    public static double AvgCritMultiplier(double critRate, double critDMG){
        var clampedCritRate = Math.max(0.0, Math.min(1.0, critRate));
        //assert clampedCritRate <= 1 && clampedCritRate >= 0;
        return (1 + (clampedCritRate * critDMG));
    }

    public static double DefMultiplier(int characterLevel, Enemy enemy, double defReduction, double defIgnore){
        return (characterLevel + 100) / ((characterLevel + 100.0) + (enemy.level() + 100.0) * (1.0-Math.min(defReduction, 0.9)) * (1.0-defIgnore)); 
    }

    public static double ResMultiplier(Enemy enemy, double resistanceReduction){
        var baseResistance = enemy.universalRes();
        var resistance = baseResistance - resistanceReduction;
        if(resistance < 0)
            return 1 - (resistance/2);
        else if (0 <= resistance && resistance < 0.75)
            return 1 - resistance;
        else //resistance >= 0.75
            return 1/(4*resistance+1);
    }

    public static double AmplifierMultiplier(double amplifier, double elementalMastery, double reactionBonus){
        return (amplifier * (1 + (2.78 * elementalMastery)/(1400 + elementalMastery) + reactionBonus));
    }

    public static Double FullDamageFormula(
        double instances, 
        double totalScalingStat,
        double motionValue,
        double baseDMGMultiplier,
        double additiveBaseDMGBonus,
        double avgCritMultiplier,
        double totalDMGBonus,
        double dmgReductionTarget,
        double defMultiplier,
        double resMultiplier,
        double amplifierMultiuplier
    ){ 
        return  (((totalScalingStat * motionValue) * baseDMGMultiplier) + additiveBaseDMGBonus)
                * avgCritMultiplier
                * (1+totalDMGBonus-dmgReductionTarget)
                * defMultiplier
                * resMultiplier
                * amplifierMultiuplier
                * instances;
    }

    private Formulas(){}
}