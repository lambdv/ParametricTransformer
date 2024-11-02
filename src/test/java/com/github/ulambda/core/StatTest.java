package com.github.ulambda.core;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StatTest {
    /**
     * Stat.parseStat() can convert string to Stat enum as intended
     */
    @Test public void parseStatConvertingStringToEnum(){
        assert Stat.parseStat("cr") == Stat.CritRate;
        assert Stat.parseStat("CR%") == Stat.CritRate;
        assert Stat.parseStat("cd") == Stat.CritDMG;
        assert Stat.parseStat("CD%") == Stat.CritDMG;
        assert Stat.parseStat("em") == Stat.ElementalMastery;
        assert Stat.parseStat("hb%") == Stat.HealingBonus;
    }
    
}
