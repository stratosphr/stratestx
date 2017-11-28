package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import visitors.interfaces.IModelVisitor;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.ISMTEncoder;

import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:06
 */
public final class Fun extends AAssignable {

    private final AArithExpr parameter;

    public Fun(String name, AArithExpr parameter) {
        super(name);
        this.parameter = parameter;
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
    public void accept(IModelVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>(Collections.singletonList(this));
    }

    public AArithExpr getParameter() {
        return parameter;
    }

    @Override
    public Fun clone() {
        return new Fun(getName(), parameter.clone());
    }

}
