package algorithms.heuristics;

import langs.formal.graphs.AbstractState;
import utilities.tuples.Tuple;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by gvoiron on 15/12/17.
 * Time : 09:36
 */
public interface IAbstractStatesOrderingFunction extends Function<Tuple<Set<AbstractState>, AbstractState>, LinkedHashSet<AbstractState>> {
}
