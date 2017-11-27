package visitors;

import com.microsoft.z3.*;
import langs.maths.generic.arith.literals.Const;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import visitors.interfaces.ISMTEncoder;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:57
 */
public final class SMTEncoder implements ISMTEncoder {

    private final Context context;
    private final Solver solver;

    public SMTEncoder(Context context, Solver solver) {
        this.context = context;
        this.solver = solver;
    }

    @Override
    public ArithExpr visit(Const aConst) {
        return context.mkIntConst(aConst.getName());
    }

    @Override
    public ArithExpr visit(Int anInt) {
        return context.mkInt(anInt.getValue());
    }

    // TODO : Take care of quantified vars
    @Override
    public ArithExpr visit(Var var) {
        return context.mkIntConst(var.getName());
    }

    // TODO : Return FunDecl application
    @Override
    public ArithExpr visit(Fun fun) {
        return context.mkIntConst(fun.getName());
    }

    @Override
    public ArithExpr visit(Plus plus) {
        throw new Error();
    }

    @Override
    public ArithExpr visit(UMinus uMinus) {
        return context.mkSub(uMinus.getOperand().accept(this));
    }

    @Override
    public ArithExpr visit(Minus minus) {
        return context.mkSub(minus.getOperands().stream().map(operand -> operand.accept(this)).toArray(ArithExpr[]::new));
    }

    @Override
    public ArithExpr visit(Times times) {
        return context.mkMul(times.getOperands().stream().map(operand -> operand.accept(this)).toArray(ArithExpr[]::new));
    }

    // TODO : Return correct division
    @Override
    public ArithExpr visit(Div div) {
        return context.mkInt(0);
    }

    @Override
    public ArithExpr visit(Mod mod) {
        throw new Error();
    }

    @Override
    public BoolExpr visit(False aFalse) {
        return context.mkFalse();
    }

    @Override
    public BoolExpr visit(True aTrue) {
        return context.mkTrue();
    }

    @Override
    public BoolExpr visit(Not not) {
        return context.mkNot(not.getOperand().accept(this));
    }

    @Override
    public BoolExpr visit(And and) {
        return context.mkAnd(and.getOperands().stream().map(operand -> operand.accept(this)).toArray(BoolExpr[]::new));
    }

    @Override
    public BoolExpr visit(Or or) {
        return context.mkOr(or.getOperands().stream().map(operand -> operand.accept(this)).toArray(BoolExpr[]::new));
    }

    @Override
    public BoolExpr visit(NotEquals notEquals) {
        return new Not(new Equals(notEquals.getLeft(), notEquals.getRight())).accept(this);
    }

    @Override
    public BoolExpr visit(Equals equals) {
        return context.mkAnd(equals.getOperands().subList(1, equals.getOperands().size()).stream().map(aArithExpr -> context.mkEq(equals.getOperands().get(0).accept(this), aArithExpr.accept(this))).toArray(BoolExpr[]::new));
    }

    @Override
    public BoolExpr visit(LT lt) {
        return context.mkLt(lt.getLeft().accept(this), lt.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(LEQ leq) {
        return context.mkLe(leq.getLeft().accept(this), leq.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(GEQ geq) {
        return context.mkGe(geq.getLeft().accept(this), geq.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(GT gt) {
        return context.mkGt(gt.getLeft().accept(this), gt.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(Implies implies) {
        return context.mkImplies(implies.getLeft().accept(this), implies.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(Equiv equiv) {
        return context.mkIff(equiv.getLeft().accept(this), equiv.getRight().accept(this));
    }

    @Override
    public BoolExpr visit(Exists exists) {
        Sort[] sorts = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        return context.mkExists(
                sorts,
                symbols,
                exists.getBody().accept(this),
                0, null, null, null, null
        );
    }

    @Override
    public BoolExpr visit(ForAll forAll) {
        Sort[] sorts = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        return context.mkForall(
                sorts,
                symbols,
                forAll.getBody().accept(this),
                0, null, null, null, null
        );
    }

    // TODO : Return domain constraint
    @Override
    public BoolExpr visit(VarInDomain varInDomain) {
        return context.mkTrue();
    }

    // TODO : Return domain constraint
    @Override
    public BoolExpr visit(InDomain inDomain) {
        return context.mkTrue();
    }

}
