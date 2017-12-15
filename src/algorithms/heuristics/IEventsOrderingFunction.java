package algorithms.heuristics;

import langs.eventb.Event;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by gvoiron on 15/12/17.
 * Time : 09:26
 */
public interface IEventsOrderingFunction extends Function<Set<Event>, LinkedHashSet<Event>> {
}
