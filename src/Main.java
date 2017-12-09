import algorithms.AbstractStatesComputer;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.AbstractTransition;
import langs.formal.graphs.CTS;
import langs.formal.graphs.MTS;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import static utilities.ResourcesManager.EAbstractionPredicatesSet.AP0;
import static utilities.ResourcesManager.EModel.EXAMPLE;
import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(EXAMPLE));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(EXAMPLE, AP0));
        ArrayList<AbstractState> as = new ArrayList<>(new AbstractStatesComputer(machine, ap).compute().getResult());
        new ATS(machine, new MTS(null, null, Collections.singletonList(new AbstractTransition(null, null, null))), new CTS(null, null, null));
    }

}
