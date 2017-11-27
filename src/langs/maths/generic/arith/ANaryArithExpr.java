package langs.maths.generic.arith;

import langs.maths.generic.AGenericTypeExpr;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public abstract class ANaryArithExpr<Operand extends AGenericTypeExpr> extends AArithExpr {

    private final List<Operand> operands;

    public ANaryArithExpr(Operand[] operands) {
        this.operands = Arrays.asList(operands);
    }

    public List<Operand> getOperands() {
        return operands;
    }

}
