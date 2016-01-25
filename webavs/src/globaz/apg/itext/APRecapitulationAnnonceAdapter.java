package globaz.apg.itext;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAbstractListeRecapitulationAnnoncesManager;
import globaz.apg.db.annonces.APRecapitulationAnnonceManager;
import globaz.globall.db.BSession;

/**
 * Agr�gateur de donn�e pour les annonces RAPG (apr�s septembre 2012)
 * 
 * @author PBA
 * @author VRE
 */
public class APRecapitulationAnnonceAdapter extends APAbstractRecapitulationAnnonceAdapter {

    public APRecapitulationAnnonceAdapter(BSession session, String forMoisAnneeComptable) {
        super(session, forMoisAnneeComptable);
    }

    @Override
    protected String getCodeUtilisateurPourCodeSysteme(String codeSysteme) throws Exception {
        if (IAPAnnonce.CS_DEMANDE_ALLOCATION.equals(codeSysteme)) {
            return APAnnoncesRapgService.subMessageType1;
        }
        if (IAPAnnonce.CS_PAIEMENT_RETROACTIF.equals(codeSysteme)) {
            return APAnnoncesRapgService.subMessageType3;
        }
        if (IAPAnnonce.CS_RESTITUTION.equals(codeSysteme)) {
            return APAnnoncesRapgService.subMessageType4;
        }
        // reste � faire les duplicata (non-impl�ment� dans les subMessage)
        return null;
    }

    @Override
    protected APAbstractListeRecapitulationAnnoncesManager getManager() {
        return new APRecapitulationAnnonceManager();
    }
}
