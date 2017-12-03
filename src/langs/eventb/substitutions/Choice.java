package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 01:12
 */
public final class Choice extends ASubstitution {

    private final List<ASubstitution> substitutions;

    public Choice(ASubstitution[] substitutions) {
        this.substitutions = Arrays.asList(substitutions);
    }

    public List<ASubstitution> getSubstitutions() {
        return substitutions;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return null;
    }

    @Override
    public ASubstitution clone() {
        return new Choice(substitutions.stream().map(ASubstitution::clone).toArray(ASubstitution[]::new));
    }

}
