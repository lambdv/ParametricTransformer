package dpscalc.core;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class for creating Weapon objects from a database
 */
public final class Weapons {
    private static final Map<String, Weapon> cache = new HashMap<>(); //cache for weapons
    private static final Path databasePath = Path.of("src/resources/data/weapons.csv");
    private static final Pattern schema = Pattern.compile("^[a-zA-Z0-9]+, [0-9]+, [a-zA-Z]+, [0-9]+(\\.[0-9]+)?%?$"); 

    private Weapons(){}

    public static Weapon of(String name) {
        name = flattenName(name);

        // if(cache.containsKey(name))
        //     return cache.get(name);

        Optional<Weapon> weapon = Optional.empty();

        weapon = getCached(name);

        if (weapon.isPresent())
            return weapon.get();

        try (Scanner scanner = new Scanner(databasePath)){
            scanner.nextLine(); //slip schema header
            
            //Stack<String> partialMatches = new Stack<>();

            while (scanner.hasNextLine()) {
                //WeaponName, baseATK, stat, statvalue
                String[] row = scanner.nextLine().split(", ");
                String weaponName = row[0];
                String flatName = flattenName(weaponName);

                // if (flatName.contains(name))
                //     partialMatches.push(weaponName);

                if (!flatName.equals(name)) 
                    continue;
                    
                weapon = Optional.of(parseWeapon(row));
                cache.put(name, weapon.get());
                break;
            }
            scanner.close();
        }
        catch (Exception e){ 
            e.printStackTrace();
            throw new ResourceNotFound("Error reading database");
        }

        if(weapon.isEmpty())
            throw new ResourceNotFound("Weapon not found in database");
        
        return weapon.get();
    }

    private static String flattenName(String name){
        name = name.toLowerCase(); //case insensitive
        name = name.replaceAll("\\s", ""); //remove spaces
        name = name.replaceAll("[^a-zA-Z0-9]", ""); //only allow alphanumeric characters
        return name;
    }

    private static Optional<Weapon> getCached(String name){
        if (cache.containsKey(name))
            return Optional.of(cache.get(name)); 
    
        // List<Weapon> matches = cache.keySet().stream()
        //     .filter(key -> key.contains(name))
        //     .map(key -> cache.get(key))
        //     .toList();
        
        // if (matches.size() == 1){
        //     var weapon = matches.get(0);
        //     cache.put(name, weapon);
        //     return Optional.of(weapon);
        // }
            
        return Optional.empty();
    }

    private static Weapon parseWeapon(String[] data){
        String weaponName = data[0];
        int rarity = 0; //TODO
        int level = 90; 
        int refinement = 5; 
        double baseATK = Double.parseDouble(data[1]);
        Stat.type mainStatType = Stat.parseStatType(data[2]);

        String mainStatAmountString = data[3];
        double mainStatAmount = 0;

        if(mainStatAmountString.contains("%"))
            mainStatAmount = Double.parseDouble(mainStatAmountString.replaceAll("%", "")) / 100;
        else
            mainStatAmount = Double.parseDouble(mainStatAmountString);

        return new Weapon(weaponName, rarity, level, refinement, baseATK, mainStatType, mainStatAmount);
    }


}
