package com.github.lambdv.core;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import com.github.lambdv.core.*;
import java.util.Map;

public class DamageInstanceTest {
    @Test public void testDamageInstance(){
        var c = Characters.of("amber").build();
        double dmg = DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1, StatTable.empty()).apply(c);
        //System.out.println(dmg);
    }
}
