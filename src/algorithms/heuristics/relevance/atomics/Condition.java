package algorithms.heuristics.relevance.atomics;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.Implies;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:38
 */
@SuppressWarnings("WeakerAccess")
public final class Condition extends ABoolExpr {

    private ABoolExpr condition;
    private AAtomicRelevancePredicate thenPart;
    private final Implies expr;

    public Condition(ABoolExpr condition, AAtomicRelevancePredicate thenPart) {
        this.condition = condition;
        this.thenPart = thenPart;
        this.expr = new Implies(condition, thenPart);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(expr);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return encoder.visit(expr);
    }

    @Override
    public ABoolExpr accept(IPrimer primer) {
        return primer.visit(expr);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return expr.getFuns();
    }

    public ABoolExpr getCondition() {
        return condition;
    }

    public AAtomicRelevancePredicate getThenPart() {
        return thenPart;
    }

    @Override
    public Condition clone() {
        return new Condition(condition.clone(), thenPart.clone());
    }

}

