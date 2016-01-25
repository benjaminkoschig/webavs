/*
 * Created on Mai 10, 2005
 */
package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.fill.FWIImportParametre;

/**
 * @author kurkus
 */
public class CAILettrePlanRecouvParam extends FWIImportParametre {

    public static final String ADRESSE = "ADRESSE";
    public static final String AFFILIE = "P_AFFILIE";
    public static final String COL1 = "COL_1";
    public static final String COL2 = "COL_2";
    public static final String COL3 = "COL_3";
    public static final String COL4 = "COL_4";
    public static final String COL5 = "COL_5";
    public static final String COL6 = "COL_6";
    public static final String COMPANYNAME = "P_COMPANYNAME";
    public static final String D1 = "D1";
    public static final String D2 = "D2";
    public static final String DATE = "P_DATE";
    public static final String DATEREF = "P_DATE_REF";
    public static final String ENTETE = "P_ENTETE";
    public static final String EXERCICE = "P_EXERCICE";

    public static final String FRAIS1 = "FRAIS1";
    public static final String HEADER_BLANK1 = "headerBlank1";
    public static final String HEADER_BLANK2 = "headerBlank2";
    public static final String HEADER1 = "HEADER1";
    public static final String HEADER2 = "HEADER2";
    public static final String LIBELLE_DATE_ECHEANCE = "P_LIBELLE_DATE_ECHEANCE";

    public static final String LIBELLE_DATE_PAIEMENT = "P_LIBELLE_DATE_PAIEMENT";
    public static final String LIBELLE_DATE_SECTION = "P_LIBELLE_DATE_SECTION";
    public static final String LIBELLE_MNT_ECHEANCE = "P_LIBELLE_MNT_ECHEANCE";

    public static final String LIBELLE_MNT_PAIEMENT = "P_LIBELLE_MNT_PAIEMENT";
    public static final String LIBELLE_SECTION = "P_LIBELLE_SECTION";
    public static final String LIBELLE_SOLDE = "P_LIBELLE_SOLDE";
    public static final String LIBELLE_SOLDE_RESIDUEL = "P_LIBELLE_SOLDE_RESIDUEL";

    public static final String LIBELLE_TITRE = "P_LIBELLE_TITRE";
    public static final String M1 = "M1";
    public static final String NOAFF = "NOAFF";
    public static final String P_ADRESSE = "P_ADRESSE";
    public static final String P_ADRESSECOPY = "P_ADRESSECOPY";
    public static final String P_ANNEXE = "P_ANNEXE";
    public static final String P_CENTIME = "P_CENTIME";
    public static final String P_COMPTE = "P_COMPTE";
    // Paramètres pour le document : CAIEcheancierLettreDecision.jrxml
    // Utilisé dans : CAILettrePlanRecouvDecision.java
    public static final String P_CONCERNE = "P_CONCERNE";

    // Header
    public static final String P_FACTURE_IMPNO = "P_FACTURE_IMPNO";
    public static final String P_FRANC = "P_FRANC";
    public static final String P_OCR = "P_OCR";
    public static final String P_PAR = "P_PAR";
    public static final String P_REMARQUE = "P_REMARQUE";
    // Summarry
    public static final String P_TEXT = "P_TEXT";
    public static final String P_TEXT_CORPS = "P_TEXT_CORPS";
    // Paramètres pour le document : CAIEcheancierVoiesDroit.jrxml
    public static final String P_TEXTVD = "P_TEXTVD";
    public static final String P_TOTAL = "P_TOTAL";

    public static final String P_VERSE = "P_VERSE";
    public static final String P1 = "P1";

    public static final String PAGE = "P_PAGE";
    public static final String PAGENUMBER = "P_PAGE_NUMBER";
    public static final String PERIODE = "P_PERIODE";
    public static final String SECTION = "P_SECTION";
    public static final String T10 = "T10";
    public static final String T11 = "T11";
    public static final String T7 = "T7";
    public static final String T8 = "T8";
    public static final String TEXT1 = "TEXT1";
    public static final String TEXT2 = "TEXT2";
    public static final String TEXT3 = "TEXT3";

    public static final String TEXT4 = "TEXT4";
    public static final String TEXT5 = "TEXT5";
    public static final String TEXT6 = "TEXT6";

    public static final String TOTALPAGENUMBER = "P_TOTAL_PAGE_NUMBER";

    public static final String TRICA = "P_TRI_CA";
    public static final String TYPEPLAN = "P_TYPE_PLAN";

    public static String getParamP(int pos) {
        return "P_" + pos;
    }
}
