package com.github.lambdv.core;
import java.util.function.Function;

/**
 * 
 */
public interface DamageInstance extends Function<StatTable, Double>{
    Double apply(StatTable target);


    

    public static DamageInstance of(Element element, DamageType type, BaseScaling scaling, Amplifier amplifier, double instances, double motionValue, StatTable buffs){
        return DamageFormulas.of(element, type, scaling, amplifier, instances, motionValue, buffs);
    }
    public static DamageInstance of(ReactionType type, int instances, StatTable buffs){
        return DamageFormulas.of(type, instances, buffs);
    }
} 

enum DamageType{ Normal, Charged, Plunging, Skill, Burst, None }
enum Element{ Pyro, Hydro, Electro, Anemo, Geo, Dendro, Cryo, Physical, None }
enum BaseScaling{ ATK, DEF, HP }
enum Amplifier{ 
    Forward(2), Reverse(1.5), None(1);
    double multiplier;
    private Amplifier(double m){this.multiplier = m;}
}
enum ReactionType { Overloaded, Superconduct, Electrocharged, Swirl, Shattered, Aggravate, Spread }

/**
 * Utility class for methods returing damage instance lambda objects
 */
class DamageFormulas {
    public static DamageInstance of(
        Element element,
        DamageType type,
        BaseScaling scaling,
        Amplifier amplifier,
        double instances, 
        double motionValue, 
        StatTable buffs
    ){
        if(amplifier == Amplifier.Forward || amplifier == Amplifier.Reverse)
            assert element.equals(Element.Pyro) || element.equals(Element.Hydro) || element.equals(Element.Cryo);

        return (c) -> {
            //var total = StatTables.merge(c, buffs);
            var total = StatTable.of(c, buffs);
            double totalBaseScalingStat = switch(scaling){
                case ATK -> Formulas.totalATK(total);
                case DEF -> Formulas.totalDEF(total);
                case HP -> Formulas.totalHP(total);
                default -> 0.0;
            };

            double amplifierMultiplier = switch(amplifier){
                case Forward -> Formulas.AmplifierMultiplier(2.0, total.get(Stat.ElementalMastery), total.get(Stat.ReactionBonus));
                case Reverse -> Formulas.AmplifierMultiplier(1.5, total.get(Stat.ElementalMastery), total.get(Stat.ReactionBonus));
                case None -> 1.0;
            };

            double totalDMGBonus = total.get(Stat.DMGBonus) 
                + total.get(Stat.ElementalDMGBonus)
                + switch(element){
                    case Pyro -> total.get(Stat.PyroDMGBonus);
                    case Hydro -> total.get(Stat.HydroDMGBonus);
                    case Electro -> total.get(Stat.ElectroDMGBonus);
                    case Anemo -> total.get(Stat.AnemoDMGBonus);
                    case Geo -> total.get(Stat.GeoDMGBonus);
                    case Dendro -> total.get(Stat.DendroDMGBonus);
                    case Cryo -> total.get(Stat.CryoDMGBonus);
                    case Physical -> total.get(Stat.PhysicalDMGBonus);
                    default -> 0.0;
                }
                + switch(type){
                    case Normal -> total.get(Stat.NormalATKDMGBonus);
                    case Charged -> total.get(Stat.ChargeATKDMGBonus);
                    case Plunging -> total.get(Stat.PlungeATKDMGBonus);
                    case Skill -> total.get(Stat.SkillDMGBonus);
                    case Burst -> total.get(Stat.BurstDMGBonus);
                    default -> 0.0;
                };

            double defReduction = total.get(Stat.DefReduction);
            double defIgnore = total.get(Stat.DefIgnore);
            double resistanceReduction = switch(element){
                case Pyro -> total.get(Stat.PyroResistanceReduction);
                case Hydro -> total.get(Stat.HydroResistanceReduction);
                case Electro -> total.get(Stat.ElectroResistanceReduction);
                case Anemo -> total.get(Stat.AnemoResistanceReduction);
                case Geo -> total.get(Stat.GeoResistanceReduction);
                case Dendro -> total.get(Stat.DendroResistanceReduction);
                case Cryo -> total.get(Stat.CryoResistanceReduction);
                case Physical -> total.get(Stat.PhysicalResistanceReduction);
                default -> 0.0;
            };

            return Formulas.FullDamageFormula(
                instances,
                totalBaseScalingStat,
                motionValue,
                1.0,
                0.0,
                Formulas.AvgCritMultiplier(total.get(Stat.CritRate), total.get(Stat.CritDMG)),
                totalDMGBonus,
                0.0,
                Formulas.DefMultiplier(90, Enemy.KQMC(), defReduction, defIgnore),
                Formulas.ResMultiplier(Enemy.KQMC(), resistanceReduction),
                amplifierMultiplier   
            );
        };
    }

    public static DamageInstance of(ReactionType type, int instances, StatTable buffs){  
        return (c) -> 0.0;
    }

    public static DamageInstance defaultPyroSkillATK(double instances, double motionValue, StatTable buffs){
        return of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, instances, motionValue, buffs);
    }

    public static DamageInstance defaultPyroSkillATK(double instances, double motionValue){
        return of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, instances, motionValue, StatTable.empty());
    }

    public static DamageInstance defaultCryoNormalATK(double instances, double motionValue, StatTable buffs){
        return of(Element.Cryo, DamageType.Normal, BaseScaling.ATK, Amplifier.None, instances, motionValue, buffs);
    }

    public static DamageInstance defaultCryoNormalATK(double instances, double motionValue){
        return of(Element.Cryo, DamageType.Normal, BaseScaling.ATK, Amplifier.None, instances, motionValue, StatTable.empty());
    }

    public static DamageInstance defaultCryoChargedATK(double instances, double motionValue){
        return of(Element.Cryo, DamageType.Charged, BaseScaling.ATK, Amplifier.None, instances, motionValue, StatTable.empty());
    }


    public static DamageInstance defaultCryoChargedATK(double instances, double motionValue, StatTable buffs){
        return of(Element.Cryo, DamageType.Charged, BaseScaling.ATK, Amplifier.None, instances, motionValue, buffs);
    }

    public static DamageInstance defaultCryoSkillATK(double instances, double motionValue){
        return of(Element.Cryo, DamageType.Skill, BaseScaling.ATK, Amplifier.None, instances, motionValue, StatTable.empty());
    }


    public static DamageInstance defaultCryoSkillATK(double instances, double motionValue, StatTable buffs){
        return of(Element.Cryo, DamageType.Skill, BaseScaling.ATK, Amplifier.None, instances, motionValue, buffs);
    }

    public static DamageInstance defaultCryoBurstATK(double instances, double motionValue){
        return of(Element.Cryo, DamageType.Burst, BaseScaling.ATK, Amplifier.None, instances, motionValue, StatTable.empty());
    }

    public static DamageInstance defaultCryoBurstATK(double instances, double motionValue, StatTable buffs){
        return of(Element.Cryo, DamageType.Burst, BaseScaling.ATK, Amplifier.None, instances, motionValue, buffs);
    }

    public static DamageInstance defaultCryoBurstATK(double instances, double motionValue, Amplifier amplifier){
        return of(Element.Cryo, DamageType.Burst, BaseScaling.ATK, amplifier, instances, motionValue, StatTable.empty());
    }

    private DamageFormulas(){}
}

