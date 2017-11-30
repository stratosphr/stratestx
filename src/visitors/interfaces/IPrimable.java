package visitors.interfaces;

import langs.maths.AExpr;

/**
 * Created by gvoiron on 30/11/17.
 * Time : 14:32
 */
public interface IPrimable {

    AExpr accept(IPrimer primer);

}
