package langs.maths.generic.bool;

import langs.maths.generic.AGenericTypeExpr;
import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:58
 */
public abstract class AUnaryBoolExpr<Operand extends AGenericTypeExpr> extends ABoolExpr {

    private Operand operand;

    public AUnaryBoolExpr(Operand operand) {
        this.operand = operand;
    }

    @Override
    public abstract AUnaryBoolExpr<Operand> accept(IPrimer primer);

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return operand.getFuns();
    }

    public Operand getOperand() {
        return operand;
    }

    @Override
    public abstract AUnaryBoolExpr<Operand> clone();

}
