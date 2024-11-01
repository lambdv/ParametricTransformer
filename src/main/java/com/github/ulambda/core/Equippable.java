package com.github.ulambda.core;
import java.util.Map;

public interface Equippable extends StatTable{
    @Override public default double addStat(Stat type, double amount){
        throw new UnsupportedOperationException("Cannot add stats to an Equippable object");
    }
}
