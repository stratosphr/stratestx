package algorithms.statistics;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 18/12/17.
 * Time : 20:21
 */
public final class SetStatistic<Element> extends AStatistic<Set<Element>> {

    SetStatistic(Set<Element> elements) {
        super(elements);
    }

    @Override
    public String toString() {
        return "\n" + value.stream().map(Object::toString).collect(Collectors.joining("\n")) + "\n";
    }

}
