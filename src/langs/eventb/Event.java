package langs.eventb;

import langs.AObject;
import langs.eventb.substitutions.ASubstitution;
import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 01:00
 */
public final class Event extends AObject {

    private final String name;
    private final ASubstitution substitution;

    public Event(String name, ASubstitution substitution) {
        this.name = name;
        this.substitution = substitution;
    }

    public String getName() {
        return name;
    }

    public ASubstitution getSubstitution() {
        return substitution;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public Event clone() {
        return new Event(name, substitution.clone());
    }

}
