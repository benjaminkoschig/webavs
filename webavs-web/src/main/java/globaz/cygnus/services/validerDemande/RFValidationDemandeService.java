/*
 * Créé le 17 février 2010
 */
package globaz.cygnus.services.validerDemande;

import globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins;
import globaz.cygnus.api.demandes.IRFDemande;
import globaz.cygnus.api.motifsRefus.IRFMotifsRefus;
import globaz.cygnus.db.attestations.RFMaintienDomicile;
import globaz.cygnus.db.demandes.RFPrDemandeJointDossier;
import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.preparerDecision.RFCalculMontantAPayerData;
import globaz.cygnus.services.preparerDecision.RFVerificationAttestationsService;
import globaz.cygnus.services.preparerDecision.RFVerificationConventionData;
import globaz.cygnus.services.preparerDecision.RFVerificationConventionService;
import globaz.cygnus.services.preparerDecision.RFVerificationDelaisDemandeData;
import globaz.cygnus.services.preparerDecision.RFVerificationDelaisDemandeService;
import globaz.cygnus.services.preparerDecision.RFVerificationDoublonData;
import globaz.cygnus.services.preparerDecision.RFVerificationMotifsRefusUtilisateurService;
import globaz.cygnus.services.preparerDecision.RFVerificationQdAssureData;
import globaz.cygnus.services.preparerDecision.RFVerificationQdAssureService;
import globaz.cygnus.services.preparerDecision.RFVerificationQdPrincipaleData;
import globaz.cygnus.services.preparerDecision.RFVerificationQdPrincipaleService;
import globaz.cygnus.services.preparerDecision.RFVerificationTypesDeSoinsData;
import globaz.cygnus.services.preparerDecision.RFVerificationTypesDeSoinsService;
import globaz.cygnus.services.preparerDecision.VerificationAttestation;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.cygnus.business.constantes.ERFProperties;

/**
 * 
 * @author jje
 */
public class RFValidationDemandeService {

    private JACalendar cal = new JACalendarGregorian();
    private String csDegreApi = "";
    private String csGenrePcAccordee = "";
    private String csTypeBeneficiaire = "";
    private String csTypePcAccordee = "";
    private String dateFacture = "";
    private String idQdPrincipale = "";
    private boolean isConventionne = false;
    private String method = "";
    private String montantAPayer = "";
    private String montantFacture = "";

    private RFSaisieDemandeViewBean viewBean = null;

    public RFValidationDemandeService(FWViewBeanInterface viewBean, String method) {
        this.viewBean = (RFSaisieDemandeViewBean) viewBean;
        this.method = method;
    }

    private void attestations() throws Exception {

        RFVerificationAttestationsService rfVerAttSer = new RFVerificationAttestationsService(viewBean.getSession());

        String idDossier = "";
        if (JadeStringUtil.isBlankOrZero(viewBean.getIdDossier())) {
            RFPrDemandeJointDossier rfPrDem = RFUtils.getDossierJointPrDemande(viewBean.getIdTiers(),
                    viewBean.getSession());
            if (rfPrDem != null) {
                idDossier = rfPrDem.getIdDossier();
            }
        } else {
            idDossier = viewBean.getIdDossier();
        }

        if (!JadeStringUtil.isBlankOrZero(idDossier)) {
            VerificationAttestation verificationAttestation = rfVerAttSer.hasAttestationSelonSousTypeDeSoin(
                    viewBean.getCodeTypeDeSoinList(),
                    viewBean.getCodeSousTypeDeSoinList(),
                    JadeStringUtil.isBlankOrZero(viewBean.getDateDebutTraitement()) ? (JadeStringUtil
                            .isBlankOrZero(viewBean.getDateFacture()) ? viewBean.getDateDecompte() : viewBean
                            .getDateFacture()) : viewBean.getDateDebutTraitement(), idDossier, "");
            if (verificationAttestation.hasNiveauAvertissement()) {
                if (!verificationAttestation.hasAttestation()) {
                    if (verificationAttestation.isNiveauAvertissement()) {
                        RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_ATTESTATION_NON_TROUVEE_FACULTATIF");
                    } else {
                        RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_ATTESTATION_NON_TROUVEE_OBLIGATOIRE");
                        viewBean.setCsStatut(IRFDemande.REFUSE);
                    }

                } else {
                    if (ERFProperties.VERIFIER_ATTESTATION_MAINTIEN_DOMICILE_CONROLE_NOMBRE_HEURE.getBooleanValue()) {
                        RFMaintienDomicile maintienDomicile = new RFMaintienDomicile();
                        maintienDomicile.setId(verificationAttestation.getIdAttestation());
                        maintienDomicile.setSession(viewBean.getSession());
                        maintienDomicile.retrieve();
                        if (!maintienDomicile.isNew()) {
                            String nbHeureDemande = viewBean.getNombreHeure();
                            String nbheureAttestations = maintienDomicile.getDureeAideRemunereTenueMenage();
                            if (JadeStringUtil.isBlank(nbheureAttestations)) {
                                nbheureAttestations = "0";
                            }
                            float i_nbheureAttestations = Float.valueOf(nbheureAttestations);
                            if (!JadeStringUtil.isBlank(nbHeureDemande)) {
                                float i_nbHeureDemande = Float.valueOf(nbHeureDemande);
                                if (i_nbHeureDemande > i_nbheureAttestations) {
                                    RFUtils.setMsgWarningViewBean(viewBean,
                                            "WARNING_RF_DEM_S_ATTESTATION_NB_HEURE_NOT_SAME", i_nbHeureDemande,
                                            i_nbheureAttestations);
                                }
                            } else {
                                RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_NB_HEURE_NOT_DEFINDED",
                                        i_nbheureAttestations);
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Test si les délais sont respectés
     */
    private void delais() throws Exception {

        Set<String> typeDeSoinPeriodeTraitementNonObligatoireSet = new HashSet<String>();

        if (RFPropertiesUtils.imputationParPeriodeDeTraitement()) {
            initTypeDeSoinImputationParPeriodeDeTraitement(typeDeSoinPeriodeTraitementNonObligatoireSet);
        } else {
            initTypeDeSoinImputationParDateDeFacture(typeDeSoinPeriodeTraitementNonObligatoireSet);
        }

        if (JadeStringUtil.isBlankOrZero(viewBean.getDateReception())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_RECEPTION_OBLIGATOIRE");
        }

        if (viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS)
                || viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_18_FRAIS_REFUSES)) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getDateDecompte())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_DECOMPTE_OBLIGATOIRE");

            } else {
                dateFacture = viewBean.getDateDecompte();

                if (isDateFactureFuture(dateFacture)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DECOMPTE_FUTUR");
                }

            }
        } else if (viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES)) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getDateDecisionOAI())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_FACTURE_OBLIGATOIRE");
            } else {
                dateFacture = viewBean.getDateDecisionOAI();

                if (isDateFactureFuture(dateFacture)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DECISION_OAI_FUTUR");
                }
            }
        } else if (viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE)) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getDateFacture())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DEVIS_OBLIGATOIRE");
            } else {
                dateFacture = viewBean.getDateFacture();

                if (isDateFactureFuture(dateFacture)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_FACTURE_FUTUR");
                }
            }

        } else {

            if (JadeStringUtil.isBlankOrZero(viewBean.getDateFacture())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_FACTURE_OBLIGATOIRE");
            } else {
                dateFacture = viewBean.getDateFacture();

                if (isDateFactureFuture(dateFacture)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_FACTURE_FUTUR");
                }
            }
        }

        // Test si la période de traitement est saisie
        if (typeDeSoinPeriodeTraitementNonObligatoireSet.contains(viewBean.getCodeTypeDeSoinList())) {

            if (!JadeStringUtil.isBlankOrZero(viewBean.getDateDebutTraitement())
                    && !JadeStringUtil.isBlankOrZero(viewBean.getDateFinTraitement())) {
                isDateDebutAnterieurDateFin(new JADate(viewBean.getDateDebutTraitement()),
                        new JADate(viewBean.getDateFinTraitement()));
            }

        } else {

            if (JadeStringUtil.isBlankOrZero(viewBean.getDateDebutTraitement())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_DEBUT_TRAITEMENT_OBLIGATOIRE");
            }

            if (!IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE.equals(viewBean.getCodeTypeDeSoinList())
                    && !IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())
                    && !IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE.equals(viewBean
                            .getCodeTypeDeSoinList()) && JadeStringUtil.isBlankOrZero(viewBean.getDateFinTraitement())) {

                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_DE_FIN_TRAITEMENT_OBLIGATOIRE");
            }

            // Test si la date de début de traitement est postérieur à la date
            // de fin de traitement
            if (!JadeStringUtil.isBlankOrZero(viewBean.getDateFinTraitement())
                    && !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                isDateDebutAnterieurDateFin(new JADate(viewBean.getDateDebutTraitement()),
                        new JADate(viewBean.getDateFinTraitement()));
            }

        }

        PRTiersWrapper w = PRTiersHelper.getTiersParId(viewBean.getSession(), viewBean.getIdTiers());
        String dateDeces = w.getDateDeces();

        // Test si le délai de 15 mois et le délai décès est respecté
        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            boolean demandeNonRetro = !viewBean.getIsRetro();

            RFVerificationDelaisDemandeService rfVerificationDelaisDemandeService = new RFVerificationDelaisDemandeService();
            Set<String[]> idsMotifRefus = rfVerificationDelaisDemandeService.verifierDelais_Deces_15Mois(
                    new RFVerificationDelaisDemandeData(dateFacture, viewBean.getDateReception(),
                            viewBean.getIdTiers(), viewBean.getMontantAPayer(), dateDeces), viewBean.getSession(),
                    demandeNonRetro);

            if ((null != idsMotifRefus) && (idsMotifRefus.size() > 0)) {

                for (String idMotifRefu[] : idsMotifRefus) {
                    if (idMotifRefu[0].equals(IRFMotifsRefus.ID_DELAI_15_MOIS_DEPASSE) && demandeNonRetro) {
                        RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_DELAI_15_MOIS_DEPASSE");
                    } else if (idMotifRefu[0].equals(IRFMotifsRefus.ID_DELAI_DECES_DEPASSE) && demandeNonRetro) {
                        RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_DELAI_12_MOIS_DECES_DEPASSE");
                    }
                }
                viewBean.setCsStatut(IRFDemande.REFUSE);
            }
        }

        // Régime, si la date de début du régime concerne une année antérieur à la date de dernier paiement des rentes,
        // on set la date de fin du régime à la fin de l'année
        if (IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE.equals(viewBean.getCodeTypeDeSoinList())) {

            BigDecimal anneeDernierPaiementRenteBig = new BigDecimal(PRDateFormater.convertDate_MMxAAAA_to_AAAA(RFUtils
                    .getDateDernierPaiementMensuelRente("", viewBean.getSession())));

            if (new BigDecimal(PRDateFormater.convertDate_MMxAAAA_to_AAAA(viewBean.getDateDebutTraitement()))
                    .compareTo(anneeDernierPaiementRenteBig) < 0) {

                if (JadeStringUtil.isBlankOrZero(viewBean.getDateFinTraitement())) {
                    viewBean.setDateFinTraitement("31.12."
                            + PRDateFormater.convertDate_MMxAAAA_to_AAAA(viewBean.getDateDebutTraitement()));
                } else {
                    if (new BigDecimal(PRDateFormater.convertDate_MMxAAAA_to_AAAA(viewBean.getDateFinTraitement()))
                            .compareTo(anneeDernierPaiementRenteBig) > 0) {
                        viewBean.setDateFinTraitement("31.12."
                                + PRDateFormater.convertDate_MMxAAAA_to_AAAA(viewBean.getDateDebutTraitement()));
                    }
                }
            }
        }
    }

    /**
     * Vérifie si la facture à déjà été remboursée
     */
    private void doublons(String idDemandeToIgnore) throws Exception {

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            Set<String> idsDemandeToIgnore = new HashSet<String>();
            if (!JadeStringUtil.isBlankOrZero(idDemandeToIgnore)) {
                idsDemandeToIgnore.add(idDemandeToIgnore);
            }
            if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeParent())) {
                idsDemandeToIgnore.add(viewBean.getIdDemandeParent());
            }

            String labelWarning = RFVerificationDoublonService.doublons(
                    new RFVerificationDoublonData(viewBean.getIdTiers(), viewBean.getCodeSousTypeDeSoinList(), viewBean
                            .getCodeTypeDeSoinList(), idsDemandeToIgnore, dateFacture, viewBean
                            .getDateDebutTraitement(), viewBean.getDateFinTraitement(), montantFacture, viewBean
                            .getNumeroDecompte()), viewBean.getSession(), false, null);

            if (!JadeStringUtil.isEmpty(labelWarning)) {
                RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_DECOMPTE_FACTURE_DEJA_REMBOURSE");
            }
        }
    }

    private void initTypeDeSoinImputationParDateDeFacture(Set<String> typeDeSoinPeriodeTraitementNonObligatoireSet) {

        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_1_COTISATIONS_PARITAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_4_RETOUCHES_COUTEUSES_DE_CHAUSSURES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet
                .add(IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI);
        typeDeSoinPeriodeTraitementNonObligatoireSet
                .add(IRFCodeTypesDeSoins.TYPE_8_LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_9_FRAIS_DE_LIVRAISON);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_10_REPRISE_DE_LIT_ELECTRIQUE);
        typeDeSoinPeriodeTraitementNonObligatoireSet
                .add(IRFCodeTypesDeSoins.TYPE_11_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_12_STRUCTURE_ET_SEJOURS);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_13_MAINTIEN_A_DOMICILE);
        typeDeSoinPeriodeTraitementNonObligatoireSet
                .add(IRFCodeTypesDeSoins.TYPE_14_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_15_FRAIS_DE_TRAITEMENT_DENTAIRE);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_16_FRAIS_DE_TRANSPORT);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_18_FRAIS_REFUSES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_20_FINANCEMENT_DES_SOINS);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_21_REPRISE);

    }

    private void initTypeDeSoinImputationParPeriodeDeTraitement(Set<String> typeDeSoinPeriodeTraitementNonObligatoireSet) {

        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_3_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_4_RETOUCHES_COUTEUSES_DE_CHAUSSURES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES);
        typeDeSoinPeriodeTraitementNonObligatoireSet
                .add(IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_9_FRAIS_DE_LIVRAISON);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_10_REPRISE_DE_LIT_ELECTRIQUE);
        typeDeSoinPeriodeTraitementNonObligatoireSet.add(IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE);

    }

    private void isDateDebutAnterieurDateFin(JADate dateDebut, JADate dateFin) {

        if (cal.compare(dateDebut, dateFin) == JACalendar.COMPARE_FIRSTUPPER) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_DATE_FIN_ANTERIEUR_DATE_DEBUT_TRAITEMENT");
        }
    }

    private boolean isDateFactureFuture(String dateFacture) throws Exception {

        JADate jaDateFacture = new JADate(dateFacture);

        if (((cal.compare(jaDateFacture, new JADate(JACalendar.todayJJsMMsAAAA())) == JACalendar.COMPARE_FIRSTUPPER))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recherche si le plafond imposé par la convention est atteint. Il ne peut exister qu'un seul montant pour un type
     * bénéficiaire, un fournisseur et un sous type de soin
     */
    private void montantsConventions() throws Exception {

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            RFVerificationConventionService rfVerificationConventionService = new RFVerificationConventionService(
                    new RFVerificationConventionData(viewBean.getIdTiers(), viewBean.getCodeSousTypeDeSoinList(),
                            viewBean.getCodeTypeDeSoinList(), viewBean.getIdFournisseurDemande(),
                            viewBean.getDateDebutTraitement(), dateFacture, viewBean.getCsEtat(), montantAPayer,
                            idQdPrincipale, viewBean.getIdDemande(), csTypeBeneficiaire, csTypePcAccordee,
                            csGenrePcAccordee, csDegreApi), viewBean.getSession(), false, null);

            RFCalculMontantAPayerData idsMotifRefusConventions = rfVerificationConventionService.convention();

            // Si null pas de limite trouvée
            if (null != idsMotifRefusConventions) {
                // Test si la demande est refusée totalement ou partiellement
                // accepté
                if ((idsMotifRefusConventions.getIdStrMotifDeRefus().size() > 0)
                        && JadeStringUtil.isEmpty(idsMotifRefusConventions.getMontantAccepte())) {

                    RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_MONTANT_CONVENTION_DEPASSE");
                    viewBean.setCsStatut(IRFDemande.REFUSE);

                } else if ((idsMotifRefusConventions.getIdStrMotifDeRefus().size() > 0)
                        && !JadeStringUtil.isEmpty(idsMotifRefusConventions.getMontantAccepte())) {

                    RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_MONTANT_CONVENTION_DEPASSE");
                    if (!IRFDemande.REFUSE.equals(viewBean.getCsStatut())) {
                        viewBean.setCsStatut(IRFDemande.PARTIELLEMENT_ACCEPTE);
                    }
                }

            }

        }
    }

    /**
     * Test si les montants sont saisis
     */
    private void montantsFacture() throws Exception {

        montantFacture = viewBean.getMontantFacture();
        montantAPayer = viewBean.getMontantAPayer();

        // Test si le montant de la facture est saisi
        if (IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(viewBean.getCodeTypeDeSoinList())
                || IRFCodeTypesDeSoins.TYPE_6_REPARTITION_DES_MOYENS_AUXILIAIRES.equals(viewBean
                        .getCodeTypeDeSoinList())
                || IRFCodeTypesDeSoins.TYPE_7_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI
                        .equals(viewBean.getCodeTypeDeSoinList())) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getMontantFacture44())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_OBLIGATOIRE");
            } else {
                if (RFUtils.isMontantArrondiCinqCts(viewBean.getMontantFacture44()).booleanValue()) {
                    montantFacture = viewBean.getMontantFacture44();
                } else {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
                }
            }
        } else if (IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS.equals(viewBean.getCodeTypeDeSoinList())
                || IRFCodeTypesDeSoins.TYPE_18_FRAIS_REFUSES.equals(viewBean.getCodeTypeDeSoinList())) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getMontantDecompte())
                    && JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeParent())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_OBLIGATOIRE");
            } else {
                if (RFUtils.isMontantArrondiCinqCts(viewBean.getMontantDecompte()).booleanValue()) {
                    montantFacture = viewBean.getMontantDecompte();
                } else {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
                }
            }

            if (viewBean.getIsPP().booleanValue()) {
                if (JadeStringUtil.isBlankOrZero(viewBean.getMontantMensuel())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_MENSUEL_OBLIGATOIRE");
                } else {
                    if (!RFUtils.isMontantArrondiCinqCts(viewBean.getMontantMensuel()).booleanValue()) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_MENSUEL_ARRONDI");
                    }
                }
            }
        }

        if (IRFCodeTypesDeSoins.TYPE_2_REGIME_ALIMENTAIRE.equals(viewBean.getCodeTypeDeSoinList())) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getMontantMensuel())
                    && JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeParent())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_MENSUEL_OBLIGATOIRE");
            } else {
                if (!RFUtils.isMontantArrondiCinqCts(viewBean.getMontantMensuel()).booleanValue()) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_MENSUEL_ARRONDI");
                }
            }
        }

        // Test si le montant versé OAI est saisi
        if (IRFCodeTypesDeSoins.TYPE_5_MOYENS_AUXILIAIRES.equals(viewBean.getCodeTypeDeSoinList())) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getMontantVerseOAI())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_OAI_OBLIGATOIRE");
            } else {
                if (RFUtils.isMontantArrondiCinqCts(viewBean.getMontantVerseOAI()).booleanValue()) {
                    montantFacture = viewBean.getMontantVerseOAI();
                } else {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
                }
            }
        }

        // Test si le montant de l'acceptation est saisi
        if (IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE.equals(viewBean.getCodeTypeDeSoinList())) {

            if (JadeStringUtil.isBlankOrZero(viewBean.getMontantAcceptation())) {
                // RFUtils.setMsgErreurViewBean(this.viewBean, "ERREUR_RF_DEM_S_MONTANT_ACCEPTATION_OBLIGATOIRE");
                montantFacture = "";
                montantAPayer = "";
            } else {
                if (RFUtils.isMontantArrondiCinqCts(viewBean.getMontantAcceptation()).booleanValue()) {
                    montantFacture = viewBean.getMontantAcceptation();
                    montantAPayer = viewBean.getMontantAcceptation();

                    if (!RFUtils.isMontantArrondiCinqCts(montantFacture).booleanValue()) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
                    }

                } else {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
                }
            }
        }

        // Test si le montant de la facture n'est pas vide
        if (JadeStringUtil.isBlankOrZero(montantFacture)
                && !IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE.equals(viewBean.getCodeTypeDeSoinList())
                && JadeStringUtil.isBlankOrZero(viewBean.getIdDemandeParent())) {
            RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_OBLIGATOIRE");
        } else {
            if (!RFUtils.isMontantArrondiCinqCts(montantFacture).booleanValue()) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_FACTURE_ARRONDI");
            }
        }

        // Test si les montants des motifs de refus sont arrondi
        for (String key : viewBean.getMontantsMotifsRefus().keySet()) {

            String montant = viewBean.getMontantsMotifsRefus().get(key)[0];
            if ((null != montant)) {
                if (!RFUtils.isMontantArrondiCinqCts(montant)) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_MONTANT_MOTIF_REFUS_ARRONDI");
                }
            }
        }

    }

    /**
     * Recherche si le montant résiduel de la Qd assuré est atteint
     */
    private void montantsQdAssure() throws Exception {

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) && !JadeStringUtil.isEmpty(idQdPrincipale)) {

            // Recherche si le bénéficiaire concerne un enfant
            boolean isEnfant = RFUtils.isEnfant(viewBean.getSession(), viewBean.getIdTiers(), idQdPrincipale);

            RFVerificationQdAssureService rfVerificationQdAssureService = new RFVerificationQdAssureService();
            RFCalculMontantAPayerData idsMotifRefus = rfVerificationQdAssureService.montantQdAssure(
                    new RFVerificationQdAssureData(viewBean.getDateDebutTraitement(), dateFacture, viewBean
                            .getIdTiers(), montantAPayer, idQdPrincipale, viewBean.getCodeSousTypeDeSoinList(),
                            viewBean.getCodeTypeDeSoinList(), isEnfant), viewBean.getSession(), false, null,
                    csTypeBeneficiaire, csGenrePcAccordee);

            // Si null pas de limite trouvée
            if (null != idsMotifRefus) {
                // Test si la demande est refusé totalement ou partiellement
                // accepté
                if ((idsMotifRefus.getIdStrMotifDeRefus().size() > 0)
                        && JadeStringUtil.isEmpty(idsMotifRefus.getMontantAccepte())) {

                    RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_PLAFOND_SOUS_TYPE_SOIN_DEPASSE");
                    viewBean.setCsStatut(IRFDemande.REFUSE);

                } else if ((idsMotifRefus.getIdStrMotifDeRefus().size() > 0)
                        && !JadeStringUtil.isEmpty(idsMotifRefus.getMontantAccepte())) {

                    RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_PLAFOND_SOUS_TYPE_SOIN_DEPASSE");
                    if (!IRFDemande.REFUSE.equals(viewBean.getCsStatut())) {
                        viewBean.setCsStatut(IRFDemande.PARTIELLEMENT_ACCEPTE);
                    }
                }

            }

        }
    }

    /**
     * Recherche si le montant résiduel de la Qd principale est atteint
     */
    private void montantsQdPrincipale() throws Exception {

        // Recherche la Qd principale selon la date de début traitement ou la
        // date de facture
        // si la date de début de traitement est nulle
        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            RFVerificationQdPrincipaleService rfVerificationQdPrincipaleService = new RFVerificationQdPrincipaleService();
            RFCalculMontantAPayerData rfCalMonAPayDat = rfVerificationQdPrincipaleService.montantQdPrincipale(
                    new RFVerificationQdPrincipaleData(viewBean.getIdQdPrincipale(), viewBean.getDateDebutTraitement(),
                            dateFacture, viewBean.getIdTiers(), montantAPayer, viewBean.getCodeTypeDeSoinList(),
                            viewBean.getCodeSousTypeDeSoinList()), viewBean.getSession(), false, null, null);

            if (null != rfCalMonAPayDat) {

                idQdPrincipale = rfCalMonAPayDat.getIdQd();
                csTypeBeneficiaire = rfCalMonAPayDat.getCsTypeBeneficiaire();
                csGenrePcAccordee = rfCalMonAPayDat.getCsGenrePcAccordee();
                csTypePcAccordee = rfCalMonAPayDat.getCsTypePcAccordee();
                csDegreApi = rfCalMonAPayDat.getCsDegreApi();

                for (String[] motifCourant : rfCalMonAPayDat.getIdStrMotifDeRefus()) {

                    if (!viewBean.getCodeTypeDeSoinList().equals(RFUtils.CODE_TYPE_DE_SOIN_FINANCEMENT_DES_SOINS_STR)) {

                        if (motifCourant[0].equals(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)) {
                            RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_MONTANT_GRANDE_QD_DEPASSE");
                        }

                        if (motifCourant[0].equals(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)) {
                            RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_SOLDE_EXCEDENT");
                        }
                    }

                    if (motifCourant[0].equals(IRFMotifsRefus.ID_ENFANT_EXCLUS_PC)) {
                        RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_ENFANT_EXCLU");
                    }

                    if (motifCourant[0].equals(IRFMotifsRefus.ID_MAXIMUM_N_FRANC_PAR_ANNEE)
                            || motifCourant[0].equals(IRFMotifsRefus.ID_SOLDE_EXECEDENT_DE_REVENU)
                            || motifCourant[0].equals(IRFMotifsRefus.ID_ENFANT_EXCLUS_PC)) {

                        if ((JadeStringUtil.isBlankOrZero(rfCalMonAPayDat.getMontantAccepte())
                                || rfCalMonAPayDat.getMontantAccepte().equals("0.00") || rfCalMonAPayDat
                                .getMontantAccepte().equals("0.0"))) {

                            viewBean.setCsStatut(IRFDemande.REFUSE);

                        } else {

                            if (!IRFDemande.REFUSE.equals(viewBean.getCsStatut())) {
                                viewBean.setCsStatut(IRFDemande.PARTIELLEMENT_ACCEPTE);
                            }

                        }
                    }

                }

            } else {
                RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_PAS_DE_DROIT_PC");
                idQdPrincipale = "";
                viewBean.setCsStatut(IRFDemande.REFUSE);
            }

        }

    }

    private void motifsDeRefusUtilisateur() throws Exception {

        RFVerificationMotifsRefusUtilisateurService rfVerificationMotifsRefusUtilisateurService = new RFVerificationMotifsRefusUtilisateurService();
        String statutVerMotRefUtiSer = rfVerificationMotifsRefusUtilisateurService.verifierMotifsDeRefusUtilisateur(
                viewBean.getMontantsMotifsRefus().entrySet(), viewBean.getCsStatut());

        // if (!IRFDemande.REFUSE.equals(this.viewBean.getCsStatut())) {
        viewBean.setCsStatut(statutVerMotRefUtiSer);
        // }
    }

    /**
     * Methode pour controler les valeurs saisies dans le type de véhicule et les
     * 
     * @throws Exception
     */
    private void transport() throws Exception {

        // controle le type de soin
        if (viewBean.getCodeTypeDeSoinList().equals(IRFCodeTypesDeSoins.TYPE_16_FRAIS_DE_TRANSPORT)) {

            // Controle le type de véhicule sélectionné. Si privée ou privéeAI
            if (viewBean.getCsTypeVhc().equals(IRFDemande.CS_TYPE_TRANSPORT_VOITURE_PRIVEE)
                    || viewBean.getCsTypeVhc().equals(IRFDemande.CS_TYPE_TRANSPORT_VOITURE_PRIVEE_AI)) {

                // Controle le champ du nombre de kilomètres
                if (JadeStringUtil.isBlankOrZero(viewBean.getNombreKilometres())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_NOMBRE_KILOMETRES_OBLIGATOIRE");
                }
                // Controle le champ du prix par kilomètre
                if (JadeStringUtil.isBlankOrZero(viewBean.getPrixKilometre())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_PRIX_DU_KILOMETRE_OBLIGATOIRE");
                }
            }
        }
    }

    /**
     * Tests spécifiques aux frais de livraison 9, moyens auxiliaires 3 5 6 7 8 11, maintien à domicile 13, structure et
     * séjour 12
     */
    private void typesDeSoins() throws Exception {

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

            RFVerificationTypesDeSoinsService rfVerificationTypesDeSoinsService = new RFVerificationTypesDeSoinsService(
                    new RFVerificationTypesDeSoinsData(viewBean.getCodeTypeDeSoinList(),
                            viewBean.getCodeSousTypeDeSoinList(), isConventionne, montantAPayer,
                            viewBean.getMontantVerseOAI(), viewBean.getMontantFacture44(), csTypePcAccordee,
                            csTypeBeneficiaire, csGenrePcAccordee), viewBean.getSession());

            Set<String[]> idsMotifRefusTypesDeSoins = rfVerificationTypesDeSoinsService.verifierTypesDeSoins();

            // Si null pas de refus
            if ((null != idsMotifRefusTypesDeSoins) && (idsMotifRefusTypesDeSoins.size() > 0)) {
                for (String[] idMotifRefuTdS : idsMotifRefusTypesDeSoins) {
                    if (null != idMotifRefuTdS) {
                        String montantAccepte = idMotifRefuTdS[2];

                        if (idMotifRefuTdS[0]
                                .equals(IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI)) {

                            RFUtils.setMsgWarningViewBean(viewBean,
                                    "WARNING_RF_DEM_S_MOYEN_AUXILIAIRE_MAXIMUM_1_3_CONTRIBUTION_AVS_AI");

                            if (!IRFDemande.REFUSE.equals(viewBean.getCsStatut())) {
                                viewBean.setCsStatut(IRFDemande.PARTIELLEMENT_ACCEPTE);
                            }

                        } else if (idMotifRefuTdS[0]
                                .equals(IRFMotifsRefus.ID_PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL)) {

                            RFUtils.setMsgWarningViewBean(viewBean,
                                    "WARNING_RF_DEM_S_PRIX_DE_PENSION_SUPERIEUR_AU_MAXIMUM_CANTONAL");

                            if (JadeStringUtil.isBlankOrZero(montantAccepte)) {
                                viewBean.setCsStatut(IRFDemande.REFUSE);
                            } else {
                                if (!IRFDemande.REFUSE.equals(viewBean.getCsStatut())) {
                                    viewBean.setCsStatut(IRFDemande.PARTIELLEMENT_ACCEPTE);
                                }
                            }
                        } else if (idMotifRefuTdS[0]
                                .equals(IRFMotifsRefus.ID_FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE)) {
                            RFUtils.setMsgWarningViewBean(viewBean,
                                    "WARNING_RF_DEM_S_FRAIS_DE_LIVRAISON_FOURNISSEUR_NON_CONVENTIONNE");
                            viewBean.setCsStatut(IRFDemande.REFUSE);
                        } else if (idMotifRefuTdS[0].equals(IRFMotifsRefus.ID_MENAGE_NON_CAR_HOME)) {
                            RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_MENAGE_HOME");
                            viewBean.setCsStatut(IRFDemande.REFUSE);
                        } else if (idMotifRefuTdS[0].equals(IRFMotifsRefus.ID_MOYEN_AUXILIAIRE_NON_CAR_HOME)) {
                            RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_MOYEN_AUX_HOME");
                            viewBean.setCsStatut(IRFDemande.REFUSE);
                        } else if (idMotifRefuTdS[0].equals(IRFMotifsRefus.ID_FINANCEMENT_NON_CAR_DOMICILE)) {
                            RFUtils.setMsgWarningViewBean(viewBean, "WARNING_RF_DEM_S_FINANCEMENT_DOM");
                            viewBean.setCsStatut(IRFDemande.REFUSE);
                        }
                    }
                }
            }
        }
    }

    public void validate() {

        try {

            if (RFSetEtatProcessService.getEtatProcessPreparerDecision(viewBean.getSession())) {
                RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_PREPARER_DECISION_DEMARRE");
            }

            if (viewBean.getCsEtat().equals(IRFDemande.CALCULE) && method.equals(RFUtils.upd)) {
                if (RFSetEtatProcessService.getEtatProcessValiderDecision(viewBean.getSession())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_VALIDER_DECISION_DEMARRE");
                }
            }

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                // Initialisation du statut de la demande
                viewBean.setCsStatut(IRFDemande.ACCEPTE);

                // Si on est en modification, on contrôle que l'état est
                // différent de validé ou payé
                if (RFUtils.upd.equals(method)) {
                    if (IRFDemande.VALIDE.equals(viewBean.getCsEtat()) || IRFDemande.PAYE.equals(viewBean.getCsEtat())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_SUPPRESSION_VALIDE_PAYE");
                    }
                }

                // Test si le fournisseur est saisi seulement si la properties "afficher.remarque.fournisseur" est à
                // false
                if (!RFPropertiesUtils.afficherRemarqueFournisseur()) {
                    if (JadeStringUtil.isBlankOrZero(viewBean.getIdFournisseurDemande())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_FOURNISSEUR_OBLIGATOIRE");
                    }
                } else {
                    if (JadeStringUtil.isBlankOrZero(viewBean.getIdFournisseurDemande())
                            && JadeStringUtil.isBlankOrZero(viewBean.getRemarqueFournisseur())) {
                        RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_FOURNISSEUR_OBLIGATOIRE");
                    }
                }

                // Test si l'adresse de paiement est saisie
                if (JadeStringUtil.isBlankOrZero(viewBean.getIdAdressePaiementDemande())
                        && !IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE.equals(viewBean.getCodeTypeDeSoinList())) {
                    RFUtils.setMsgErreurViewBean(viewBean, "ERREUR_RF_DEM_S_ADRESSE_PAIEMENT_OBLIGATOIRE");
                }

                transport();

                delais();

                montantsFacture();

                if (!(JadeStringUtil.isBlankOrZero(viewBean.getMontantAcceptation()) && IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE
                        .equals(viewBean.getCodeTypeDeSoinList()))) {
                    montantsQdPrincipale();
                }

                if (RFUtils.upd.equals(method)) {
                    doublons(viewBean.getIdDemande());
                } else {
                    doublons("");
                }

                if (!(JadeStringUtil.isBlankOrZero(viewBean.getMontantAcceptation()) && IRFCodeTypesDeSoins.TYPE_19_DEVIS_DENTAIRE
                        .equals(viewBean.getCodeTypeDeSoinList()))) {

                    montantsQdAssure();

                    montantsConventions();

                }

                attestations();

                typesDeSoins();

                motifsDeRefusUtilisateur();

            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }
    }
}
