package globaz.hercule.db;

/**
 * @author SCO
 * @since 14 juin 2010
 */
public interface ICEControleEmployeur {

    public static final String CATEGORIE_MASSE_0 = "0";
    public static final String CATEGORIE_MASSE_1 = "1";
    public static final String CATEGORIE_MASSE_1A = "1A";
    public static final String CATEGORIE_MASSE_1B = "1B";

    public static final String CATEGORIE_MASSE_2 = "2";
    public static final String CATEGORIE_MASSE_3 = "3";
    public static final String CATEGORIE_MASSE_4 = "4";
    // Catégorie de masse salariale
    public static final double CATEGORIE_MASSE_PALIER_0 = 50000;
    public static final double CATEGORIE_MASSE_PALIER_I = 100000;

    public static final double CATEGORIE_MASSE_PALIER_II = 500000;
    public static final double CATEGORIE_MASSE_PALIER_III = 5000000;
    // Note de collaboration
    public static final String COLLABORATION = "19300003";
    public static final String COLLABORATION_B = "851008";
    public static final String COLLABORATION_C = "851009";

    public static final String COLLABORATION_M = "851010";
    public static final String COLLABORATION_TM = "851011";
    // Note de criteres d'entreprise
    public static final String CRITERES_ENTREPRISE = "19300004";
    public static final String CRITERES_ENTREPRISE_AP = "851014";
    public static final String CRITERES_ENTREPRISE_P = "851013";

    public static final String CRITERES_ENTREPRISE_PP = "851012";
    public static final String CRITERES_ENTREPRISE_TP = "851015";
    // Note de dernier revision
    public static final String DERNIERE_REVISION = "19300001";
    public static final String DERNIERE_REVISION_PAS_DIFFERENCES = "851001";

    public static final String DERNIERE_REVISION_PROBLEME_IMPORTANT = "851003";
    public static final String DERNIERE_REVISION_PROBLEME_MINIME = "851002";
    public static final String PERIODE1 = "PERIODE1";
    public static final String PERIODE2 = "PERIODE2";
    public static final String PERIODE3 = "PERIODE3";
    // Note de qualite RH
    public static final String QUALITE_RH = "19300002";
    public static final String QUALITE_RH_AB = "851005";

    public static final String QUALITE_RH_M = "851006";
    public static final String QUALITE_RH_TB = "851004";
    public static final String QUALITE_RH_TM = "851007";
}
