package com.github.lambdv.core;
import java.util.*;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Function;

/**
 * External object that specifiy a sequence of damage instances.
 * @note Character and Rotation "know about each other" by having a reference to each other.
 * @note Rotations hold dynamic instructions/formulas that are executed to output a Damage Per Rotation (DPR) value.
 * @note Rotations or Damage Instances are needed by a character to calculate damage and give a target to optimize stats and gearing for.
 */
public record Rotation(Map<String, DamageInstance> actions){
    public Rotation(){
        this(new HashMap<>());
    }
    public double compute(StatTable target){
        return actions.values().parallelStream()
            .mapToDouble(f->f.apply(target))
            .sum();
    }

    public double compute(StatTable target, StatTable... buffs){
        var total = new BuffedStatTable(target, buffs);
        return actions.values().parallelStream()
            .mapToDouble(f->f.apply(total))
            .sum();
    }
    public Rotation add(String name, DamageInstance instance){
        actions.put(name, instance); return this;
    }
} 