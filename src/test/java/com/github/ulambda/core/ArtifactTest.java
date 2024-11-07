package com.github.ulambda.core;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

//json
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.nio.file.Files;


import org.junit.Test;
import com.github.ulambda.core.Character;
import com.github.ulambda.utils.AssetManager;

public class ArtifactTest {
    @Test public void JSONParserTest(){
        try{
            //open artifactMainStatTable.json file
            File file = AssetManager.getFileResource("artifactMainStatTable.json");
            String content = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);
            //System.out.println(json);
            //get 5 star object
            JSONObject fiveStar = json.getJSONObject("substatValues").getJSONObject("5star");
            System.out.println(fiveStar);
            assert fiveStar.getDouble("FlatHP") == 298.75;
            assert fiveStar.getDouble("FlatATK") == 19.45;
            assert fiveStar.getDouble("FlatDEF") == 23.15;
            assert fiveStar.getDouble("HP%") == 5.83;
            assert fiveStar.getDouble("ATK%") == 5.83;
            assert fiveStar.getDouble("DEF%") == 7.29;
            assert fiveStar.getDouble("EM") == 23.31;
            assert fiveStar.getDouble("ER") == 6.48;
            assert fiveStar.getDouble("CR") == 3.89;
            assert fiveStar.getDouble("CD") == 7.77;    
        }
        catch(Throwable t){ 
            t.printStackTrace(); 
            throw new RuntimeException(t);
        }
    }
}