package visitors;

import langs.eventb.Event;
import langs.eventb.Machine;
import langs.eventb.substitutions.*;
import langs.formal.graphs.ConcreteState;
import langs.formal.graphs.ConcreteTransition;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Enum;
import langs.maths.set.literals.*;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import visitors.interfaces.IObjectFormatter;

import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:12
 */
public final class ObjectFormatter extends AFormatter implements IObjectFormatter {

    @Override
    public String visit(Int anInt) {
        return anInt.getValue().toString();
    }

    @Override
    public String visit(Const aConst) {
        return aConst.getName();
    }

    @Override
    public String visit(EnumValue enumValue) {
        return enumValue.getName() + "[" + enumValue.getValue() + "]";
    }

    @Override
    public String visit(Var var) {
        return var.getName();
    }

    @Override
    public String visit(FunVar funVar) {
        return funVar.getName();
    }

    @Override
    public String visit(Fun fun) {
        return fun.getName() + "(" + fun.getParameter().accept(this) + ")";
    }

    @Override
    public String visit(Plus plus) {
        return "(" + plus.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" + ")) + ")";
    }

    @Override
    public String visit(UMinus uMinus) {
        return "-" + uMinus.getOperand().accept(this);
    }

    @Override
    public String visit(Minus minus) {
        return "(" + minus.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" - ")) + ")";
    }

    @Override
    public String visit(Times times) {
        return "(" + times.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" * ")) + ")";
    }

    @Override
    public String visit(Div div) {
        return "(" + div.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" / ")) + ")";
    }

    @Override
    public String visit(Mod mod) {
        return "(" + mod.getLeft().accept(this) + " % " + mod.getRight().accept(this) + ")";
    }

    @Override
    public String visit(False aFalse) {
        return "false";
    }

    @Override
    public String visit(True aTrue) {
        return "true";
    }

    @Override
    public String visit(Invariant invariant) {
        return invariant.getExpr().accept(this);
    }

    @Override
    public String visit(ConcreteState concreteState) {
        return concreteState.getName() + " = " + concreteState.getExpr().accept(this);
    }

    @Override
    public String visit(Not not) {
        return "¬(" + not.getOperand().accept(this) + ")";
    }

    @Override
    public String visit(And and) {
        return line("and(") + indentRight() + and.getOperands().stream().map(operand -> indentLine(operand.accept(this))).collect(Collectors.joining()) + indentLeft() + indent(")");
    }

    @Override
    public String visit(Or or) {
        return line("or(") + indentRight() + or.getOperands().stream().map(operand -> indentLine(operand.accept(this))).collect(Collectors.joining()) + indentLeft() + indent(")");
    }

    @Override
    public String visit(NotEquals notEquals) {
        return notEquals.getLeft().accept(this) + " ≠ " + notEquals.getRight().accept(this);
    }

    @Override
    public String visit(Equals equals) {
        return equals.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" = "));
    }

    @Override
    public String visit(LT lt) {
        return lt.getLeft().accept(this) + " < " + lt.getRight().accept(this);
    }

    @Override
    public String visit(LEQ leq) {
        return leq.getLeft().accept(this) + " <= " + leq.getRight().accept(this);
    }

    @Override
    public String visit(GEQ geq) {
        return geq.getLeft().accept(this) + " >= " + geq.getRight().accept(this);
    }

    @Override
    public String visit(GT gt) {
        return gt.getLeft().accept(this) + " > " + gt.getRight().accept(this);
    }

    @Override
    public String visit(Implies implies) {
        return implies.getLeft().accept(this) + " => " + implies.getRight().accept(this);
    }

    @Override
    public String visit(Equiv equiv) {
        return equiv.getLeft().accept(this) + " <=> " + equiv.getRight().accept(this);
    }

    @Override
    public String visit(Exists exists) {
        return "∃(" + exists.getQuantifiedVarsDefs().stream().map(varAInDomain -> varAInDomain.getLeft().getName()).collect(Collectors.joining(", ")) + ").(" + exists.getBody().accept(this) + ")";
    }

    @Override
    public String visit(ForAll forAll) {
        return "∀(" + forAll.getQuantifiedVarsDefs().stream().map(varAInDomain -> varAInDomain.getLeft().getName()).collect(Collectors.joining(", ")) + ").(" + forAll.getBody().accept(this) + ")";
    }

    @Override
    public String visit(VarInDomain varInDomain) {
        return varInDomain.getLeft().accept(this) + " ∈ " + varInDomain.getRight().accept(this);
    }

    @Override
    public String visit(InDomain inDomain) {
        return inDomain.getLeft().accept(this) + " ∈ " + inDomain.getRight().accept(this);
    }

    @Override
    public String visit(Z z) {
        return "ℤ";
    }

    @Override
    public String visit(Set set) {
        return "{" + set.getElements().stream().map(element -> element.accept(this)).collect(Collectors.joining(", ")) + "}";
    }

    @Override
    public String visit(Range range) {
        return "[" + range.getLowerBound().accept(this) + ".." + range.getUpperBound().accept(this) + "]";
    }

    @Override
    public String visit(Enum anEnum) {
        return "{" + anEnum.getEnumValues().stream().map(enumValue -> enumValue.accept(this)).collect(Collectors.joining(", ")) + "}";
    }

    @Override
    public String visit(NamedSet namedSet) {
        return namedSet.getName();
    }

    @Override
    public String visit(Intersection intersection) {
        return "∩(" + intersection.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String visit(Union union) {
        return "∪(" + union.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String visit(Difference difference) {
        return "(" + difference.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" - ")) + ")";
    }

    @Override
    public String visit(Skip skip) {
        return "SKIP";
    }

    @Override
    public String visit(Assignments assignments) {
        return assignments.getAssignments().stream().map(aAssignment -> aAssignment.accept(this)).collect(Collectors.joining(" ||" + line() + indent("")));
    }

    @Override
    public String visit(VarAssignment varAssignment) {
        return varAssignment.getAssignable().accept(this) + " := " + varAssignment.getValue().accept(this);
    }

    @Override
    public String visit(FunAssignment funAssignment) {
        return funAssignment.getAssignable().accept(this) + " := " + funAssignment.getValue().accept(this);
    }

    @Override
    public String visit(Select select) {
        return line("SELECT") + indentRight() + indentLine(select.getCondition().accept(this)) + indentLeft() + indentLine("THEN") + indentRight() + indentLine(select.getSubstitution().accept(this)) + indentLeft() + indent("END");
    }

    @Override
    public String visit(IfThenElse ifThenElse) {
        return line("IF") + indentRight() + indentLine(ifThenElse.getCondition().accept(this)) + indentLeft() + indentLine("THEN") + indentRight() + indentLine(ifThenElse.getThenPart().accept(this)) + indentLeft() + indentLine("ELSE") + indentRight() + indentLine(ifThenElse.getElsePart().accept(this)) + indentLeft() + indent("END");
    }

    @Override
    public String visit(Choice choice) {
        return line("CHOICE") + indentRight() + choice.getSubstitutions().stream().map(substitution -> indentLine(substitution.accept(this))).collect(Collectors.joining(indentLeft() + indentLine("OR") + indentRight())) + indentLeft() + indent("END");
    }

    @Override
    public String visit(Any any) {
        return line("ANY") + indentRight() + any.getQuantifiedVarsDefs().stream().map(varInDomain -> indentLine(varInDomain.accept(this))).collect(Collectors.joining()) + indentLeft() + indentLine("WHERE") + indentRight() + indentLine(any.getCondition().accept(this)) + indentLeft() + indentLine("THEN") + indentRight() + indentLine(any.getSubstitution().accept(this)) + indentLeft() + indent("END");
    }

    @Override
    public String visit(Event event) {
        return line(event.getName() + " = ") + indentRight() + indentLine(event.getSubstitution().accept(this)) + indentLeft();
    }

    @Override
    public String visit(Machine machine) {
        String formatted = line("MACHINE " + machine.getName());
        formatted += machine.getConstsDefs().isEmpty() ? "" : line() + indentRight() + indentLine("CONSTS") + indentRight() + machine.getConstsDefs().entrySet().stream().map(entry -> indentLine(new Const(entry.getKey()).accept(this) + " = " + entry.getValue().accept(this))).collect(Collectors.joining()) + indentLeft() + indentLeft();
        formatted += machine.getNamedSetsDefs().isEmpty() ? "" : line() + indentRight() + indentLine("SETS") + indentRight() + machine.getNamedSetsDefs().entrySet().stream().map(entry -> indentLine(entry.getKey() + " = " + entry.getValue().accept(this))).collect(Collectors.joining()) + indentLeft() + indentLeft();
        formatted += machine.getVarsDefs().isEmpty() ? "" : line() + indentRight() + indentLine("VARS") + indentRight() + machine.getVarsDefs().entrySet().stream().map(entry -> indentLine(new Var(entry.getKey()).accept(this) + " : " + entry.getValue().accept(this))).collect(Collectors.joining()) + indentLeft() + indentLeft();
        formatted += machine.getFunsDefs().isEmpty() ? "" : line() + indentRight() + indentLine("FUNS") + indentRight() + machine.getFunsDefs().entrySet().stream().map(entry -> indentLine(entry.getKey() + " : " + entry.getValue().getLeft().accept(this) + " -> " + entry.getValue().getRight().accept(this))).collect(Collectors.joining()) + indentLeft() + indentLeft();
        formatted += line() + indentRight() + indentLine("INVARIANT") + indentRight() + indentLine(machine.getInvariant().accept(this)) + indentLeft() + indentLeft();
        formatted += line() + indentRight() + indentLine("INITIALISATION") + indentRight() + indentLine(machine.getInitialisation().accept(this)) + indentLeft() + indentLeft();
        formatted += line() + indentRight() + indentLine("EVENTS") + indentRight() + machine.getEvents().entrySet().stream().map(entry -> indentLine(entry.getValue().accept(this))).collect(Collectors.joining()) + indentLeft() + indentLeft();
        return formatted;
    }

    @Override
    public String visit(ConcreteTransition concreteTransition) {
        return concreteTransition.getSource().accept(this) + " -[ " + concreteTransition.getEvent().getName() + " ]-> " + concreteTransition.getTarget().accept(this);
    }

}
