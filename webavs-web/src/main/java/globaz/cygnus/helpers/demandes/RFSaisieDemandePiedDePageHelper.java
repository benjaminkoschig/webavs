package globaz.cygnus.helpers.demandes;

import globaz.corvus.utils.REPmtMensuel;
import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.dossiers.IRFDossiers;
import globaz.cygnus.db.contributions.RFContributionsAssistanceAI;
import globaz.cygnus.db.contributions.RFContributionsAssistanceAIManager;
import globaz.cygnus.db.conventions.RFConventionJointAssConFouTsJointFournisseurJointMontantManager;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15;
import globaz.cygnus.db.demandes.RFAssDemandeDev19Ftd15Manager;
import globaz.cygnus.db.demandes.RFDemande;
import globaz.cygnus.db.demandes.RFDemandeDev19;
import globaz.cygnus.db.demandes.RFDemandeFra16;
import globaz.cygnus.db.demandes.RFDemandeFrq17Fra18;
import globaz.cygnus.db.demandes.RFDemandeFtd15;
import globaz.cygnus.db.demandes.RFDemandeMai13;
import globaz.cygnus.db.demandes.RFDemandeMoy5_6_7;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.db.dossiers.RFDossierJointTiers;
import globaz.cygnus.db.dossiers.RFDossierJointTiersManager;
import globaz.cygnus.db.dossiers.RFPeriodeCAAIWrapper;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.preparerDecision.RFAnnulerPreparationDecisionService;
import globaz.cygnus.services.preparerDecision.RFRechercheMotifsRefusService;
import globaz.cygnus.services.validerDemande.RFValidationDemandeService;
import globaz.cygnus.utils.RFMotifsRefusListBuillder;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIUtils;
import globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean;
import globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author jje
 */
public class RFSaisieDemandePiedDePageHelper extends PRAbstractHelper {

    /**
     * Ajout d'une demande
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BISession
     * @throws Exception
     */
    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieDemandeViewBean vb = (RFSaisieDemandeViewBean) viewBean;

        if (!vb.getWarningMode()) {
            validate(vb, RFUtils.add);
            if (FWViewBeanInterface.OK.equals(vb.getMsgType())) {
                vb.setWarningMode(true);
            }
        }

        // Si le viewBean n'a pas d'erreurs on modifie la demande
        if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType()) && vb.getWarningMode()) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // Recherche de l'id sous type de soin en fonction des codes
                String idSousTypeDeSoin = RFUtils.getIdSousTypeDeSoin(vb.getCodeTypeDeSoinList(),
                        vb.getCodeSousTypeDeSoinList(), (BSession) session);

                if (!JadeStringUtil.isBlank(idSousTypeDeSoin)) {
                    vb.setIdSousTypeDeSoin(idSousTypeDeSoin);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(vb, "_add()", "RFSaisieDemandePiedDePageHelper");
                }

                setIdDossierViewBean(vb, transaction, session);

                // Si le viewBean n'a pas d'erreurs on enregistre la demande
                if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                    RFDemande rfDemande = new RFDemande();

                    if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEFAUT.equals(vb.getTypeDemande())) {

                        rfDemande.setSession((BSession) vb.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MAI13.equals(vb.getTypeDemande())) {

                        rfDemande.setSession((BSession) viewBean.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeMai13 rfDemandeMail13 = new RFDemandeMai13();
                        rfDemandeMail13.setSession((BSession) vb.getISession());
                        rfDemandeMail13.setIdDemandeMaintienDom13(rfDemande.getIdDemande());
                        rfDemandeMail13.setNombreHeure(vb.getNombreHeure());

                        rfDemandeMail13.add(transaction);

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MOY5_6_7.equals(vb.getTypeDemande())) {

                        rfDemande.setSession((BSession) viewBean.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());

                        if (!JadeStringUtil.isBlankOrZero(vb.getDateDecisionOAI())) {
                            rfDemande.setDateFacture(vb.getDateDecisionOAI());
                        } else {
                            rfDemande.setDateFacture(vb.getDateFacture());
                        }

                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture44());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeMoy5_6_7 rfDemandeMoy5 = new RFDemandeMoy5_6_7();
                        rfDemandeMoy5.setSession((BSession) vb.getISession());
                        rfDemandeMoy5.setIdDemandeMoyensAux(rfDemande.getIdDemande());
                        rfDemandeMoy5.setMontantFacture44(vb.getMontantFacture44());

                        if (!JadeStringUtil.isBlankOrZero(vb.getDateDecisionOAI())) {
                            rfDemandeMoy5.setDateDecisionOAI(vb.getDateDecisionOAI());
                        }

                        if (!JadeStringUtil.isBlankOrZero(vb.getMontantVerseOAI())) {
                            rfDemandeMoy5.setMontantVerseOAI(vb.getMontantVerseOAI());
                        }

                        rfDemandeMoy5.add(transaction);

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FQP17_FRE18.equals(vb.getTypeDemande())) {

                        rfDemande.setSession((BSession) viewBean.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateDecompte());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantDecompte());
                        rfDemande.setNumeroFacture(vb.getNumeroDecompte());
                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
                        rfDemandeFrq17Fra18.setSession((BSession) viewBean.getISession());
                        rfDemandeFrq17Fra18.setCsGenreDeSoin(vb.getCsGenreDeSoin());
                        rfDemandeFrq17Fra18.setDateDecompte(vb.getDateDecompte());
                        rfDemandeFrq17Fra18.setIdDemande1718(rfDemande.getIdDemande());
                        rfDemandeFrq17Fra18.setMontantDecompte(vb.getMontantDecompte());
                        rfDemandeFrq17Fra18.setNumeroDecompte(vb.getNumeroDecompte());

                        rfDemandeFrq17Fra18.add(transaction);

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FRA16.equals(vb.getTypeDemande())) {
                        rfDemande.setSession((BSession) vb.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeFra16 rfDemFra16 = new RFDemandeFra16();
                        rfDemFra16.setSession((BSession) vb.getISession());
                        rfDemFra16.setIdDemandeFra16(rfDemande.getIdDemande());
                        rfDemFra16.setCsTypeVhc(vb.getCsTypeVhc());
                        rfDemFra16.setNombreKilometres(vb.getNombreKilometres());
                        rfDemFra16.setPrixKilometre(vb.getPrixKilometre());
                        rfDemFra16.setTva(vb.getIsTVA());
                        rfDemFra16.add(transaction);

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FTD15.equals(vb.getTypeDemande())) {

                        rfDemande.setSession((BSession) viewBean.getISession());

                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeFtd15 rfDemandeFtd15 = new RFDemandeFtd15();
                        rfDemandeFtd15.setSession((BSession) vb.getISession());
                        rfDemandeFtd15.setIdDemandeFtd15(rfDemande.getIdDemande());

                        rfDemandeFtd15.add(transaction);

                        // Sauvegarde des devis
                        String[] idDevisMontantTab = vb.getListIdDevis().split(",");

                        for (int i = 0; i < idDevisMontantTab.length; i++) {
                            if (!JadeStringUtil.isBlankOrZero(idDevisMontantTab[i])) {

                                String[] idDevisTab = idDevisMontantTab[i].split("-");

                                RFAssDemandeDev19Ftd15 rfDemDev19JointFtd15 = new RFAssDemandeDev19Ftd15();
                                rfDemDev19JointFtd15.setSession((BSession) vb.getISession());
                                rfDemDev19JointFtd15.setIdDemandeDevis19(idDevisTab[0]);
                                rfDemDev19JointFtd15.setIdDemandeFtd15(rfDemande.getIdDemande());
                                rfDemDev19JointFtd15.setMontantAssocieAuDevis(idDevisTab[1]);

                                rfDemDev19JointFtd15.add(transaction);

                            }
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEV19.equals(vb.getTypeDemande())) {

                        /*
                         * Date Facture -> date devis Mnt a payer -> mnt acceptation Mnt facture -> mnt devis Pas de
                         * date de traitement Pas de mnt à déduire
                         */

                        rfDemande.setSession((BSession) vb.getISession());

                        // rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        // rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAcceptation());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setCsEtat(IRFDemande.ENREGISTRE);
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.add(transaction);

                        RFDemandeDev19 rfDemandeDev19 = new RFDemandeDev19();
                        rfDemandeDev19.setSession((BSession) vb.getISession());
                        rfDemandeDev19.setMontantAcceptation(vb.getMontantAcceptation());
                        rfDemandeDev19.setDateEnvoiAcceptation(vb.getDateEnvoiAcceptation());
                        rfDemandeDev19.setDateEnvoiMDC(vb.getDateEnvoiMDC());
                        rfDemandeDev19.setDateEnvoiMDT(vb.getDateEnvoiMDT());
                        rfDemandeDev19.setDateReceptionPreavis(vb.getDateReceptionPreavis());
                        rfDemandeDev19.setIdDemandeDevis19(rfDemande.getIdDemande());

                        rfDemandeDev19.add(transaction);

                    }

                    // Enregistrement des motifs de refus
                    ajouterMotifsDeRefus(vb.getMontantsMotifsRefus().entrySet().iterator(), rfDemande.getIdDemande(),
                            (BSession) vb.getISession(), transaction);

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
     * Suppression d'une demande
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieDemandeViewBean vb = (RFSaisieDemandeViewBean) viewBean;

        validerSupprimer(vb);

        if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {

            BITransaction transaction = null;
            try {

                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                // Si le viewBean n'a pas d'erreurs on modifie la demande
                if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {

                    // Si la demande est dans l'état calculé, le calcul du process "Préparer décision" doit être
                    // annulé
                    if (IRFDemande.CALCULE.equals(vb.getCsEtat())) {
                        RFAnnulerPreparationDecisionService.annulerPreparationDecision(vb.getGestionnaire(), "",
                                vb.getIdQdPrincipale(), (BSession) session, (BTransaction) transaction);
                        vb.setCsEtat(IRFDemande.ENREGISTRE);

                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    }

                    String idDemande = "";

                    if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEFAUT.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {
                            rfDemande.delete(transaction);

                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MAI13.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeMai13 rfDemandeMai13 = new RFDemandeMai13();
                            rfDemandeMai13.setSession((BSession) vb.getISession());
                            rfDemandeMai13.setIdDemandeMaintienDom13(vb.getIdDemande());

                            rfDemandeMai13.retrieve(transaction);

                            if (!rfDemandeMai13.isNew()) {

                                rfDemandeMai13.delete(transaction);
                                rfDemande.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MOY5_6_7.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeMoy5_6_7 rfDemandeMoy5 = new RFDemandeMoy5_6_7();
                            rfDemandeMoy5.setSession((BSession) vb.getISession());
                            rfDemandeMoy5.setIdDemandeMoyensAux(vb.getIdDemande());
                            rfDemandeMoy5.retrieve(transaction);

                            if (!rfDemandeMoy5.isNew()) {

                                rfDemandeMoy5.delete(transaction);
                                rfDemande.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FQP17_FRE18.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
                            rfDemandeFrq17Fra18.setSession((BSession) vb.getISession());
                            rfDemandeFrq17Fra18.setIdDemande1718(vb.getIdDemande());
                            rfDemandeFrq17Fra18.retrieve(transaction);

                            if (!rfDemandeFrq17Fra18.isNew()) {

                                rfDemandeFrq17Fra18.delete(transaction);
                                rfDemande.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FRA16.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeFra16 rfDemFra16 = new RFDemandeFra16();
                            rfDemFra16.setSession((BSession) vb.getISession());
                            rfDemFra16.setIdDemandeFra16(vb.getIdDemande());
                            rfDemFra16.retrieve(transaction);

                            if (!rfDemFra16.isNew()) {

                                rfDemFra16.delete(transaction);
                                rfDemande.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FTD15.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) viewBean.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeFtd15 rfDemandeFtd15 = new RFDemandeFtd15();
                            rfDemandeFtd15.setSession((BSession) viewBean.getISession());
                            rfDemandeFtd15.setIdDemandeFtd15(rfDemande.getIdDemande());

                            rfDemandeFtd15.retrieve(transaction);

                            if (!rfDemandeFtd15.isNew()) {

                                // suppression de l'association avec les devis
                                // 19
                                RFAssDemandeDev19Ftd15Manager rfAssDemDev19Ftd15Mgr = new RFAssDemandeDev19Ftd15Manager();
                                rfAssDemDev19Ftd15Mgr.setSession((BSession) vb.getISession());
                                rfAssDemDev19Ftd15Mgr.setForIdDemande15(rfDemande.getIdDemande());
                                rfAssDemDev19Ftd15Mgr.changeManagerSize(0);
                                rfAssDemDev19Ftd15Mgr.find();

                                rfAssDemDev19Ftd15Mgr.delete(transaction);

                                rfDemandeFtd15.delete(transaction);

                                rfDemande.delete(transaction);
                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEV19.equals(vb.getTypeDemande())) {

                        idDemande = vb.getIdDemande();

                        RFDemande rfDemande = new RFDemande();
                        rfDemande.setSession((BSession) vb.getISession());
                        rfDemande.setIdDemande(vb.getIdDemande());

                        rfDemande.retrieve(transaction);

                        if (!rfDemande.isNew() && rfDemande.getCsEtat().equals(IRFDemande.ENREGISTRE)) {

                            RFDemandeDev19 rfDemandeDev19 = new RFDemandeDev19();
                            rfDemandeDev19.setSession((BSession) viewBean.getISession());
                            rfDemandeDev19.setIdDemandeDevis19(rfDemande.getIdDemande());

                            rfDemandeDev19.retrieve(transaction);

                            if (!rfDemandeDev19.isNew()) {

                                // suppression de l'association avec les FTD15
                                RFAssDemandeDev19Ftd15Manager rfAssDemDev19Ftd15Mgr = new RFAssDemandeDev19Ftd15Manager();
                                rfAssDemDev19Ftd15Mgr.setSession((BSession) vb.getISession());
                                rfAssDemDev19Ftd15Mgr.setForIdDemande19(rfDemande.getIdDemande());
                                rfAssDemDev19Ftd15Mgr.changeManagerSize(0);
                                rfAssDemDev19Ftd15Mgr.find();

                                rfAssDemDev19Ftd15Mgr.delete(transaction);

                                rfDemandeDev19.delete(transaction);
                                rfDemande.delete(transaction);

                            } else {
                                RFUtils.setMsgErreurInattendueViewBean(viewBean, "modifierDemande()",
                                        "RFSaisieDemandePiedDePageHelper");
                            }
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(viewBean, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }

                    }

                    // Suppression des motifs de refus

                    RFAssMotifsRefusDemandeManager rfAssMotifsRefusMgr = new RFAssMotifsRefusDemandeManager();
                    rfAssMotifsRefusMgr.setSession((BSession) viewBean.getISession());
                    rfAssMotifsRefusMgr.changeManagerSize(0);
                    rfAssMotifsRefusMgr.setForIdDemande(idDemande);

                    rfAssMotifsRefusMgr.delete(transaction);

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
     * Override nécessaire pour gérer les membres de la famille du bénéficiaire et les adresses paiement et fournisseur
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieDemandeViewBean vb = ((RFSaisieDemandeViewBean) viewBean);

        if (RFPropertiesUtils.afficherCaseForcerPaiementDemande()) {
            vb.setAfficherCaseForcerPaiement(true);
        }

        if (RFPropertiesUtils.afficherRemarqueFournisseur()) {
            vb.setAfficherRemFournisseur(true);
        }

        if (!vb.isRetourDepuisPyxis()) {

            validerAfficherPiedDePage((RFSaisieDemandeViewBean) viewBean);

            vb.setCsEtat(IRFDemande.ENREGISTRE);

            // Recherche de l'adresse paiement de l'assuré
            if (vb.getIdSousTypeDeSoin().equals((IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_AVANCES))
                    && RFPropertiesUtils.remonterAdressePaiementFournisseurSAS()) {
                String idTiersSas = RFPropertiesUtils.getIdTiersAvanceSas();
                vb.setIdAdressePaiementDemande(idTiersSas);
                vb.setIdFournisseurDemande(idTiersSas);
            } else {
                vb.setIdAdressePaiementDemande(vb.getIdTiers());
            }

            // Recherche de la liste des devis pour ftd 15
            if (vb.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)) {
                vb.rechercheListDevis("");
            }
        } else {
            vb.getDescFournisseur();
            vb.setIsConventionne(isConventionne(vb));
            vb.setRetourPyxisFalse();
        }

        // chargement des données pour les contributions d'assistance AI (mandat InfoRom D0034)

        RFDossierJointTiersManager dossierManager = new RFDossierJointTiersManager();
        dossierManager.setSession((BSession) session);
        dossierManager.setForIdTiers(vb.getIdTiers());
        dossierManager.setForOrderBy(RFDossierJointTiersManager.OrdreDeTri.DateDebut.getOrdre());
        dossierManager.find();

        if (dossierManager.size() > 0) {
            RFDossierJointTiers dossierDuTiers = (RFDossierJointTiers) dossierManager.get(0);

            RFContributionsAssistanceAIManager contributionManager = new RFContributionsAssistanceAIManager();
            contributionManager.setSession((BSession) session);
            contributionManager.setForIdDossierRFM(dossierDuTiers.getIdDossier());
            contributionManager.find();

            if (contributionManager.size() > 0) {

                List<RFPeriodeCAAIWrapper> periodes = new ArrayList<RFPeriodeCAAIWrapper>();

                for (int i = 0; i < contributionManager.size(); i++) {
                    RFContributionsAssistanceAI uneContributon = (RFContributionsAssistanceAI) contributionManager
                            .get(i);

                    RFPeriodeCAAIWrapper wrapper = new RFPeriodeCAAIWrapper();
                    wrapper.setIdContributionAssistanceAI(uneContributon.getIdContributionAssistanceAI());
                    wrapper.setDateDebutCAAI(uneContributon.getDateDebutPeriode());
                    wrapper.setDateFinCAAI(uneContributon.getDateFinPeriode());
                    wrapper.setMontantCAAI(uneContributon.getMontantContribution());
                    wrapper.setMontantAPI(uneContributon.getMontantAPI());
                    wrapper.setCodeAPI(RFContributionsAssistanceAIUtils.getDetailCodeAPI((BSession) session,
                            uneContributon.getCodeAPI()));

                    periodes.add(wrapper);
                }

                Collections.sort(periodes);
                vb.setPeriodesCAAI(periodes);
            }
        }

        vb.setDateDernierPaiement(REPmtMensuel.getDateDernierPmt((BSession) session));
    }

    /**
     * Override nécessaire pour gérer les motifs de refus, l'adresse de paiement et les membres famille
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        try {

            RFSaisieDemandeViewBean vb = (RFSaisieDemandeViewBean) viewBean;

            if (!vb.isRetourDepuisPyxis()) {
                vb.retrieve();
            }

            // Recherche de la description du fournisseur
            if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                vb.getDescFournisseur();
            }

            vb.setIsConventionne(isConventionne(vb));

            if (RFPropertiesUtils.afficherCaseForcerPaiementDemande()) {
                vb.setAfficherCaseForcerPaiement(true);
            }

            if (RFPropertiesUtils.afficherRemarqueFournisseur()) {
                vb.setAfficherRemFournisseur(true);
            }

            if (!vb.isRetourDepuisPyxis()) {
                // Recherche des motifs de refus
                if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                    StringBuffer listMotifsRefus = new StringBuffer();
                    RFRechercheMotifsRefusService rfRechercheMotifsRefusService = new RFRechercheMotifsRefusService();
                    vb.setMontantsMotifsRefus(rfRechercheMotifsRefusService.rechercherMotifsRefus(vb.getSession(),
                            vb.getIdDemande(), null));

                    Iterator<Map.Entry<String, String[]>> iter = vb.getMontantsMotifsRefus().entrySet().iterator();

                    while (iter.hasNext()) {

                        Map.Entry<String, String[]> motEnt = iter.next();

                        if (null != motEnt) {

                            if (listMotifsRefus.length() == 0) {
                                listMotifsRefus.append(motEnt.getKey());
                            } else {
                                listMotifsRefus.append("," + motEnt.getKey());
                            }

                        }
                    }

                    vb.setListMotifsRefusInput(listMotifsRefus.toString());
                }

                // Maj du champ hasMotifRefusDemande
                boolean hasMotifRefusDemande = false;
                Vector<String[]> motifsRefus = ((RFMotifsRefusListBuillder.getInstance((vb.getSession()))
                        .getMotifsRefusParSoinMap().get(vb.getCodeTypeDeSoinList()))).get(vb
                        .getCodeSousTypeDeSoinList());

                for (String[] motifCourant : motifsRefus) {
                    if (motifCourant[2].equals(Boolean.FALSE.toString()) && vb.isChecked(motifCourant[0])) {
                        hasMotifRefusDemande = true;
                    }
                }

                vb.setHasMotifRefusDemande(hasMotifRefusDemande);

                // Recherche des membres famille
                // if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR) && !vb.isRetourDepuisPyxis()) {
                // this.majMembresFamilleDescQdDemande(vb);
                // }

                // Recherche de la liste des devis pour ftd 15
                if (vb.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE)) {
                    vb.rechercheListDevis(vb.getIdDemande());
                }

            } else {
                vb.getDescFournisseur();

                vb.setIsConventionne(isConventionne(vb));
                vb.setRetourPyxisFalse();
            }

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

    }

    /**
     * Modification d'une demande
     * 
     * @param FWViewBeanInterface
     *            , FWAction, BSession
     * @throws Exception
     */
    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFSaisieDemandeViewBean vb = (RFSaisieDemandeViewBean) viewBean;

        if (!vb.getWarningMode()) {
            validate(vb, RFUtils.upd);
            if (FWViewBeanInterface.OK.equals(vb.getMsgType())) {
                vb.setWarningMode(true);
            }
        }

        // Si le viewBean n'a pas d'erreurs on modifie la demande
        if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType()) && vb.getWarningMode()) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                String idDemande = "";

                setIdDossierViewBean(vb, transaction, session);

                if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEFAUT.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());
                        // rfDemande.setIdQD(vb.getIdQD());

                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MAI13.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                        RFDemandeMai13 rfDemandeMai13 = new RFDemandeMai13();
                        rfDemandeMai13.setSession((BSession) vb.getISession());
                        rfDemandeMai13.setIdDemandeMaintienDom13(vb.getIdDemande());

                        rfDemandeMai13.retrieve();

                        if (!rfDemandeMai13.isNew()) {
                            rfDemandeMai13.setNombreHeure(vb.getNombreHeure());

                            rfDemandeMai13.update(transaction);

                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_MOY5_6_7.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());

                        if (!JadeStringUtil.isBlankOrZero(vb.getDateDecisionOAI())) {
                            rfDemande.setDateFacture(vb.getDateDecisionOAI());
                        } else {
                            rfDemande.setDateFacture(vb.getDateFacture());
                        }

                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture44());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                        RFDemandeMoy5_6_7 rfDemandeMoy5 = new RFDemandeMoy5_6_7();
                        rfDemandeMoy5.setSession((BSession) vb.getISession());
                        rfDemandeMoy5.setIdDemandeMoyensAux(vb.getIdDemande());
                        rfDemandeMoy5.retrieve();

                        if (!rfDemandeMoy5.isNew()) {
                            rfDemandeMoy5.setMontantFacture44(vb.getMontantFacture44());

                            if (!JadeStringUtil.isBlankOrZero(vb.getDateDecisionOAI())) {
                                rfDemandeMoy5.setDateDecisionOAI(vb.getDateDecisionOAI());
                            }

                            if (!JadeStringUtil.isBlankOrZero(vb.getMontantVerseOAI())) {
                                rfDemandeMoy5.setMontantVerseOAI(vb.getMontantVerseOAI());
                            }

                            rfDemandeMoy5.update(transaction);

                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FQP17_FRE18.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateDecompte());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantDecompte());
                        rfDemande.setNumeroFacture(vb.getNumeroDecompte());
                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                        RFDemandeFrq17Fra18 rfDemandeFrq17Fra18 = new RFDemandeFrq17Fra18();
                        rfDemandeFrq17Fra18.setSession((BSession) vb.getISession());
                        rfDemandeFrq17Fra18.setIdDemande1718(vb.getIdDemande());
                        rfDemandeFrq17Fra18.retrieve();

                        if (!rfDemandeFrq17Fra18.isNew()) {
                            rfDemandeFrq17Fra18.setCsGenreDeSoin(vb.getCsGenreDeSoin());
                            rfDemandeFrq17Fra18.setDateDecompte(vb.getDateDecompte());
                            rfDemandeFrq17Fra18.setMontantDecompte(vb.getMontantDecompte());
                            rfDemandeFrq17Fra18.setNumeroDecompte(vb.getNumeroDecompte());

                            rfDemandeFrq17Fra18.update(transaction);
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FRA16.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setMontantMensuel(vb.getMontantMensuel());
                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                        RFDemandeFra16 rfDemFra16 = new RFDemandeFra16();
                        rfDemFra16.setSession((BSession) vb.getISession());
                        rfDemFra16.setIdDemandeFra16(vb.getIdDemande());
                        rfDemFra16.retrieve();

                        if (!rfDemFra16.isNew()) {
                            rfDemFra16.setCsTypeVhc(vb.getCsTypeVhc());
                            rfDemFra16.setNombreKilometres(vb.getNombreKilometres());
                            rfDemFra16.setPrixKilometre(vb.getPrixKilometre());
                            rfDemFra16.setTva(vb.getIsTVA());
                            rfDemFra16.update(transaction);
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_FTD15.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) viewBean.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {
                        rfDemande.setSession((BSession) viewBean.getISession());
                        rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAPayer());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        // update des devis
                        // suppression des devis si ils existent

                        RFAssDemandeDev19Ftd15Manager rfAssDemDev19Ftd15Mgr = new RFAssDemandeDev19Ftd15Manager();
                        rfAssDemDev19Ftd15Mgr.setSession((BSession) vb.getISession());
                        rfAssDemDev19Ftd15Mgr.setForIdDemande15(rfDemande.getIdDemande());
                        rfAssDemDev19Ftd15Mgr.changeManagerSize(0);
                        rfAssDemDev19Ftd15Mgr.find();

                        rfAssDemDev19Ftd15Mgr.delete(transaction);

                        // ajout des devis
                        String[] idDevisMontantTab = vb.getListIdDevis().split(",");

                        for (int i = 0; i < idDevisMontantTab.length; i++) {
                            if (!JadeStringUtil.isBlankOrZero(idDevisMontantTab[i])) {

                                String[] idDevisTab = idDevisMontantTab[i].split("-");

                                RFAssDemandeDev19Ftd15 rfDemDev19JointFtd15 = new RFAssDemandeDev19Ftd15();
                                rfDemDev19JointFtd15.setSession((BSession) vb.getISession());
                                rfDemDev19JointFtd15.setIdDemandeDevis19(idDevisTab[0]);
                                rfDemDev19JointFtd15.setIdDemandeFtd15(rfDemande.getIdDemande());
                                rfDemDev19JointFtd15.setMontantAssocieAuDevis(idDevisTab[1]);

                                rfDemDev19JointFtd15.add(transaction);

                            }
                        }

                        rfDemande.update(transaction);
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(vb, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                } else if (RFSaisieDemandeAbstractViewBean.TYPE_DEMANDE_DEV19.equals(vb.getTypeDemande())) {

                    idDemande = vb.getIdDemande();

                    RFDemande rfDemande = new RFDemande();
                    rfDemande.setSession((BSession) vb.getISession());
                    rfDemande.setIdDemande(vb.getIdDemande());

                    rfDemande.retrieve();

                    if (!rfDemande.isNew()) {

                        // rfDemande.setDateDebutTraitement(vb.getDateDebutTraitement());
                        // rfDemande.setDateFinTraitement(vb.getDateFinTraitement());
                        rfDemande.setDateFacture(vb.getDateFacture());
                        rfDemande.setDateReception(vb.getDateReception());
                        rfDemande.setIdAdressePaiement(vb.getIdAdressePaiementDemande());
                        rfDemande.setIdDossier(vb.getIdDossier());
                        rfDemande.setIdFournisseur(vb.getIdFournisseurDemande());
                        rfDemande.setIdGestionnaire(vb.getIdGestionnaire());
                        rfDemande.setIdSousTypeDeSoin(vb.getIdSousTypeDeSoin());
                        rfDemande.setIsForcerPaiement(vb.getIsForcerPaiement());
                        rfDemande.setIsContratDeTravail(vb.getIsContratDeTravail());
                        rfDemande.setIsPP(vb.getIsPP());
                        rfDemande.setIsTexteRedirection(vb.getIsTexteRedirection());
                        rfDemande.setMontantAPayer(vb.getMontantAcceptation());
                        rfDemande.setMontantFacture(vb.getMontantFacture());
                        rfDemande.setNumeroFacture(vb.getNumeroFacture());
                        rfDemande.setRemarqueFournisseur(vb.getRemarqueFournisseur());
                        rfDemande.setIsRetro(vb.getIsRetro());

                        rfDemande.setCsEtat(vb.getCsEtat());
                        rfDemande.setCsSource(IRFDemande.GESTIONNAIRE);
                        rfDemande.setCsStatut(vb.getCsStatut());

                        rfDemande.update(transaction);

                        RFDemandeDev19 rfDemandeDev19 = new RFDemandeDev19();
                        rfDemandeDev19.setSession((BSession) viewBean.getISession());
                        rfDemandeDev19.setIdDemandeDevis19(rfDemande.getIdDemande());

                        rfDemandeDev19.retrieve();

                        if (!rfDemandeDev19.isNew()) {
                            rfDemandeDev19.setMontantAcceptation(vb.getMontantAcceptation());
                            rfDemandeDev19.setDateEnvoiAcceptation(vb.getDateEnvoiAcceptation());
                            rfDemandeDev19.setDateEnvoiMDC(vb.getDateEnvoiMDC());
                            rfDemandeDev19.setDateEnvoiMDT(vb.getDateEnvoiMDT());
                            rfDemandeDev19.setDateReceptionPreavis(vb.getDateReceptionPreavis());
                            rfDemandeDev19.setIdDemandeDevis19(rfDemande.getIdDemande());

                            rfDemandeDev19.update(transaction);
                        } else {
                            RFUtils.setMsgErreurInattendueViewBean(viewBean, "modifierDemande()",
                                    "RFSaisieDemandePiedDePageHelper");
                        }
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(viewBean, "modifierDemande()",
                                "RFSaisieDemandePiedDePageHelper");
                    }

                }

                // Suppression des motifs de refus
                if (!FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {
                    RFAssMotifsRefusDemandeManager rfAssMotifsRefusMgr = new RFAssMotifsRefusDemandeManager();
                    rfAssMotifsRefusMgr.setSession((BSession) viewBean.getISession());
                    rfAssMotifsRefusMgr.changeManagerSize(0);
                    rfAssMotifsRefusMgr.setForIdDemande(idDemande);

                    rfAssMotifsRefusMgr.delete(transaction);

                    // Enregistrement des motifs de refus
                    ajouterMotifsDeRefus(vb.getMontantsMotifsRefus().entrySet().iterator(), idDemande,
                            (BSession) vb.getISession(), transaction);
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

                            // Si la demande est dans l'état calculé, le calcul du process "Préparer décision" doit être
                            // annulé
                            if (IRFDemande.CALCULE.equals(vb.getCsEtat())) {
                                RFAnnulerPreparationDecisionService.annulerPreparationDecision(vb.getGestionnaire(),
                                        "", vb.getIdQdPrincipale(), (BSession) session, (BTransaction) transaction);
                                vb.setCsEtat(IRFDemande.ENREGISTRE);

                                if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                                    transaction.rollback();
                                } else {
                                    transaction.commit();
                                }
                            }
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

    private void ajouterMotifsDeRefus(Iterator<Map.Entry<String, String[]>> montantMotifsRefusKeysIter,
            String idDemande, BSession session, BITransaction transaction) throws Exception {

        while (montantMotifsRefusKeysIter.hasNext()) {
            Map.Entry<String, String[]> montantMotifsRefusKeys = montantMotifsRefusKeysIter.next();

            if (!((montantMotifsRefusKeys.getValue()[0] != null) && JadeStringUtil.isBlankOrZero(montantMotifsRefusKeys
                    .getValue()[0]))) {
                RFAssMotifsRefusDemande rfAssMotifsDeRefusDem = new RFAssMotifsRefusDemande();
                rfAssMotifsDeRefusDem.setSession(session);
                rfAssMotifsDeRefusDem.setIdDemande(idDemande);
                rfAssMotifsDeRefusDem.setIdMotifsRefus(montantMotifsRefusKeys.getKey());

                if (montantMotifsRefusKeys.getValue()[0] != null) {
                    rfAssMotifsDeRefusDem.setMntMotifsDeRefus(montantMotifsRefusKeys.getValue()[0]);
                }

                rfAssMotifsDeRefusDem.add(transaction);
            }
        }

    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Méthode détérminant si la demande possède une QdAssuré définie par une convention
     * 
     * @return boolean
     * @throws Exception
     */
    protected boolean isConventionne(RFSaisieDemandeViewBean vb) throws Exception {

        RFConventionJointAssConFouTsJointFournisseurJointMontantManager rfConvJointAssConFouTsJointFouJointMntMgr = new RFConventionJointAssConFouTsJointFournisseurJointMontantManager();

        rfConvJointAssConFouTsJointFouJointMntMgr.setSession(vb.getSession());
        rfConvJointAssConFouTsJointFouJointMntMgr.setForCodeTypeDeSoin(vb.getCodeTypeDeSoinList());
        rfConvJointAssConFouTsJointFouJointMntMgr.setForCodeSousTypeDeSoin(vb.getCodeSousTypeDeSoinList());
        rfConvJointAssConFouTsJointFouJointMntMgr.setForActif(true);
        rfConvJointAssConFouTsJointFouJointMntMgr.setForIdFournisseur(vb.getIdFournisseurDemande());
        rfConvJointAssConFouTsJointFouJointMntMgr.changeManagerSize(0);
        rfConvJointAssConFouTsJointFouJointMntMgr.find();

        if (rfConvJointAssConFouTsJointFouJointMntMgr.size() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Recherche du no de dossier en fct de l'idTiers Si le dossier n'a pas été créé on le créé quelque soit l'état du
     * droitPC. Un dossier RFM peut exister même si le droit PC n'existe pas.
     * 
     * @param FWViewBeanInterface
     *            , BITransaction
     * @throws Exception
     */
    private void setIdDossierViewBean(FWViewBeanInterface viewBean, BITransaction transaction, BISession session)
            throws Exception {

        RFSaisieDemandeViewBean vb = (RFSaisieDemandeViewBean) viewBean;

        // Recherche du no de dossier en fct de l'idTiers
        RFPrDemandeJointDossier rfPrDemandeJointDossier = RFUtils.getDossierJointPrDemande(vb.getIdTiers(),
                (BSession) session);

        // Si le dossier n'existe pas on le créé.
        if (null == rfPrDemandeJointDossier) {

            // creation du dossier prdemap
            PRDemande demandePrestation = new PRDemande();
            demandePrestation.setSession((BSession) session);
            demandePrestation.setIdTiers(vb.getIdTiers());
            demandePrestation.setTypeDemande(IPRDemande.CS_TYPE_RFM);
            demandePrestation.setEtat(IPRDemande.CS_ETAT_OUVERT);
            demandePrestation.add(transaction);

            // creation du dossier RFM
            RFDossier dossierRFM = new RFDossier();
            dossierRFM.setSession((BSession) vb.getISession());
            dossierRFM.setIdGestionnaire(vb.getIdGestionnaire());
            dossierRFM.setDateDebut(JACalendar.todayJJsMMsAAAA());
            // dossierRFM.setDateFin();
            dossierRFM.setCsEtatDossier(IRFDossiers.OUVERT);
            dossierRFM.setIdPrDem(demandePrestation.getIdDemande());
            dossierRFM.setCsSource(IRFDossiers.SYSTEME);

            dossierRFM.add(transaction);

            vb.setIdDossier(dossierRFM.getIdDossier());

        } else {
            vb.setIdDossier(rfPrDemandeJointDossier.getIdDossier());
        }
    }

    /**
     * Méthode executant la validation de la demande
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    private void validate(FWViewBeanInterface viewBean, String method) throws Exception {

        RFValidationDemandeService rfValDemSer = new RFValidationDemandeService(viewBean, method);
        rfValDemSer.validate();

    }

    /**
     * Méthode effectuant la validation de la seconde partie de la saisie d'une demande
     * 
     * @param FWViewBeanInterface
     * @throws Exception
     */
    private void validerAfficherPiedDePage(RFSaisieDemandeAbstractViewBean viewBean) {

        if (JadeStringUtil.isBlank(viewBean.getIdTiers())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_BENEFICIAIRE_NON_SELECTIONNE");
            viewBean.setErreurSaisieDemande(true);
        }

        if (JadeStringUtil.isBlank(viewBean.getIdGestionnaire())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_GESTIONNAIRE_NON_SELECTIONNE");
            viewBean.setErreurSaisieDemande(true);
        }
    }

    private void validerSupprimer(RFSaisieDemandeViewBean vb) throws Exception {

        if (vb.getCsEtat().equals(IRFDemande.VALIDE) || vb.getCsEtat().equals(IRFDemande.PAYE)) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_DEM_S_SUPPRESSION_VALIDE_PAYE");
        }

        if (RFSetEtatProcessService.getEtatProcessPreparerDecision(vb.getSession())) {
            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_DEM_S_PREPARER_DECISION_DEMARRE");
        }

        if (vb.getCsEtat().equals(IRFDemande.CALCULE)) {
            if (RFSetEtatProcessService.getEtatProcessValiderDecision(vb.getSession())) {
                RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_DEM_S_VALIDER_DECISION_DEMARRE");
            }
        }

    }

}