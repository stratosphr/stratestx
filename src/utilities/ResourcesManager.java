package utilities;

import java.io.File;

/**
 * Created by gvoiron on 20/11/17.
 * Time : 16:31
 */
public final class ResourcesManager {

    private static final File resourcesRoot = new File("resources");
    private static final File xmlSchemasRoot = new File(resourcesRoot, "xml-schemas");
    private static final File examplesRoot = new File(resourcesRoot, "examples");

    public static File getXMLSchema(EXMLSchema xmlSchema) {
        switch (xmlSchema) {
            case AP:
                return new File(xmlSchemasRoot, "ap.xsd");
            case EBM:
                return new File(xmlSchemasRoot, "ebm.xsd");
            case RP:
                return new File(xmlSchemasRoot, "rp.xsd");
        }
        throw new Error("Error: unknown xml schema resource \"" + xmlSchema + "\".");
    }

    public static File getModel(EModel model) {
        switch (model) {
            case CA:
                return new File(new File(examplesRoot, "CA"), "CA.ebm");
            case CM:
                return new File(new File(examplesRoot, "CM"), "CM.ebm");
            case EL:
                return new File(new File(examplesRoot, "EL"), "EL.ebm");
            case EV:
                return new File(new File(examplesRoot, "EV"), "EV.ebm");
            case EXAMPLE:
                return new File(new File(examplesRoot, "EXAMPLE"), "EXAMPLE.ebm");
            case GSM:
                return new File(new File(examplesRoot, "GSM"), "GSM.ebm");
            case L14:
                return new File(new File(examplesRoot, "L14"), "L14.ebm");
            case L14_2:
                return new File(new File(examplesRoot, "L14_2"), "L14_2.ebm");
            case PH:
                return new File(new File(examplesRoot, "PH"), "PH.ebm");
            default:
                throw new Error("Error: unknown model resource \"" + model + "\".");
        }
    }

    public static File getAbstractionPredicatesSet(EModel model, EAbstractionPredicatesSet abstractionPredicatesSet) {
        switch (model) {
            case CA:
                return new File(new File(examplesRoot, "CA"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case CM:
                return new File(new File(examplesRoot, "CM"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case EL:
                return new File(new File(examplesRoot, "EL"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case EV:
                return new File(new File(examplesRoot, "EV"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case EXAMPLE:
                return new File(new File(examplesRoot, "EXAMPLE"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case GSM:
                return new File(new File(examplesRoot, "GSM"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case L14:
                return new File(new File(examplesRoot, "L14"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case L14_2:
                return new File(new File(examplesRoot, "L14_2"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            case PH:
                return new File(new File(examplesRoot, "PH"), abstractionPredicatesSet.toString().toLowerCase() + ".ap");
            default:
                throw new Error("Error: unknown model resource \"" + model + "\".");
        }
    }

    public enum EXMLSchema {AP, EBM, RP}

    public enum EModel {CA, CM, EL, EV, EXAMPLE, GSM, L14, L14_2, PH}

    public enum EAbstractionPredicatesSet {AP0, AP1, AP2, AP3, AP4}

}
