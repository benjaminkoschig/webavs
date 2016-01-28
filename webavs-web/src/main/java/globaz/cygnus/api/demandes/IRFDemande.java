/*
 * Créé le 17 décembre 2009
 */
package globaz.cygnus.api.demandes;

/**
 * @author jje
 */
public interface IRFDemande {

    // CONSTANTES POUR LE STATUT DE LA DEMANDE
    public static final String ACCEPTE = "66000040";

    public static final String ADAPTATION = "66000032";
    public static final String ANNULE = "66000024";
    public static final String AUTRES = "66001107";
    public static final String CALCULE = "66000021";
    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    public static final String CS_GROUPE_ETAT_DEMANDE = "RFETADEM";
    public static final String CS_GROUPE_GENRE_DE_SOIN = "RFGEDESO";

    public static final String CS_GROUPE_SOURCE_DEMANDE = "RFSRCDEM";
    public static final String CS_GROUPE_STATUT_DEMANDE = "RFSTADEM";

    public static final String CS_TYPE_TRANSPORT_CROIX_ROUGE = "66003107";
    public static final String CS_TYPE_TRANSPORT_PAR_LE_HOME = "66003105";
    public static final String CS_TYPE_TRANSPORT_PUBLIC = "66003104";
    public static final String CS_TYPE_TRANSPORT_STRUCTURE_DE_JOUR = "66003106";
    public static final String CS_TYPE_TRANSPORT_TAXI = "66003101";
    public static final String CS_TYPE_TRANSPORT_VOITURE_PRIVEE = "66003102";
    public static final String CS_TYPE_TRANSPORT_VOITURE_PRIVEE_AI = "66003103";

    public static final String CS_TYPE_VHC = "RFDFT16";

    public static final String DENTISTE = "66001106";

    // CONSTANTES POUR L'ETAT DE LA DEMANDE
    public static final String ENREGISTRE = "66000020";

    // CONSTANTES POUR LA SOURCE DE LA DEMANDE
    public static final String GESTIONNAIRE = "66000030";

    // CONSTANTES POUR LES GENRES DE SOIN
    public static final String HOSPITALISATION = "66001101";
    public static final String LABORATOIRE_D_ANALYSE = "66001105";

    public static final String MEDECIN = "66001103";
    public static final String PARTIELLEMENT_ACCEPTE = "66000042";
    public static final String PAYE = "66000023";

    public static final String PHARMACIE = "66001102";

    public static final String REFUSE = "66000041";
    public static final String SYSTEME = "66000031";
    public static final String TRANSPORT = "66001104";
    public static final String VALIDE = "66000022";
}
