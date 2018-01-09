package algorithms.heuristics.relevance.atomics.vars;

import algorithms.heuristics.relevance.AVariantComputer;
import algorithms.heuristics.relevance.atomics.ADecreases;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Var;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:33
 */

public final class VarDecreases extends ADecreases<Var> {

    public VarDecreases(Var assignable) {
        super(assignable);
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
    public VarDecreases cloned() {
        return new VarDecreases(assignable.cloned());
    }

}
