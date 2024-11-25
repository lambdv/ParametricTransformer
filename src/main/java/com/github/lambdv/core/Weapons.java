package com.github.lambdv.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.lambdv.utils.*;

/**
 * Utility class that provides a factory method for getting weapons from a database.
 */
public final class Weapons {
    private static final Map<String, Weapon> cache = new HashMap<>(); //cache for weapons
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/data/weapons.csv");
    
    /**
     * Factory method for getting a weapon from the database
     * @note name is case-insensitive and special characters are ignored
     * @note method reads the database and constructs a weapon object: this object is cached and any method call with the same name will return the same object
     * @param name
     * @return Weapon
     * @throws RuntimeException exception is leaked if weapon is not found: program should crash
     */
    public static Weapon of(String name) {
        var normalizedName = StandardUtils.flattenName(name);
        if(cache.containsKey(normalizedName))
            return cache.get(normalizedName);
        try {
            return Files.lines(databasePath)
                .parallel()
                .skip(1)
                .map(line -> line.split(", "))
                .filter(line -> StandardUtils.flattenName(line[0]).equals(normalizedName))
                .map(Weapons::parseWeapon)
                .reduce ((a, b) -> { throw new RuntimeException("Multiple weapons found with name: " + name); })
                .map(w -> { cache.put(normalizedName, w); return w; })
                .orElseThrow(() -> new RuntimeException("Weapon not found in database"));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    /**
     * Factory method for caching multiple weapons from the database in 1 loop and returns a map of weapons
     * @param names
     * @return
     * @throws RuntimeException exception is leaked if weapon is not found: program should crash
     */
    public static Map<String, Weapon> of(String... names){
        try {   
            Files.lines(databasePath)
                .parallel()
                .skip(1)
                .map(line -> line.split(", "))
                .filter(line -> Arrays.stream(names).anyMatch(name -> StandardUtils.flattenName(line[0]).equals(StandardUtils.flattenName(name))))
                .map(Weapons::parseWeapon)
                .forEach(w -> cache.put(StandardUtils.flattenName(w.name()), w));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
        return Collections.unmodifiableMap(cache);
    }

    public static void cacheAll(){
        try {
            Files.lines(databasePath)
                .parallel()
                .skip(1)
                .map(line -> line.split(", "))
                .map(Weapons::parseWeapon)
                .forEach(w -> cache.put(StandardUtils.flattenName(w.name()), w));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    private static Weapon parseWeapon(String[] data){
        return new Weapon(
            data[0], //weapon name
            0, //rarity
            90, //level
            5, //refinement
            Double.parseDouble(data[1]), //baseATK
            Stat.parseStat(data[2]), //mainStatType
            data[3].contains("%") ? //mainStatAmount
                Double.parseDouble(data[3].replaceAll("%", "")) / 100 :
                Double.parseDouble(data[3].replaceAll("%", "")) 
        );
    }

    public static boolean partialMatch(String needle, String haystack){
        var narray = StandardUtils.flattenName(needle).toCharArray();
        var harray = StandardUtils.flattenName(haystack).toCharArray();

        int nidx = 0;
        for(int i = 0; i < harray.length; i++){
            if(harray[i] == narray[nidx]){
                nidx++;
                if(nidx == narray.length) return true;
            }
        }
        return false;
    } 
    private Weapons(){}
}