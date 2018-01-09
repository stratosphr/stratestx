package visitors.interfaces;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.IntExpr;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:46
 */

public interface ISMTEncoder {

    IntExpr visit(Int anInt);

    IntExpr visit(Const aConst);

    IntExpr visit(EnumValue enumValue);

    IntExpr visit(Var var);

    IntExpr visit(FunVar funVar);

    IntExpr visit(Fun fun);

    IntExpr visit(Plus plus);

    IntExpr visit(UMinus uMinus);

    IntExpr visit(Minus minus);

    IntExpr visit(Times times);

    IntExpr visit(Div div);

    IntExpr visit(Mod mod);

    BoolExpr visit(False aFalse);

    BoolExpr visit(True aTrue);

    BoolExpr visit(Invariant invariant);

    BoolExpr visit(Predicate predicate);

    BoolExpr visit(ConcreteState concreteState);

    BoolExpr visit(AbstractState abstractState);

    BoolExpr visit(Not not);

    BoolExpr visit(And and);

    BoolExpr visit(Or or);

    BoolExpr visit(NotEquals notEquals);

    BoolExpr visit(Equals equals);

    BoolExpr visit(LT lt);

    BoolExpr visit(LEQ leq);

    BoolExpr visit(GEQ geq);

    BoolExpr visit(GT gt);

    BoolExpr visit(Implies implies);

    BoolExpr visit(Equiv equiv);

    BoolExpr visit(Exists exists);

    BoolExpr visit(ForAll forAll);

    BoolExpr visit(VarInDomain varInDomain);

    BoolExpr visit(InDomain inDomain);

}
