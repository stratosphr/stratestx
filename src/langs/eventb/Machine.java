package langs.eventb;

import langs.AObject;
import langs.eventb.substitutions.ASubstitution;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ASetExpr;
import utilities.tuples.Tuple;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 23:10
 */
public final class Machine extends AObject {

    private final String name;
    private final DefsRegister defsRegister;
    private final Invariant invariant;
    private final ASubstitution initialisation;
    private final LinkedHashMap<String, Event> events;
    private final LinkedHashSet<AAssignable> assignables;

    public Machine(String name, DefsRegister defsRegister, Invariant invariant, ASubstitution initialisation, LinkedHashSet<Event> events) {
        this.name = name;
        this.defsRegister = defsRegister;
        this.invariant = invariant;
        this.initialisation = initialisation;
        this.events = events.stream().collect(Collectors.toMap(Event::getName, Function.identity(), (event1, event2) -> event1, LinkedHashMap::new));
        this.assignables = new LinkedHashSet<>();
        defsRegister.getVarsDefs().keySet().forEach(varName -> assignables.add(new Var(varName)));
        defsRegister.getFunsDefs().forEach((funName, tuple) -> tuple.getLeft().getElementsValues(defsRegister).forEach(value -> {
            assignables.add(new Fun(funName, value));
        }));
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    public String getName() {
        return name;
    }

    public DefsRegister getDefsRegister() {
        return defsRegister;
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

    @Override
    public Machine clone() {
        return new Machine(name, new DefsRegister(defsRegister), invariant.clone(), initialisation.clone(), events.values().stream().map(Event::clone).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

}
