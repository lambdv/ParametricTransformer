package dpscalc.test;

import dpscalc.core.Stat;
import dpscalc.core.Equippable;
import dpscalc.core.Weapon;
import dpscalc.core.Weapons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class WeaponTest {
    @Test public void test_01() throws Throwable{

        Weapon w = Weapons.of("Key of Khaj-Nisut");
        assertEquals(w.name(), "Key of Khaj-Nisut");
        assertEquals(w.baseATK(), 542);
        assertEquals(w.mainStatType(), Stat.type.HPPercent);
        double expected = 0.662;
        assertEquals(expected, w.mainStatAmount());
        
        Weapon w2 = Weapons.of("Key of Khaj-Nisut");
        assert w == w2; //pointer equality from cache
    }
}
