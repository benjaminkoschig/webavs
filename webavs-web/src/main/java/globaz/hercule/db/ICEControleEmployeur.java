/*
 * Globaz SA.
 */
package globaz.hercule.db;

/**
 * @author SCO
 * @since 14 juin 2010
 */
public interface ICEControleEmployeur {

    public String CATEGORIE_MASSE_0 = "0";
    public String CATEGORIE_MASSE_1 = "1";
    public String CATEGORIE_MASSE_1A = "1A";
    public String CATEGORIE_MASSE_1B = "1B";

    public String CATEGORIE_MASSE_2 = "2";
    public String CATEGORIE_MASSE_3 = "3";
    public String CATEGORIE_MASSE_4 = "4";
    // Catégorie de masse salariale
    public double CATEGORIE_MASSE_PALIER_0 = 50000;
    public double CATEGORIE_MASSE_PALIER_I = 100000;

    public double CATEGORIE_MASSE_PALIER_II = 500000;
    public double CATEGORIE_MASSE_PALIER_III = 5000000;
    // Note de collaboration
    public String COLLABORATION = "19300003";
    public String COLLABORATION_B = "851008";
    public String COLLABORATION_C = "851009";

    public String COLLABORATION_M = "851010";
    public String COLLABORATION_TM = "851011";
    // Note de criteres d'entreprise
    public String CRITERES_ENTREPRISE = "19300004";
    public String CRITERES_ENTREPRISE_AP = "851014";
    public String CRITERES_ENTREPRISE_P = "851013";

    public String CRITERES_ENTREPRISE_PP = "851012";
    public String CRITERES_ENTREPRISE_TP = "851015";
    // Note de dernier revision
    public String DERNIERE_REVISION = "19300001";
    public String DERNIERE_REVISION_PAS_DIFFERENCES = "851001";

    public String DERNIERE_REVISION_PROBLEME_IMPORTANT = "851003";
    public String DERNIERE_REVISION_PROBLEME_MINIME = "851002";
    public String PERIODE1 = "PERIODE1";
    public String PERIODE2 = "PERIODE2";
    public String PERIODE3 = "PERIODE3";
    // Note de qualite RH
    public String QUALITE_RH = "19300002";
    public String QUALITE_RH_AB = "851005";

    public String QUALITE_RH_M = "851006";
    public String QUALITE_RH_TB = "851004";
    public String QUALITE_RH_TM = "851007";
}
