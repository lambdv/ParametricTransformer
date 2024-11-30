package com.github.lambdv.core;
import com.github.lambdv.core.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import java.util.Map;


public class RotationTest {
    @Test public void NakedManualRotationCreationAndExecution(){
        StatTable target = Characters.of("amber")
            .build();
        Rotation r = new Rotation(target, Map.of(
            "e", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, StatTable.empty())
        ));
        assertEquals(124.274469230769, r.compute(), 0.0001);
    }

    @Test public void NakedBuffedManualRotationCreationAndExecution(){
        StatTable amber = Characters.of("amber").build();

        var buffs = StatTable.of(
            Stat.ATKPercent, 0.1,
            Stat.CritRate, 0.1,
            Stat.CritDMG, 0.1
        );

        var total = Characters.of("amber").add(buffs).build();
        assertEquals(Formulas.totalATK(total), 298.820, 0.00000001);
        assertEquals(total.get(Stat.CritRate), 0.15, 0.00000001);
        assertEquals(total.get(Stat.CritDMG), 0.6, 0.00000001);

        Rotation r = new Rotation(amber, Map.of(
            "test", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "e", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "q", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "a", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "1", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "2", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "3", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "4", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "5", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs),
            "6", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, buffs)
        ));

        double dmg = r.compute();
        assertEquals(142.812973846 * r.instances.size(), dmg, 0.0001);
        //System.out.println(dmg);
    }

    @Test public void ManualKQMCCharacterBuiltRotation(){
        StatTable amber = Characters.of("diluc")
            .equip(Weapons.of("rainslasher"))
            .add(()->Map.of(
                Stat.ATKPercent, 0.1,
                Stat.CritRate, 0.1,
                Stat.CritDMG, 0.1
            ))
            .build();

        Rotation r = new Rotation(amber, Map.of(
            "e", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1, 1.0, StatTable.empty()),
            "q", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1, 1.0, StatTable.empty()),
            "a", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1, 1.0, StatTable.empty()),
            "c1", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1, 1.0, StatTable.empty()),
            "c2", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1, 1.0, StatTable.empty()),
            "c3", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, StatTable.empty()),
            "c4", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, StatTable.empty())
        ));
        //assertEquals(142.812973846*r.instances.size(), r.compute(), 0.0001);
    }
}



