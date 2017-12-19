package algorithms.outputs;

import langs.AObject;
import langs.formal.graphs.ConcreteTransition;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 20:17
 */
public final class Test extends ArrayList<ConcreteTransition> {

    @Override
    public String toString() {
        return stream().map(AObject::toString).collect(Collectors.joining("\n")) + "\n############################################";
    }

}
