import algorithms.EAlgorithm;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.heuristics.relevance.atomics.Condition;
import algorithms.heuristics.relevance.atomics.Conditions;
import algorithms.heuristics.relevance.atomics.funs.FunChanges;
import algorithms.heuristics.relevance.atomics.funs.FunDecreases;
import algorithms.heuristics.relevance.atomics.funs.FunIncreases;
import algorithms.heuristics.relevance.atomics.vars.VarChanges;
import algorithms.heuristics.relevance.atomics.vars.VarDecreases;
import algorithms.heuristics.relevance.atomics.vars.VarIncreases;
import langs.maths.generic.arith.literals.EnumValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.Minus;
import langs.maths.generic.arith.operators.Plus;
import langs.maths.generic.bool.operators.Equals;

import static algorithms.EAlgorithm.*;
import static algorithms.statistics.Saver.save;
import static utilities.ResourcesManager.EAbstractionPredicatesSet.*;
import static utilities.ResourcesManager.EModel.*;
import static utilities.ResourcesManager.ERelevancePredicate.*;

class Main {

    public static void main(String[] args) {
        /*savePlot("ne-varies", EV, AP0, REL0, Arrays.asList(
                new Tuple<>("NE", new Range(new Int(3), new Int(30)))
        ), Arrays.asList(
                NB_CT_RCHD,
                TIME_ATS
        ), RCXP);*/
        /*savePlot("ns-varies", L14_2, AP0, REL3, Arrays.asList(
                new Tuple<>("NS", new Range(new Int(4), new Int(30)))
        ), Arrays.asList(
                NB_CT_RCHD,
                TIME_ATS
        ), RCXP);*/
        /*Machine machine = new Parser().parseModel(ResourcesManager.getModel(CM));
        LinkedHashSet<Predicate> ap = new Parser().parseAbstractionPredicatesSet(ResourcesManager.getAbstractionPredicatesSet(CM, AP0));
        LinkedHashSet<AbstractState> as = new AbstractStatesComputer(machine, ap).compute().getResult();
        Tuple<RelevancePredicate, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>>> relevance = new Parser().parseRelevance(ResourcesManager.getRelevancePredicate(CM, REL0), as, machine.getEvents());
        Tuple<RelevancePredicate, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>>> relevance2 = new Parser().parseRelevance(ResourcesManager.getRelevancePredicate(CM, REL2), as, machine.getEvents());
        Tuple<RelevancePredicate, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>>> relevance3 = new Parser().parseRelevance(ResourcesManager.getRelevancePredicate(CM, REL3), as, machine.getEvents());
        DefaultVariantComputer rcxpVariantComputer = new DefaultVariantComputer(machine, relevance.getLeft());
        DefaultVariantComputer rcxpVariantComputer2 = new DefaultVariantComputer(machine, relevance2.getLeft());
        DefaultVariantComputer rcxpVariantComputer3 = new DefaultVariantComputer(machine, relevance3.getLeft());
        List<EStatistic> rowStatisticsFilter = Arrays.asList(NB_MAY, NB_MUST_MINUS, NB_MUST_PLUS, NB_MUST_SHARP, TESTS, SET_EXPECTED_AS, SET_RCHD_AS, SET_RCHD_EXPECTED_AS, SET_EXPECTED_AT, SET_RCHD_AT, SET_RCHD_EXPECTED_AT, SET_UNRCHD_AS, SET_UNRCHD_EXPECTED_AS, SET_UNRCHD_AT, SET_UNRCHD_EXPECTED_AT);
        File resultsFolder = ResourcesManager.getResultsFolder(CM, AP0, "default");
        File statsFolder = new File(resultsFolder, "stats");
        File dotFolder = new File(resultsFolder, "dot");
        File pdfFolder = new File(resultsFolder, "pdf");
        boolean createResultsFolders = (resultsFolder.exists() || resultsFolder.mkdirs()) && (statsFolder.exists() || statsFolder.mkdirs()) && (dotFolder.exists() || dotFolder.mkdirs()) && (pdfFolder.exists() || pdfFolder.mkdirs());
        if (!createResultsFolders) {
            throw new Error("Unable to create results folders \"" + resultsRoot + "\".");
        }
        try {
            Files.write(new File(resultsFolder, machine.getName() + ".mch").toPath(), machine.toString().getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "default" + ".ap_stat").toPath(), ("Abstraction Predicates (" + ap.size() + "):" + "\n" + "\n" + ap.stream().map(AExpr::toString).collect(Collectors.joining("\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "default" + ".as_stat").toPath(), ("Abstract States (" + as.size() + " in -1):" + "\n" + "\n" + as.stream().map(AState::toString).collect(Collectors.joining("\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "default" + ".rel_stat").toPath(), ("Relevance Predicate:" + "\n" + "\n" + relevance.getLeft()).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "default" + ".rel2_stat").toPath(), ("Relevance Predicate:" + "\n" + "\n" + relevance2.getLeft()).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "default" + ".rel3_stat").toPath(), ("Relevance Predicate:" + "\n" + "\n" + relevance3.getLeft()).getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComputerResult<ATS> cxpResult = new CXPComputer(machine, as).compute();
        ComputerResult<ATS> rcxpResult = new RCXPComputer(machine, cxpResult.getResult(), rcxpVariantComputer).compute();
        ComputerResult<ATS> rcxpResult2 = new RCXPComputer(machine, rcxpResult.getResult(), rcxpVariantComputer2).compute();
        ComputerResult<ATS> rcxpResult3 = new RCXPComputer(machine, rcxpResult2.getResult(), rcxpVariantComputer3).compute();
        Statistics statistics = new Statistics(rcxpResult.getResult(), AP0, ap, relevance.getRight(), rcxpResult.getTime());
        Statistics statistics2 = new Statistics(rcxpResult2.getResult(), AP0, ap, relevance2.getRight(), rcxpResult2.getTime());
        Statistics statistics3 = new Statistics(rcxpResult3.getResult(), AP0, ap, relevance3.getRight(), rcxpResult3.getTime());
        //Files.write(new File(statsFolder, "cxp.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
        LinkedHashMap<ConcreteState, Integer> variants = rcxpVariantComputer.getVariantsMapping().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().values().stream().mapToInt(AValue::getValue).sum(), (o1, o2) -> o1, LinkedHashMap::new));
        try {
            Files.write(new File(statsFolder, "rcxp.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "rcxp.stat").toPath(), ("Results for RCXP (in " + rcxpResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_small.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_full.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedHashMap<ConcreteState, Integer> variants2 = rcxpVariantComputer2.getVariantsMapping().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().values().stream().mapToInt(AValue::getValue).sum(), (o1, o2) -> o1, LinkedHashMap::new));
        try {
            Files.write(new File(statsFolder, "rcxp2.row").toPath(), statistics2.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "rcxp2.stat").toPath(), ("Results for RCXP2 (in " + rcxpResult2.getTime() + "):" + "\n" + "\n" + statistics2.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_small2.dot").toPath(), rcxpResult2.getResult().getCTS().accept(new DOTEncoder<>(false, LR, Collections.singletonList(new Tuple<>("v", variants2)))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_full2.dot").toPath(), rcxpResult2.getResult().getCTS().accept(new DOTEncoder<>(true, LR, Collections.singletonList(new Tuple<>("v", variants2)))).getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedHashMap<ConcreteState, Integer> variants3 = rcxpVariantComputer3.getVariantsMapping().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().values().stream().mapToInt(AValue::getValue).sum(), (o1, o2) -> o1, LinkedHashMap::new));
        try {
            Files.write(new File(statsFolder, "rcxp3.row").toPath(), statistics3.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, "rcxp3.stat").toPath(), ("Results for RCXP3 (in " + rcxpResult3.getTime() + "):" + "\n" + "\n" + statistics3.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_small3.dot").toPath(), rcxpResult3.getResult().getCTS().accept(new DOTEncoder<>(false, LR, Collections.singletonList(new Tuple<>("v", variants3)))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(dotFolder, "rcxp_full3.dot").toPath(), rcxpResult3.getResult().getCTS().accept(new DOTEncoder<>(true, LR, Collections.singletonList(new Tuple<>("v", variants3)))).getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        EAlgorithm[] algorithms = false ? new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO, FULL} : new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO};
        save("default", CM, AP0, REL0, algorithms);
    }

    @SuppressWarnings("SameParameterValue")
    private static void all(boolean computeFull) {
        EAlgorithm[] algorithms = computeFull ? new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO, FULL} : new EAlgorithm[]{CXP, CXPASO, RCXP, RCXPASO};
        long start = System.nanoTime();
        save("default", CM, AP0, REL0, algorithms);
        save("default", CM, AP1, REL1, algorithms);
        save("default", PH, AP0, REL0, algorithms);
        save("default", PH, AP1, REL0, algorithms);
        save("default", PH, AP2, REL0, algorithms);
        save("default", PH, AP3, REL0, algorithms);
        save("1rel", L14_2, AP0, REL0, algorithms);
        save("1rel", L14_2, AP1, REL1, algorithms);
        save("1rel", L14_2, AP2, REL2, algorithms);
        save("default", EL, AP0, REL0, algorithms);
        save("default", EL, AP1, REL1, algorithms);
        save("default", EL, AP2, REL2, algorithms);
        save("default", EV, AP0, REL0, algorithms);
        save("default", GSM, AP0, REL0, algorithms);
        save("default", EV, AP1, REL1, algorithms);
        save("default", GSM, AP1, REL1, algorithms);
        save("default", EV, AP2, REL2, algorithms);
        save("default", GSM, AP2, REL2, algorithms);
        save("3rel", L14_2, AP0, REL3, algorithms);
        System.out.println((System.nanoTime() - start) * 1.0E-9);
    }

    private static RelevancePredicate el() {
        return new RelevancePredicate(
                new FunChanges(new Fun("bat", new Int(1)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(2)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(3)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(4)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(5)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(6)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(7)), new EnumValue("ok"), new EnumValue("ko")),
                new FunChanges(new Fun("bat", new Int(8)), new EnumValue("ok"), new EnumValue("ko"))
        );
    }

    private static RelevancePredicate ev() {
        return new RelevancePredicate(
                new FunChanges(new Fun("PE", new Var("Pos")), new EnumValue("ouvertes"), new EnumValue("fermees")),
                new FunChanges(new Fun("PE", new Var("Pos")), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new VarChanges(new Var("PC"), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new VarChanges(new Var("PC"), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new VarChanges(new Var("Dir"), new Int(1), new Int(-1)),
                new Conditions(
                        new Condition(new Equals(new Var("Dir"), new Int(1)), new VarIncreases(new Var("Pos"))),
                        new Condition(new Equals(new Var("Dir"), new Int(-1)), new VarDecreases(new Var("Pos")))
                ),
                new FunChanges(new Fun("BM", new Plus(new Var("Pos"), new Int(1))), new Int(0), new Int(1)),
                new FunChanges(new Fun("BD", new Minus(new Var("Pos"), new Int(1))), new Int(0), new Int(1))
        );
    }

    private static RelevancePredicate cm() {
        return new RelevancePredicate(
                new VarIncreases(new Var("Balance")),
                new VarDecreases(new Var("CoffeeLeft")),
                new VarChanges(new Var("AskCoffee"), new Int(0), new Int(1)),
                new VarDecreases(new Var("Pot")),
                new VarChanges(new Var("Status"), new EnumValue("on"), new EnumValue("off")),
                new VarChanges(new Var("Status"), new EnumValue("error"), new EnumValue("off"))
        );
    }

    private static RelevancePredicate l14_1Rrel() {
        return new RelevancePredicate(
                new FunChanges(new Fun("Mvt", new Int(1)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(1)))),
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(1))))
                )
        );
    }

    private static RelevancePredicate l14_3Rrel() {
        return new RelevancePredicate(
                new FunChanges(new Fun("Mvt", new Int(1)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(1)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(1)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(1)))),
                        new Condition(new Equals(new Fun("Dir", new Int(1)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(1))))
                ),
                new FunChanges(new Fun("Mvt", new Int(2)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(2)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(2)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(2)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(2)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(2)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(2)))),
                        new Condition(new Equals(new Fun("Dir", new Int(2)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(2))))
                ),
                new FunChanges(new Fun("Mvt", new Int(3)), new Int(0), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(3)), new Int(-1), new Int(1)),
                new FunChanges(new Fun("Dir", new Int(3)), new Int(1), new Int(-1)),
                new FunChanges(new Fun("Portes", new Int(3)), new EnumValue("fermees"), new EnumValue("ouvertes")),
                new FunChanges(new Fun("Portes", new Int(3)), new EnumValue("ouvertes"), new EnumValue("refermees")),
                new Conditions(
                        new Condition(new Equals(new Fun("Dir", new Int(3)), new Int(1)), new FunIncreases(new Fun("Pos", new Int(3)))),
                        new Condition(new Equals(new Fun("Dir", new Int(3)), new Int(-1)), new FunDecreases(new Fun("Pos", new Int(3))))
                )
        );
    }

    private static RelevancePredicate ca() {
        return new RelevancePredicate(
                new VarIncreases(new Var("De")),
                new VarDecreases(new Var("Us")),
                new VarIncreases(new Var("Do")),
                new VarIncreases(new Var("Be")),
                new VarIncreases(new Var("Lo")),
                new VarIncreases(new Var("AC"))
        );
    }

    private static RelevancePredicate ph() {
        return new RelevancePredicate(
                new VarIncreases(new Var("Etat"))
        );
    }

}
