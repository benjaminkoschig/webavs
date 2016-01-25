package ch.globaz.al.business.constantes;

/**
 * Interface utilisées pour la journalisation du domaine AF
 * 
 * @author pta
 * 
 */
public interface ALConstJournalisation {
    public static final String CHANGEMENT_ETAT_DOSSIER = "Mise à jour dossier AF - Etat ";
    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_LETTRE_ENVOI = "Etat mis à jour par l'envoi lettre au poste de travail";

    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AFFILIE = "Etat mis à jour par : radiation de l'affilié";

    public static final String CHANGEMENT_ETAT_DOSSIER_REMARQUE_RADIATION_AUTO = "Etat mis à jour par : radiation de masse de dossiers AF";

    public static final String CREATION_ETAT_DOSSIER = "Création du dossier AF - Etat ";

    /**
     * motif/libellé journalisation d'une décision édité
     */
    public static final String DECISION_MOTIF_JOURNALISATION = "DECISION EDITEE";

    /**
     * motif/libellé journalisation ouverture d'un dossier
     */
    public static final String DOSSIER_MOTIF_JOURNALISATION_OUVERTURE = "Ouverture du dossier journalisation";

    /**
     * motif/libellé journalisation pour Suppression d'un dossier AF
     */
    public static final String DOSSIER_MOTIF_JOURNALISATION_SUPPRIMER = "Suppression du dossier AF";

}
