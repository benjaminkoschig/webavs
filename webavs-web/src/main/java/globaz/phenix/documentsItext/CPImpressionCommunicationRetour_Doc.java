package globaz.phenix.documentsItext;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.ged.target.JadeGedTargetProperties;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidation;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidationManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPLienCommunicationsPlausi;
import globaz.phenix.db.communications.CPLienCommunicationsPlausiManager;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPSedexConjoint;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.listes.itext.CPIListeCommunicationRetourParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.translation.CodeSystem;
import globaz.phenix.util.Constante;
import globaz.phenix.util.DocumentInfoPhenix;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPImpressionCommunicationRetour_Doc extends FWIDocumentManager {

    private static final long serialVersionUID = -6988686766450063847L;

    String canton = "";

    private ICommunicationRetour communication = null;

    private CPCommunicationFiscaleRetourValidation entity = null;

    private String forIdPassage = "";
    private String forIdPlausibilite = "";

    private String forStatus = "";

    private java.lang.String fromNumAffilie = "";

    // Champ pour la recherche
    private String idJournalRetour = "";
    private String idRetour = "";

    private CPJournalRetour journalRetour = null;

    private String logs = "";

    private CPCommunicationFiscaleRetourValidationManager manager = null;

    private int nbCommunication = 0;

    private long progressCounter = -1;
    private CPSedexConjoint sedexConjoint = null;
    private BStatement statement = null;
    private TITiersViewBean tiersConjoint = null;
    private TITiersViewBean tiersFiscale = null;

    private java.lang.String tillNumAffilie = "";
    private String tri = "";
    private String traitementUnitaire = "false";
    private Boolean wantMiseEnGEd = Boolean.FALSE;

    public CPImpressionCommunicationRetour_Doc() throws Exception {
        this(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public CPImpressionCommunicationRetour_Doc(BProcess parent) throws FWIException {
        super(parent, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    public CPImpressionCommunicationRetour_Doc(BSession session) throws FWIException {
        super(session, CPApplication.APPLICATION_PHENIX_REP, "COMMUNICATION_FISCALE");
        super.setFileTitle(getSession().getLabel("CP_MSG_0192"));
    }

    @Override
    protected void _executeCleanUp() {
        if ((getTransaction() != null) && (getTransaction().isOpened())) {
            try {
                getTransaction().closeTransaction();
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        // Permet l'affichage des données du processus
        setState(Constante.FWPROCESS_MGS_220);
        super._executeCleanUp();
    }

    @Override
    protected void _validate() throws java.lang.Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        // Les erreurs sont ajoutées à la session,
        // abort permet l'arrêt du process
        if (!JadeStringUtil.isEmpty(getMessage())) {
            abort();
        }
    }

    @Override
    public void afterExecuteReport() {
        try {
            if (nbCommunication > 0) {
                getMemoryLog().logMessage(
                        "\n" + getSession().getLabel("CP_MSG_0149") + " " + nbCommunication + " "
                                + getSession().getLabel("CP_MSG_0179"), globaz.framework.util.FWMessage.INFORMATION,
                        this.getClass().getName());
            } else {
                getMemoryLog().logMessage(getSession().getLabel("CP_MSG_0151"),
                        globaz.framework.util.FWMessage.INFORMATION, this.getClass().getName());
            }
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            // on remplace les fichiers sauf si on fait un envoit ged auqeul cas
            // on a besoin des fichiers unitaire.
            boolean isMiseEnGed = CPApplication.isComFisEnvoiGed();
            this.mergePDF(docInfo, !isMiseEnGed, 500, true, null, null);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        super.afterExecuteReport();
    }

    @Override
    public void beforeBuildReport() {
        try {
            // Définition des champs pour le retour du fisc
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_CANTON, communication.getCodeCanton());

            super.setParametres(CPIListeCommunicationRetourParam.PARAM_GROUPE_EXTRACTION,
                    getSession().getLabel("CODE_EXTRACTION") + " " + entity.getGroupeExtraction());
            super.setParametres(
                    CPIListeCommunicationRetourParam.PARAM_GROUPE_TAXATION,
                    getSession().getLabel("CODE_TAXATION") + " "
                            + CodeSystem.getCodeUtilisateur(getSession(), entity.getGroupeTaxation()));
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_DESCRIPTION, communication.getDescription(0));
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL,
                    CodeSystem.getLibelle(getSession(), communication.getEtatCivil()));
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE,
                    CodeSystem.getLibelle(getSession(), communication.getCodeSexe()));
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS,
                    NSUtil.formatAVSUnknown(communication.getNumAvsFisc(1)));
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_GENRE_AFFILIE,
                    CodeSystem.getLibelle(getSession(), communication.getGenreAffilie()));
            // Définition des champs pour l'affiliation
            if (!JadeStringUtil.isIntegerEmpty(communication.getIdAffiliation())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, entity.getNumAffilie());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DEBUT_AFF, entity.getDebutAffiliationTiers()
                        + "-" + entity.getFinAffiliationTiers());
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DEBUT_AFF, " ");

            }
            if (!JadeStringUtil.isIntegerEmpty(communication.getIdTiers())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_AFF, entity.getDateNaissance());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_AFF, entity.getNumAVSActuel());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF,
                        tiersFiscale.getAdresseAsString());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_AFF,
                        CodeSystem.getLibelle(getSession(), entity.getEtatCivil()));
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_AFF,
                        CodeSystem.getLibelle(getSession(), entity.getSexe()));
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_AFF, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_AFF, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_AFF, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_AFF, " ");
            }
            // Définition des champs pour le conjoint
            if (!JadeStringUtil.isIntegerEmpty(communication.getIdAffiliationConjoint())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON,
                        entity.getNumAffiliationConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_CON,
                        entity.getDebutAffiliationConjoint() + "-" + entity.getFinAffiliationConjoint());
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_CON, " ");
            }
            if (!JadeStringUtil.isIntegerEmpty(communication.getIdConjoint())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON,
                        entity.getDateNaissanceConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON, entity.getNumAVSConjoint());
                if (tiersConjoint.getAdresseAsString().length() > 0) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON,
                            tiersConjoint.getAdresseAsString());
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON,
                            tiersConjoint.getDesignation2() + " " + tiersConjoint.getDesignation1());
                }
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON,
                        CodeSystem.getLibelle(getSession(), entity.getEtatCivilConjoint()));
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON,
                        CodeSystem.getLibelle(getSession(), entity.getSexeConjoint()));
            } else if ((sedexConjoint != null) && !sedexConjoint.isNew()) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON,
                        CPToolBox.formatDate(sedexConjoint.getYearMonthDay(), 2));
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON,
                        NSUtil.formatAVSUnknown(sedexConjoint.getVn()));
                String adresse = sedexConjoint.getFirstName() + " " + sedexConjoint.getOfficialName() + "\n";
                if (!JadeStringUtil.isBlank(sedexConjoint.getStreet())
                        || !JadeStringUtil.isBlank(sedexConjoint.getHouseNumber())) {
                    adresse = adresse + sedexConjoint.getStreet() + sedexConjoint.getHouseNumber() + "\n";
                }
                if (!JadeStringUtil.isBlank(sedexConjoint.getCountry())) {
                    adresse = adresse + sedexConjoint.getCountry();
                }
                if (!JadeStringUtil.isBlank(sedexConjoint.getTown())) {
                    adresse = adresse + sedexConjoint.getSwissZipCode() + " " + sedexConjoint.getTown() + "\n";
                }
                super.setParametres("P_DESC_CONJOINT", adresse);
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, adresse);
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON,
                        CPToolBox.getLibEtatCivil(getSession(), sedexConjoint.getMaritalStatus()));
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON,
                        CPToolBox.getLibSexe(getSession(), sedexConjoint.getSex()));
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON, " ");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON, " ");
            }
            // Zone des revenus
            // if (entity.isNonActif()) {
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_REVENU_RENTE_AVS,
                    communication.getMontantTotalRenteAVS());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_R, communication.getRevenuR());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_NA, communication.getRevenuNA());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_A, communication.getRevenuA());
            if ("SE".equalsIgnoreCase(communication.getJournalRetour().getCodeCanton())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENTREPRISE, JANumberFormatter.fmt(
                        JANumberFormatter.deQuote(communication.getCapitalEntreprise()), true, false, false, 0));
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENTREPRISE,
                        communication.getCapitalEntreprise());
            }
            // Si TSE on ne doit pas afficher le salaire, qui est le même que le revenu agricole
            if (CPDecision.CS_TSE.equalsIgnoreCase(communication.getGenreAffilie())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_SALAIRE, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_SALAIRE, communication.getSalaire());
            }
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_AUTRE_REV, communication.getAutreRevenu());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_RACHAT_LPP, communication.getRachatLpp());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_RACHAT_LPP_CJT, communication.getRachatLppCjt());
            // Zone des revenus du conjoint
            if (existeConjoint()) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_NA_CON,
                        communication.getRevenuNAConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_A_CON,
                        communication.getRevenuAConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENTREPRISE_CON,
                        communication.getCapitalEntrepriseConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_SALAIRE_CON,
                        communication.getSalaireConjoint());
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_AUTRE_REV_CON,
                        communication.getAutreRevenuConjoint());
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_NA_CON, "");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_REV_A_CON, "");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENTREPRISE_CON, "");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_SALAIRE_CON, "");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_AUTRE_REV_CON, "");
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RACHAT_LPP_CJT, "");
            }
            // Zone des revenus communs
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_FORTUNE_PRIVEE, communication.getFortune());
            // Tableau provisoire (selon communication)
            String varMontant = "";
            String varMontantEnCours = "";
            CPDonneesCalcul donCalcul = new CPDonneesCalcul();
            donCalcul.setSession(getSession());
            String periode = "";
            periode = JACalendar.getMonth(entity.getDebutDecision()) + "."
                    + JACalendar.getYear(entity.getDebutDecision());
            periode = periode + " - " + JACalendar.getMonth(entity.getFinDecision()) + "."
                    + JACalendar.getYear(entity.getFinDecision());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE, periode);

            // Additionner les cotis si revenu <> 0 ou si revenu autre = 0
            String rev1 = "";
            String revAutre1 = "";
            /**
             * S150921_001
             * if (!JadeStringUtil.isBlankOrZero(entity.getCotisation1())) {
             * if (!JadeStringUtil.isBlankOrZero(entity.getRevenu1())
             * || JadeStringUtil.isBlankOrZero(entity.getRevenuAutre1())) {
             * FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getRevenu1()));
             * curr.add(JANumberFormatter.deQuote(entity.getCotisation1()));
             * rev1 = curr.toString();
             * revAutre1 = entity.getRevenuAutre1();
             * } else {
             * rev1 = entity.getRevenu1();
             * FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getRevenuAutre1()));
             * curr.add(JANumberFormatter.deQuote(entity.getCotisation1()));
             * revAutre1 = curr.toString();
             * }
             * } else {
             * rev1 = entity.getRevenu1();
             * revAutre1 = entity.getRevenuAutre1();
             * }
             */
            rev1 = entity.getRevenu1();
            revAutre1 = entity.getRevenuAutre1();

            if (entity.isNonActif(entity.getIdTiersDecision())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA, "0");
                if (JadeStringUtil.isBlankOrZero(rev1)) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR, "0");
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR,
                            JANumberFormatter.fmt(rev1, true, false, true, 0));
                }
                varMontant = donCalcul.getMontant(entity.getIdDecision(), CPDonneesCalcul.CS_FORTUNE_TOTALE);
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR, "0");
                if (JadeStringUtil.isBlankOrZero(rev1)) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA, "0");
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA,
                            JANumberFormatter.fmt(rev1, true, false, true, 0));
                }
                varMontant = donCalcul.getMontant(entity.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
            }
            if (JadeStringUtil.isBlankOrZero(revAutre1)) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RA, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RA,
                        JANumberFormatter.fmt(revAutre1, true, false, true, 0));
            }
            if (JadeStringUtil.isBlankOrZero(entity.getCotisation1())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_COTISATION, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_COTISATION,
                        JANumberFormatter.fmt(entity.getCotisation1(), true, false, true, 2));
            }
            if (JadeStringUtil.isBlankOrZero(entity.getCapital())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL,
                        JANumberFormatter.fmt(entity.getCapital(), true, false, true, 0));
            }
            if (JadeStringUtil.isBlankOrZero(entity.getFortuneTotale())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_FORTUNE, "0");
            } else {
                String varInter = JANumberFormatter.deQuote(entity.getFortuneTotale());
                if (!JadeStringUtil.isIntegerEmpty(varInter)) {
                    if (entity.getDiv2().booleanValue() || !JadeStringUtil.isBlankOrZero(entity.getIdCjt())) {
                        varInter = new Float(2 * new Float(varInter).floatValue()).toString();
                    }
                }
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_FORTUNE,
                        JANumberFormatter.fmt(varInter, true, false, true, 0));
            }
            if (JadeStringUtil.isBlankOrZero(varMontant)) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_MONTANT_DETERMINANT, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_MONTANT_DETERMINANT, varMontant);
            }
            // Tableau dernière décision concernée pour la période
            String periodeEnCours = "";
            periodeEnCours = JACalendar.getMonth(entity.getDebutDecisionEnCours()) + "."
                    + JACalendar.getYear(entity.getDebutDecisionEnCours());
            periodeEnCours = periodeEnCours + " - " + JACalendar.getMonth(entity.getFinDecisionEnCours()) + "."
                    + JACalendar.getYear(entity.getFinDecisionEnCours());
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_ENCOURS, periodeEnCours);

            String rev1EnCours = "";
            String revAutre1EnCours = "";
            /**
             * if (!JadeStringUtil.isBlankOrZero(entity.getCotisation1EnCours())) {
             * if (!JadeStringUtil.isBlankOrZero(entity.getRevenu1EnCours())
             * || JadeStringUtil.isBlankOrZero(entity.getRevenuAutre1EnCours())) {
             * FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getRevenu1EnCours()));
             * curr.add(JANumberFormatter.deQuote(entity.getCotisation1EnCours()));
             * rev1EnCours = curr.toString();
             * revAutre1EnCours = entity.getRevenuAutre1EnCours();
             * } else {
             * rev1EnCours = entity.getRevenu1EnCours();
             * FWCurrency curr = new FWCurrency(JANumberFormatter.deQuote(entity.getRevenuAutre1EnCours()));
             * curr.add(JANumberFormatter.deQuote(entity.getCotisation1EnCours()));
             * revAutre1EnCours = curr.toString();
             * }
             * } else {
             * 
             * rev1EnCours = entity.getRevenu1EnCours();
             * revAutre1EnCours = entity.getRevenuAutre1EnCours();
             * }
             */
            rev1EnCours = entity.getRevenu1EnCours();
            revAutre1EnCours = entity.getRevenuAutre1EnCours();
            if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(entity.getGenreAffilieEnCours())
                    || CPDecision.CS_ETUDIANT.equalsIgnoreCase(entity.getGenreAffilieEnCours())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA_ENCOURS, "0");
                if (JadeStringUtil.isBlankOrZero(rev1EnCours)) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR_ENCOURS, "0");
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR_ENCOURS,
                            JANumberFormatter.fmt(rev1EnCours, true, false, true, 0));
                }
                varMontantEnCours = donCalcul.getMontant(entity.getIdDecisionProvisoire(),
                        CPDonneesCalcul.CS_FORTUNE_TOTALE);
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RR_ENCOURS, "0");
                if (JadeStringUtil.isBlankOrZero(rev1EnCours)) {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA_ENCOURS, "0");
                } else {
                    super.setParametres(CPIListeCommunicationRetourParam.PARAM_RNA_ENCOURS,
                            JANumberFormatter.fmt(rev1EnCours, true, false, true, 0));
                }
                varMontantEnCours = donCalcul.getMontant(entity.getIdDecisionProvisoire(), CPDonneesCalcul.CS_REV_NET);
            }
            if (JadeStringUtil.isBlankOrZero(revAutre1EnCours)) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RA_ENCOURS, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_RA_ENCOURS,
                        JANumberFormatter.fmt(revAutre1EnCours, true, false, true, 0));
            }

            if (JadeStringUtil.isBlankOrZero(entity.getCotisation1EnCours())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_COTISATION_ENCOURS, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_COTISATION_ENCOURS,
                        JANumberFormatter.fmt(entity.getCotisation1EnCours(), true, false, true, 2));
            }

            if (JadeStringUtil.isBlankOrZero(entity.getCapitalEnCours())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENCOURS, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_CAPITAL_ENCOURS,
                        JANumberFormatter.fmt(entity.getCapitalEnCours(), true, false, true, 0));
            }
            if (JadeStringUtil.isBlankOrZero(entity.getFortuneTotaleEnCours())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_FORTUNE_ENCOURS, "0");
            } else {
                String varInter = JANumberFormatter.deQuote(entity.getFortuneTotaleEnCours());
                if (!JadeStringUtil.isIntegerEmpty(varInter)) {
                    if (entity.getDiv2EnCours().booleanValue()
                            || !JadeStringUtil.isBlankOrZero(entity.getIdCjtEnCours())) {
                        varInter = new Float(2 * new Float(varInter).floatValue()).toString();
                    }
                }
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_FORTUNE_ENCOURS,
                        JANumberFormatter.fmt(varInter, true, false, true, 0));
            }
            if (JadeStringUtil.isBlankOrZero(varMontantEnCours)) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_MONTANT_DETERMINANT_ENCOURS, "0");
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_MONTANT_DETERMINANT_ENCOURS,
                        varMontantEnCours);
            }
            // Difference
            diffrenceEntreDecision(varMontant, varMontantEnCours, periode, periodeEnCours);
            // ENTETE
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_IFD, "IFD");
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_D, "D");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ANNEE, entity.getAnnee1());
            // Mettre la date du jour pour les cas ou la décision n'a pas été générée sinon mettre la date de calcul
            if (JadeStringUtil.isEmpty(entity.getDateCalcul())) {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE, JACalendar.todayJJsMMsAAAA());
            } else {
                super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE, entity.getDateCalcul());
            }
            // Remarque
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_REMARQUE, communication.getRemarque());
            // LOGS
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_LOGS, logs);

            CPApplication phenixApplication = (CPApplication) getSession().getApplication();

            setDocumentInfo();

            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(),
                    phenixApplication.getLangue2ISO(phenixApplication.getLangueCantonISO(canton)));
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            headerBean.setNoAffilie(null);
            headerBean.setNoAvs(" ");
            headerBean.setAdresse(" ");
            headerBean.setDate(" ");
            headerBean.setRecommandee(false);
            headerBean.setConfidentiel(false);

            caisseReportHelper.addSignatureParameters(this, "");

            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
            caisseReportHelper.addHeaderParameters(this, headerBean);
        } catch (Exception e) {
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_DESCRIPTION, " ");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL, " ");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE, " ");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS, " ");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_CONTRIBUABLE, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ANNEE_NAISSANCE, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_GENRE_AFFILIE, " ");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_DEBUT_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_AFF, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AFF_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_PERIODE_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_DATE_N_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_NUM_AVS_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ADRESSE_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_ETAT_CIVIL_CON, "");
            super.setParametres(CPIListeCommunicationRetourParam.PARAM_CODE_SEXE_CON, "");
        }
    }

    /**
     * Récupère les informations du décompte avant impression.
     */
    @Override
    public void beforeExecuteReport() {
        // Variable pour le comptage
        try {
            super.setTailleLot(0);
            super.setImpressionParLot(true);
            // On charge le journal
            journalRetour = new CPJournalRetour();
            journalRetour.setSession(getSession());
            journalRetour.setIdJournalRetour(getIdJournalRetour());
            journalRetour.retrieve();
            // On charge le canton
            canton = journalRetour.getCanton();
            // Traitement des communications suivant la selection
            manager = new CPCommunicationFiscaleRetourValidationManager();
            manager.setSession(getSession());
            manager.setForIdPlausibilite(getForIdPlausibilite());
            manager.setForIdJournalRetour(getIdJournalRetour());
            manager.setFromNumAffilieOrConjoint(getFromNumAffilie());
            manager.setTillNumAffilieOrConjoint(getTillNumAffilie());
            manager.setForIdRetour(getIdRetour());
            manager.setForIdPassage(getForIdPassage());
            manager.setWhitPavsAffilie(true);
            manager.setWhitPersAffilie(true);
            manager.setWhitAffiliation(true);
            manager.setWhitPavsConjoint(true);
            manager.setWhitPersConjoint(true);
            manager.setWhitAffiliationConjoint(true);
            manager.setForStatus(getForStatus());
            manager.setForImpression(Boolean.TRUE);
            manager.setForWantMiseEnGed(getWantMiseEnGEd());

            // Détermine le tri voulu
            determineOrderBy();

            // ---------------------
            // Chargement des labels
            // ---------------------
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_CONTRIBUABLE,
                    getSession().getLabel("CP_CONTRIBUABLE"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_AFFILIE,
                    getSession().getLabel("DETAIL_FISC_AFFILIE"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_ETAT_CIVIL,
                    getSession().getLabel("MARITALSTATUS"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_CODE_SEXE, getSession().getLabel("SEX"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_NUM_AVS, getSession().getLabel("NUM_AVS"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_CONJOINT,
                    getSession().getLabel("DETAIL_FISC_CONJOINT"));
            String libelleRevenu = "";
            String libelleRevenuAutre = "";
            String libelleRevenuRente = getSession().getLabel("DETAIL_FISC_RENTE");
            if (((CPApplication) getSession().getApplication()).isRevenuAgricole()) {
                libelleRevenu = getSession().getLabel("DETAIL_FISC_VS_RNA");
                libelleRevenuAutre = getSession().getLabel("REVENU_AGRICOLE");
            } else {
                libelleRevenu = getSession().getLabel("REVENU_ANNUEL");
                libelleRevenuAutre = getSession().getLabel("REVENU_AUTRE");
            }
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_REVENU_RENTE_AVS,
                    getSession().getLabel("DOCUMENT_0133CCP_REVENU_AVS"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_REV_NA, libelleRevenu);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_REV_A, libelleRevenuAutre);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_REV_R, libelleRevenuRente);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_COTISATION, getSession().getLabel("COTISATION"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_CAPITAL_ENTREPRISE,
                    getSession().getLabel("DETAIL_FISC_VS_CEE"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_FORTUNE_PRIVEE,
                    getSession().getLabel("DETAIL_FISC_VS_FP"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_SALAIRE,
                    getSession().getLabel("DETAIL_FISC_VS_S"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_AUTRE_REV,
                    getSession().getLabel("DETAIL_FISC_AUTRE_REV"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_RACHAT_LPP,
                    getSession().getLabel("DETAIL_FISC_RACHAT_LPP"));
            // Labels du tableau
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_PROVISOIRE,
                    getSession().getLabel("COMMUNICATIONS_FISCALES"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_ENCOURS, getSession().getLabel("CP_DECISION"));
            // super.setParametres(CPIListeCommunicationRetourParam.LABEL_COMMUNICATION,
            // "Communication");
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_PERIODE, getSession().getLabel("CP_PERIODE"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_RNA, libelleRevenu);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_RA, libelleRevenuAutre);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_RR, libelleRevenuRente);
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_CAPITAL, getSession().getLabel("CAPITAL"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_MONTANT_DETERMINANT,
                    getSession().getLabel("FORTUNE_DET"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_INFORMATION, getSession().getLabel("CP_INFO"));
            // Labels des codes
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_GENRE_TAXATION,
                    getSession().getLabel("DETAIL_FISC_GENRE_TAX"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_GTAXATION1,
                    getSession().getLabel("DETAIL_FISC_CAS1"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_GTAXATION2,
                    getSession().getLabel("DETAIL_FISC_CAS2"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_GTAXATION3,
                    getSession().getLabel("DETAIL_FISC_CAS3"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_GTAXATION4,
                    getSession().getLabel("DETAIL_FISC_CAS4"));
            super.setParametres(CPIListeCommunicationRetourParam.LABEL_REMARQUE,
                    getSession().getLabel("DETAIL_FISC_REMARQUE"));
            nbCommunication = manager.getCount(getTransaction());
            // nombre de communication fiscale à traiter
            // Entrer les informations pour l' état du process
            setState(getSession().getLabel("OBJEMAIL_FAPRINT_IMPRESSSIONCOMMUNICATION"));
            if (nbCommunication > 0) {
                setProgressScaleValue(nbCommunication);
            } else {
                setProgressScaleValue(1);
            }
            statement = manager.cursorOpen(getTransaction());

        } catch (Exception e) {
            this._addError("false");
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            try {
                getTransaction().rollback();
            } catch (Exception f) {
                getMemoryLog().logMessage(f.getMessage(), FWMessage.FATAL, this.getClass().getName());

            } finally {
                try {
                    if (statement != null) {
                        manager.cursorClose(statement);
                    }
                } catch (Exception g) {
                    getMemoryLog().logMessage(g.getMessage(), FWMessage.FATAL, this.getClass().getName());
                }
            }
        }
    }

    private void determineOrderBy() {
        if ("ORDER_BY_CONTRIBUABLE".equals(getTri())) {
            manager.orderByNumContribuable();
            manager.orderByNumContribuableCjt();
            manager.orderByNumIFD();
        } else if ("ORDER_BY_GTAXATION".equals(getTri())) {
            manager.orderByTaxation();
            manager.orderByNumIFD();
        } else if ("ORDER_BY_AFFILIE".equals(getTri())) {
            manager.orderByNumAffilie();
            manager.orderByNumAffilieCjt();
            manager.orderByNumIFD();
        } else if ("ORDER_BY_IFD".equals(getTri())) {
            manager.orderByNumIFD();
            manager.orderByNumContribuable();
            manager.orderByNumContribuableCjt();
        } else if ("ORDER_BY_AVS".equals(getTri())) {
            manager.orderByNumAvs();
            manager.orderByNumAvsCjt();
            manager.orderByNumIFD();
        } else { // Défaut
            manager.orderByNumContribuable();
            manager.orderByNumContribuableCjt();
            manager.orderByNumIFD();
        }
    }

    @Override
    public boolean beforePrintDocument() {
        if ((size() == 0) || isAborted()) {

            // Permet l'affichage des données du processus
            setState(Constante.FWPROCESS_MGS_220);
            return false;
        } else { // On met la liste de document dans l'ordre
            super.DocumentSort();
            return true;
        }
    }

    @Override
    public void createDataSource() {
        try {

            super.setTemplateFile("PHENIX_VALIDATION_RETOUR_FISC");

            if ("ORDER_BY_CONTRIBUABLE".equalsIgnoreCase(getTri())) {
                setDocumentTitle(communication.getDescription(1) + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_AFFILIE".equalsIgnoreCase(getTri()) || "ORDER_BY_GTAXATION".equalsIgnoreCase(getTri())) {
                setDocumentTitle(communication.getDescription(2) + " - " + entity.getAnnee1());
            } else if ("ORDER_BY_IFD".equalsIgnoreCase(getTri())) {
                setDocumentTitle(entity.getAnnee1() + " - " + communication.getDescription(1));
            } else if ("ORDER_BY_AVS".equalsIgnoreCase(getTri())) {
                setDocumentTitle(communication.getDescription(2) + " - " + entity.getAnnee1());
            } else { // Défaut
                setDocumentTitle(communication.getDescription(1) + " - " + entity.getAnnee1());
            }
            getDocumentInfo().setDocumentTypeNumber("0133CCP");
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return;
        }
        return;
    }

    private void diffrenceEntreDecision(String varMontant, String varMontantEnCours, String periode,
            String periodeEnCours) {
        if (!periode.equalsIgnoreCase(periodeEnCours)) {
            super.setParametres("P_DIF_PERIODE", "X");
        } else {
            super.setParametres("P_DIF_PERIODE", " ");
        }
        super.setParametres("P_DIF_RNA", " ");
        super.setParametres("P_DIF_RR", " ");
        if (JadeStringUtil.isIntegerEmpty(entity.getRevenu1())
                && JadeStringUtil.isIntegerEmpty(entity.getRevenu1EnCours())) {
            super.setParametres("P_DIF_RNA", " ");
            super.setParametres("P_DIF_RR", " ");
        } else if (!entity.getRevenu1().equalsIgnoreCase(entity.getRevenu1EnCours())) {
            if (entity.isNonActif()) {
                super.setParametres("P_DIF_RR", "X");
            } else {
                super.setParametres("P_DIF_RNA", "X");
            }
        }
        if (JadeStringUtil.isIntegerEmpty(entity.getRevenuAutre1())
                && JadeStringUtil.isIntegerEmpty(entity.getRevenuAutre1EnCours())) {
            super.setParametres("P_DIF_RA", " ");
        } else if (!entity.getRevenuAutre1().equalsIgnoreCase(entity.getRevenuAutre1EnCours())) {
            super.setParametres("P_DIF_RA", "X");
        } else {
            super.setParametres("P_DIF_RA", " ");
        }

        if (JadeStringUtil.isIntegerEmpty(entity.getCotisation1())
                && JadeStringUtil.isIntegerEmpty(entity.getCotisation1EnCours())) {
            super.setParametres("P_DIF_COTISATION", " ");
        } else if (!entity.getCotisation1().equalsIgnoreCase(entity.getCotisation1EnCours())) {
            super.setParametres("P_DIF_COTISATION", "X");
        }

        if (JadeStringUtil.isIntegerEmpty(entity.getCapital())
                && JadeStringUtil.isIntegerEmpty(entity.getCapitalEnCours())) {
            super.setParametres("P_DIF_CAPITAL", " ");
        } else if (!entity.getCapital().equalsIgnoreCase(entity.getCapitalEnCours())) {
            super.setParametres("P_DIF_CAPITAL", "X");
        } else {
            super.setParametres("P_DIF_CAPITAL", " ");
        }
        if (JadeStringUtil.isIntegerEmpty(entity.getFortuneTotale())
                && JadeStringUtil.isIntegerEmpty(entity.getFortuneTotaleEnCours())) {
            super.setParametres("P_DIF_FORTUNE", " ");
        } else if (!entity.getFortuneTotale().equalsIgnoreCase(entity.getFortuneTotaleEnCours())) {
            super.setParametres("P_DIF_FORTUNE", "X");
        } else {
            super.setParametres("P_DIF_FORTUNE", " ");
        }
        if (JadeStringUtil.isIntegerEmpty(varMontant) && JadeStringUtil.isIntegerEmpty(varMontantEnCours)) {
            super.setParametres("P_DIF_MDETERMINANT", " ");
        } else if (!varMontant.equalsIgnoreCase(varMontantEnCours)) {
            super.setParametres("P_DIF_MDETERMINANT", "X");
        } else {
            super.setParametres("P_DIF_MDETERMINANT", " ");
        }
    }

    private boolean existeConjoint() {
        if ((communication != null)
                && (!JadeStringUtil.isBlankOrZero(communication.getRevenuAConjoint())
                        || !JadeStringUtil.isBlankOrZero(communication.getRevenuNAConjoint())
                        || !JadeStringUtil.isBlankOrZero(communication.getSalaireConjoint())
                        || !JadeStringUtil.isBlankOrZero(communication.getCapitalEntrepriseConjoint()) || !JadeStringUtil
                            .isBlankOrZero(communication.getAutreRevenuConjoint()))) {
            return true;
        }
        if (((sedexConjoint == null) || (JadeStringUtil.isBlankOrZero(sedexConjoint.getVn())
                && JadeStringUtil.isBlankOrZero(sedexConjoint.getOfficialName()) && JadeStringUtil
                    .isBlankOrZero(sedexConjoint.getFirstName())))) {
            return false;
        } else {
            return true;
        }
    }

    public CPCommunicationFiscaleRetourValidation getEntity() {
        return entity;
    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    public String getForStatus() {
        return forStatus;
    }

    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public CPCommunicationFiscaleRetourValidationManager getManager() {
        return manager;
    }

    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    public String getTraitementUnitaire() {
        return traitementUnitaire;
    }

    public String getTri() {
        return tri;
    }

    public Boolean getWantMiseEnGEd() {
        return wantMiseEnGEd;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        try {
            if (((entity = (CPCommunicationFiscaleRetourValidation) manager.cursorReadNext(statement)) != null)
                    && (!entity.isNew()) && !super.isAborted()) {
                // ---------------------------------------------------------------------
                setProgressCounter(progressCounter++);
                // ---------------------------------------------------------------------
                // construit le document pour la prochaine communication
                if (getWantMiseEnGEd().equals(Boolean.TRUE)) {
                    // Impression automatique via la facturation
                    communication = new CPCommunicationFiscaleRetourSEDEXViewBean();
                    communication.setWantDonneeBase(true);
                    communication.setWantDonneeContribuable(true);
                } else {
                    communication = journalRetour.determinationEntity();
                }
                communication.setSession(getSession());
                communication.setIdRetour(entity.getIdRetours());
                communication.retrieve();
                if (!JadeStringUtil.isIntegerEmpty(communication.getIdConjoint())) {
                    tiersConjoint = new TITiersViewBean();
                    tiersConjoint.setSession(getSession());
                    tiersConjoint.setIdTiers(entity.getIdConjoint());
                    tiersConjoint.retrieve();
                }
                if (!JadeStringUtil.isIntegerEmpty(communication.getIdTiers())) {
                    tiersFiscale = new TITiersViewBean();
                    tiersFiscale.setSession(getSession());
                    tiersFiscale.setIdTiers(entity.getIdTiers());
                    tiersFiscale.retrieve();
                }
                // extraction des données du conjoint envoyées par Sedex
                sedexConjoint = new CPSedexConjoint();
                sedexConjoint.setSession(getSession());
                sedexConjoint.setIdRetour(communication.getIdRetour());
                sedexConjoint.retrieve();

                // On en prend que 10
                int nbLogs = 10;
                logs = "";
                CPLienCommunicationsPlausiManager plausiManager = new CPLienCommunicationsPlausiManager();
                plausiManager.setSession(getSession());
                plausiManager.setForIdCommunication(communication.getIdRetour());
                plausiManager.find();
                for (int i = 0; i < plausiManager.size(); i++) {
                    if (nbLogs-- > 0) {
                        CPLienCommunicationsPlausi lien = (CPLienCommunicationsPlausi) plausiManager.get(i);
                        // On va rechercher le message
                        CPParametrePlausibilite plausi = new CPParametrePlausibilite();
                        plausi.setSession(getSession());
                        plausi.setIdParametre(lien.getIdPlausibilite());
                        try {
                            plausi.retrieve();
                        } catch (Exception e) {
                        }
                        if (plausi.getDescription_fr().length() > 0) {
                            if (getSession().getIdLangueISO().equalsIgnoreCase("DE")) {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_de();
                            } else if (getSession().getIdLangueISO().equalsIgnoreCase("IT")) {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_it();
                            } else {
                                logs = logs + plausi.getId() + " - " + plausi.getDescription_fr();
                            }
                        }
                        logs = logs + "\n";
                    }
                }
                return true;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(), e.toString());
            throw new FWIException(e);
        }
        return false;
    }

    public void setDocumentInfo() throws FWIException {
        try {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID,
                    ((CPApplication) getSession().getApplication()).getGedTypeDossier());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "");
        }
        try {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE,
                    ((CPApplication) getSession().getApplication()).getGedService());
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty(JadeGedTargetProperties.SERVICE, "");
        }

        getDocumentInfo().setDocumentTypeNumber("0133CCP");

        getDocumentInfo().setDocumentProperty(CADocumentInfoHelper.SECTION_ID_EXTERNE, entity.getAnnee1());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_GENRE, entity.getGenreAffilie());
        getDocumentInfo().setDocumentProperty(DocumentInfoPhenix.DECISION_NUMERO_PASSAGE, entity.getIdPassage());

        // Traitement des cas en erreur
        if (CPCommunicationFiscaleRetourViewBean.CS_ERREUR.equals(communication.getStatus())
                || CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE.equals(communication.getStatus())
                || CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equals(communication.getStatus())) {
            // Numéro de l'affilié
            try {
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                        CPToolBox.unFormat(entity.getNumAffilie()));
                getDocumentInfo()
                        .setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, entity.getNumAffilie());
            } catch (Exception e) {
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, "");
            }

            // Numéro du conjoint
            try {
                getDocumentInfo().setDocumentProperty("numero.role.conjoint.formatte",
                        entity.getNumAffiliationConjoint());
                getDocumentInfo().setDocumentProperty("numero.role.conjoint.non.formatte",
                        CPToolBox.unFormat(entity.getNumAffiliationConjoint()));
            } catch (Exception e) {
                getDocumentInfo().setDocumentProperty("numero.role.conjoint.non.formatte", "");
                getDocumentInfo().setDocumentProperty("numero.role.conjoint.formatte", "");
            }
        } else {
            // Traitement des cas qui ne sont pas en erreur
            try {
                if (entity.getIdTiers().equalsIgnoreCase(entity.getIdTiersDecision())
                        || JadeStringUtil.isEmpty(entity.getNumAffiliationConjoint())) {
                    getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                            CPToolBox.unFormat(entity.getNumAffilie()));
                    getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE,
                            entity.getNumAffilie());
                } else {
                    getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE,
                            CPToolBox.unFormat(entity.getNumAffiliationConjoint()));
                    getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE,
                            entity.getNumAffiliationConjoint());
                }
            } catch (Exception e) {
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_NON_FORMATTE, "");
                getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.NUMERO_ROLE_FORMATTE, "");
            }
        }

        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_ID, entity.getIdTiers());
        String langueIso = "";
        TITiersViewBean tierslu = entity.getTiers();
        if (tierslu != null) {
            langueIso = tierslu.getLangueIso();
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_LANGUE_ISO, langueIso);
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NOM, entity.getNom());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_PRENOM, entity.getPrenom());
        /* Personne Avs */
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_DATE_NAISSANCE, entity.getDateNaissance());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE, entity.getNumAvs(0));
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(entity.getNumAvs(0), '.'));

        getDocumentInfo().setDocumentProperty("traitement.unitaire", getTraitementUnitaire());

        getDocumentInfo().setPublishDocument(false);
        getDocumentInfo().setArchiveDocument(true);
    }

    public void setEntity(CPCommunicationFiscaleRetourValidation entity) {
        this.entity = entity;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    public void setFromNumAffilie(java.lang.String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    public void setForIdPassage(String forIdjournalFacturation) {
        forIdPassage = forIdjournalFacturation;
    }

    public void setIdJournalRetour(String idJournalRetour) {
        this.idJournalRetour = idJournalRetour;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setManager(CPCommunicationFiscaleRetourValidationManager manager) {
        this.manager = manager;
    }

    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

    public void setTri(String tri) {
        this.tri = tri;
    }

    public void setTraitementUnitaire(String traitementUnitaire) {
        this.traitementUnitaire = traitementUnitaire;
    }

    public void setWantMiseEnGEd(Boolean wantMiseEnGEd) {
        this.wantMiseEnGEd = wantMiseEnGEd;
    }

}
