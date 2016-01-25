package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;

/**
 * Implémentation de méthodes spéfiques pour le module AF.
 * 
 * @author DDA
 * 
 */
public interface APIGestionSpecifiqueAFExterne {

    /**
     * Return le solde pour une section en cours de traitement. Au solde de la section sera ajouté le total des
     * écritures et versements d'un journal. (Etat des opérations ouvertes également sommées)
     * 
     * @param session
     * @param idJournal
     * @param idSection
     * @return
     * @throws Exception
     */
    public FWCurrency getMontantSectionEnCours(BSession session, String idJournal, String idSection) throws Exception;
}
