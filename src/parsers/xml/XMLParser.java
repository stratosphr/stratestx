package parsers.xml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by gvoiron on 16/11/17.
 * Time : 15:20
 */
@SuppressWarnings("unused")
public final class XMLParser extends DefaultHandler {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private final boolean validate;
    private final SAXParserFactory factory;
    private Locator locator;
    private XMLNode root;
    private XMLNode currentNode;

    public XMLParser(boolean validate) {
        this.validate = validate;
        factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(validate);
    }

    public XMLNode parse(File file) {
        return parse(file, null);
    }

    public XMLNode parse(File file, File xsd) {
        try {
            SAXParser parser = factory.newSAXParser();
            if (validate) {
                parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                if (xsd != null) {
                    parser.setProperty(JAXP_SCHEMA_SOURCE, xsd);
                }
            }
            parser.parse(file, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes) {
        XMLNode node = new XMLNode(tagName, new File(locator.getSystemId()), locator.getLineNumber(), locator.getColumnNumber());
        for (int i = 0; i < attributes.getLength(); i++) {
            node.addAttribute(attributes.getLocalName(i), attributes.getValue(i));
        }
        if (currentNode == null) {
            root = node;
        } else {
            currentNode.addChild(node);
        }
        currentNode = node;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        currentNode = currentNode.getParent();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        currentNode.addText(new String(ch, start, length).trim());
    }

    @Override
    public void warning(SAXParseException e) {
        e.printStackTrace();
    }

    @Override
    public void error(SAXParseException e) {
        e.printStackTrace();
    }

    @Override
    public void fatalError(SAXParseException e) {
        e.printStackTrace();
    }

}
