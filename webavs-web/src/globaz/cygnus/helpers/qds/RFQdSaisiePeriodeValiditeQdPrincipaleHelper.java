/*
 * Créé le 11 décembre 2009
 */
package globaz.cygnus.helpers.qds;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFQdSaisiePeriodeValiditeQdPrincipaleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author jje
 */
public class RFQdSaisiePeriodeValiditeQdPrincipaleHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajout historisé d'une période de validité d'une grande Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;
        try {
            transaction = currentSession.newTransaction();
            transaction.openTransaction();

            RFQdSaisiePeriodeValiditeQdPrincipaleViewBean rfQdSaiPerValQdPriVb = (RFQdSaisiePeriodeValiditeQdPrincipaleViewBean) viewBean;

            validerPeriode(rfQdSaiPerValQdPriVb, "");

            if (!FWViewBeanInterface.ERROR.equals(rfQdSaiPerValQdPriVb.getMsgType())) {

                // calcule le nouvel id unique de famille de modification
                int famModifCompteur = 0;
                RFPeriodeValiditeQdPrincipaleManager mgr = new RFPeriodeValiditeQdPrincipaleManager();
                mgr.setSession(currentSession);
                mgr.setForIdFamilleMax(true);
                mgr.changeManagerSize(0);
                mgr.find(transaction);

                if (!mgr.isEmpty()) {
                    RFPeriodeValiditeQdPrincipale pv = (RFPeriodeValiditeQdPrincipale) mgr.getFirstEntity();
                    if (null != pv) {
                        famModifCompteur = JadeStringUtil.parseInt(pv.getIdFamilleModification(), 0) + 1;
                    } else {
                        famModifCompteur = 1;
                    }
                } else {
                    famModifCompteur = 1;
                }

                RFPeriodeValiditeQdPrincipale rfPerValQdPri = new RFPeriodeValiditeQdPrincipale();
                rfPerValQdPri.setSession(currentSession);
                rfPerValQdPri.setRemarque(rfQdSaiPerValQdPriVb.getRemarque());
                rfPerValQdPri.setConcerne(rfQdSaiPerValQdPriVb.getConcerne());
                rfPerValQdPri.setDateDebut(rfQdSaiPerValQdPriVb.getDateDebut());
                rfPerValQdPri.setDateFin(rfQdSaiPerValQdPriVb.getDateFin());
                rfPerValQdPri.setIdGestionnaire(currentSession.getUserName());
                rfPerValQdPri.setIdFamilleModification(Integer.toString(famModifCompteur));
                rfPerValQdPri.setIdQd(rfQdSaiPerValQdPriVb.getIdQd());
                rfPerValQdPri.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfPerValQdPri.setTypeModification(IRFQd.CS_AJOUT);
                rfPerValQdPri.setIdPeriodeValModifiePar("");

                rfPerValQdPri.add(transaction);
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
     * Suppression historisée du solde de charge d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BSession currentSession = (BSession) session;

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFQdSaisiePeriodeValiditeQdPrincipaleViewBean rfQdSaiPerValQdPriVb = (RFQdSaisiePeriodeValiditeQdPrincipaleViewBean) viewBean;

            // TODO: delete permis si aucunes demandes rattachés à la période
            // this.isDemandeImputeeSurPeriode(rfQdSaiPerValQdPriVb);

            if (!rfQdSaiPerValQdPriVb.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                RFPeriodeValiditeQdPrincipale rfPerValQdPri = new RFPeriodeValiditeQdPrincipale();
                rfPerValQdPri.setSession(currentSession);
                rfPerValQdPri.setRemarque("");
                rfPerValQdPri.setConcerne(currentSession.getLabel("JSP_RF_QD_S_SUPPRESSION_MANUELLE") + ", "
                        + rfQdSaiPerValQdPriVb.getRemarque());
                rfPerValQdPri.setDateDebut(rfQdSaiPerValQdPriVb.getDateDebut());
                rfPerValQdPri.setDateFin(rfQdSaiPerValQdPriVb.getDateFin());
                rfPerValQdPri.setIdGestionnaire(currentSession.getUserName());
                rfPerValQdPri.setIdFamilleModification(rfQdSaiPerValQdPriVb.getIdFamilleModification());
                rfPerValQdPri.setIdQd(rfQdSaiPerValQdPriVb.getIdQd());
                rfPerValQdPri.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfPerValQdPri.setTypeModification(IRFQd.CS_SUPPRESSION);
                rfPerValQdPri.setIdPeriodeValModifiePar(rfQdSaiPerValQdPriVb.getIdPeriodeValidite());

                rfPerValQdPri.add(transaction);

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

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWHelper#_find(globaz.globall.db. BIPersistentObjectList,
     * globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        super._find(persistentList, action, session);
    }

    /**
     * Modification historisée du solde de charge d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        BSession currentSession = (BSession) session;

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFQdSaisiePeriodeValiditeQdPrincipaleViewBean rfQdSaiPerValQdPriVb = (RFQdSaisiePeriodeValiditeQdPrincipaleViewBean) viewBean;

            validerPeriode(rfQdSaiPerValQdPriVb, rfQdSaiPerValQdPriVb.getIdPeriodeValidite());

            if (!FWViewBeanInterface.ERROR.equals(rfQdSaiPerValQdPriVb.getMsgType())) {
                RFPeriodeValiditeQdPrincipale rfPerValQdPri = new RFPeriodeValiditeQdPrincipale();
                rfPerValQdPri.setSession(currentSession);
                rfPerValQdPri.setRemarque(rfQdSaiPerValQdPriVb.getRemarque());
                rfPerValQdPri.setConcerne(rfQdSaiPerValQdPriVb.getConcerne());
                rfPerValQdPri.setDateDebut(rfQdSaiPerValQdPriVb.getDateDebut());
                rfPerValQdPri.setDateFin(rfQdSaiPerValQdPriVb.getDateFin());
                rfPerValQdPri.setIdGestionnaire(currentSession.getUserName());
                rfPerValQdPri.setIdFamilleModification(rfQdSaiPerValQdPriVb.getIdFamilleModification());
                rfPerValQdPri.setIdQd(rfQdSaiPerValQdPriVb.getIdQd());
                rfPerValQdPri.setDateModification(JadeDateUtil.getDMYDate(new Date()));
                rfPerValQdPri.setTypeModification(IRFQd.CS_MODIFICATION);
                rfPerValQdPri.setIdPeriodeValModifiePar(rfQdSaiPerValQdPriVb.getIdPeriodeValidite());

                rfPerValQdPri.add(transaction);
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

    private void isDemandeImputeeSurPeriode(RFQdSaisiePeriodeValiditeQdPrincipaleViewBean vb) throws Exception {

        RFDemandeManager rfDemMgr = new RFDemandeManager();
        rfDemMgr.setSession(vb.getSession());
        rfDemMgr.setForIdQdPrincipale(vb.getIdQd());
        rfDemMgr.setForDateDebutBetweenDateDebutDem(vb.getDateDebut());
        rfDemMgr.setForDateFinBetweenDateDebutDem(vb.getDateFin());
        rfDemMgr.changeManagerSize(0);
        // rfDemMgr.setForEtatsDemande(new String[] { IRFDemande.VALIDE, IRFDemande.PAYE });
        rfDemMgr.find();

        if (rfDemMgr.size() > 0) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_PERIODE_VALIDITE_LIEE_DEMANDE");
        }
    }

    /*
     * Valide l'ajout d'une période de validité d'une QD
     * 
     * La date de début de période est obligatoire
     * 
     * la date de début doit être plus petite ou égale à la date de fin
     * 
     * Si la nouvelle période est postérieur à la dernière période entrée, la dernière période doit comporter une date
     * de fin
     * 
     * Si la nouvelle période est antérieur à la dernière période, la nouvelle période doit comporter une date de fin
     * 
     * Les périodes ne doivent pas se chevaucher
     * 
     * TODO: ??La période ne peut pas concerner l'année suivante
     * 
     * les deux dates de la nouvelle période doivent concerner la même année
     */
    private void validerPeriode(RFQdSaisiePeriodeValiditeQdPrincipaleViewBean vb, String idPeriodeValToIgnore)
            throws Exception {

        // Date de début obligatoire
        if (!JadeStringUtil.isBlankOrZero(vb.getDateDebut())) {

            JADate dateDebut = new JADate(vb.getDateDebut());
            int anneeQd = new Integer(vb.getAnneeQd()).intValue();

            if (dateDebut.getYear() == anneeQd) {

                // Date de fin plus grande que celle de début
                if (!JadeStringUtil.isBlankOrZero(vb.getDateFin())) {

                    JACalendar cal = new JACalendarGregorian();
                    JADate dateFin = new JADate(vb.getDateFin());

                    if (cal.compare(dateDebut, dateFin) == JACalendar.COMPARE_FIRSTUPPER) {
                        RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_DATE_FIN_PLUS_PETITE_DATE_DEBUT");
                    }
                    // Doit concerner la même année
                    if (anneeQd != dateFin.getYear()) {
                        RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_PERIODE_VALIDITE_DATE_ANNEE_QD");
                    }
                }

            } else {
                RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_PERIODE_VALIDITE_DATE_ANNEE_QD");
            }

        } else {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_DATE_DE_DEBUT_OBLIGATOIRE");
        }

        if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {
                // on test ensuite si il existe une période de validité couvrant
                // la période de la Qd à ajouter

                // Recherche des membres de la grande Qd
                Set<String> idsTiersMembresFamillePrisDansCalcul = new HashSet<String>();

                RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
                rfAssQdDossierJointDossierJointTiersMgr.setSession(vb.getSession());
                rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(vb.getIdQd());
                rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
                rfAssQdDossierJointDossierJointTiersMgr.find();

                if (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) {

                    Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr
                            .iterator();

                    while (rfAssQdDossierJointDossierJointTiersIter.hasNext()) {

                        RFAssQdDossierJointDossierJointTiers rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter
                                .next();

                        if (rfAssQdDossierJointDossierJointTiers != null) {
                            if (rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()) {
                                idsTiersMembresFamillePrisDansCalcul.add(rfAssQdDossierJointDossierJointTiers
                                        .getIdTiers());
                            }
                        } else {
                            throw new Exception(
                                    "RFQdSaisiePeriodeValiditeQdPrincipaleHelper impossible de remonter les membres de la Qd principale");
                        }
                    }

                } else {
                    throw new Exception(
                            "RFQdSaisiePeriodeValiditeQdPrincipaleHelper impossible de remonter les membres de la Qd principale");
                }

                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerJointDosJointTiersJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();

                rfQdJointPerJointDosJointTiersJointDemMgr.setSession(vb.getSession());
                rfQdJointPerJointDosJointTiersJointDemMgr.setForIdsTiers(idsTiersMembresFamillePrisDansCalcul);
                rfQdJointPerJointDosJointTiersJointDemMgr.setForAnneeQd(vb.getAnneeQd());
                rfQdJointPerJointDosJointTiersJointDemMgr.setForDateDebutBetweenPeriode(vb.getDateDebut());
                rfQdJointPerJointDosJointTiersJointDemMgr.setComprisDansCalcul(true);

                if (!JadeStringUtil.isBlankOrZero(idPeriodeValToIgnore)) {
                    rfQdJointPerJointDosJointTiersJointDemMgr.setForIdPeriodeValToIgnore(idPeriodeValToIgnore);
                }

                rfQdJointPerJointDosJointTiersJointDemMgr.setForDateFinBetweenPeriode(JadeStringUtil.isBlankOrZero(vb
                        .getDateFin()) ? RFUtils.MAX_DATE_VALUE : vb.getDateFin());

                rfQdJointPerJointDosJointTiersJointDemMgr.changeManagerSize(0);
                rfQdJointPerJointDosJointTiersJointDemMgr.find();

                if (rfQdJointPerJointDosJointTiersJointDemMgr.size() > 0) {
                    RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_QDDROITPC_DEJA_EXISTANTE");
                }
            }
        }

    }

}