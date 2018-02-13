package visitors.dot;

import langs.AObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 16:53
 */

public abstract class ADotObject<This extends ADotObject<This>> extends AObject {

    private final Map<Object, Object> parameters;
    private String comment;

    ADotObject() {
        this.parameters = new LinkedHashMap<>();
        this.comment = "";
    }

    protected abstract This getThis();

    private This numberParameter(Object key, Object value) {
        parameters.put(key, value);
        return getThis();
    }

    private This quoteParameter(Object key, Object value) {
        parameters.put(key, "\"" + value + "\"");
        return getThis();
    }

    @SuppressWarnings("SameParameterValue")
    private This htmlParameter(Object key, Object value) {
        parameters.put(key, "<" + value + ">");
        return getThis();
    }

    public final This setLabel(Object label) {
        return setLabel(label, false);
    }

    public final This setLabel(Object label, boolean isHTML) {
        return isHTML ? htmlParameter("label", label) : quoteParameter("label", label);
    }

    public final This setShape(Object shape) {
        return quoteParameter("shape", shape);
    }

    public final This setStyle(Object style) {
        return quoteParameter("style", style);
    }

    public final This setFillColor(Object fillColor) {
        return quoteParameter("fillcolor", fillColor);
    }

    public final This setColor(Object color) {
        return quoteParameter("color", color);
    }

    public final This setPenWidth(Integer penWidth) {
        return numberParameter("penwidth", penWidth.toString());
    }

    public final This setWeight(double weight) {
        return numberParameter("weight", String.valueOf(weight));
    }

    public final This setComment(Object s) {
        comment = s.toString();
        return getThis();
    }

    public final Map<Object, Object> getParameters() {
        return parameters;
    }

    public final String getComment() {
        return comment;
    }

    @Override
    public abstract ADotObject cloned();

}
