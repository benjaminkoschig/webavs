package globaz.osiris.api;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;

/**
 * Impl�mentation de m�thodes sp�fiques pour le module AF.
 * 
 * @author DDA
 * 
 */
public interface APIGestionSpecifiqueAFExterne {

    /**
     * Return le solde pour une section en cours de traitement. Au solde de la section sera ajout� le total des
     * �critures et versements d'un journal. (Etat des op�rations ouvertes �galement somm�es)
     * 
     * @param session
     * @param idJournal
     * @param idSection
     * @return
     * @throws Exception
     */
    public FWCurrency getMontantSectionEnCours(BSession session, String idJournal, String idSection) throws Exception;
}
