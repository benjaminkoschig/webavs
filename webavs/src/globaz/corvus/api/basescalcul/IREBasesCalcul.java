package globaz.corvus.api.basescalcul;

/**
 * @author scr
 * 
 */
public interface IREBasesCalcul {

    public final static String CS_CALCUL_COMPARATIF_AI_FAVORABLE = "52813002";
    public final static String CS_CALCUL_COMPARATIF_SURVIVANT_FAVORABLE = "52813004";
    public final static String CS_CALCUL_COMPARATIF_VIEILLESSE_FAVORABLE = "52813003";
    public final static String CS_ETAT_ACTIF = "52826001";
    public final static String CS_ETAT_ANNULE = "52826002";

    // REETABC ://Etat de la base de calcul
    public final static String CS_GROUPE_ETAT_BASE_CALCUL = "REETABC";
    // RERESCOMP ://RESULTAT_CALCUL_COMPARATIF
    public static final String CS_GROUPE_RESULTAT_CALCUL_COMPARATIF = "RERESCOMP";
    // RETYPCCOMP ://TYPE_CALCUL_COMPARATIF
    public static final String CS_GROUPE_TYPE_CALCUL_COMPARATIF = "RETYPCCOMP";
    public final static String CS_PAS_CALCUL_COMPARATIF = "52813001";
    public final static String CS_TYPE_CALCUL_COMPARATIF_DECES_CONJOINT_R10 = "52814003";

    public final static String CS_TYPE_CALCUL_COMPARATIF_DECES_CONJOINT_R50 = "52814004";
    public final static String CS_TYPE_CALCUL_COMPARATIF_PAS_COMPARAISON = "52814001";
    public final static String CS_TYPE_CALCUL_COMPARATIF_REPRISE_AI_COMP_INVALIDITE_VIEILLESSE = "52814002";

}
