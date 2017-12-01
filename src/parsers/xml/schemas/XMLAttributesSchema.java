package parsers.xml.schemas;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 13:02
 */
public final class XMLAttributesSchema {

    private final List<String> requiredAttributes;
    private final List<String> optionalAttributes;

    public XMLAttributesSchema(String[] required) {
        this(required, new String[]{});
    }

    public XMLAttributesSchema(String[] requiredAttributes, String[] optionalAttributes) {
        this.requiredAttributes = Arrays.asList(requiredAttributes);
        this.optionalAttributes = Arrays.asList(optionalAttributes);
    }

    public List<String> getRequiredAttributes() {
        return requiredAttributes;
    }

    public List<String> getOptionalAttributes() {
        return optionalAttributes;
    }

}
