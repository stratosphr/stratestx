package langs.maths.generic.bool.operators;

import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:57
 */
public abstract class AQuantifier extends ABoolExpr {

    private final ABoolExpr body;
    private final List<VarInDomain> quantifiedVarsDefs;

    AQuantifier(ABoolExpr body, VarInDomain[] quantifiedVarsDefs) {
        this.body = body;
        this.quantifiedVarsDefs = Arrays.asList(quantifiedVarsDefs);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return body.getFuns();
    }

    public ABoolExpr getBody() {
        return body;
    }

    public List<VarInDomain> getQuantifiedVarsDefs() {
        return quantifiedVarsDefs;
    }

}
