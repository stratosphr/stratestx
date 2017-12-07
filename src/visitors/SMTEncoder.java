package visitors;

import com.microsoft.z3.*;
import langs.formal.graphs.AbstractState;
import langs.formal.graphs.ConcreteState;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.Predicate;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.literals.Z;
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
    private final DefsRegister defsRegister;
    private final HashMap<String, IntExpr> constsDecls;
    private final HashMap<String, IntExpr> varsDecls;
    private final HashMap<String, FuncDecl> funsDecls;
    private List<Var> quantifiedVars;

    public SMTEncoder(Context context, Solver solver, DefsRegister defsRegister) {
        this.context = context;
        this.solver = solver;
        this.defsRegister = defsRegister;
        this.constsDecls = new HashMap<>();
        this.varsDecls = new HashMap<>();
        this.funsDecls = new HashMap<>();
        this.quantifiedVars = new ArrayList<>();
    }

    @Override
    public IntExpr visit(Int anInt) {
        return context.mkInt(anInt.getValue());
    }

    @Override
    public IntExpr visit(Const aConst) {
        if (!defsRegister.getConstsDefs().containsKey(aConst.getName())) {
            throw new Error("Error: Constant \"" + aConst.getName() + "\" was not declared in this scope.");
        } else if (!constsDecls.containsKey(aConst.getName())) {
            constsDecls.put(aConst.getName(), context.mkIntConst(aConst.getName()));
            solver.add(new Equals(aConst, defsRegister.getConstsDefs().get(aConst.getName())).accept(this));
        }
        return constsDecls.get(aConst.getName());
    }

    @Override
    public IntExpr visit(EnumValue enumValue) {
        return new Int(enumValue.getValue()).accept(this);
    }

    @Override
    public IntExpr visit(Var var) {
        Var nonPrimedVar = var.accept(new Primer(0));
        if (quantifiedVars.contains(var)) {
            return (IntExpr) context.mkBound(quantifiedVars.size() - quantifiedVars.indexOf(var) - 1, context.getIntSort());
        } else if (!defsRegister.getVarsDefs().containsKey(nonPrimedVar.getName())) {
            throw new Error("Error: Variable \"" + var.getName() + "\" was not declared in this scope.");
        } else if (!varsDecls.containsKey(var.getName())) {
            varsDecls.put(var.getName(), context.mkIntConst(var.getName()));
            solver.add(new InDomain(var, defsRegister.getVarsDefs().get(nonPrimedVar.getName())).accept(this));
        }
        return varsDecls.get(var.getName());
    }

    @Override
    public IntExpr visit(FunVar funVar) {
        FunVar nonPrimedFunVar = funVar.accept(new Primer(0));
        if (!defsRegister.getFunsDefs().containsKey(nonPrimedFunVar.getFun().getName())) {
            throw new Error("Error: Function \"" + nonPrimedFunVar.getFun() + "\" was not declared in this scope.");
        } else if (!varsDecls.containsKey(funVar.getName())) {
            varsDecls.put(funVar.getName(), context.mkIntConst(funVar.getName()));
            solver.add(new InDomain(funVar, defsRegister.getFunsDefs().get(nonPrimedFunVar.getFun().getName()).getRight()).accept(this));
        }
        return varsDecls.get(funVar.getName());
    }

    @Override
    public IntExpr visit(Fun fun) {
        Fun nonPrimedFun = fun.accept(new Primer(0));
        if (!defsRegister.getFunsDefs().containsKey(nonPrimedFun.getName())) {
            throw new Error("Error: Function \"" + nonPrimedFun.getName() + "\" was not declared in this scope.");
        } else if (!funsDecls.containsKey(fun.getName())) {
            funsDecls.put(fun.getName(), context.mkFuncDecl(fun.getName(), context.getIntSort(), context.getIntSort()));
            solver.add(new And(
                    defsRegister.getFunsDefs().get(nonPrimedFun.getName()).getLeft().getElementsValues(defsRegister).stream().map(value ->
                            new Equals(new Fun(fun.getName(), value), new FunVar(new Fun(fun.getName(), value)))
                    ).toArray(ABoolExpr[]::new)
            ).accept(this));
            Var index = new Var("i!");
            ForAll forAll = new ForAll(
                    new And(
                            new Implies(
                                    new Not(new InDomain(index, defsRegister.getFunsDefs().get(nonPrimedFun.getName()).getLeft())),
                                    new Not(new InDomain(new Fun(fun.getName(), index), defsRegister.getFunsDefs().get(nonPrimedFun.getName()).getRight()))
                            )
                    ),
                    new VarInDomain(index, new Z())
            );
            solver.add(forAll.accept(this));
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
    public BoolExpr visit(Invariant invariant) {
        return invariant.getExpr().accept(this);
    }

    @Override
    public BoolExpr visit(Predicate predicate) {
        return predicate.getExpr().accept(this);
    }

    @Override
    public BoolExpr visit(ConcreteState concreteState) {
        return concreteState.getExpr().accept(this);
    }

    @Override
    public BoolExpr visit(AbstractState abstractState) {
        return abstractState.getExpr().accept(this);
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

    // TODO: Should additional constraints be added if "fun(expr)" is used in Exists and "expr" is not in "fun" domain?
    @Override
    public BoolExpr visit(Exists exists) {
        for (Fun fun : exists.getFuns()) {
            if (!defsRegister.getFunsDefs().containsKey(fun.accept(new Primer(0)).getName())) {
                throw new Error("Error: Function \"" + fun.getName() + "\" was not declared in this scope.");
            }
        }
        exists.getQuantifiedVarsDefs().forEach(varAInDomain -> quantifiedVars.add(varAInDomain.getLeft()));
        Sort[] sorts = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = exists.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        Quantifier quantifier = context.mkExists(
                sorts,
                symbols,
                /*new And(
                        new And(exists.getFuns().stream().map(fun -> new InDomain(fun.getParameter(), defsRegister.getFunsDefs().get(fun.getName()).getLeft())).toArray(InDomain[]::new)),
                        exists.getBody()
                ).accept(this),*/
                exists.getBody().accept(this),
                0, null, null, null, null
        );
        quantifiedVars = quantifiedVars.subList(0, quantifiedVars.size() - exists.getQuantifiedVarsDefs().size());
        return quantifier;
    }

    // TODO: Should additional constraints be added if "fun(expr)" is used in ForAll and "expr" is not in "fun" domain?
    @Override
    public BoolExpr visit(ForAll forAll) {
        for (Fun fun : forAll.getFuns()) {
            Fun nonPrimedFun = fun.accept(new Primer(0));
            if (!defsRegister.getFunsDefs().containsKey(nonPrimedFun.getName())) {
                throw new Error("Error: Function \"" + fun.getName() + "\" was not declared in this scope.");
            }
        }
        forAll.getQuantifiedVarsDefs().forEach(varAInDomain -> quantifiedVars.add(varAInDomain.getLeft()));
        Sort[] sorts = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.getIntSort()).toArray(Sort[]::new);
        Symbol[] symbols = forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> context.mkSymbol(varInDomain.getLeft().getName())).toArray(Symbol[]::new);
        Quantifier quantifier = context.mkForall(
                sorts,
                symbols,
                forAll.getBody().accept(this),
                0, null, null, null, null
        );
        quantifiedVars = quantifiedVars.subList(0, quantifiedVars.size() - forAll.getQuantifiedVarsDefs().size());
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
