package com.github.ulambda.core;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.github.ulambda.core.Character;
import java.util.*;
import com.github.ulambda.core.Weapons;
import com.github.ulambda.utils.StandardUtils;

/**
 * Utility class that provides a factory method for getting characters from a database.
 */
public final class Characters {
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/characters.csv");
    private static final Map<String, String[]> cache = new HashMap<>(); //cache for characters base stats

    /**
     * Factory method for constructing characters from the database
     * @param name
     * @return
     */
    public static Character of(String name){
        name = StandardUtils.flattenName(name);
        
        if(cache.containsKey(name))
            return parseCharacter(cache.get(name));

        Optional<Character> character = Optional.empty();
        
        try (Scanner scanner = new Scanner(databasePath)){
            scanner.nextLine(); //slip schema header
            while (scanner.hasNextLine()) {
                var row = scanner.nextLine().split(", "); //name, baseHP, baseATK, baseDEF, ascensionStatType, ascensionStatAmount
                String characterName = StandardUtils.flattenName(row[0]);
                if (!characterName.equals(name)) 
                    continue;
                cache.put(name, row);
                return parseCharacter(row);
            }
        }

        catch (Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
        return character.orElseThrow(()->new RuntimeException("Character not found in database"));
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
}