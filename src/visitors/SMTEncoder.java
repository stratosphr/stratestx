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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:57
 */
public final class SMTEncoder implements ISMTEncoder {

    private final Context context;
    private final Solver solver;
    private List<Var> quantifiedVars;
    private HashMap<String, FuncDecl> funsDecls;

    public SMTEncoder(Context context, Solver solver) {
        this.context = context;
        this.solver = solver;
        this.quantifiedVars = new ArrayList<>();
        this.funsDecls = new HashMap<>();
    }

    @Override
    public IntExpr visit(Int anInt) {
        return context.mkInt(anInt.getValue());
    }

    @Override
    public IntExpr visit(Const aConst) {
        return context.mkIntConst(aConst.getName());
    }

    @Override
    public IntExpr visit(Var var) {
        if (quantifiedVars.contains(var)) {
            return (IntExpr) context.mkBound(quantifiedVars.size() - quantifiedVars.indexOf(var) - 1, context.getIntSort());
        }
        return context.mkIntConst(var.getName());
    }

    @Override
    public IntExpr visit(Fun fun) {
        if (!funsDecls.containsKey(fun.getName())) {
            funsDecls.put(fun.getName(), context.mkFuncDecl(fun.getName(), context.getIntSort(), context.getIntSort()));
        }
        return (IntExpr) funsDecls.get(fun.getName()).apply(fun.getParameter().accept(this));
    }

    @Override
    public IntExpr visit(Plus plus) {
        return (IntExpr) context.mkAdd(plus.getOperands().stream().map(operand -> operand.accept(this)).toArray(ArithExpr[]::new));
    }

    @Override
    public IntExpr visit(UMinus uMinus) {
        return (IntExpr) context.mkUnaryMinus(uMinus.getOperand().accept(this));
    }

    @Override
    public IntExpr visit(Minus minus) {
        return (IntExpr) context.mkSub(minus.getOperands().stream().map(operand -> operand.accept(this)).toArray(ArithExpr[]::new));
    }

    @Override
    public IntExpr visit(Times times) {
        return (IntExpr) context.mkMul(times.getOperands().stream().map(operand -> operand.accept(this)).toArray(ArithExpr[]::new));
    }

    @Override
    public IntExpr visit(Div div) {
        return div.getOperands().subList(2, div.getOperands().size()).stream().reduce((IntExpr) context.mkDiv(div.getOperands().get(0).accept(this), div.getOperands().get(1).accept(this)), (left, expr) -> (IntExpr) context.mkDiv(left, expr.accept(this)), (left, right) -> left);
    }

    @Override
    public IntExpr visit(Mod mod) {
        return context.mkMod(mod.getLeft().accept(this), mod.getRight().accept(this));
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
        return equals.getOperands().size() == 2 ? context.mkEq(equals.getOperands().get(0).accept(this), equals.getOperands().get(1).accept(this)) : context.mkAnd(equals.getOperands().subList(1, equals.getOperands().size()).stream().map(aArithExpr -> context.mkEq(equals.getOperands().get(0).accept(this), aArithExpr.accept(this))).toArray(BoolExpr[]::new));
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
        exists.getQuantifiedVarsDefs().forEach(varAInDomain -> quantifiedVars.add(varAInDomain.getLeft()));
        System.out.println(quantifiedVars);
        Sort[] sorts = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        Quantifier quantifier = context.mkExists(
                sorts,
                symbols,
                exists.getBody().accept(this),
                0, null, null, null, null
        );
        quantifiedVars = quantifiedVars.subList(0, quantifiedVars.size() - exists.getQuantifiedVarsDefs().size());
        System.out.println("#" + quantifiedVars);
        return quantifier;
    }

    @Override
    public BoolExpr visit(ForAll forAll) {
        forAll.getQuantifiedVarsDefs().forEach(varAInDomain -> quantifiedVars.add(varAInDomain.getLeft()));
        System.out.println(quantifiedVars);
        Sort[] sorts = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        Quantifier quantifier = context.mkForall(
                sorts,
                symbols,
                forAll.getBody().accept(this),
                0, null, null, null, null
        );
        quantifiedVars = quantifiedVars.subList(0, quantifiedVars.size() - forAll.getQuantifiedVarsDefs().size());
        System.out.println("#" + quantifiedVars);
        return quantifier;
    }

    @Override
    public BoolExpr visit(VarInDomain varInDomain) {
        return varInDomain.getRight().getConstraint(varInDomain.getLeft()).accept(this);
    }

    @Override
    public BoolExpr visit(InDomain inDomain) {
        return inDomain.getRight().getConstraint(inDomain.getLeft()).accept(this);
    }

}
