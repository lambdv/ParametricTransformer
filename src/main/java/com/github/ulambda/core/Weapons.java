package com.github.ulambda.core;

import com.github.ulambda.utils.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class that provides a factory method for getting weapons from a database.
 */
public final class Weapons {
    private static final Map<String, Weapon> cache = new HashMap<>(); //cache for weapons
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/data/weapons.csv");
    private Weapons(){}
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
            return Files.lines(databasePath).parallel()
                .skip(1)
                .map(line -> line.split(", "))
                .filter(line -> StandardUtils.flattenName(line[0]).equals(normalizedName))
                .map(Weapons::parseWeapon)
                .reduce ((a, b) -> { throw new RuntimeException("Multiple weapons found with name: " + name); })
                .map(w -> { cache.put(normalizedName, w); return w; })
                .orElseThrow(() -> new RuntimeException("Weapon not found in database"));
        }
        catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
        // Optional<Weapon> weapon = Optional.empty();
        // try (Scanner scanner = new Scanner(databasePath)){
        //     scanner.nextLine(); //slip schema header
        //     //Optional<Weapon> partialMatch = Optional.empty();
        //     while (scanner.hasNextLine()) {
        //         String[] row = scanner.nextLine().split(", "); //WeaponName, baseATK, stat, statvalue
        //         String weaponName = flattenName(row[0]);
        //         if (!weaponName.equals(name)) 
        //             continue;
        //         weapon = Optional.of(parseWeapon(row));
        //         cache.put(name, weapon.get());
        //         break;
        //     }
        //     scanner.close();
        // }
        
        // catch (Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
        // return weapon.orElseThrow(()-> new RuntimeException("Weapon not found in database"));
    }

    public static Weapon[] of(String... names){

        throw new UnsupportedOperationException("Not implemented yet");
        // List<String> nameList = Arrays.stream(names).map(Weapons::flattenName).toList();
        // try {   
        //     return Files.lines(databasePath).parallel()
        //         .skip(1)
        //         .map(line -> line.split(", "))
        //         .filter(line -> nameList.contains(flattenName(line[0])))
        //         .sorted(Comparator.comparing(line -> nameList.indexOf(flattenName(line[0]))))
        //         .map(Weapons::parseWeapon)
        //         .toArray(Weapon[]::new);
        // }

        // catch(Exception e){ throw new RuntimeException("Error reading database: " + e.getMessage()); }
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
}