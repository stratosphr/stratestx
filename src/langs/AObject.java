package langs;

import visitors.ObjectFormatter;
import visitors.interfaces.IObjectFormattable;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:09
 */
public abstract class AObject implements IObjectFormattable {

    @Override
    public final String toString() {
        return accept(new ObjectFormatter());
    }

}
