package com.github.lambdv.ParametricTransformer.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import com.github.lambdv.ParametricTransformer.core.*;
import java.util.Map;

/**
 * Tests for the DamageInstance class
 */
public class DamageInstanceTest {
    @Test public void testDamageInstance(){
        var c = Characters.of("amber").build();
        double dmg = DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1, StatTable.empty()).apply(c);
        //System.out.println(dmg);
    }
}
