public class Character{

    //stats
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
    private double DMGBonus;
    private double ElementalDMGBonus;
    private double PhysicalDMGBonus;
    private double NormalATKDMGBonus;
    private double ChargeATKDMGBonus;
    private double PlungeATKDMGBonus;
    private double SkillDMGBonus;
    private double BurstDMGBonus;
    private double HealingBonus;

    //equipped items
    private Weapon weapon;
    private Artifact flower;
    private Artifact feather;
    private Artifact sands;
    private Artifact goblet;
    private Artifact circlet;

    //artifact substats
    private int HProlls = 0;
    private int flatHProlls = 0;
    private int ATKrolls = 0;
    private int flatATKrolls = 0;
    private int DEFrolls = 0;
    private int flatDEFrolls = 0;
    private int EMrolls = 0;
    private int CRrolls = 0;
    private int CDrolls = 0;
    private int ERrolls = 0;

    //constructor
    public Character addStat(String statType, double statValue){
        switch(statType.toLowerCase()){
            case "basehp": this.baseHP += statValue; break;
            case "baseatk": this.baseATK += statValue; break;
            case "basedef": this.baseDEF += statValue; break;
            case "flathp": this.flatHP += statValue; break;
            case "flatatk": this.flatATK += statValue; break;
            case "flatdef": this.flatDEF += statValue; break;
            case "atk%": this.ATKpercent += statValue; break;
            case "er%": this.EnergyRecharge += statValue; break;
            case "def%": this.DEFpercent += statValue; break;
            case "hp%": this.HPpercent += statValue; break;
            case "em": this.ElementalMastery += statValue; break;
            case "elementalmastery": this.ElementalMastery += statValue; break;
            case "dmgbonus": this.DMGBonus += statValue; break;
            case "dmg%": this.DMGBonus += statValue; break;
            case "elementaldmgbonus": this.ElementalDMGBonus += statValue; break;
            case "elementaldmg%": this.ElementalDMGBonus += statValue; break;
            case "physicaldmgbonus": this.PhysicalDMGBonus += statValue; break;
            case "physicaldmg%": this.PhysicalDMGBonus += statValue; break;
            case "physical%": this.PhysicalDMGBonus += statValue; break;
            case "normalatkbonus": this.NormalATKDMGBonus += statValue; break;
            case "normalatk%": this.NormalATKDMGBonus += statValue; break;
            case "chargeatkbonus": this.ChargeATKDMGBonus += statValue; break;
            case "chargeatk%": this.ChargeATKDMGBonus += statValue; break;
            case "plungeatkbonus": this.PlungeATKDMGBonus += statValue; break;
            case "plungeatk%": this.PlungeATKDMGBonus += statValue; break;
            case "plungebonus": this.PlungeATKDMGBonus += statValue; break;
            case "plunge%": this.PlungeATKDMGBonus += statValue; break;
            case "skilldmgbonus": this.SkillDMGBonus += statValue; break;
            case "skilldmg%": this.SkillDMGBonus += statValue; break;
            case "burstdmgbonus": this.BurstDMGBonus += statValue; break;
            case "burstdmg%": this.BurstDMGBonus += statValue; break;
            case "critrate%": this.CritRate += statValue; break;
            case "critrate": this.CritRate += statValue; break;
            case "cr": this.CritRate += statValue; break;
            case "critdmg%": this.CritDMG += statValue; break;
            case "critdmg": this.CritDMG += statValue; break;
            case "cd": this.CritDMG += statValue; break;
            case "healingbonus": this.HealingBonus += statValue; break;
            case "hb": this.HealingBonus += statValue; break;
        }
        return this;
    }

    public Character(String Name, double baseHP, double baseATK, double baseDEF, String AscensionStat, double AscensionStatValue){
        this.EnergyRecharge = 1.00;
        this.CritRate = 0.05;
        this.CritDMG = 1.00;
        this.baseHP = baseHP;
        this.baseDEF = baseDEF;
        this.baseATK = baseATK;
        addStat(AscensionStat, AscensionStatValue);
        this.Name = Name;
    }


    //methods
    public void printStats(){
        System.out.println("                                        ");
        System.out.println("Total HP: " + totalHP());
        System.out.println("Total ATK: " + totalATK());
        System.out.println("Total DEF: " + (baseDEF + (1+DEFpercent) + flatDEF));
        System.out.println("Total EM: " + (ElementalMastery));
        System.out.println("Total CritRate: " + CritRate);
        System.out.println("Total CritDMG: " + (CritDMG));
        System.out.println("Total EnergyRecharge: " + EnergyRecharge);
        System.out.println("Total DMGBonus: " + (DMGBonus));
        System.out.println("Total ElementalDMGBonus: " + (ElementalDMGBonus));
        System.out.println("Total PhysicalDMGBonus: " + (PhysicalDMGBonus));
        System.out.println("Total NormalATKDMG%: " + (NormalATKDMGBonus));
        System.out.println("Total ChargeATKDMG%: " + (ChargeATKDMGBonus));
        System.out.println("Total PlungeATKDMG%: " + (PlungeATKDMGBonus));
        System.out.println("Total SkillDMG%: " + (SkillDMGBonus));
        System.out.println("Total BurstDMG%: " + (BurstDMGBonus));
        System.out.println("Total HealingBonus: " + (HealingBonus));
    }

    public double CritMultiplier(){
        return (1+((CritRate)*(CritDMG)));
    }

    //getters
    public double totalATK(){
        return (baseATK * (1+ATKpercent) + flatATK);
    }

    public double totalATK(double additionalATKpercent, double additionalflatATK){
        return (baseATK * (1+ATKpercent + additionalATKpercent) + (flatATK + additionalflatATK));
    }

    public double totalHP(){
        return (baseHP * (1+HPpercent) + flatHP);
    }

    public String getName(){return this.Name;}
    public double getCritRate(){return this.CritRate;}
    public double getCritDMG(){return this.CritDMG;}
    public double getElementalDMGBonus(){return this.ElementalDMGBonus;}
    public double getNormalATKDMGBonus(){return this.NormalATKDMGBonus;}
    public double getChargeATKDMGBonus(){return this.ChargeATKDMGBonus;}


    public Artifact getFlower(){return this.flower; }
    public Artifact getFeather(){return this.feather; }
    public Artifact getSands(){return this.sands; }
    public Artifact getGoblet(){return this.goblet; }
    public Artifact getCirclet(){return this.circlet; }

    //adders
    public void equip(Weapon weapon){
        this.weapon = weapon;
        this.baseATK += weapon.getBaseATK();
        addStat(weapon.getStat(), weapon.getStatValue());

    }

    public void equip(Artifact artifact){
        switch (artifact.getType()){
            case FLOWER: flower = artifact; break;
            case FEATHER: feather = artifact; break;
            case SANDS: sands = artifact; break;
            case GOBLET: goblet = artifact; break;
            case CIRCLET: circlet = artifact; break;
        }
        addStat(artifact.getMainStat(), artifact.getMainStatValue());
    }


    
}