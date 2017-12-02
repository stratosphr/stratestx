package langs.eventb.substitutions;

import visitors.interfaces.IObjectFormatter;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:44
 */
public final class Skip extends ASubstitution {

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ASubstitution clone() {
        return new Skip();
    }

}
