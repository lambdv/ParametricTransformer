package com.github.lambdv.core;
import java.util.*;
import java.util.stream.Stream;

// public class ArtifactSubStats implements StatTable{
//     Map<Stat, Double> substats;
//     public Map<Stat, Double> stats() {
//         return Collections.unmodifiableMap(substats);
//     }
//     private static Stream<Stat> possibleSubStats(){
//         return List.of(
//             Stat.HPPercent, 
//             Stat.FlatHP,
//             Stat.ATKPercent,
//             Stat.FlatATK,
//             Stat.DEFPercent,
//             Stat.FlatDEF,
//             Stat.ElementalMastery,
//             Stat.CritRate,
//             Stat.CritDMG,
//             Stat.EnergyRecharge
//         ).stream();
//     }
// }