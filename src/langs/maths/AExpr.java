package langs.maths;

import langs.AObject;
import langs.maths.generic.arith.literals.Fun;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:21
 */
public abstract class AExpr extends AObject {

    public abstract LinkedHashSet<Fun> getFuns();

    @Override
    public abstract AExpr clone();

}
