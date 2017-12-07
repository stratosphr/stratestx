package parsers.xml;

import parsers.xml.schemas.XMLNodeSchema;
import visitors.XMLFormatter;
import visitors.interfaces.IXMLFormattable;
import visitors.interfaces.IXMLFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 12:52
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public final class XMLNode implements IXMLFormattable {

    private XMLNode parent;
    private final String name;
    private final Map<String, String> attributes;
    private final List<XMLNode> children;
    private String text;
    private final File file;
    private final int line;
    private final int column;

    XMLNode(String name) {
        this(name, null, -1, -1);
    }

    XMLNode(String name, File file, int line, int column) {
        this(name, new LinkedHashMap<>(), new ArrayList<>(), file, line, column);
    }

    XMLNode(String name, Map<String, String> attributes) {
        this(name, attributes, null, -1, -1);
    }

    XMLNode(String name, Map<String, String> attributes, File file, int line, int column) {
        this(name, attributes, new ArrayList<>(), file, line, column);
    }

    XMLNode(String name, List<XMLNode> children) {
        this(name, new LinkedHashMap<>(), children, null, -1, -1);
    }

    XMLNode(String name, List<XMLNode> children, File file, int line, int column) {
        this(name, new LinkedHashMap<>(), children, file, line, column);
    }

    XMLNode(String name, Map<String, String> attributes, List<XMLNode> children) {
        this(name, attributes, children, null, -1, -1);
    }

    XMLNode(String name, Map<String, String> attributes, List<XMLNode> children, File file, int line, int column) {
        this.parent = null;
        this.name = name;
        this.attributes = attributes;
        this.children = children;
        this.text = "";
        this.file = file;
        this.line = line;
        this.column = column;
    }

    public final void assertConformsTo(XMLNodeSchema schema) {
        schema.validate(this);
    }

    public XMLNode getFirstChildWithName(String name) {
        return children.stream().filter(node -> node.getName().equals(name)).findFirst().orElse(null);
    }

    final XMLNode getParent() {
        return parent;
    }

    private void setParent(XMLNode parent) {
        this.parent = parent;
    }

    public final String getName() {
        return name;
    }

    public final Map<String, String> getAttributes() {
        return attributes;
    }

    void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public final List<XMLNode> getChildren() {
        return children;
    }

    void addChild(XMLNode child) {
        child.setParent(this);
        children.add(child);
    }

    void addText(String text) {
        this.text += text;
    }

    public String getText() {
        return text;
    }

    public File getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String accept(IXMLFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public String toString() {
        return accept(new XMLFormatter());
    }

}
