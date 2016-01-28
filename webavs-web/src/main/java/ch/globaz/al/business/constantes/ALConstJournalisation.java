package ch.globaz.al.business.constantes;

/**
 * Interface utilis�es pour la journalisation du domaine AF
 * 
 * @author pta
 * 
 */
public interface ALConstJournalisation {
    public static final String CHANGEMENT_ETAT_DOSSIER = "Mise � jour dossier AF - Etat ";
    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_LETTRE_ENVOI = "Etat mis � jour par l'envoi lettre au poste de travail";

    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AFFILIE = "Etat mis � jour par : radiation de l'affili�";

    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AUTO = "Etat mis � jour par : radiation de masse de dossiers AF";

    public static final String CREATION_ETAT_DOSSIER = "Cr�ation du dossier AF - Etat ";

    /**
     * motif/libell� journalisation d'une d�cision �dit�
     */
    public static final String DECISION_MOTIF_JOURNALISATION = "DECISION EDITEE";

    /**
     * motif/libell� journalisation ouverture d'un dossier
     */
    public static final String DOSSIER_MOTIF_JOURNALISATION_OUVERTURE = "Ouverture du dossier journalisation";

    /**
     * motif/libell� journalisation pour Suppression d'un dossier AF
     */
    public static final String DOSSIER_MOTIF_JOURNALISATION_SUPPRIMER = "Suppression du dossier AF";

}
