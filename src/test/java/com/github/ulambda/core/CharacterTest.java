package com.github.ulambda.core;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.github.ulambda.core.Character;

public class CharacterTest {
    @Test public void CreateCharacterWithConstructor(){
        Character c = new Character(10, 20, 30, Stat.BaseHP, 100);
    }
}