package langs.eventb.substitutions;

import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:48
 */
public final class Assignments extends ASubstitution {

    private final List<AAssignment> assignments;

    public Assignments(AAssignment... assignments) {
        this.assignments = Arrays.asList(assignments);
    }

    public List<AAssignment> getAssignments() {
        return assignments;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ASubstitution clone() {
        return new Assignments(assignments.stream().map(ASubstitution::clone).toArray(AAssignment[]::new));
    }

}
