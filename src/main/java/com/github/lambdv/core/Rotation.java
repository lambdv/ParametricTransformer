package com.github.lambdv.core;
import java.util.*;
import java.util.function.Function;

/**
 * External object that specifiy a sequence of damage instances.
 * @note Character and Rotation "know about each other" by having a reference to each other.
 * @note Rotations hold dynamic instructions/formulas that are executed to output a Damage Per Rotation (DPR) value.
 * @note Rotations or Damage Instances are needed by a character to calculate damage and give a target to optimize stats and gearing for.
 */
public class Rotation {
    StatTable target;
    Map<String, DamageInstance> instances;

    public Rotation(StatTable target){
        this.target = target;
        instances = new HashMap<>();
    }

    public Rotation(StatTable target, Map<String, DamageInstance> instances){
        this.target = target;
        this.instances = instances;
    }

    public Rotation add(String name, DamageInstance instance){
        instances.put(name, instance);
        return this;
    }
    
    public double compute(StatTable target){
        return instances.values().stream()
            .mapToDouble(d->d.apply(target))
            .sum();
    }

    public double compute(){
        return compute(target);
    }
}