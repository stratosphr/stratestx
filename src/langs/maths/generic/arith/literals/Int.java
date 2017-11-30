package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import langs.AObject;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:20
 */
public final class Int extends AValue {

    public Int(int value) {
        super(value);
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
    public Int accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    @Override
    public Int clone() {
        return new Int(getValue());
    }

    @Override
    public int compareTo(AObject aObject) {
        return getValue().compareTo(((Int) aObject).getValue());
    }

}
