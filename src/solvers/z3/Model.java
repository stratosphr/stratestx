package solvers.z3;

import com.microsoft.z3.Context;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import visitors.interfaces.IModelVisitor;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:117717
 */
public final class Model extends TreeMap<AAssignable, AValue> implements IModelVisitor {

    private final com.microsoft.z3.Model model;
    private final Context context;
    private final DefsRegister defsRegister;
    private final Set<AAssignable> assignables;

    Model(com.microsoft.z3.Model model, Context context, DefsRegister defsRegister, Set<AAssignable> assignables) {
        this.model = model;
        this.context = context;
        this.defsRegister = defsRegister;
        this.assignables = assignables;
        assignables.forEach(assignable -> assignable.accept(this));
    }

    @Override
    public void visit(Var var) {
        if (Arrays.stream(model.getConstDecls()).anyMatch(funcDecl -> funcDecl.getName().toString().equals(var.getName()))) {
            put(var, new Int(Integer.parseInt(model.eval(context.mkIntConst(var.getName()), true).toString())));
        } else {
            throw new Error("Error: Variable \"" + var + "\" is never used in checked expression.");
        }
    }

    @Override
    public void visit(Fun fun) {
        if (Arrays.stream(model.getFuncDecls()).anyMatch(funcDecl -> funcDecl.getName().toString().equals(fun.getName()))) {
            put(fun, new Int(Integer.parseInt(model.eval(context.mkFuncDecl(fun.getName(), context.getIntSort(), context.getIntSort()).apply(context.mkInt(((Int) fun.getParameter()).getValue())), true).toString())));
        } else {
            throw new Error("Error: Function \"" + fun + "\" is never used in checked expression.");
        }
    }

}