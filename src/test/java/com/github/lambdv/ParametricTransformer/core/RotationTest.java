package com.github.lambdv.ParametricTransformer.core;
import com.github.lambdv.ParametricTransformer.core.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Tests for the Rotation class and checking results with other calculations
 */
public class RotationTest {
    @Test public void NakedManualRotationCreationAndExecution(){
        StatTable target = Characters.of("amber")
            .build();
        Rotation r = new Rotation(Map.of(
            "e", DamageInstance.of(Element.Pyro, DamageType.Skill, BaseScaling.ATK, Amplifier.None, 1.0, 1.0, StatTable.empty())
        ));
        assertEquals(124.274469230769, r.compute(target), 0.0001);
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

        Rotation r = new Rotation(Map.of(
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

        double dmg = r.compute(amber);
        assertEquals(142.812973846 * r.actions().size(), dmg, 0.0001);
        //System.out.println(dmg);
    }
}



