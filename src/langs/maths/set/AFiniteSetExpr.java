package langs.maths.set;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.AValue;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 15:26
 */
public abstract class AFiniteSetExpr extends ASetExpr {

    private final DefsRegister defsRegister;
    private LinkedHashSet<AValue> elementsValues;

    protected AFiniteSetExpr(DefsRegister defsRegister) {
        this.defsRegister = new DefsRegister(defsRegister);
        this.elementsValues = null;
    }

    public final LinkedHashSet<AValue> getElementsValues() {
        if (elementsValues == null) {
            elementsValues = computeElementsValues();
        }
        return elementsValues;
    }

    protected abstract LinkedHashSet<AValue> computeElementsValues();

    public DefsRegister getDefsRegister() {
        return defsRegister;
    }

    @Override
    public abstract AFiniteSetExpr clone();

}
