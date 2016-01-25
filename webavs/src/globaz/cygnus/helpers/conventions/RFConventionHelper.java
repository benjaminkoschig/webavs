// créé le 24 mars 2010
package globaz.cygnus.helpers.conventions;

import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.db.conventions.RFAssConventionFournisseurSousTypeDeSoinManager;
import globaz.cygnus.db.conventions.RFConvention;
import globaz.cygnus.db.conventions.RFConventionAssureManager;
import globaz.cygnus.db.conventions.RFMontantsConventionManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;

/**
 * author fha
 */
public class RFConventionHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // validation des champs du formulaire avant l'ajout d'une convention en
        // BDD
        validate(viewBean);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFConventionViewBean convVB = (RFConventionViewBean) viewBean;

                // creation de la convention
                RFConvention conventionRFM = new RFConvention();
                conventionRFM.setSession(convVB.getSession());
                conventionRFM.setTextLibelle(convVB.getLibelle());
                conventionRFM.setDateCreation(convVB.getForDateCreation());
                conventionRFM.setIsActif(convVB.getForActif());
                conventionRFM.setIdGestionnaire(convVB.getIdGestionnaire());

                conventionRFM.add(transaction);

                convVB.setIdConvention(conventionRFM.getIdConvention());

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
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFConventionViewBean conventionVb = (RFConventionViewBean) viewBean;

            // recherche de la convention
            RFConvention convention = new RFConvention();
            convention.setSession(conventionVb.getSession());
            convention.setIdConvention(conventionVb.getIdConvention());
            convention.retrieve(transaction);

            if (!convention.isNew()) {
                // recherche et suppression des montants associés à cette
                // convention
                RFMontantsConventionManager montantsConvention = new RFMontantsConventionManager();
                montantsConvention.setSession((BSession) session);
                montantsConvention.setForIdConvention(convention.getIdConvention());
                montantsConvention.changeManagerSize(0);
                montantsConvention.find();

                montantsConvention.delete(transaction);

                // recherche et suppression des fournisseurs associés à cette
                // convention
                RFAssConventionFournisseurSousTypeDeSoinManager fournisseursConvention = new RFAssConventionFournisseurSousTypeDeSoinManager();
                fournisseursConvention.setSession((BSession) session);
                fournisseursConvention.setForIdConvention(convention.getIdConvention());
                fournisseursConvention.changeManagerSize(0);
                fournisseursConvention.find();

                fournisseursConvention.delete(transaction);

                // recherche et suppression des assurés associés à cette
                // convention
                RFConventionAssureManager conventionAssure = new RFConventionAssureManager();
                conventionAssure.setSession((BSession) session);
                conventionAssure.setForIdConvention(convention.getIdConvention());
                conventionAssure.changeManagerSize(0);
                conventionAssure.find();

                conventionAssure.delete(transaction);

                // suppression de la convention
                convention.delete(transaction);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(convention, "_delete()", "RFConventionHelper");
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
     * on charge la ligne qu'on a dans le viewBean
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {

            RFConventionViewBean vb = (RFConventionViewBean) viewBean;

            vb.setId(vb.getIdConvention());
            vb.setNomEcranCourant(IRFConventions.ECRAN_CONVENTION);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }
    }

    // quand on séléctionne un élément de la rcListe
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean);
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            // on arrive avec RFConventionViewBean mais il faut remplir
            RFConventionViewBean rfConvVb = (RFConventionViewBean) viewBean;

            // initialisation des champs à updater
            RFConvention convention = new RFConvention();
            convention.setSession(rfConvVb.getSession());
            convention.setIdConvention(rfConvVb.getIdConvention());
            convention.retrieve(transaction);

            if (!convention.isNew()) {
                convention.setIdGestionnaire(rfConvVb.getIdGestionnaire());
                convention.setIsActif(rfConvVb.getForActif());
                convention.setTextLibelle(rfConvVb.getLibelle());
                convention.setDateCreation(rfConvVb.getForDateCreation());

                convention.update(transaction);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfConvVb, "_update()", "RFConventionHelper");
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
     * Méthode de validation des conventions
     * 
     * @param session
     * @param statement
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean) throws Exception {

        RFConventionViewBean rfConvVb = (RFConventionViewBean) viewBean;

        // construction du message d'erreur
        if (JadeStringUtil.isBlank(rfConvVb.getLibelle())) {
            RFUtils.setMsgErreurViewBean(rfConvVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_LIBELLE_VIDE");
        }
        if (JadeStringUtil.isBlank(rfConvVb.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(rfConvVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_GESTIONNAIRE_VIDE");
        }
        if (JadeStringUtil.isBlank(rfConvVb.getForDateCreation())) {
            RFUtils.setMsgErreurViewBean(rfConvVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_DATE_VIDE");
        }
        // Vérifier que ce soit bien dans le format JJ.MM.AAAA
        if ((rfConvVb.getForDateCreation().length() > 10) || (rfConvVb.getForDateCreation().length() < 10)) {
            RFUtils.setMsgErreurViewBean(rfConvVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_DATE");
        }
    }

}
