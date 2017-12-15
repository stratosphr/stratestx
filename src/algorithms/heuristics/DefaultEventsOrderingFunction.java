package algorithms.heuristics;

import langs.eventb.Event;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 15/12/17.
 * Time : 09:42
 */
public final class DefaultEventsOrderingFunction implements IEventsOrderingFunction {

    @Override
    public LinkedHashSet<Event> apply(Set<Event> events) {
        return events.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
