package langs.maths.generic.bool;

import langs.maths.generic.AGenericTypeExpr;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public abstract class ANaryBoolExpr<Operand extends AGenericTypeExpr> extends ABoolExpr {

    private final List<Operand> operands;

    public ANaryBoolExpr(Operand[] operands) {
        this.operands = Arrays.asList(operands);
    }

    public List<Operand> getOperands() {
        return operands;
    }

}
