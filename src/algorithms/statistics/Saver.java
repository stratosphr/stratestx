package algorithms.statistics;

import algorithms.*;
import algorithms.heuristics.relevance.ReducedVariantComputer;
import algorithms.heuristics.relevance.RelevancePredicate;
import algorithms.outputs.ATS;
import langs.eventb.Machine;
import langs.formal.graphs.AState;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.AbstractTransition;
import langs.formal.graphs.MTS;
import langs.maths.AExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.bool.literals.Predicate;
import parsers.stratest.Parser;
import utilities.tuples.Tuple;
import visitors.dot.DOTEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static algorithms.statistics.EStatistic.*;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static utilities.ResourcesManager.*;
import static visitors.dot.DOTEncoder.ERankDir.LR;

/**
 * Created by gvoiron on 21/12/17.
 * Time : 00:31
 */
public final class Saver {

    public static void save(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, EAlgorithm... algorithms) {
        save(identifier, model, abstractionPredicatesSet, relevancePredicate, new LinkedHashMap<>(), algorithms);
    }

    @SuppressWarnings({"ConstantConditions", "WeakerAccess"})
    public static void save(String identifier, EModel model, EAbstractionPredicatesSet abstractionPredicatesSet, ERelevancePredicate relevancePredicate, LinkedHashMap<String, AArithExpr> parameters, EAlgorithm... algorithms) {
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
        FullSemanticsComputer fullSemanticsComputer = new FullSemanticsComputer(machine, as);
        ComputerResult<ATS> cxpResult = null;
        ComputerResult<ATS> cxpasoResult = null;
        ComputerResult<ATS> rcxpResult = null;
        ComputerResult<ATS> rcxpasoResult = null;
        ComputerResult<ATS> fullResult = null;
        MTS mts = null;
        for (EAlgorithm algorithm : algorithms) {
            switch (algorithm) {
                case CXP:
                    if (cxpResult == null) {
                        cxpResult = cxpComputer.compute();
                        mts = cxpResult.getResult().getMTS();
                    }
                    break;
                case CXPASO:
                    if (cxpasoResult == null) {
                        cxpasoResult = cxpasoComputer.compute();
                        mts = cxpasoResult.getResult().getMTS();
                    }
                    break;
                case RCXP:
                    if (cxpResult == null) {
                        cxpResult = cxpComputer.compute();
                        mts = cxpResult.getResult().getMTS();
                    }
                    if (rcxpResult == null) {
                        rcxpResult = new RCXPComputer(machine, cxpResult.getResult(), new ReducedVariantComputer(machine, rp)).compute();
                    }
                    break;
                case RCXPASO:
                    if (cxpasoResult == null) {
                        cxpasoResult = cxpasoComputer.compute();
                        mts = cxpasoResult.getResult().getMTS();
                    }
                    if (rcxpasoResult == null) {
                        rcxpasoResult = new RCXPComputer(machine, cxpasoResult.getResult(), new ReducedVariantComputer(machine, rp)).compute();
                    }
                    break;
                case FULL:
                    if (fullResult == null) {
                        fullResult = fullSemanticsComputer.compute();
                        mts = fullResult.getResult().getMTS();
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
                Files.write(new File(statsFolder, "rcxp.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "rcxp.stat").toPath(), ("Results for RCXP (in " + rcxpResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxp_small.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxp_full.dot").toPath(), rcxpResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (rcxpasoResult != null) {
                Statistics statistics = new Statistics(rcxpasoResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), rcxpasoResult.getTime());
                Files.write(new File(statsFolder, "rcxpaso.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "rcxpaso.stat").toPath(), ("Results for RCXPASO (in " + rcxpasoResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxpaso_small.dot").toPath(), rcxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(false, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(dotFolder, "rcxpaso_full.dot").toPath(), rcxpasoResult.getResult().getCTS().accept(new DOTEncoder<>(true, LR)).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
            if (fullResult != null) {
                System.out.println(fullResult.getTime());
                List<EStatistic> fullStatisticsFilter = new ArrayList<>(Arrays.asList(EStatistic.values()));
                fullStatisticsFilter.remove(TESTS);
                Statistics statistics = new Statistics(fullResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), fullResult.getTime(), fullStatisticsFilter.toArray(new EStatistic[0]));
                //Statistics statistics = new Statistics(fullResult.getResult(), abstractionPredicatesSet, ap, relevance.getRight(), fullResult.getTime());
                Files.write(new File(statsFolder, "full.row").toPath(), statistics.entrySet().stream().filter(entry -> !rowStatisticsFilter.contains(entry.getKey())).map(entry -> entry.getValue().toString()).collect(Collectors.joining(" ")).getBytes(), CREATE, TRUNCATE_EXISTING);
                Files.write(new File(statsFolder, "full.stat").toPath(), ("Results for FULL (in " + fullResult.getTime() + "):" + "\n" + "\n" + statistics.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n\n"))).getBytes(), CREATE, TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
