package algorithms.statistics;

import algorithms.*;
import algorithms.heuristics.relevance.ReducedVariantComputer;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.*;
import langs.maths.AExpr;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.set.AFiniteSetExpr;
import parsers.stratest.Parser;
import utilities.tuples.Tuple;
import visitors.dot.DOTEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static algorithms.EAlgorithm.*;
import static algorithms.statistics.EStatistic.*;
import static java.nio.file.StandardOpenOption.*;
import static utilities.ResourcesManager.*;
import static visitors.dot.DOTEncoder.ERankDir.LR;

/**
 * Created by gvoiron on 21/12/17.
 * Time : 00:31
 */
public final class Saver {

    public static void save(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, EAlgorithm... algorithms) {
        save(identifier, model, abstractionPredicatesSet, relevancePredicate, false, algorithms);
    }

    @SuppressWarnings("WeakerAccess")
    public static void save(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, boolean saveFullDOT, EAlgorithm... algorithms) {
        save(identifier, model, abstractionPredicatesSet, relevancePredicate, new LinkedHashMap<>(), saveFullDOT, algorithms);
    }

    @SuppressWarnings({"ConstantConditions", "WeakerAccess"})
    public static void save(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, LinkedHashMap<String, AArithExpr> parameters, boolean saveFullDOT, EAlgorithm... algorithms) {
        Parser parser = new Parser();
        Machine machine = parser.parseModel(getModel(model), parameters);
        LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(model, abstractionPredicatesSet));
        ComputerResult<LinkedHashSet<AbstractState>> asResult = new AbstractStatesComputer(machine, ap).compute();
        LinkedHashSet<AbstractState> as = asResult.getResult();
        Tuple<RelevancePredicate, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>>> relevance = parser.parseRelevance(getRelevancePredicate(model, relevancePredicate), as, machine.getEvents());
        RelevancePredicate rp = relevance.getLeft();
        File resultsFolder = getResultsFolder(model, abstractionPredicatesSet, identifier);
        File statsFolder = new File(resultsFolder, "stats");
        File dotFolder = new File(resultsFolder, "dot");
        File pdfFolder = new File(resultsFolder, "pdf");
        boolean createResultsFolders = (resultsFolder.exists() || resultsFolder.mkdirs()) && (statsFolder.exists() || statsFolder.mkdirs()) && (dotFolder.exists() || dotFolder.mkdirs()) && (pdfFolder.exists() || pdfFolder.mkdirs());
        if (!createResultsFolders) {
            throw new Error("Unable to create results folders \"" + resultsRoot + "\".");
        }
        try {
            Files.write(new File(resultsFolder, machine.getName() + ".mch").toPath(), machine.toString().getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, identifier + ".ap_stat").toPath(), ("Abstraction Predicates (" + ap.size() + "):" + "\n" + "\n" + ap.stream().map(AExpr::toString).collect(Collectors.joining("\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, identifier + ".as_stat").toPath(), ("Abstract States (" + as.size() + " in " + asResult.getTime() + "):" + "\n" + "\n" + as.stream().map(AState::toString).collect(Collectors.joining("\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            Files.write(new File(statsFolder, identifier + ".rel_stat").toPath(), ("Relevance Predicate:" + "\n" + "\n" + rp).getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CXPComputer cxpComputer = new CXPComputer(machine, as);
        CXPASOComputer cxpasoComputer = new CXPASOComputer(machine, as);
        ReducedVariantComputer rcxpVariantComputer = new ReducedVariantComputer(machine, rp);
        ReducedVariantComputer rcxpasoVariantComputer = new ReducedVariantComputer(machine, rp);
        FullSemanticsComputer fullSemanticsComputer = new FullSemanticsComputer(machine, as);
        ComputerResult<ATS> cxpResult = null;
        ComputerResult<ATS> cxpasoResult = null;
        ComputerResult<ATS> rcxpResult = null;
        ComputerResult<ATS> rcxpasoResult = null;
        ComputerResult<ATS> fullResult = null;
        MTS mts = null;
        System.out.println("System: " + machine.getName());
        for (EAlgorithm algorithm : algorithms) {
            switch (algorithm) {
                case CXP:
                    if (cxpResult == null) {
                        System.out.print("CXP... ");
                        cxpResult = cxpComputer.compute();
                        mts = cxpResult.getResult().getMTS();
                        System.out.println("Done.");
                    }
                    break;
                case CXPASO:
                    if (cxpasoResult == null) {
                        System.out.print("CXPASO... ");
                        cxpasoResult = cxpasoComputer.compute();
                        mts = cxpasoResult.getResult().getMTS();
                        System.out.println("Done.");
                    }
                    break;
                case RCXP:
                    if (cxpResult == null) {
                        System.out.print("CXP... ");
                        cxpResult = cxpComputer.compute();
                        mts = cxpResult.getResult().getMTS();
                        System.out.println("Done.");
                    }
                    if (rcxpResult == null) {
                        System.out.print("RCXP... ");
                        rcxpResult = new RCXPComputer(machine, cxpResult.getResult(), rcxpVariantComputer).compute();
                        System.out.println("Done.");
                    }
                    break;
                case RCXPASO:
                    if (cxpasoResult == null) {
                        System.out.print("CXPASO... ");
                        cxpasoResult = cxpasoComputer.compute();
                        mts = cxpasoResult.getResult().getMTS();
                        System.out.println("Done.");
                    }
                    if (rcxpasoResult == null) {
                        System.out.print("RCXPASO... ");
                        rcxpasoResult = new RCXPComputer(machine, cxpasoResult.getResult(), rcxpVariantComputer).compute();
                        System.out.println("Done.");
                    }
                    break;
                case FULL:
                    if (fullResult == null) {
                        System.out.print("FULL... ");
                        fullResult = fullSemanticsComputer.compute();
                        mts = fullResult.getResult().getMTS();
                        System.out.println("Done.");
                    }
                    break;
                default:
                    throw new Error("Unable to compute unknown algorithm \"" + algorithm + "\".");
            }
        }
        try {
            List<EStatistic> rowStatisticsFilter = Arrays.asList(NB_MAY, NB_MUST_MINUS, NB_MUST_PLUS, NB_MUST_SHARP, TESTS, SET_EXPECTED_AS, SET_RCHD_AS, SET_RCHD_EXPECTED_AS, SET_EXPECTED_AT, SET_RCHD_AT, SET_RCHD_EXPECTED_AT, SET_UNRCHD_AS, SET_UNRCHD_EXPECTED_AS, SET_UNRCHD_AT, SET_UNRCHD_EXPECTED_AT);
            if (mts != null) {
                Files.write(new File(dotFolder, "mts_small.dot").toPath(), mts.accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "mts_full.dot").toPath(), mts.accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (cxpResult != null) {
                Statistics statistics = new Statistics(cxpResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), cxpResult.getTime());
                Files.write(new File(statsFolder, "cxp.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "cxp.stat").toPath(), ("Results for CXP (in " + cxpResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "cxp_small.dot").toPath(), cxpResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "cxp_full.dot").toPath(), cxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (cxpasoResult != null) {
                Statistics statistics = new Statistics(cxpasoResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), cxpasoResult.getTime());
                Files.write(new File(statsFolder, "cxpaso.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "cxpaso.stat").toPath(), ("Results for CXPASO (in " + cxpasoResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "cxpaso_small.dot").toPath(), cxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "cxpaso_full.dot").toPath(), cxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (rcxpResult != null) {
                Statistics statistics = new Statistics(rcxpResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), rcxpResult.getTime());
                LinkedHashMap<ConcreteState, Integer> variants = rcxpVariantComputer.getVariantsMapping().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        o -> o.getValue().values().stream().mapToInt(AValue::getValue).sum(),
                        (o1, o2) -> o1,
                        LinkedHashMap::new
                ));
                Files.write(new File(statsFolder, "rcxp.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "rcxp.stat").toPath(), ("Results for RCXP (in " + rcxpResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxp_small.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxp_full.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (rcxpasoResult != null) {
                LinkedHashMap<ConcreteState, Integer> variants = rcxpasoVariantComputer.getVariantsMapping().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        o -> o.getValue().values().stream().mapToInt(AValue::getValue).sum(),
                        (o1, o2) -> o1,
                        LinkedHashMap::new
                ));
                Statistics statistics = new Statistics(rcxpasoResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), rcxpasoResult.getTime());
                Files.write(new File(statsFolder, "rcxpaso.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "rcxpaso.stat").toPath(), ("Results for RCXPASO (in " + rcxpasoResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxpaso_small.dot").toPath(), rcxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxpaso_full.dot").toPath(), rcxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR, Collections.singletonList(new Tuple<>("v", variants)))).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (fullResult != null) {
                System.out.println(fullResult.getTime());
                List<EStatistic> fullStatisticsFilter = new ArrayList<>(Arrays.asList(EStatistic.values()));
                fullStatisticsFilter.remove(TESTS);
                Statistics statistics = new Statistics(fullResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), fullResult.getTime(), fullStatisticsFilter.toArray(new EStatistic[0]));
                //Statistics statistics = new Statistics(fullResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), fullResult.getTime());
                Files.write(new File(statsFolder, "full.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "full.stat").toPath(), ("Results for FULL (in " + fullResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                if (saveFullDOT) {
                    Files.write(new File(dotFolder, "full_small.dot").toPath(), fullResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                    Files.write(new File(dotFolder, "full_full.dot").toPath(), fullResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void savePlot(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, List<Tuple<String, AFiniteSetExpr>> x, List<EStatistic> y, EAlgorithm... algorithms) {
        Parser parser = new Parser();
        DefsRegister defsRegister = parser.parseModel(getModel(model), new LinkedHashMap<>()).getDefsRegister();
        int nbValues = -1;
        for (Tuple<String, AFiniteSetExpr> tuple : x) {
            if (nbValues == -1) {
                nbValues = tuple.getRight().getElementsValues(defsRegister).size();
            } else {
                if (tuple.getRight().getElementsValues(defsRegister).size() != nbValues) {
                    throw new Error("Error: the number of values for parameter \"" + tuple.getLeft() + "\" must be equal to " + nbValues + " (was " + tuple.getRight().getElementsValues(defsRegister).size() + ") since the domain of the first parameter \"" + x.get(0).getLeft() + "\" is " + nbValues + ".");
                }
            }
        }
        File resultsFolder = getResultsFolder(model, abstractionPredicatesSet, identifier);
        File statsFolder = new File(resultsFolder, "stats");
        File pdfFolder = new File(resultsFolder, "pdf");
        boolean createResultsFolders = (resultsFolder.exists() || resultsFolder.mkdirs()) && (statsFolder.exists() || statsFolder.mkdirs()) && (pdfFolder.exists() || pdfFolder.mkdirs());
        if (!createResultsFolders) {
            throw new Error("Unable to create results folders \"" + resultsRoot + "\".");
        }
        try {
            Files.deleteIfExists(new File(statsFolder, identifier + ".plot").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < nbValues; i++) {
            LinkedHashMap<String, AArithExpr> parameters = new LinkedHashMap<>();
            for (Tuple<String, AFiniteSetExpr> tuple : x) {
                parameters.put(tuple.getLeft(), new ArrayList<>(tuple.getRight().getElementsValues(defsRegister)).get(i));
            }
            Machine machine = parser.parseModel(getModel(model), parameters);
            LinkedHashSet<Predicate> ap = parser.parseAbstractionPredicatesSet(getAbstractionPredicatesSet(model, abstractionPredicatesSet));
            ComputerResult<LinkedHashSet<AbstractState>> asResult = new AbstractStatesComputer(machine, ap).compute();
            LinkedHashSet<AbstractState> as = asResult.getResult();
            Tuple<RelevancePredicate, Tuple<LinkedHashSet<AbstractState>, LinkedHashSet<AbstractTransition>>> relevance = parser.parseRelevance(getRelevancePredicate(model, relevancePredicate), as, machine.getEvents());
            RelevancePredicate rp = relevance.getLeft();
            CXPComputer cxpComputer = new CXPComputer(machine, as);
            CXPASOComputer cxpasoComputer = new CXPASOComputer(machine, as);
            FullSemanticsComputer fullSemanticsComputer = new FullSemanticsComputer(machine, as);
            ComputerResult<ATS> cxpResult = null;
            ComputerResult<ATS> cxpasoResult = null;
            ComputerResult<ATS> rcxpResult = null;
            ComputerResult<ATS> rcxpasoResult = null;
            ComputerResult<ATS> fullResult = null;
            System.out.println("System: " + machine.getName());
            StringBuilder row = new StringBuilder();
            if (i == 0) {
                parameters.forEach((key, value) -> row.append(key).append(" "));
                Arrays.asList(algorithms).forEach(algorithm -> {
                    row.append(algorithm).append("[ ");
                    y.forEach(statistic -> row.append(statistic.toString()).append(" "));
                    row.append("] ");
                });
                row.append("\n");
            }
            for (EAlgorithm algorithm : algorithms) {
                switch (algorithm) {
                    case CXP:
                        if (cxpResult == null) {
                            System.out.print("CXP... ");
                            cxpResult = cxpComputer.compute();
                            System.out.println("Done.");
                        }
                        break;
                    case CXPASO:
                        if (cxpasoResult == null) {
                            System.out.print("CXPASO... ");
                            cxpasoResult = cxpasoComputer.compute();
                            System.out.println("Done.");
                        }
                        break;
                    case RCXP:
                        if (cxpResult == null) {
                            System.out.print("CXP... ");
                            cxpResult = cxpComputer.compute();
                            System.out.println("Done.");
                        }
                        if (rcxpResult == null) {
                            System.out.print("RCXP... ");
                            rcxpResult = new RCXPComputer(machine, cxpResult.getResult(), new ReducedVariantComputer(machine, rp)).compute();
                            System.out.println("Done.");
                        }
                        break;
                    case RCXPASO:
                        if (cxpasoResult == null) {
                            System.out.print("CXPASO... ");
                            cxpasoResult = cxpasoComputer.compute();
                            System.out.println("Done.");
                        }
                        if (rcxpasoResult == null) {
                            System.out.print("RCXPASO... ");
                            rcxpasoResult = new RCXPComputer(machine, cxpasoResult.getResult(), new ReducedVariantComputer(machine, rp)).compute();
                            System.out.println("Done.");
                        }
                        break;
                    case FULL:
                        if (fullResult == null) {
                            System.out.print("FULL... ");
                            fullResult = fullSemanticsComputer.compute();
                            System.out.println("Done.");
                        }
                        break;
                    default:
                        throw new Error("Unable to compute unknown algorithm \"" + algorithm + "\".");
                }
            }
            parameters.forEach((key, value) -> row.append(value).append(" "));
            if (Arrays.asList(algorithms).contains(CXP) && cxpResult != null) {
                Statistics statistics = new Statistics(cxpResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), cxpResult.getTime(), y.toArray(new EStatistic[0]));
                row.append(statistics.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")));
                row.append(" ");
            }
            if (Arrays.asList(algorithms).contains(CXPASO) && cxpasoResult != null) {
                Statistics statistics = new Statistics(cxpasoResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), cxpasoResult.getTime(), y.toArray(new EStatistic[0]));
                row.append(statistics.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")));
                row.append(" ");
            }
            if (Arrays.asList(algorithms).contains(RCXP) && rcxpResult != null) {
                Statistics statistics = new Statistics(rcxpResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), rcxpResult.getTime(), y.toArray(new EStatistic[0]));
                row.append(statistics.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")));
                row.append(" ");
            }
            if (Arrays.asList(algorithms).contains(RCXPASO) && rcxpasoResult != null) {
                Statistics statistics = new Statistics(rcxpasoResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), rcxpasoResult.getTime(), y.toArray(new EStatistic[0]));
                row.append(statistics.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")));
                row.append(" ");
            }
            if (Arrays.asList(algorithms).contains(FULL) && fullResult != null) {
                Statistics statistics = new Statistics(fullResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), fullResult.getTime(), y.toArray(new EStatistic[0]));
                row.append(statistics.entrySet().stream().map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")));
                row.append(" ");
            }
            row.append("\n");
            try {
                Files.write(new File(statsFolder, identifier + ".plot").toPath(), row.toString().getBytes(), CREATE, APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
