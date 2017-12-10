import algorithms.AbstractStatesComputer;
import algorithms.CXPASOComputer;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import visitors.dot.DOTEncoder;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static utilities.ResourcesManager.EAbstractionPredicatesSet.AP0;
import static utilities.ResourcesManager.EModel.GSM;
import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;
import static visitors.dot.DOTEncoder.ERankDir.LR;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(GSM));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(GSM, AP0));
        ArrayList<AbstractState> as = new ArrayList<>(new AbstractStatesComputer(machine, ap).compute().getResult());
        ATS result = new CXPASOComputer(machine, as).compute().getResult();
        System.out.println(result.getMTS().accept(new DOTEncoder<>(false, LR)));
        System.out.println(result.getCTS().accept(new DOTEncoder<>(false, LR)));
    }

}
