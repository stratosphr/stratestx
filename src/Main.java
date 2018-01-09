import algorithms.EAlgorithm;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.heuristics.relevance.atomics.Condition;
import algorithms.heuristics.relevance.atomics.Conditions;
import algorithms.heuristics.relevance.atomics.funs.FunChanges;
import algorithms.heuristics.relevance.atomics.funs.FunDecreases;
import algorithms.heuristics.relevance.atomics.funs.FunIncreases;
import algorithms.heuristics.relevance.atomics.vars.VarChanges;
import algorithms.heuristics.relevance.atomics.vars.VarDecreases;
import algorithms.heuristics.relevance.atomics.vars.VarIncreases;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.Minus;
import langs.maths.generic.arith.operators.Plus;
import langs.maths.generic.bool.operators.Equals;

import static algorithms.EAlgorithm.*;
import static algorithms.statistics.Saver.save;
import static utilities.ResourcesManager.EAbstractionPredicatesSet.*;
import static utilities.ResourcesManager.EModel.*;
import static utilities.ResourcesManager.ERelevancePredicate.RP0;
import static utilities.ResourcesManager.ERelevancePredicate.RP1;

class Main {

    public static void main(String[] args) {
        all(false);
    }

    @SuppressWarnings("SameParameterValue")
    private static void all(boolean computeFull) {
        EAlgorithm[] algorithms = computeFull ? new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO, FULL} : new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO};
        long start = System.nanoTime();
        save("default", CA, AP0, RP0, algorithms);
        System.out.println("1");
        save("default", CA, AP1, RP0, algorithms);
        System.out.println("2");
        save("default", CM, AP0, RP0, algorithms);
        System.out.println("3");
        save("default", CM, AP1, RP0, algorithms);
        System.out.println("4");
        save("default", CM, AP2, RP0, algorithms);
        System.out.println("5");
        save("default", EL, AP0, RP0, algorithms);
        System.out.println("6");
        save("default", EL, AP1, RP0, algorithms);
        System.out.println("7");
        save("default", EL, AP2, RP0, algorithms);
        System.out.println("8");
        save("default", EL, AP3, RP0, algorithms);
        System.out.println("9");
        save("default", PH, AP0, RP0, algorithms);
        System.out.println("10");
        save("default", PH, AP1, RP0, algorithms);
        System.out.println("11");
        save("default", PH, AP2, RP0, algorithms);
        System.out.println("12");
        save("default", PH, AP3, RP0, algorithms);
        System.out.println("13");
        save("1rel", L14_2, AP0, RP0, algorithms);
        System.out.println("14");
        save("1rel", L14_2, AP1, RP0, algorithms);
        System.out.println("15");
        save("1rel", L14_2, AP2, RP0, algorithms);
        System.out.println("16");
        save("1rel", L14_2, AP3, RP0, algorithms);
        System.out.println("17");
        save("3rel", L14_2, AP0, RP1, algorithms);
        System.out.println("18");
        save("3rel", L14_2, AP1, RP1, algorithms);
        System.out.println("19");
        save("3rel", L14_2, AP2, RP1, algorithms);
        System.out.println("20");
        save("3rel", L14_2, AP3, RP1, algorithms);
        System.out.println("21");
        save("default", EV, AP0, RP0, algorithms);
        System.out.println("18");
        save("default", EV, AP1, RP0, algorithms);
        System.out.println("19");
        save("default", EV, AP2, RP0, algorithms);
        System.out.println("20");
        System.out.println((System.nanoTime() - start) * 1.0E-9);
    }

    private static RelevancePredicate el() {
        return new RelevancePredicate(
                new FunChanges(new Fun("bat", new Int(1)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(2)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(3)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(4)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(5)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(6)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(7)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(8)), new EnumValue("ok"), new EnumValue("ko"))
        );
    }

    private static RelevancePredicate ev() {
        return new RelevancePredicate(
                new FunChanges(new Fun("PE", new Var("Pos")), new EnumValue("ouvertes"), new EnumValue("fermees")),
                new FunChanges(new Fun("PE", new Var("Pos")), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new VarChanges(new Var("PC"), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new VarChanges(new Var("PC"), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new VarChanges(new Var("Dir"), new Int(1), new Int(-1)),
                new Conditions(
                        new Condition(new Equals(new Var("Dir"), new Int(1)), new VarIncreases(new Var("Pos"))),
                        new Condition(new Equals(new Var("Dir"), new Int(-1)), new VarDecreases(new Var("Pos")))
                ),
                new FunChanges(new Fun("BM", new Plus(new Var("Pos"), new Int(1))), new Int(0), new Int(1)),
                new FunChanges(new Fun("BD", new Minus(new Var("Pos"), new Int(1))), new Int(0), new Int(1))
        );
    }

    private static RelevancePredicate cm() {
        return new RelevancePredicate(
                new VarIncreases(new Var("Balance")),
                new VarDecreases(new Var("CoffeeLeft")),
                new VarChanges(new Var("AskCoffee"), new Int(0), new Int(1)),
                new VarDecreases(new Var("Pot")),
                new VarChanges(new Var("Status"), new EnumValue("on"), new EnumValue("off")),
                new VarChanges(new Var("Status"), new EnumValue("error"), new EnumValue("off"))
        );
    }

    private static RelevancePredicate l14_1Rrel() {
        return new RelevancePredicate(
                new FunChanges(new Fun("Mvt", new Int(1)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(1)))),
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(1))))
                )
        );
    }

    private static RelevancePredicate l14_3Rrel() {
        return new RelevancePredicate(
                new FunChanges(new Fun("Mvt", new Int(1)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(1)))),
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(1))))
                ),
                new FunChanges(new Fun("Mvt", new Int(2)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(2)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(2)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(2)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(2)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(2)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(2)))),
                        new Condition(new Equals(new Fun("Dir", new Int(2)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(2))))
                ),
                new FunChanges(new Fun("Mvt", new Int(3)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(3)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(3)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(3)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(3)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(3)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(3)))),
                        new Condition(new Equals(new Fun("Dir", new Int(3)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(3))))
                )
        );
    }

    private static RelevancePredicate ca() {
        return new RelevancePredicate(
                new VarIncreases(new Var("De")),
                new VarDecreases(new Var("Us")),
                new VarIncreases(new Var("Do")),
                new VarIncreases(new Var("Be")),
                new VarIncreases(new Var("Lo")),
                new VarIncreases(new Var("AC"))
        );
    }

    private static RelevancePredicate ph() {
        return new RelevancePredicate(
                new VarIncreases(new Var("Etat"))
        );
    }

}
