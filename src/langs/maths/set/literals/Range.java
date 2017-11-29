package langs.maths.set.literals;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.GEQ;
import langs.maths.generic.bool.operators.LEQ;
import langs.maths.set.AFiniteSetExpr;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.Maths;
import visitors.interfaces.IObjectFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:18
 */
public final class Range extends AFiniteSetExpr {

    private final AArithExpr lowerBound;
    private final AArithExpr upperBound;

    public Range(DefsRegister defsRegister, AArithExpr lowerBound, AArithExpr upperBound) {
        super(defsRegister);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return Stream.of(lowerBound.getFuns(), upperBound.getFuns()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public AArithExpr getLowerBound() {
        return lowerBound;
    }

    public AArithExpr getUpperBound() {
        return upperBound;
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(new GEQ(expr, lowerBound), new LEQ(expr, upperBound));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues() {
        DefsRegister tmpDefsRegister = new DefsRegister(getDefsRegister());
        ArrayList<ABoolExpr> boundsBindings = new ArrayList<>();
        Var lowerBoundVar = new Var("lowerBound!");
        Var upperBoundVar = new Var("upperBound!");
        tmpDefsRegister.getVarsDefs().put(lowerBoundVar.getName(), new Z());
        tmpDefsRegister.getVarsDefs().put(upperBoundVar.getName(), new Z());
        LinkedHashSet<AAssignable> boundsVars = new LinkedHashSet<>(Arrays.asList(lowerBoundVar, upperBoundVar));
        boundsBindings.add(new Equals(lowerBoundVar, lowerBound));
        boundsBindings.add(new Equals(upperBoundVar, upperBound));
        Z3Result result = Z3.checkSAT(new And(boundsBindings.toArray(new ABoolExpr[0])), tmpDefsRegister);
        if (result.isSAT()) {
            AValue lowerBoundValue = result.getModel(boundsVars).get(lowerBoundVar);
            AValue upperBoundValue = result.getModel(boundsVars).get(upperBoundVar);
            return Maths.range(lowerBoundValue.getValue(), upperBoundValue.getValue()).stream().map(Int::new).collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            throw new Error("Error: Unable to compute values of elements in set \"" + this + "\".");
        }
    }

    @Override
    public Range clone() {
        return new Range(new DefsRegister(getDefsRegister()), lowerBound, upperBound);
    }

}
