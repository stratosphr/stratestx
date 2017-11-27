package visitors;

import langs.maths.generic.arith.literals.Const;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Range;
import langs.maths.set.literals.Set;
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
    public String visit(Var var) {
        return var.getName();
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
    public String visit(Set set) {
        return "{" + set.getElements().stream().map(aArithExpr -> aArithExpr.accept(this)).collect(Collectors.joining(", ")) + "}";
    }

    @Override
    public String visit(Range range) {
        return "[" + range.getLowerBound().accept(this) + ".." + range.getUpperBound().accept(this) + "]";
    }

    @Override
    public String visit(Union union) {
        return "∪(" + union.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String visit(Intersection intersection) {
        return "∩(" + intersection.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public String visit(Difference difference) {
        return "(" + difference.getOperands().stream().map(operand -> operand.accept(this)).collect(Collectors.joining(" - ")) + ")";
    }

}
