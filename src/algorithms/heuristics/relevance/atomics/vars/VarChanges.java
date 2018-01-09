package algorithms.heuristics.relevance.atomics.vars;

import algorithms.heuristics.relevance.AVariantComputer;
import algorithms.heuristics.relevance.atomics.AChanges;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Var;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 14:23
 */

public final class VarChanges extends AChanges<Var> {

    public VarChanges(Var var, AArithExpr value, AArithExpr value_) {
        super(var, value, value_);
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
    public VarChanges cloned() {
        return new VarChanges(assignable.cloned(), value.cloned(), value_.cloned());
    }

}
