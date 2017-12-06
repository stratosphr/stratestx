import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import langs.formal.graphs.FSM;
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
import parsers.stratest.StratestParser;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.ResourcesManager;
import utilities.Tuple;
import visitors.Primer;
import visitors.SMTEncoder;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static utilities.ResourcesManager.getModel;

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

    private static void coverAll() {
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
        StratestParser stratestParser = new StratestParser();
        Machine machine = stratestParser.parseModel(getModel(ResourcesManager.EModel.EL));
        Z3Result result;
        result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                machine.getInitialisation().getPrd(machine.getAssignables())
        ), machine.getDefsRegister());
        ConcreteState c = new ConcreteState("c", result.getModel(machine.getAssignables().stream().map(aAssignable -> aAssignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new))));
        result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                c,
                machine.getEvents().get("Tic").getSubstitution().getPrd(machine.getAssignables())
        ), machine.getDefsRegister());
        ConcreteState c_ = new ConcreteState("c_", result.getModel(machine.getAssignables().stream().map(aAssignable -> aAssignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new))));
        System.out.println(c);
        System.out.println(c_);
        System.out.println(new ConcreteTransition(c, machine.getEvents().get("Tic"), c_));
        result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                c,
                machine.getEvents().get("Tic").getSubstitution().getPrd(machine.getAssignables()),
                c_.accept(new Primer(1))
        ), machine.getDefsRegister());
        System.out.println(result.isSAT());
        FSM<ConcreteState, ConcreteTransition> fsm = new FSM<>(
                new LinkedHashSet<>(Arrays.asList(c)),
                new LinkedHashSet<>(Arrays.asList(c, c_)),
                new LinkedHashSet<>(Arrays.asList(
                        new ConcreteTransition(c, machine.getEvents().get("Tic"), c_)
                ))
        );
        System.out.println(fsm);
    }

}
