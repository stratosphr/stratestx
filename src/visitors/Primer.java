package visitors;

import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.literals.Enum;
import langs.maths.set.literals.*;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import visitors.interfaces.IPrimer;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 30/11/17.
 * Time : 14:34
 */
public final class Primer implements IPrimer {

    private final static String suffix = "_";
    private int primeLevel;
    private LinkedHashSet<Var> quantifiedVars;
    private boolean isVisitingInvariant;

    public Primer(int primeLevel) {
        this.primeLevel = primeLevel;
        this.quantifiedVars = new LinkedHashSet<>();
        this.isVisitingInvariant = false;
    }

    private String prime(String name) {
        return name.replaceAll(suffix + "*$", "") + String.join("", Collections.nCopies(primeLevel, suffix));
    }

    @Override
    public Int visit(Int anInt) {
        return new Int(anInt.getValue());
    }

    @Override
    public Const visit(Const aConst) {
        return new Const(aConst.getName());
    }

    @Override
    public EnumValue visit(EnumValue enumValue) {
        return new EnumValue(enumValue.getName());
    }

    @Override
    public Var visit(Var var) {
        if (!quantifiedVars.contains(var)) {
            return new Var(prime(var.getName()));
        } else {
            return var;
        }
    }

    @Override
    public FunVar visit(FunVar funVar) {
        return new FunVar(funVar.getFun().accept(this));
    }

    @Override
    public Fun visit(Fun fun) {
        if (isVisitingInvariant) {
            return new Fun(prime(fun.getName()), fun.getParameter().accept(this));
        } else {
            return new Fun(prime(fun.getName()), fun.getParameter());
        }
    }

    @Override
    public Plus visit(Plus plus) {
        return new Plus(plus.getOperands().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public UMinus visit(UMinus uMinus) {
        return new UMinus(uMinus.getOperand().accept(this));
    }

    @Override
    public Minus visit(Minus minus) {
        return new Minus(minus.getOperands().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public Times visit(Times times) {
        return new Times(times.getOperands().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public Div visit(Div div) {
        return new Div(div.getOperands().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public Mod visit(Mod mod) {
        return new Mod(mod.getLeft().accept(this), mod.getRight().accept(this));
    }

    @Override
    public False visit(False aFalse) {
        return new False();
    }

    @Override
    public True visit(True aTrue) {
        return new True();
    }

    @Override
    public Invariant visit(Invariant invariant) {
        isVisitingInvariant = true;
        Invariant primed = new Invariant(invariant.getExpr().accept(this));
        isVisitingInvariant = false;
        return primed;
    }

    @Override
    public ConcreteState visit(ConcreteState concreteState) {
        return new ConcreteState(concreteState.getName(), concreteState.getMapping().entrySet().stream().collect(Collectors.toMap(o -> o.getKey().accept(this), o -> o.getValue().accept(this), (value1, value2) -> value1, TreeMap::new)));
    }

    @Override
    public Not visit(Not not) {
        return new Not(not.getOperand().accept(this));
    }

    @Override
    public And visit(And and) {
        return new And(and.getOperands().stream().map(operand -> operand.accept(this)).toArray(ABoolExpr[]::new));
    }

    @Override
    public Or visit(Or or) {
        return new Or(or.getOperands().stream().map(operand -> operand.accept(this)).toArray(ABoolExpr[]::new));
    }

    @Override
    public NotEquals visit(NotEquals notEquals) {
        return new NotEquals(notEquals.getLeft().accept(this), notEquals.getRight().accept(this));
    }

    @Override
    public Equals visit(Equals equals) {
        return new Equals(equals.getOperands().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public LT visit(LT lt) {
        return new LT(lt.getLeft().accept(this), lt.getRight().accept(this));
    }

    @Override
    public LEQ visit(LEQ leq) {
        return new LEQ(leq.getLeft().accept(this), leq.getRight().accept(this));
    }

    @Override
    public GEQ visit(GEQ geq) {
        return new GEQ(geq.getLeft().accept(this), geq.getRight().accept(this));
    }

    @Override
    public GT visit(GT gt) {
        return new GT(gt.getLeft().accept(this), gt.getRight().accept(this));
    }

    @Override
    public Implies visit(Implies implies) {
        return new Implies(implies.getLeft().accept(this), implies.getRight().accept(this));
    }

    @Override
    public Equiv visit(Equiv equiv) {
        return new Equiv(equiv.getLeft().accept(this), equiv.getRight().accept(this));
    }

    @Override
    public Exists visit(Exists exists) {
        LinkedHashSet<Var> oldQuantifiedVars = new LinkedHashSet<>(quantifiedVars);
        exists.getQuantifiedVarsDefs().forEach(varInDomain -> quantifiedVars.add(varInDomain.getLeft()));
        Exists quantifier = new Exists(
                exists.getBody().getOperands().get(1).accept(this),
                exists.getQuantifiedVarsDefs().stream().map(varInDomain -> varInDomain.accept(this)).toArray(VarInDomain[]::new)
        );
        quantifiedVars = oldQuantifiedVars;
        return quantifier;
    }

    @Override
    public ForAll visit(ForAll forAll) {
        LinkedHashSet<Var> oldQuantifiedVars = new LinkedHashSet<>(quantifiedVars);
        forAll.getQuantifiedVarsDefs().forEach(varInDomain -> quantifiedVars.add(varInDomain.getLeft()));
        ForAll quantifier = new ForAll(
                forAll.getBody().getRight().accept(this),
                forAll.getQuantifiedVarsDefs().stream().map(varInDomain -> varInDomain.accept(this)).toArray(VarInDomain[]::new)
        );
        quantifiedVars = oldQuantifiedVars;
        return quantifier;
    }

    @Override
    public VarInDomain visit(VarInDomain varInDomain) {
        return new VarInDomain(varInDomain.getLeft().accept(this), varInDomain.getRight().accept(this));
    }

    @Override
    public InDomain visit(InDomain inDomain) {
        return new InDomain(inDomain.getLeft().accept(this), inDomain.getRight().accept(this));
    }

    @Override
    public Z visit(Z z) {
        return new Z();
    }

    @Override
    public Set visit(Set set) {
        return new Set(set.getElements().stream().map(operand -> operand.accept(this)).toArray(AArithExpr[]::new));
    }

    @Override
    public Range visit(Range range) {
        return new Range(range.getLowerBound().accept(this), range.getUpperBound().accept(this));
    }

    @Override
    public Enum visit(Enum anEnum) {
        return new Enum(anEnum.getEnumValues().stream().map(enumValue -> enumValue.accept(this)).toArray(EnumValue[]::new));
    }

    @Override
    public NamedSet visit(NamedSet namedSet) {
        return new NamedSet(namedSet.getName(), namedSet.getSet().accept(this));
    }

    @Override
    public Intersection visit(Intersection intersection) {
        return new Intersection(intersection.getOperands().stream().map(operand -> operand.accept(this)).toArray(AFiniteSetExpr[]::new));
    }

    @Override
    public Union visit(Union union) {
        return new Union(union.getOperands().stream().map(operand -> operand.accept(this)).toArray(AFiniteSetExpr[]::new));
    }

    @Override
    public Difference visit(Difference difference) {
        return new Difference(difference.getOperands().stream().map(operand -> operand.accept(this)).toArray(AFiniteSetExpr[]::new));
    }

}
