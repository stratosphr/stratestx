package visitors.interfaces;

import parsers.xml.XMLNode;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 13:46
 */
public interface IXMLFormatter {

    String visit(XMLNode node);

}
