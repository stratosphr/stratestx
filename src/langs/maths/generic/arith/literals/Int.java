package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:20
 */
public final class Int extends AArithExpr {

    private int value;

    public Int(int value) {
        this.value = value;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public IntExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public Int clone() {
        return new Int(value);
    }

}
