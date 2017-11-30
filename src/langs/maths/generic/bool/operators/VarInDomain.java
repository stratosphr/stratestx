package langs.maths.generic.bool.operators;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.literals.Var;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 01:09
 */
public final class VarInDomain extends AInDomain<Var> {

    public VarInDomain(Var var, ASetExpr set) {
        super(var, set);
    }

    @Override
    public VarInDomain accept(IPrimer primer) {
        return primer.visit(this);
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
    public VarInDomain clone() {
        return new VarInDomain(getLeft().clone(), getRight().clone());
    }

}
