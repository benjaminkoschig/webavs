package globaz.osiris.externe;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.osiris.api.APIGestionSpecifiqueAFExterne;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CASection;
import java.util.ArrayList;

/**
 * @see APIGestionSpecifiqueAFExterne
 * @author DDA
 * 
 */
public class CAGestionSpecifiqueAFExterne implements APIGestionSpecifiqueAFExterne {

    /**
     * @see APIGestionSpecifiqueAFExterne#getMontantSectionEnCours(BSession, String, String)
     */
    @Override
    public FWCurrency getMontantSectionEnCours(BSession session, String idJournal, String idSection) throws Exception {
        FWCurrency result = getSoldeSection(session, idSection);
        result.add(getTotalEcrituresAndVersements(session, idJournal, idSection));

        return result;
    }

    /**
     * Return le solde actuel de la section.
     * 
     * @param session
     * @param idSection
     * @return
     * @throws Exception
     */
    private FWCurrency getSoldeSection(BSession session, String idSection) throws Exception {
        CASection section = new CASection();
        section.setSession(session);

        section.setIdSection(idSection);

        section.retrieve();

        if (section.hasErrors() || section.isNew()) {
            throw new Exception("Section non résolue");
        }

        return section.getSoldeToCurrency();
    }

    /**
     * Return le total des écritures et versements du journal en état non inactifs.
     * 
     * @param session
     * @param idJournal
     * @param idSection
     * @return
     * @throws Exception
     */
    private FWCurrency getTotalEcrituresAndVersements(BSession session, String idJournal, String idSection)
            throws Exception {
        CAOperationManager manager = new CAOperationManager();
        manager.setSession(session);

        manager.setForIdJournal(idJournal);
        manager.setForIdSection(idSection);

        ArrayList idTypeOperationLike = new ArrayList();
        idTypeOperationLike.add(APIOperation.CAECRITURE);
        idTypeOperationLike.add(APIOperation.CAOPERATIONORDREVERSEMENT);
        manager.setForIdTypeOperationLikeIn(idTypeOperationLike);

        ArrayList etatNotIn = new ArrayList();
        etatNotIn.add(APIOperation.ETAT_INACTIF);
        manager.setForEtatNotIn(etatNotIn);

        return new FWCurrency((manager.getSum(CAOperation.FIELD_MONTANT)).doubleValue());
    }
}
