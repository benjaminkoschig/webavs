/*
 * Créé le 11 décembre 2009
 */
package globaz.cygnus.helpers.dossiers;

import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.api.dossiers.IRFDossiers;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.dossiers.RFDossierJointTiersListViewBean;
import globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * @author jje
 */
public class RFDossierJointTiersHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajout d'un dossier
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        validate(viewBean, false);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFDossierJointTiersViewBean rfdosVb = (RFDossierJointTiersViewBean) viewBean;

                // creation du dossier prdemap
                PRDemande demandePrestation = new PRDemande();
                demandePrestation.setSession((BSession) session);
                demandePrestation.setIdTiers(rfdosVb.getIdTiers());
                demandePrestation.setTypeDemande(IPRDemande.CS_TYPE_RFM);
                demandePrestation.setEtat(IPRDemande.CS_ETAT_OUVERT);
                demandePrestation.add(transaction);

                // creation du dossier RFM
                RFDossier dossierRFM = new RFDossier();
                dossierRFM.setSession(rfdosVb.getSession());
                dossierRFM.setIdGestionnaire(rfdosVb.getIdGestionnaire());
                dossierRFM.setDateDebut(rfdosVb.getDateDebutPeriodeDossier());
                dossierRFM.setDateFin(rfdosVb.getDateFinPeriodeDossier());
                dossierRFM.setCsEtatDossier(rfdosVb.getCsEtatDossier());
                dossierRFM.setIdPrDem(demandePrestation.getIdDemande());
                dossierRFM.setCsSource(IRFDossiers.GESTIONNAIRE);

                dossierRFM.add(transaction);

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

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        // chargement de la date du dernier paiement pour les comparaisons avec les périodes de contributions
        // d'assistance AI (mandat InfoRom D0034)
        RFDossierJointTiersListViewBean listViewBean = (RFDossierJointTiersListViewBean) persistentList;
        listViewBean.setDateDernierPaiement(REPmtMensuel.getDateDernierPmt((BSession) session));

        super._find(persistentList, action, session);
    }

    /**
     * Modification d'un dossier
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFDossierJointTiersViewBean rfdosVb = (RFDossierJointTiersViewBean) viewBean;

            validate(rfdosVb, true);

            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                // recherche du dossier RFM
                RFDossier dossierRFM = new RFDossier();
                dossierRFM.setSession(rfdosVb.getSession());
                dossierRFM.setIdDossier(rfdosVb.getIdDossier());
                dossierRFM.retrieve(transaction);

                if (!dossierRFM.isNew()) {

                    // recherche de la demande
                    PRDemande demandePrestation = new PRDemande();
                    demandePrestation.setSession((BSession) session);
                    demandePrestation.setIdDemande(rfdosVb.getIdDemandePrestation());
                    demandePrestation.setTypeDemande(IPRDemande.CS_TYPE_RFM);
                    demandePrestation.setEtat(IPRDemande.CS_ETAT_OUVERT);
                    demandePrestation.retrieve(transaction);

                    if (!demandePrestation.isNew()) {

                        // màj du dossier
                        dossierRFM.setIdGestionnaire(rfdosVb.getIdGestionnaire());
                        dossierRFM.setDateDebut(rfdosVb.getDateDebutPeriodeDossier());
                        dossierRFM.setDateFin(rfdosVb.getDateFinPeriodeDossier());
                        dossierRFM.setCsEtatDossier(rfdosVb.getCsEtatDossier());

                        demandePrestation.update(transaction);
                        dossierRFM.update(transaction);

                    } else {
                        RFUtils.setMsgErreurViewBean(viewBean,
                                "RFDossierJointTiersHelper._update(): Impossible de retrouver le demande de prestation");
                    }

                } else {
                    RFUtils.setMsgErreurViewBean(viewBean,
                            "RFDossierJointTiersHelper._update(): Impossible de retrouver le dossier RFM");
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
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

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

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Méthode qui valide l'ajout d'un dossier RFM
     * 
     * @param session
     * 
     * @param statement
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean, boolean isUpdate) throws Exception {

        // Un seul dossier par idTiers
        if (!isUpdate) {
            RFPrDemandeJointDossier rfPrDemandeJointDossier = RFUtils.getDossierJointPrDemande(
                    ((RFDossierJointTiersViewBean) viewBean).getIdTiers(),
                    ((RFDossierJointTiersViewBean) viewBean).getSession());
            if (null != rfPrDemandeJointDossier) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOS_D_DOSSIER_UNIQUE_PAR_TIERS");
            }
        }

        // Gestionnaire obligatoire
        if (JadeStringUtil.isBlankOrZero(((RFDossierJointTiersViewBean) viewBean).getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DOS_D_GESTIONNAIRE");
        }

    }
}