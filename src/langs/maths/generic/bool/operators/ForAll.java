package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.bool.ABinaryBoolExpr;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:57
 */
public final class ForAll extends AQuantifier {

    public ForAll(ABoolExpr body, VarInDomain... quantifiedVarsDefs) {
        super(new Implies(new And(quantifiedVarsDefs), body), quantifiedVarsDefs);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public ForAll accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public Implies getBody() {
        return (Implies) super.getBody();
    }

    @Override
    public ForAll cloned() {
        return new ForAll(getBody().getRight().cloned(), getQuantifiedVarsDefs().stream().map(ABinaryBoolExpr::cloned).toArray(VarInDomain[]::new));
    }

}
