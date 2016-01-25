package globaz.musca.itext;

/**
 * Insérez la description du type ici. Date de création : (26.02.2003 17:34:54)
 * 
 * @author: Administrator
 */
public class FAImpressionFacture_Param extends FAImpressionFacturation_Param {

    public static final String L_INDICE = "L_INDICE";
    // d'afacts dans un
    // décompte
    // Header
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

    public static final String P_DECIMAL = "P_DECIMAL";

    public static final String P_FACTURE_IMPNO = "P_FACTURE_IMPNO";
    public static final String P_FRANC = "P_FRANC";

    public static final String P_GERANCE = "P_GERANCE";
    /*
     * OCA - Permet de choisir si l'on veut afficher le header sur chaque page. ceci est utile lorsque les ligne de
     * facture prennent plus d'une page, certain client veulent que les ligne commence dès le début de la page suivante,
     * d'autre, comme la FER ont du papier préimprimé et doivent donc répéter le header sur chaque page.
     * 
     * Dans le modèle de docuement jasper, ce paramètre est lié a l'expression suivante
     * 
     * qui affiche le header sur la première page dans tous les cas, et sur les pages suivantes de manière
     * conditionelle. new Boolean( ($V{PAGE_NUMBER}.intValue()==1) || $P{P_HEADER_EACH_PAGE}.booleanValue() )
     * 
     * Le paramétrage se fait par la méthode wantHeaderOnEachPage(), qui cherche si il existe une property
     * headerOnEachPage (dans le fichier COMMON.properties par ex.)
     */
    public static final String P_HEADER_EACH_PAGE = "P_HEADER_EACH_PAGE";
    public static final String P_INDICE = "P_INDICE";
    public static final String P_LIBELLE = "P_LIBELLE";
    public static final String P_OCR = "P_OCR";

    public static final String P_PAR = "P_PAR";
    public static final String P_PRENEUR = "P_PRENEUR";
    public static final String P_REMARQUE = "P_REMARQUE";
    // Summarry
    public static final String P_TEXT = "P_TEXT";
    public static final String P_TEXT1 = "P_TEXT1";
    public static final String P_TEXT2 = "P_TEXT2";
    public static final String P_TEXT3 = "P_TEXT3";
    public static final String P_TOTAL = "V_TOTAL";
    // General
    public static final String P_TOTAL_ROW = "P_TOTAL_ROW"; // nombre total
    public static final String P_VERSE = "P_VERSE";
    public static final String P_ZERONEG = "P_ZERONEG";
    public static final String P_ZEROPOS = "P_ZEROPOS";
}
