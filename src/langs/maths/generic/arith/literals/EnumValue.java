package langs.maths.generic.arith.literals;

import com.microsoft.z3.IntExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 00:48
 */
public final class EnumValue extends AValue {

    private static final LinkedHashMap<String, Integer> enumValuesRegister = new LinkedHashMap<>();
    private final String name;

    public EnumValue(String name) {
        super(enumValuesRegister.containsKey(name) ? enumValuesRegister.get(name) : enumValuesRegister.size());
        this.name = name;
        enumValuesRegister.putIfAbsent(name, enumValuesRegister.size());
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public IntExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public EnumValue accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public EnumValue cloned() {
        return new EnumValue(name);
    }

}
