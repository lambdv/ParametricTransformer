package com.github.lambdv.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.lambdv.core.Stat;
import com.github.lambdv.core.Weapons;

/**
 * Test for the Weapon class and Weapons class factory method
 */
public class WeaponTest {
    /**
    * Weapons.of() Factory Method can create Weapon object from CSV database with a given name
    */
    @Test public void ofMethodWeaponObjectFromCSV(){
        //var startTime = System.nanoTime();
        var w = Weapons.of("Ibis Piercer"); //get weapon from database
        //var endTime = System.nanoTime();
        //System.out.println((endTime - startTime) + "ns");
        //check values are correct
        assertTrue(w.name().equals("Ibis Piercer"));
        assertTrue(w.baseATK() == 565);
        assertTrue(w.mainStatType().equals(Stat.ATKPercent));
        assertTrue(w.mainStatAmount() == 0.276);

        try{ Weapons.of("Not a weapon"); assert false; } 
        catch (RuntimeException e){}
    }

    @Test public void ofMethodWeaponObjectFromCSV2(){
        var startTime = System.nanoTime();
        Weapons.cacheAll();
        var w = Weapons.of("Ibis Piercer"); //get weapon from database
        var endTime = System.nanoTime();
        //System.out.println((endTime - startTime) / 1_000_000_000.0 + " seconds");
        //check values are correct
        assertTrue(w.name().equals("Ibis Piercer"));
        // assertTrue(w.baseATK() == 565);
        // assertTrue(w.mainStatType().equals(Stat.ATKPercent));
        // assertTrue(w.mainStatAmount() == 0.276);

        // try{ Weapons.of("Not a weapon"); assert false; } 
        // catch (RuntimeException e){}
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
        var ws = Weapons.of("Ibis Piercer", "Mistsplitter Reforged");
        assertNotNull(ws);
        // assertTrue(ws.size() == 2);
        // assertTrue(ws.containsKey("ibispiercer"));
        // assertTrue(ws.containsKey("mistsplitterreforged"));
        assertTrue(ws.get("ibispiercer") == Weapons.of("ibis piercer"));
        assertTrue(ws.get("mistsplitterreforged") == Weapons.of("mistsplitter reforged"));
        
        try{
            Weapons.of("Not a weapon");
            assert false;
        } catch (RuntimeException e){}

        try{
            Weapons.of("Ibis Piercer", "Ibis");
            //assert false;
        } catch (RuntimeException e){}
    }

    @Test public void partialMatchTest(){
        assertTrue(Weapons.partialMatch("Ibis", "Ibis Piercer"));
        assertTrue(Weapons.partialMatch("Ibis", "Ibis"));
        assertTrue(Weapons.partialMatch("wgs", "wolf's gravestone"));
        assertTrue(Weapons.partialMatch("fav bow", "favonius warbow"));
        assertFalse(Weapons.partialMatch("fav bow", "favonius lance"));
        

    }
}