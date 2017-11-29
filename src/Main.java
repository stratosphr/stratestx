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
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.Tuple;
import visitors.SMTEncoder;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class Main {

    private static final DefsRegister defsRegister = new DefsRegister();

    private final static ABoolExpr formula1 = new And(
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
                            defsRegister,
                            new Union(
                                    defsRegister,
                                    new Range(defsRegister, new Int(0), new Mod(new Int(42), new Int(0))),
                                    new Difference(
                                            defsRegister,
                                            new Set(defsRegister, new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Var("c"))),
                                            new Set(defsRegister, new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Fun("fun", new Var("c"))))
                                    )
                            ),
                            new Set(defsRegister, new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c")))
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
                                            new VarInDomain(new Var("a"), new Set(defsRegister, new Int(0), new Int(42))),
                                            new VarInDomain(new Var("b"), new Set(defsRegister, new Int(42), new Int(0)))
                                    ))
                            )
                    ),
                    new VarInDomain(new Var("a"), new Range(defsRegister, new Int(0), new Int(42))),
                    new VarInDomain(new Var("b"), new Union(defsRegister, new Set(defsRegister, new Int(42), new Int(0)), new Set(defsRegister, new Int(1)))),
                    new VarInDomain(new Var("c"), new Set(defsRegister, new Var("a"), new Var("b")))
            )
    );

    private static void coverAll() throws CloneNotSupportedException {
        Main main = new Main();
        Context context = new Context();
        Solver solver = context.mkSolver();
        DefsRegister defsRegister = new DefsRegister();
        defsRegister.getConstsDefs().put("b", new Int(42));
        defsRegister.getConstsDefs().put("c", new Int(64));
        defsRegister.getVarsDefs().put("a", new Z());
        defsRegister.getVarsDefs().put("b", new Z());
        defsRegister.getVarsDefs().put("c", new Z());
        defsRegister.getFunsDefs().put("fun", new Tuple<>(new Set(defsRegister), new Set(defsRegister)));
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
        DefsRegister defsRegister = new DefsRegister();
        defsRegister.getConstsDefs().put("n", new Int(10));
        defsRegister.getVarsDefs().put("sw", new Range(defsRegister, new Int(1), new Int(3)));
        defsRegister.getVarsDefs().put("sw_", new Range(defsRegister, new Int(1), new Int(3)));
        defsRegister.getFunsDefs().put("bat", new Tuple<>(new Range(defsRegister, new Int(1), new Int(3)), new Set(defsRegister, new Int(0), new Int(1))));
        defsRegister.getFunsDefs().put("bat_", new Tuple<>(new Range(defsRegister, new Int(1), new Int(10)), new Range(defsRegister, new Int(0), new Int(4))));
        ABoolExpr expr = new And(
                new Equals(new Fun("bat", new Var("sw")), new Int(1)),
                new Equals(new Fun("bat_", new Var("sw_")), new Int(1)),
                new Equals(new Fun("bat", new Int(1)), new Int(0)),
                new Equals(new Fun("bat", new Int(2)), new Int(1)),
                new Equals(new Fun("bat", new Int(3)), new Int(0)),
                new Equals(new Fun("bat_", new Int(1)), new Int(1)),
                new Equals(new Fun("bat_", new Int(2)), new Int(1)),
                new Equals(new Fun("bat_", new Int(3)), new Int(1))
        );
        Z3Result result = Z3.checkSAT(expr, defsRegister);
        System.out.println(result.getModel(new LinkedHashSet<>(Arrays.asList(
                new Var("sw"),
                new Var("sw_"),
                new Fun("bat", new Int(1)),
                new Fun("bat", new Int(2)),
                new Fun("bat", new Int(3)),
                new Fun("bat", new Int(4)),
                new Fun("bat", new Int(12)),
                new Fun("bat_", new Int(1)),
                new Fun("bat_", new Int(2)),
                new Fun("bat_", new Int(3)),
                new Fun("bat_", new Int(4)),
                new Fun("bat_", new Int(5)),
                new Fun("bat_", new Int(6)),
                new Fun("bat_", new Int(7)),
                new Fun("bat_", new Int(8))
        ))));
    }

}
