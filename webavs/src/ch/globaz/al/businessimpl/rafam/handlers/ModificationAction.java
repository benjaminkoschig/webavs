package ch.globaz.al.businessimpl.rafam.handlers;

public enum ModificationAction {
    
    /** Traitement standard, pas d'action particulière à exécuter */
    STANDARD,
    /** Annulation de l'annonce existante puis création d'une nouvelle annonce */
    ANNULATION_ET_CREATION,
    /**
     * mise à jour de tous les NSS des annonces correspondant au record number de la dernière annonce
     * puis poursuite du traitement normal
     */
    UPDATE_NSS_ET_STANDARD
}
