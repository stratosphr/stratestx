package parsers.stratest;

import langs.eventb.Event;
import langs.eventb.Machine;
import langs.eventb.substitutions.*;
import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.*;
import langs.maths.generic.arith.operators.*;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.literals.False;
import langs.maths.generic.bool.literals.Invariant;
import langs.maths.generic.bool.literals.True;
import langs.maths.generic.bool.operators.*;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ASetExpr;
import langs.maths.set.literals.Enum;
import langs.maths.set.literals.*;
import langs.maths.set.operators.Difference;
import langs.maths.set.operators.Intersection;
import langs.maths.set.operators.Union;
import parsers.xml.XMLNode;
import parsers.xml.XMLParser;
import parsers.xml.schemas.XMLAttributesSchema;
import parsers.xml.schemas.XMLNodeSchema;
import utilities.Tuple;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import static utilities.ResourcesManager.EXMLSchema;
import static utilities.ResourcesManager.getXMLSchema;

/**
 * Created by gvoiron on 01/12/17.
 * Time : 19:25
 */
public final class StratestParser {

    private DefsRegister defsRegister;
    private List<String> errors;

    public StratestParser() {
        this.defsRegister = new DefsRegister();
        this.errors = new ArrayList<>();
    }

    public Machine parseModel(File file) {
        XMLParser parser = new XMLParser(true);
        XMLNode rootNode = parser.parse(file, getXMLSchema(EXMLSchema.EBM));
        rootNode.assertConformsTo(new XMLNodeSchema("model", new XMLAttributesSchema("name")));
        Invariant invariant = new Invariant(new True());
        ASubstitution initialisation = new Skip();
        LinkedHashSet<Event> events = new LinkedHashSet<>();
        XMLNode constsDefsNode = rootNode.getFirstChildWithName("consts-defs");
        XMLNode setsDefsNode = rootNode.getFirstChildWithName("sets-defs");
        XMLNode varsDefsNode = rootNode.getFirstChildWithName("vars-defs");
        XMLNode funsDefsNode = rootNode.getFirstChildWithName("funs-defs");
        XMLNode invariantNode = rootNode.getFirstChildWithName("invariant");
        XMLNode initialisationNode = rootNode.getFirstChildWithName("initialisation");
        XMLNode eventsNode = rootNode.getFirstChildWithName("events");
        try {
            if (constsDefsNode != null) {
                parseConstsDefs(constsDefsNode).forEach(constDef -> defsRegister.getConstsDefs().put(constDef.getLeft(), constDef.getRight()));
                System.out.println("constsDefs: " + defsRegister.getConstsDefs());
            }
            if (setsDefsNode != null) {
                parseSetsDefs(setsDefsNode).forEach(setDef -> defsRegister.getNamedSetsDefs().put(setDef.getLeft(), setDef.getRight()));
                System.out.println("setsDefs: " + defsRegister.getNamedSetsDefs());
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
            if (initialisationNode != null) {
                initialisation = parseInitialisation(initialisationNode);
                System.out.println("initialisation: " + initialisation);
            }
            if (eventsNode != null) {
                events = parseEvents(eventsNode);
                System.out.println("events:");
                events.forEach(System.out::println);
            }
        } catch (Exception e) {
            if (!errors.isEmpty()) {
                throw new Error("Errors encountered while parsing file \"" + file.getAbsolutePath() + "\" as EBM model:\n" + errors.stream().collect(Collectors.joining("\n")));
            } else {
                e.printStackTrace();
            }
        }
        return new Machine(defsRegister, invariant, initialisation, events);
    }

    private void handleException(XMLNode node, String message) {
        String fullMessage = "l." + node.getLine() + ", c." + node.getColumn() + ": " + message;
        errors.add(fullMessage);
        try {
            throw new Exception(fullMessage);
        } catch (Exception ignored) {
        }
    }

    private List<Tuple<String, AArithExpr>> parseConstsDefs(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("consts-defs"));
        return node.getChildren().stream().map(this::parseConstDef).collect(Collectors.toList());
    }

    private Tuple<String, AArithExpr> parseConstDef(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("const-def", new XMLAttributesSchema("name")));
        return new Tuple<>(node.getAttributes().get("name"), parseArithExpr(node.getChildren().get(0)));
    }

    private List<Tuple<String, AFiniteSetExpr>> parseSetsDefs(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("sets-defs"));
        return node.getChildren().stream().map(this::parseSetDef).collect(Collectors.toList());
    }

    private Tuple<String, AFiniteSetExpr> parseSetDef(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("set-def", new XMLAttributesSchema("name")));
        return new Tuple<>(node.getAttributes().get("name"), parseFiniteSetExpr(node.getChildren().get(0)));
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
            case "plus":
                return parsePlus(node);
            case "minus":
                return parseMinus(node);
            case "times":
                return parseTimes(node);
            case "div":
                return parseDiv(node);
            case "mod":
                return parseMod(node);
            default:
                handleException(node, "The following node was found but an arithmetic expression was expected:\n" + node);
        }
        return null;
    }

    private Int parseInt(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("int", new XMLAttributesSchema("value")));
        try {
            return new Int(Integer.parseInt(node.getAttributes().get("value")));
        } catch (NumberFormatException ignored) {
            handleException(node, "Unable to parse value \"" + node.getAttributes().get("value") + "\" to integer.");
        }
        return null;
    }

    private Const parseConst(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("const", new XMLAttributesSchema("name")));
        return new Const(node.getAttributes().get("name"));
    }

    private EnumValue parseEnumValue(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("enum-value", new XMLAttributesSchema("name")));
        return new EnumValue(node.getAttributes().get("name"));
    }

    private Var parseVar(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("var", new XMLAttributesSchema("name")));
        return new Var(node.getAttributes().get("name"));
    }

    private Fun parseFun(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("fun", new XMLAttributesSchema("name")));
        return new Fun(node.getAttributes().get("name"), parseArithExpr(node.getChildren().get(0)));
    }

    private Plus parsePlus(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("plus"));
        return new Plus(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private Minus parseMinus(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("minus"));
        return new Minus(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private Times parseTimes(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("times"));
        return new Times(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private Div parseDiv(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("div"));
        return new Div(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private Mod parseMod(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("mod"));
        return new Mod(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }


    private ABoolExpr parseBoolExpr(XMLNode node) {
        switch (node.getName()) {
            case "false":
                return parseFalse(node);
            case "true":
                return parseTrue(node);
            case "not":
                return parseNot(node);
            case "and":
                return parseAnd(node);
            case "or":
                return parseOr(node);
            case "equals":
                return parseEquals(node);
            case "neq":
                return parseNotEquals(node);
            case "lt":
                return parseLT(node);
            case "leq":
                return parseLEQ(node);
            case "geq":
                return parseGEQ(node);
            case "gt":
                return parseGT(node);
            case "implies":
                return parseImplies(node);
            case "equiv":
                return parseEquiv(node);
            case "in-domain":
                return parseInDomain(node);
            case "forall":
                return parseForAll(node);
            case "exists":
                return parseExists(node);
            default:
                handleException(node, "The following node was found but a boolean expression was expected:\n" + node);
        }
        return null;
    }

    private False parseFalse(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("false"));
        return new False();
    }

    private True parseTrue(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("true"));
        return new True();
    }

    private Not parseNot(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("not"));
        return new Not(parseBoolExpr(node.getChildren().get(0)));
    }

    private And parseAnd(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("and"));
        return new And(node.getChildren().stream().map(this::parseBoolExpr).toArray(ABoolExpr[]::new));
    }

    private Or parseOr(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("or"));
        return new Or(node.getChildren().stream().map(this::parseBoolExpr).toArray(ABoolExpr[]::new));
    }

    private NotEquals parseNotEquals(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("neq"));
        return new NotEquals(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private Equals parseEquals(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("equals"));
        return new Equals(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private LT parseLT(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("lt"));
        return new LT(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private LEQ parseLEQ(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("leq"));
        return new LEQ(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private GEQ parseGEQ(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("geq"));
        return new GEQ(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private GT parseGT(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("gt"));
        return new GT(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private Exists parseExists(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("exists"));
        return new Exists(parseBoolExpr(node.getChildren().get(1)), parseVarsDefs(node.getChildren().get(0)).stream().map(tuple -> new VarInDomain(new Var(tuple.getLeft()), tuple.getRight())).toArray(VarInDomain[]::new));
    }

    private ForAll parseForAll(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("forall"));
        return new ForAll(parseBoolExpr(node.getChildren().get(1)), parseVarsDefs(node.getChildren().get(0)).stream().map(tuple -> new VarInDomain(new Var(tuple.getLeft()), tuple.getRight())).toArray(VarInDomain[]::new));
    }

    private Implies parseImplies(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("implies"));
        return new Implies(parseBoolExpr(node.getChildren().get(0)), parseBoolExpr(node.getChildren().get(1)));
    }

    private Equiv parseEquiv(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("equiv"));
        return new Equiv(parseBoolExpr(node.getChildren().get(0)), parseBoolExpr(node.getChildren().get(1)));
    }

    private InDomain parseInDomain(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("in-domain"));
        return new InDomain(parseArithExpr(node.getChildren().get(0)), parseSetExpr(node.getChildren().get(1)));
    }

    private Invariant parseInvariant(XMLNode node) {
        return new Invariant(parseBoolExpr(node.getChildren().get(0)));
    }

    private ASetExpr parseSetExpr(XMLNode node) {
        switch (node.getName()) {
            case "integers":
                return parseZ(node);
            case "set":
                return parseFiniteSetExpr(node);
            case "range":
                return parseFiniteSetExpr(node);
            case "enum":
                return parseFiniteSetExpr(node);
            case "named-set":
                return parseFiniteSetExpr(node);
            case "union":
                return parseFiniteSetExpr(node);
            case "intersection":
                return parseFiniteSetExpr(node);
            case "difference":
                return parseFiniteSetExpr(node);
            default:
                handleException(node, "The following node was found but a set expression was expected:\n" + node);
        }
        return null;
    }

    private Z parseZ(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("integers"));
        return new Z();
    }

    private AFiniteSetExpr parseFiniteSetExpr(XMLNode node) {
        switch (node.getName()) {
            case "set":
                return parseSet(node);
            case "range":
                return parseRange(node);
            case "enum":
                return parseEnum(node);
            case "named-set":
                return parseNamedSet(node);
            case "intersection":
                return parseIntersection(node);
            case "union":
                return parseUnion(node);
            case "difference":
                return parseDifference(node);
            default:
                handleException(node, "The following node was found but a finite set expression was expected:\n" + node);
        }
        return null;
    }

    private Set parseSet(XMLNode node) {
        return new Set(node.getChildren().stream().map(this::parseArithExpr).toArray(AArithExpr[]::new));
    }

    private Enum parseEnum(XMLNode node) {
        return new Enum(node.getChildren().stream().map(this::parseEnumValue).toArray(EnumValue[]::new));
    }

    private Range parseRange(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("range"));
        return new Range(parseArithExpr(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private NamedSet parseNamedSet(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("named-set", new XMLAttributesSchema("name")));
        if (!defsRegister.getNamedSetsDefs().containsKey(node.getAttributes().get("name"))) {
            handleException(node, "Named set \"" + node.getAttributes().get("name") + "\" was not defined in this scope.");
        }
        return new NamedSet(node.getAttributes().get("name"), defsRegister.getNamedSetsDefs().get(node.getAttributes().get("name")));
    }

    private Intersection parseIntersection(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("intersection"));
        return new Intersection(node.getChildren().stream().map(this::parseFiniteSetExpr).toArray(AFiniteSetExpr[]::new));
    }

    private Union parseUnion(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("union"));
        return new Union(node.getChildren().stream().map(this::parseFiniteSetExpr).toArray(AFiniteSetExpr[]::new));
    }

    private Difference parseDifference(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("difference"));
        return new Difference(node.getChildren().stream().map(this::parseFiniteSetExpr).toArray(AFiniteSetExpr[]::new));
    }

    private ASubstitution parseInitialisation(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("initialisation"));
        return parseSubstitution(node.getChildren().get(0));
    }

    private ASubstitution parseSubstitution(XMLNode node) {
        switch (node.getName()) {
            case "skip":
                return parseSkip(node);
            case "assignments":
                return parseAssignments(node);
            case "var-assignment":
                return parseAssignment(node);
            case "fun-assignment":
                return parseAssignment(node);
            case "select":
                return parseSelect(node);
            case "if-then-else":
                return parseIfThenElse(node);
            case "choice":
                return parseChoice(node);
            case "any":
                return parseAny(node);
            default:
                handleException(node, "The following node was found but a substitution was expected:\n" + node);
        }
        return null;
    }

    private Skip parseSkip(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("skip"));
        return new Skip();
    }

    private Assignments parseAssignments(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("assignments"));
        return new Assignments(node.getChildren().stream().map(this::parseAssignment).toArray(AAssignment[]::new));
    }

    private AAssignment parseAssignment(XMLNode node) {
        switch (node.getName()) {
            case "var-assignment":
                return parseVarAssignment(node);
            case "fun-assignment":
                return parseFunAssignment(node);
            default:
                handleException(node, "The following node was found but an assignment was expected:\n" + node);
        }
        return null;
    }

    private VarAssignment parseVarAssignment(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("var-assignment"));
        return new VarAssignment(parseVar(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private FunAssignment parseFunAssignment(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("fun-assignment"));
        return new FunAssignment(parseFun(node.getChildren().get(0)), parseArithExpr(node.getChildren().get(1)));
    }

    private Select parseSelect(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("select"));
        return new Select(parseBoolExpr(node.getChildren().get(0)), parseSubstitution(node.getChildren().get(1)));
    }

    private IfThenElse parseIfThenElse(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("if-then-else"));
        return new IfThenElse(parseBoolExpr(node.getChildren().get(0)), parseSubstitution(node.getChildren().get(1)), parseSubstitution(node.getChildren().get(2)));
    }

    private Choice parseChoice(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("choice"));
        return new Choice(node.getChildren().stream().map(this::parseSubstitution).toArray(ASubstitution[]::new));
    }

    private Any parseAny(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("any"));
        return new Any(parseBoolExpr(node.getChildren().get(1)), parseSubstitution(node.getChildren().get(2)), parseVarsDefs(node.getChildren().get(0)).stream().map(tuple -> new VarInDomain(new Var(tuple.getLeft()), tuple.getRight())).toArray(VarInDomain[]::new));
    }

    private LinkedHashSet<Event> parseEvents(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("events"));
        return node.getChildren().stream().map(this::parseEvent).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Event parseEvent(XMLNode node) {
        node.assertConformsTo(new XMLNodeSchema("event", new XMLAttributesSchema("name")));
        return new Event(node.getAttributes().get("name"), parseSubstitution(node.getChildren().get(0)));
    }

}
