package algorithms.statistics;

import algorithms.ComputerResult;
import algorithms.RchblPartComputer;
import algorithms.SCConcreteGraphComputer;
import algorithms.TestsComputer;
import algorithms.outputs.ATS;
import algorithms.outputs.Test;
import langs.formal.graphs.*;
import langs.maths.generic.bool.literals.Predicate;
import utilities.Time;
import utilities.tuples.Tuple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 17:23
 */
@SuppressWarnings("WeakerAccess")
public final class Statistics extends LinkedHashMap<EStatistic, AStatistic> {

    private final ATS ats;
    private final int abstractionPredicatesID;
    private final LinkedHashSet<Predicate> abstractionPredicates;
    private final Time atsComputationTime;
    private ComputerResult<Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>>> rchdConcretePart;
    private Tuple<LinkedHashSet<AbstractState>, ArrayList<AbstractTransition>> rchdAbstractPart;
    private ComputerResult<List<Test>> tests;

    public Statistics(ATS ats, int abstractionPredicatesID, LinkedHashSet<Predicate> abstractionPredicates, Time atsComputationTime) {
        this(ats, abstractionPredicatesID, abstractionPredicates, atsComputationTime, EStatistic.values());
    }

    public Statistics(ATS ats, int abstractionPredicatesID, LinkedHashSet<Predicate> abstractionPredicates, Time atsComputationTime, EStatistic... statistics) {
        this.ats = ats;
        this.abstractionPredicatesID = abstractionPredicatesID;
        this.abstractionPredicates = abstractionPredicates;
        this.atsComputationTime = atsComputationTime;
        for (EStatistic statistic : statistics) {
            AStatistic value;
            switch (statistic) {
                case NB_EV:
                    value = getNbEv();
                    break;
                case AP:
                    value = getAP();
                    break;
                case NB_AP:
                    value = getNbAP();
                    break;
                case NB_MAY:
                    value = getNbMay();
                    break;
                case NB_MUST_MINUS:
                    value = getNbMustMinus();
                    break;
                case NB_MUST_PLUS:
                    value = getNbMustPlus();
                    break;
                case NB_MUST_SHARP:
                    value = getNbMustSharp();
                    break;
                case NB_AS:
                    value = getNbAS();
                    break;
                case NB_AS_RCHD:
                    value = getNbASRchd();
                    break;
                case TAU_AS:
                    value = getTauAS();
                    break;
                case NB_AT:
                    value = getNbAT();
                    break;
                case NB_AT_RCHD:
                    value = getNbATRchd();
                    break;
                case TAU_AT:
                    value = getTauAT();
                    break;
                case NB_CS:
                    value = getNbCS();
                    break;
                case NB_CS_RCHD:
                    value = getNbCSRchd();
                    break;
                case NB_CT:
                    value = getNbCT();
                    break;
                case NB_CT_RCHD:
                    value = getNbCTRchd();
                    break;
                case RHO_CS:
                    value = getRhoCS();
                    break;
                case RHO_CT:
                    value = getRhoCT();
                    break;
                case NB_TESTS:
                    value = getNbTests();
                    break;
                case NB_STEPS:
                    value = getNbSteps();
                    break;
                case TESTS:
                    value = getTests();
                    break;
                case TIME_ATS:
                    value = getTimeATS();
                    break;
                case TIME_TESTS:
                    value = getTimeTests();
                    break;
                default:
                    throw new Error("Unable to compute value for unknown statistic \"" + statistic + "\".");
            }
            put(statistic, value);
        }
    }

    private IntegerStatistic getNbEv() {
        return new IntegerStatistic(ats.getMachine().getEvents().size());
    }

    public IntegerStatistic getAP() {
        return new IntegerStatistic(abstractionPredicatesID);
    }

    private IntegerStatistic getNbAP() {
        return new IntegerStatistic(abstractionPredicates.size());
    }

    private IntegerStatistic getNbMay() {
        return new IntegerStatistic(-1);
    }

    private IntegerStatistic getNbMustMinus() {
        return new IntegerStatistic(-1);
    }

    private IntegerStatistic getNbMustPlus() {
        return new IntegerStatistic(-1);
    }

    private IntegerStatistic getNbMustSharp() {
        return new IntegerStatistic(-1);
    }

    private IntegerStatistic getNbAS() {
        return new IntegerStatistic(ats.getMTS().getStates().size());
    }

    private IntegerStatistic getNbASRchd() {
        return new IntegerStatistic(getAbstractRchdPart().getLeft().size());
    }

    private PercentageStatistic getTauAS() {
        return new PercentageStatistic(getNbAS().getValue() == 0 ? 0 : 100d * getNbASRchd().getValue() / getNbAS().getValue());
    }

    private IntegerStatistic getNbAT() {
        return new IntegerStatistic(ats.getMTS().getTransitions().size());
    }

    private IntegerStatistic getNbATRchd() {
        return new IntegerStatistic(getAbstractRchdPart().getRight().size());
    }

    private PercentageStatistic getTauAT() {
        return new PercentageStatistic(getNbAT().getValue() == 0 ? 0 : 100d * getNbATRchd().getValue() / getNbAT().getValue());
    }

    private IntegerStatistic getNbCS() {
        return new IntegerStatistic(ats.getCTS().getStates().size());
    }

    private IntegerStatistic getNbCSRchd() {
        return new IntegerStatistic(getConcreteRchdPartComputation().getResult().getLeft().size());
    }

    private IntegerStatistic getNbCT() {
        return new IntegerStatistic(ats.getCTS().getTransitions().size());
    }

    private IntegerStatistic getNbCTRchd() {
        return new IntegerStatistic(getConcreteRchdPartComputation().getResult().getRight().size());
    }

    private PercentageStatistic getRhoCS() {
        return new PercentageStatistic(getNbCS().getValue() == 0 ? 0 : 100d * getNbCSRchd().getValue() / getNbCS().getValue());
    }

    private PercentageStatistic getRhoCT() {
        return new PercentageStatistic(getNbCT().getValue() == 0 ? 0 : 100d * getNbCTRchd().getValue() / getNbCT().getValue());
    }

    private IntegerStatistic getNbTests() {
        return new IntegerStatistic(getTests().getValue().size());
    }

    private IntegerStatistic getNbSteps() {
        return new IntegerStatistic(-1);
    }

    private SetStatistic<List<ConcreteTransition>> getTests() {
        return new SetStatistic<>(new LinkedHashSet<>(getTestsComputation().getResult()));
    }

    private TimeStatistic getTimeATS() {
        return new TimeStatistic(atsComputationTime);
    }

    private TimeStatistic getTimeTests() {
        return new TimeStatistic(getTestsComputation().getTime());
    }

    private ComputerResult<Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>>> getConcreteRchdPartComputation() {
        if (this.rchdConcretePart == null) {
            this.rchdConcretePart = new RchblPartComputer<>(ats.getCTS()).compute();
        }
        return rchdConcretePart;
    }

    private Tuple<LinkedHashSet<AbstractState>, ArrayList<AbstractTransition>> getAbstractRchdPart() {
        Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>> concreteRchdPart = getConcreteRchdPartComputation().getResult();
        if (this.rchdAbstractPart == null) {
            this.rchdAbstractPart = new Tuple<>(ats.getMTS().getStates().stream().filter(q -> concreteRchdPart.getLeft().stream().anyMatch(c -> ats.getAlpha().get(c).equals(q))).collect(Collectors.toCollection(LinkedHashSet::new)), ats.getMTS().getTransitions().stream().filter(at -> concreteRchdPart.getRight().stream().anyMatch(ct -> ats.getAlpha().get(ct.getSource()).equals(at.getSource()) && at.getEvent().equals(ct.getEvent()) && ats.getAlpha().get(ct.getTarget()).equals(at.getTarget()))).collect(Collectors.toCollection(ArrayList::new)));
        }
        return rchdAbstractPart;
    }

    private ComputerResult<List<Test>> getTestsComputation() {
        if (this.tests == null) {
            Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>> result = getConcreteRchdPartComputation().getResult();
            ConcreteState ghostState = new ConcreteState("__ghost__", new TreeMap<>());
            ComputerResult<AGraph<ConcreteState, ConcreteTransition>> scGraphComputation = new SCConcreteGraphComputer(new CTS(ats.getCTS().getInitialStates(), result.getLeft(), result.getRight()), ghostState, "__init__", "__reset__").compute();
            AGraph<ConcreteState, ConcreteTransition> scGraph = scGraphComputation.getResult();
            ComputerResult<List<Test>> testsComputation = new TestsComputer(ghostState, scGraph.getStates(), scGraph.getTransitions()).compute();
            for (Test test : testsComputation.getResult()) {
                test.removeIf(concreteTransition -> concreteTransition.getSource().equals(ghostState) || concreteTransition.getTarget().equals(ghostState));
            }
            this.tests = new ComputerResult<>(testsComputation.getResult(), new Time(scGraphComputation.getTime().getNanoseconds() + testsComputation.getTime().getNanoseconds()));
        }
        return tests;
    }

}
