package com.github.lambdv.ParametricTransformer.core;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import com.github.lambdv.ParametricTransformer.core.Character;
import com.github.lambdv.ParametricTransformer.core.Weapons;
import com.github.lambdv.ParametricTransformer.utils.StandardUtils;

import java.util.*;

/**
 * Utility class that provides a factory method for getting characters from a database.
 */
public final class Characters {
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/data/characters.csv");
    private static final Map<String, CharacterKey> cache = new HashMap<>(); //cache for characters base stats

    /**
     * Factory method for constructing characters from the database
     * @param name
     * @return
     */
    public static Character of(String name){
        var flattenName = StandardUtils.flattenName(name);
        if(!cache.containsKey(flattenName))
            cache(name);
        return cache.get(flattenName).parseCharacter();
    }

    public static Character cache(String name){
        var flattenName = StandardUtils.flattenName(name); //normalize name
        try{ 
            return Files.lines(databasePath)//read file from databasePath as a stream of lines
                .skip(1) //skip schema header
                //.parallel()
                .map(line -> line.split(",")) //split each line by comma
                .filter(row -> StandardUtils.flattenName(row[0]).equals(flattenName))
                .map(row -> CharacterKey.of(row)) //parse each row into a CharacterKey
                .reduce((a, b)->{throw new RuntimeException("Multiple characters with the same name in the database");})
                .map(key -> {
                    cache.put(flattenName, key);
                    return key.parseCharacter();
                })
                .orElseThrow(()->new RuntimeException("Character not found in database"));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    public static void cache(String... names){
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Type for storing character base stats from the database
     * Also provided a way to build a new character from the base stats
     */
    record CharacterKey(String name, double baseHP, double baseATK, double baseDEF, Stat ascensionStatType, double ascensionStatAmount) {
        public static CharacterKey of(String[] data){
            return new CharacterKey(
                data[0], //name
                Double.parseDouble(data[1]), //baseHP
                Double.parseDouble(data[2]), //baseATK
                Double.parseDouble(data[3]), //baseDEF
                Stat.parseStat(data[4]), //ascensionStatType
                Double.parseDouble(data[5]) //ascensionStatAmount
            );
        }
        public Character parseCharacter(){
            return new Character(name(), baseHP(), baseATK(), baseDEF(), ascensionStatType(), ascensionStatAmount());
        }
    }
}