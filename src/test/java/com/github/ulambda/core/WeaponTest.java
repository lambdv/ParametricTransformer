package com.github.ulambda.core;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class WeaponTest {
    /**Weapons.of() can get weapons from db as intended*/
    @Test public void Test1(){
        var w = Weapons.of("Ibis Piercer");
        assertTrue(w.name().equals("Ibis Piercer"));
        assertTrue(w.baseATK() == 565);
        assertTrue(w.mainStatType().equals(Stat.ATKPercent));
        assertTrue(w.mainStatAmount() == 0.276);
    }

    /**cached pointer equality check*/
    @Test public void Test2(){
        var w1 = Weapons.of("Ibis Piercer");
        var w2 = Weapons.of("Ibis Piercer");
        assertTrue(w1 == w2);
        var w3 = Weapons.of("Mistsplitter Reforged");
        assertTrue(w1 != w3);
    }
}
