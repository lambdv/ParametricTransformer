package com.github.lambdv.core;

public enum Rarity {
    OneStar(1),
    TwoStar(2),
    ThreeStar(3),
    FourStar(4),
    FiveStar(5);
    int value;
    private Rarity(int value){this.value=value;}
    public int value(){return value;}
}
