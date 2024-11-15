package com.github.ulambda.core;

import java.util.*;

public class ArtifactBuilder {
    Flower flower;
    Feather feather;
    Sands sands;
    Goblet goblet;
    Circlet circlet;

    //mock substats
    Map<Stat, Integer> substatRolls; //current number of substat rolls
    Map<Stat, Integer> substatConstraints; //number of substat rolls for each stat type that are left


    ArtifactBuilder(Flower flower, Feather feather, Sands sands, Goblet goblet, Circlet circlet){
        this.flower = flower;
        this.feather = feather;
        this.sands = sands;
        this.goblet = goblet;
        this.circlet = circlet;
    }

    public ArtifactBuilder(Stat sandsStat, Stat gobletStat, Stat circletStat){
        this(
            new Flower(ArtifactSet.empty(), 5, 20),
            new Feather(ArtifactSet.empty(), 5, 20),
            new Sands(ArtifactSet.empty(), 5, 20, sandsStat),
            new Goblet(ArtifactSet.empty(), 5, 20, gobletStat),
            new Circlet(ArtifactSet.empty(), 5, 20, circletStat)
        );
    }
    
}

