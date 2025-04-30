package com.github.lambdv.ParametricTransformer.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.lambdv.ParametricTransformer.core.ArtifactBuilder;
import com.github.lambdv.ParametricTransformer.core.Artifacts;
import com.github.lambdv.ParametricTransformer.core.Character;
import com.github.lambdv.ParametricTransformer.core.Stat;
import com.github.lambdv.ParametricTransformer.utils.AssetManager;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.nio.file.Files;
import java.security.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.function.BiFunction;

/**
 * Tests for the Artifact class, Substats and ArtifactBuilder
 */
public class ArtifactTest {
    @Test public void LoadArtifactSubStatResourceAsJSON(){
        try{
            //open artifactMainStatTable.json file
            File file = AssetManager.getDataFileResource("artifactSubStats.json");
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);
            //System.out.println(json);
            //get 5 star object
            JSONObject fiveStar = json.getJSONObject("5star");
            //System.out.println(fiveStar);
            assertEquals(298.75, fiveStar.getDouble("FlatHP"), 0.001);
            assertEquals(19.45, fiveStar.getDouble("FlatATK"), 0.001);
            assertEquals(23.15, fiveStar.getDouble("FlatDEF"), 0.001);
            assertEquals(0.0583, fiveStar.getDouble("HPPercent"), 0.001);
            assertEquals(0.0583, fiveStar.getDouble("ATKPercent"), 0.001);
            assertEquals(0.0729, fiveStar.getDouble("DEFPercent"), 0.001);
            assertEquals(23.31, fiveStar.getDouble("ElementalMastery"), 0.001);
            assertEquals(0.0648, fiveStar.getDouble("EnergyRecharge"), 0.001);
            assertEquals(0.0389, fiveStar.getDouble("CritRate"), 0.001);
            assertEquals(0.0777, fiveStar.getDouble("CritDMG"), 0.001);
        }
        catch(Throwable t){throw new RuntimeException(t);}
    }

    @Test public void LoadArtifactMainStatResourceAsJSON(){
        try{
            //open artifactMainStatTable.json file
            JSONObject json = AssetManager.getJSONResource("artifactMainStats.json");
            json = json.getJSONObject("5star");
            //System.out.println(json);
            assertEquals(717, json.getJSONArray("FlatHP").getDouble(0), 0.01); //level 0
            assertEquals(4780, json.getJSONArray("FlatHP").getDouble(20), 0.01); //level 20
            assertEquals(62.2/100.0, json.getJSONArray("CritDMG").getDouble(20), 0.01); //level 20
        }
        catch(Throwable t){throw new RuntimeException(t);}
    }

    @Test public void ArtifactsUtilsClassGetMainAndSubstatBaseValues(){
        assertEquals(717, Artifacts.getMainStatValue(5, 0, Stat.FlatHP), 0.01);
        assertEquals(4780, Artifacts.getMainStatValue(5, 20, Stat.FlatHP), 0.01);
        assertEquals(62.2/100.0, Artifacts.getMainStatValue(5, 20, Stat.CritDMG), 0.01);
        assertEquals(298.75, Artifacts.getSubStatValue(5, Stat.FlatHP), 0.01);
        assertEquals(19.45, Artifacts.getSubStatValue(5, Stat.FlatATK), 0.01);
        assertEquals(23.15, Artifacts.getSubStatValue(5, Stat.FlatDEF), 0.01);
        assertEquals(0.0583, Artifacts.getSubStatValue(5, Stat.HPPercent), 0.01);
        assertEquals(0.0583, Artifacts.getSubStatValue(5, Stat.ATKPercent), 0.01);
        assertEquals(0.0729, Artifacts.getSubStatValue(5, Stat.DEFPercent), 0.01);
        assertEquals(23.31, Artifacts.getSubStatValue(5, Stat.ElementalMastery), 0.01);
        assertEquals(0.0648, Artifacts.getSubStatValue(5, Stat.EnergyRecharge), 0.01);
        assertEquals(0.0389, Artifacts.getSubStatValue(5, Stat.CritRate), 0.01);
        assertEquals(0.0777, Artifacts.getSubStatValue(5, Stat.CritDMG), 0.01);

        // var flower = new Artifact(Artifact.ArtifactType.FLOWER, 20, 5, Stat.FlatHP, new ArtifactSetBonus(Map.of(), Map.of()));
        // var feather = new Artifact(Artifact.ArtifactType.FEATHER, 20, 5, Stat.FlatATK, new ArtifactSetBonus(Map.of(), Map.of()));
        // var sands = new Artifact(Artifact.ArtifactType.SANDS, 20, 5, Stat.HPPercent, new ArtifactSetBonus(Map.of(), Map.of()));
        // var goblet = new Artifact(Artifact.ArtifactType.GOBLET, 20, 5, Stat.ATKPercent, new ArtifactSetBonus(Map.of(), Map.of()));
        // var circlet = new Artifact(Artifact.ArtifactType.CIRCLET, 20, 5, Stat.CritRate, new ArtifactSetBonus(Map.of(), Map.of()));

        // assertEquals(4780, flower.getStat(Stat.FlatHP), 0.01);
        // assertEquals(717, feather.getStat(Stat.FlatATK), 0.01);
        // assertEquals(62.2, sands.getStat(Stat.HPPercent), 0.01);
        // assertEquals(4780, flower.getStat(Stat.FlatHP), 0.01);
        // assertEquals(717, feather.getStat(Stat.FlatATK), 0.01);
        
    }


    @Test public void TestMaxRollsForMethod(){
        ArtifactBuilder bob = ArtifactBuilder.KQMC(
            Stat.ATKPercent, 
            Stat.PyroDMGBonus, 
            Stat.CritRate
        );
        BiFunction<Integer, Integer, Integer> Test = (rarity, level) -> Artifacts.maxRollsFor(new Flower(null, rarity, level));
        assertEquals(0, Test.apply(1,0));
        assertEquals(1, Test.apply(1,4));
        assertEquals(1, Test.apply(2,0));
        assertEquals(2, Test.apply(2,4));
        assertEquals(2, Test.apply(3,0));
        assertEquals(3, Test.apply(3,4));
        assertEquals(4, Test.apply(3,8));
        assertEquals(5, Test.apply(3,12));

        assertEquals(3, Test.apply(4,0));
        assertEquals(4, Test.apply(4,4));
        assertEquals(5, Test.apply(4,8));
        assertEquals(6, Test.apply(4,12));
        assertEquals(7, Test.apply(4,16));

        assertEquals(4, Test.apply(5,0));
        assertEquals(5, Test.apply(5,4));
        assertEquals(6, Test.apply(5,8));
        assertEquals(7, Test.apply(5,12));
        assertEquals(8, Test.apply(5,16));
        assertEquals(9, Test.apply(5,20));
    }

    @Test public void ArtifactBuilderTest(){
        ArtifactBuilder bob = new ArtifactBuilder(
            new Flower (ArtifactSet.empty(), 5, 20),
            new Feather(ArtifactSet.empty(), 5, 20),
            new Sands  (ArtifactSet.empty(), 5, 20, Stat.ATKPercent),
            new Goblet(ArtifactSet.empty(), 5, 20, Stat.PyroDMGBonus),
            new Circlet(ArtifactSet.empty(), 5, 20, Stat.CritRate)
        );
        assertEquals(0, bob.numRolls());
        assertEquals(45, bob.maxRolls());
        assertEquals(45, bob.numRollsLeft());

        //4-star artifacts will have a penalty of -2 distributed substats per 4-star artifact
        //total 2*5 = 10 substat rolls less than 5-star artifacts
        bob = new ArtifactBuilder(
            new Flower (ArtifactSet.empty(), 4, 16),
            new Feather(ArtifactSet.empty(), 4, 16),
            new Sands  (ArtifactSet.empty(),4, 16, Stat.ATKPercent),
            new Goblet(ArtifactSet.empty(), 4, 16, Stat.PyroDMGBonus),
            new Circlet(ArtifactSet.empty(), 4, 16, Stat.CritRate)
        );
        assertEquals(0, bob.numRolls());
        assertEquals(35, bob.maxRolls());

        //but what if all artifacts are 4 stars for the set bonus and only 1 5-star artifact
        //eg instructor set with 5 star er sands

        bob = new ArtifactBuilder(
            new Flower (ArtifactSet.empty(), 4, 16),
            new Feather(ArtifactSet.empty(), 4, 16),
            new Sands  (ArtifactSet.empty(), 5, 20, Stat.EnergyRecharge),
            new Goblet(ArtifactSet.empty(), 4, 16, Stat.ElementalMastery),
            new Circlet(ArtifactSet.empty(), 4, 16, Stat.CritRate)
        );

        assertEquals(0, bob.numRolls());
        assertEquals(37, bob.maxRolls());
    }

    @Test public void KQMArtifactBuilderTest(){
        ArtifactBuilder bob = ArtifactBuilder.KQMC(
            Stat.ATKPercent, 
            Stat.PyroDMGBonus, 
            Stat.CritRate
        );
        assertEquals(20, bob.numRolls());
        assertEquals(40, bob.maxRolls());
        assertEquals(20, bob.numRollsLeft());


        // assertEquals(10, bob.substatConstraints.get(Stat.HPPercent));
        // assertEquals(8, bob.substatConstraints.get(Stat.ATKPercent));
        // assertEquals(8, bob.substatConstraints.get(Stat.CritRate));

        assertEquals(10, bob.numRollsLeft(Stat.HPPercent));
        assertEquals(8, bob.numRollsLeft(Stat.ATKPercent));
        assertEquals(8, bob.numRollsLeft(Stat.CritRate));
    }

    @Test public void ArtifactBuilderRollingSubstats(){
        ArtifactBuilder bob = ArtifactBuilder.KQMC(
            Stat.ATKPercent, 
            Stat.PyroDMGBonus, 
            Stat.CritRate
        );

        //assertEquals(10, bob.substatConstraints.get(Stat.HPPercent));
        assertEquals(10, bob.numRollsLeft(Stat.HPPercent));

        assertEquals(20, bob.numRolls());

        for (int i = 1; i <= 10; i++){
            bob.roll(Stat.HPPercent, Artifacts.RollQuality.AVG);
            //assertEquals(9-i+1, bob.substatConstraints.get(Stat.HPPercent));
            assertEquals(10-i, bob.numRollsLeft(Stat.HPPercent));
        }
        try{
            bob.roll(Stat.HPPercent, Artifacts.RollQuality.AVG);
            assertTrue(false);
        }
        catch(AssertionError e){}

        //System.out.println(bob.substats());
        var substats = bob.substats();
        assertEquals(12, bob.numRolls(Stat.HPPercent));
        assertEquals(0.496, (Artifacts.getSubStatValue(5, Stat.HPPercent) * Artifacts.RollQuality.AVG.multiplier)/10, 1);
        assertEquals(0.0496 * 12, substats.get(Stat.HPPercent), 0.1);
        assertEquals(507.88, substats.get(Stat.FlatHP), 0.1);
        assertEquals(0.0992, substats.get(Stat.ATKPercent), 0.1);
        assertEquals(33.08, substats.get(Stat.FlatATK), 0.1);
        assertEquals(0.124, substats.get(Stat.DEFPercent), 0.1);
        assertEquals(39.64, substats.get(Stat.FlatDEF), 1); //susge
        assertEquals(39.64, substats.get(Stat.ElementalMastery), 0.1);
        assertEquals(0.0662, substats.get(Stat.CritRate), 0.1);
        assertEquals(0.1324, substats.get(Stat.CritDMG), 0.1);
        assertEquals(0.1102, substats.get(Stat.EnergyRecharge), 0.1);
    }

    @Test public void ArtifactBuilderRollingSubstatsFor4Star(){
        ArtifactBuilder bob = ArtifactBuilder.KQMC(
            Optional.of(new Flower(ArtifactSet.empty(), 4, 16)),
            Optional.of(new Feather(ArtifactSet.empty(), 4, 16)),
            Optional.of(new Sands(ArtifactSet.empty(), 4, 16, Stat.ATKPercent)),
            Optional.of(new Goblet(ArtifactSet.empty(), 4, 16, Stat.PyroDMGBonus)),
            Optional.of(new Circlet(ArtifactSet.empty(), 4, 16, Stat.CritRate))
        );
        //assertEquals(10, bob.substatConstraints.get(Stat.HPPercent));
        assertEquals(20, bob.maxRolls());
    }


    @Test public void BuffedStatTableCapturesArtifactBuilderRollStatUpdates(){
        var bob = ArtifactBuilder.KQMC(Stat.ATKPercent, Stat.AnemoDMGBonus, Stat.CritDMG);
        
        var c = Characters.of("amber");

        BuffedStatTable buffed = new BuffedStatTable(c, bob);

        var before = buffed.get(Stat.ATKPercent);

        bob.roll(Stat.ATKPercent, Artifacts.RollQuality.AVG);
        bob.roll(Stat.ATKPercent, Artifacts.RollQuality.AVG);
        bob.roll(Stat.ATKPercent, Artifacts.RollQuality.AVG);
        bob.roll(Stat.ATKPercent, Artifacts.RollQuality.AVG);
        bob.roll(Stat.ATKPercent, Artifacts.RollQuality.AVG);

        var after = buffed.get(Stat.ATKPercent);

        assert before != after;

        //System.out.println(before);
        //System.out.println(after);


    }


}