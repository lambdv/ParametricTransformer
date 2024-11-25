package com.github.lambdv.core;
import java.util.function.Function;

/**
 * 
 */
public interface DamageInstance extends Function<StatTable, Double>{
    Double apply(StatTable target);
} 

enum DamageType{
    Physical,
    Pyro,
    Hydro,
    Electro,
    Anemo,
    Geo,
    Dendro,
    Cryo,
    NormalATK,
    ChargedATK,
    PlungingATK,
    ElementalSkill,
    ElementalBurst,
    Reaction,
    Vape,
    Melt,
    Overloaded,
    Superconduct,
    Electrocharged,
    Swirl,
    Shattered,
    ATKScaling,
    DEFScaling,
    HPScaling,
}