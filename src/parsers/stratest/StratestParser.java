package parsers.stratest;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Const;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.arith.literals.Int;
import langs.maths.generic.arith.literals.Var;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ASetExpr;
import langs.maths.set.literals.Range;
import parsers.xml.XMLNode;
import parsers.xml.XMLParser;
import parsers.xml.schemas.XMLAttributesSchema;
import parsers.xml.schemas.XMLNodeSchema;
import utilities.Tuple;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static utilities.ResourcesManager.EXMLSchema;
import static utilities.ResourcesManager.getXMLSchema;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 19:25
 */
public final class StratestParser {

    // TODO : Remove errors thrown and return null instead (The exceptions should all be handled safely)
    public void parseModel(File file) {
        // TODO : put verification to true
        XMLParser parser = new XMLParser(false);
        XMLNode rootNode = parser.parse(file, getXMLSchema(EXMLSchema.EBM));
        DefsRegister defsRegister = new DefsRegister();
        ABoolExpr invariant = new True();
        rootNode.assertConformsTo(new XMLNodeSchema("model", new XMLAttributesSchema("name")));
        XMLNode constsDefsNode = rootNode.getFirstChildWithName("consts-defs");
        XMLNode varsDefsNode = rootNode.getFirstChildWithName("vars-defs");
        XMLNode funsDefsNode = rootNode.getFirstChildWithName("funs-defs");
        XMLNode invariantNode = rootNode.getFirstChildWithName("invariant");
        try {
            if (constsDefsNode != null) {
                parseConstsDefs(constsDefsNode).forEach(constDef -> defsRegister.getConstsDefs().put(constDef.getLeft(), constDef.getRight()));
                System.out.println("constsDefs: " + defsRegister.getConstsDefs());
            }
            if (varsDefsNode != null) {
                parseVarsDefs(varsDefsNode).forEach(varDef -> defsRegister.getVarsDefs().put(varDef.getLeft(), varDef.getRight()));
                System.out.println("varsDefs: " + defsRegister.getVarsDefs());
            }
            if (funsDefsNode != null) {
                parseFunsDefs(funsDefsNode).forEach(funDef -> defsRegister.getFunsDefs().put(funDef.getLeft(), funDef.getRight()));
                System.out.println("funsDefs: " + defsRegister.getFunsDefs());
            }
            if (invariantNode != null) {
                invariant = parseInvariant(invariantNode);
                System.out.println("invariant: " + invariant);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error();
        }
    }

    private void handleException(XMLNode node, String message) {
        throw new Error("Error l." + node.getLine() + ", c." + node.getColumn() + ": " + message);
    }

    private List<Tuple<String, AArithExpr>> parseConstsDefs(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("consts-defs"));
        return node.getChildren().stream().map(this::parseConstDef).collect(Collectors.toList());
    }

    private Tuple<String, AArithExpr> parseConstDef(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("const-def", new XMLAttributesSchema("name")));
        return new Tuple<>(node.getAttributes().get("name"), parseArithExpr(node.getChildren().get(0)));
    }

    private List<Tuple<String, ASetExpr>> parseVarsDefs(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("vars-defs"));
        return node.getChildren().stream().map(this::parseVarDef).collect(Collectors.toList());
    }

    private Tuple<String, ASetExpr> parseVarDef(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("var-def", new XMLAttributesSchema("name")));
        return new Tuple<>(node.getAttributes().get("name"), parseSetExpr(node.getChildren().get(0)));
    }

    private List<Tuple<String, Tuple<AFiniteSetExpr, ASetExpr>>> parseFunsDefs(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("funs-defs"));
        return node.getChildren().stream().map(this::parseFunDef).collect(Collectors.toList());
    }

    private Tuple<String, Tuple<AFiniteSetExpr, ASetExpr>> parseFunDef(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("fun-def", new XMLAttributesSchema("name")));
        return new Tuple<>(node.getAttributes().get("name"), new Tuple<>(parseFiniteSetExpr(node.getChildren().get(0)), parseSetExpr(node.getChildren().get(1))));
    }

    private AArithExpr parseArithExpr(XMLNode node) {
        switch (node.getName()) {
            case "int":
                return parseInt(node);
            case "const":
                return parseConst(node);
            case "var":
                return parseVar(node);
            case "fun":
                return parseFun(node);
            case "enum-value":
                return parseEnumValue(node);
            default:
                handleException(node, "The following node was found but an arithmetic expression was expected:\n" + node);
        }
        throw new Error();
    }

    // TODO : Use precise return type
    private AArithExpr parseInt(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("int", new XMLAttributesSchema("value")));
        try {
            return new Int(Integer.parseInt(node.getAttributes().get("value")));
        } catch (NumberFormatException ignored) {
            handleException(node, "Unable to parse value \"" + node.getAttributes().get("value") + "\" to integer.");
        }
        throw new Error();
    }

    private AArithExpr parseConst(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("const", new XMLAttributesSchema("name")));
        return new Const(node.getAttributes().get("name"));
    }

    // TODO: Handle enum values
    private AArithExpr parseEnumValue(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("enum-value", new XMLAttributesSchema("name")));
        return new Int(-42);
    }

    private AArithExpr parseVar(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("var", new XMLAttributesSchema("name")));
        return new Var(node.getAttributes().get("name"));
    }

    private AArithExpr parseFun(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("fun", new XMLAttributesSchema("name")));
        return new Fun(node.getAttributes().get("name"), parseArithExpr(node.getChildren().get(0)));
    }

    private ABoolExpr parseBoolExpr(XMLNode node) {
        switch (node.getName()) {
            case "equals":
                return parseEquals(node);
            default:
                handleException(node, "The following node was found but an boolean expression was expected:\n" + node);
        }
        throw new Error();
    }

    private ABoolExpr parseEquals(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("equals"));
        return new Equals(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    // TODO: Return Invariant instance
    private ABoolExpr parseInvariant(XMLNode node) {
        return parseBoolExpr(node.getChildren().get(0));
    }

    private ASetExpr parseSetExpr(XMLNode node) {
        switch (node.getName()) {
            case "set":
                return parseFiniteSetExpr(node);
            case "range":
                return parseFiniteSetExpr(node);
            case "enum":
                return parseFiniteSetExpr(node);
            default:
                handleException(node, "The following node was found but a set expression was expected:\n" + node);
        }
        throw new Error();
    }

    private AFiniteSetExpr parseFiniteSetExpr(XMLNode node) {
        switch (node.getName()) {
            case "set":
                return parseSet(node);
            case "range":
                return parseRange(node);
            case "enum":
                return parseEnum(node);
            default:
                handleException(node, "The following node was found but a finite set expression was expected:\n" + node);
        }
        throw new Error();
    }

    private AFiniteSetExpr parseSet(XMLNode node) {
        throw new Error();
    }

    // TODO: Handle enums sets
    private AFiniteSetExpr parseEnum(XMLNode node) {
        return null;
    }

    private AFiniteSetExpr parseRange(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("range"));
        return new Range(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

}
