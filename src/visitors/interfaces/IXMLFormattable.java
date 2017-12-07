package visitors.interfaces;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 13:46
 */
public interface IXMLFormattable {

    @SuppressWarnings("unused")
    String accept(IXMLFormatter formatter);

}
