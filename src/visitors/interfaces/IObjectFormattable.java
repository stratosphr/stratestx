package visitors.interfaces;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:49
 */
public interface IObjectFormattable {

    String accept(IObjectFormatter visitor);

}
