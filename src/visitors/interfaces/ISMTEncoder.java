package visitors.interfaces;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.literals.Const;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:46
 */
public interface ISMTEncoder {

    ArithExpr visit(Const aConst);

    ArithExpr visit(Int anInt);

    ArithExpr visit(Var var);

    ArithExpr visit(Fun fun);

    ArithExpr visit(Plus plus);

    ArithExpr visit(UMinus uMinus);

    ArithExpr visit(Minus minus);

    ArithExpr visit(Times times);

    ArithExpr visit(Div div);

    ArithExpr visit(Mod mod);

    BoolExpr visit(False aFalse);

    BoolExpr visit(True aTrue);

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
