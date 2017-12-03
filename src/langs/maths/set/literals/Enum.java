package langs.maths.set.literals;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.AFiniteSetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 00:46
 */
public final class Enum extends AFiniteSetExpr {

    private final LinkedHashSet<EnumValue> enumValues;

    public Enum(EnumValue... enumValues) {
        this.enumValues = new LinkedHashSet<>(Arrays.asList(enumValues));
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public Enum accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new Or(enumValues.stream().map(enumValue -> new Equals(expr, enumValue)).toArray(ABoolExpr[]::new));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister) {
        return new LinkedHashSet<>(enumValues);
    }

    public LinkedHashSet<EnumValue> getEnumValues() {
        return enumValues;
    }

    @Override
    public Enum clone() {
        return new Enum(enumValues.stream().map(EnumValue::clone).toArray(EnumValue[]::new));
    }

}
