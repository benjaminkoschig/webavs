/*
 * Créé le 3 oct. 05
 */
package globaz.ij.api.annonces;

/**
 * @author dvh
 */
public interface IIJAnnonce {

    public static final String CS_ENVOYEE = "52417003";
    public static final String CS_ERRONEE = "52417004";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_GROUPE_ETAT_ANNONCE = "IJETATANN";
    public static final String CS_OUVERT = "52417001";
    public static final String CS_VALIDE = "52417002";
    /** Comprend les etats ouvert, valide et errone */
    public static final String ETATS_NON_ENVOYE = "52417005";

    public static final String VERSEMENT_ASSURE = "1";
    public static final String VERSEMENT_EMPLOYEUR_CENTRE_READAPTATION = "2";
    public static final String VERSEMENT_REPARTI = "3";

}
