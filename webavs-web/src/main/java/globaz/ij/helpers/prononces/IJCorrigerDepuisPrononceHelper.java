package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prestations.IJGrandeIJCalculee;
import globaz.ij.db.prestations.IJGrandeIJCalculeeManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prestations.IJPetiteIJCalculee;
import globaz.ij.db.prestations.IJPetiteIJCalculeeManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJCorrigerDepuisPrononceViewBean;
import globaz.prestation.helpers.PRAbstractHelper;
import ch.globaz.ij.business.services.exception.IJPrononceServiceException;

/**
 * 
 * @author rco Crée le 28.06.2013 Modifié le 30.09.2013
 * 
 */
public class IJCorrigerDepuisPrononceHelper extends PRAbstractHelper {

    /**
	 * 
	 */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        IJCorrigerDepuisPrononceViewBean vb = (IJCorrigerDepuisPrononceViewBean) viewBean;
        vb.retrieve();
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception, IJPrononceServiceException {

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) ((BSession) session).newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // Récupèration de notre viewBean
            IJCorrigerDepuisPrononceViewBean pdViewBean = (IJCorrigerDepuisPrononceViewBean) viewBean;
            BSession myBSession = (BSession) session;

            corrigePrononceDepuis(transaction, pdViewBean, myBSession);

            if (transaction.hasErrors()) {
                transaction.rollback();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage("Erreur lors de l'annulation de la correction du prononcé. La transcation a des erreurs : "
                        + transaction.getErrors().toString());
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    private void copieAllocFraisGardeOuAIT(BTransaction transaction, BSession myBSession, IJPrononce prononce1,
            IJPrononce prononce2) throws Exception {
        String oldIdIJCalculee;
        String newIdIJCalculee;
        { // On a une AIT ou une Alloc frais de garde
            IJIJCalculeeManager ijCalculeeManager = new IJIJCalculeeManager();
            ijCalculeeManager.setSession(myBSession);
            ijCalculeeManager.setForIdPrononce(prononce1.getIdPrononce());
            ijCalculeeManager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < ijCalculeeManager.size(); i++) {
                IJIJCalculee ijCalculee = (IJIJCalculee) ijCalculeeManager.getEntity(i);

                oldIdIJCalculee = ijCalculee.getIdIJCalculee();

                ijCalculee.setIdPrononce(prononce2.getIdPrononce());
                ijCalculee.setId(null);
                ijCalculee.setIdIJCalculee(null);
                ijCalculee.add(transaction);

                newIdIJCalculee = ijCalculee.getIdIJCalculee();
                copieIJIndemniteJournaliere(myBSession, oldIdIJCalculee, newIdIJCalculee, transaction);
            }
        }
    }

    private void copieGrandeIJ(BTransaction transaction, BSession myBSession, IJPrononce prononce1, IJPrononce prononce2)
            throws Exception {
        String oldIdIJCalculee;
        String newIdIJCalculee;
        {
            IJGrandeIJCalculeeManager grandeIJCalculeeManager = new IJGrandeIJCalculeeManager();
            grandeIJCalculeeManager.setSession(myBSession);
            grandeIJCalculeeManager.setForIdPrononce(prononce1.getIdPrononce());
            grandeIJCalculeeManager.find();

            for (int i = 0; i < grandeIJCalculeeManager.size(); i++) {
                IJGrandeIJCalculee grandeIJCalculee = (IJGrandeIJCalculee) grandeIJCalculeeManager.getEntity(i);

                oldIdIJCalculee = grandeIJCalculee.getIdIJCalculee();

                grandeIJCalculee.setId(null);
                grandeIJCalculee.setIdPrononce(prononce2.getIdPrononce());
                grandeIJCalculee.setIdIJCalculee(null);
                grandeIJCalculee.add(transaction);

                newIdIJCalculee = grandeIJCalculee.getIdIJCalculee();
                copieIJIndemniteJournaliere(myBSession, oldIdIJCalculee, newIdIJCalculee, transaction);
            }
        }
    }

    /**
     * 
     * @param myBSession
     * @param oldIdIJCalculee
     * @param idIJCalculee
     * @param transaction
     * @throws Exception
     */
    private void copieIJIndemniteJournaliere(BSession myBSession, String oldIdIJCalculee, String idIJCalculee,
            BITransaction transaction) throws Exception {
        IJIndemniteJournaliereManager ijIndemniteJournaliereManager = new IJIndemniteJournaliereManager();
        ijIndemniteJournaliereManager.setSession(myBSession);
        ijIndemniteJournaliereManager.setForIdIJCalculee(oldIdIJCalculee);

        ijIndemniteJournaliereManager.find();

        for (int i = 0; i < ijIndemniteJournaliereManager.size(); i++) {
            IJIndemniteJournaliere ijIndemniteJournaliere = (IJIndemniteJournaliere) ijIndemniteJournaliereManager
                    .getEntity(i);

            ijIndemniteJournaliere.setId(null);
            ijIndemniteJournaliere.setIdIndemniteJournaliere(null);
            ijIndemniteJournaliere.setIdIJCalculee(idIJCalculee);
            ijIndemniteJournaliere.add(transaction);

        }
    }

    private void copiePetiteIJ(BTransaction transaction, BSession myBSession, IJPrononce prononce1, IJPrononce prononce2)
            throws Exception {
        String oldIdIJCalculee;
        String newIdIJCalculee;
        {
            IJPetiteIJCalculeeManager ijPetiteIJCalculeeManager = new IJPetiteIJCalculeeManager();
            ijPetiteIJCalculeeManager.setSession(myBSession);
            ijPetiteIJCalculeeManager.setForIdPrononce(prononce1.getIdPrononce());
            ijPetiteIJCalculeeManager.find();

            for (int i = 0; i < ijPetiteIJCalculeeManager.size(); i++) {
                IJPetiteIJCalculee petiteIJCalculee = (IJPetiteIJCalculee) ijPetiteIJCalculeeManager.getEntity(i);

                oldIdIJCalculee = petiteIJCalculee.getIdIJCalculee();

                petiteIJCalculee.setId(null);
                petiteIJCalculee.setIdPrononce(prononce2.getIdPrononce());
                petiteIJCalculee.setIdIJCalculee(null);
                petiteIJCalculee.add(transaction);

                newIdIJCalculee = petiteIJCalculee.getIdIJCalculee();
                copieIJIndemniteJournaliere(myBSession, oldIdIJCalculee, newIdIJCalculee, transaction);
            }
        }
    }

    /**
     * 
     * @param transaction
     * @param pdViewBean
     * @param myBSession
     * @throws Exception
     * @throws JAException
     * @throws IJPrononceServiceException
     */
    private void corrigePrononceDepuis(BTransaction transaction, IJCorrigerDepuisPrononceViewBean pdViewBean,
            BSession myBSession) throws Exception, JAException, IJPrononceServiceException {
        // Chargement de notre prononcé
        IJPrononce prononce1 = IJPrononce.loadPrononce(myBSession, transaction, pdViewBean.getIdPrononce(),
                pdViewBean.getCsTypeIJ());

        IJCorrigerDepuisPrononce corrigerDepuisPrononce = new IJCorrigerDepuisPrononce();

        // Récupèration de nos bases d'indemnité
        IJBaseIndemnisationManager baseIndemnisationManager = recuperationBasesIndemnites(myBSession,
                prononce1.getIdPrononce(), transaction);

        if (prononce1.isNew()) {
            String message = "prononce1 is New. Voir class IJCorrigerDepuisPrononceHelper";
            throw new IllegalArgumentException(message);
        }

        // Si les dates correspondent alors on commence le traitement
        if ((corrigerDepuisPrononce.verifierDateCorrectionDansPrononce(pdViewBean.getDateCorrection(),
                prononce1.getDateDebutPrononce(), prononce1.getDateFinPrononce()))
                && corrigerDepuisPrononce.verifierDateCorrectionDansBaseIndemnite(pdViewBean.getDateCorrection(),
                        baseIndemnisationManager)) {

            // Copie du prononce et de ses fils selon le type d'IJ
            IJPrononce prononce2 = IJPrononceRegles.creerCopie(myBSession, transaction, prononce1);

            corrigerDepuisPrononce.deplaceBaseIndemnite(baseIndemnisationManager, prononce1, prononce2,
                    pdViewBean.getDateCorrection());

            String typeIJ = prononce1.getCsTypeIJ();
            String oldIdIJCalculee = "";
            String newIdIJCalculee = "";

            if (typeIJ.equals(IIJPrononce.CS_GRANDE_IJ)) {
                copieGrandeIJ(transaction, myBSession, prononce1, prononce2);
            } else if (typeIJ.equals(IIJPrononce.CS_PETITE_IJ)) {
                copiePetiteIJ(transaction, myBSession, prononce1, prononce2);
            } else {
                copieAllocFraisGardeOuAIT(transaction, myBSession, prononce1, prononce2);
            }

            prononce1.update(transaction);
            prononce2.update(transaction);

            IJPrononce prononce2prime = IJPrononceRegles.creerCorrection(myBSession, transaction, prononce2);
            prononce2prime.setCsEtat(IIJPrononce.CS_ATTENTE);
            prononce2prime.setIdParentCorrigeDepuis(null);

            prononce2prime.update(transaction);

        } else {
            throw new IJPrononceServiceException(IJCorrigerDepuisPrononceErreur.composerMessageErreur(
                    corrigerDepuisPrononce.getTypeErreur(),
                    myBSession.getLabel(corrigerDepuisPrononce.getTypeErreur().getLibelleErreur())));
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        return deleguerExecute(viewBean, action, session);
    }

    /**
     * 
     * @param myBSession
     * @param idPrononce
     * @param transaction
     * @return
     * @throws Exception
     */
    private IJBaseIndemnisationManager recuperationBasesIndemnites(BSession myBSession, String idPrononce,
            BITransaction transaction) throws Exception {

        IJBaseIndemnisationManager baseIndemnisationManager = new IJBaseIndemnisationManager();
        baseIndemnisationManager.setSession(myBSession);
        baseIndemnisationManager.setForIdPrononce(idPrononce);
        baseIndemnisationManager.setOrderBy(IJBaseIndemnisation.FIELDNAME_DATEDEBUTPERIODE);

        baseIndemnisationManager.find(transaction, BManager.SIZE_NOLIMIT);

        return baseIndemnisationManager;
    }
}
