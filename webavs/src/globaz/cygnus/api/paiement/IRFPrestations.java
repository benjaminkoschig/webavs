/*
 * Créé le 15 avril 2010
 */
package globaz.cygnus.api.paiement;

/**
 * @author fha
 */
public interface IRFPrestations {

    public static final String CS_ETAT_PRESTATION_ERREUR = "66002503";
    public static final String CS_ETAT_PRESTATION_GROUPE = "RFETPRE";
    public static final String CS_ETAT_PRESTATION_MIS_EN_LOT = "66002502";
    public static final String CS_ETAT_PRESTATION_VALIDE = "66002501";

    public static final String CS_GROUPE_SOURCE_RFMACCORDEES = "RFACCSRC";
    public static final String CS_SOURCE_RFACCORDEES_AUTRE = "66003204";
    public static final String CS_SOURCE_RFACCORDEES_FRQP = "66003202";
    public static final String CS_SOURCE_RFACCORDEES_REGIME = "66003201";
    public static final String CS_SOURCE_RFACCORDEES_REGIME_DIABETIQUE = "66003203";

}
