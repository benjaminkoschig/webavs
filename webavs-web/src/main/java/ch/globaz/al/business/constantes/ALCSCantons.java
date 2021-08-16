package ch.globaz.al.business.constantes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Codes système liés aux cantons
 * 
 * <ul>
 * <li>Liste des cantons</li>
 * </ul>
 * 
 * @author jts
 * 
 */
public interface ALCSCantons {

    /*
     * Cantons suisses
     */

    /**
     * CS : canton "Argovie"
     */
    String AG = "61020001";
    /**
     * CS : canton "Appenzel int."
     */
    String AI = "61020002";
    /**
     * CS : canton "Appenzel ext."
     */
    String AR = "61020003";
    /**
     * CS : canton "Berne"
     */
    String BE = "61020004";
    /**
     * CS : canton "Bâle campagne"
     */
    String BL = "61020005";
    /**
     * CS : canton "Bâle ville"
     */
    String BS = "61020006";
    /**
     * CS : canton "Fribourg"
     */
    String FR = "61020007";
    /**
     * CS : canton "Genève"
     */
    String GE = "61020008";
    /**
     * CS : canton "Glaris"
     */
    String GL = "61020009";
    /**
     * CS : canton "Grisons"
     */
    String GR = "61020010";
    /**
     * CS : groupe "Cantons"
     */
    String GROUP_CANTONS = "60020000";

    /**
     * CS : canton "Jura"
     */
    String JU = "61020011";
    /**
     * CS : canton "Lucerne"
     */
    String LU = "61020012";
    /**
     * CS : canton "Neuchâtel"
     */
    String NE = "61020013";
    /**
     * CS : canton "Nidwald"
     */
    String NW = "61020014";
    /**
     * CS : canton "Obwald"
     */
    String OW = "61020015";
    /**
     * CS : canton "St-Gall"
     */
    String SG = "61020016";
    /**
     * CS : canton "Schaffouse"
     */
    String SH = "61020017";
    /**
     * CS : canton "Soleure"
     */
    String SO = "61020018";
    /**
     * CS : canton "Schwytz"
     */
    String SZ = "61020019";
    /**
     * CS : canton "Thurgovie"
     */
    String TG = "61020021";
    /**
     * CS : canton "Tessin"
     */
    String TI = "61020020";
    /**
     * CS : canton "Uri"
     */
    String UR = "61020022";
    /**
     * CS : canton "Vaud"
     */
    String VD = "61020023";
    /**
     * CS : canton "Valais"
     */
    String VS = "61020024";
    /**
     * CS : canton "Zoug"
     */
    String ZG = "61020025";
    /**
     * CS : canton "Zürich"
     */
    String ZH = "61020026";
    /**
     * Liste des abréviations des cantons
     */
    List<String> CANTONS = new ArrayList<>(Arrays.asList("AG", "AI", "AR", "BE", "BL", "BS", "FR", "GE", "GL", "GR", "JU", "LU", "NE", "NW", "OW", "SG", "SH", "SO", "SZ", "TG", "TI", "UR", "VD", "VS", "ZG", "ZH"));
}
