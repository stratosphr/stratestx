package langs.maths.generic.arith;

import visitors.interfaces.IModelVisitable;
import visitors.interfaces.IPrimer;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:30
 */
public abstract class AAssignable extends AArithExpr implements IModelVisitable {

    private String name;

    public AAssignable(String name) {
        this.name = name;
    }

    @Override
    public abstract AAssignable accept(IPrimer primer);

    @Override
    public abstract AAssignable clone();

    public String getName() {
        return name;
    }

}
