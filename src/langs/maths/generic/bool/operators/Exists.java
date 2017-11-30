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
public final class Exists extends AQuantifier {

    public Exists(ABoolExpr body, VarInDomain... quantifiedVarsDefs) {
        super(new And(new And(quantifiedVarsDefs), body), quantifiedVarsDefs);
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
    public Exists accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public And getBody() {
        return (And) super.getBody();
    }

    @Override
    public Exists clone() {
        return new Exists(getBody().getOperands().get(1).clone(), getQuantifiedVarsDefs().stream().map(ABinaryBoolExpr::clone).toArray(VarInDomain[]::new));
    }

}
