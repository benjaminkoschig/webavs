package globaz.osiris.print.itext;

import globaz.framework.printing.itext.fill.FWIImportParametre;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CAImpressionBulletinsSoldes_Param extends FWIImportParametre {
    public static final String L_INDICE = "L_INDICE";
    // // Header
    public static final String L_LIBELLE = "L_";
    public static final String L_PRENEUR = "L_PRENEUR";
    public static final String L_TAUX = "L_TAUX";
    public static final String L_TITRE1 = "L_TITRE1";
    public static final String L_TITRE2 = "L_TITRE2";
    public static final String L_TITRE3 = "L_TITRE3";
    public static final String P_ADR_CAISSE = "P_ADR_CAISSE";
    public static final String P_ADRESSE = "P_ADRESSE";
    public static final String P_ADRESSECOPY = "P_ADRESSECOPY";
    public static final String P_CENTIME = "P_CENTIME";
    public static final String P_COMPTE = "P_COMPTE";
    public static final String P_FACTURE_IMPNO = "P_FACTURE_IMPNO";

    public static final String P_FRANC = "P_FRANC";
    public static final String P_GERANCE = "P_GERANCE";

    public static final String P_INDICE = "P_INDICE";
    public static final String P_OCR = "P_OCR";
    public static final String P_PAR = "P_PAR";
    public static final String P_PRENEUR = "P_PRENEUR";
    public static final String P_REMARQUE = "P_REMARQUE";
    //
    // //Summarry
    public static final String P_TEXT = "P_TEXT";
    public static final String P_TEXT2 = "P_TEXT2";
    public static final String P_TEXT3 = "P_TEXT3";
    public static final String P_TOTAL = "V_TOTAL";
    public static final String P_VERSE = "P_VERSE";

    public static String getParamP(int pos) {
        return "P_" + pos;
    }
}
