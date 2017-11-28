import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.Const;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Range;
import langs.maths.set.literals.Set;
import langs.maths.set.literals.Z;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import utilities.Tuple;
import visitors.SMTEncoder;

public class Main {

    private static final ABoolExpr formula1 = new And(
            new False(),
            new True(),
            new Or(
                    new Equals(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c"))),
                    new Implies(new GEQ(new Int(0), new Int(42)), new GT(new Int(42), new Int(0))),
                    new Equiv(new LEQ(new Int(0), new Int(42)), new LT(new Int(42), new Int(0)))
            ),
            new Not(new InDomain(
                    new Plus(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c"))),
                    new Z()
            )),
            new NotEquals(
                    new Times(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Fun("fun", new Var("c")))),
                    new Div(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Fun("fun", new Var("c"))))
            ),
            new VarInDomain(
                    new Var("a"),
                    new Intersection(
                            new Union(
                                    new Range(new Int(0), new Mod(new Int(42), new Int(0))),
                                    new Difference(
                                            new Set(new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Var("c"))),
                                            new Set(new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Fun("fun", new Var("c"))))
                                    )
                            ),
                            new Set(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c")))
                    )
            ),
            new ForAll(
                    new And(
                            new True(),
                            new Or(
                                    new False(),
                                    new Not(new Exists(
                                            new Equals(
                                                    new UMinus(new Minus(
                                                            new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Var("c"))
                                                    )),
                                                    new Mod(new Int(42), new Const("c"))
                                            ),
                                            new VarInDomain(new Var("a"), new Set(new Int(0), new Int(42))),
                                            new VarInDomain(new Var("b"), new Set(new Int(42), new Int(0)))
                                    ))
                            )
                    ),
                    new VarInDomain(new Var("a"), new Range(new Int(0), new Int(42))),
                    new VarInDomain(new Var("b"), new Union(new Set(new Int(42), new Int(0)), new Set(new Int(1)))),
                    new VarInDomain(new Var("c"), new Set(new Var("a"), new Var("b")))
            )
    );

    private static void coverAll() throws CloneNotSupportedException {
        Context context = new Context();
        Solver solver = context.mkSolver();
        DefsRegister defsRegister = new DefsRegister();
        defsRegister.getConstsDefs().put("b", new Int(42));
        defsRegister.getConstsDefs().put("c", new Int(64));
        defsRegister.getVarsDefs().put("a", new Z());
        defsRegister.getVarsDefs().put("b", new Z());
        defsRegister.getVarsDefs().put("c", new Z());
        defsRegister.getFunsDefs().put("fun", new Tuple<>(new Set(), new Set()));
        System.out.println(formula1);
        long l = System.nanoTime();
        solver.add(formula1.accept(new SMTEncoder(context, solver, defsRegister)));
        solver.check();
        System.out.println((System.nanoTime() - l) * 1.0E-9);
        System.out.println(solver.check());
        if (formula1.hashCode() != formula1.clone().hashCode()) {
            throw new Error(formula1 + "\n\n\n" + formula1.clone());
        }
        if (!formula1.toString().equals(formula1.clone().toString())) {
            throw new Error();
        }
        if (!formula1.equals(formula1.clone())) {
            throw new Error();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        coverAll();
        DefsRegister defsRegister = new DefsRegister();
        defsRegister.getConstsDefs().put("n", new Int(10));
    }

}
