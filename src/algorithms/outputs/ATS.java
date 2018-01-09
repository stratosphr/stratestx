package algorithms.outputs;

import algorithms.heuristics.EColor;
import langs.eventb.Machine;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.CTS;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.MTS;
import utilities.ICloneable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 09/12/17.
 * Time : 21:32
 */
public final class ATS implements ICloneable<ATS> {

    private final Machine machine;
    private final MTS mts;
    private final CTS cts;
    private final LinkedHashMap<ConcreteState, AbstractState> alpha;
    private final LinkedHashMap<ConcreteState, EColor> kappa;

    public ATS(Machine machine, MTS mts, CTS cts, LinkedHashMap<ConcreteState, AbstractState> alpha, LinkedHashMap<ConcreteState, EColor> kappa) {
        this.machine = machine;
        this.mts = mts;
        this.cts = cts;
        this.alpha = alpha;
        this.kappa = kappa;
    }

    public Machine getMachine() {
        return machine;
    }

    public CTS getCTS() {
        return cts;
    }

    public MTS getMTS() {
        return mts;
    }

    public LinkedHashMap<ConcreteState, AbstractState> getAlpha() {
        return alpha;
    }

    public LinkedHashMap<ConcreteState, EColor> getKappa() {
        return kappa;
    }


    @Override
    public ATS cloned() {
        return new ATS(machine.cloned(), mts.cloned(), cts.cloned(), alpha.entrySet().stream().collect(Collectors.toMap(o -> o.getKey().cloned(), o -> o.getValue().cloned(), (o1, o2) -> o1.cloned(), LinkedHashMap::new)), kappa.entrySet().stream().collect(Collectors.toMap(o -> o.getKey().cloned(), Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new)));
    }

}
