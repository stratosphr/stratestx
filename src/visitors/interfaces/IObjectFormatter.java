package visitors.interfaces;

import langs.eventb.Event;
import langs.eventb.Machine;
import langs.eventb.substitutions.*;
import langs.formal.graphs.*;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Enum;
import langs.maths.set.literals.*;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import visitors.dot.DOTEdge;
import visitors.dot.DOTNode;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:14
 */
public interface IObjectFormatter {

    String visit(Int anInt);

    String visit(Const aConst);

    String visit(EnumValue enumValue);

    String visit(Var var);

    String visit(FunVar funVar);

    String visit(Fun fun);

    String visit(Plus plus);

    String visit(UMinus uMinus);

    String visit(Minus minus);

    String visit(Times times);

    String visit(Div div);

    String visit(Mod mod);

    @SuppressWarnings("SameReturnValue")
    String visit(False aFalse);

    @SuppressWarnings("SameReturnValue")
    String visit(True aTrue);

    String visit(Invariant invariant);

    String visit(Predicate predicate);

    String visit(AbstractState abstractState);

    String visit(ConcreteState concreteState);

    String visit(Not not);

    String visit(And and);

    String visit(Or or);

    String visit(NotEquals notEquals);

    String visit(Equals equals);

    String visit(LT lt);

    String visit(LEQ leq);

    String visit(GEQ geq);

    String visit(GT gt);

    String visit(Implies implies);

    String visit(Equiv equiv);

    String visit(Exists exists);

    String visit(ForAll forAll);

    String visit(VarInDomain varInDomain);

    String visit(InDomain inDomain);

    @SuppressWarnings("SameReturnValue")
    String visit(Z z);

    String visit(Set set);

    String visit(Range range);

    String visit(Enum anEnum);

    String visit(NamedSet namedSet);

    String visit(Intersection intersection);

    String visit(Union union);

    String visit(Difference difference);

    @SuppressWarnings("SameReturnValue")
    String visit(Skip skip);

    String visit(Assignments assignments);

    String visit(VarAssignment varAssignment);

    String visit(FunAssignment funAssignment);

    String visit(Select select);

    String visit(IfThenElse ifThenElse);

    String visit(Choice choice);

    String visit(Any any);

    String visit(Event event);

    String visit(Machine machine);

    String visit(AbstractTransition abstractTransition);

    String visit(ConcreteTransition concreteTransition);

    String visit(MTS mts);

    String visit(CTS CTS);

    String visit(DOTNode dotNode);

    String visit(DOTEdge dotEdge);

}
