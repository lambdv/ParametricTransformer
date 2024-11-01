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
    private int HPRolls = 0;
    private int flatHPRolls = 0;
    private int ATKRolls = 0;
    private int flatATKRolls = 0;
    private int DEFRolls = 0;
    private int flatDEFRolls = 0;
    private int EMRolls = 0;
    private int CRRolls = 0;
    private int CDRolls = 0;
    private int ERRolls = 0;

    private double EnergyRechargeRequirement = 1.0;

    public Character addStat(String statType, double statValue){
        switch(statType.toLowerCase().replace("%", "")){
            case "basehp": this.baseHP += statValue; break;
            case "baseatk": this.baseATK += statValue; break;
            case "basedef": this.baseDEF += statValue; break;
            case "flathp": this.flatHP += statValue; break;
            case "flatatk": this.flatATK += statValue; break;
            case "flatdef": this.flatDEF += statValue; break;
            case "atk": this.ATKpercent += statValue; break;
            case "er": this.EnergyRecharge += statValue; break;
            case "def": this.DEFpercent += statValue; break;
            case "hp": this.HPpercent += statValue; break;
            case "em": this.ElementalMastery += statValue; break;
            case "elementalmastery": this.ElementalMastery += statValue; break;
            case "dmgbonus": this.DMGBonus += statValue; break;
            case "dmg": this.DMGBonus += statValue; break;
            case "elementaldmgbonus": this.ElementalDMGBonus += statValue; break;
            case "elementaldmg": this.ElementalDMGBonus += statValue; break;
            case "physicaldmgbonus": this.PhysicalDMGBonus += statValue; break;
            case "physicaldmg": this.PhysicalDMGBonus += statValue; break;
            case "physical": this.PhysicalDMGBonus += statValue; break;
            case "normalatkbonus": this.NormalATKDMGBonus += statValue; break;
            case "normalatk%": this.NormalATKDMGBonus += statValue; break;
            case "chargeatkbonus": this.ChargeATKDMGBonus += statValue; break;
            case "chargeatk": this.ChargeATKDMGBonus += statValue; break;
            case "plungeatkbonus": this.PlungeATKDMGBonus += statValue; break;
            case "plungeatk": this.PlungeATKDMGBonus += statValue; break;
            case "plungebonus": this.PlungeATKDMGBonus += statValue; break;
            case "plunge": this.PlungeATKDMGBonus += statValue; break;
            case "skilldmgbonus": this.SkillDMGBonus += statValue; break;
            case "skilldmg": this.SkillDMGBonus += statValue; break;
            case "burstdmgbonus": this.BurstDMGBonus += statValue; break;
            case "burstdmg": this.BurstDMGBonus += statValue; break;
            case "critrate": this.CritRate += statValue; break;
            case "cr": this.CritRate += statValue; break;
            case "critdmg": this.CritDMG += statValue; break;
            case "cd": this.CritDMG += statValue; break;
            case "healingbonus": this.HealingBonus += statValue; break;
            case "hb": this.HealingBonus += statValue; break;
        }
        return this;
    }

    //constructor
    public Character(String Name, double baseHP, double baseATK, double baseDEF, String AscensionStat, double AscensionStatValue){
        this.EnergyRecharge = 1;
        this.CritRate = 0.05;
        this.CritDMG = 0.5;
        this.baseHP = baseHP;
        this.baseDEF = baseDEF;
        this.baseATK = baseATK;
        addStat(AscensionStat, AscensionStatValue);
        this.Name = Name;
    }


    //methods
    public void printStats() {
        System.out.println("                                        ");
        System.out.println("Total HP: " + String.format("%.2f", totalHP()));
        System.out.println("Total ATK: " + String.format("%.2f", totalATK()));
        System.out.println("Total DEF: " + String.format("%.2f", baseDEF + (1 + DEFpercent) + flatDEF));
        System.out.println("Total EM: " + String.format("%.2f", ElementalMastery));
        System.out.println("Total CritRate: " + String.format("%.2f", CritRate));
        System.out.println("Total CritDMG: " + String.format("%.2f", CritDMG));
        System.out.println("Total EnergyRecharge: " + String.format("%.2f", EnergyRecharge));
        System.out.println("Total DMGBonus: " + String.format("%.2f", DMGBonus));
        System.out.println("Total ElementalDMGBonus: " + String.format("%.2f", ElementalDMGBonus));
        System.out.println("Total PhysicalDMGBonus: " + String.format("%.2f", PhysicalDMGBonus));
        System.out.println("Total NormalATKDMG%: " + String.format("%.2f", NormalATKDMGBonus));
        System.out.println("Total ChargeATKDMG%: " + String.format("%.2f", ChargeATKDMGBonus));
        System.out.println("Total PlungeATKDMG%: " + String.format("%.2f", PlungeATKDMGBonus));
        System.out.println("Total SkillDMG%: " + String.format("%.2f", SkillDMGBonus));
        System.out.println("Total BurstDMG%: " + String.format("%.2f", BurstDMGBonus));
        System.out.println("Total HealingBonus: " + String.format("%.2f", HealingBonus));
    }

    public void printSubs(){
        System.out.println("Roll Counts:");
        System.out.println("flatHPRolls: " + (flatHPRolls - 2));
        System.out.println("HPRolls: " + (HPRolls-2));
        System.out.println("flatATKRolls: " + (flatATKRolls-2));
        System.out.println("ATKRolls: " + (ATKRolls-2));
        System.out.println("flatDEFRolls: " + (flatDEFRolls-2));
        System.out.println("DEFRolls: " + (DEFRolls-2));
        System.out.println("EMRolls: " + (EMRolls-2));
        System.out.println("CRRolls: " + (CRRolls-2));
        System.out.println("CDRolls: " + (CDRolls-2));
        System.out.println("ERRolls: " + (ERRolls-2));
    }

    //formulas
    public double CritMultiplier(){
        return (1+(Math.min(1.00,CritRate)*(CritDMG)));
    }

    public double totalATK(){
        return (baseATK * (1+ATKpercent) + flatATK);
    }

    public double totalATK(double additionalATKpercent, double additionalflatATK){
        return (baseATK * (1+ATKpercent + additionalATKpercent) + (flatATK + additionalflatATK));
    }

    public double totalHP(){
        return (baseHP * (1+HPpercent) + flatHP);
    }


    //getters
    public String getName() { return this.Name; }
    public double getBaseHP() { return this.baseHP; }
    public double getBaseATK() { return this.baseATK;  }
    public double getBaseDEF() { return this.baseDEF; }
    public double getHPpercent() { return this.HPpercent; }
    public double getFlatHP() { return this.flatHP; }
    public double getATKpercent() { return this.ATKpercent; }
    public double getFlatATK() { return this.flatATK; }
    public double getDEFpercent() { return this.DEFpercent; }
    public double getFlatDEF() { return this.flatDEF; }
    public double getElementalMastery() { return this.ElementalMastery; }
    public double getCritRate() { return this.CritRate; }
    public double getCritDMG() { return this.CritDMG; }
    public double getEnergyRecharge() { return this.EnergyRecharge; }
    public double getDMGBonus() { return this.DMGBonus; }
    public double getElementalDMGBonus() { return this.ElementalDMGBonus; }
    public double getPhysicalDMGBonus() { return this.PhysicalDMGBonus; }
    public double getNormalATKDMGBonus() { return this.NormalATKDMGBonus; }
    public double getChargeATKDMGBonus() { return this.ChargeATKDMGBonus; }
    public double getPlungeATKDMGBonus() { return this.PlungeATKDMGBonus; }
    public double getSkillDMGBonus() { return this.SkillDMGBonus; }
    public double getBurstDMGBonus() { return this.BurstDMGBonus; }
    public double getHealingBonus() { return this.HealingBonus; }
    public Weapon getWeapon() { return this.weapon; }
    public Artifact getFlower() { return this.flower; }
    public Artifact getFeather() { return this.feather; }
    public Artifact getSands() { return this.sands; }
    public Artifact getGoblet() { return this.goblet; }
    public Artifact getCirclet() { return this.circlet; }
    
    public int getHProlls() { return this.HPRolls; }
    public int getFlatHProlls() { return this.flatHPRolls; }
    public int getATKrolls() { return this.ATKRolls; }
    public int getFlatATKrolls() { return this.flatATKRolls; }
    public int getDEFrolls() { return this.DEFRolls; }
    public int getFlatDEFrolls() { return this.flatDEFRolls; }
    public int getEMrolls() { return this.EMRolls; }
    public int getCRrolls() { return this.CRRolls; }
    public int getCDrolls() { return this.CDRolls; }
    public int getERrolls() { return this.ERRolls; }


    public double getEnergyRechargeRequirement() {
        return this.EnergyRechargeRequirement;
    }

    //setters
    public Weapon equip(Weapon weapon){
        this.weapon = weapon;
        this.baseATK += weapon.getBaseATK();
        addStat(weapon.getStat(), weapon.getStatValue());
        return weapon;
    }

    public Artifact equip(Artifact artifact){
        switch (artifact.getType()){
            case FLOWER: flower = artifact; break;
            case FEATHER: feather = artifact; break;
            case SANDS: sands = artifact; break;
            case GOBLET: goblet = artifact; break;
            case CIRCLET: circlet = artifact; break;
        }
        addStat(artifact.getMainStat(), artifact.getMainStatValue());
        return artifact;
    }

    public double setERR(double requirement){
        this.EnergyRechargeRequirement = requirement;
        return EnergyRechargeRequirement;
    }    


    //adders
    public double rollFlatHP( int rarity){
        flatHPRolls++;
        addStat("flatHP", 253.94 * (rarity == 5 ? 1.0 : 0.8));
        return flatHPRolls;
    }

    public double rollHP(int rarity){
        HPRolls++;
        addStat("HP%", 0.0496 * (rarity == 5 ? 1.0 : 0.8));
        return HPRolls;
    }

    public double rollFlatATK(int rarity){
        flatATKRolls++;
        addStat("flatATK", 16.54 * (rarity == 5 ? 1.0 : 0.8));
        return flatATKRolls;
    }

    public double rollATK(int rarity){
        ATKRolls++;
        addStat("ATK%", 0.0496 * (rarity == 5 ? 1.0 : 0.8));
        return ATKRolls;
    }

    public double rollFlatDEF(int rarity){
        flatDEFRolls++;
        addStat("flatDEF", 19.68 * (rarity == 5 ? 1.0 : 0.8));
        return flatDEFRolls;
    }

    public double rollDEF(int rarity){
        DEFRolls++;
        addStat("DEF%", 0.0620 * (rarity == 5 ? 1.0 : 0.8));
        return DEFRolls;
    }

    public double rollEM(int rarity){
        EMRolls++;
        addStat("EM", 19.82 * (rarity == 5 ? 1.0 : 0.8));
        return EMRolls;
    }

    public double rollCR(int rarity){
        CRRolls++;
        addStat("CR%", 0.0331 * (rarity == 5 ? 1.0 : 0.8));
        return CRRolls;
    }

    public double rollCD(int rarity){
        CDRolls++;
        addStat("CD%", 0.0662 * (rarity == 5 ? 1.0 : 0.8));
        return CDRolls;
    }

    public double rollER(int rarity){
        ERRolls++;
        addStat("ER%", 0.0551 * (rarity == 5 ? 1.0 : 0.8));
        return ERRolls;
    }

    public void addFixedSubs(int rarity){
        for(int i = 0 ; i < 2 ; i++){
            rollFlatHP(rarity);
            rollHP(rarity);
            rollFlatATK(rarity);
            rollATK(rarity);
            rollFlatDEF(rarity);
            rollDEF(rarity);
            rollEM(rarity);
            rollCR(rarity);
            rollCD(rarity);
            rollER(rarity);
        }
    }
}