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
            "e", DamageFormulas.DefaultATKFormula(1, 1.0, ()->Map.of())
        ));
        assertEquals(124.274469230769, r.compute(), 0.0001);
    }

    @Test public void NakedBuffedManualRotationCreationAndExecution(){
        StatTable amber = Characters.of("amber").build();

        var buffs = Map.of(
            Stat.ATKPercent, 0.1,
            Stat.CritRate, 0.1,
            Stat.CritDMG, 0.1
        );

        var total = Characters.of("amber").add(()->buffs).build();
        assertEquals(Formulas.totalATK(total), 298.820, 0.00000001);
        assertEquals(total.get(Stat.CritRate), 0.15, 0.00000001);
        assertEquals(total.get(Stat.CritDMG), 0.6, 0.00000001);

        Rotation r = new Rotation(amber, Map.of(
            "test", DamageFormulas.DefaultATKFormula(1, 1.0, ()->buffs)
        ));

        double dmg = r.compute();
        assertEquals(142.812973846, dmg, 0.0001);
        System.out.println(dmg);
    }
}



