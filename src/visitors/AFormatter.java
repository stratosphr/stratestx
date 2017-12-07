package visitors;

import java.util.Collections;

/**
 * Created by gvoiron on 16/11/17.
 * Time : 21:55
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "SameReturnValue"})
public abstract class AFormatter {

    private int indentation;

    protected AFormatter() {
        this.indentation = 0;
    }

    protected String fold(String text, int foldThreshold) {
        return fold(text, foldThreshold, "", ", ");
    }

    protected String fold(String text, int foldThreshold, String lineDelimiter, String tabDelimiter) {
        if (text.length() < foldThreshold) {
            return text;
        } else {
            return text.replaceAll("[(]\n*\t*", "(").replaceAll("[]]\n*", "]").replaceAll("\n", lineDelimiter).replaceAll("\t", tabDelimiter).replaceAll(tabDelimiter + tabDelimiter, tabDelimiter).replaceAll(tabDelimiter + "[)]", ")");
        }
    }

    protected String indentLeft() {
        if (indentation > 0) {
            --indentation;
        }
        return "";
    }

    protected String indentRight() {
        ++indentation;
        return "";
    }

    protected String indent(String text) {
        return String.join("", Collections.nCopies(indentation, "\t")) + text;
    }

    protected String line() {
        return line("");
    }

    protected String line(String line) {
        return line + "\n";
    }

    protected String indentLine(String line) {
        return indent("") + line(line);
    }

}
