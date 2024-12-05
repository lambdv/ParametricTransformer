package com.github.lambdv.core;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.github.lambdv.core.Stat;

/**
 * Tests for the Stat class
 */
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
