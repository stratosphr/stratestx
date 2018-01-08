package algorithms.heuristics.relevance.atomics.vars;

import algorithms.heuristics.relevance.AVariantComputer;
import algorithms.heuristics.relevance.atomics.AIncreases;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Var;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:33
 */
@SuppressWarnings("WeakerAccess")
public final class VarIncreases extends AIncreases<Var> {

    public VarIncreases(Var var) {
        super(var);
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
    public VarIncreases clone() {
        return new VarIncreases(assignable.clone());
    }

}
