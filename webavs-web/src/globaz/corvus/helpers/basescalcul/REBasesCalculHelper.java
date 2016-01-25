/*
 * Créé le 12 juil. 07
 */

package globaz.corvus.helpers.basescalcul;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.vb.basescalcul.REAbstractBasesCalculProxyViewBean;
import globaz.corvus.vb.basescalcul.REBasesCalculDixiemeRevisionViewBean;
import globaz.corvus.vb.basescalcul.REBasesCalculNeuviemeRevisionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;
import ch.globaz.corvus.business.services.CorvusCrudServiceLocator;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.corvus.domaine.DemandeRente;

/**
 * @author HPE
 */
public class REBasesCalculHelper extends PRAbstractHelper {

    private static final String EDITER_NUMERO_DECISION_AJAX = "editerNumeroDecisionAJAX";

    @Override
    protected void _delete(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        BITransaction transaction = null;

        String idBaseCalcul = "";
        String idRenteCalculee = "";

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            if (viewBean instanceof REBasesCalculDixiemeRevisionViewBean) {

                REBasesCalculDixiemeRevisionViewBean vb = (REBasesCalculDixiemeRevisionViewBean) viewBean;
                idBaseCalcul = vb.getIdBasesCalcul();
                idRenteCalculee = vb.getIdRenteCalculee();
            } else {

                REBasesCalculNeuviemeRevisionViewBean vb = (REBasesCalculNeuviemeRevisionViewBean) viewBean;
                idBaseCalcul = vb.getIdBasesCalcul();
                idRenteCalculee = vb.getIdRenteCalculee();
            }

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession((BSession) session);
            bc.setIdBasesCalcul(idBaseCalcul);
            bc.retrieve(transaction);

            REDeleteCascadeDemandeAPrestationsDues.supprimerBaseCalculCascade_noCommit((BSession) session, transaction,
                    bc);

            // BZ 6648 - Si la demande ne possède plus de bases de calcul, la mettre à l'état enregistré
            updateEtatDemandeCalculee(session, idRenteCalculee);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        // mise à jour de la période de la demande
        REDemandeRente demandeRenteEntity = new REDemandeRente();
        demandeRenteEntity.setSession((BSession) session);
        demandeRenteEntity.setIdRenteCalculee(idRenteCalculee);
        demandeRenteEntity.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demandeRenteEntity.retrieve();

        if (!demandeRenteEntity.isNew()) {
            DemandeRente demandeRente = CorvusCrudServiceLocator.getDemandeRenteCrudService().read(
                    Long.parseLong(demandeRenteEntity.getIdDemandeRente()));
            CorvusServiceLocator.getDemandeRenteService().mettreAJourLaPeriodeDeLaDemandeEnFonctionDesRentes(
                    demandeRente);
        }

    }

    /**************************************************************************
     * Si la demande d'une rente calculé ne possède plus de bases de calcul, la mettre à l'état enregistré
     * 
     * @param session La session
     * @param idRenteCalculee L'ID de la rente calculée concernée
     * @throws Exception En cas de problème technique lié à la persistance
     */
    private static void updateEtatDemandeCalculee(final BISession session, final String idRenteCalculee)
            throws Exception {
        REBasesCalculManager basesCalculManager = new REBasesCalculManager();
        basesCalculManager.setForIdRenteCalculee(idRenteCalculee);
        basesCalculManager.find();
        int basesDeCalculCount = basesCalculManager.size();

        // Si la demande ne possède pas de bases de calcul ...
        if (basesDeCalculCount == 0) {
            REDemandeRente demandeRente = new REDemandeRente();
            demandeRente.setSession((BSession) session);
            demandeRente.setIdRenteCalculee(idRenteCalculee);
            demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
            demandeRente.retrieve();

            // ... mettre la demande de l'état calculé à l'état enregistré
            if (demandeRente.isNew() == false
                    && IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demandeRente.getCsEtat())) {

                demandeRente.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE);

                demandeRente.update();
            }
        }
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession iSession) {
        // Si édition du numéro de décision via appel AJAX...
        if (EDITER_NUMERO_DECISION_AJAX.equals(action.getActionPart())) {
            try {
                BSession session = (BSession) iSession;
                REAbstractBasesCalculProxyViewBean bcViewBean = (REAbstractBasesCalculProxyViewBean) viewBean;

                // Sauvegarde de la valeur du numéro de décision
                String referenceDecision = bcViewBean.getReferenceDecision();

                // Le numéro de décision doit être vide ou un chiffre entier non négatif
                if ((JadeStringUtil.isEmpty(referenceDecision) || JadeStringUtil.isDigit(referenceDecision)) == false) {
                    throw new RETechnicalException(session.getLabel("ERREUR_REFERENCE_DECISION_NON_VALIDE"));
                }

                // Ré-extraction du view-bean pour ne changer que le numéro de décision
                bcViewBean.retrieve();

                if (bcViewBean.getBasesCalcul().isNew()) {
                    throw new RETechnicalException(session.getLabel("ERREUR_BASE_DE_CALCUL_NON_EXISTANTE"));
                }

                // Mettre à jour le numéro de décision
                bcViewBean.setReferenceDecision(referenceDecision);

                // Persister
                _update(viewBean, action, iSession);
            } catch (Exception e) {
                viewBean.setMessage(e.toString());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return viewBean;
    }

    /**
     * Correction du bug 5175
     */
    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {
        super._retrieve(viewBean, action, session);

        REAbstractBasesCalculProxyViewBean bcViewBean = (REAbstractBasesCalculProxyViewBean) viewBean;
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            if (JadeStringUtil.isBlank(bcViewBean.getSurvenanceEvtAssAyantDroit())) {
                REDemandeRente demandeRente = new REDemandeRente();
                demandeRente.setSession((BSession) session);
                demandeRente.setIdRenteCalculee(bcViewBean.getIdRenteCalculee());
                demandeRente.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                demandeRente.retrieve(transaction);

                if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equalsIgnoreCase(demandeRente
                        .getCsTypeDemandeRente())) {
                    REPeriodeInvaliditeManager manager = new REPeriodeInvaliditeManager();
                    manager.setSession((BSession) session);
                    manager.setForIdDemandeRente(demandeRente.getIdDemandeRente());
                    manager.find(transaction);

                    String oldiestDate = "31.12.2099";

                    for (Iterator<REPeriodeInvalidite> iterator = manager.iterator(); iterator.hasNext();) {
                        REPeriodeInvalidite periodeInvalidite = iterator.next();

                        if (JadeDateUtil.isDateAfter(oldiestDate, periodeInvalidite.getDateDebutInvalidite())) {
                            oldiestDate = periodeInvalidite.getDateDebutInvalidite();
                        }
                    }

                    if (!JadeDateUtil.areDatesEquals("31.12.2099", oldiestDate)) {
                        String survenanceEvtAssAyantDroit = JadeDateUtil.convertDateMonthYear(oldiestDate);
                        bcViewBean.setSurvenanceEvtAssAyantDroit(survenanceEvtAssAyantDroit);

                        // sauvegarde en base du résultat
                        REBasesCalcul basesCalcul = new REBasesCalcul();
                        basesCalcul.setSession((BSession) session);
                        basesCalcul.setIdBasesCalcul(bcViewBean.getIdBasesCalcul());
                        basesCalcul.retrieve(transaction);

                        basesCalcul.setSurvenanceEvtAssAyantDroit(survenanceEvtAssAyantDroit);

                        basesCalcul.update(transaction);
                    }
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    protected void _update(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            String idBaseCalcul = "";
            String refDecision = "";
            idBaseCalcul = ((REAbstractBasesCalculProxyViewBean) viewBean).getIdBasesCalcul();
            refDecision = ((REAbstractBasesCalculProxyViewBean) viewBean).getReferenceDecision();

            REBasesCalcul bcAvantMAJ = new REBasesCalcul();
            bcAvantMAJ.setSession((BSession) session);
            bcAvantMAJ.setIdBasesCalcul(idBaseCalcul);
            bcAvantMAJ.retrieve(transaction);

            // MAJ de la base de calcul.
            // Doit obligatoirement se faire avant la maj de la RA pour que la modification soit prise en compte.
            ((REAbstractBasesCalculProxyViewBean) viewBean).update(transaction);

            // Force la maj de la clé de regroupement des décisions, si elle a changée.
            if ((!JadeStringUtil.isBlankOrZero(refDecision) && !refDecision.equals(bcAvantMAJ.getReferenceDecision()))
                    || (JadeStringUtil.isBlankOrZero(refDecision) && !JadeStringUtil.isBlankOrZero(bcAvantMAJ
                            .getReferenceDecision()))) {

                RERenteAccordeeManager mgr = new RERenteAccordeeManager();
                mgr.setSession((BSession) session);
                mgr.setForIdBaseCalcul(idBaseCalcul);
                mgr.find(transaction);
                for (int i = 0; i < mgr.size(); i++) {
                    RERenteAccordee ra = (RERenteAccordee) mgr.getEntity(i);
                    ra.update(transaction);
                }
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

}