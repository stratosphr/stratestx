import algorithms.*;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import utilities.ResourcesManager;
import utilities.tuples.Tuple;
import visitors.dot.DOTEncoder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.TreeMap;

import static utilities.ResourcesManager.getAbstractionPredicatesSet;
import static utilities.ResourcesManager.getModel;

class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        ResourcesManager.EModel model = ResourcesManager.EModel.GSM;
        Machine machine = parser.parseModel(getModel(model));
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(model, ResourcesManager.EAbstractionPredicatesSet.AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        CTS cts = new CXPComputer(machine, as).compute().getResult().getCTS();
        Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>> rchblPart = new RchblPartComputer<>(cts).compute().getResult();
        AGraph<ConcreteState, ConcreteTransition> sccts = new SCConcreteGraphComputer(new CTS(cts.getInitialStates(), rchblPart.getLeft(), rchblPart.getRight()), new ConcreteState("__ghost__", new TreeMap<>()), "__init__", "__reset__").compute().getResult();
//        AGraph<ConcreteState, ConcreteTransition> sccts = new SCConcreteGraphComputer(cts, new ConcreteState("__GHOST__", new TreeMap<>()), "__INIT__", "__RESET__").compute().getResult();
        System.out.println(sccts.accept(new DOTEncoder<>(false, DOTEncoder.ERankDir.TB)));
        ArrayList<LinkedHashSet<ConcreteState>> components = new SCComponentsComputer<>(sccts).compute().getResult();
        if (components.size() != 1) {
            for (LinkedHashSet<ConcreteState> component : components) {
                for (ConcreteState concreteState : component) {
                    System.out.println(concreteState);
                }
                System.out.println("###################################");
            }
            throw new Error("GRAPH NOT STRONGLY CONNECTED!");
        }
    }

}
