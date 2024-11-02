package com.github.ulambda.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class that provides a factory method for getting weapons from a database.
 */
public final class Weapons {
    private static final Map<String, Weapon> cache = new HashMap<>(); //cache for weapons
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/main/java/com/github/ulambda/resources/weapons.csv");

    /**
     * Factory method for getting a weapon from the database
     * @param name
     * @return Weapon
     */
    public static Weapon of(String name) {
        name = flattenName(name);
        if(cache.containsKey(name))
            return cache.get(name);
        Optional<Weapon> weapon = Optional.empty();
        try (Scanner scanner = new Scanner(databasePath)){
            scanner.nextLine(); //slip schema header
            //Optional<Weapon> partialMatch = Optional.empty();
            while (scanner.hasNextLine()) {
                String[] row = scanner.nextLine().split(", "); //WeaponName, baseATK, stat, statvalue
                String weaponName = flattenName(row[0]);
                if (!weaponName.equals(name)) 
                    continue;
                weapon = Optional.of(parseWeapon(row));
                cache.put(name, weapon.get());
                break;
            }
            scanner.close();
        }
        catch (Exception e){ throw new RuntimeException("Error reading database"); }
        if(weapon.isEmpty()){ throw new RuntimeException("Weapon not found in database"); }
        return weapon.get();
    }

    public static Weapon[] of(String name, String... names){
        throw new UnsupportedOperationException("Not implemented"); //TODO
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

    private static String flattenName(String name){
        return name.toLowerCase()
            .replaceAll("\\s", "")
            .replaceAll("[^a-zA-Z0-9]", "");
    }
}