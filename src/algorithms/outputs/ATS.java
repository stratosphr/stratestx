package algorithms.outputs;

import langs.eventb.Machine;
import langs.formal.graphs.CTS;
import langs.formal.graphs.MTS;
import utilities.ICloneable;

/**
 * Created by gvoiron on 09/12/17.
 * Time : 21:32
 */
public final class ATS implements ICloneable<ATS> {

    private final Machine machine;
    private final MTS mts;
    private final CTS cts;

    public ATS(Machine machine, MTS mts, CTS cts) {
        this.machine = machine;
        this.mts = mts;
        this.cts = cts;
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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ATS clone() {
        return new ATS(machine.clone(), mts.clone(), cts.clone());
    }

}
