package com.github.ulambda.core;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class StatTest {
    /**
     * Stat.parseStat() can convert string to Stat enum as intended
     */
    @Test public void parseStatConvertingStringToEnum(){
        assertTrue(Stat.parseStat("cr") == Stat.CritRate);
        assertTrue(Stat.parseStat("CR%") == Stat.CritRate);
        assertTrue(Stat.parseStat("cd") == Stat.CritDMG);
        assertTrue(Stat.parseStat("CD%") == Stat.CritDMG);
        assertTrue(Stat.parseStat("em") == Stat.ElementalMastery);
        assertTrue(Stat.parseStat("hb%") == Stat.HealingBonus);
    }
}
