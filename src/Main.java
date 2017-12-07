import algorithms.AbstractStatesComputer;
import algorithms.FullSemanticsComputer;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import langs.formal.graphs.FSM;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.operators.And;
import parsers.stratest.Parser;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static utilities.ResourcesManager.EAbstractionPredicatesSet.AP0;
import static utilities.ResourcesManager.EModel.EXAMPLE;
import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(EXAMPLE));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(EXAMPLE, AP0));
        for (Predicate predicate : ap) {
            System.out.println(predicate);
        }
        ArrayList<AbstractState> as = new ArrayList<>(new AbstractStatesComputer(machine, ap).compute().getResult());
        for (AbstractState a : as) {
            System.out.println(a);
        }
        Z3Result result = Z3.checkSAT(new And(
                machine.getInvariant(),
                machine.getInvariant().accept(new Primer(1)),
                machine.getInitialisation().getPrd(machine.getAssignables()),
                as.get(1).accept(new Primer(1))
        ), machine.getDefsRegister());
        if (result.isSAT()) {
            System.out.println(result.getModel(machine.getAssignables().stream().map(assignable -> assignable.accept(new Primer(1))).collect(Collectors.toCollection(LinkedHashSet::new))));
        } else {
            System.out.println("UNSAT");
        }
        FSM<ConcreteState, ConcreteTransition> result1 = new FullSemanticsComputer(machine).compute().getResult();
        System.out.println(result1);
    }

}
