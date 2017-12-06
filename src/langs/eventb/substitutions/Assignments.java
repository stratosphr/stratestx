package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Z;
import visitors.Primer;
import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new And(Stream.of(
                assignments.stream().filter(assignment -> assignables.stream().anyMatch(assignable -> assignable.getName().equals(assignment.getAssignable().getName()))).map(AAssignment::getPrdInAssignments).collect(Collectors.toList()),
                assignables.stream().filter(assignable -> assignable instanceof Var && assignments.stream().noneMatch(assignment -> assignable.getName().equals(assignment.getAssignable().getName()))).map(assignable -> new Equals(assignable.accept(new Primer(1)), assignable)).collect(Collectors.toList()),
                assignables.stream().filter(assignable -> assignable instanceof Fun).collect(Collectors.groupingBy(AAssignable::getName)).keySet().stream().map(funName -> new ForAll(new Implies(new And(assignments.stream().filter(assignment -> assignment instanceof FunAssignment && assignment.getAssignable().getName().equals(funName)).map(assignment -> new NotEquals(new Var("i!"), ((FunAssignment) assignment).getAssignable().getParameter())).toArray(ABoolExpr[]::new)), new Equals(new Fun(funName, new Var("i!")).accept(new Primer(1)), new Fun(funName, new Var("i!")))), new VarInDomain(new Var("i!"), new Z()))).collect(Collectors.toList())
        ).flatMap(Collection::stream).toArray(ABoolExpr[]::new));
    }

    @Override
    public Assignments clone() {
        return new Assignments(assignments.stream().map(ASubstitution::clone).toArray(AAssignment[]::new));
    }

}
