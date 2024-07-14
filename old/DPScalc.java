import java.io.*;
import java.util.*;
import java.nio.file.*; 

public class DPScalc{ 

     //fields
     private static Map<String, Character> characters = new HashMap<>();
     private static Map<String, Weapon> weapons = new HashMap<>();
     private static Map<String, Artifact> artifacts = new HashMap<>();
     private static Enemy enemy = new Enemy(100, 0.1, 0.1);


     //methods
     public static double rotation(Character character){
          Map<String, Double> damage = new HashMap<String, Double>();

          character.addStat("flatATK", (0.0596*character.totalHP()));

          double multi = (character.totalATK()) * character.CritMultiplier() * 1+character.getElementalDMGBonus() * enemy.DEFMultiplier() * enemy.ElementalResistanceMultiplier(0);
          
          damage.put("n1 vape", multi * 0.789 * 8);
          damage.put("n2 vape", multi * 0.812 * 8);
          damage.put("ca vape", multi * 2.287 * 8);
          damage.put("bb vape", multi * 1.09 * 2);
          damage.put("q vape", multi * 5.88 * 1);
          
          character.addStat("flatATK", -(0.0596*character.totalHP()));

          double DPR = 0;
          for(double DMG : damage.values()) DPR += DMG;
          return Math.round(DPR);
     }


     public static Map<String, Character> loadCharacterData() {
          Map<String, Character> characters = new HashMap<>();

          try {
               Path path = Path.of("database/characterDB.txt");
               Scanner scanner = new Scanner(path);
               while (scanner.hasNextLine()) {  
                    String line = scanner.nextLine();
                    String[] data = line.split(", ");
                    String name = data[0];
                    int baseHP = Integer.parseInt(data[1]);
                    int baseATK = Integer.parseInt(data[2]);
                    int baseDEF = Integer.parseInt(data[3]);
                    String ascensionStat = data[4];
                    double ascensionStatValue = Double.parseDouble(data[5]);
                    characters.put(name, new Character(name, baseHP, baseATK, baseDEF, ascensionStat, ascensionStatValue));
               }
               scanner.close();
          } 
          catch (IOException e) { System.out.println(e);}
          return characters;
     }



     public static double optimizeSubs(Character character) { //global search for optimum value of kqmc substat rolls
          double currentDPR = 0;
          double maxDPR = 0;
          character.addFixedSubs(5);

          int fluidSubsLimit = 20 - (character.getFlower().getRarity() == 4 ? 2 : 0) - (character.getFeather().getRarity() == 4 ? 2 : 0) - - (character.getSands().getRarity() == 4 ? 2 : 0) - (character.getGoblet().getRarity() == 4 ? 2 : 0) - (character.getCirclet().getRarity() == 4 ? 2 : 0);
          int currentRolls = 0;

          //roll constraints
          int flatHPRollConstraint = 8;
          int HPRollConstraint = 10 - (character.getSands().getMainStat() == "HP%" ? 2 : 0) - (character.getGoblet().getMainStat() == "HP%" ? 2 : 0) - (character.getCirclet().getMainStat() == "HP%" ? 2 : 0);
          int flatATKRollConstraint = 8; 
          int ATKRollConstraint = 10 - (character.getSands().getMainStat() == "ATK%" ? 2 : 0) - (character.getGoblet().getMainStat() == "ATK%" ? 2 : 0) - (character.getCirclet().getMainStat() == "ATK%" ? 2 : 0);
          int flatDEFRollConstraint = 8; 
          int DEFRollConstraint = 10 - (character.getSands().getMainStat() == "DEF%" ? 2 : 0) - (character.getGoblet().getMainStat() == "DEF%" ? 2 : 0) - (character.getCirclet().getMainStat() == "DEF%" ? 2 : 0);
          int EMRollConstraint = 10 - (character.getSands().getMainStat() == "EM" ? 2 : 0) - (character.getGoblet().getMainStat() == "EM" ? 2 : 0) - (character.getCirclet().getMainStat() == "EM" ? 2 : 0);
          int CRRollConstraint = 10 - (character.getCirclet().getMainStat() == "CritRate" ? 2 : 0);
          int CDRollConstraint = 10 - (character.getCirclet().getMainStat() == "CritDMG" ? 2 : 0);
          int ERRollConstraint = 10 - (character.getSands().getMainStat() == "ER%" ? 2 : 0);


          while(currentRolls < fluidSubsLimit){
               if(character.getEnergyRecharge() < character.getEnergyRechargeRequirement()){ 
                    character.rollER(5);
                    currentRolls++;
               }
               double d_flatHPRolls = 0;
               double d_HPRolls = 0;
               double d_flatATKRolls = 0; 
               double d_ATKRolls = 0;
               double d_flatDEFRolls = 0;
               double d_DEFRolls = 0;
               double d_EMRolls = 0;
               double d_CRRolls = 0;
               double d_CDRolls = 0;
               double d_ERRolls = 0;

               currentDPR = rotation(character);

               if(character.getFlatHProlls()-2 < flatHPRollConstraint){
                    d_flatHPRolls = rotation(character.addStat("flatHP", 253.94)) - currentDPR;
                    character.addStat("flatHP", -253.94);
               }

               d_HPRolls = rotation(character.addStat("HP%", 0.0496)) - currentDPR;
               character.addStat("HP%", -0.0496);

               d_flatATKRolls = rotation(character.addStat("flatATK", 16.54)) - currentDPR;
               character.addStat("flatATK", -16.54);

               d_ATKRolls = rotation(character.addStat("ATK%", 0.0496)) - currentDPR;
               character.addStat("ATK%", -0.0496);

               d_flatDEFRolls = rotation(character.addStat("flatDEF", 19.68)) - currentDPR;
               character.addStat("flatDEF", -19.68);

               d_DEFRolls = rotation(character.addStat("DEF", 0.0620)) - currentDPR;
               character.addStat("DEF", -0.0620);

               d_EMRolls = rotation(character.addStat("EM", 19.82)) - currentDPR;
               character.addStat("EM", -19.82);

               if(character.getCRrolls()-2 < CRRollConstraint){
                    d_CRRolls = rotation(character.addStat("CR", 0.0331)) - currentDPR;
                    character.addStat("CR", -0.0331);
               }

               if(character.getCDrolls()-2 < CDRollConstraint){
                    d_CDRolls = rotation(character.addStat("CD", 0.0662)) - currentDPR;
                    character.addStat("CD", -0.0662);
               }

               d_ERRolls = rotation(character.addStat("ER", 0.0551)) - currentDPR;
               character.addStat("ER", -0.0551);

               if (d_flatHPRolls > d_CRRolls && d_flatHPRolls > d_HPRolls && d_flatHPRolls > d_flatATKRolls && d_flatHPRolls > d_ATKRolls && d_flatHPRolls > d_flatDEFRolls && d_flatHPRolls > d_EMRolls && d_flatHPRolls > d_CDRolls && d_flatHPRolls > d_ERRolls) { character.rollFlatHP(5);}
               else if (d_HPRolls > d_CRRolls && d_HPRolls > d_flatHPRolls && d_HPRolls > d_flatATKRolls && d_HPRolls > d_ATKRolls && d_HPRolls > d_flatDEFRolls && d_HPRolls > d_EMRolls && d_HPRolls > d_CDRolls && d_HPRolls > d_ERRolls) { character.rollHP(5);}
               else if (d_flatATKRolls > d_CRRolls && d_flatATKRolls > d_flatHPRolls && d_flatATKRolls > d_HPRolls && d_flatATKRolls > d_ATKRolls && d_flatATKRolls > d_flatDEFRolls && d_flatATKRolls > d_EMRolls && d_flatATKRolls > d_CDRolls && d_flatATKRolls > d_ERRolls) { character.rollFlatATK(5);}
               else if (d_ATKRolls > d_CRRolls && d_ATKRolls > d_flatHPRolls && d_ATKRolls > d_HPRolls && d_ATKRolls > d_flatATKRolls && d_ATKRolls > d_flatDEFRolls && d_ATKRolls > d_EMRolls && d_ATKRolls > d_CDRolls && d_ATKRolls > d_ERRolls) { character.rollATK(5);}
               else if (d_flatDEFRolls > d_CRRolls && d_flatDEFRolls > d_flatHPRolls && d_flatDEFRolls > d_HPRolls && d_flatDEFRolls > d_flatATKRolls && d_flatDEFRolls > d_ATKRolls && d_flatDEFRolls > d_EMRolls && d_flatDEFRolls > d_CDRolls && d_flatDEFRolls > d_ERRolls) { character.rollFlatDEF(5);}
               else if (d_DEFRolls > d_CRRolls && d_DEFRolls > d_flatHPRolls && d_DEFRolls > d_HPRolls && d_DEFRolls > d_flatATKRolls && d_DEFRolls > d_ATKRolls && d_DEFRolls > d_EMRolls && d_DEFRolls > d_CDRolls && d_DEFRolls > d_ERRolls) { character.rollDEF(5);}
               else if (d_EMRolls > d_CRRolls && d_EMRolls > d_flatHPRolls && d_EMRolls > d_HPRolls && d_EMRolls > d_flatATKRolls && d_EMRolls > d_ATKRolls && d_EMRolls > d_flatDEFRolls && d_EMRolls > d_CDRolls && d_EMRolls > d_ERRolls) { character.rollEM(5);}
               else if (d_CRRolls > d_flatHPRolls && d_CRRolls > d_HPRolls && d_CRRolls > d_flatATKRolls && d_CRRolls > d_ATKRolls && d_CRRolls > d_flatDEFRolls && d_CRRolls > d_EMRolls && d_CRRolls > d_CDRolls && d_CRRolls > d_ERRolls) { character.rollCR(5);}
               else if (d_CDRolls > d_CRRolls && d_CDRolls > d_flatHPRolls && d_CDRolls > d_HPRolls && d_CDRolls > d_flatATKRolls && d_CDRolls > d_ATKRolls && d_CDRolls > d_flatDEFRolls && d_CDRolls > d_EMRolls && d_CDRolls > d_ERRolls) { character.rollCD(5);}
               else if (d_ERRolls > d_CRRolls && d_ERRolls > d_flatHPRolls && d_ERRolls > d_HPRolls && d_ERRolls > d_flatATKRolls && d_ERRolls > d_ATKRolls && d_ERRolls > d_flatDEFRolls && d_ERRolls > d_EMRolls && d_ERRolls > d_CDRolls) { character.rollER(5);}
               
               currentRolls++;
               maxDPR = rotation(character);
          }
          return maxDPR;
     }

     
     public static void main(String[] args) { //main 

          characters = loadCharacterData();

          Character hutao = characters.get("Hutao"); 
          hutao.setERR(0);

          //equip items
          hutao.equip(new Artifact(5, "flatHP", Artifact.ArtifactType.FLOWER));
          hutao.equip(new Artifact(5, "flatATK", Artifact.ArtifactType.FEATHER));
          hutao.equip(new Artifact(5, "HP%", Artifact.ArtifactType.SANDS));
          hutao.equip(new Artifact(5, "ElementalDMGBonus", Artifact.ArtifactType.GOBLET));
          hutao.equip(new Artifact(5, "CritRate", Artifact.ArtifactType.CIRCLET));
          hutao.equip(new Weapon("DragonsBane", 454, "EM", 221, 5));
     
          //buffs
          hutao.addStat("HP%", 0);
          hutao.addStat("flatHP", 0);
          hutao.addStat("ATK%", 0);
          //hutao.addStat("flatATK", (0.0596*hutao.totalHP()));
          hutao.addStat("DEF%", 0);
          hutao.addStat("flatDEF", 0);
          hutao.addStat("EM", 0);
          hutao.addStat("DMG%", 0.32);
          hutao.addStat("ElementalDMGBonus", 0.15);
          hutao.addStat("PhysicalDMGBonus", 0);
          hutao.addStat("NormalATKBonus", 0);
          hutao.addStat("PlungeATKBonus", 0);
          hutao.addStat("SkillDMGBonus", 0);
          hutao.addStat("BurstDMGBonus", 0);
          hutao.addStat("HealingBonus", 0);
          
          double DPR = optimizeSubs(hutao);
          hutao.printSubs();
          hutao.printStats();
          System.out.println("DPR: " + DPR);

    }
}