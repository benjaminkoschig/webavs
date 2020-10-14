/*
 * Créé le 8 novembre 2010
 */
package globaz.cygnus.helpers.qds;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.demandes.RFDemandeManager;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.qds.RFAssQdDossier;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFAssQdDossierManager;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipale;
import globaz.cygnus.db.qds.RFPeriodeValiditeQdPrincipaleManager;
import globaz.cygnus.db.qds.RFQd;
import globaz.cygnus.db.qds.RFQdAssure;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdAugmentation;
import globaz.cygnus.db.qds.RFQdAugmentationManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.db.qds.RFQdPrincipale;
import globaz.cygnus.db.qds.RFQdSoldeCharge;
import globaz.cygnus.db.qds.RFQdSoldeChargeManager;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenu;
import globaz.cygnus.db.qds.RFQdSoldeExcedentDeRevenuManager;
import globaz.cygnus.services.RFRetrieveLimiteAnnuelleSousTypeDeSoinService;
import globaz.cygnus.services.RFRetrievePotQdPrincipaleInfoDroitPcService;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFSaisieQdViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.tools.PRDateFormater;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * @author jje
 */
public class RFSaisieQdPiedDePageHelper extends PRAbstractHelper {

    /**
     * Ajout d'une QD
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieQdViewBean rfQdVb = (RFSaisieQdViewBean) viewBean;
        boolean isCoupleSepare = false;

        if (IRFQd.CS_GRANDE_QD.equals(rfQdVb.getCsGenreQd())) {
            // MàJ des membres famille en fonction des personnes prises dans le calcul
            String[] idTiersPersonnesPrisesDansCalculTab = rfQdVb.getIdTiersPersonnesPriseDansCalcul().split(",");
            if (rfQdVb.getMembresFamille() != null) {
                for (String[] membreCourant : rfQdVb.getMembresFamille()) {
                    if (!membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                        if (membreCourant[8].equals(Boolean.TRUE.toString())) {
                            boolean isPrisDansCalcul = false;
                            for (String idTiersCourant : idTiersPersonnesPrisesDansCalculTab) {
                                if (membreCourant[0].equals(idTiersCourant)) {
                                    isPrisDansCalcul = true;
                                }
                            }

                            if (isPrisDansCalcul) {
                                membreCourant[8] = Boolean.TRUE.toString();
                            } else {
                                membreCourant[8] = Boolean.FALSE.toString();
                            }

                        }
                    }
                }
            }

            if (rfQdVb.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)
                    || rfQdVb.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)) {
                isCoupleSepare = true;
            }

            validerAjouterQdDroitPc(rfQdVb, isCoupleSepare);

        } else if (IRFQd.CS_PETITE_QD.equals(rfQdVb.getCsGenreQd())) {
            validerAjouterQdAssure(rfQdVb);
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                if (IRFQd.CS_GRANDE_QD.equals(rfQdVb.getCsGenreQd())) {

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        // Si la création concerne un couple séparé on crée directement 2 qds pour chaque membre du
                        // couple
                        if (isCoupleSepare) {

                            ajoutGrandeQdCoupleSepare(rfQdVb, session, transaction);

                        } else {
                            String idQd = ajouterGrandeQd(rfQdVb, rfQdVb.getIdDossier(),
                                    rfQdVb.getTypeRemboursementRequerant(), rfQdVb.getTypeRemboursementConjoint(),
                                    rfQdVb.getCsGenrePCAccordee(), session, transaction);
                            ajouterSoldeDeChargeAugmentationDeQd(rfQdVb, session, transaction, idQd);
                        }
                    }
                }

                if (IRFQd.CS_PETITE_QD.equals(rfQdVb.getCsGenreQd())) {

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        // Creation de la Qd assuré

                        // RFQd
                        RFQd qd = new RFQd();
                        qd.setSession(rfQdVb.getSession());

                        qd.setLimiteAnnuelle(rfQdVb.getLimiteAnnuelle());
                        qd.setIsPlafonnee(rfQdVb.getIsPlafonnee());
                        qd.setDateCreation(rfQdVb.getDateCreation());
                        qd.setAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(rfQdVb.getDateDebut()));
                        qd.setCsGenreQd(IRFQd.CS_PETITE_QD);
                        qd.setCsSource(IRFQd.CS_SOURCE_QD_GESTIONNAIRE);
                        qd.setCsEtat(rfQdVb.getCsEtat());// IRFQd.OUVERT);
                        qd.setIdGestionnaire(rfQdVb.getGestionnaire());
                        /*
                         * if (!JadeStringUtil.isBlankOrZero(rfQdVb.getAugmentationQd())) {
                         * qd.setCsTypeQd(IRFQd.CS_AUGMENTEE); } else { qd.setCsTypeQd(IRFQd.CS_STANDARD); }
                         */

                        /*
                         * if (rfQdVb.getIsPlafonnee()){ qd.setMntResiduel(RFUtils
                         * .getMntResiduel(rfQdVb.getLimiteAnnuelle(), augmentationQd, soldeDeCharge, "0.00")); }else{
                         * qd.setMntResiduel(""); }
                         */

                        qd.add(transaction);

                        // RFQdAssure
                        RFQdAssure qdAssure = new RFQdAssure();
                        qdAssure.setSession(rfQdVb.getSession());

                        // qdAssure.setIdMontantConvention("");
                        // qdAssure.setIdQdPrincipale(rfQdVb.getIdQdPrincipale());
                        qdAssure.setIdQdAssure(qd.getIdQd());
                        qdAssure.setDateDebut(rfQdVb.getDateDebut());
                        qdAssure.setDateFin(rfQdVb.getDateFin());

                        if (rfQdVb.getIsPlafonnee()) {
                            qdAssure.setIdPotSousTypeDeSoin(rfQdVb.getIdPotSousTypeDeSoin());
                        }

                        qdAssure.setIdSousTypeDeSoin(RFUtils.getIdSousTypeDeSoin(rfQdVb.getCodeTypeDeSoinList(),
                                rfQdVb.getCodeSousTypeDeSoinList(), rfQdVb.getSession()));

                        String idQd = qd.getIdQd();

                        qdAssure.add(transaction);

                        // Création du lien entre les dossiers et la Qd de base
                        if (RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(rfQdVb.getCodeTypeDeSoinList(),
                                rfQdVb.getCodeSousTypeDeSoinList())) {

                            if (rfQdVb.getMembresFamille().size() > 0) {
                                RFUtils.ajouterAssociationDossierQdMembreFamille(rfQdVb.getMembresFamille(),
                                        rfQdVb.getIdGestionnaire(), rfQdVb.getSession(), transaction, idQd,
                                        rfQdVb.getCsTypeBeneficiaire(), rfQdVb.getDateDebutPCAccordee());
                            } else {
                                RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_PERSONNES_DANS_CALCUL");
                            }

                        } else {
                            RFAssQdDossier rfAssQdDossier = new RFAssQdDossier();
                            rfAssQdDossier.setSession(rfQdVb.getSession());

                            rfAssQdDossier.setIdQd(idQd);
                            rfAssQdDossier.setIdDossier(rfQdVb.getIdDossier());
                            rfAssQdDossier.setTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
                            rfAssQdDossier.setIsComprisDansCalcul(Boolean.TRUE);

                            rfAssQdDossier.add(transaction);
                        }

                        ajouterSoldeDeChargeAugmentationDeQd(rfQdVb, session, transaction, idQd);
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

    /**
     * Suppression d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieQdViewBean rfQdVb = (RFSaisieQdViewBean) viewBean;

        validerSupprimerQd(rfQdVb);

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                if (IRFQd.CS_GRANDE_QD.equals(rfQdVb.getCsGenreQd())) {

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        RFQdPrincipale rfQdPrincipale = new RFQdPrincipale();
                        rfQdPrincipale.setSession(rfQdVb.getSession());
                        rfQdPrincipale.setIdQdPrincipale(rfQdVb.getIdQd());

                        rfQdPrincipale.retrieve(transaction);

                        if (!rfQdPrincipale.isNew()) {
                            rfQdPrincipale.delete(transaction);

                            // RFQd
                            RFQd qd = new RFQd();
                            qd.setSession(rfQdVb.getSession());
                            qd.setIdQd(rfQdVb.getIdQd());

                            qd.retrieve(transaction);

                            if (!qd.isNew()) {
                                qd.delete(transaction);

                                // Suppression des périodes de validités de la
                                // grande Qd
                                RFPeriodeValiditeQdPrincipaleManager rfPerValQdPriMgr = new RFPeriodeValiditeQdPrincipaleManager();
                                rfPerValQdPriMgr.setSession(rfQdVb.getSession());
                                rfPerValQdPriMgr.setForIdQd(rfQdVb.getIdQd());
                                rfPerValQdPriMgr.changeManagerSize(0);

                                rfPerValQdPriMgr.find(transaction);

                                rfPerValQdPriMgr.delete(transaction);

                                // Suppression du champ historisé Solde excédent de revenu
                                RFQdSoldeExcedentDeRevenuManager rfQdSolExcDeRevMgr = new RFQdSoldeExcedentDeRevenuManager();
                                rfQdSolExcDeRevMgr.setSession(rfQdVb.getSession());
                                rfQdSolExcDeRevMgr.setForIdQd(rfQdVb.getIdQdPrincipale());
                                rfQdSolExcDeRevMgr.changeManagerSize(0);

                                rfQdSolExcDeRevMgr.find(transaction);

                                rfQdSolExcDeRevMgr.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_delete()",
                                        "RFSaisieQdPiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_delete()", "RFSaisieQdPiedDePageHelper");
                        }

                    }

                }
                if (IRFQd.CS_PETITE_QD.equals(rfQdVb.getCsGenreQd())) {

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        RFQdAssure qdAssure = new RFQdAssure();
                        qdAssure.setSession(rfQdVb.getSession());
                        qdAssure.setIdQdAssure(rfQdVb.getIdQd());

                        qdAssure.retrieve(transaction);

                        if (!qdAssure.isNew()) {
                            qdAssure.delete(transaction);

                            RFQd qd = new RFQd();
                            qd.setSession(rfQdVb.getSession());
                            qd.setIdQd(rfQdVb.getIdQd());

                            qd.retrieve(transaction);

                            if (!qd.isNew()) {
                                qd.delete(transaction);
                            }
                        }
                    }
                }

                if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                    // Suppresion de l'association Qd - dossier

                    RFAssQdDossierManager rfAssQdDosMgr = new RFAssQdDossierManager();
                    rfAssQdDosMgr.setSession(rfQdVb.getSession());
                    rfAssQdDosMgr.setForIdQdBase(rfQdVb.getIdQd());
                    rfAssQdDosMgr.changeManagerSize(0);
                    rfAssQdDosMgr.find(transaction);

                    rfAssQdDosMgr.delete(transaction);

                    // Suppression des champs historisés

                    RFQdSoldeChargeManager rfQdSolChaMgr = new RFQdSoldeChargeManager();
                    rfQdSolChaMgr.setSession(rfQdVb.getSession());
                    rfQdSolChaMgr.setForIdQd(rfQdVb.getIdQd());
                    rfQdSolChaMgr.changeManagerSize(0);

                    rfQdSolChaMgr.find(transaction);

                    rfQdSolChaMgr.delete(transaction);

                    RFQdAugmentationManager rfQdAugMgr = new RFQdAugmentationManager();
                    rfQdAugMgr.setSession(rfQdVb.getSession());
                    rfQdAugMgr.setForIdQd(rfQdVb.getIdQd());
                    rfQdAugMgr.changeManagerSize(0);
                    rfQdAugMgr.find(transaction);

                    rfQdAugMgr.delete(transaction);
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
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        if (RFPropertiesUtils.afficherTypeRemboursement()) {
            ((RFSaisieQdViewBean) viewBean).setIsAfficherTypeRemboursement(Boolean.TRUE);
        }

        if (RFPropertiesUtils.afficherCaseRi()) {
            ((RFSaisieQdViewBean) viewBean).setIsAfficherCaseRi(Boolean.TRUE);
        }

        super._init(viewBean, action, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        super._retrieve(viewBean, action, session);

        RFSaisieQdViewBean vb = (RFSaisieQdViewBean) viewBean;

        vb.setAfficherDetail(true);

        // Recherche du détail du requerant et de la famille
        vb.rechercheDetailRequerantFamille();

        // Recherche du solde de charge
        vb.setSoldeCharge(RFUtils.getSoldeDeCharge(vb.getIdQd(), vb.getSession()));

        // Recherche de l'augmentation de Qd
        vb.setAugmentationQd(RFUtils.getAugmentationQd(vb.getIdQd(), vb.getSession()));

        // Recherche de la charge
        if (IRFQd.CS_GRANDE_QD.equals(vb.getCsGenreQd())) {
            // Recherche de l'excédent de revenu
            vb.setSoldeExcedentPCAccordee(RFUtils.getSoldeExcedentDeRevenu(vb.getIdQd(), vb.getSession()));
            vb.recherchePeriodesValidite();
        }

        if (RFPropertiesUtils.afficherTypeRemboursement()) {
            vb.setIsAfficherTypeRemboursement(Boolean.TRUE);

        }

        if (RFPropertiesUtils.afficherCaseRi()) {
            vb.setIsAfficherCaseRi(Boolean.TRUE);
        }
    }

    /**
     * Modification d'une Qd
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieQdViewBean rfQdVb = (RFSaisieQdViewBean) viewBean;

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(rfQdVb.getSession())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_PREPARER_DECISION_DEMARRE");
        }
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            if (IRFQd.CS_PETITE_QD.equals(rfQdVb.getCsGenreQd())) {
                validerModifierQdAssure(rfQdVb);
            }
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                if (IRFQd.CS_GRANDE_QD.equals(rfQdVb.getCsGenreQd())) {

                    // this.validerTypeBeneficiaireTypePc(rfQdVb);

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        // Modification de la Qd principale

                        RFQd qd = new RFQd();
                        qd.setSession(rfQdVb.getSession());
                        qd.setIdQd(rfQdVb.getIdQd());

                        qd.retrieve(transaction);

                        if (!qd.isNew()) {

                            qd.setCsEtat(rfQdVb.getCsEtat());

                            // On ne modifie pas les champs relatif au droit PC
                            qd.update(transaction);

                            // Fermeture de la période courante de validité si l'état est clôturé ou fermé et si la date
                            // de fin de la dernière p. val. est vide
                            if (/* rfQdVb.getCsEtat().equals(IRFQd.FERME) || */rfQdVb.getCsEtat().equals(
                                    IRFQd.CS_ETAT_QD_CLOTURE)) {

                                RFPeriodeValiditeQdPrincipaleManager rfPerValQdPriMgr = new RFPeriodeValiditeQdPrincipaleManager();
                                rfPerValQdPriMgr.setSession(rfQdVb.getSession());
                                rfPerValQdPriMgr.setForIdQd(rfQdVb.getIdQd());
                                rfPerValQdPriMgr.setForDerniereVersion(true);
                                rfPerValQdPriMgr.setOrderByDateDebutDesc(true);
                                rfPerValQdPriMgr.changeManagerSize(0);
                                rfPerValQdPriMgr.find(transaction);

                                if (rfPerValQdPriMgr.size() > 0) {

                                    RFPeriodeValiditeQdPrincipale rfDernierePeriodeValiditeQdPrincipale = (RFPeriodeValiditeQdPrincipale) rfPerValQdPriMgr
                                            .getFirstEntity();

                                    if (rfDernierePeriodeValiditeQdPrincipale != null) {

                                        if (JadeStringUtil.isBlankOrZero(rfDernierePeriodeValiditeQdPrincipale
                                                .getDateFin())) {

                                            JADate jaDateDebPVal = new JADate(
                                                    rfDernierePeriodeValiditeQdPrincipale.getDateDebut());
                                            JACalendar cal = new JACalendarGregorian();
                                            if (cal.compare(jaDateDebPVal, JACalendar.today()) == JACalendar.COMPARE_FIRSTLOWER) {

                                                RFPeriodeValiditeQdPrincipale rfPeriodeValiditeQdPrincipale = new RFPeriodeValiditeQdPrincipale();
                                                rfPeriodeValiditeQdPrincipale.setSession(rfQdVb.getSession());

                                                rfPeriodeValiditeQdPrincipale
                                                        .setIdQd(rfDernierePeriodeValiditeQdPrincipale.getIdQd());
                                                rfPeriodeValiditeQdPrincipale
                                                        .setDateDebut(rfDernierePeriodeValiditeQdPrincipale
                                                                .getDateDebut());
                                                rfPeriodeValiditeQdPrincipale.setDateFin(JACalendar.todayJJsMMsAAAA());
                                                rfPeriodeValiditeQdPrincipale
                                                        .setTypeModification(IRFQd.CS_MODIFICATION);

                                                rfPeriodeValiditeQdPrincipale
                                                        .setIdFamilleModification(rfDernierePeriodeValiditeQdPrincipale
                                                                .getIdFamilleModification());
                                                rfPeriodeValiditeQdPrincipale
                                                        .setIdPeriodeValModifiePar(rfDernierePeriodeValiditeQdPrincipale
                                                                .getIdPeriodeValidite());

                                                rfPeriodeValiditeQdPrincipale.setDateModification(JACalendar
                                                        .todayJJsMMsAAAA());
                                                rfPeriodeValiditeQdPrincipale.setConcerne(((BSession) session)
                                                        .getLabel("JSP_RF_QD_S_CLOTURE_FERMETURE"));

                                                rfPeriodeValiditeQdPrincipale.setIdGestionnaire(rfQdVb
                                                        .getIdGestionnaire());

                                                rfPeriodeValiditeQdPrincipale.add(transaction);
                                            }
                                        }

                                    }
                                }

                            }

                            RFQdPrincipale rfQdPri = new RFQdPrincipale();
                            rfQdPri.setSession(rfQdVb.getSession());

                            rfQdPri.setIdQdPrincipale(rfQdVb.getIdQd());

                            rfQdPri.retrieve();

                            if (!rfQdPri.isNew()) {
                                // NE PAS DECOMMENTER
                                // if (JadeStringUtil.isBlankOrZero(rfQdPri.getDateDebutPCAccordee())) {
                                rfQdPri.setCsTypeBeneficiaire(rfQdVb.getCsTypeBeneficiaire());
                                rfQdPri.setCsGenrePCAccordee(rfQdVb.getCsGenrePCAccordee());
                                rfQdPri.setCsTypePCAccordee(rfQdVb.getCsTypePCAccordee());

                                if (rfQdVb.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                                    rfQdPri.setRemboursementRequerant(rfQdVb.getTypeRemboursementRequerant());
                                    rfQdPri.setRemboursementConjoint(rfQdVb.getTypeRemboursementConjoint());
                                } else {
                                    rfQdPri.setRemboursementRequerant("");
                                    rfQdPri.setRemboursementConjoint("");
                                }

                                rfQdPri.setIsRI(rfQdVb.getIsRi());

                                rfQdPri.update(transaction);
                                // }
                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_update()",
                                        "RFSaisieQdPiedDePageHelper");
                            }

                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_update()", "RFSaisieQdPiedDePageHelper");
                        }

                    }

                }
                if (IRFQd.CS_PETITE_QD.equals(rfQdVb.getCsGenreQd())) {

                    if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        // Modification de la Qd assuré

                        RFQd qd = new RFQd();
                        qd.setSession(rfQdVb.getSession());
                        qd.setIdQd(rfQdVb.getIdQd());

                        qd.retrieve(transaction);

                        if (!qd.isNew()) {

                            qd.setCsEtat(rfQdVb.getCsEtat());
                            qd.update(transaction);

                            RFQdAssure rfQdAssure = new RFQdAssure();
                            rfQdAssure.setSession(rfQdVb.getSession());
                            rfQdAssure.setIdQdAssure(qd.getIdQd());

                            rfQdAssure.retrieve(transaction);

                            if (!rfQdAssure.isNew()) {
                                rfQdAssure.setDateFin(rfQdVb.getDateFin());

                                rfQdAssure.update(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_update()",
                                        "RFSaisieQdPiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(rfQdVb, "_update()", "RFSaisieQdPiedDePageHelper");
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
    }

    private String ajouterGrandeQd(RFSaisieQdViewBean rfQdVb, String idDossier, String typeRemboursementRequerent,
            String typeRemboursementConjoint, String csGenrePcAccordee, BISession session, BITransaction transaction)
            throws Exception {

        // creation de la QD de droit PC
        String idQd = "";

        // RFQd
        RFQd qd = new RFQd();
        qd.setSession(rfQdVb.getSession());

        if (rfQdVb.getIsPlafonnee()) {
            qd.setLimiteAnnuelle(rfQdVb.getLimiteAnnuelle());
        } else {
            qd.setLimiteAnnuelle("");
        }
        qd.setIsPlafonnee(rfQdVb.getIsPlafonnee());
        qd.setDateCreation(rfQdVb.getDateCreation());
        qd.setAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(rfQdVb.getDateDebut()));
        qd.setCsGenreQd(IRFQd.CS_GRANDE_QD);
        qd.setCsSource(IRFQd.CS_SOURCE_QD_GESTIONNAIRE);
        qd.setCsEtat(rfQdVb.getCsEtat());// IRFQd.OUVERT);
        qd.setIdGestionnaire(rfQdVb.getGestionnaire());
        /*
         * if (!JadeStringUtil.isBlankOrZero(rfQdVb.getAugmentationQd())) { qd.setCsTypeQd(IRFQd.CS_AUGMENTEE); } else {
         * qd.setCsTypeQd(IRFQd.CS_STANDARD); }
         */

        /*
         * if (rfQdVb.getIsPlafonnee()){ qd.setMntResiduel(RFUtils .getMntResiduel(rfQdVb.getLimiteAnnuelle(),
         * augmentationQd, soldeDeCharge, "0.00")); }
         */

        qd.add(transaction);

        idQd = qd.getIdQd();

        // RFQdPrincipale
        RFQdPrincipale qdPrincipale = new RFQdPrincipale();
        qdPrincipale.setSession(rfQdVb.getSession());

        qdPrincipale.setCsGenrePCAccordee(csGenrePcAccordee);
        qdPrincipale.setCsTypePCAccordee(rfQdVb.getCsTypePCAccordee());
        qdPrincipale.setCsTypeBeneficiaire(rfQdVb.getCsTypeBeneficiaire());
        qdPrincipale.setDateDebutPCAccordee(rfQdVb.getDateDebutPCAccordee());
        qdPrincipale.setDateFinPCAccordee(rfQdVb.getDateFinPCAccordee());
        qdPrincipale.setExcedentPCAccordee(rfQdVb.getExcedentPCAccordee());
        qdPrincipale.setIdQdPrincipale(qd.getIdQd());

        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
            qdPrincipale.setRemboursementConjoint(typeRemboursementConjoint);
            qdPrincipale.setRemboursementRequerant(typeRemboursementRequerent);
        } else {
            qdPrincipale.setRemboursementConjoint("");
            qdPrincipale.setRemboursementRequerant("");
        }

        qdPrincipale.setCsDegreApi(rfQdVb.getCsDegreApi());

        if (rfQdVb.getIsPlafonnee()) {
            qdPrincipale.setIdPotDroitPc(rfQdVb.getIdPotDroitPC());
        } else {
            qdPrincipale.setIdPotDroitPc("");
        }

        qdPrincipale.setIsRI(rfQdVb.getIsRi());

        qdPrincipale.add(transaction);

        // Création de la période de validité
        int famModifCompteur = 0;
        RFPeriodeValiditeQdPrincipaleManager mgr = new RFPeriodeValiditeQdPrincipaleManager();
        mgr.setSession(rfQdVb.getSession());
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

        RFPeriodeValiditeQdPrincipale rfPeriodeValiditeQdPrincipale = new RFPeriodeValiditeQdPrincipale();
        rfPeriodeValiditeQdPrincipale.setSession(rfQdVb.getSession());

        rfPeriodeValiditeQdPrincipale.setIdQd(idQd);

        rfPeriodeValiditeQdPrincipale.setDateDebut(rfQdVb.getDateDebut());
        rfPeriodeValiditeQdPrincipale.setDateFin(rfQdVb.getDateFin());

        rfPeriodeValiditeQdPrincipale.setTypeModification(IRFQd.CS_AJOUT);
        rfPeriodeValiditeQdPrincipale.setDateModification(JadeDateUtil.getDMYDate(new Date()));
        rfPeriodeValiditeQdPrincipale.setIdPeriodeValModifiePar("");
        rfPeriodeValiditeQdPrincipale.setConcerne(((BSession) session).getLabel("JSP_RF_QD_S_CREATION_MANUELLE"));
        rfPeriodeValiditeQdPrincipale.setIdFamilleModification(Integer.toString(famModifCompteur));

        rfPeriodeValiditeQdPrincipale.setIdGestionnaire(rfQdVb.getIdGestionnaire());

        rfPeriodeValiditeQdPrincipale.add(transaction);

        // Création du solde excédent de revenu
        if (!JadeStringUtil.isBlankOrZero(rfQdVb.getSoldeExcedentPCAccordee())) {

            // calcule le nouvel id unique de famille de modification
            famModifCompteur = 0;

            RFQdSoldeExcedentDeRevenuManager rfQdSolExcDeRevMgr = new RFQdSoldeExcedentDeRevenuManager();
            rfQdSolExcDeRevMgr.setSession((BSession) session);
            rfQdSolExcDeRevMgr.setForIdFamilleMax(true);
            rfQdSolExcDeRevMgr.changeManagerSize(0);
            rfQdSolExcDeRevMgr.find(transaction);

            if (!rfQdSolExcDeRevMgr.isEmpty()) {
                RFQdSoldeExcedentDeRevenu sc = (RFQdSoldeExcedentDeRevenu) rfQdSolExcDeRevMgr.getFirstEntity();
                if (null != sc) {
                    famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                } else {
                    famModifCompteur = 1;
                }
            } else {
                famModifCompteur = 1;
            }

            RFQdSoldeExcedentDeRevenu rfQdSolExcDeRev = new RFQdSoldeExcedentDeRevenu();
            rfQdSolExcDeRev.setSession(rfQdVb.getSession());
            rfQdSolExcDeRev.setIdFamilleModification(Integer.toString(famModifCompteur));
            rfQdSolExcDeRev.setConcerne(((BSession) session).getLabel("JSP_RF_QD_S_CREATION_MANUELLE"));
            rfQdSolExcDeRev.setIdQd(idQd);
            rfQdSolExcDeRev.setTypeModification(IRFQd.CS_AJOUT);
            rfQdSolExcDeRev.setVisaGestionnaire(rfQdVb.getGestionnaire());
            rfQdSolExcDeRev.setMontantSoldeExcedent(rfQdVb.getSoldeExcedentPCAccordee());
            rfQdSolExcDeRev.setDateModification(JadeDateUtil.getDMYDate(new Date()));
            rfQdSolExcDeRev.setIdSoldeExcedentModifie("");

            rfQdSolExcDeRev.add(transaction);

        }

        // Création du lien entre le dossier et la Qd de base
        if (RFUtils.typeBeneficiairePlusieursPersonnesComprisesDansCalcul.contains(rfQdVb.getCsTypeBeneficiaire())) {

            if (rfQdVb.getMembresFamille().size() > 0) {
                RFUtils.ajouterAssociationDossierQdMembreFamille(rfQdVb.getMembresFamille(), rfQdVb.getGestionnaire(),
                        rfQdVb.getSession(), transaction, idQd, rfQdVb.getCsTypeBeneficiaire(), rfQdVb.getDateDebutPCAccordee());
            } else {
                RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_PERSONNES_DANS_CALCUL");
            }

            // Un seul dossier
        } else {
            RFAssQdDossier rfAssQdDossier = new RFAssQdDossier();
            rfAssQdDossier.setSession(rfQdVb.getSession());

            rfAssQdDossier.setIdQd(idQd);
            rfAssQdDossier.setIdDossier(idDossier);
            rfAssQdDossier.setTypeRelation(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
            rfAssQdDossier.setIsComprisDansCalcul(Boolean.TRUE);

            rfAssQdDossier.add(transaction);
        }

        return idQd;
    }

    private void ajouterSoldeDeChargeAugmentationDeQd(RFSaisieQdViewBean rfQdVb, BISession session,
            BITransaction transaction, String idQd) throws Exception {

        // if (!FWViewBeanInterface.ERROR.equals(rfQdVb.getMsgType())) {

        String soldeDeCharge = rfQdVb.getSoldeCharge();
        String augmentationQd = rfQdVb.getAugmentationQd();

        // Création du solde de charge
        if (!JadeStringUtil.isBlankOrZero(soldeDeCharge)) {

            // calcule le nouvel id unique de famille de
            // modification
            int famModifCompteur = 0;

            RFQdSoldeChargeManager mgr = new RFQdSoldeChargeManager();
            mgr.setSession((BSession) session);
            mgr.setForIdFamilleMax(true);
            mgr.changeManagerSize(0);
            mgr.find(transaction);

            if (!mgr.isEmpty()) {
                RFQdSoldeCharge sc = (RFQdSoldeCharge) mgr.getFirstEntity();
                if (null != sc) {
                    famModifCompteur = JadeStringUtil.parseInt(sc.getIdFamilleModification(), 0) + 1;
                } else {
                    famModifCompteur = 1;
                }
            } else {
                famModifCompteur = 1;
            }

            RFQdSoldeCharge rfSoldeDeCharge = new RFQdSoldeCharge();
            rfSoldeDeCharge.setSession(rfQdVb.getSession());
            rfSoldeDeCharge.setIdFamilleModification(Integer.toString(famModifCompteur));
            rfSoldeDeCharge.setConcerne(((BSession) session).getLabel("JSP_RF_QD_S_CREATION_MANUELLE"));
            rfSoldeDeCharge.setIdQd(idQd);
            rfSoldeDeCharge.setTypeModification(IRFQd.CS_AJOUT);
            rfSoldeDeCharge.setVisaGestionnaire(rfQdVb.getGestionnaire());
            rfSoldeDeCharge.setMontantSolde(soldeDeCharge);
            rfSoldeDeCharge.setDateModification(JadeDateUtil.getDMYDate(new Date()));
            rfSoldeDeCharge.setIdSoldeChargeModifie("");

            rfSoldeDeCharge.add(transaction);

        }

        // Création de l'augmentation de QD
        if (!JadeStringUtil.isBlankOrZero(augmentationQd) && rfQdVb.getIsPlafonnee()) {

            // calcul le nouvel id unique de famille de modification
            int famModifCompteurAug = 0;

            RFQdAugmentationManager mgr = new RFQdAugmentationManager();
            mgr.setSession((BSession) session);
            mgr.setForIdFamilleMax(true);
            mgr.changeManagerSize(0);
            mgr.find(transaction);

            if (!mgr.isEmpty()) {
                RFQdAugmentation aug = (RFQdAugmentation) mgr.getFirstEntity();
                if (null != aug) {
                    famModifCompteurAug = JadeStringUtil.parseInt(aug.getIdFamilleModification(), 0) + 1;
                } else {
                    famModifCompteurAug = 1;
                }
            } else {
                famModifCompteurAug = 1;
            }

            RFQdAugmentation rfAugmentation = new RFQdAugmentation();
            rfAugmentation.setSession(rfQdVb.getSession());
            rfAugmentation.setIdFamilleModification(Integer.toString(famModifCompteurAug));
            // rfAugmentation.setDateModification(JACalendar.todayJJsMMsAAAA());
            rfAugmentation.setConcerne(((BSession) session).getLabel("JSP_RF_QD_S_CREATION_MANUELLE"));
            rfAugmentation.setIdQd(idQd);
            rfAugmentation.setTypeModification(IRFQd.CS_AJOUT);
            rfAugmentation.setVisaGestionnaire(rfQdVb.getGestionnaire());
            rfAugmentation.setMontantAugmentationQd(augmentationQd);
            rfAugmentation.setDateModification(JadeDateUtil.getDMYDate(new Date()));
            rfAugmentation.setIdAugmentationQdModifiePar("");

            rfAugmentation.add(transaction);

        }
        // }

    }

    /**
     * 
     * Créé directement une QD pour chaque personne du couple séparé par la maladie
     * 
     * @param rfQdVb
     * @param session
     * @param transaction
     * @throws Exception
     */
    private void ajoutGrandeQdCoupleSepare(RFSaisieQdViewBean rfQdVb, BISession session, BITransaction transaction)
            throws Exception {

        boolean isRequerantTrouve = false;
        boolean hasQdAjoute = false;
        boolean isConjointTrouve = false;
        boolean isHomeTrouve = false;
        boolean isDomicileTrouve = false;
        boolean hasMembre1DroitPC = false;
        boolean hasMembre2DroitPC = false;
        int i = 0;
        String idQd = "";
        String csGenrePcAccordeeInitial = rfQdVb.getCsGenrePCAccordee();
        String typeRemboursementConjoint = "";
        String typeRemboursementRequerant = "";

        if (rfQdVb.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
            rfQdVb.setTypeRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
        }

        // On crée une QD pour le conjoint et le requérent
        for (String[] membreCourant : rfQdVb.getMembresFamille()) {

            if (membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
                isRequerantTrouve = true;
            }
            if (membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                isConjointTrouve = true;
            }

            if (membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)
                    || membreCourant[1].equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {

                // Traitement du conjoint
                if (!rfQdVb.getIdTiers().equals(membreCourant[0])) {

                    // Si HOME DOMICILE, on adapte le genre de la PC accordée
                    if (rfQdVb.getCsTypeBeneficiaire().equals(
                            IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)) {

                        rfQdVb.setCsGenrePCAccordee(csGenrePcAccordeeInitial.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE) ? IPCPCAccordee.CS_GENRE_PC_HOME
                                : IPCPCAccordee.CS_GENRE_PC_DOMICILE);

                    }

                    typeRemboursementConjoint = rfQdVb.getTypeRemboursementRequerant();
                    typeRemboursementRequerant = rfQdVb.getTypeRemboursementConjoint();

                } else {

                    rfQdVb.setCsGenrePCAccordee(csGenrePcAccordeeInitial);

                    typeRemboursementConjoint = rfQdVb.getTypeRemboursementConjoint();
                    typeRemboursementRequerant = rfQdVb.getTypeRemboursementRequerant();
                }

                retrievePotQdPrincipaleInfoDroitPc(rfQdVb, membreCourant[0]);

                i++;

                RFPrDemandeJointDossier rfPrDemJoiDos = RFUtils.getDossierJointPrDemande(membreCourant[0],
                        (BSession) session);
                String idDossier = "";
                if (null == rfPrDemJoiDos) {
                    idDossier = RFUtils.ajouterDossier(membreCourant[0], rfQdVb.getIdGestionnaire(),
                            (BSession) session, transaction);
                } else {
                    idDossier = rfPrDemJoiDos.getIdDossier();
                }

                if (rfQdVb.getIsDroitPC().booleanValue()) {

                    if (rfQdVb.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                        isDomicileTrouve = true;
                    }
                    if (rfQdVb.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                        isHomeTrouve = true;
                    }

                    if (i == 1) {
                        hasMembre1DroitPC = true;
                    } else {
                        hasMembre2DroitPC = true;
                    }
                }

                if (validerAjouterQdCoupleSepare(rfQdVb, membreCourant[0], membreCourant[2])) {

                    idQd = ajouterGrandeQd(rfQdVb, idDossier, typeRemboursementRequerant, typeRemboursementConjoint,
                            rfQdVb.getCsGenrePCAccordee(), session, transaction);
                    ajouterSoldeDeChargeAugmentationDeQd(rfQdVb, session, transaction, idQd);

                    hasQdAjoute = true;
                }

            }

        }

        if (!hasQdAjoute) {
            RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_QDDROITPC_DEJA_EXISTANTE");
        }

        if (rfQdVb.getIsDroitPC().booleanValue()) {

            if (rfQdVb.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)) {

                if (!(isRequerantTrouve && isConjointTrouve) && !(hasMembre1DroitPC == hasMembre2DroitPC)
                        && !(isHomeTrouve && isDomicileTrouve)) {
                    RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_PERSONNES_CONCERNEES_QD");
                }

            } else {

                if (!(isRequerantTrouve && isConjointTrouve) && !(hasMembre1DroitPC == hasMembre2DroitPC)
                        && !isDomicileTrouve) {
                    RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_PERSONNES_CONCERNEES_QD");
                }

            }

        } else {

            if (!(isRequerantTrouve && isConjointTrouve) || hasMembre1DroitPC || hasMembre2DroitPC) {
                RFUtils.setMsgErreurViewBean(rfQdVb, "ERREUR_RF_QD_S_PERSONNES_CONCERNEES_QD");
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

    private Set<String> getMembresFamilleViewBean(RFSaisieQdViewBean viewBean, boolean uniquementComprisDansCalcul) {

        Set<String> idsTiersMembreFamille = new HashSet<String>();

        for (String[] idMembreFamille : viewBean.getMembresFamille()) {
            if (!uniquementComprisDansCalcul || idMembreFamille[8].equals(Boolean.TRUE.toString())) {
                idsTiersMembreFamille.add(idMembreFamille[0]);
            }
        }

        return idsTiersMembreFamille;
    }

    private boolean isDemandeImputeeSurQd(RFSaisieQdViewBean vb) throws Exception {

        RFDemandeManager rfDemMgr = new RFDemandeManager();
        rfDemMgr.setSession(vb.getSession());
        if (IRFQd.CS_GRANDE_QD.equals(vb.getCsGenreQd())) {
            rfDemMgr.setForIdQdPrincipale(vb.getIdQd());
        } else {
            rfDemMgr.setForIdQdAssure(vb.getIdQd());
        }
        rfDemMgr.changeManagerSize(0);
        rfDemMgr.find();

        if (rfDemMgr.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public FWViewBeanInterface majLimiteAnnuelleQdAssure(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        RFSaisieQdViewBean vb = (RFSaisieQdViewBean) viewBean;

        qdDateValidate(vb);

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            retrieveQdPrincipale(vb);
        }

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            retrievePotSousTypeDeSoin(vb);
        } else {
            vb.setLimiteAnnuelle("");
            vb.setIsLimiteAnnuelleOk(false);
            vb.setIdPotSousTypeDeSoin("");
        }

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            vb.setIsFamilleOk(true);
        }

        return vb;

    }

    public FWViewBeanInterface majLimiteAnnuelleQdDroitPC(FWViewBeanInterface viewBean, FWAction action,
            BSession session) throws Exception {

        RFSaisieQdViewBean vb = (RFSaisieQdViewBean) viewBean;

        qdDateValidate(vb);

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            retrievePotQdPrincipaleInfoDroitPc(vb, "");
        } else {
            vb.setLimiteAnnuelle("");
            vb.setIsLimiteAnnuelleOk(false);
            vb.setIdPotDroitPC("");
        }

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            vb.setIsFamilleOk(true);
        }

        return vb;

    }

    private void qdDateValidate(RFSaisieQdViewBean vb) throws Exception {

        // Date de début obligatoire
        if (!JadeStringUtil.isBlankOrZero(vb.getDateDebut())) {

            // Date de fin plus grande que celle de début
            if (!JadeStringUtil.isBlankOrZero(vb.getDateFin())) {

                JACalendar cal = new JACalendarGregorian();
                JADate dateFin = new JADate(vb.getDateFin());
                JADate dateDebut = new JADate(vb.getDateDebut());

                if (cal.compare(dateDebut, dateFin) == JACalendar.COMPARE_FIRSTUPPER) {
                    RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_DATE_FIN_ANTERIEUR_DATE_DE_DEBUT");
                }
                // Doit concerner la même année
                if (dateDebut.getYear() != dateFin.getYear()) {
                    RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_PERIODE_VALIDITE_DATE_MEME_ANNEE");
                }
            }

        } else {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_DATE_DE_DEBUT_OBLIGATOIRE");
        }

    }

    private void retrievePotQdPrincipaleInfoDroitPc(RFSaisieQdViewBean viewBean, String idTiersCoupleSepare)
            throws Exception {

        // Ajout des personnes comprises dans la situation familiale des rentes
        BITransaction transaction = null;
        try {
            transaction = (viewBean.getSession()).newTransaction();
            transaction.openTransaction();

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                viewBean = RFRetrievePotQdPrincipaleInfoDroitPcService.retrieve(viewBean, idTiersCoupleSepare,
                        (BTransaction) transaction);

                if (JadeStringUtil.isBlankOrZero(idTiersCoupleSepare)) {

                    // Ajout des personnes comprises dans le calcul PC
                    if (viewBean.getIsDroitPC().booleanValue()) {

                        if (viewBean.getPersonnesDansPlanCalculList().size() > 0) {

                            Vector<String[]> membresFamilleVec = new Vector<String[]>();

                            for (PCAMembreFamilleVO perDsCal : viewBean.getPersonnesDansPlanCalculList()) {

                                membresFamilleVec.add(RFUtils.getMembreFamilleTabString(perDsCal.getIdTiers(),
                                        perDsCal.getCsRoleFamillePC(), perDsCal.getNss(), perDsCal.getNom(),
                                        perDsCal.getPrenom(), perDsCal.getDateNaissance(), perDsCal.getCsSexe(),
                                        perDsCal.getCsNationalite(), perDsCal.getIsComprisDansCalcul(), perDsCal.getIsRentier()));
                            }

                            viewBean.setMembresFamille(membresFamilleVec);
                        }

                    } else {

                        // Recherche dans la situation familliale des rentes

                        viewBean.setMembresFamille(RFUtils.getMembreFamille(
                                (BTransaction) transaction,
                                viewBean.getIdTiers(),
                                viewBean.getDateDebut(),
                                viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.ENFANTS_AVEC_ENFANTS) ? true
                                        : false, viewBean.getSession()));

                        // Si pas de situation familliale on ajoute le tiers
                        if ((viewBean.getMembresFamille() == null) || (viewBean.getMembresFamille().size() == 0)) {

                            Vector<String[]> membresFamVec = new Vector<String[]>();

                            membresFamVec.add(RFUtils.getMembreFamilleTabString(viewBean.getIdTiers(),
                                    IPCDroits.CS_ROLE_FAMILLE_REQUERANT, viewBean.getNss(), viewBean.getNom(),
                                    viewBean.getPrenom(), viewBean.getDateNaissance(), viewBean.getCsSexe(),
                                    viewBean.getCsNationalite(), Boolean.TRUE));

                            viewBean.setMembresFamille(membresFamVec);
                        }
                    }
                }

            }

        }/* catch (RFAdaptationJournaliereTiersNonComprisException e) { } */
        catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        } finally {
            transaction.closeTransaction();
        }
    }

    private void retrievePotSousTypeDeSoin(RFSaisieQdViewBean vb) throws Exception {

        if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            RFRetrieveLimiteAnnuelleSousTypeDeSoinService rfLimAnnSouTypDeSoiSer = new RFRetrieveLimiteAnnuelleSousTypeDeSoinService();
            String resultat[] = rfLimAnnSouTypDeSoiSer.getLimiteAnnuelleTypeDeSoinIdTiers(vb.getSession(),
                    vb.getCodeTypeDeSoinList(), vb.getCodeSousTypeDeSoinList(), vb.getIdTiers(), vb.getDateDebut(),
                    null, vb.getCsTypeBeneficiaire(), vb.getCsGenrePCAccordee(), vb.getDateNaissance());

            if (!JadeStringUtil.isBlankOrZero(resultat[0]) && !JadeStringUtil.isBlankOrZero(resultat[1])) {
                vb.setLimiteAnnuelle(resultat[0]);
                vb.setIsLimiteAnnuelleOk(true);
                vb.setIdPotSousTypeDeSoin(resultat[1]);
            } else {
                if (vb.getIsPlafonnee()) {
                    RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_POT_SOUS_TYPE_DE_SOIN_NON_TROUVE");
                }
                vb.setLimiteAnnuelle("");
                vb.setIsLimiteAnnuelleOk(false);
                vb.setIdPotSousTypeDeSoin("");
            }

            RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
            rfAssQdDossierJointDossierJointTiersMgr.setSession(vb.getSession());
            rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(vb.getIdQdPrincipale());
            rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
            rfAssQdDossierJointDossierJointTiersMgr.find();

            if (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) {

                Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr
                        .iterator();
                Vector<String[]> membresFamVec = new Vector<String[]>();
                while (rfAssQdDossierJointDossierJointTiersIter.hasNext()) {

                    RFAssQdDossierJointDossierJointTiers rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter
                            .next();

                    if (null != rfAssQdDossierJointDossierJointTiers) {

                        membresFamVec.add(RFUtils.getMembreFamilleTabString(
                                rfAssQdDossierJointDossierJointTiers.getIdTiers(),
                                rfAssQdDossierJointDossierJointTiers.getTypeRelation(),
                                rfAssQdDossierJointDossierJointTiers.getNss(),
                                rfAssQdDossierJointDossierJointTiers.getNom(),
                                rfAssQdDossierJointDossierJointTiers.getPrenom(),
                                rfAssQdDossierJointDossierJointTiers.getDateNaissance(),
                                rfAssQdDossierJointDossierJointTiers.getCsSexe(),
                                rfAssQdDossierJointDossierJointTiers.getCsNationalite(),
                                rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()));
                    }
                }
                vb.setMembresFamille(membresFamVec);
            }

            vb.setIsFamilleOk(true);
        }
    }

    private void retrieveQdPrincipale(RFSaisieQdViewBean viewBean) throws Exception {

        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerValJointDosJointTieJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerValJointDosJointTieJointDemMgr.setSession(viewBean.getSession());
        rfQdJointPerValJointDosJointTieJointDemMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
        rfQdJointPerValJointDosJointTieJointDemMgr.setForIdTiers(viewBean.getIdTiers());
        rfQdJointPerValJointDosJointTieJointDemMgr.setComprisDansCalcul(true);

        // TODO: mouefffe... a voir une période assuré ne doit pas forcément correspondre à une P. val
        if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebut())) {
            rfQdJointPerValJointDosJointTieJointDemMgr.setForAnneeQd(PRDateFormater
                    .convertDate_JJxMMxAAAA_to_AAAA(viewBean.getDateDebut()));
            rfQdJointPerValJointDosJointTieJointDemMgr.setForDateDebutBetweenPeriode(viewBean.getDateDebut());
        }
        rfQdJointPerValJointDosJointTieJointDemMgr.changeManagerSize(0);
        rfQdJointPerValJointDosJointTieJointDemMgr.find();

        // Doit être égal à un, voir validation P. val Qd
        if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 1) {
            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfQdJointPerValJointDosJointTieJointDem = (RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfQdJointPerValJointDosJointTieJointDemMgr
                    .getFirstEntity();
            if (null != rfQdJointPerValJointDosJointTieJointDem) {
                viewBean.setIdQdPrincipale(rfQdJointPerValJointDosJointTieJointDem.getIdQdPrincipale());
                viewBean.setCsTypeBeneficiaire(rfQdJointPerValJointDosJointTieJointDem.getCsTypeBeneficiaire());
                viewBean.setCsTypePCAccordee(rfQdJointPerValJointDosJointTieJointDem.getCsTypePCAccordee());
                viewBean.setCsGenrePCAccordee(rfQdJointPerValJointDosJointTieJointDem.getCsGenrePCAccordee());
            } else {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDDROITPC_NON_EXISTANTE");
            }

        } else {
            if (rfQdJointPerValJointDosJointTieJointDemMgr.size() == 0) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDDROITPC_NON_EXISTANTE");
            } else {
                RFUtils.setMsgErreurInattendueViewBean(viewBean, "retrieveQdPrincipale()", "RFSaisieQdPiedDePageHelper");
            }
        }
    }

    /**
     * Valide l'ajout d'une Qd assuré
     * 
     * @param RFSaisieQdViewBean
     * @throws Exception
     */
    private void validerAjouterQdAssure(RFSaisieQdViewBean viewBean) throws Exception {

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(viewBean.getSession())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_PREPARER_DECISION_DEMARRE");
        }

        if (!viewBean.getIsPlafonnee()) {
            retrieveQdPrincipale(viewBean);
        }

        // Date de début obligatoire
        qdDateValidate(viewBean);

        // Limmite annuelle
        if (JadeStringUtil.isBlankOrZero(viewBean.getIdPotSousTypeDeSoin())) {
            if (viewBean.getIsPlafonnee()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_POT_QD_ASSURE");
                viewBean.setIsLimiteAnnuelleOk(false);
            }
        }

        if (!viewBean.getIsFamilleOk()) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QD_ASSURE_FAMILLE");
        }

        // Date de création obligatoire
        if (JadeStringUtil.isBlankOrZero(viewBean.getDateCreation())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_DATE_DE_CREATION_OBLIGATOIRE");
        }

        // Gestionnaire obligatoire
        if (JadeStringUtil.isBlank(viewBean.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_GESTIONNAIRE_NON_SELECTIONNE");
        }

        // Id Qd principale obligatoire
        if (JadeStringUtil.isBlank(viewBean.getIdQdPrincipale())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDDROITPC_NON_EXISTANTE");
        }

        // La période d'une Qd assure concernant un groupe de personnes et un sous type de soin
        // -> ne doivent pas se chevaucher
        if (!viewBean.getCsEtat().equals(IRFQd.CS_ETAT_QD_CLOTURE)) {

            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())
                    && viewBean.getCsEtat().equals(IRFQd.CS_ETAT_QD_OUVERT)) {

                RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                        .getRFQdAssureJointDossierJointTiersManager(
                                viewBean.getSession(),
                                RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(
                                        viewBean.getCodeTypeDeSoinList(), viewBean.getCodeSousTypeDeSoinList()) ? getMembresFamilleViewBean(
                                        viewBean, false) : null,
                                RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(
                                        viewBean.getCodeTypeDeSoinList(), viewBean.getCodeSousTypeDeSoinList()) ? null
                                        : viewBean.getIdTiers(), viewBean.getCodeTypeDeSoinList(), viewBean
                                        .getCodeSousTypeDeSoinList(), IRFQd.CS_ETAT_QD_OUVERT, "", "", "", "", true,
                                "", "");

                if (rfQdAssJointDosJointTieMgr.size() > 0) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDASSURE_OUVERTE_DEJA_EXISTANTE");
                }
            }

            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                        .getRFQdAssureJointDossierJointTiersManager(
                                viewBean.getSession(),
                                RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(
                                        viewBean.getCodeTypeDeSoinList(), viewBean.getCodeSousTypeDeSoinList()) ? getMembresFamilleViewBean(
                                        viewBean, false) : null,
                                RFUtils.isSousTypeDeSoinCodeConcernePlusieursPersonnes(
                                        viewBean.getCodeTypeDeSoinList(), viewBean.getCodeSousTypeDeSoinList()) ? null
                                        : viewBean.getIdTiers(),
                                viewBean.getCodeTypeDeSoinList(),
                                viewBean.getCodeSousTypeDeSoinList(),
                                "",
                                PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(viewBean.getDateDebut()),
                                viewBean.getDateDebut(),
                                JadeStringUtil.isBlankOrZero(viewBean.getDateFin()) ? RFUtils.MAX_DATE_VALUE : viewBean
                                        .getDateFin(), "", true, "", "");

                if (rfQdAssJointDosJointTieMgr.size() > 0) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDASSURE_DEJA_EXISTANTE");
                }
            }
        }

        // Impossible de créer une QdAssuré non plafonnée concernant un sousType
        // de soin possédant une limite
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()) && !viewBean.getIsPlafonnee()) {

            RFRetrieveLimiteAnnuelleSousTypeDeSoinService rfRetrieveLimiteAnnuelleSousTypeDeSoinService = new RFRetrieveLimiteAnnuelleSousTypeDeSoinService();

            String[] resultat = rfRetrieveLimiteAnnuelleSousTypeDeSoinService.getLimiteAnnuelleTypeDeSoinIdTiers(
                    viewBean.getSession(), viewBean.getCodeTypeDeSoinList(), viewBean.getCodeSousTypeDeSoinList(),
                    viewBean.getIdTiers(), viewBean.getDateDebut(), null, viewBean.getCsTypeBeneficiaire(),
                    viewBean.getCsTypePCAccordee(), viewBean.getDateNaissance());

            if (!JadeStringUtil.isBlankOrZero(resultat[0])) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDASSURE_NON_PLAFONEE_LIMITE_SOUS_TYPE");
            }
        }

        validerArrondi(viewBean);
    }

    private boolean validerAjouterQdCoupleSepare(RFSaisieQdViewBean viewBean, String idTiers, String nss)
            throws Exception {

        Set<String> idsTiersSet = new HashSet<String>();
        idsTiersSet.add(idTiers);

        if (viewBean.getCsEtat().equals(IRFQd.CS_ETAT_QD_OUVERT)) {

            // Si l'état de la qd à ajouter est ouvert on test si il
            // n'existe pas de qd ouverte pour cette année
            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerJointDosJointTiersJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
            rfQdJointPerJointDosJointTiersJointDemMgr.setSession(viewBean.getSession());

            rfQdJointPerJointDosJointTiersJointDemMgr.setForIdsTiers(idsTiersSet);
            rfQdJointPerJointDosJointTiersJointDemMgr.setComprisDansCalcul(true);
            rfQdJointPerJointDosJointTiersJointDemMgr.setForCsEtatQd(IRFQd.CS_ETAT_QD_OUVERT);
            rfQdJointPerJointDosJointTiersJointDemMgr.changeManagerSize(0);
            rfQdJointPerJointDosJointTiersJointDemMgr.find();

            if (rfQdJointPerJointDosJointTiersJointDemMgr.size() > 0) {
                return false;
            }

        }

        // on test ensuite si il existe une période de validité couvrant la
        // période de la Qd à ajouter
        RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerJointDosJointTiersJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
        rfQdJointPerJointDosJointTiersJointDemMgr.setSession(viewBean.getSession());
        rfQdJointPerJointDosJointTiersJointDemMgr.setForIdsTiers(idsTiersSet);
        rfQdJointPerJointDosJointTiersJointDemMgr.setComprisDansCalcul(true);
        rfQdJointPerJointDosJointTiersJointDemMgr.setForAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(viewBean
                .getDateDebut()));
        rfQdJointPerJointDosJointTiersJointDemMgr.setForDateDebutBetweenPeriode(viewBean.getDateDebut());

        rfQdJointPerJointDosJointTiersJointDemMgr.setForDateFinBetweenPeriode(JadeStringUtil.isBlankOrZero(viewBean
                .getDateFin()) ? RFUtils.MAX_DATE_VALUE : viewBean.getDateFin());
        rfQdJointPerJointDosJointTiersJointDemMgr.changeManagerSize(0);
        rfQdJointPerJointDosJointTiersJointDemMgr.find();

        if (rfQdJointPerJointDosJointTiersJointDemMgr.size() > 0) {
            return false;
        }

        return true;
    }

    /**
     * Valide l'ajout d'une Qd de DroitPc
     * 
     * @param RFSaisieQdViewBean
     * @throws Exception
     */
    private void validerAjouterQdDroitPc(RFSaisieQdViewBean viewBean, boolean isCoupleSepare) throws Exception {

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(viewBean.getSession())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_PREPARER_DECISION_DEMARRE");
        }

        // Le test pour détérminer si le dossier exsiste pour cet assuré est
        // réalisé dans RFSaisieQdChoixGenreHelper

        // Date de début obligatoire
        qdDateValidate(viewBean);

        // Date de création obligatoire
        if (JadeStringUtil.isBlankOrZero(viewBean.getDateCreation())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_DATE_DE_CREATION_OBLIGATOIRE");
        }

        // Limite annuelle obligatoire
        if (JadeStringUtil.isBlankOrZero(viewBean.getIdPotDroitPC())) {
            if (viewBean.getIsPlafonnee() && !viewBean.getIsLimiteAnnuelleOk()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_POT_QD_DROIT_PC");
                viewBean.setIsLimiteAnnuelleOk(false);
            }
        }

        if (!viewBean.getIsFamilleOk()) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QD_DROIT_PC");
        }

        // Gestionnaire obligatoire
        if (JadeStringUtil.isBlank(viewBean.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_GESTIONNAIRE_NON_SELECTIONNE");
        }

        // Genre PC obligatoire
        if (JadeStringUtil.isBlank(viewBean.getCsGenrePCAccordee())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_GENRE_PC");
        }

        // Type PC obligatoire
        if (JadeStringUtil.isBlank(viewBean.getCsTypePCAccordee())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_PC");
        }

        // Type bénéficiaire obligatoire
        if (JadeStringUtil.isBlank(viewBean.getCsTypeBeneficiaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE");
        }

        if (!viewBean.getCsEtat().equals(IRFQd.CS_ETAT_QD_CLOTURE) && !isCoupleSepare) {

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                if (viewBean.getCsEtat().equals(IRFQd.CS_ETAT_QD_OUVERT)) {

                    // Si l'état de la qd à ajouter est ouvert on test si il
                    // n'existe pas de qd ouverte pour cette année
                    RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerJointDosJointTiersJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
                    rfQdJointPerJointDosJointTiersJointDemMgr.setSession(viewBean.getSession());
                    rfQdJointPerJointDosJointTiersJointDemMgr.setForIdsTiers(getMembresFamilleViewBean(viewBean, true));
                    rfQdJointPerJointDosJointTiersJointDemMgr.setComprisDansCalcul(true);
                    rfQdJointPerJointDosJointTiersJointDemMgr.setForCsEtatQd(IRFQd.CS_ETAT_QD_OUVERT);
                    rfQdJointPerJointDosJointTiersJointDemMgr.changeManagerSize(0);
                    rfQdJointPerJointDosJointTiersJointDemMgr.find();

                    if (rfQdJointPerJointDosJointTiersJointDemMgr.size() > 0) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDDROITPC_OUVERTE_DEJA_EXISTANTE");
                    }

                }
            }

            if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                // on test ensuite si il existe une période de validité couvrant la
                // période de la Qd à ajouter
                RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfQdJointPerJointDosJointTiersJointDemMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
                rfQdJointPerJointDosJointTiersJointDemMgr.setSession(viewBean.getSession());
                rfQdJointPerJointDosJointTiersJointDemMgr.setForIdsTiers(getMembresFamilleViewBean(viewBean, true));
                rfQdJointPerJointDosJointTiersJointDemMgr.setComprisDansCalcul(true);
                rfQdJointPerJointDosJointTiersJointDemMgr.setForAnneeQd(PRDateFormater
                        .convertDate_JJxMMxAAAA_to_AAAA(viewBean.getDateDebut()));
                rfQdJointPerJointDosJointTiersJointDemMgr.setForDateDebutBetweenPeriode(viewBean.getDateDebut());

                rfQdJointPerJointDosJointTiersJointDemMgr.setForDateFinBetweenPeriode(JadeStringUtil
                        .isBlankOrZero(viewBean.getDateFin()) ? RFUtils.MAX_DATE_VALUE : viewBean.getDateFin());
                rfQdJointPerJointDosJointTiersJointDemMgr.changeManagerSize(0);
                rfQdJointPerJointDosJointTiersJointDemMgr.find();

                if (rfQdJointPerJointDosJointTiersJointDemMgr.size() > 0) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDDROITPC_DEJA_EXISTANTE");
                }
            }
        }

        validerArrondi(viewBean);

        validerTypeBeneficiaireTypePc(viewBean);
    }

    private void validerArrondi(RFSaisieQdViewBean viewBean) {

        if (!JadeStringUtil.isBlankOrZero(viewBean.getExcedentPCAccordee())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getExcedentPCAccordee()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_MONTANT_EXCEDENT_ARRONDI");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getSoldeExcedentPCAccordee())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getSoldeExcedentPCAccordee()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_MONTANT_SOLDE_EXCEDENT_ARRONDI");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getMontantChargeRfm())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getMontantChargeRfm()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_MONTANT_CHARGE_RFM_ARRONDI");
            }
        }

        if (!JadeStringUtil.isBlankOrZero(viewBean.getAugmentationQd())) {
            if (!RFUtils.isMontantArrondiCinqCts(viewBean.getAugmentationQd()).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_MONTANT_AUGMENTATION_QD_ARRONDI");
            }
        }

    }

    private void validerModifierQdAssure(RFSaisieQdViewBean viewBean) throws Exception {

        qdDateValidate(viewBean);

        RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                .getRFQdAssureJointDossierJointTiersManager(
                        viewBean.getSession(),
                        getMembresFamilleViewBean(viewBean, true),
                        null,
                        viewBean.getCodeTypeDeSoinList(),
                        viewBean.getCodeSousTypeDeSoinList(),
                        "",
                        PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(viewBean.getDateDebut()),
                        viewBean.getDateDebut(),
                        JadeStringUtil.isBlankOrZero(viewBean.getDateFin()) ? RFUtils.MAX_DATE_VALUE : viewBean
                                .getDateFin(), "", false, viewBean.getIdQdAssure(), "");

        if (rfQdAssJointDosJointTieMgr.size() > 0) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QDASSURE_DEJA_EXISTANTE");
        }
    }

    private void validerSupprimerQd(RFSaisieQdViewBean viewBean) throws Exception {

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(viewBean.getSession())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_PREPARER_DECISION_DEMARRE");
        }

        if (isDemandeImputeeSurQd(viewBean)) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QD_DEMANDE_IMPUTEE");
        }

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            if (IRFQd.CS_GRANDE_QD.equals(viewBean.getCsGenreQd())) {

                // Vérification si des petites Qds sont reliées à la grande Qd
                RFQdAssureJointDossierJointTiersManager rfQdAssJointDosJointTieMgr = RFUtils
                        .getRFQdAssureJointDossierJointTiersManager(viewBean.getSession(),
                                getMembresFamilleViewBean(viewBean, true), null, viewBean.getCodeTypeDeSoinList(),
                                viewBean.getCodeSousTypeDeSoinList(), "", viewBean.getAnneeQd(), "", "", "", false, "",
                                "");

                if (rfQdAssJointDosJointTieMgr.size() > 0) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_QD_PETITE_QD");
                }
            }
        }
    }

    private void validerTypeBeneficiaireTypePc(RFSaisieQdViewBean viewBean) throws Exception {

        boolean isCouple = false;
        boolean hasEnfant = false;
        if (viewBean.getMembresFamille() != null) {
            for (String[] membre : viewBean.getMembresFamille()) {
                if (membre[1].equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT) && membre[8].equals(Boolean.TRUE.toString())) {
                    isCouple = true;
                }
                if (membre[1].equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT) && membre[8].equals(Boolean.TRUE.toString())) {
                    hasEnfant = true;
                }
            }
        }

        if ((viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE)
                || (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) || viewBean
                .getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS))
                && viewBean.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {

            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_TYPE_PC_INCOMPATIBLE");

        }

        if ((viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME))
                && viewBean.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {

            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_TYPE_PC_INCOMPATIBLE");

        }

        if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)
                || viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)
                || viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE)
                || viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) {

            if (!isCouple) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }

            if (!hasEnfant && viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }

            if (hasEnfant
                    && (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE)
                            || viewBean.getCsTypeBeneficiaire().equals(
                                    IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE) || viewBean
                            .getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME))) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }

        }

        if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS)) {

            if (!hasEnfant || isCouple) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }

        }

        if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES)) {

            if (isCouple || hasEnfant) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }
        }

        if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) {
            if (!hasEnfant) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE_MEMBRES_FAMILLE_INCOMPATIBLE");
            }
        }

        if (RFPropertiesUtils.utiliserTypeBeneficiaire()) {
            if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)) {

                if (viewBean.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {

                    viewBean.setTypeRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);

                    if (JadeStringUtil.isBlankOrZero(viewBean.getTypeRemboursementConjoint())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_DE_REMBOURSEMENT_CONJOINT");
                    }

                } else {

                    viewBean.setTypeRemboursementConjoint(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);

                    if (JadeStringUtil.isBlankOrZero(viewBean.getTypeRemboursementRequerant())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_DE_REMBOURSEMENT_REQUERANT");
                    }
                }
            }

            if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)) {

                if (JadeStringUtil.isBlankOrZero(viewBean.getTypeRemboursementConjoint())
                        || viewBean.getTypeRemboursementConjoint().equals(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_DE_REMBOURSEMENT_CONJOINT");
                }

                if (JadeStringUtil.isBlankOrZero(viewBean.getTypeRemboursementRequerant())
                        || viewBean.getTypeRemboursementRequerant().equals(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_DE_REMBOURSEMENT_REQUERANT");
                }

            }

            if (viewBean.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES)) {

                if (viewBean.getCsGenrePCAccordee().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                    viewBean.setTypeRemboursementRequerant(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
                } else {
                    if (JadeStringUtil.isBlankOrZero(viewBean.getTypeRemboursementRequerant())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_TYPE_DE_REMBOURSEMENT_REQUERANT");
                    }
                }
            }
        }
    }
}
