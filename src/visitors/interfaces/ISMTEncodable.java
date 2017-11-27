package visitors.interfaces;

import com.microsoft.z3.Expr;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:46
 */
public interface ISMTEncodable {

    Expr accept(ISMTEncoder encoder);

}
