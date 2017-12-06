package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 28/11/17.
 * Time : 00:22
 */
public final class FunVar extends AArithExpr {

    private final String name;
    private final Fun fun;

    public FunVar(Fun fun) {
        this.name = fun.getName() + "!" + fun.getParameter();
        this.fun = fun;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    @Override
    public FunVar accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public IntExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    public String getName() {
        return name;
    }

    public Fun getFun() {
        return fun;
    }

    @Override
    public FunVar clone() {
        return new FunVar(fun.clone());
    }

}
