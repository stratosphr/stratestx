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
import parsers.xml.XMLNode;
import parsers.xml.XMLParser;
import parsers.xml.schemas.XMLAttributesSchema;
import parsers.xml.schemas.XMLNodeSchema;
import utilities.ResourcesManager;
import utilities.Tuple;
import visitors.Primer;
import visitors.SMTEncoder;

import static utilities.ResourcesManager.EModel.EXAMPLE;
import static utilities.ResourcesManager.EXMLSchema.EBM;

public class Main {

    private final static ABoolExpr formula = new And(
            new False(),
            new True(),
            new Or(
                    new Equals(new Int(0), new Plus(new Int(42), new Var("a")), new Const("b"), new Fun("fun", new Var("c"))),
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
    private final static ABoolExpr primedFormula = formula.accept(new Primer(1));

    private static void coverAll() throws CloneNotSupportedException {
        Main main = new Main();
        Context context = new Context();
        Solver solver = context.mkSolver();
        DefsRegister defsRegister = new DefsRegister();
        defsRegister.getConstsDefs().put("b", new Int(42));
        defsRegister.getConstsDefs().put("c", new Int(64));
        defsRegister.getVarsDefs().put("a", new Z());
        defsRegister.getVarsDefs().put("b", new Z());
        defsRegister.getVarsDefs().put("c", new Union(new Range(new Int(0), new Int(3)), new Difference(new Set(new Int(42), new Int(64)))));
        defsRegister.getFunsDefs().put("fun", new Tuple<>(new Intersection(new Set(), new Range(new Int(42), new Int(64))), new Union(new Intersection(new Set(), new Range(new Int(42), new Int(64)), new Set(new Int(42))), new Set(new Int(0)), new Set(new Int(3), new Int(42)))));
        System.out.println(formula);
        long l = System.nanoTime();
        solver.add(formula.accept(new SMTEncoder(context, solver, defsRegister)));
        solver.check();
        System.out.println((System.nanoTime() - l) * 1.0E-9);
        System.out.println(solver.check());
        if (formula.hashCode() != formula.clone().hashCode()) {
            throw new Error(formula + "\n\n\n" + formula.clone());
        }
        if (!formula.toString().equals(formula.clone().toString())) {
            throw new Error();
        }
        if (!formula.equals(formula.clone())) {
            throw new Error();
        }
        System.out.println(primedFormula);
        l = System.nanoTime();
        solver.add(primedFormula.accept(new SMTEncoder(context, solver, defsRegister)));
        solver.check();
        System.out.println((System.nanoTime() - l) * 1.0E-9);
        System.out.println(solver.check());
        if (primedFormula.hashCode() != primedFormula.clone().hashCode()) {
            throw new Error(formula + "\n\n\n" + formula.clone());
        }
        if (!primedFormula.toString().equals(primedFormula.clone().toString())) {
            throw new Error();
        }
        if (!primedFormula.equals(primedFormula.clone())) {
            throw new Error();
        }
    }

    public static void main(String[] args) {
        XMLParser xmlParser = new XMLParser(false);
        XMLNode parse = xmlParser.parse(ResourcesManager.getModel(EXAMPLE), ResourcesManager.getXMLSchema(EBM));
        XMLNodeSchema child1 = new XMLNodeSchema("expected-child-name", new XMLAttributesSchema(
                new String[]{"required-child-attribute", "also-required-child-attribute"},
                new String[]{"not-required-child-attribute", "not-required-either-child-attribute"}
        ));
        XMLNodeSchema child2 = new XMLNodeSchema("other-expected-child-name", new XMLAttributesSchema(
                new String[]{"other-required-child-attribute", "other-also-required-child-attribute"},
                new String[]{"other-not-required-child-attribute", "other-not-required-either-child-attribute"}
        ));
        XMLNodeSchema child3 = new XMLNodeSchema("another-expected-child-name", new XMLAttributesSchema(
                new String[]{"another-required-child-attribute", "another-also-required-child-attribute"},
                new String[]{"another-not-required-child-attribute", "another-not-required-either-child-attribute"}
        ));
        parse.assertConformsTo(new XMLNodeSchema(
                "expected-name",
                new XMLAttributesSchema(
                        new String[]{"required", "also-required"},
                        new String[]{"not-required", "not-required-either"}
                )
        ));
    }
}
