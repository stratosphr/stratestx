package langs.eventb;

import langs.eventb.substitutions.ASubstitution;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ASetExpr;
import utilities.Tuple;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 23:10
 */
public class Machine {

    private final DefsRegister defsRegister;
    private final Invariant invariant;
    private final ASubstitution initialisation;
    private final LinkedHashMap<String, Event> events;
    private final LinkedHashSet<AAssignable> assignables;

    public Machine(DefsRegister defsRegister, Invariant invariant, ASubstitution initialisation, LinkedHashSet<Event> events) {
        this.defsRegister = defsRegister;
        this.invariant = invariant;
        this.initialisation = initialisation;
        this.events = new LinkedHashMap<>(events.stream().collect(Collectors.toMap(Event::getName, Function.identity())));
        this.assignables = new LinkedHashSet<>();
        defsRegister.getVarsDefs().keySet().forEach(name -> assignables.add(new Var(name)));
        defsRegister.getFunsDefs().forEach((name, tuple) -> tuple.getLeft().getElementsValues(defsRegister).forEach(value -> {
            assignables.add(new Fun(name, value));
        }));
    }

    public LinkedHashMap<String, AArithExpr> getConstsDefs() {
        return defsRegister.getConstsDefs();
    }

    public LinkedHashMap<String, AFiniteSetExpr> getNamedSetsDefs() {
        return defsRegister.getNamedSetsDefs();
    }

    public LinkedHashMap<String, ASetExpr> getVarsDefs() {
        return defsRegister.getVarsDefs();
    }

    public LinkedHashMap<String, Tuple<AFiniteSetExpr, ASetExpr>> getFunsDefs() {
        return defsRegister.getFunsDefs();
    }

    public Invariant getInvariant() {
        return invariant;
    }

    public ASubstitution getInitialisation() {
        return initialisation;
    }

    public LinkedHashMap<String, Event> getEvents() {
        return events;
    }

    public LinkedHashSet<AAssignable> getAssignables() {
        return assignables;
    }

}
