package com.github.lambdv.core;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.lambdv.core.*;
import com.github.lambdv.core.Character;
import com.github.lambdv.core.Characters;
import com.github.lambdv.core.Weapon;
import com.github.lambdv.core.Formulas;

/**
 * Test for the Character class and Characters class factory method
 */
public class CharacterTest {
    @Test public void CreateCharacterWithConstructor(){
        Character c = new Character("", 10, 20, 30, Stat.BaseHP, 100);
    }

    @Test public void CreateCharacterWithFactoryMethod(){
        Character c = Characters.of("Diluc");
        //System.out.println(c);
        assertEquals("Diluc", c.name);
        assertEquals(12981, c.get(Stat.BaseHP), 0);
        assertEquals(335, c.get(Stat.BaseATK), 0);
        assertEquals(784, c.get(Stat.BaseDEF), 0);
        assertEquals(Stat.CritRate, c.ascensionStatType);
        assertEquals(0.192+0.05, c.get(Stat.CritRate), 0);
        assertEquals(0.5, c.get(Stat.CritDMG), 0);
        
        Character c2 = Characters.of("Diluc");
        assertTrue(c!=c2); //factory method creates new instance, only the base stats are cached
    }

    @Test public void EquipingWeapon(){
        Character c = Characters.of("Diluc");
        Weapon w = Weapons.of("Wolf's Gravestone");
        var before = c.get(Stat.BaseATK);
        c.equip(w);
        var after = c.get(Stat.BaseATK);
        assertEquals(w, c.weapon().get());
        assertEquals(before+608.0, after, 0);
        c.unequipWeapon();
        assertFalse(c.weapon().isPresent());
        assertEquals(c.get(Stat.BaseATK), before, 0);        
    }

    @Test public void EquipingArtifact(){
        Character c = Characters.of("Diluc");
        Flower f = new Flower(ArtifactSet.empty(), 5, 20);
        var before = c.get(Stat.FlatHP);
        c.equip(f);
        assert c.flower().isPresent();
        assert c.flower().get() == f;
        var after = c.get(Stat.FlatHP);
        assertEquals(f, c.flower().get());
        //System.out.println(before);
        //System.out.println(f.getStat(Stat.BaseHP));
        assertEquals(before+4780.0, after, 0);
        c.unequipFlower();
        assertFalse(c.flower().isPresent());
        assertEquals(c.get(Stat.FlatHP), before, 0);
    }

    @Test public void dynamicFluidStats(){
        Character c = Characters.of("hutao");
        assertEquals(0.884, c.get(Stat.CritDMG), 0.00001);
        assertEquals(15552, c.get(Stat.BaseHP), 0.00001);
        assertEquals(106.43, c.get(Stat.BaseATK), 0.00001);
        assertEquals(0.05, c.get(Stat.CritRate), 0.00001);
        assertEquals(0.884, c.get(Stat.CritDMG), 0.00001);
        assertEquals(0.0, c.get(Stat.ElementalDMGBonus), 0.00001);
        //assertEquals(106, Formulas.totalATK(c), 0.00001);
        var rot = new Rotation()
            .add("t", DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1.00, StatTable.of()));
        var before = rot.compute(c);
        assertEquals(48, (int) before, 0.00000001);    
        c.add(Stat.FlatATK, (s)->Formulas.totalHP(c)*0.0596);
        var after = rot.compute(c);
        assertEquals(473, (int) after, 0.00000001);
        //c.add(Stat.HPPercent, 0.2);
        c.add(Stat.HPPercent, (s)->0.2);
        var after2 = rot.compute(c);
        assertEquals(557, (int) after2, 0.00000001);
    }


}