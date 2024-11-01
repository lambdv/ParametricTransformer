package dpscalc.test;

import dpscalc.core.Stat;
import dpscalc.core.Character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CharacterTest {

    @Test public void test_01(){
        Character c = new Character(10,10,10, new Stat.ATKPercent(10));
    }
}
