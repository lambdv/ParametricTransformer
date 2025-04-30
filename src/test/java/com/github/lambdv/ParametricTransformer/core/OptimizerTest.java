package com.github.lambdv.ParametricTransformer.core;
import com.github.lambdv.ParametricTransformer.core.DamageFormulas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;


/**
 * Tests for optimization algorithms and visitor methods
 */
public class OptimizerTest {
    // @Test public void OptimizeSubStatFunctionTest(){
    //     long start = System.currentTimeMillis();
    //     var c = Characters.of("amber")
    //         .equip(Weapons.of("slingshot"))
    //         .equip(new Flower(5,20))
    //         .equip(new Feather(5,20))
    //         .equip(new Sands(5,20, Stat.ATKPercent))
    //         .equip(new Goblet(5,20, Stat.PyroDMGBonus))
    //         .equip(new Circlet(5,20, Stat.CritDMG));
    //     var r = new Rotation()
    //         .add("test", DamageFormulas.defaultPyroSkillATK(1, 2, StatTable.empty()));
    //     ArtifactBuilder subs = Optimizers.optimialArtifactSubStatDistrbution(c,r,0);
    //     long end = System.currentTimeMillis();
    //     //System.out.println("Time taken: " + (end - start) + "ms");
    //     //System.out.println(subs);
    //     //System.out.println(c.stats());
    //     assertEquals(0.20, subs.substats().get(Stat.ATKPercent), 0.01); //2 rolls + 2 fixed
    //     assertEquals(0.40, subs.substats().get(Stat.CritRate), 0.01); //10 rolls + 2 fixed
    //     assertEquals(0.662, subs.substats().get(Stat.CritDMG), 0.01); //8 rolls + 2 fixed
    // }

    // @Test public void OptimizeFiveStarMainStatFunctionTest(){
    //     Character c = Characters.of("amber").equip(Weapons.of("polarstar"));
    //     long start = System.currentTimeMillis();
    //     var bob = Optimizers.optimal5StarArtifactMainStats(
    //         c,
    //         new Rotation()
    //             .add("test", DamageFormulas.defaultPyroSkillATK(1, 2, StatTable.empty())),
    //         0
    //     );
    //     long end = System.currentTimeMillis();
    //     //System.out.println("Time taken: " + (end - start) + "ms");
    //     assert c.flower().isEmpty();
    //     assert c.feather().isEmpty();
    //     assert c.sands().isEmpty();
    //     assert c.goblet().isEmpty();
    //     assert c.circlet().isEmpty();
    
    //     //System.out.println(bob.sands().get().statType());
    //     //System.out.println(bob.goblet().get().statType());
    //     //System.out.println(bob.circlet().get().statType());
    // }

    // @Test public void ArtifactBuilderCorrectValuesForFourStarSubstat(){
    //     var bob = ArtifactBuilder.KQMC(
    //         Optional.of(new Flower(ArtifactSet.empty(), 4, 16)),
    //         Optional.of(new Feather(ArtifactSet.empty(), 4, 16)),
    //         Optional.of(new Sands(ArtifactSet.empty(), 4, 16, Stat.ATKPercent)),
    //         Optional.of(new Goblet(ArtifactSet.empty(), 4, 16, Stat.PyroDMGBonus)),
    //         Optional.of(new Circlet(ArtifactSet.empty(), 4, 16, Stat.CritRate))
    //     );

    //     assert 
        
    // }

    @Test public void AcceptArtifactOptimizer(){
        Rotation r = new Rotation()
            .add("t", DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1.00, StatTable.of()));
        var c = Characters.of("hutao")
            .equip(Weapons.of("dragonsbane"));
        var before = r.compute(c);
        // c.accept(
        //     new KQMSArtifactOptimizer(r,0)
        // );
        var bob = c.accept(Optimizers.KQMSArtifactOptimizer(r, 0));
        //System.out.println(bob);
        var after = r.compute(c);
        assert before < after;

    }

    @Test public void ArtifactOptimizerNotEnoughERCase(){
            //not enough energy recharge case
            var r = new Rotation()
                .add("t", DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1.00, StatTable.of()));
            var c = Characters.of("hutao");
                // .equip(Weapons.of("thecatch"));
            try{
                //c.accept(new KQMSArtifactOptimizer(r, 2.00));
                c.accept(Optimizers.KQMSArtifactOptimizer(r, 2.0));
                assert false;
            }
            catch (Throwable e){
                //assert e.getMessage().equals("Energy Recharge requirements cannot be met with substats alone");
                //System.out.println(c.get(Stat.EnergyRecharge) + Artifacts.getMainStatValue(5, 20, Stat.EnergyRecharge));
            }
    }

    @Test public void ArtifactOptimizerEnoughERCase(){
        //enough energy recharge case
        var r = new Rotation()
            .add("t", DamageInstance.of(Element.Electro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1.00, StatTable.of()));
        var c = Characters.of("raiden")
            .equip(Weapons.of("thecatch"))
            .add(StatTable.of(
                Stat.FlatATK, 900
            ))
        ;
        try{
            //var bob = c.accept(new KQMSArtifactOptimizer(r, 2.00));
            var bob = c.accept(Optimizers.KQMSArtifactOptimizer(r, 2.0));

            System.out.println(bob);
            System.out.println(c.get(Stat.EnergyRecharge));
            assert c.get(Stat.EnergyRecharge) >= 2.00;
        } catch (IllegalArgumentException e){ throw e; }
        //System.out.println(r.compute(c));
    }

    @Test public void AcceptArtifactOptimizerWithRotations(){
        var r = new Rotation()
            .add("a", DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1, StatTable.of()));
        var c = Characters.of("diluc")
            //.equip(Weapons.of("rainslasher"))
        ;
        
        // System.out.println(
        //     c.accept(new KQMSArtifactOptimizer(r,0))
        // );
        // System.out.println(Formulas.totalATK(c));
        // System.out.println(c.stats());
        
        // System.out.println(r.compute(c));
    }

    @Test public void GreedyVsFullSearch(){
        var r = new Rotation()
            .add("a", DamageInstance.of(Element.Pyro, DamageType.Normal, BaseScaling.ATK, Amplifier.None, 1, 1, StatTable.of()));
        var c = Characters.of("amber")
            .equip(Weapons.of("stringless"))
            .equip(new Flower(5,20))
            .equip(new Feather(5,20))
            .equip(new Sands(5,20, Stat.ATKPercent))
            .equip(new Goblet(5,20, Stat.PyroDMGBonus))
            .equip(new Circlet(5,20, Stat.CritRate))
        ;

        // var greedyBob = Optimizer.greedyOptimialArtifactSubStatDistrbution(c, r, 0);
        // var greedyDMG = r.compute(c, ()->greedyBob.substats());
        // System.out.println(greedyBob);
        // System.out.println(greedyDMG);

        // var fullBob = Optimizer.optimialArtifactSubStatDistrbution(c, r, 0);
        // var fullDMG = r.compute(c, ()->fullBob.substats());
        // System.out.println(fullBob);
        // System.out.println(fullDMG);
        
    }
}
