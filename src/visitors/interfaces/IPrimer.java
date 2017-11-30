package visitors.interfaces;

import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Range;
import langs.maths.set.literals.Set;
import langs.maths.set.literals.Z;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;

/**
 * Created by gvoiron on 30/11/17.
 * Time : 14:34
 */
public interface IPrimer {

    Int visit(Int anInt);

    Const visit(Const aConst);

    Var visit(Var var);

    FunVar visit(FunVar funVar);

    Fun visit(Fun fun);

    Plus visit(Plus plus);

    UMinus visit(UMinus uMinus);

    Minus visit(Minus minus);

    Times visit(Times times);

    Div visit(Div div);

    Mod visit(Mod mod);

    False visit(False aFalse);

    True visit(True aTrue);

    Not visit(Not not);

    And visit(And and);

    Or visit(Or or);

    NotEquals visit(NotEquals notEquals);

    Equals visit(Equals equals);

    LT visit(LT lt);

    LEQ visit(LEQ leq);

    GEQ visit(GEQ geq);

    GT visit(GT gt);

    Implies visit(Implies implies);

    Equiv visit(Equiv equiv);

    Exists visit(Exists exists);

    ForAll visit(ForAll forAll);

    VarInDomain visit(VarInDomain varInDomain);

    InDomain visit(InDomain inDomain);

    Z visit(Z z);

    Set visit(Set set);

    Range visit(Range range);

    Intersection visit(Intersection intersection);

    Union visit(Union union);

    Difference visit(Difference difference);

}
