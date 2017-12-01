package visitors;

import parsers.xml.XMLNode;
import visitors.interfaces.IXMLFormatter;

import java.util.stream.Collectors;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 13:46
 */
public final class XMLFormatter extends AFormatter implements IXMLFormatter {

    @Override
    public String visit(XMLNode node) {
        String formatted = "<" + node.getName() + (node.getAttributes().isEmpty() ? "" : " " + node.getAttributes().keySet().stream().map(key -> key + "=\"" + node.getAttributes().get(key) + "\"").collect(Collectors.joining(" ")));
        if (node.getChildren().isEmpty()) {
            formatted += "/>";
        } else {
            formatted += ">\n" + indentRight() + node.getChildren().stream().map(child -> indentLine(child.accept(this))).collect(Collectors.joining()) + indentLeft() + indent("</" + node.getName() + ">");
        }
        return formatted;
    }

}
