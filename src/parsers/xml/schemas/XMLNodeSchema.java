package parsers.xml.schemas;

import parsers.xml.XMLNode;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 12:59
 */
public final class XMLNodeSchema {

    private final String name;
    private final XMLAttributesSchema attributesSchema;

    public XMLNodeSchema(String name, XMLAttributesSchema attributesSchema) {
        this.name = name;
        this.attributesSchema = attributesSchema;
    }

    public void validate(XMLNode node) {
        if (!node.getName().equals(name)) {
            handleException(node, "Expected name \"" + name + "\" but \"" + node.getName() + "\" found.");
        }
        for (String required : attributesSchema.getRequiredAttributes()) {
            if (!node.getAttributes().containsKey(required)) {
                handleException(node, "Missing required attribute \"" + required + "\".");
            }
        }
        for (String attribute : node.getAttributes().keySet()) {
            if (!attributesSchema.getRequiredAttributes().contains(attribute) && !attributesSchema.getOptionalAttributes().contains(attribute)) {
                handleException(node, "Unexpected attribute \"" + attribute + "\".");
            }
        }
    }

    private void handleException(XMLNode node, String message) {
        try {
            throw new Error("Error l." + node.getLine() + ", c." + node.getColumn() + ": " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
