package algorithms.heuristics;

import langs.formal.graphs.AbstractState;
import utilities.Tuple;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gvoiron on 15/12/17.
 * Time : 09:42
 */
public final class DefaultAbstractStatesOrderingFunction implements IAbstractStatesOrderingFunction {

    @Override
    public LinkedHashSet<AbstractState> apply(Tuple<Set<AbstractState>, AbstractState> tuple) {
        return Stream.of(Collections.singletonList(tuple.getRight()), tuple.getLeft()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
