package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:06
 */
public final class Var extends AArithExpr {

    private String name;

    public Var(String name) {
        this.name = name;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public IntExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    @Override
    public Var clone() {
        return new Var(name);
    }

}
