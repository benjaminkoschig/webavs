/*
 * Créé le 11 novembre 2009
 */
package globaz.cygnus.api.dossiers;

/**
 * @author jje
 */
public interface IRFDossiers {

    public static final String CLOTURE = "66000002";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_GROUPE_ETAT_DOSSIER = "RFETADOS";
    public static final String CS_GROUPE_SOURCE_DOSSIER = "RFSRCDOS";

    // CONSTANTES POUR LA SOURCE DU DOSSIER
    public static final String GESTIONNAIRE = "66000010";

    // CONSTANTES POUR L'ETAT DU DOSSIER
    public static final String OUVERT = "66000001";
    public static final String SYSTEME = "66000011";

}
