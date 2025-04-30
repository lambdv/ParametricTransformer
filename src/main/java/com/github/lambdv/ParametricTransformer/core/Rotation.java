package com.github.lambdv.ParametricTransformer.core;
import java.util.*;
import java.util.function.Function;

/**
 * External object that specifiy a sequence of damage instances.
 * @note Character and Rotation "know about each other" by having a reference to each other.
 * @note Rotations hold dynamic instructions/formulas that are executed to output a Damage Per Rotation (DPR) value.
 * @note Rotations or Damage Instances are needed by a character to calculate damage and give a target to optimize stats and gearing for.
 */
public record Rotation(Map<String, DamageInstance> actions){
    /**
     * Create an empty rotation
     */
    public Rotation(){
        this(new HashMap<>());
    }

    /**
     * Compute the total damage of the rotation
     * @param target
     * @return
     */
    public double compute(StatTable target){
        return actions.values().stream()
            .mapToDouble(f->f.apply(target))
            .sum();
    }

    /**
     * Compute the total damage of the rotation with buffs
     * @param target
     * @param buffs
     * @return
     */
    public double compute(StatTable target, StatTable... buffs){
        var total = new BuffedStatTable(target, buffs);
        return actions.values().stream()
            .mapToDouble(f->f.apply(total))
            .sum();
    }

    /**
     * Add a damage instance to the rotation
     * @param name
     * @param instance
     * @return
     */
    public Rotation add(String name, DamageInstance instance){
        if(actions.containsKey(name))
            throw new IllegalArgumentException("Action name already exists: " + name);
        actions.computeIfAbsent(name, (k)->instance);
        return this;
    }

    /**
     * Add a damage instance to the rotation with the key being a random UUID
     * @param instance
     * @return
     */
    public Rotation add(DamageInstance instance) {
        var hash = UUID.randomUUID().toString();
        actions.computeIfAbsent(hash, (k) -> instance);
        return this;
    }
} 