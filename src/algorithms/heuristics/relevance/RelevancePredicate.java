package algorithms.heuristics.relevance;

import langs.eventb.Machine;
import langs.formal.graphs.ConcreteState;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
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
    private final IVariantComputer variantComputer;
    private final LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> variantsMapping;

    public RelevancePredicate(IVariantComputer variantComputer, AAtomicRelevancePredicate... atomicPredicates) {
        super(new Or(atomicPredicates));
        this.variantComputer = variantComputer;
        this.atomicPredicates = new LinkedHashSet<>(Arrays.asList(atomicPredicates));
        this.variantsMapping = new LinkedHashMap<>();
    }

    public final AArithExpr getVInit(ConcreteState c, Machine machine) {
        variantsMapping.putIfAbsent(c, new LinkedHashMap<>());
        return new Plus(atomicPredicates.stream().map(atomicPredicate -> atomicPredicate.getVInit(variantComputer, c, variantsMapping, machine)).toArray(AArithExpr[]::new));
    }

    public final AArithExpr getV(ConcreteState c, ConcreteState c_, Machine machine) {
        variantsMapping.put(c_, new LinkedHashMap<>());
        ArrayList<AArithExpr> sumOperands = new ArrayList<>();
        for (AAtomicRelevancePredicate atomicPredicate : atomicPredicates) {
            AArithExpr variant_ = atomicPredicate.getV(variantComputer, c, c_, variantsMapping, machine);
            if (Z3.checkSAT(new And(
                    machine.getInvariant(),
                    machine.getInvariant().accept(new Primer(1)),
                    c,
                    c_.accept(new Primer(1)),
                    new Not(atomicPredicate)
            ), machine.getDefsRegister()).isSAT()) {
                sumOperands.add(variantsMapping.get(c).get(atomicPredicate));
            } else {
                sumOperands.add(variant_);
            }
        }
        variantsMapping.put(c_, new LinkedHashMap<>());
        Iterator<AAtomicRelevancePredicate> apIterator = atomicPredicates.iterator();
        Iterator<AArithExpr> opIterator = sumOperands.iterator();
        while (apIterator.hasNext()) {
            Var variantVar = new Var("variant!");
            AAtomicRelevancePredicate ap = apIterator.next();
            AArithExpr op = opIterator.next();
            DefsRegister tmpDefsRegister = new DefsRegister(machine.getDefsRegister());
            tmpDefsRegister.getVarsDefs().put(variantVar.getName(), new Z());
            Z3Result result1 = Z3.checkSAT(new And(machine.getInvariant(), machine.getInvariant().accept(new Primer(1)), c, c_.accept(new Primer(1)), new Equals(variantVar, op)), tmpDefsRegister);
            variantsMapping.get(c_).put(ap, result1.getModel(new LinkedHashSet<>(Collections.singletonList(variantVar))).get(variantVar));
        }
        return new Plus(sumOperands.toArray(new AArithExpr[0]));
    }

    public LinkedHashSet<AAtomicRelevancePredicate> getAtomicPredicates() {
        return atomicPredicates;
    }

    public LinkedHashMap<ConcreteState, LinkedHashMap<AAtomicRelevancePredicate, AValue>> getVariantsMapping() {
        return variantsMapping;
    }

    @Override
    public RelevancePredicate clone() {
        return new RelevancePredicate(variantComputer, atomicPredicates.stream().map(AAtomicRelevancePredicate::clone).toArray(AAtomicRelevancePredicate[]::new));
    }

}
