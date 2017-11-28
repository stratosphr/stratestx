package langs.maths.set;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.AValue;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 15:26
 */
public abstract class AFiniteSetExpr extends ASetExpr {

    private LinkedHashSet<AValue> elementsValues;

    protected AFiniteSetExpr() {
        this.elementsValues = null;
    }

    public final LinkedHashSet<AValue> getElementsValues(DefsRegister defsRegister) {
        if (elementsValues == null) {
            elementsValues = computeElementsValues(defsRegister);
        }
        return elementsValues;
    }

    protected abstract LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister);

    @Override
    public abstract AFiniteSetExpr clone();

}
