public class Character{

    private String Name;
    private double baseHP;
    private double HPpercent;
    private double flatHP;
    private double baseATK;
    private double ATKpercent;
    private double flatATK;
    private double baseDEF;
    private double DEFpercent;
    private double flatDEF;
    private double ElementalMastery;
    private double CritRate;
    private double CritDMG;
    private double EnergyRecharge;
    private double ElementalDMGBonus;
    private double PhysicalDMGBonus;
    private double NormalATKDMGBonus;
    private double ChargeATKDMGBonus;
    private double PlungeATKDMGBonus;
    private double SkillDMGBonus;
    private double BurstDMGBonus;
    private double HealingBonus;



    public Character(String Name) {
        this.Name = Name;
    }


    //getters
    double totalATK(){
        return (baseATK * (1+ATKpercent) + flatATK);
    }

    double totalATK(double additionalATKpercent, double additionalflatATK){
        return (baseATK * (1+ATKpercent + additionalATKpercent) + (flatATK + additionalflatATK));
    }

    double CritMultiplier(){
        return (1+((CritRate)*(CritDMG)));
    }


    //double dmg = totalATK() * 2.00 * CritMultiplier * ElementalDMGBonus * enemy.DEFMultiplier() * enemy.ElementalResistanceMultiplier();

    //stat getters
    double getCritRate(){return this.CritRate; }
    double getCritDMG(){return this.CritDMG; }

    //setters
    double setCritRate(double CritRate){return this.CritRate = CritRate; }
    double setCritDMG(double CritDMG){return this.CritDMG = CritDMG; }

    //adders
    
}