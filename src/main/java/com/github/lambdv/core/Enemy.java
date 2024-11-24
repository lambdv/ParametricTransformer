package com.github.lambdv.core;

public record Enemy(
    String name, 
    int level, 
    double baseHP,
    double baseATK, 
    double baseDEF, 
    double pyroRes,
    double cryoRes,
    double hydroRes,
    double electroRes,
    double anemoRes,
    double geoRes,
    double dendroRes,
    double physicalRes,
    double universalRes
) {   
    private static final Enemy KQMC = new Enemy("", 100, 0, 0, 1000, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1);
    public static Enemy KQMC(){ return KQMC; }
}