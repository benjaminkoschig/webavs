package ch.globaz.al.businessimpl.rafam.handlers;

public enum ModificationAction {
    
    /** Traitement standard, pas d'action particuli�re � ex�cuter */
    STANDARD,
    /** Annulation de l'annonce existante puis cr�ation d'une nouvelle annonce */
    ANNULATION_ET_CREATION,
    /**
     * mise � jour de tous les NSS des annonces correspondant au record number de la derni�re annonce
     * puis poursuite du traitement normal
     */
    UPDATE_NSS_ET_STANDARD
}
