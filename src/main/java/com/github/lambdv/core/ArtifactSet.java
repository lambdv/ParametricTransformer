package com.github.lambdv.core;
import java.util.Map;


public record ArtifactSet(
    String setName,
    Map<Stat, Double> twoPiece, Map<Stat,Double> fourPiece
){
    public static ArtifactSet empty(){
        return new ArtifactSet("", Map.of(), Map.of());
    }
}