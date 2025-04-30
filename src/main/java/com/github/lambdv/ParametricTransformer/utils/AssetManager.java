package com.github.lambdv.ParametricTransformer.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class AssetManager {
    public final static Path resourcePath(){
        return Paths.get("").toAbsolutePath().resolve("src/resources/");
    }

    public final static File getFileResource(String fileName) throws FileNotFoundException{
        return resourcePath().resolve(fileName).toFile();
    }

    public final static File getDataFileResource(String fileName) throws FileNotFoundException{
        return getFileResource("data/"+fileName);
    }
    
    public final static JSONObject getJSONResource(String fileName) throws FileNotFoundException{
        try{
            File resource = AssetManager.getDataFileResource(fileName);
            var bytes = Files.readAllBytes(resource.toPath());
            String content = new String(bytes);
            return new JSONObject(content);
        }
        catch(FileNotFoundException e){ throw e; }
        catch(Throwable t){ throw new RuntimeException(t); }
    }
}
