package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prestations.IJIJCalculee;
import globaz.ij.db.prestations.IJIJCalculeeManager;
import globaz.ij.db.prestations.IJIndemniteJournaliere;
import globaz.ij.db.prestations.IJIndemniteJournaliereManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceManager;
import globaz.ij.vb.prononces.IJAnnulerCorrigerDepuisPrononceViewBean;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.ArrayList;

/**
 * 
 * @author rco Crée le 09.07.2013 Modifié le 04.10.2013
 * 
 */
public class IJAnnulerCorrigerDepuisPrononceHelper extends PRAbstractHelper {

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        BTransaction transaction = null;

        try {
            // Création de la transaction
            BSession myBSession = (BSession) session;
            transaction = (BTransaction) myBSession.newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // Récupèration de notre viewBean
            IJAnnulerCorrigerDepuisPrononceViewBean pdViewBean = (IJAnnulerCorrigerDepuisPrononceViewBean) viewBean;

            // Chargement de notre prononcé2
            IJPrononce prononce2 = IJPrononce.loadPrononce(myBSession, transaction, pdViewBean.getIdPrononce(),
                    pdViewBean.getCsTypeIJ());

            // Chargement de notre prononcé2prime
            IJPrononce prononce2prime = null;
            if (prononce2.loadEnfantCorrection(transaction) != null) {
                prononce2prime = IJPrononce.loadPrononce((BSession) session, transaction, prononce2
                        .loadEnfantCorrection(transaction).getId(), pdViewBean.getCsTypeIJ());
            }

            // Chargement de notre prononcé1
            IJPrononce prononce1 = IJPrononce.loadPrononce(myBSession, transaction,
                    prononce2.getIdParentCorrigeDepuis(), pdViewBean.getCsTypeIJ());

            // Ajuster la date de fin du prononce1
            ajusterDatePrononce(transaction, prononce2, prononce1);

            // Traitement des bases d'indemenité
            deplacementBasesIndemnites(transaction, myBSession, prononce2, prononce2prime, prononce1);

            // Effacement des bases IJCalculés du prononcé2
            supprimerBasesIJCalculeDuPrononce(transaction, myBSession, prononce2);
            prononce2.delete(transaction);

            // Effacement des bases IJCalculés du prononcé2prime
            if ((prononce2prime != null) && (!prononce2prime.isNew())) {

                supprimerBasesIJCalculeDuPrononce(transaction, myBSession, prononce2prime);
                prononce2prime.delete(transaction);
            }

            // Vérification de la transaction
            if (transaction.hasErrors()) {
                transaction.rollback();
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage("Erreur lors de l'annulation de la correction du prononcé. La transcation a des erreurs : "
                        + transaction.getErrors().toString());
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage("Erreur lors de l'annulation de la correction du prononcé. " + e.getMessage());
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);

        BTransaction transaction = null;

        try {
            // Création de la transaction
            BSession myBSession = (BSession) session;
            transaction = (BTransaction) myBSession.newTransaction();

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            // Récupèration de notre viewBean
            IJAnnulerCorrigerDepuisPrononceViewBean pdViewBean = (IJAnnulerCorrigerDepuisPrononceViewBean) viewBean;

            // Chargement de notre prononcé à annuler
            IJPrononce prononce = IJPrononce.loadPrononce(myBSession, transaction, pdViewBean.getIdPrononce(),
                    pdViewBean.getCsTypeIJ());

            boolean peutOnAnnuler = isAnnulerPossible(transaction, myBSession, pdViewBean, prononce);

            // Permet d'afficher un gros message en rouge
            pdViewBean.setIsAnnulerPossible(peutOnAnnuler);

            // ----------------------------------
            // Vérification de la transaction
            if (transaction.hasErrors()) {
                transaction.rollback();
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage("Erreur lors de l'annulation de la correction du prononcé. La transcation a des erreurs : "
                        + transaction.getErrors().toString());
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                viewBean.setMsgType(FWMessage.ERREUR);
                viewBean.setMessage("Erreur lors de l'annulation de la correction du prononcé. " + e.getMessage());
            }
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }

        }
    }

    /**
     * 
     * @param transaction
     * @param prononceReference
     * @param prononceAAjuster
     * @throws JAException
     * @throws Exception
     */
    private void ajusterDatePrononce(BTransaction transaction, IJPrononce prononceReference, IJPrononce prononceAAjuster)
            throws JAException, Exception {
        JACalendar myJACalendar = new JACalendarGregorian();
        if (myJACalendar.compare(prononceAAjuster.getDateFinPrononce(), prononceReference.getDateFinPrononce()) == JACalendar.COMPARE_FIRSTLOWER) {
            prononceAAjuster.setDateFinPrononce(prononceReference.getDateFinPrononce());
            prononceAAjuster.update(transaction);
        }
    }

    /**
     * 
     * @param myBSession
     * @param newIdIJCalculee
     * @param transaction
     * @throws Exception
     */
    private void deleteIJIndemniteJournaliere(BSession myBSession, String newIdIJCalculee, BTransaction transaction)
            throws Exception {
        IJIndemniteJournaliereManager ijIndemniteJournaliereManager = new IJIndemniteJournaliereManager();
        ijIndemniteJournaliereManager.setSession(myBSession);
        ijIndemniteJournaliereManager.setForIdIJCalculee(newIdIJCalculee);

        ijIndemniteJournaliereManager.find();

        for (int i = 0; i < ijIndemniteJournaliereManager.size(); i++) {
            IJIndemniteJournaliere ijIndemniteJournaliere = (IJIndemniteJournaliere) ijIndemniteJournaliereManager
                    .getEntity(i);

            ijIndemniteJournaliere.delete(transaction);
        }
    }

    /**
     * 
     * @param transaction
     * @param myBSession
     * @param prononce2
     * @param prononce2prime
     * @param prononce1
     * @throws Exception
     */
    private void deplacementBasesIndemnites(BTransaction transaction, BSession myBSession, IJPrononce prononce2,
            IJPrononce prononce2prime, IJPrononce prononce1) throws Exception {
        // Récupèration des bases d'indemnité du prononce2
        IJBaseIndemnisationManager baseIndemnisationManager = recuperationBasesIndemnites(myBSession,
                prononce2.getIdPrononce(), transaction);

        // les bases d’indemnisation du prononcé2 sont déplacées dans le prononcé1
        for (int i = 0; i < baseIndemnisationManager.size(); i++) {
            IJBaseIndemnisation baseIndemnisation = (IJBaseIndemnisation) baseIndemnisationManager.getEntity(i);
            baseIndemnisation.setIdPrononce(prononce1.getIdPrononce());
            baseIndemnisation.update(transaction);
        }

        if ((prononce2prime != null) && (!prononce2prime.isNew())) {
            baseIndemnisationManager = recuperationBasesIndemnites(myBSession, prononce2prime.getIdPrononce(),
                    transaction);
        }

        if ((prononce2prime != null) && (!prononce2prime.isNew())) {
            // les bases d’indemnisation du prononcé2prime sont supprimées
            for (int i = 0; i < baseIndemnisationManager.size(); i++) {
                IJBaseIndemnisation baseIndemnisation = (IJBaseIndemnisation) baseIndemnisationManager.getEntity(i);
                baseIndemnisation.delete(transaction);
            }
        }
    }

    /**
     * 
     * @param transaction
     * @param myBSession
     * @param pdViewBean
     * @param prononce
     * @return
     * @throws Exception
     * @throws JAException
     */
    private boolean isAnnulerPossible(BTransaction transaction, BSession myBSession,
            IJAnnulerCorrigerDepuisPrononceViewBean pdViewBean, IJPrononce prononce) throws Exception, JAException {
        boolean peutOnAnnuler = true;
        // Check pour voir s'il y a des prononces entre celui d'origine et celui qu'on annule
        // Si c'est le cas, alors on ne doit pas laisser faire de supprimer
        IJPrononceManager prononces = new IJPrononceManager();
        prononces.setForIdParentCorrigerDepuis(prononce.getIdParentCorrigeDepuis());
        prononces.setForCsEtats(new String[] { /* IIJPrononce.CS_VALIDE, */IIJPrononce.CS_COMMUNIQUE });
        prononces.setSession(myBSession);
        prononces.find(transaction);

        if (prononces.getSize() > 1) {
            ArrayList<IJPrononce> mesPrononces = new ArrayList<IJPrononce>();
            for (int i = 0; i < prononces.getSize(); i++) {
                mesPrononces.add((IJPrononce) prononces.get(i));
            }

            JACalendar myJACalendar = new JACalendarGregorian();

            // Comparaison des dates pour voir si effectivement elle se trouve entre deux
            for (IJPrononce p : mesPrononces) {
                if (myJACalendar.compare(p.getDateFinPrononce(), prononce.getDateFinPrononce()) == JACalendar.COMPARE_FIRSTLOWER) {
                    peutOnAnnuler = false;
                    pdViewBean.setDateDebutPrononcerAAnnuler(prononce.getDateDebutPrononce());
                    pdViewBean.setDateDebutPrononcerAAnnulerAvant(p.getDateDebutPrononce());
                    pdViewBean.setDateFinPrononcerAAnnuler(prononce.getDateFinPrononce());
                    pdViewBean.setDateFinPrononcerAAnnulerAvant(p.getDateFinPrononce());
                }
            }
        }
        return peutOnAnnuler;
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

        baseIndemnisationManager.find(transaction, BManager.SIZE_NOLIMIT);

        return baseIndemnisationManager;
    }

    /**
     * 
     * @param transaction
     * @param myBSession
     * @param prononce2
     * @throws Exception
     */
    private void supprimerBasesIJCalculeDuPrononce(BTransaction transaction, BSession myBSession, IJPrononce prononce2)
            throws Exception {
        IJIJCalculeeManager ijCalculeeManager = new IJIJCalculeeManager();
        ijCalculeeManager.setSession(myBSession);
        ijCalculeeManager.setForIdPrononce(prononce2.getIdPrononce());
        ijCalculeeManager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < ijCalculeeManager.size(); i++) {
            IJIJCalculee ijCalculee = (IJIJCalculee) ijCalculeeManager.getEntity(i);
            String newIdIJCalculee = ijCalculee.getIdIJCalculee();

            ijCalculee.delete(transaction);
            deleteIJIndemniteJournaliere(myBSession, newIdIJCalculee, transaction);
        }
    }
}
