package langs.maths.generic.bool.operators;

import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:57
 */
public final class ForAll extends AQuantifier {

    public ForAll(ABoolExpr body, VarInDomain... quantifiedVarsDefs) {
        super(new Implies(new And(quantifiedVarsDefs), body), quantifiedVarsDefs);
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

}
