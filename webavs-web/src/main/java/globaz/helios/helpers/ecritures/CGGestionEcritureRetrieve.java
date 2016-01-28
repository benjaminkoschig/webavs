package globaz.helios.helpers.ecritures;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGEnteteEcritureViewBean;
import globaz.helios.db.ecritures.CGGestionEcritureViewBean;
import globaz.helios.helpers.ecritures.utils.CGGestionEcritureUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * Class permettant de retrouver les écritures depuis la DB afin de composer un objet contenant entête et écritures
 * liées.
 * 
 * @author DDA
 * 
 */
public class CGGestionEcritureRetrieve {

    /**
     * Recherche les écritures pour l'entête et l'idjournal.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    private static void fillWithEcritures(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        boolean montantEtrange = false;
        boolean centreCharge = false;
        CGEcritureListViewBean manager = new CGEcritureListViewBean();
        manager.setSession((BSession) session);

        manager.setForIdEnteteEcriture(ecritures.getIdEnteteEcriture());
        manager.setForIdJournal(ecritures.getIdJournal());

        manager.find(BManager.SIZE_NOLIMIT);

        String remarque = "";
        ArrayList attachedEcritures = new ArrayList();
        for (int i = 0; i < manager.size(); i++) {
            CGEcritureViewBean ecriture = (CGEcritureViewBean) manager.get(i);
            ecriture.setIdExterneCompte(CGGestionEcritureUtils.getIdExterneCompte(session, ecriture));
            remarque = ((CGEcritureViewBean) manager.get(i)).getRemarque();

            if (!JadeStringUtil.isBlankOrZero(ecriture.getCoursMonnaie())) {
                montantEtrange = true;
            }
            if (!JadeStringUtil.isBlankOrZero(ecriture.getIdCentreCharge())) {
                centreCharge = true;
            }

            attachedEcritures.add(ecriture);

        }

        if (attachedEcritures.isEmpty()) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_AUCUNE_ECRITURES_TROUVEES"));
        }

        ecritures.setShowRows(manager.size());

        if (ecritures.isDetteAvoir() || !ecritures.isJournalEditable()) {
            ecritures.setMaxRows(manager.size());
        }

        ecritures.setMontantEtrangerAffiche(montantEtrange);
        ecritures.setCentreChargeAffiche(centreCharge);
        ecritures.setEcritures(attachedEcritures);
        ecritures.setRemarque(remarque);
    }

    /**
     * Mise à jour de l'entête.
     * 
     * @param session
     * @param ecritures
     * @throws Exception
     */
    private static void fillWithEntete(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        CGEnteteEcritureViewBean entete = CGGestionEcritureUtils.getEntete(session, null,
                ecritures.getIdEnteteEcriture());

        ecritures.setIdJournal(entete.getIdJournal());
        ecritures.setPiece(entete.getPiece());
        ecritures.setDateValeur(entete.getDateValeur());

        ecritures.setIdFournisseur(entete.getIdFournisseur());
        ecritures.setIdSection(entete.getIdSection());
    }

    /**
     * Recharge les écritures pour affichage écran.
     * 
     * @param session
     * @param viewBean
     * @throws Exception
     */
    public static void retrieveEcritures(BISession session, FWViewBeanInterface viewBean) throws Exception {
        CGGestionEcritureViewBean ecritures = (CGGestionEcritureViewBean) viewBean;

        validate(session, ecritures);

        fillWithEntete(session, ecritures);

        fillWithEcritures(session, ecritures);
    }

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param ecritures
     */
    private static void validate(BISession session, CGGestionEcritureViewBean ecritures) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(ecritures.getIdEnteteEcriture())) {
            throw new Exception(((BSession) session).getLabel("GESTION_ECRITURES_ENTETE_NON_RESOLUE"));
        }

    }
}
