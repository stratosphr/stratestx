package algorithms.heuristics.relevance.atomics.funs;

import algorithms.heuristics.relevance.AVariantComputer;
import algorithms.heuristics.relevance.atomics.AChanges;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Fun;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 14:17
 */

public final class FunChanges extends AChanges<Fun> {

    public FunChanges(Fun fun, AArithExpr value, AArithExpr value_) {
        super(fun, value, value_);
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
    public FunChanges cloned() {
        return new FunChanges(assignable.cloned(), value.cloned(), value_.cloned());
    }

}
