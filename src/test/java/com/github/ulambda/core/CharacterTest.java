package com.github.ulambda.core;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import com.github.ulambda.core.Character;
import com.github.ulambda.core.Characters;
import com.github.ulambda.core.*;

public class CharacterTest {
    @Test public void CreateCharacterWithConstructor(){
        Character c = new Character("", 10, 20, 30, Stat.BaseHP, 100);
    }

    @Test public void CreateCharacterWithFactoryMethod(){
        Character c = Characters.of("Diluc");
        //System.out.println(c);
        assertEquals("Diluc", c.name);
        assertEquals(14268, c.getStat(Stat.BaseHP), 0);
        assertEquals(415, c.getStat(Stat.BaseATK), 0);
        assertEquals(876, c.getStat(Stat.BaseDEF), 0);
        assertEquals(Stat.CritRate, c.ascensionStatType);
        assertEquals(0.242+0.05, c.getStat(Stat.CritRate), 0);
        assertEquals(0.5, c.getStat(Stat.CritDMG), 0);
        
        Character c2 = Characters.of("Diluc");
        assertTrue(c!=c2); //factory method creates new instance, only the base stats are cached
    }
}