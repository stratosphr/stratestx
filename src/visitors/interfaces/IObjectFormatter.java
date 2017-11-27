package visitors.interfaces;

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
import langs.maths.set.literals.Z;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:14
 */
public interface IObjectFormatter {

    String visit(Int anInt);

    String visit(Const aConst);

    String visit(Var var);

    String visit(Fun fun);

    String visit(Plus plus);

    String visit(UMinus uMinus);

    String visit(Minus minus);

    String visit(Times times);

    String visit(Div div);

    String visit(Mod mod);

    String visit(False aFalse);

    String visit(True aTrue);

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

    String visit(Z z);

    String visit(Set set);

    String visit(Range range);

    String visit(Union union);

    String visit(Intersection intersection);

    String visit(Difference difference);

}
