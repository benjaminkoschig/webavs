package globaz.cygnus.services;

import globaz.cygnus.api.IRFTypesBeneficiairePc;
import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.qds.RFSaisieQdViewBean;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAAccordeePlanClaculeAndMembreFamilleVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAMembreFamilleVO;

/**
 * 
 * @author JJE
 * 
 */
public class RFRetrieveInfoDroitPCService {
    public static final String RENTE_COMPLEMENTAIRE = "complementaire";
    public static final String RENTE_PRINCIPALE = "principale";

    private String getMontantExcedent(PCAAccordeePlanClaculeAndMembreFamilleVO pcAccPlaCalAndMemFamVOCourant,
            String typeDeDecisionPc, String CsEtatPc, boolean isAdaptationAnnuelle) {

        boolean hasMontantExcedent = false;
        String montantExcedendDeRecette = "";

        if (!isAdaptationAnnuelle) {
            if (!(typeDeDecisionPc.equals(IPCDecision.CS_TYPE_OCTROI_AC) || typeDeDecisionPc
                    .equals(IPCDecision.CS_TYPE_PARTIEL_AC))) {
                hasMontantExcedent = true;
            }
        } else {
            if (!(CsEtatPc.equals(IPCValeursPlanCalcul.STATUS_OCTROI) || CsEtatPc
                    .equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL))) {
                hasMontantExcedent = true;
            }
        }

        if (hasMontantExcedent) {

            MathContext mathContext = new MathContext(15);
            BigDecimal excedentPCAnnuelBigDec = new BigDecimal(pcAccPlaCalAndMemFamVOCourant.getExcedentPCAnnuel(),
                    mathContext);
            BigDecimal montantPrimeMoyenAssMaladieBigDec = new BigDecimal(
                    pcAccPlaCalAndMemFamVOCourant.getMontantPrimeMoyenAssMaladie(), mathContext);
            BigDecimal montantExcedentDeRecetteBigDec = new BigDecimal("0", mathContext);

            montantExcedentDeRecetteBigDec = excedentPCAnnuelBigDec.abs(mathContext).subtract(
                    montantPrimeMoyenAssMaladieBigDec.abs(mathContext), mathContext);

            if (montantExcedentDeRecetteBigDec.compareTo(new BigDecimal("0")) > 0) {
                montantExcedendDeRecette = montantExcedentDeRecetteBigDec.toString();
            }

        }

        return montantExcedendDeRecette;
    }

    private boolean isCreationQdManuelle(String IdAdaptationJournaliere, boolean isAdaptationAnnuelle) {
        return JadeStringUtil.isBlankOrZero(IdAdaptationJournaliere) && !isAdaptationAnnuelle;
    }

    private boolean isRi(String typeBeneficiaire, String idPcAccordee, BSession session, BTransaction transaction,
            final boolean isCreationManuelle, RFSaisieQdViewBean vb) throws Exception {

        try {

            if (RFPropertiesUtils.afficherCaseRi()) {

                BigDecimal montantMaxRiBigDec = null;

                if (typeBeneficiaire.equals(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES)) {
                    montantMaxRiBigDec = new BigDecimal(PRAbstractApplication.getApplication(
                            RFApplication.DEFAULT_APPLICATION_CYGNUS).getProperty(
                            RFApplication.PROPERTY_MNT_MAX_RI_PERSONNE_SEULE));
                } else {
                    montantMaxRiBigDec = new BigDecimal(PRAbstractApplication.getApplication(
                            RFApplication.DEFAULT_APPLICATION_CYGNUS).getProperty(
                            RFApplication.PROPERTY_MNT_MAX_RI_COUPLE_FAMILLE));
                }

                RFFortuneNetteService rfFortuneNetteService = new RFFortuneNetteService(transaction);
                BigDecimal fortuneNetteBigDec = rfFortuneNetteService.getFortuneNette("", idPcAccordee);
                if (fortuneNetteBigDec.signum() != -1) {
                    if (fortuneNetteBigDec.compareTo(montantMaxRiBigDec) > 0) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } catch (Exception e) {
            if (!isCreationManuelle) {
                throw new Exception(
                        "RFRetrieveInfoDroitPCService.isRi(): impossible de déterminer si la personne est au RI");
            } else {
                RFUtils.setMsgWarningViewBean(vb, "WARNING_RF_QD_S_RI_NON_TROUVE");
                return false;
            }
        }

    }

    private boolean isTiersQdComprisDansCalcul(String idTiers, List<PCAMembreFamilleVO> personneDansPlanCalculList) {

        for (PCAMembreFamilleVO pcMemFamVoCourant : personneDansPlanCalculList) {

            if (pcMemFamVoCourant.getIdTiers().equals(idTiers)
                    && !pcMemFamVoCourant.getIsComprisDansCalcul().booleanValue()) {
                return false;
            }

        }

        return true;
    }

    /**
     * Méthode qui retourne le type bénéficiare, le RI, la famille et l'excédent de recette en fonction d'un idTiers et
     * d'une date
     * 
     * @param FWViewBeanInterface
     *            , String, String
     * @throws Exception
     */
    public RFRetrieveInfoDroitPCServiceData[] retrieve(List<String[]> logsList, String IdAdaptationJournaliere,
            RFSaisieQdViewBean vb, BISession session, String idTiers, String date, BITransaction transaction,
            String typeDeDecisionPc, boolean isAdaptationAnnuelle) throws Exception {

        if (null == transaction) {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();
        }

        RFRetrieveInfoDroitPCServiceData[] resultat = null;

        RFPCAccordeeWithCalculeRetenuService rfPcAccWitCalRetSer = new RFPCAccordeeWithCalculeRetenuService(
                (BTransaction) transaction);

        List<PCAAccordeePlanClaculeAndMembreFamilleVO> pcAccPlaCalAndMemFamVOList = rfPcAccWitCalRetSer
                .getPCAccordeeWithCalculeRetenuVO(idTiers, date);

        if (isAdaptationAnnuelle) {
            List<PCAAccordeePlanClaculeAndMembreFamilleVO> pcAccPlaCalAndMemFamVOAdaptationAnnuelleList = new ArrayList<PCAAccordeePlanClaculeAndMembreFamilleVO>();

            // On parse la liste pour supprimmer toutes les PC accordées qui ne commencent pas par le début de l'année
            // de l'adaptation
            for (PCAAccordeePlanClaculeAndMembreFamilleVO pcVoCourante : pcAccPlaCalAndMemFamVOList) {
                if (pcVoCourante.getDateDebut().equals("01." + String.valueOf(JACalendar.today().getYear() + 1))) {
                    pcAccPlaCalAndMemFamVOAdaptationAnnuelleList.add(pcVoCourante);
                }
            }

            pcAccPlaCalAndMemFamVOList = pcAccPlaCalAndMemFamVOAdaptationAnnuelleList;
        }

        // Si il ne s'agit pas d'un couple séparé par la maladie (idDossier PC1 != idDossier PC2), on parse la liste
        // pour supprimmer les PCAccordées dont le tiers n'est pas compris dans le calcul (car personne comprises dans
        // plusieurs calcul PC)
        if ((null != pcAccPlaCalAndMemFamVOList) && (pcAccPlaCalAndMemFamVOList.size() == 2)) {
            String idDossierPcAcc = "";
            boolean premiereIteration = true;
            boolean isCoupleSepare = false;
            for (PCAAccordeePlanClaculeAndMembreFamilleVO pcVoCourante : pcAccPlaCalAndMemFamVOList) {
                if (!premiereIteration) {
                    if (idDossierPcAcc.equals(pcVoCourante.getIdDossier())) {
                        isCoupleSepare = true;
                    }
                }
                idDossierPcAcc = pcVoCourante.getIdDossier();
                premiereIteration = false;
            }

            if (!isCoupleSepare) {
                List<PCAAccordeePlanClaculeAndMembreFamilleVO> pcAccPlaCalAndMemFamVOTiersComprisDsPlsCalulList = new ArrayList<PCAAccordeePlanClaculeAndMembreFamilleVO>();

                // On parse la liste pour supprimmer toutes les PC accordées dont le tiers n'est pas compris dans le
                // calcul
                for (PCAAccordeePlanClaculeAndMembreFamilleVO pcVoCourante : pcAccPlaCalAndMemFamVOList) {
                    if (isTiersQdComprisDansCalcul(idTiers, pcVoCourante.getListMembreFamilleVO())) {
                        pcAccPlaCalAndMemFamVOTiersComprisDsPlsCalulList.add(pcVoCourante);
                    }
                }

                pcAccPlaCalAndMemFamVOList = pcAccPlaCalAndMemFamVOTiersComprisDsPlsCalulList;
            }
        }

        if ((null != pcAccPlaCalAndMemFamVOList) && (pcAccPlaCalAndMemFamVOList.size() > 0)) {

            // 1 seule PC accordée
            if (pcAccPlaCalAndMemFamVOList.size() == 1) {

                resultat = new RFRetrieveInfoDroitPCServiceData[1];

                PCAAccordeePlanClaculeAndMembreFamilleVO pcAccPlaCalAndMemFamVOCourant = pcAccPlaCalAndMemFamVOList
                        .get(0);

                if (null != pcAccPlaCalAndMemFamVOCourant) {

                    List<PCAMembreFamilleVO> personneDansPlanCalculList = pcAccPlaCalAndMemFamVOCourant
                            .getListMembreFamilleVO();

                    if (!isTiersQdComprisDansCalcul(idTiers, personneDansPlanCalculList)) {
                        if (vb != null) {
                            RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_QD_S_TIERS_NON_PRIS_DANS_LE_CALCUL");
                        } else {
                            logsList.add(new String[] { IdAdaptationJournaliere, idTiers,
                                    ((BSession) session).getLabel("ERREUR_RF_QD_S_TIERS_NON_PRIS_DANS_LE_CALCUL") });
                        }
                        return null;
                    }

                    String montantExcedendDeRecette = getMontantExcedent(pcAccPlaCalAndMemFamVOCourant,
                            typeDeDecisionPc, pcAccPlaCalAndMemFamVOCourant.getCsEtatPC(), isAdaptationAnnuelle);

                    resultat[0] = setResultat(resultat[0], "", personneDansPlanCalculList,
                            pcAccPlaCalAndMemFamVOCourant.getCsGenrePC(), pcAccPlaCalAndMemFamVOCourant.getCsTypePC(),
                            idTiers, montantExcedendDeRecette, pcAccPlaCalAndMemFamVOCourant.getDateDebut(),
                            pcAccPlaCalAndMemFamVOCourant.getDateFin(),
                            pcAccPlaCalAndMemFamVOCourant.getIdPcAccordee(),
                            pcAccPlaCalAndMemFamVOCourant.getIdVersionDroitPC());

                    // pas d'enfants ni de conjoints pris dans le calcul
                    if (personneDansPlanCalculList.size() == 1) {
                        PCAMembreFamilleVO personneCourante = personneDansPlanCalculList.get(0);
                        if (null != personneCourante) {
                            if (personneCourante.getIsComprisDansCalcul().booleanValue()) {
                                if (personneCourante.getIsRequerantEnfant()) {
                                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.ENFANTS_VIVANT_SEPARES);
                                } else {
                                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES);
                                }
                            } else {
                                throw new Exception(
                                        "RFRetrieveInfoDroitPCService.retrieve(): la seul personne du plan n'est pas comprise dans le calcul");
                            }
                        } else {
                            throw new Exception(
                                    "RFRetrieveInfoDroitPCService.retrieve(): impossible de retrouver la personne prise dans le calcul");
                        }
                    } else if (personneDansPlanCalculList.size() > 1) {

                        boolean hasEnfantPrisDansLeCalcul = false;
                        boolean hasConjointPrisDansLeCalcul = false;
                        boolean isRequerantEnfant = false;

                        for (PCAMembreFamilleVO personneDansCalcul : personneDansPlanCalculList) {
                            if (personneDansCalcul.getIsComprisDansCalcul().booleanValue()) {
                                if (personneDansCalcul.getCsRoleFamillePC().equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {
                                    hasEnfantPrisDansLeCalcul = true;
                                } else if (personneDansCalcul.getCsRoleFamillePC().equals(
                                        IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                                    hasConjointPrisDansLeCalcul = true;
                                } else if (personneDansCalcul.getCsRoleFamillePC().equals(
                                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT)
                                        && personneDansCalcul.getIsRequerantEnfant()) {
                                    isRequerantEnfant = true;
                                }
                            }
                        }

                        // conjoint et enfant pris dans le calcul
                        if (hasEnfantPrisDansLeCalcul && hasConjointPrisDansLeCalcul) {
                            resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_AVEC_ENFANTS);
                        } else {
                            // Pas d'enfant mais un conjoint
                            if (!hasEnfantPrisDansLeCalcul && hasConjointPrisDansLeCalcul) {
                                if (pcAccPlaCalAndMemFamVOCourant.getCsGenrePC().equals(
                                        IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_A_DOMICILE);
                                } else {
                                    throw new Exception(
                                            "RFRetrieveInfoDroitPCService.retrieve(): données PC inconsistantes");
                                }
                            } else if (hasEnfantPrisDansLeCalcul && !hasConjointPrisDansLeCalcul) {
                                resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.ADULTE_AVEC_ENFANTS);
                            } else if (!hasEnfantPrisDansLeCalcul && !hasConjointPrisDansLeCalcul) {
                                if (isRequerantEnfant) {
                                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.ENFANTS_VIVANT_SEPARES);
                                } else {
                                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.PERSONNES_SEULES_VEUVES);
                                }
                            } else {
                                throw new Exception(
                                        "RFRetrieveInfoDroitPCService.retrieve(): données PC inconsistantes");
                            }
                        }

                    } else {
                        throw new Exception(
                                "RFRetrieveInfoDroitPCService.retrieve(): aucunes personnes prises dans le calcul");
                    }

                } else {
                    throw new Exception(
                            "RFRetrieveInfoDroitPCService.retrieve(): impossible de retrouver la PC accordée");
                }
                // 2 PC accordées
            } else if (pcAccPlaCalAndMemFamVOList.size() == 2) {

                resultat = new RFRetrieveInfoDroitPCServiceData[2];

                boolean hasPCAccordeeHome = false;
                boolean hasPCAccordeeDom = false;
                int i = 0;
                for (PCAAccordeePlanClaculeAndMembreFamilleVO pcAccPlaCalAndMemFamVOCourant : pcAccPlaCalAndMemFamVOList) {

                    if (null != pcAccPlaCalAndMemFamVOCourant) {

                        if (!isTiersQdComprisDansCalcul(idTiers, pcAccPlaCalAndMemFamVOCourant.getListMembreFamilleVO())) {
                            throw new Exception("RFRetrieveInfoDroitPCService.retrieve(): "
                                    + ((BSession) session).getLabel("ERREUR_RF_QD_S_TIERS_NON_PRIS_DANS_LE_CALCUL"));
                        }

                        String montantExcedendDeRecette = getMontantExcedent(pcAccPlaCalAndMemFamVOCourant,
                                typeDeDecisionPc, pcAccPlaCalAndMemFamVOCourant.getCsEtatPC(), isAdaptationAnnuelle);

                        resultat[i] = setResultat(resultat[i], "",
                                pcAccPlaCalAndMemFamVOCourant.getListMembreFamilleVO(),
                                pcAccPlaCalAndMemFamVOCourant.getCsGenrePC(),
                                pcAccPlaCalAndMemFamVOCourant.getCsTypePC(),
                                pcAccPlaCalAndMemFamVOCourant.getIdTiersBeneficiair(), montantExcedendDeRecette,
                                pcAccPlaCalAndMemFamVOCourant.getDateDebut(),
                                pcAccPlaCalAndMemFamVOCourant.getDateFin(),
                                pcAccPlaCalAndMemFamVOCourant.getIdPcAccordee(),
                                pcAccPlaCalAndMemFamVOCourant.getIdVersionDroitPC());
                        i++;

                        if (pcAccPlaCalAndMemFamVOCourant.getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
                            hasPCAccordeeDom = true;
                        } else if (pcAccPlaCalAndMemFamVOCourant.getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                            hasPCAccordeeHome = true;
                        } else {
                            throw new Exception("RFRetrieveInfoDroitPCService.retrieve(): Genre PC accordée inconnu");
                        }

                    } else {
                        throw new Exception(
                                "RFRetrieveInfoDroitPCService.retrieve(): impossible de retrouver la PC accordée");
                    }
                }

                if (!hasPCAccordeeDom && hasPCAccordeeHome) {
                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME);
                    resultat[1].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_HOME);
                } else if (hasPCAccordeeDom && hasPCAccordeeHome) {
                    resultat[0].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE);
                    resultat[1].setTypeBeneficiaire(IRFTypesBeneficiairePc.COUPLE_SEPARE_MALADIE_HOME_DOMICILE);
                } else {
                    throw new Exception("RFRetrieveInfoDroitPCService.retrieve(): données PC inconsistantes");
                }

            } else {
                /*
                 * TODO: attention si 3 pc accordées pour couple avec dateQd = dateFin pcAccordee conjoint 1 et dateQd =
                 * date début pcAccordee conjoint 2
                 */
                throw new Exception("RFRetrieveInfoDroitPCService.retrieve(): plus de 2 PCAccordées trouvées");
            }

            // Recherche du RI
            for (RFRetrieveInfoDroitPCServiceData infoDroitPcCourant : resultat) {
                infoDroitPcCourant.setRi(isRi(infoDroitPcCourant.getTypeBeneficiaire(),
                        infoDroitPcCourant.getIdPcAccordee(), (BSession) session, (BTransaction) transaction,
                        isCreationQdManuelle(IdAdaptationJournaliere, isAdaptationAnnuelle), vb));
            }

            // Recherche du type de home
            RFRetrieveTypeHomeService rfRetTypHomSer = new RFRetrieveTypeHomeService((BTransaction) transaction);
            for (RFRetrieveInfoDroitPCServiceData infoDroitPcCourant : resultat) {
                if (infoDroitPcCourant.getGenrePc().equals(IPCPCAccordee.CS_GENRE_PC_HOME)) {
                    String typeHome = rfRetTypHomSer.getCsTypeHome(infoDroitPcCourant.getIdTiers(),
                            infoDroitPcCourant.getDateDebutPcaccordee(), infoDroitPcCourant.getIdVersionDroit());

                    if (!JadeStringUtil.isBlankOrZero(typeHome)) {
                        infoDroitPcCourant.setTypeRemboursement(typeHome);
                    }
                } else {
                    infoDroitPcCourant.setTypeRemboursement(IRFQd.CS_TYPE_REMBOURSEMENT_DOMICILE);
                }
            }

            // Recherche du degre de l'API
            RFRetrieveDegreApiService rfRetDegApiSer = new RFRetrieveDegreApiService((BTransaction) transaction);
            for (RFRetrieveInfoDroitPCServiceData infoDroitPcCourant : resultat) {
                infoDroitPcCourant.setCsDegreApi(rfRetDegApiSer.retrieveCsDegreApi(infoDroitPcCourant.getIdTiers(),
                        infoDroitPcCourant.getDateDebutPcaccordee(), infoDroitPcCourant.getIdVersionDroit()));
            }

            return resultat;
        } else {
            return null;
        }
    }

    private RFRetrieveInfoDroitPCServiceData setResultat(RFRetrieveInfoDroitPCServiceData resultat,
            String typeBeneficiaire, List<PCAMembreFamilleVO> personnesDansCalcul, String genrePc, String typePc,
            String idTiers, String soldeExcedent, String dateDebutPcaccordee, String dateFinPcaccordee,
            String idPcAccordee, String idVersionDroit) throws Exception {

        resultat = new RFRetrieveInfoDroitPCServiceData();

        if ((null != genrePc)
                && (genrePc.equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE) || genrePc
                        .equals(IPCPCAccordee.CS_GENRE_PC_HOME))) {
            resultat.setGenrePc(genrePc);
        } else {
            throw new Exception("RFRetrieveInfoDroitPCService.setResultat(): impossible de retrouver le type PC");
        }

        if (!JadeStringUtil.isBlankOrZero(typePc) && !JadeStringUtil.isBlankOrZero(idTiers)
                && !JadeStringUtil.isBlankOrZero(dateDebutPcaccordee)) {

            resultat.setTypeBeneficiaire(typeBeneficiaire);
            resultat.setPersonneDansPlanCalculList(personnesDansCalcul);
            resultat.setTypePc(typePc);
            resultat.setIdTiers(idTiers);
            resultat.setSoldeExcedent(soldeExcedent);
            resultat.setDateDebutPcaccordee(dateDebutPcaccordee);
            resultat.setDateFinPcaccordee(dateFinPcaccordee);
            resultat.setIdPcAccordee(idPcAccordee);
            resultat.setIdVersionDroit(idVersionDroit);

        } else {
            throw new Exception(
                    "RFRetrieveInfoDroitPCService.setResultat(): impossible de retrouver les informations liées au droit PC");
        }

        return resultat;
    }
}
