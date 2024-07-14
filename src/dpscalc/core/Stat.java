package dpscalc.core;

import java.util.function.BiFunction;

/**
 * Parent type of all Stat types which are all static inner classes
 */
public interface Stat {
    double value();

    private Stat operate(Stat other, BiFunction<Double, Double, Double> operation) {
        if(other == null) 
            throw new NullPointerException();//other cannot be null
            
        if(this.getClass() != other.getClass()) 
            throw new IllegalArgumentException("Cannot preform operations between two different types of stat"); //this and other must be the same stat
            
        double res = operation.apply(this.value(), other.value());

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

    default Stat subtract(Stat other){
        return this.operate(other, (x,y) -> x - y);
    }

    default Stat multiply(Stat other){
        return this.operate(other, (x,y) -> x * y);
    }

    default Stat divide(Stat other){
        return this.operate(other, (x,y) -> x / y);
    }

    record BaseATK(double value) implements Stat{}
    record FlatATK(double value) implements Stat{}
    record ATKPercent(double value) implements Stat{}
    record BaseHP(double value) implements Stat{}
    record FlatHP(double value) implements Stat{}
    record HPPercent(double value) implements Stat{}
    record BaseDEF(double value) implements Stat{}
    record FlatDEF(double value) implements Stat{}
    record DEFPercent(double value) implements Stat{}
    record ElementalMastery(double value) implements Stat{}
    record CritRate(double value) implements Stat{}
    record CritDMG(double value) implements Stat{}
    record EnergyRecharge(double value) implements Stat{}
    record DMGBonus(double value) implements Stat{}
    record ElementalDMGBonus(double value) implements Stat{}
    record PyroDMGBonus(double value) implements Stat{}
    record CryoDMGBonus(double value) implements Stat{}
    record GeoDMGBonus(double value) implements Stat{}
    record DendroDMGBonus(double value) implements Stat{}
    record ElectroDMGBonus(double value) implements Stat{}
    record HydroDMGBonus(double value) implements Stat{}
    record AnemoDMGBonus(double value) implements Stat{}
    record PhysicalDMGBonus(double value) implements Stat{}
    record NormalATKDMGBonus(double value) implements Stat{}
    record ChargeATKDMGBonus(double value) implements Stat{}
    record PlungeATKDMGBonus(double value) implements Stat{}
    record SkillDMGBonus(double value) implements Stat{}
    record BurstDMGBonus(double value) implements Stat{}
    record HealingBonus(double value) implements Stat{}

    public enum type{
        BASEHP, HPPERCENT, FLATHP,
        BASEATK, ATKPERCENT, FLATATK,
        BASEDEF, DEFPERCENT, FLATDEF,
        ELEMENTALMASTERY, 
        CRITRATE, CRITDMG,
        ENERGYRECHARGE, 
        DMGBONUS, PYRODMGBONUS, CRYODMGBONUS, GEODMGBONUS, DENDRODMGBONUS, ELECTRODMGBONUS, HYDRODMGBONUS, ANEMODMGBONUS, PHYSICALDMGBONUS,
        NOMRALATKDMGBONUS, CHARGEATKDMGBONUS, PLUNGEATKDMGBONUS, SKILLDMGBONUS, BURSTDMGBONUS,
        HEALINGBONUS
    };
}
