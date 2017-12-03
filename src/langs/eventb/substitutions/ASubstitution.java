package langs.eventb.substitutions;

import langs.AObject;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:44
 */
public abstract class ASubstitution extends AObject {

    public abstract ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables);

    @Override
    public abstract ASubstitution clone();

}
