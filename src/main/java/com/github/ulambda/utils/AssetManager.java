package com.github.ulambda.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class AssetManager {
    public final static Path resourcePath(){
        return Paths.get("").toAbsolutePath().resolve("src/resources/");
    }

    public final static File getFileResource(String fileName){
        return resourcePath().resolve(fileName).toFile();
    }
    
    public final static JSONObject getJSONResource(String fileName){
        try{
            File resource = AssetManager.getFileResource(fileName);
            var bytes = Files.readAllBytes(resource.toPath());
            String content = new String(bytes);
            return new JSONObject(content);
        }
        catch(Throwable t){ throw new RuntimeException(t); }
    }
}
