package com.github.ulambda.core;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.github.ulambda.core.Character;
import java.util.*;
import com.github.ulambda.core.Weapons;
import com.github.ulambda.utils.StandardUtils;

/**
 * Utility class that provides a factory method for getting characters from a database.
 */
public final class Characters {
    private static final Path databasePath = Paths.get("").toAbsolutePath().resolve("src/resources/characters.csv");

    /**
     * Factory method for constructing characters from the database
     * @param name
     * @return
     */
    public static Character of(String name){
        name = StandardUtils.flattenName(name);
        



        throw new UnsupportedOperationException("Not implemented"); //TODO
    }
}