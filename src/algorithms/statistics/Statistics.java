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

import static utilities.ResourcesManager.EAbstractionPredicatesSet;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 17:23
 */

final class Statistics extends LinkedHashMap<EStatistic, AStatistic> {

    private final ATS ats;
    private final EAbstractionPredicatesSet abstractionPredicatesSet;
    private final LinkedHashSet<Predicate> abstractionPredicates;
    private final Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>> expectedPart;
    private final Time atsComputationTime;
    private ComputerResult<Tuple<LinkedHashSet<ConcreteState>, ArrayList<ConcreteTransition>>> rchdConcretePart;
    private Tuple<LinkedHashSet<AbstractState>, ArrayList<AbstractTransition>> rchdAbstractPart;
    private ComputerResult<List<Test>> tests;
    private Integer expectedRchdPart;

    @SuppressWarnings("WeakerAccess")
    public Statistics(ATS ats, EAbstractionPredicatesSet abstractionPredicatesSet, LinkedHashSet<Predicate> abstractionPredicates, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>> expectedPart, Time atsComputationTime) {
        this(ats, abstractionPredicatesSet, abstractionPredicates, expectedPart, atsComputationTime, EStatistic.values());
    }

    @SuppressWarnings("WeakerAccess")
    public Statistics(ATS ats, EAbstractionPredicatesSet abstractionPredicatesSet, LinkedHashSet<Predicate> abstractionPredicates, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>> expectedPart, Time atsComputationTime, EStatistic... statistics) {
        this.ats = ats;
        this.abstractionPredicatesSet = abstractionPredicatesSet;
        this.abstractionPredicates = abstractionPredicates;
        this.expectedPart = expectedPart;
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
                case NB_EXPECTED_AS:
                    value = getNbExpectedAS();
                    break;
                case NB_EXPECTED_AS_RCHD:
                    value = getNbExpectedASRchd();
                    break;
                case TAU_EXPECTED_AS:
                    value = getTauExpectedAS();
                    break;
                case NB_EXPECTED_AT:
                    value = getNbExpectedAT();
                    break;
                case NB_EXPECTED_AT_RCHD:
                    value = getNbExpectedATRchd();
                    break;
                case TAU_EXPECTED_AT:
                    value = getTauExpectedAT();
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
                case SET_EXPECTED_AS:
                    value = getSetExpectedAS();
                    break;
                case SET_RCHD_AS:
                    value = getSetRchdAS();
                    break;
                case SET_RCHD_EXPECTED_AS:
                    value = getSetRchdExpectedAS();
                    break;
                case SET_EXPECTED_AT:
                    value = getSetExpectedAT();
                    break;
                case SET_RCHD_AT:
                    value = getSetRchdAT();
                    break;
                case SET_RCHD_EXPECTED_AT:
                    value = getSetRchdExpectedAT();
                    break;
                case SET_UNRCHD_AS:
                    value = getSetUnrchdAS();
                    break;
                case SET_UNRCHD_EXPECTED_AS:
                    value = getSetUnrchdExpectedAS();
                    break;
                case SET_UNRCHD_AT:
                    value = getSetUnrchdAT();
                    break;
                case SET_UNRCHD_EXPECTED_AT:
                    value = getSetUnrchdExpectedAT();
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

    private StringStatistic getAP() {
        return new StringStatistic(abstractionPredicatesSet.toString());
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

    private IntegerStatistic getNbExpectedAS() {
        return new IntegerStatistic(getSetExpectedAS().getValue().size());
    }

    private IntegerStatistic getNbExpectedASRchd() {
        return new IntegerStatistic(getExpectedRchdPart().getLeft().size());
    }

    private PercentageStatistic getTauExpectedAS() {
        return new PercentageStatistic(getNbExpectedAS().getValue() == 0 ? 0 : 100d * getNbExpectedASRchd().getValue() / getNbExpectedAS().getValue());
    }

    private IntegerStatistic getNbExpectedAT() {
        return new IntegerStatistic(getSetExpectedAT().getValue().size());
    }

    private IntegerStatistic getNbExpectedATRchd() {
        return new IntegerStatistic(getExpectedRchdPart().getRight().size());
    }

    private PercentageStatistic getTauExpectedAT() {
        return new PercentageStatistic(getNbExpectedAT().getValue() == 0 ? 0 : 100d * getNbExpectedATRchd().getValue() / getNbExpectedAT().getValue());
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

    private LongStatistic getNbSteps() {
        return new LongStatistic(getTests().getValue().stream().mapToLong(Collection::size).sum());
    }

    private SetStatistic<Test> getTests() {
        return new SetStatistic<>(new LinkedHashSet<>(getTestsComputation().getResult()), false);
    }

    private SetStatistic<AbstractState> getSetExpectedAS() {
        return new SetStatistic<>(expectedPart.getLeft().stream().filter(q -> ats.getMTS().getStates().contains(q)).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private SetStatistic<AbstractState> getSetRchdAS() {
        return new SetStatistic<>(getAbstractRchdPart().getLeft());
    }

    private SetStatistic<AbstractState> getSetRchdExpectedAS() {
        return new SetStatistic<>(getExpectedRchdPart().getLeft());
    }

    private SetStatistic<AbstractTransition> getSetExpectedAT() {
        return new SetStatistic<>(expectedPart.getRight().stream().filter(t -> ats.getMTS().getTransitions().contains(t)).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private SetStatistic<AbstractTransition> getSetRchdAT() {
        return new SetStatistic<>(new LinkedHashSet<>(getAbstractRchdPart().getRight()));
    }

    private SetStatistic<AbstractTransition> getSetRchdExpectedAT() {
        return new SetStatistic<>(getExpectedRchdPart().getRight());
    }

    private SetStatistic<AbstractState> getSetUnrchdAS() {
        return new SetStatistic<>(ats.getMTS().getStates().stream().filter(q -> !getSetRchdAS().getValue().contains(q)).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private SetStatistic<AbstractState> getSetUnrchdExpectedAS() {
        return new SetStatistic<>(getSetExpectedAS().getValue().stream().filter(q -> !getSetRchdExpectedAS().getValue().contains(q)).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private SetStatistic<AbstractTransition> getSetUnrchdAT() {
        return new SetStatistic<>(ats.getMTS().getTransitions().stream().filter(t -> !getSetRchdAT().getValue().contains(t)).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private SetStatistic<AbstractTransition> getSetUnrchdExpectedAT() {
        return new SetStatistic<>(getSetExpectedAT().getValue().stream().filter(t -> !getSetRchdExpectedAT().getValue().contains(t)).collect(Collectors.toCollection(LinkedHashSet::new)));
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

    private Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>> getExpectedRchdPart() {
        return new Tuple<>(expectedPart.getLeft().stream().filter(q -> getAbstractRchdPart().getLeft().contains(q)).collect(Collectors.toCollection(LinkedHashSet::new)), expectedPart.getRight().stream().filter(t -> getAbstractRchdPart().getRight().contains(t)).collect(Collectors.toCollection(LinkedHashSet::new)));
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
            testsComputation.getResult().removeIf(ArrayList::isEmpty);
            this.tests = new ComputerResult<>(testsComputation.getResult(), new Time(scGraphComputation.getTime().getNanoseconds() + testsComputation.getTime().getNanoseconds()));
        }
        return tests;
    }

}
