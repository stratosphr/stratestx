package algorithms.heuristics.relevance.atomics.funs;

import algorithms.heuristics.relevance.AVariantComputer;
import algorithms.heuristics.relevance.atomics.AIncreases;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Fun;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:33
 */

public final class FunIncreases extends AIncreases<Fun> {

    public FunIncreases(Fun fun) {
        super(fun);
    }

    @Override
    public AArithExpr getVInit(AVariantComputer computer, ConcreteState c) {
        return computer.getVInit(this, c);
    }

    @Override
    public AArithExpr getV(AVariantComputer computer, ConcreteState c, ConcreteState c_) {
        return computer.getV(this, c, c_);
    }

    @Override
    public FunIncreases cloned() {
        return new FunIncreases(assignable.cloned());
    }

}
