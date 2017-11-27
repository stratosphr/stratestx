import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
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
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import visitors.SMTEncoder;

public class Main {

    private static final ABoolExpr formula = new And(
            new False(),
            new True(),
            new Or(
                    new Equals(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c"))),
                    new Implies(new GEQ(new Int(0), new Int(42)), new GT(new Int(42), new Int(0))),
                    new Equiv(new LEQ(new Int(0), new Int(42)), new LT(new Int(42), new Int(0)))
            ),
            new Not(new InDomain(
                    new Plus(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c"))),
                    new Set(new Int(0), new Int(42), new Var("a"), new Const("b"), new Fun("fun", new Var("c")))
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
                            new Set(new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Fun("fun", new Var("c"))))
                    )
            ),
            new ForAll(
                    new Exists(
                            new Equals(
                                    new UMinus(new Minus(
                                            new Int(0), new Int(42), new Var("a"), new Var("b"), new Fun("fun", new Const("c"))
                                    )),
                                    new Const("c")
                            ),
                            new VarInDomain(new Var("a"), new Set(new Int(0), new Int(42))),
                            new VarInDomain(new Var("b"), new Set(new Int(42), new Int(0)))
                    ),
                    new VarInDomain(new Var("a"), new Set(new Int(0), new Int(42))),
                    new VarInDomain(new Var("b"), new Set(new Int(42), new Int(0)))
            )
    );

    public static void main(String[] args) {
        Context context = new Context();
        Solver solver = context.mkSolver();
        System.out.println(formula);
        System.out.println(formula.accept(new SMTEncoder(context, solver)));
    }

}
