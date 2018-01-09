package algorithms;

import algorithms.heuristics.*;
import algorithms.outputs.ATS;
import langs.eventb.Event;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Or;
import solvers.z3.Model;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import utilities.tuples.Tuple;
import visitors.Primer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static algorithms.heuristics.EColor.BLUE;
import static algorithms.heuristics.EColor.GREEN;

/**
 * Created by gvoiron on 15/12/17.
 * Time : 09:03
 */

public final class CXPComputer extends AComputer<ATS> {

    private final Machine machine;
    private final LinkedHashSet<AbstractState> A;
    private final LinkedHashSet<Event> oEv;
    private final IAbstractStatesOrderingFunction abstractStatesOrderingFunction;
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
    private final LinkedHashMap<EColor, LinkedHashSet<ConcreteState>> _kappa;
    private final LinkedHashSet<AAssignable> primedAssignables;

    public CXPComputer(Machine machine, LinkedHashSet<AbstractState> A) {
        this(machine, A, new DefaultEventsOrderingFunction(), new DefaultAbstractStatesOrderingFunction());
    }

    public CXPComputer(Machine machine, LinkedHashSet<AbstractState> A, IEventsOrderingFunction eventsOrderingFunction) {
        this(machine, A, eventsOrderingFunction, new DefaultAbstractStatesOrderingFunction());
    }

    public CXPComputer(Machine machine, LinkedHashSet<AbstractState> A, IAbstractStatesOrderingFunction abstractStatesOrderingFunction) {
        this(machine, A, new DefaultEventsOrderingFunction(), abstractStatesOrderingFunction);
    }

    @SuppressWarnings("WeakerAccess")
    public CXPComputer(Machine machine, LinkedHashSet<AbstractState> A, IEventsOrderingFunction eventsOrderingFunction, IAbstractStatesOrderingFunction abstractStatesOrderingFunction) {
        this.machine = machine;
        this.A = A;
        this.oEv = eventsOrderingFunction.apply(new LinkedHashSet<>(machine.getEvents().values()));
        this.abstractStatesOrderingFunction = abstractStatesOrderingFunction;
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
        this._kappa = new LinkedHashMap<>();
        this._kappa.put(BLUE, new LinkedHashSet<>());
        this._kappa.put(GREEN, new LinkedHashSet<>());
        this.primedAssignables = machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new));
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
                _kappa.get(GREEN).add(c);
            }
        }
        C.addAll(C0);
        C.forEach(c -> CMappings.put(c.getMapping(), c));
        LinkedHashSet<AbstractState> RQ = new LinkedHashSet<>(Q0);
        while (!RQ.isEmpty()) {
            AbstractState q = RQ.iterator().next();
            RQ.remove(q);
            Q.add(q);
            for (Event e : oEv) {
                for (AbstractState q_ : abstractStatesOrderingFunction.apply(new Tuple<>(A, q))) {
                    result = Z3.checkSAT(new And(
                            machine.getInvariant(),
                            machine.getInvariant().accept(new Primer(1)),
                            q,
                            e.getSubstitution().getPrd(machine.getAssignables()),
                            q_.accept(new Primer(1))
                    ), machine.getDefsRegister());
                    if (result.isSAT()) {
                        Model cwModel = result.getModel(machine.getAssignables());
                        Model cw_Model = result.getModel(primedAssignables);
                        LinkedHashSet<ConcreteState> GC = _alpha.get(q).stream().filter(cq -> _kappa.get(GREEN).contains(cq)).collect(Collectors.toCollection(LinkedHashSet::new));
                        result = Z3.checkSAT(new And(
                                machine.getInvariant(),
                                machine.getInvariant().accept(new Primer(1)),
                                new Or(GC.toArray(new ConcreteState[0])),
                                e.getSubstitution().getPrd(machine.getAssignables()),
                                q_.accept(new Primer(1))
                        ), machine.getDefsRegister());
                        if (result.isSAT()) {
                            Model cModel = result.getModel(machine.getAssignables());
                            Model c_Model = result.getModel(primedAssignables);
                            ConcreteState c = CMappings.getOrDefault(cModel, new ConcreteState("c" + C.size() + q.getName(), cModel));
                            C.add(c);
                            CMappings.putIfAbsent(c.getMapping(), c);
                            ConcreteState c_ = CMappings.getOrDefault(c_Model, new ConcreteState("c" + C.size() + q_.getName(), c_Model));
                            C.add(c_);
                            CMappings.putIfAbsent(c_.getMapping(), c_);
                            DeltaC.add(new ConcreteTransition(c, e, c_));
                            alpha.put(c, q);
                            _alpha.putIfAbsent(q, new LinkedHashSet<>());
                            _alpha.get(q).add(c);
                            alpha.put(c_, q_);
                            _alpha.putIfAbsent(q_, new LinkedHashSet<>());
                            _alpha.get(q_).add(c_);
                            kappa.put(c_, GREEN);
                            _kappa.get(GREEN).add(c_);
                            _kappa.get(BLUE).remove(c_);
                            LinkedHashSet<ConcreteState> BC = _alpha.get(q_).stream().filter(c_q_ -> _kappa.get(BLUE).contains(c_q_)).collect(Collectors.toCollection(LinkedHashSet::new));
                            result = Z3.checkSAT(new And(
                                    machine.getInvariant(),
                                    machine.getInvariant().accept(new Primer(1)),
                                    new Or(GC.toArray(new ConcreteState[0])),
                                    e.getSubstitution().getPrd(machine.getAssignables()),
                                    new Or(BC.stream().map(c_q_ -> c_q_.accept(new Primer(1))).toArray(ABoolExpr[]::new))
                            ), machine.getDefsRegister());
                            if (result.isSAT()) {
                                cModel = result.getModel(machine.getAssignables());
                                c_Model = result.getModel(primedAssignables);
                                c = CMappings.getOrDefault(cModel, new ConcreteState("c" + C.size() + q.getName(), cModel));
                                C.add(c);
                                CMappings.putIfAbsent(c.getMapping(), c);
                                c_ = CMappings.getOrDefault(c_Model, new ConcreteState("c" + C.size() + q_.getName(), c_Model));
                                C.add(c_);
                                CMappings.putIfAbsent(c_.getMapping(), c_);
                                DeltaC.add(new ConcreteTransition(c, e, c_));
                                kappa.put(c_, GREEN);
                                _kappa.get(GREEN).add(c_);
                                _kappa.get(BLUE).remove(c_);
                                propagateGreenColor(c_);
                            }
                        }
                        Delta.add(new AbstractTransition(q, e, q_));
                        ConcreteState cw = CMappings.getOrDefault(cwModel, new ConcreteState("c" + C.size() + q.getName(), cwModel));
                        C.add(cw);
                        CMappings.putIfAbsent(cw.getMapping(), cw);
                        ConcreteState cw_ = CMappings.getOrDefault(cw_Model, new ConcreteState("c" + C.size() + q_.getName(), cw_Model));
                        C.add(cw_);
                        CMappings.putIfAbsent(cw_.getMapping(), cw_);
                        DeltaC.add(new ConcreteTransition(cw, e, cw_));
                        alpha.put(cw, q);
                        _alpha.putIfAbsent(q, new LinkedHashSet<>());
                        _alpha.get(q).add(cw);
                        alpha.put(cw_, q_);
                        _alpha.putIfAbsent(q_, new LinkedHashSet<>());
                        _alpha.get(q_).add(cw_);
                        if (!kappa.containsKey(cw)) {
                            kappa.putIfAbsent(cw, BLUE);
                            _kappa.putIfAbsent(BLUE, new LinkedHashSet<>());
                            _kappa.get(BLUE).add(cw);
                        }
                        if (!kappa.containsKey(cw_) || kappa.get(cw) == GREEN) {
                            kappa.put(cw_, kappa.get(cw));
                            _kappa.putIfAbsent(kappa.get(cw), new LinkedHashSet<>());
                            _kappa.get(kappa.get(cw)).add(cw_);
                        }
                        if (!Q.contains(q_)) {
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

    private void propagateGreenColor(ConcreteState c) {
        DeltaC.stream().filter(t -> t.getSource().equals(c) && kappa.get(t.getTarget()) == BLUE).map(ATransition::getTarget).forEach(c_ -> {
            kappa.put(c_, GREEN);
            _kappa.get(BLUE).remove(c_);
            _kappa.get(GREEN).add(c_);
            propagateGreenColor(c_);
        });
    }

}
