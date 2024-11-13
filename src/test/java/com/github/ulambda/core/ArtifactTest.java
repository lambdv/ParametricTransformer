package com.github.ulambda.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

//json
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import com.github.ulambda.core.Character;
import com.github.ulambda.utils.AssetManager;

public class ArtifactTest {
    @Test public void LoadArtifactSubStatResourceAsJSON(){
        try{
            //open artifactMainStatTable.json file
            File file = AssetManager.getFileResource("artifactSubStats.json");
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);
            //System.out.println(json);
            //get 5 star object
            JSONObject fiveStar = json.getJSONObject("5star");
            //System.out.println(fiveStar);
            assertEquals(298.75, fiveStar.getDouble("FlatHP"), 0.001);
            assertEquals(19.45, fiveStar.getDouble("FlatATK"), 0.001);
            assertEquals(23.15, fiveStar.getDouble("FlatDEF"), 0.001);
            assertEquals(5.83, fiveStar.getDouble("HPPercent"), 0.001);
            assertEquals(5.83, fiveStar.getDouble("ATKPercent"), 0.001);
            assertEquals(7.29, fiveStar.getDouble("DEFPercent"), 0.001);
            assertEquals(23.31, fiveStar.getDouble("ElementalMastery"), 0.001);
            assertEquals(6.48, fiveStar.getDouble("EnergyRecharge"), 0.001);
            assertEquals(3.89, fiveStar.getDouble("CritRate"), 0.001);
            assertEquals(7.77, fiveStar.getDouble("CritDMG"), 0.001);
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
            assertEquals(62.2, json.getJSONArray("CritDMG").getDouble(20), 0.01); //level 20
        }
        catch(Throwable t){throw new RuntimeException(t);}
    }

    @Test public void ArtifactsUtilsClassGetMainAndSubstatBaseValues(){
        assertEquals(717, Artifacts.getMainStatValue(5, 0, Stat.FlatHP), 0.01);
        assertEquals(4780, Artifacts.getMainStatValue(5, 20, Stat.FlatHP), 0.01);
        assertEquals(62.2, Artifacts.getMainStatValue(5, 20, Stat.CritDMG), 0.01);
        assertEquals(298.75, Artifacts.getSubStatValue(5, Stat.FlatHP), 0.01);
        assertEquals(19.45, Artifacts.getSubStatValue(5, Stat.FlatATK), 0.01);
        assertEquals(23.15, Artifacts.getSubStatValue(5, Stat.FlatDEF), 0.01);
        assertEquals(5.83, Artifacts.getSubStatValue(5, Stat.HPPercent), 0.01);
        assertEquals(5.83, Artifacts.getSubStatValue(5, Stat.ATKPercent), 0.01);
        assertEquals(7.29, Artifacts.getSubStatValue(5, Stat.DEFPercent), 0.01);
        assertEquals(23.31, Artifacts.getSubStatValue(5, Stat.ElementalMastery), 0.01);
        assertEquals(6.48, Artifacts.getSubStatValue(5, Stat.EnergyRecharge), 0.01);
        assertEquals(3.89, Artifacts.getSubStatValue(5, Stat.CritRate), 0.01);
        assertEquals(7.77, Artifacts.getSubStatValue(5, Stat.CritDMG), 0.01);

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

    @Test public void SubstatFunctionality(){
        Artifact flower = new Artifact(Artifact.ArtifactType.FLOWER, 20, 5, Stat.FlatHP, new ArtifactSetBonus(Map.of(), Map.of()));
        SubStats flowerSUbs = new SubStats(flower, 
            Stat.FlatATK, RollQuality.AVG,  
            Stat.FlatDEF,  RollQuality.AVG,
            Stat.HPPercent, RollQuality.AVG,
            Stat.ATKPercent, RollQuality.AVG
        );
        flower.set(flowerSUbs);
        
    }


    
}