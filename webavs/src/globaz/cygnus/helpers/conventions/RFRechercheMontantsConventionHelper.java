// créé le 24 mars 2010
package globaz.cygnus.helpers.conventions;

import globaz.cygnus.api.conventions.IRFConventions;
import globaz.cygnus.db.conventions.RFMontantsConvention;
import globaz.cygnus.db.conventions.RFMontantsConventionDetailManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Iterator;

/**
 * author fha
 */
public class RFRechercheMontantsConventionHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean, (BSession) session);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFConventionViewBean convVB = (RFConventionViewBean) viewBean;

                if (verifierUnicite(convVB, session, true)) {

                    // creation du montant convention
                    RFMontantsConvention conventionRFM = new RFMontantsConvention();
                    conventionRFM.setSession(convVB.getSession());
                    conventionRFM.setDateDebut(convVB.getDateDebut());
                    conventionRFM.setDateFin(convVB.getDateFin());
                    conventionRFM.setPlafonne(convVB.getPlafonne());
                    conventionRFM.setIdConvention(convVB.getIdConvention());
                    conventionRFM.setCsTypeBeneficiaire(convVB.getCsTypeBeneficiaire());
                    conventionRFM.setPeriodicite(convVB.getCsPeriodicite());
                    conventionRFM.setMntMaxDefaut(convVB.getMntMaxDefaut());
                    conventionRFM.setMntMaxSansApi(convVB.getMntMaxSansApi());
                    conventionRFM.setMntMaxAvecApiFaible(convVB.getMntMaxAvecApiFaible());
                    conventionRFM.setMntMaxAvecApiGrave(convVB.getMntMaxAvecApiGrave());
                    conventionRFM.setMntMaxAvecApiMoyen(convVB.getMntMaxAvecApiMoyen());
                    conventionRFM.setTypePc(convVB.getCsTypePCAccordee());
                    conventionRFM.setGenrePc(convVB.getCsGenrePCAccordee());

                    conventionRFM.add(transaction);

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

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFConventionViewBean rfMontantsVb = (RFConventionViewBean) viewBean;

            // recherche des montants de la convention
            RFMontantsConvention montantsConv = new RFMontantsConvention();
            montantsConv.setSession(rfMontantsVb.getSession());
            montantsConv.setIdMontant(rfMontantsVb.getIdMontant());

            montantsConv.retrieve(transaction);

            if (!montantsConv.isNew()) {
                montantsConv.delete(transaction);
            } else {
                RFUtils.setMsgErreurInattendueViewBean(rfMontantsVb, "_delete()", "RFRechercheMontantsConventionHelper");
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

    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);
        ((RFConventionViewBean) viewBean).setNomEcranCourant("nouveauMontantConvention");
        ((RFConventionViewBean) viewBean).setPlafonne(Boolean.TRUE);
    }

    /**
     * on charge la ligne qu'on a dans le viewBean
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        try {
            RFConventionViewBean vb = (RFConventionViewBean) viewBean;
            vb.setNomEcranCourant(IRFConventions.ECRAN_MONTANT_CONVENTION);
            vb.setId(vb.getIdMontant());
            vb.setIsUpdate(Boolean.TRUE);
        } catch (Exception e) {
            RFUtils.setMsgErreurViewBean(viewBean, e.getMessage());
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        validate(viewBean, (BSession) session);

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFConventionViewBean rfMontantsVb = (RFConventionViewBean) viewBean;

            // recherche des montants de la convention
            RFMontantsConvention montantsConv = new RFMontantsConvention();

            if (verifierUnicite(rfMontantsVb, session, false)) {

                montantsConv.setSession(rfMontantsVb.getSession());
                montantsConv.setIdMontant(rfMontantsVb.getIdMontant());
                montantsConv.retrieve(transaction);

                if (!montantsConv.isNew()) {
                    montantsConv.setDateDebut(rfMontantsVb.getDateDebut());
                    montantsConv.setDateFin(rfMontantsVb.getDateFin());
                    montantsConv.setPeriodicite(rfMontantsVb.getCsPeriodicite());
                    montantsConv.setCsTypeBeneficiaire(rfMontantsVb.getCsTypeBeneficiaire());
                    montantsConv.setMntMaxDefaut(rfMontantsVb.getMntMaxDefaut());
                    montantsConv.setMntMaxAvecApiFaible(rfMontantsVb.getMntMaxAvecApiFaible());
                    montantsConv.setMntMaxAvecApiGrave(rfMontantsVb.getMntMaxAvecApiGrave());
                    montantsConv.setMntMaxAvecApiMoyen(rfMontantsVb.getMntMaxAvecApiMoyen());
                    montantsConv.setMntMaxSansApi(rfMontantsVb.getMntMaxSansApi());
                    montantsConv.setPlafonne(rfMontantsVb.getPlafonne());
                    montantsConv.setTypePc(rfMontantsVb.getCsTypePCAccordee());
                    montantsConv.setGenrePc(rfMontantsVb.getCsGenrePCAccordee());

                    montantsConv.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(rfMontantsVb, "_update()",
                            "RFRechercheMontantsConventionHelper");
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

    /*
     * test si le String en paramètre représente un montant positif ou nul renvoi vrai si positif ou nul faux si négatif
     */
    private Boolean isNumericPositifOuNul(String montant) {
        // enlever le formatage : '
        montant = montant.replace("'", "");
        Float floatMontant = Float.parseFloat(montant);
        return floatMontant >= 0;
    }

    /**
     * Méthode de validation de la saisie des montants d'une convention
     * 
     * @param session
     * @param statement
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean, BSession session) throws Exception {

        RFConventionViewBean rfMontantsVb = (RFConventionViewBean) viewBean;

        if (JadeStringUtil.isEmpty(rfMontantsVb.getCsTypeBeneficiaire())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_BENEFICIAIRE_VIDE");
        }

        if (JadeStringUtil.isEmpty(rfMontantsVb.getCsPeriodicite())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_PERIODICITE_VIDE");
        }

        if (JadeStringUtil.isEmpty(rfMontantsVb.getCsTypePCAccordee())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_TYPE_PC_VIDE");
        }

        if (JadeStringUtil.isEmpty(rfMontantsVb.getCsGenrePCAccordee())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_GENRE_PC_VIDE");
        }

        // Vérifier que ce soit bien dans le format JJ.MM.AAAA
        if ((rfMontantsVb.getDateDebut().length() > 10) || (rfMontantsVb.getDateDebut().length() < 10)) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_DATE_DEBUT");
        }

        // comparaison de date debut et fin (si date de fin non vide)
        if (!JadeStringUtil.isEmpty(rfMontantsVb.getDateDebut()) && !JadeStringUtil.isEmpty(rfMontantsVb.getDateFin())) {
            if (JadeDateUtil.isDateAfter(rfMontantsVb.getDateDebut().toString(), rfMontantsVb.getDateFin().toString())) {
                RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_COMPARAISON_DATE");
            }
        }

        if ("0.00".equals(rfMontantsVb.getMntMaxDefaut())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_CHAMP_MNT_DEFAUT_VIDE");
        } else if (!isNumericPositifOuNul(rfMontantsVb.getMntMaxDefaut())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_MNT_DEFAUT");
        }

        // vérification que tout les champs montant soient vide ou numerique
        if (!JadeStringUtil.isEmpty(rfMontantsVb.getMntMaxAvecApiFaible())
                && !isNumericPositifOuNul(rfMontantsVb.getMntMaxAvecApiFaible())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_MNT_FAIBLE");
        }
        if (!JadeStringUtil.isEmpty(rfMontantsVb.getMntMaxAvecApiFaible())
                && !isNumericPositifOuNul(rfMontantsVb.getMntMaxAvecApiGrave())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_MNT_GRAVE");
        }
        if (!JadeStringUtil.isEmpty(rfMontantsVb.getMntMaxAvecApiFaible())
                && !isNumericPositifOuNul(rfMontantsVb.getMntMaxAvecApiMoyen())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_MNT_MOYEN");
        }
        if (!JadeStringUtil.isEmpty(rfMontantsVb.getMntMaxAvecApiFaible())
                && !isNumericPositifOuNul(rfMontantsVb.getMntMaxSansApi())) {
            RFUtils.setMsgErreurViewBean(rfMontantsVb, "ERREUR_RF_SAISIE_CONVENTION_FORMAT_MNT_SANS_API");
        }

    }

    private Boolean verifierUnicite(RFConventionViewBean viewBean, BISession session, Boolean isAjout) throws Exception {
        RFMontantsConventionDetailManager montantsConvMgr = new RFMontantsConventionDetailManager();
        montantsConvMgr.setSession(viewBean.getSession());
        montantsConvMgr.setForIdConvention(viewBean.getIdConvention());
        montantsConvMgr.setForCsTypeBeneficiairePc(viewBean.getCsTypeBeneficiaire());
        montantsConvMgr.setForDateDebut(viewBean.getDateDebut());
        montantsConvMgr.setForDateFin(JadeStringUtil.isBlankOrZero(viewBean.getDateFin()) ? RFUtils.MAX_DATE_VALUE
                : viewBean.getDateFin());
        montantsConvMgr.setForCsGenrePc(viewBean.getCsGenrePCAccordee());
        montantsConvMgr.setForCsTypePc(viewBean.getCsTypePCAccordee());
        montantsConvMgr.changeManagerSize(0);
        montantsConvMgr.find();

        if (isAjout) {
            if (montantsConvMgr.getSize() > 0) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_CONVENTION_EXISTE_DEJA");
                return false;
            }
        } else {
            for (Iterator<RFMontantsConvention> it = montantsConvMgr.iterator(); it.hasNext();) {
                RFMontantsConvention montantConv = it.next();
                if (!viewBean.getIdMontant().equals(montantConv.getIdMontant())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_SAISIE_CONVENTION_EXISTE_DEJA");
                    return false;
                }
            }
        }
        return true;
    }

}
