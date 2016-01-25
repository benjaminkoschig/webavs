package globaz.ij.helpers.prononces;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.lots.IJLot;
import globaz.ij.db.prestations.IJPrestation;
import globaz.ij.db.prestations.IJPrestationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.regles.IJPrononceRegles;
import globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJPrononceJointDemandeHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public FWViewBeanInterface actionAnnuler(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        IJPrononceJointDemandeViewBean pdViewBean = (IJPrononceJointDemandeViewBean) viewBean;
        IJPrononce prononce = IJPrononce.loadPrononce(session, null, pdViewBean.getIdPrononce(),
                pdViewBean.getCsTypeIJ());

        if (!JadeStringUtil.isBlankOrZero(prononce.getIdParent())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("ANNULATION_PRONONCE_ENFANT_ERROR"));
            return viewBean;
        }

        if (IIJPrononce.CS_ATTENTE.equals(prononce.getCsEtat()) || IIJPrononce.CS_VALIDE.equals(prononce.getCsEtat())
                || IIJPrononce.CS_DECIDE.equals(prononce.getCsEtat())) {
            ;
        } else {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("ANNULATION_PRONONCE_ENFANT_ERROR"));
            return viewBean;
        }

        // Changer l'état du prononcé
        prononce.setCsEtat(IIJPrononce.CS_ANNULE);
        prononce.update();

        // Supprimer toutes les éventuelles prestations du prononcé
        IJBaseIndemnisationManager biMgr = new IJBaseIndemnisationManager();
        biMgr.setSession(session);
        biMgr.setForIdPrononce(prononce.getIdPrononce());
        biMgr.find();

        for (Iterator iterator = biMgr.iterator(); iterator.hasNext();) {
            IJBaseIndemnisation bi = (IJBaseIndemnisation) iterator.next();

            IJPrestationManager prstMgr = new IJPrestationManager();
            prstMgr.setSession(session);
            prstMgr.setForIdBaseIndemnisation(bi.getIdBaseIndemisation());
            prstMgr.find();

            for (Iterator iterator2 = prstMgr.iterator(); iterator2.hasNext();) {
                IJPrestation prestation = (IJPrestation) iterator2.next();

                prestation.delete();

            }

            bi.setCsEtat(IIJBaseIndemnisation.CS_OUVERT);
            bi.update();

        }

        return viewBean;
    }

    /**
     * 
     * Crée une copie (clone) du prononce.
     * 
     * @param viewBean
     * @param action
     * @param session
     * @return
     * @throws Exception
     */
    public FWViewBeanInterface actionCreerCopie(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJPrononceJointDemandeViewBean pdViewBean = (IJPrononceJointDemandeViewBean) viewBean;
        IJPrononce prononce = IJPrononce.loadPrononce(session, null, pdViewBean.getIdPrononce(),
                pdViewBean.getCsTypeIJ());

        IJPrononceRegles.creerCopie(session, session.getCurrentThreadTransaction(), prononce);

        return viewBean;
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionCreerCorrection(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        IJPrononceJointDemandeViewBean pdViewBean = (IJPrononceJointDemandeViewBean) viewBean;
        IJPrononce prononce = IJPrononce.loadPrononce(session, null, pdViewBean.getIdPrononce(),
                pdViewBean.getCsTypeIJ());

        if (IJPrononceRegles.isCorrigerPermis(prononce)) {
            IJPrononceRegles.creerCorrection(session, session.getCurrentThreadTransaction(), prononce);
        }

        return viewBean;
    }

    /**
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Action custom permettant de simuler le paiement d'un prononce.
     * 
     * Est utilisé pour récupéré des anciens cas de l'AS400 qui doivent être restitué.
     * 
     * 1) Creation d'un lot a l'état "Définitif" contenant les prestations du prononce 2) Mise à jours des prestations
     * du droit à l'état "Définitif" 3) Mise à jours des bases d'indemnisation à l'état "Communiqué" 4) Mise à jours de
     * l'état du droit à "Définitif"
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void simulerPaiementDroit(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        IJPrononceJointDemandeViewBean vbDroit = (IJPrononceJointDemandeViewBean) viewBean;

        BTransaction bTrans = null;

        try {
            bTrans = (BTransaction) session.newTransaction();
            bTrans.openTransaction();

            IJPrononce pro = new IJPrononce();
            pro.setSession(session);
            pro.setIdPrononce(vbDroit.getIdPrononce());
            pro.retrieve(bTrans);

            if (!pro.getCsEtat().equals(IIJPrononce.CS_COMMUNIQUE)) {

                // creation du lot
                IJLot lot = new IJLot();
                lot.setSession(session);
                lot.setDateCreation(JACalendar.today().toStr("."));
                lot.setDescription(java.text.MessageFormat.format(session.getLabel("LOT_REPRISE"),
                        new Object[] { JACalendar.today().toStr(".") }));
                lot.setCsEtat(IIJLot.CS_OUVERT);
                lot.add(bTrans);

                // pour toutes les bases d'indemnisation
                IJBaseIndemnisationManager biManager = new IJBaseIndemnisationManager();
                biManager.setSession(session);
                biManager.setForIdPrononce(pro.getIdPrononce());
                biManager.find(bTrans);

                Iterator iter = biManager.iterator();

                boolean isPrestationsFound = false;
                while (iter.hasNext()) {
                    IJBaseIndemnisation bi = (IJBaseIndemnisation) iter.next();

                    // pour toutes les prestations
                    IJPrestationManager pManager = new IJPrestationManager();
                    pManager.setSession(session);
                    pManager.setForIdBaseIndemnisation(bi.getIdBaseIndemisation());
                    pManager.find(bTrans);

                    Iterator iter2 = pManager.iterator();
                    while (iter2.hasNext()) {
                        isPrestationsFound = true;
                        IJPrestation p = (IJPrestation) iter2.next();

                        // mise a jour de la prestation
                        p.setIdLot(lot.getIdLot());
                        p.setCsEtat(IIJPrestation.CS_DEFINITIF);
                        p.update(bTrans);
                    }

                    // mise a jour de la base d'indemnisation
                    bi.setCsEtat(IIJBaseIndemnisation.CS_COMMUNIQUE);
                    bi.update(bTrans);
                }

                // Aucune prestations trouvée pour ce prononcé, on interdit la
                // simulation du paiement, car n'a aucun sens.
                // Il faudrait dans ce cas l'annuler.
                // cf. BZ-2792
                if (!isPrestationsFound) {
                    // Pour les fouilles merdes :-) cette erreur n'est pas
                    // internationalisé car uniquement les
                    // gestionnaire de Globaz sont abilité à exécuter cette
                    // action !!!
                    vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                    vbDroit.setMessage(session
                            .getLabel("Opération non authorisée, car aucune prestation trouvée pour ce prononcé."));
                    throw new Exception("Opération non authorisée, car aucune prestation trouvée pour ce prononcé.");
                }

                // mise a jour du prononce
                pro.setCsEtat(IIJPrononce.CS_COMMUNIQUE);
                pro.update(bTrans);

                // mise a jour su lot
                lot.setCsEtat(IIJLot.CS_VALIDE);
                lot.update(bTrans);

                if (bTrans.hasErrors()) {
                    throw new Exception(bTrans.getErrors().toString());
                }

            } else {
                vbDroit.setMsgType(FWViewBeanInterface.ERROR);
                vbDroit.setMessage(session.getLabel("ACTION_IMPOSSIBLE_POUR_PRONONCE_COMMUNIQUE"));
            }
            bTrans.commit();
        } catch (Exception e) {
            if (bTrans != null) {
                bTrans.rollback();
            }
            throw e;
        } finally {
            if (bTrans != null) {
                bTrans.closeTransaction();
            }
        }
    }
}
