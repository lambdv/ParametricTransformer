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



    private double Attack;



    public Character(String Name, double Attack, double CritRate, double CritDMG) {
        this.Name = Name;
        this.Attack = Attack;
        this.CritRate = CritRate;
        this.CritDMG = CritDMG;
    }

    //getters

    double getAttack(){return this.Attack; }
    double getCritRate(){return this.CritRate; }
    double getCritDMG(){return this.CritDMG; }

    //setter
    double setAttack(double Attack){return this.Attack = Attack;}
    double setCritRate(double CritRate){return this.CritRate = CritRate; }
    double setCritDMG(double CritDMG){return this.CritDMG = CritDMG; }
    
}