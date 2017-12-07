package algorithms;

import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.operators.And;
import solvers.z3.Z3;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeMap;

/**
 * Created by gvoiron on 07/12/17.
 * Time : 20:56
 */
public final class AbstractStatesComputer extends AComputer<LinkedHashSet<AbstractState>> {

    private final Machine machine;
    private final LinkedHashSet<Predicate> abstractionPredicates;

    public AbstractStatesComputer(Machine machine, LinkedHashSet<Predicate> abstractionPredicates) {
        this.machine = machine;
        this.abstractionPredicates = abstractionPredicates;
    }

    @Override
    LinkedHashSet<AbstractState> run() {
        LinkedHashSet<AbstractState> abstractStates = new LinkedHashSet<>();
        for (int i = 0; i < Math.pow(2, abstractionPredicates.size()); i++) {
            TreeMap<Predicate, Boolean> mapping = new TreeMap<>();
            Iterator<Predicate> iterator = abstractionPredicates.iterator();
            String binaryString = String.format("%" + abstractionPredicates.size() + "s", Integer.toBinaryString(i)).replace(' ', '0');
            for (int j = 0; j < binaryString.length(); j++) {
                mapping.put(iterator.next(), binaryString.charAt(j) == '1');
            }
            AbstractState abstractState = new AbstractState("q" + i, mapping);
            //AbstractState abstractState = new AbstractState("q" + binaryString, mapping);
            if (Z3.checkSAT(new And(machine.getInvariant(), abstractState), machine.getDefsRegister()).isSAT()) {
                abstractStates.add(abstractState);
            } else if (Z3.checkSAT(new And(machine.getInvariant(), abstractState), machine.getDefsRegister()).isUNKNOWN()) {
                throw new Error("Error: solver returned \"unknown\".");
            }
        }
        return abstractStates;
    }

}
