package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Exists;
import langs.maths.generic.bool.operators.VarInDomain;
import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 01:06
 */
public final class Any extends ASubstitution {

    private final ABoolExpr condition;
    private final ASubstitution substitution;
    private final List<VarInDomain> quantifiedVarsDefs;

    public Any(ABoolExpr condition, ASubstitution substitution, VarInDomain[] quantifiedVarsDefs) {
        this.condition = condition;
        this.substitution = substitution;
        this.quantifiedVarsDefs = Arrays.asList(quantifiedVarsDefs);
    }

    public ABoolExpr getCondition() {
        return condition;
    }

    public ASubstitution getSubstitution() {
        return substitution;
    }

    public List<VarInDomain> getQuantifiedVarsDefs() {
        return quantifiedVarsDefs;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new Exists(
                new And(condition, substitution.getPrd(assignables)),
                quantifiedVarsDefs.toArray(new VarInDomain[0])
        );
    }

    @Override
    public ASubstitution clone() {
        return new Any(condition.clone(), substitution.clone(), quantifiedVarsDefs.stream().map(VarInDomain::clone).toArray(VarInDomain[]::new));
    }

}
