package com.github.lambdv.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.lambdv.core.Stat;
import com.github.lambdv.core.Weapons;

public class WeaponTest {
    /**
    * Weapons.of() Factory Method can create Weapon object from CSV database with a given name
    */
    @Test public void ofMethodWeaponObjectFromCSV(){
        var w = Weapons.of("Ibis Piercer"); //get weapon from database
        //check values are correct
        assertTrue(w.name().equals("Ibis Piercer"));
        assertTrue(w.baseATK() == 565);
        assertTrue(w.mainStatType().equals(Stat.ATKPercent));
        assertTrue(w.mainStatAmount() == 0.276);
    }

    /**
    * Check cached weapons have the same reference
    */
    @Test public void ofMethodPointerEquality(){
        //get the same weapon twice
        var w1 = Weapons.of("Ibis Piercer");
        var w2 = Weapons.of("Ibis Piercer");
        assertTrue(w1 == w2); //check for pointer equality
        var w3 = Weapons.of("Mistsplitter Reforged");
        assertTrue(w1 != w3); //check for pointer inequality
    }


    @Test public void multiOfMethod(){
        // var w = Weapons.of("Ibis Piercer", "Mistsplitter Reforged");
        // assertNotNull(w);
        // assertEquals(w.length, 2);
        // assertTrue(w[0].name().equals("Ibis Piercer"));
        // assertEquals(w[0].baseATK(), 565.0);
        // assertTrue(w[1].name().equals("Mistsplitter Reforged"));
        // assertEquals(w[1].baseATK(), 674);
    }
}