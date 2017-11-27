package visitors;

import java.util.Collections;

/**
 * Created by gvoiron on 16/11/17.
 * Time : 21:55
 */
abstract class AFormatter {

    private int indentation;

    AFormatter() {
        this.indentation = 0;
    }

    String indentLeft() {
        if (indentation > 0) {
            --indentation;
        }
        return "";
    }

    String indentRight() {
        ++indentation;
        return "";
    }

    String indent(String text) {
        return String.join("", Collections.nCopies(indentation, "\t")) + text;
    }

    String line() {
        return line("");
    }

    String line(String line) {
        return line + "\n";
    }

    String indentLine(String line) {
        return indent("") + line(line);
    }

}
