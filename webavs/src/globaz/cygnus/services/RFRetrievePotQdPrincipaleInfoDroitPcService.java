package globaz.cygnus.services;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.db.pots.RFPotsPC;
import globaz.cygnus.db.pots.RFPotsPCManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFSaisieQdViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.vo.decision.DecisionPcVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * @author JJE
 * 
 */
public class RFRetrievePotQdPrincipaleInfoDroitPcService {

    /**
     * Méthode qui retourne la limitte annuelle et les informations du droit PC
     * 
     * @return RFSaisieQdViewBean
     * 
     * @throws Exception
     */
    public static RFSaisieQdViewBean retrieve(RFSaisieQdViewBean vb, String idTiersCoupleSepare,
            BTransaction transaction) throws Exception {

        RFRetrieveInfoDroitPCService rfRetInfDroPcSer = new RFRetrieveInfoDroitPCService();

        String idTiers = "";

        if (JadeStringUtil.isBlankOrZero(idTiersCoupleSepare)) {
            idTiers = vb.getIdTiers();
        } else {
            idTiers = idTiersCoupleSepare;
        }

        // Recherche du type de décision (utile pour retrouver l'excédent de revenu)
        List<String> idDecisionsList = new ArrayList<String>();
        idDecisionsList.add(vb.getDateDebut());
        RFDecisionPcService rfDecisionPcService = new RFDecisionPcService(transaction);
        List<DecisionPcVO> decisionPcVos = rfDecisionPcService.getDecisionPc(idDecisionsList);
        String typeDeDecisionPc = "";
        if (decisionPcVos.size() == 1) {
            for (DecisionPcVO decisionPcVoCourant : decisionPcVos) {
                if (null != decisionPcVoCourant) {
                    typeDeDecisionPc = decisionPcVoCourant.getCsTypeDecision();
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(vb, "retrieve(): impossible de retrouver la décision PC",
                            "RFRetrievePotQdPrincipaleInfoDroitPcService");
                }
            }
        } /*
           * else { RFUtils.setMsgErreurInattendueViewBean(vb, "retrieve(): impossible de retrouver la décision PC",
           * "RFRetrievePotQdPrincipaleInfoDroitPcService"); }
           */

        RFRetrieveInfoDroitPCServiceData[] resultat = rfRetInfDroPcSer.retrieve(null, "", vb, vb.getSession(), idTiers,
                vb.getDateDebut(), transaction, typeDeDecisionPc, false);
        if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            if ((null != resultat) && (resultat.length > 0)) {

                if (resultat.length == 1) {
                    vb = RFRetrievePotQdPrincipaleInfoDroitPcService.setViewBean(vb, resultat[0].getTypeBeneficiaire(),
                            resultat[0].getGenrePc(), resultat[0].getTypePc(), resultat[0].getSoldeExcedent(),
                            resultat[0].getPersonneDansPlanCalculList(), resultat[0].getDateDebutPcaccordee(),
                            resultat[0].getDateFinPcaccordee(), Boolean.TRUE, resultat[0].isRi(),
                            resultat[0].getTypeRemboursement(), "", resultat[0].getCsDegreApi());
                } else {
                    boolean isIdTiersTraite = false;

                    for (RFRetrieveInfoDroitPCServiceData resCourant : resultat) {
                        if (resCourant.getIdTiers().equals(idTiers)) {
                            vb = RFRetrievePotQdPrincipaleInfoDroitPcService.setViewBean(vb,
                                    resCourant.getTypeBeneficiaire(), resCourant.getGenrePc(), resCourant.getTypePc(),
                                    resCourant.getSoldeExcedent(), resCourant.getPersonneDansPlanCalculList(),
                                    resCourant.getDateDebutPcaccordee(), resCourant.getDateFinPcaccordee(),
                                    Boolean.TRUE, resCourant.isRi(), resultat[0].getTypeRemboursement(), "",
                                    resultat[0].getCsDegreApi());
                            isIdTiersTraite = true;
                        } else {
                            vb.setTypeRemboursementConjoint(resultat[0].getTypeRemboursement());
                        }
                    }

                    if (!isIdTiersTraite) {
                        RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_INFO_TIERS_DROIT_PC_NON_TROUVE");
                    }

                }

            } else {
                if (!JadeStringUtil.isBlankOrZero(vb.getCsTypeBeneficiaire())
                        && !JadeStringUtil.isBlankOrZero(vb.getCsGenrePCAccordee())) {
                    vb = RFRetrievePotQdPrincipaleInfoDroitPcService.setViewBean(vb, vb.getCsTypeBeneficiaire(),
                            vb.getCsGenrePCAccordee(), vb.getCsTypePCAccordee(), vb.getSoldeExcedentPCAccordee(), null,
                            "", "", Boolean.FALSE, null, vb.getTypeRemboursementRequerant(),
                            vb.getTypeRemboursementConjoint(), vb.getCsDegreApi());
                } else {
                    if (JadeStringUtil.isBlankOrZero(vb.getCsTypeBeneficiaire())) {
                        RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_TYPE_BENEFICIAIRE");
                    }
                    if (JadeStringUtil.isBlankOrZero(vb.getCsGenrePCAccordee())) {
                        RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_GENRE_PC");
                    }

                    vb.setIsLimiteAnnuelleOk(false);
                }
            }
        }

        if (!vb.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            String[] resultatPot = RFRetrievePotQdPrincipaleInfoDroitPcService.retrieveLimiteAnnuelle(vb,
                    vb.getDateDebut(), vb.getCsTypeBeneficiaire(), vb.getCsGenrePCAccordee(), vb.getSession(),
                    transaction);

            vb.setIdPotDroitPC(resultatPot[0]);
            vb.setLimiteAnnuelle(resultatPot[1]);
            vb.setIsLimiteAnnuelleOk(Boolean.TRUE.toString().equals(resultatPot[2]));
        }

        return vb;
    }

    /**
     * 
     * Méthode retournant la limite annuelle d'un pot grande Qd
     * 
     * @param date
     * @param session
     * @param transaction
     * @return String[idPot, limiteAnnuelle, isLimiteAnnuelleOk]
     */
    public static String[] retrieveLimiteAnnuelle(FWViewBeanInterface viewBean, String date, String typeBeneficiaire,
            String csGenrePcAccordee, BSession session, BITransaction transaction) throws Exception {

        String[] resultat = new String[] { "", "", "" };

        RFPotsPCManager rfPotPcMgr = new RFPotsPCManager();

        rfPotPcMgr.setSession(session);
        rfPotPcMgr.setForDateDebut(date);
        rfPotPcMgr.setForDateFin(date);
        rfPotPcMgr.changeManagerSize(0);
        rfPotPcMgr.find(transaction);

        if (rfPotPcMgr.size() == 1) {
            RFPotsPC rfPotPc = (RFPotsPC) rfPotPcMgr.getFirstEntity();

            if (null != rfPotPc) {

                resultat[0] = rfPotPc.getIdPotPC();
                resultat[2] = Boolean.TRUE.toString();

                if ((JadeStringUtil.isBlankOrZero(rfPotPc.getMontantPlafondPourTous()) && csGenrePcAccordee
                        .equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE))
                        || (JadeStringUtil.isBlankOrZero(rfPotPc.getMontantPlafondPensionnairePourTous()) && csGenrePcAccordee
                                .equals(IPCPCAccordee.CS_GENRE_PC_HOME))) {

                    if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondPersonneSeule();
                        } else {
                            resultat[1] = rfPotPc.getMontantPlafondPensionnairePersonneSeule();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondCoupleDomicile();
                        } else {
                            resultat[2] = Boolean.FALSE.toString();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondCoupleEnfants();
                        } else {
                            resultat[2] = Boolean.FALSE.toString();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondAdulteEnfants();
                        } else {
                            resultat[2] = Boolean.FALSE.toString();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondSepareDomicile();
                        } else {
                            resultat[1] = rfPotPc.getMontantPlafondPensionnaireSepareDomicile();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                            resultat[1] = rfPotPc.getMontantPlafondPensionnaireSepareHome();
                        } else {
                            resultat[2] = Boolean.FALSE.toString();
                        }

                    } else if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.ENFANTS_VIVANT_SEPARES)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondEnfantsSepares();
                        } else {
                            resultat[1] = rfPotPc.getMontantPlafondPensionnaireEnfantsSepares();
                        }

                    }
                    if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.ENFANTS_AVEC_ENFANTS)) {

                        if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            resultat[1] = rfPotPc.getMontantPlafondEnfantsEnfants();
                        } else {
                            resultat[1] = rfPotPc.getMontantPlafondPensionnaireEnfantsEnfants();
                        }

                    }

                } else {
                    if (csGenrePcAccordee.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                        resultat[1] = rfPotPc.getMontantPlafondPourTous();
                    } else {
                        resultat[1] = rfPotPc.getMontantPlafondPensionnairePourTous();
                    }
                }

            } else {
                if (null != viewBean) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_QD_S_POT_DROIT_PC_NON_TROUVE");
                }
                resultat[2] = Boolean.FALSE.toString();
            }
        }

        if (resultat[2].equals(Boolean.FALSE.toString())) {
            resultat[0] = "";
        }

        return resultat;
    }

    private static RFSaisieQdViewBean setViewBean(RFSaisieQdViewBean vb, String typeBeneficiaire, String genrePc,
            String typePc, String soldeExcedent, List<PCAMembreFamilleVO> personneDansPlanCalculList,
            String dateDebutPcAccordee, String dateFinPcaccordee, Boolean isDroitPc, Boolean isRi,
            String typeRemboursementRequerant, String typeRemboursementConjoint, String csDegreApi) {

        vb.setCsTypeBeneficiaire(typeBeneficiaire);
        vb.setCsGenrePCAccordee(genrePc);
        vb.setCsTypePCAccordee(typePc);
        vb.setSoldeExcedentPCAccordee(soldeExcedent);
        vb.setDateDebutPCAccordee(dateDebutPcAccordee);
        vb.setDateFinPCAccordee(dateFinPcaccordee);
        vb.setPersonnesDansPlanCalculList(personneDansPlanCalculList);
        vb.setIsDroitPC(isDroitPc);

        vb.setTypeRemboursementRequerant(typeRemboursementRequerant);
        vb.setTypeRemboursementConjoint(typeRemboursementConjoint);

        vb.setCsDegreApi(csDegreApi);

        if (null != isRi) {
            vb.setIsRi(isRi);
        }

        /*
         * if (null != personneDansPlanCalculList) { for (PersonneDansPlanCalcul perDsPlaCal :
         * personneDansPlanCalculList) { String[] perStrTab = new String[] { perDsPlaCal.getIdTiers(),
         * perDsPlaCal.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC() };
         * 
         * vb.getPersonnesDansPlanCalculList().add(perStrTab); } }
         */

        return vb;
    }

}
