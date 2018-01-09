package algorithms.heuristics.relevance;

import algorithms.heuristics.relevance.atomics.AAtomicRelevancePredicate;
import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.arith.operators.Plus;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Not;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.literals.Z;
import solvers.z3.Z3;
import solvers.z3.Z3Result;
import visitors.Primer;

import java.util.*;

/**
 * Created by gvoiron on 19/12/17.
 * Time : 22:09
 */
public final class RelevancePredicate extends ARelevancePredicate {

    private final LinkedHashSet<AAtomicRelevancePredicate> atomicPredicates;

    public RelevancePredicate(AAtomicRelevancePredicate... atomicPredicates) {
        super(new Or(atomicPredicates));
        this.atomicPredicates = new LinkedHashSet<>(Arrays.asList(atomicPredicates));
    }

    public final AArithExpr getVInit(AVariantComputer variantComputer, ConcreteState c, Machine machine) {
        /*variantsMapping.putIfAbsent(c, new LinkedHashMap<>());
        return new Plus(atomicPredicates.stream().map(atomicPredicate -> atomicPredicate.getVInit(variantComputer, c, variantsMapping, machine)).toArray(AArithExpr[]::new));*/
        //return new Plus(atomicPredicates.stream().map(atomicPredicate -> atomicPredicate.getVInit(variantComputer, c)).toArray(AArithExpr[]::new));
        variantComputer.getVariantsMapping().putIfAbsent(c, new LinkedHashMap<>());
        return new Plus(atomicPredicates.stream().map(atomicPredicate -> variantComputer.computeVInit(atomicPredicate, c)).toArray(AArithExpr[]::new));
    }

    public final AArithExpr getV(AVariantComputer variantComputer, ConcreteState c, ConcreteState c_, Machine machine) {
        variantComputer.getVariantsMapping().put(c_, new LinkedHashMap<>());
        ArrayList<AArithExpr> sumOperands = new ArrayList<>();
        for (AAtomicRelevancePredicate atomicPredicate : atomicPredicates) {
            AArithExpr variant_ = variantComputer.computeV(atomicPredicate, c, c_);//atomicPredicate.getV(variantComputer, c, c_);
            if (Z3.checkSAT(new And(
                    machine.getInvariant(),
                    machine.getInvariant().accept(new Primer(1)),
                    c,
                    c_.accept(new Primer(1)),
                    new Not(atomicPredicate)
            ), machine.getDefsRegister()).isSAT()) {
                sumOperands.add(variantComputer.getVariantsMapping().get(c).get(atomicPredicate));
            } else {
                sumOperands.add(variant_);
            }
        }
        variantComputer.getVariantsMapping().put(c_, new LinkedHashMap<>());
        Iterator<AAtomicRelevancePredicate> apIterator = atomicPredicates.iterator();
        Iterator<AArithExpr> opIterator = sumOperands.iterator();
        while (apIterator.hasNext()) {
            Var variantVar = new Var("variant!");
            AAtomicRelevancePredicate ap = apIterator.next();
            AArithExpr op = opIterator.next();
            DefsRegister tmpDefsRegister = new DefsRegister(machine.getDefsRegister());
            tmpDefsRegister.getVarsDefs().put(variantVar.getName(), new Z());
            Z3Result result1 = Z3.checkSAT(new And(machine.getInvariant(), machine.getInvariant().accept(new Primer(1)), c, c_.accept(new Primer(1)), new Equals(variantVar, op)), tmpDefsRegister);
            variantComputer.getVariantsMapping().get(c_).put(ap, result1.getModel(new LinkedHashSet<>(Collections.singletonList(variantVar))).get(variantVar));
        }
        return new Plus(sumOperands.toArray(new AArithExpr[0]));
    }

    public LinkedHashSet<AAtomicRelevancePredicate> getAtomicPredicates() {
        return atomicPredicates;
    }

    @Override
    public RelevancePredicate cloned() {
        return new RelevancePredicate(atomicPredicates.stream().map(AAtomicRelevancePredicate::cloned).toArray(AAtomicRelevancePredicate[]::new));
    }

}
