package langs.maths.def;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ASetExpr;
import utilities.Tuple;

import java.util.LinkedHashMap;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 12:19
 */
public final class DefsRegister {

    private final LinkedHashMap<String, AArithExpr> constsDefs;
    private final LinkedHashMap<String, ASetExpr> varsDefs;
    private final LinkedHashMap<String, Tuple<AFiniteSetExpr, ASetExpr>> funsDefs;

    public DefsRegister(DefsRegister defsRegister) {
        this.constsDefs = new LinkedHashMap<>(defsRegister.getConstsDefs());
        this.varsDefs = new LinkedHashMap<>(defsRegister.getVarsDefs());
        this.funsDefs = new LinkedHashMap<>(defsRegister.getFunsDefs());
    }

    public DefsRegister() {
        this.constsDefs = new LinkedHashMap<>();
        this.varsDefs = new LinkedHashMap<>();
        this.funsDefs = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, AArithExpr> getConstsDefs() {
        return constsDefs;
    }

    public LinkedHashMap<String, ASetExpr> getVarsDefs() {
        return varsDefs;
    }

    public LinkedHashMap<String, Tuple<AFiniteSetExpr, ASetExpr>> getFunsDefs() {
        return funsDefs;
    }

}
