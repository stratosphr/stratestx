package langs.maths.set;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.AValue;
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 15:26
 */
public abstract class AFiniteSetExpr extends ASetExpr {

    @Override
    public abstract AFiniteSetExpr accept(IPrimer primer);

    public final LinkedHashSet<AValue> getElementsValues(DefsRegister defsRegister) {
        return computeElementsValues(defsRegister);
    }

    protected abstract LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister);

    @Override
    public abstract AFiniteSetExpr cloned();

}
