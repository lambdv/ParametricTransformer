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
    private static volatile Map<String, Weapon> cache = Collections.synchronizedMap(new HashMap<String, Weapon>()); //cache for weapons
    //private static final Map<String, Weapon> cache = Collections.synchronizedMap(new HashMap<String, Weapon>()); //cache for weapons

    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/data/weapons.csv");
    public enum WeaponFactory{ Instance; public Weapon get(String name){return Weapons.of(name);} }
    
    /**
     * Factory method for getting a weapon from the database
     * @note name is case-insensitive and special characters are ignored
     * @note method reads the database and constructs a weapon object: this object is cached and any method call with the same name will return the same object
     * @param name
     * @return Weapon
     * @throws RuntimeException exception is leaked if weapon is not found: program should crash
     */
    public static Weapon of(String name) {
        var flatName = StandardUtils.flattenName(name);
        if(!cache.containsKey(flatName)) 
            Weapons.cashe(flatName);
        return cache.get(flatName);
    }

    /**
     * Factory method for caching multiple weapons from the database in 1 loop and returns a map of weapons
     * @param names
     * @return
     * @throws RuntimeException exception is leaked if weapon is not found: program should crash
     */
    public static WeaponFactory of(String... names){
        Weapons.cashe(names);
        return WeaponFactory.Instance;
    }

    public static void cashe(String name){
        var normalizedName = StandardUtils.flattenName(name);
        if(cache.containsKey(normalizedName)) return;
        try {
            Files.lines(databasePath)
                .skip(1)
                //.parallel()
                .map(line -> line.split(", "))
                .filter(line -> StandardUtils.flattenName(line[0]).equals(normalizedName) 
                    || Weapons.partialMatch(normalizedName, line[0]) //partial match
                )
                .map(Weapons::parseWeapon)
                .reduce ((x,y)->{throw new RuntimeException("Multiple weapons found with name: " + name);})
                .map(w -> { cache.put(normalizedName, w); return w; } )
                .orElseThrow(() -> new RuntimeException("Weapon not found in database"));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    public static void cashe(String... names){
        String[] namesToFind = Arrays.stream(names)
            .map(StandardUtils::flattenName)
            .filter(n -> !cache.containsKey(n))
            .toArray(String[]::new);
        if(namesToFind.length == 0) return;
        if(Arrays.stream(namesToFind).distinct().count() != namesToFind.length)
            throw new RuntimeException("Duplicate names found in list: " + Arrays.toString(namesToFind));
        try {   
            Files.lines(databasePath)
                .skip(1)
                .parallel()
                .map(line -> line.split(", "))
                .filter(line -> {
                    var lineFlatName = StandardUtils.flattenName(line[0]);
                    //final var c = Weapons.cache;
                    //if(c.containsKey(lineFlatName)) 
                        //throw new RuntimeException("Multiple weapons found with name: " + line[0] + "in db. Provide a more specific name.");
                    return Arrays.stream(namesToFind).anyMatch(name -> lineFlatName.equals(name));
                })
                .map(Weapons::parseWeapon)
                .forEach(w -> cache.put(StandardUtils.flattenName(w.name()), w));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    public static void cacheAll(){
        try {
            Files.lines(databasePath)
                .parallel()
                .skip(1)
                .map(line -> line.split(", "))
                .map(Weapons::parseWeapon)
                //.forEach(w -> cache.put(StandardUtils.flattenName(w.name()), w));
                ;
            }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
    }

    public static boolean isCached(String name){
        return cache.containsKey(StandardUtils.flattenName(name));
    }

    public static boolean isCached(Weapon weapon){
        return isCached(weapon.name()) && cache.values().stream().anyMatch(w -> w.equals(weapon));
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