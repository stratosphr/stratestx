import algorithms.AbstractStatesComputer;
import algorithms.CXPComputer;
import algorithms.ComputerResult;
import algorithms.RCXPComputer;
import algorithms.heuristics.relevance.DefaultVariantComputer;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.heuristics.relevance.atomics.vars.VarChanges;
import algorithms.heuristics.relevance.atomics.vars.VarDecreases;
import algorithms.heuristics.relevance.atomics.vars.VarIncreases;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
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
        ResourcesManager.EModel model = ResourcesManager.EModel.CM;
        Machine machine = parser.parseModel(getModel(model));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(model, ResourcesManager.EAbstractionPredicatesSet.AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        ComputerResult<ATS> cxpResult = new CXPComputer(machine, as).compute();
        ATS result = new RCXPComputer(machine, cxpResult.getResult(), new RelevancePredicate(
                new DefaultVariantComputer(),
                new VarIncreases(new Var("Balance")),
                new VarDecreases(new Var("CoffeeLeft")),
                new VarChanges(new Var("AskCoffee"), new Int(0), new Int(1)),
                new VarDecreases(new Var("Pot")),
                new VarChanges(new Var("Status"), new EnumValue("on"), new EnumValue("off")),
                new VarChanges(new Var("Status"), new EnumValue("error"), new EnumValue("off"))
        )).compute().getResult();
        System.out.println(cxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, DOTEncoder.ERankDir.TB)));
        System.out.println(result.getCTS().accept(new DOTEncoder<>(true, DOTEncoder.ERankDir.TB)));
    }

}
