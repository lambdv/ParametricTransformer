package com.github.lambdv.core;
import java.util.Map;
public record StatPair<T extends Number>(Stat type, T amount) implements Map.Entry<Stat, T> {
    @Override public Stat getKey() { return type; }
    @Override public T getValue() { return amount; }
    @Override public T setValue(T value) {
        throw new UnsupportedOperationException("StatPair is immutable");
    }
}