import algorithms.AbstractStatesComputer;
import algorithms.CXPASOComputer;
import algorithms.ComputerResult;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import utilities.ResourcesManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(ResourcesManager.EModel.EXAMPLE));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(ResourcesManager.EModel.EXAMPLE, ResourcesManager.EAbstractionPredicatesSet.AP0));
        ArrayList<AbstractState> as = new ArrayList<>(new AbstractStatesComputer(machine, ap).compute().getResult());
        ComputerResult<ATS> result = new CXPASOComputer(machine, as).compute();
    }

}
