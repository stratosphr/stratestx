import algorithms.AbstractStatesComputer;
import algorithms.CXPComputer;
import algorithms.ComputerResult;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import utilities.ResourcesManager;
import visitors.dot.DOTEncoder;

import java.util.LinkedHashSet;

import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(ResourcesManager.EModel.EL));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(ResourcesManager.EModel.EL, ResourcesManager.EAbstractionPredicatesSet.AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        ComputerResult<ATS> result = new CXPComputer(machine, as).compute();
        System.out.println(result.getResult().getCTS().accept(new DOTEncoder<>(true, DOTEncoder.ERankDir.TB)));
        System.out.println(result.getResult().getMTS().accept(new DOTEncoder<>(false, DOTEncoder.ERankDir.TB)));
    }

}
