package langs.maths.set;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:52
 */
public abstract class ANarySetExpr extends ASetExpr {

    private final List<ASetExpr> operands;

    public ANarySetExpr(ASetExpr[] operands) {
        this.operands = Arrays.asList(operands);
    }

    public List<ASetExpr> getOperands() {
        return operands;
    }

}
