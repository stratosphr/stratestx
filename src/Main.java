import algorithms.AbstractStatesComputer;
import algorithms.CXPComputer;
import algorithms.ComputerResult;
import algorithms.RCXPComputer;
import algorithms.heuristics.relevance.DefaultVariantComputer;
import algorithms.heuristics.relevance.FunChanges;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
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
        ATS result = new RCXPComputer(machine, cxpResult.getResult(), new RelevancePredicate(
                new DefaultVariantComputer(),
                new FunChanges(new Fun("bat", new Int(1)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(2)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(3)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(4)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(5)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(6)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(7)), new EnumValue("ok"), new EnumValue("ko"))
        )).compute().getResult();
        System.out.println(cxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, DOTEncoder.ERankDir.TB)));
        System.out.println(result.getCTS().accept(new DOTEncoder<>(true, DOTEncoder.ERankDir.TB)));
    }

}
