import algorithms.AbstractStatesComputer;
import algorithms.CXPComputer;
import algorithms.ComputerResult;
import algorithms.FullSemanticsComputer;
import algorithms.outputs.ATS;
import algorithms.statistics.Statistics;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.CTS;
import langs.formal.graphs.MTS;
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
        ResourcesManager.EModel model = ResourcesManager.EModel.EXAMPLE;
        Machine machine = parser.parseModel(getModel(model));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(model, ResourcesManager.EAbstractionPredicatesSet.AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        ComputerResult<ATS> cxpResult = new CXPComputer(machine, as).compute();
        System.out.println(cxpResult.getResult().getCTS().accept(new DOTEncoder<>(false, DOTEncoder.ERankDir.TB)));
        System.out.println(new Statistics(cxpResult.getResult(), 0, ap, cxpResult.getTime()));
        ComputerResult<CTS> fullSemanticsComputer = new FullSemanticsComputer(machine).compute();
        ATS ats = new ATS(machine, new MTS(new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>()), fullSemanticsComputer.getResult(), null, null);
        System.out.println(new Statistics(ats, 0, ap, fullSemanticsComputer.getTime()));
    }

}
