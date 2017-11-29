package langs.maths.set.literals;

import langs.maths.AExpr;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.AFiniteSetExpr;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.interfaces.IObjectFormatter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:19
 */
public final class Set extends AFiniteSetExpr {

    private final LinkedHashSet<AArithExpr> elements;

    public Set(AArithExpr... elements) {
        this.elements = new LinkedHashSet<>(Arrays.asList(elements));
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return elements.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister) {
        DefsRegister tmpDefsRegister = new DefsRegister(defsRegister);
        ABoolExpr[] elementsBindings = new ABoolExpr[elements.size()];
        LinkedHashSet<AAssignable> elementsVars = new LinkedHashSet<>();
        List<AArithExpr> elementsList = new ArrayList<>(elements);
        for (int i = 0; i < elements.size(); i++) {
            Var var = new Var("e" + i + "!");
            tmpDefsRegister.getVarsDefs().put(var.getName(), new Z());
            elementsVars.add(var);
            elementsBindings[i] = new Equals(var, elementsList.get(i));
        }
        Z3Result result = Z3.checkSAT(new And(elementsBindings), tmpDefsRegister);
        if (result.isSAT()) {
            return new LinkedHashSet<>(result.getModel(elementsVars).values());
        } else {
            throw new Error("Error: Unable to compute values of elements in set \"" + this + "\".");
        }
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new Or(elements.stream().map(element -> new Equals(expr, element)).toArray(ABoolExpr[]::new));
    }

    public LinkedHashSet<AArithExpr> getElements() {
        return elements;
    }

    @Override
    public Set clone() {
        return new Set(elements.toArray(new AArithExpr[0]));
    }

}
