package visitors.interfaces;

import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Var;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:40
 */
public interface IModelVisitor {

    void visit(Var var);

    void visit(Fun fun);

}
