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

          double multi = (character.totalATK()) * character.CritMultiplier() * 1+character.getElementalDMGBonus() * enemy.DEFMultiplier() * enemy.ElementalResistanceMultiplier(0);
          
          damage.put("n1 vape", multi * 0.789 * 8);
          damage.put("n2 vape", multi * 0.812 * 8);
          damage.put("ca vape", multi * 2.287 * 8);
          damage.put("bb vape", multi * 1.09 * 2);
          damage.put("q vape", multi * 5.88 * 1);

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

     public static double optimizeSubs(Character character) {
          Character target = character;
          double maxDPR = 0;
          double currentDPR = 0;
          int constraint = 20;
          int currentRolls = 0;

          double d_flatHPRolls, d_HPRolls, d_flatATKRolls, d_ATKRolls, d_flatDEFRolls, d_DEFRolls, d_EMRolls, d_CRRolls, d_CDRolls, d_ERRolls = 0;

          int flatHPRolls = 0;
          int HPRolls = 0;
          int flatATKRolls  = 0;
          int ATKRolls  = 0;
          int flatDEFRolls = 0;
          int DEFRolls  = 0;
          int EMRolls  = 0;
          int CRRolls  = 0;
          int CDRolls  = 0;
          int ERRolls = 0;
          
          int flatHPRollConstraint = 0;
          int HPRollConstraint = 0;
          int flatATKRollConstraint = 0;
          int ATKRollConstraint = 0;
          int flatDEFRollConstraint = 0;
          int DEFRollConstraint = 0;
          int EMRollConstraint = 0;
          int CRRollConstraint = 0;
          int CDRollConstraint = 0;
          int ERRollConstraint = 0;


          for (int i = 0; i < constraint ; i++){

               currentDPR = rotation(target);

               d_flatHPRolls = rotation(target.addStat("flatHP", 253.94)) - currentDPR;
               target.addStat("flatHP", -253.94);

               d_HPRolls = rotation(target.addStat("HP%", 0.0496)) - currentDPR;
               target.addStat("HP%", -0.0496);

               d_CRRolls = rotation(target.addStat("CR", 0.0331)) - currentDPR;
               target.addStat("CR", -0.0331);

               d_CDRolls = rotation(target.addStat("CD", 0.0662)) - currentDPR;
               target.addStat("CD", -0.0662);

               d_ATKRolls = rotation(target.addStat("ATK%", 0.0551)) - currentDPR;
               target.addStat("ATK%", -0.0551);

               double differenceSum = d_flatHPRolls + d_HPRolls + d_ATKRolls + d_CRRolls + d_CDRolls + d_ERRolls;

               if (d_ATKRolls > (differenceSum - d_ATKRolls)) { ATKRolls += 1; }


          }

          return maxDPR;
     }

     //main 
     public static void main(String[] args) {
          characters = loadCharacterData();

          Character hutao = characters.get("Hutao");

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
          hutao.addStat("flatATK", (0.0596*hutao.totalHP()));
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

          System.out.println("DPR: " + optimizeSubs(hutao));
          hutao.printStats();

    }
}