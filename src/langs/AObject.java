package langs;

import utilities.ICloneable;
import visitors.ObjectFormatter;
import visitors.interfaces.IObjectFormattable;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:09
 */
public abstract class AObject implements IObjectFormattable, ICloneable, Comparable<AObject> {

    private Integer hashCode;

    @Override
    public abstract AObject clone();

    @Override
    public final int hashCode() {
        if (hashCode == null) {
            hashCode = toString().hashCode();
        }
        return hashCode;
    }

    @Override
    public final boolean equals(Object o) {
        return this == o || (getClass().equals(o.getClass()) && hashCode() == o.hashCode() && toString().equals(o.toString()));
    }

    @Override
    public int compareTo(AObject aObject) {
        return toString().compareTo(aObject.toString());
    }

    @Override
    public final String toString() {
        return accept(new ObjectFormatter());
    }

}
