package com.github.lambdv.core;
import java.util.Map;


public class DamageFormulas {
    public static DamageInstance DefaultATKFormula(int instances, double motionValue, StatTable buffs){  
        return (c) -> {
            var total = StatTables.merge(c, buffs);
            return Formulas.FullDamageFormula(
                instances,
                Formulas.totalATK(total),
                motionValue,
                1.0,
                0.0,
                Formulas.AvgCritMultiplier(total.get(Stat.CritRate), total.get(Stat.CritDMG)),
                total.get(Stat.DMGBonus),
                0.0,
                Formulas.DefMultiplier(90, Enemy.KQMC(), 0.0, 0.0),
                Formulas.ResMultiplier(Enemy.KQMC(), 0),
                1.0   
            );
        };
    }
    private DamageFormulas(){}
}

class Formulas{
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
        return (1 + ((Math.max(0.0, Math.min(1.0, critRate))) * critDMG));
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