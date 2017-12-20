import algorithms.heuristics.relevance.DefaultVariantComputer;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.heuristics.relevance.atomics.funs.FunChanges;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;

import static algorithms.EAlgorithm.*;
import static algorithms.statistics.Saver.save;
import static utilities.ResourcesManager.EAbstractionPredicatesSet.AP0;
import static utilities.ResourcesManager.EAbstractionPredicatesSet.AP1;
import static utilities.ResourcesManager.EModel.EXAMPLE;

class Main {

    public static void main(String[] args) {
        save("test1", EXAMPLE, AP0, el(), CXP, CXPASO, RCXP, RCXPASO, FULL);
        save("test2", EXAMPLE, AP1, el(), CXP, CXPASO, RCXP, RCXPASO, FULL);
    }

    private static RelevancePredicate el() {
        return new RelevancePredicate(
                new DefaultVariantComputer(),
                new FunChanges(new Fun("bat", new Int(1)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(2)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(3)), new EnumValue("ok"), new EnumValue("ko"))
        );
    }

}
