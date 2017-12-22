package algorithms;

import algorithms.heuristics.EColor;
import algorithms.outputs.ATS;
import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.literals.Z;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.Streams;
import visitors.Primer;

import java.util.*;
import java.util.stream.Collectors;

import static algorithms.heuristics.EColor.BLUE;
import static algorithms.heuristics.EColor.GREEN;

/**
 * Created by gvoiron on 09/12/17.
 * Time : 22:27
 */
public final class CXPASOComputer extends AComputer<ATS> {

    private final Machine machine;
    private final LinkedHashSet<AbstractState> A;
    private final LinkedHashSet<AbstractState> Q0;
    private final LinkedHashSet<AbstractState> Q;
    private final LinkedHashSet<AbstractTransition> Delta;
    private final LinkedHashSet<ConcreteState> C0;
    private final LinkedHashSet<ConcreteState> C;
    private final LinkedHashMap<Map<AAssignable, AValue>, ConcreteState> CMappings;
    private final LinkedHashSet<ConcreteTransition> DeltaC;
    private final LinkedHashMap<ConcreteState, AbstractState> alpha;
    private final LinkedHashMap<AbstractState, LinkedHashSet<ConcreteState>> _alpha;
    private final LinkedHashMap<ConcreteState, EColor> kappa;
    private final LinkedHashSet<AAssignable> primedAssignables;
    private final Var q_Index;
    private final DefsRegister defsRegisterWithIndex;

    public CXPASOComputer(Machine machine, LinkedHashSet<AbstractState> A) {
        this.machine = machine;
        this.A = A;
        this.Q0 = new LinkedHashSet<>();
        this.Q = new LinkedHashSet<>();
        this.Delta = new LinkedHashSet<>();
        this.C0 = new LinkedHashSet<>();
        this.C = new LinkedHashSet<>();
        this.CMappings = new LinkedHashMap<>();
        this.DeltaC = new LinkedHashSet<>();
        this.alpha = new LinkedHashMap<>();
        this._alpha = new LinkedHashMap<>();
        this.kappa = new LinkedHashMap<>();
        this.primedAssignables = machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new));
        this.q_Index = new Var("i!");
        this.defsRegisterWithIndex = new DefsRegister(machine.getDefsRegister());
        defsRegisterWithIndex.getVarsDefs().put(q_Index.getName(), new Z());
    }

    @Override
    ATS run() {
        Z3Result result;
        for (AbstractState q : A) {
            result = Z3.checkSAT(new And(
                    machine.getInvariant(),
                    machine.getInvariant().accept(new Primer(1)),
                    machine.getInitialisation().getPrd(machine.getAssignables()),
                    q.accept(new Primer(1))
            ), machine.getDefsRegister());
            if (result.isSAT()) {
                Model model = result.getModel(primedAssignables);
                ConcreteState c = new ConcreteState("c" + C0.size() + q.getName(), model);
                Q0.add(q);
                C0.add(c);
                alpha.put(c, q);
                _alpha.putIfAbsent(q, new LinkedHashSet<>());
                _alpha.get(q).add(c);
                kappa.put(c, GREEN);
            }
        }
        C.addAll(C0);
        C.forEach(c -> CMappings.put(c.getMapping(), c));
        LinkedHashSet<AbstractState> RQ = new LinkedHashSet<>(Q0);
        while (!RQ.isEmpty()) {
            AbstractState q = RQ.iterator().next();
            RQ.remove(q);
            Q.add(q);
            for (Event e : machine.getEvents().values()) {
                LinkedHashSet<AbstractState> RQ_ = A.stream().filter(q_ -> !Delta.contains(new AbstractTransition(q, e, q_))).collect(Collectors.toCollection(LinkedHashSet::new));
                do {
                    LinkedHashSet<ConcreteState> RCS = _alpha.get(q);
                    result = Z3.checkSAT(new And(
                            machine.getInvariant(),
                            machine.getInvariant().accept(new Primer(1)),
                            new Or(RCS.toArray(new ABoolExpr[0])),
                            e.getSubstitution().getPrd(machine.getAssignables()),
                            new Or(Streams.mapWithIndex(RQ_.stream(), ((index, q_) -> new And(new Equals(q_Index, new Int(index)), q_.accept(new Primer(1))))).toArray(ABoolExpr[]::new))
                    ), defsRegisterWithIndex);
                    if (result.isSAT()) {
                        Model q_Model = result.getModel(new LinkedHashSet<>(Collections.singletonList(q_Index)));
                        Model cModel = result.getModel(new LinkedHashSet<>(machine.getAssignables()));
                        Model c_Model = result.getModel(new LinkedHashSet<>(primedAssignables));
                        AbstractState q_ = Streams.filterWithIndex(RQ_.stream(), entry -> entry.getKey().equals(q_Model.get(q_Index).getValue())).findFirst().get().getValue();
                        Delta.add(new AbstractTransition(q, e, q_));
                        ConcreteState c = CMappings.get(cModel);
                        ConcreteState c_ = CMappings.getOrDefault(c_Model, new ConcreteState("c" + C.size() + q_.getName(), c_Model));
                        C.add(c_);
                        CMappings.put(c_.getMapping(), c_);
                        DeltaC.add(new ConcreteTransition(c, e, c_));
                        alpha.put(c_, q_);
                        _alpha.putIfAbsent(q_, new LinkedHashSet<>());
                        _alpha.get(q_).add(c_);
                        kappa.put(c_, GREEN);
                        RQ.add(q_);
                        RQ_.remove(q_);
                    }
                } while (result.isSAT());
            }
        }
        RQ = new LinkedHashSet<>(Q);
        while (!RQ.isEmpty()) {
            AbstractState q = RQ.iterator().next();
            RQ.remove(q);
            for (Event e : machine.getEvents().values()) {
                for (AbstractState q_ : A.stream().filter(q_ -> !Delta.contains(new AbstractTransition(q, e, q_))).collect(Collectors.toList())) {
                    result = Z3.checkSAT(new And(
                            machine.getInvariant(),
                            machine.getInvariant().accept(new Primer(1)),
                            q,
                            e.getSubstitution().getPrd(machine.getAssignables()),
                            q_.accept(new Primer(1))
                    ), machine.getDefsRegister());
                    if (result.isSAT()) {
                        Model cModel = result.getModel(machine.getAssignables());
                        Model c_Model = result.getModel(primedAssignables);
                        Delta.add(new AbstractTransition(q, e, q_));
                        ConcreteState c = CMappings.getOrDefault(cModel, new ConcreteState("c" + C.size() + q.getName(), cModel));
                        C.add(c);
                        CMappings.put(c.getMapping(), c);
                        ConcreteState c_ = CMappings.getOrDefault(c_Model, new ConcreteState("c" + C.size() + q_.getName(), c_Model));
                        C.add(c_);
                        CMappings.put(c_.getMapping(), c_);
                        DeltaC.add(new ConcreteTransition(c, e, c_));
                        alpha.put(c, q);
                        alpha.put(c_, q_);
                        _alpha.get(q).add(c);
                        _alpha.putIfAbsent(q_, new LinkedHashSet<>());
                        _alpha.get(q_).add(c_);
                        kappa.putIfAbsent(c, BLUE);
                        if (!kappa.containsKey(c_) || kappa.get(c) == GREEN) {
                            kappa.put(c_, kappa.get(c));
                        }
                        if (!Q.contains(q_)) {
                            Q.add(q_);
                            RQ.add(q_);
                        }
                    } else if (result.isUNKNOWN()) {
                        throw new Error("Result is UNKNOWN.");
                    }
                }
            }
        }
        return new ATS(machine, new MTS(Q0, Q, Delta), new CTS(C0, C, new ArrayList<>(DeltaC)), alpha, kappa);
    }

}
