package com.github.ulambda.core;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;
import com.github.ulambda.core.Character;
import java.util.*;
import com.github.ulambda.core.Weapons;
import com.github.ulambda.utils.StandardUtils;

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
        var flattenName = StandardUtils.flattenName(name); //normalize name
        if(cache.containsKey(flattenName)) //check for cached CharacterKey
            return cache.get(flattenName).parseCharacter();
        try{ return Files.lines(databasePath) //read file from databasePath as a stream of lines
                .skip(1) //skip schema header
                .map(line -> line.split(",")) //split each line by comma
                .map(row -> CharacterKey.of(row)) //parse each row into a CharacterKey
                .filter(key -> StandardUtils.flattenName(key.name()).equals(flattenName))
                .reduce((a, b)->{throw new RuntimeException("Multiple characters with the same name in the database");})
                .map(key -> {
                    cache.put(flattenName, key);
                    return key.parseCharacter();
                })
                .orElseThrow(()->new RuntimeException("Character not found in database"));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    private static Character parseCharacter(String[] data){
        return new Character(
            data[0], //name
            Double.parseDouble(data[1]), //baseHP
            Double.parseDouble(data[2]), //baseATK
            Double.parseDouble(data[3]), //baseDEF
            Stat.parseStat(data[4]), //ascensionStatType
            Double.parseDouble(data[5]) //ascensionStatAmount
        );
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