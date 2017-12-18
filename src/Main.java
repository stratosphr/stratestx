import algorithms.AbstractStatesComputer;
import algorithms.CXPComputer;
import algorithms.ComputerResult;
import algorithms.FullSemanticsComputer;
import algorithms.outputs.ATS;
import algorithms.statistics.Statistics;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.CTS;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import utilities.ResourcesManager;

import java.util.LinkedHashSet;

import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(ResourcesManager.EModel.EXAMPLE));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(ResourcesManager.EModel.EXAMPLE, ResourcesManager.EAbstractionPredicatesSet.AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        ComputerResult<ATS> cxp = new CXPComputer(machine, as).compute();
        System.out.println(cxp.getTime());
        ComputerResult<CTS> full = new FullSemanticsComputer(machine).compute();
        System.out.println(full.getTime());
        Statistics statistics = new Statistics(cxp.getResult(), 0, ap, cxp.getTime());
        statistics.forEach((statistic, value) -> System.out.println(statistic + ": " + value));
        System.out.println(cxp.getResult().getCTS());
    }

}
