package algorithms.heuristics.relevance;

import langs.formal.graphs.ConcreteState;
import langs.maths.generic.arith.AArithExpr;

/**
 * Created by gvoiron on 20/12/17.
 * Time : 13:40
 */
public interface IVariantComputable {

    AArithExpr getVInit(AVariantComputer computer, ConcreteState c);

    AArithExpr getV(AVariantComputer computer, ConcreteState c, ConcreteState c_);

}
