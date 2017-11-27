package langs.maths.generic.bool.operators;

import langs.maths.generic.arith.literals.Var;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 01:09
 */
public final class VarInDomain extends AInDomain<Var> {

    public VarInDomain(Var var, ASetExpr set) {
        super(var, set);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
