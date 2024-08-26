package dpscalc.test;
import dpscalc.core.Stat;

import org.junit.jupiter.api.Test;

public class StatTest {
    
    /**
     * testing that the add method results in a new stat where the values are the sum of the orignal two
     */
    @Test public void test1(){
        Stat a = new Stat.ATKPercent(10);
        Stat b = new Stat.ATKPercent(20);
        Stat c = a.add(b);
        assert c.amount() == 30;
    }

    /**
     * tests that two different stats cannot be added together
     */
    @Test public void test2(){
        Stat a = new Stat.FlatATK(10);
        Stat b = new Stat.CritDMG(120);
        try{
            Stat c = a.add(b);
        }
        catch(IllegalArgumentException e) { return; }
        assert false;
    }

    /**
     * tests that the other stat cannot be a null
     */
    @Test public void test3(){
        Stat a = new Stat.FlatATK(10);
        Stat b = null;
        try{
            Stat c = a.add(b);
        }
        catch(NullPointerException e) { return; }
        assert false;
    }

    /**
     * testing that subtract works as intended
     */
    // @Test public void test4(){
    //     Stat a = new Stat.CritRate(5);
    //     Stat b = new Stat.CritRate(5);
    //     assert a.subtract(b).value() == 0;
    // }

    /**
     * testing that the parseStatType method works as intended
     */
    @Test public void test5(){
        assert Stat.parseStatType("cr") == Stat.type.CritRate;
        assert Stat.parseStatType("CR%") == Stat.type.CritRate;

        assert Stat.parseStatType("cd") == Stat.type.CritDMG;
        assert Stat.parseStatType("CD%") == Stat.type.CritDMG;

        assert Stat.parseStatType("em") == Stat.type.ElementalMastery;
        assert Stat.parseStatType("hb%") == Stat.type.HealingBonus;
    }

}
