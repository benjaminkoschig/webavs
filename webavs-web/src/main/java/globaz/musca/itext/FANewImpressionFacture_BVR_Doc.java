package globaz.musca.itext;

import globaz.aquila.print.COParameter;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.FADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.itext.newimpbvrutil.FANewImpFactDataSource;
import globaz.musca.itext.newimpbvrutil.FANewImpFactDataSourceProviderFactory;
import globaz.musca.itext.newimpbvrutil.FANewImpFactPropertiesProvider;
import globaz.musca.itext.newimpbvrutil.IFANewImpFactDataSourceProvider;
import globaz.musca.process.FANewImpressionFactureProcess;
import globaz.musca.process.FANewImpressionFactureProcessSansLSVRemb;
import globaz.musca.util.FAUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.utils.CAReferenceBVR;
import globaz.osiris.db.utils.CAReferenceQR;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import globaz.webavs.common.CommonProperties;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Le document imprime les zones de facture selon les paramètres suivants: _modeRecouvrement : aucun, bvr,
 * remboursement, recouvrement direct _critereDecompte : interne, positif, note de credit, decompte zéro Date de
 * création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class FANewImpressionFacture_BVR_Doc extends FAImpressionFacturation {

    private static final long serialVersionUID = -6113464139626514078L;
    private final static String CODEDECOMPTESALAIRE13 = "13";
    private final static String CODEDECOMPTESALAIRE14 = "14";
    private final static String CODEDECOMPTESALAIRE30 = "30";
    private final static String CODEDECOMPTESALAIRE33 = "33";
    private final static String HEADER_FILENAME = "header.filename.bvr";
    public final static String NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE = "0099CFA"; // Utilisé par défaut
    public final static String NUM_INFOROM_FACTURE_DECOMPTE_PERSONELLE = "0290CFA";
    public final static String NUM_INFOROM_FACTURE_PERIODIQUE_PARITAIRE = "0288CFA";
    public final static String NUM_INFOROM_FACTURE_PERIODIQUE_PERSONELLE = "0289CFA";
    public final static String TEMPLATE_FILENAME = "NEW_MUSCA_BVR_1";
    public final static String TEMPLATE_FILENAME_BVR_NEUTRE = "MUSCA_BVR_NEUTRE_QR";
    public final static String TEMPLATE_FILENAME4DECSAL = "NEW_MUSCA_BVR4DECSAL"; // Template

    public static String getTemplateFilename(FAEnteteFacture entFacture) {
        if (FANewImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE13.equalsIgnoreCase(entFacture.getIdExterneFacture()
                .substring(4, 6))
                || FANewImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE14.equalsIgnoreCase(entFacture
                        .getIdExterneFacture().substring(4, 6))
                || FANewImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE33.equalsIgnoreCase(entFacture
                        .getIdExterneFacture().substring(4, 6))
                || FANewImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE30.equalsIgnoreCase(entFacture
                        .getIdExterneFacture().substring(4, 6))) {
            return FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL; // Template pour déclaration de
            // salaire
        } else if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(entFacture.getIdTypeFacture())) {
            return FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME_BVR_NEUTRE; // Template pour bulletin
            // neutre
        } else {
            return FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME;
        }
    }

    private FAApplication application = null;
    private long buildReportStartTime = 0;

    private int countFactures = 0;
    private ArrayList enteteFactures = null;
    private Boolean envoyerGed = new Boolean(false);
    // Va contenir les données à utiliser dans le cadre de la génération des
    // documents
    private IFANewImpFactDataSourceProvider impressionFactureDataSourceProvider = null;
    private boolean imprimable = false;
    private int index = 1;
    private String montantMinimeMax;
    // recherche les valeurs de MUSCA.properties
    private String montantMinimeNeg;

    private String montantMinimePos;
    private long printDocumentStartTime = 0;
    private String rolePersonelle = "";
    private long startTime;
    private String textMontant = "";
    private long totalBuildReport = 0;
    private long totalDataSourceLoadingTime = 0;
    private long totalPrintDocumentTime = 0;
    private long totalTimeDataSource = 0;
    private boolean unificationProcess = false;

    JadeUser user = null;

    private boolean wantReferenceFacture = false;

    public FANewImpressionFacture_BVR_Doc() throws Exception {
        this(new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public FANewImpressionFacture_BVR_Doc(BSession session) throws java.lang.Exception {
        super(session, FAApplication.APPLICATION_MUSCA_REP, "CaisseDeCompensation");
        setDocumentTitle(getSession().getLabel("TITRE_DOC_FACTURE"));
        init(session);
    }

    public FANewImpressionFacture_BVR_Doc(FANewImpressionFactureProcess parent) throws java.lang.Exception {
        super(parent, FAApplication.APPLICATION_MUSCA_REP, parent.getFactureType());
        super.setDocumentTitle(parent.getFactureType());
        setSendMailOnError(false); // le processus n'enverra jamais de mail et
        // son parent s'en charge
        setEnvoyerGed(parent.getEnvoyerGed());
        setImprimable(parent.isImprimable());
        setUnificationProcess(parent.isUnificationProcess());
        init(parent.getSession());
    }

    public FANewImpressionFacture_BVR_Doc(FANewImpressionFactureProcessSansLSVRemb parent) throws java.lang.Exception {
        super(parent, FAApplication.APPLICATION_MUSCA_REP, parent.getFactureType());
        super.setDocumentTitle(parent.getFactureType());
        setSendMailOnError(false); // le processus n'enverra jamais de mail et
        // son parent s'en charge
        setEnvoyerGed(parent.getEnvoyerGed());
        setImprimable(parent.isImprimable());
        setUnificationProcess(parent.isUnificationProcess());
        init(parent.getSession());
    }

    protected void _bvrText() {

        try {
            getBvr().setSession(getSession());
            if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(newCurrentDataSource.getEnteteFacture()
                    .getIdTypeFacture())) {
                getBvr().setBVRNeutre(newCurrentDataSource.getEnteteFacture(), isFactureAvecMontantMinime(),
                        reporterMontant);
                textMontant = getBvr().getTextBVRNeutre(getSession(),
                        newCurrentDataSource.getEnteteFacture().getISOLangueTiers());
                super.setParametres(FAImpressionFacture_Param.P_REMARQUE, textMontant);
            } else {
                getBvr().setBVR(newCurrentDataSource.getEnteteFacture(), isFactureAvecMontantMinime(), reporterMontant);
            }

            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");

            super.setParametres(FAImpressionFacture_Param.P_ADRESSE,
                    getBvr().getAdresse(newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(FAImpressionFacture_Param.P_ADRESSECOPY,
                    getBvr().getAdresse(newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(FAImpressionFacture_Param.P_COMPTE, getBvr().getNumeroCC());
            super.setParametres(FAImpressionFacture_Param.P_VERSE, getBvr().getLigneReference() + "\n"
                    + adresseDebiteur);
            super.setParametres(FAImpressionFacture_Param.P_PAR, adresseDebiteur);

            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(FAImpressionFacture_Param.P_OCR, getBvr().getOcrb());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr: "
                            + newCurrentDataSource.getEnteteFacture().getIdExterneRole() + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        // étirer le montant avec deux espace blanc entre 2 chiffres
        if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(newCurrentDataSource.getEnteteFacture().getIdTypeFacture())) {
            super.setParametres(FAImpressionFacture_Param.P_FRANC, " ");
            super.setParametres(FAImpressionFacture_Param.P_CENTIME, " ");
        } else if (!(tmpCurrency.isNegative() || tmpCurrency.isZero() || isFactureAvecMontantMinime() || (isFactureMontantReport() && modeReporterMontantMinime))) {
            if (newCurrentDataSource.getEnteteFacture().getIdModeRecouvrement()
                    .equals(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)) {
                super.setParametres(FAImpressionFacture_Param.P_FRANC, "XXXXXXXXXXXXXXX");
                super.setParametres(FAImpressionFacture_Param.P_CENTIME, "XX");
            } else {
                super.setParametres(FAImpressionFacture_Param.P_FRANC, montantSansCentime);
                super.setParametres(FAImpressionFacture_Param.P_CENTIME, centimes);
            }
        } else {
            super.setParametres(FAImpressionFacture_Param.P_FRANC, "XXXXXXXXXXXXXXX");
            super.setParametres(FAImpressionFacture_Param.P_CENTIME, "XX");
        }
    }

    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            String dateImpression2 = getDateImpression();

            // Si aucune date n'est renseingée, on prend la date de facturation
            if (JAUtil.isDateEmpty(dateImpression2)) {
                dateImpression2 = getPassage().getDateFacturation();
            }
            // texte de la date
            headerBean.setDate(JACalendar.format(dateImpression2, newCurrentDataSource.getEnteteFacture()
                    .getISOLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(newCurrentDataSource.getAdressePrincipale());
            if (wantReferenceFacture
                    && !JadeStringUtil.isEmpty(newCurrentDataSource.getEnteteFacture().getReferenceFacture())) {

                JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                JadeUser user = service.load(newCurrentDataSource.getEnteteFacture().getReferenceFacture());
                setJadeUser(user);
            }
            if (getJadeUser() != null) {
                headerBean.setNomCollaborateur(getJadeUser().getFirstname() + " " + getJadeUser().getLastname());
                headerBean.setTelCollaborateur(getJadeUser().getPhone());
                headerBean.setUser(getJadeUser());
            }

            // numéro AVS
            headerBean.setNoAvs("");
            if (!CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(newCurrentDataSource.getEnteteFacture().getIdRole())) {
                if (!"".equals(newCurrentDataSource.getEnteteFacture().getNumeroAVSTiers(getTransaction()))) {
                    headerBean.setNoAvs(newCurrentDataSource.getEnteteFacture().getNumeroAVSTiers(getTransaction()));
                }
            }

            // No affilié
            headerBean.setNoAffilie(newCurrentDataSource.getEnteteFacture().getIdExterneRole());
            headerBean.setConfidentiel(true);

            // description du décompte
            super.setParametres(FAImpressionFacturation_Param.getParamP(5), newCurrentDataSource.getEnteteFacture()
                    .getDescriptionDecompte(newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));

            // Numéro de page
            super.setParametres(FAImpressionFacture_Param.P_FACTURE_IMPNO, newCurrentDataSource.getEnteteFacture()
                    .getIdPassage() + "-" + new Integer(getFactureImpressionNo()));
            if (!newCurrentDataSource.getEnteteFacture().getLibelle().equals(null)) {
                super.setParametres(FAImpressionFacture_Param.P_LIBELLE, newCurrentDataSource.getEnteteFacture()
                        .getLibelle());
            }

            super.setParametres(
                    FAImpressionFacture_Param.P_TEXT2,
                    getSession().getApplication().getLabel("FACREPORT",
                            newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(
                    FAImpressionFacture_Param.P_TEXT1,
                    getSession().getApplication().getLabel("NEW_FACREPORTMONT",
                            newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Les paramêtres de l'objet peuvent ne pas avoir été mis correctement"
                            + newCurrentDataSource.getEnteteFacture().getIdExterneRole(), FWMessage.AVERTISSEMENT,
                    headerBean.getClass().getName());
        }
    }

    protected void _summaryText() {
        String dateEcheance = "";
        try {
            APISectionDescriptor sectionDescriptor = application.getSectionDescriptor(getSession());

            // initialiser le section descriptor avec les paramètres de l'entête
            // de facture
            sectionDescriptor.setSection(newCurrentDataSource.getEnteteFacture().getIdExterneFacture(), newCurrentDataSource
                    .getEnteteFacture().getIdTypeFacture(), newCurrentDataSource.getEnteteFacture().getIdSousType(),
                    passage.getDateFacturation(), "", "");

            // la date d'échéance est calculée par le sectionDescriptor
            dateEcheance = sectionDescriptor.getDateEcheanceFacturation();

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        try {

            // Texte pour les bulletins neutre
            if (!APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(newCurrentDataSource.getEnteteFacture()
                    .getIdTypeFacture())) {

                // /////////////////////////////////
                if ("-".equalsIgnoreCase(newCurrentDataSource.getEnteteFacture().getTotalFacture().substring(0, 1))) {

                    // cas pour montant minime négatif
                    if (isFactureAvecMontantMinime()) {
                        textMontant = application.getLabel("NEW_FACTEXT_MINIMENEG", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    }
                    // Cas négatif retenu manuellement
                    else if (FAEnteteFacture.CS_MODE_RETENU.equalsIgnoreCase(newCurrentDataSource.getEnteteFacture()
                            .getIdModeRecouvrement())) {
                        textMontant = application.getLabel("NEW_FACTEXT_RETENUNEG", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    }
                    // Cas négatif retenu pour cause de contentieux
                    else if (FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE.equalsIgnoreCase(newCurrentDataSource
                            .getEnteteFacture().getIdModeRecouvrement())) {
                        textMontant = application.getLabel("NEW_FACTEXT_CONTENTIEUX", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    } else {
                        // pas d'adresse de paiement
                        if (JadeStringUtil.isBlankOrZero(newCurrentDataSource.getEnteteFacture().getIdAdressePaiement())) {
                            textMontant = application.getLabel("NEW_FACTEXT_COMPTE_MANQUANT", newCurrentDataSource
                                    .getEnteteFacture().getISOLangueTiers());
                        } else {
                            textMontant = application.getLabel("NEW_FACTEXT_NCR", newCurrentDataSource.getEnteteFacture()
                                    .getISOLangueTiers())
                                    + "\n"
                                    + application.getLabel("FACTEXT_NCR2", newCurrentDataSource.getEnteteFacture()
                                            .getISOLangueTiers());
                        }
                    }

                }
                // Texte montant POSITIF
                // /////////////////////////////////

                // cas pour montant minime positif
                else {
                    if ((new FWCurrency(newCurrentDataSource.getEnteteFacture().getTotalFacture()).isZero())) {
                        textMontant = application.getLabel("NEW_FACTEXT_TOTAL_NOCOMMENT", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    } else if (isFactureAvecMontantMinime()) {
                        textMontant = application.getLabel("NEW_FACTEXT_MINIMEPOS", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    }

                    else if (isModeReporterMontantMinimal()
                            && !new FWCurrency(newCurrentDataSource.getEnteteFacture().getTotalFacture()).isZero()) // Dans
                    // le
                    // mode
                    // de
                    // repport
                    // du
                    // montant
                    {
                        if (isFactureMontantReport() && modeReporterMontantMinime) {
                            try {
                                // Si l'affilié a un compte annexe on lui dit
                                // que le
                                // montant est reporté
                                // if (_getCompteAnnexe().isNew() ||
                                // JadeStringUtil.isIntegerEmpty(_getCompteAnnexe().getRole().getDateFin()))
                                // {
                                textMontant = getSession().getApplication().getLabel("NEW_FACTEXT_MINIMEPOS_REPORTE",
                                        newCurrentDataSource.getEnteteFacture().getISOLangueTiers());
                                // }
                            } catch (Exception e1) {
                                getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                            }
                        } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equalsIgnoreCase(newCurrentDataSource
                                .getEnteteFacture().getIdModeRecouvrement())) {
                            textMontant = application.getLabel("NEW_FACTEXT_RECOUVREMENT_DIRECT", newCurrentDataSource
                                    .getEnteteFacture().getISOLangueTiers())
                                    + " " + dateEcheance;
                        } else {
                            if (newCurrentDataSource.getEnteteFacture().getIdSousType().equals("227030")) {
                                textMontant = application.getLabel("NEW_FACTEXT1_BIS", newCurrentDataSource
                                        .getEnteteFacture().getISOLangueTiers())
                                        + application.getLabel(dateEcheance, newCurrentDataSource.getEnteteFacture()
                                                .getISOLangueTiers());
                            } else {
                                textMontant = application.getLabel("NEW_FACTEXT_1", newCurrentDataSource
                                        .getEnteteFacture().getISOLangueTiers())
                                        + " "
                                        + dateEcheance
                                        + " "
                                        + application.getLabel("FACTEXT_2", newCurrentDataSource.getEnteteFacture()
                                                .getISOLangueTiers());
                                // en allemand, le texte de fin suit la date
                            }
                        }
                    } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equalsIgnoreCase(newCurrentDataSource
                            .getEnteteFacture().getIdModeRecouvrement())) {
                        textMontant = application.getLabel("NEW_FACTEXT_RECOUVREMENT_DIRECT", newCurrentDataSource
                                .getEnteteFacture().getISOLangueTiers())
                                + " " + dateEcheance;
                    } else if (!new FWCurrency(newCurrentDataSource.getEnteteFacture().getTotalFacture()).isZero()) {
                        if (newCurrentDataSource.getEnteteFacture().getIdSousType().equals("227030")) {
                            textMontant = application.getLabel("NEW_FACTEXT1_BIS", newCurrentDataSource.getEnteteFacture()
                                    .getISOLangueTiers())
                                    + application.getLabel(dateEcheance, newCurrentDataSource.getEnteteFacture()
                                            .getISOLangueTiers());
                        } else {
                            textMontant = application.getLabel("NEW_FACTEXT_1", newCurrentDataSource.getEnteteFacture()
                                    .getISOLangueTiers())
                                    + " "
                                    + dateEcheance
                                    + " "
                                    + application.getLabel("FACTEXT_2", newCurrentDataSource.getEnteteFacture()
                                            .getISOLangueTiers());
                            // en allemand, le texte de fin suit la date
                        }
                    }
                }

                super.setParametres(FAImpressionFacture_Param.P_REMARQUE, newCurrentDataSource.getEnteteFacture()
                        .getRemarque(getTransaction()));

                super.setParametres(FAImpressionFacture_Param.P_TEXT, textMontant);
            }

            super.setParametres(FAImpressionFacture_Param.P_ADR_CAISSE,
                    application.getLabel("ADR_CAISSE", newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    protected void _tableHeader() {
        String langueIso = newCurrentDataSource.getEnteteFacture().getISOLangueTiers();
        // setColumnHeader(1, application.getLabel("LOW_CASE_FACLIBELLE",
        // langueIso));
        setColumnHeader(1, "");
        setColumnHeader(2, application.getLabel("LOW_CASE_FACBASE", langueIso));
        setColumnHeader(3, application.getLabel("LOW_CASE_FACACOMPTE", langueIso));
        setColumnHeader(4, application.getLabel("LOW_CASE_FACSOLDE", langueIso));
        setColumnHeader(5, application.getLabel("LOW_CASE_FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("LOW_CASE_FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("LOW_CASE_FACPERIODE", langueIso));
        setColumnHeader(0, application.getLabel("LOW_CASE_FATOTAUX", langueIso));

    }

    protected void _tableHeader4DecSal() {
        String langueIso = newCurrentDataSource.getEnteteFacture().getISOLangueTiers();
        // setColumnHeader(1, application.getLabel("LOW_CASE_FACLIBELLE",
        // langueIso));
        setColumnHeader(1, "");
        setColumnHeader(2, application.getLabel("LOW_CASE_FACBASE", langueIso));
        setColumnHeader(3, application.getLabel("LOW_CASE_FACACOMPTE", langueIso));
        setColumnHeader(5, application.getLabel("LOW_CASE_FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("LOW_CASE_FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("LOW_CASE_FACEFFECTIF", langueIso));
        setColumnHeader(0, application.getLabel("LOW_CASE_FATOTAUX", langueIso));
    }

    protected void _tableHeaderBvrNeutre() {
        String langueIso = newCurrentDataSource.getEnteteFacture().getISOLangueTiers();
        // setColumnHeader(1, application.getLabel("LOW_CASE_FACLIBELLE",
        // langueIso));
        setColumnHeader(1, "");
        setColumnHeader(2, application.getLabel("LOW_CASE_FACMASSE", langueIso));
        setColumnHeader(5, application.getLabel("LOW_CASE_FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("LOW_CASE_FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("LOW_CASE_FACPERIODE", langueIso));
        setColumnHeader(0, application.getLabel("LOW_CASE_FATOTAUX", langueIso));
    }

    @Override
    public void afterBuildReport() {
        try {
            if (((BApplication) application).getProperty("gestionVerso").equals("avec")) {
                JasperPrint verso = null;
                verso = getVerso();
                if (verso != null) {
                    verso.setName(newCurrentDataSource.getEnteteFacture().getIdExterneRole() + " - " + index
                            + " - 2_VERSO");
                    index++;
                    super.getDocumentList().add(verso);
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant la lecture des properties l'impression du verso");
        }
        totalBuildReport += (System.currentTimeMillis() - buildReportStartTime);
    }

    @Override
    public void afterExecuteReport() {
        if (FANewImpFactPropertiesProvider.IS_DEBUG_MODE) {
            JadeLogger.info(this, "----> Temps total de l'exécution du reporting : "
                    + (System.currentTimeMillis() - startTime));
            JadeLogger.info(this, "----> Temps moyen d'exécution de la méthode <createDataSource> : "
                    + (totalTimeDataSource / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen d'exécution du build report : "
                    + (totalBuildReport / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen d'exécution du print document : "
                    + (totalPrintDocumentTime / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen du chargement des données (FAImpressionFactureDatasource) :"
                    + totalDataSourceLoadingTime / factureImpressionNo);
        }
    }

    @Override
    public void afterPrintDocument() {
        totalPrintDocumentTime += (System.currentTimeMillis() - printDocumentStartTime);
    }

    @Override
    public void beforeBuildReport() {
        try {
            super.setParameter(
                    "PARAM_NEW_MUSCA_BVR_DETAIL",
                    JadeStringUtil.change(getSession().getApplication().getExternalModelPath()
                            + FAApplication.APPLICATION_MUSCA_REP, '\\', '/')
                            + "/" + "model" + "/" + "NEW_MUSCA_BVR_DETAIL.jasper");
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "FANewImpressionFacture_BVR_Doc");
            abort();
        }
        super.setDocumentTitle(newCurrentDataSource.getEnteteFacture().getIdExterneRole() + " - " + index + " - " + "1_"
                + newCurrentDataSource.getEnteteFacture().getNomTiers());
        buildReportStartTime = System.currentTimeMillis();
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTailleLot(1);
        startTime = System.currentTimeMillis();
        // Récupère le provider des données chargées de manière asynchrone ou
        // synchrone
        impressionFactureDataSourceProvider = FANewImpFactDataSourceProviderFactory.newInstance(application,
                getSession(), passage, enteteFactures);
    }

    @Override
    public boolean beforePrintDocument() {
        printDocumentStartTime = System.currentTimeMillis();
        return super.beforePrintDocument();
    }

    /**
     * Returns the factureAvecMontantMinime.
     * 
     * @return boolean
     */
    public void checkFactureAvecMontantMinime(FAEnteteFacture entFacture) {
        FWCurrency montantFacCur = new FWCurrency(entFacture.getTotalFacture());
        if ((!montantFacCur.isZero()) && (montantFacCur.compareTo(new FWCurrency(montantMinimeNeg)) > 0)
                && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) < 0)) {
            setFactureAvecMontantMinime(true);
        } else {
            setFactureAvecMontantMinime(false);
        }
        if ((!montantFacCur.isZero()) && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) > 0)
                && (montantFacCur.compareTo(new FWCurrency(montantMinimeMax)) < 0)) {
            setFactureMontantReport(true);
        } else {
            setFactureMontantReport(false);
        }
    }

    @Override
    public void createDataSource() throws Exception {

        fillDocInfo();

        FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

        super.setParametres(FAImpressionFacture_Param.P_HEADER_EACH_PAGE, Boolean.TRUE);
        long currentTime = System.currentTimeMillis();
        // incrementer le conteur le la progress bar
        getParent().setProgressCounter(countFactures++);
        super.setTemplateFile(FANewImpressionFacture_BVR_Doc.getTemplateFilename(newCurrentDataSource.getEnteteFacture()));
        // Vérifier l'id de l'entête
        if (JadeStringUtil.isIntegerEmpty(newCurrentDataSource.getEnteteFacture().getIdEntete())) {
            return;
        }
        super.setDataSource(newCurrentDataSource.getImpressionFacture_DS());
        super.setParametres(FAImpressionFacture_Param.P_TOTAL_ROW, newCurrentDataSource.getNbDeLigne());

        // controller si la facture contient un montant minime
        checkFactureAvecMontantMinime(newCurrentDataSource.getEnteteFacture());

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), application, newCurrentDataSource.getEnteteFacture().getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setUser(getSession().getUserInfo());
        _headerText(headerBean);
        if (FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL.equalsIgnoreCase(FANewImpressionFacture_BVR_Doc
                .getTemplateFilename(newCurrentDataSource.getEnteteFacture()))) {
            _tableHeader4DecSal();
        } else if (FANewImpressionFacture_BVR_Doc.TEMPLATE_FILENAME_BVR_NEUTRE
                .equalsIgnoreCase(FANewImpressionFacture_BVR_Doc.getTemplateFilename(newCurrentDataSource
                        .getEnteteFacture()))) {
            _tableHeaderBvrNeutre();
        } else {
            _tableHeader();
        }
        _summaryText();

        initCommonVar();

        if (ch.globaz.common.properties.CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new CAReferenceQR();
            qrFacture.setSession(getSession());
            // Initialisation des variables du document
            initVariableQR();
            // Génération du document QR
            qrFacture.initQR(this);
        } else {
            // BVR
            _bvrText();
        }

        caisseReportHelper.addHeaderParameters(this, headerBean);

        // Implémenter la même fonctionalité des signatures pour les entêtes
        // spécifiques aux documents et aux caisses...
        if ("" != getTemplateProperty(getDocumentInfo(), FANewImpressionFacture_BVR_Doc.HEADER_FILENAME)) {

            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), FANewImpressionFacture_BVR_Doc.HEADER_FILENAME));
            // pour la CICICAM, l'entête des BVRs n'a que la date, faire en
            // sorte que pour les autres caisses,
            // il y aie aussi le "lieu,le "...
        }
        totalTimeDataSource += (System.currentTimeMillis() - currentTime);
    }

    private void fillDocInfo() throws FWIException {
        // Info
        setDocumentInfo();
        // GED
        if (getEnvoyerGed().booleanValue()) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        } else if ((application).getProperty("mettreGed").equals("true") && isUnificationProcess()) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        } else {
            getDocumentInfo().setPublishDocument(true);
            getDocumentInfo().setArchiveDocument(false);
        }
        if (isImprimable()) {
            getDocumentInfo().setSeparateDocument(false);
        } else {
            getDocumentInfo().setSeparateDocument(true);
        }
    }

    /**
     * @return
     */
    private String getDecisionProVersoPourCaisse(String langue) {
        String numCaisse = null;
        try {
            numCaisse = ((BApplication) application).getProperty(CommonProperties.KEY_NO_CAISSE_FORMATE);
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Erreur pendant la lecture des properties du numéro de caisse pour l'impression du verso");
            return langue;
        }
        if (JadeStringUtil.isEmpty(numCaisse)) {
            return langue;
        }
        return numCaisse + "_" + langue;
    }

    /**
     * @return
     */
    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    /**
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return
     * @throws Exception
     */
    protected JasperPrint getVerso() throws Exception {
        // VYJ : Peut faire mieux, mais ce code n'a pratiquement aucun impacte
        String langue = newCurrentDataSource.getEnteteFacture().getISOLangueTiers();
        String documentKey = "";
        JasperPrint doc = null;
        String sousType = newCurrentDataSource.getEnteteFacture().getIdSousType();
        sousType = sousType.substring(4, 6);
        int type = JadeStringUtil.parseInt(sousType, 0);
        if (((type >= 1) && (type <= 12)) || ((type >= 40) && (type <= 46))) {
            if (((BApplication) application).getProperty("gestionVersoAcompte").equals("avec")) {
                documentKey = "MUSCA_FACTURE_VERSO_" + getDecisionProVersoPourCaisse(langue);
            } else {
                documentKey = "MUSCA_FACTURE_VERSO";
            }
        } else if (type == 22) {
            if (((BApplication) application).getProperty("gestionVersoEtudiant").equals("avec")) {
                documentKey = "MUSCA_FACTURE_ETUDIANT_VERSO_" + getDecisionProVersoPourCaisse(langue);
            } else {
                documentKey = "MUSCA_FACTURE_VERSO";
            }
        } else {
            if (((BApplication) application).getProperty("gestionVersoDecompte").equals("avec")) {
                documentKey = "MUSCA_FACTURE_VERSO_" + getDecisionProVersoPourCaisse(langue);
            } else {
                documentKey = "MUSCA_FACTURE_VERSO";
            }
        }
        try {
            doc = super.getImporter().importReport(documentKey, super.getImporter().getImportPath());
        } catch (Exception e) {
            doc = null;
        }
        if (doc == null) {
            // Recherche verso par défaut
            documentKey = "MUSCA_FACTURE_VERSO";
            try {
                doc = super.getImporter().importReport(documentKey, super.getImporter().getImportPath());
            } catch (Exception e) {
                doc = null;
            }
        }
        return doc;
    }

    protected boolean hasContentieux(CASection section, CACompteAnnexe compteAnnexe) throws CATechnicalException {

        if (isCAContentieux(compteAnnexe)) {
            return true;
        } else {
            if (isSectionContentieux(section)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Méthode d'initialisation du processus de génération/impression des document
     * 
     * @param session
     *            La session en cours
     * @throws Exception
     *             Levée en cas de problème dans la récupération de l'instance application <code>FAApplication</code>
     *             via la session
     */
    private void init(BSession session) throws Exception {
        application = ((FAApplication) session.getApplication());
        montantMinimeNeg = application.getMontantMinimeNeg(); // "-2"
        montantMinimePos = application.getMontantMinimePos(); // "+2"
        montantMinimeMax = application.getMontantMinimeMax(); // "+20"
        modeReporterMontantMinime = application.isModeReporterMontantMinimal();
        if (JadeStringUtil.isEmpty(montantMinimeNeg)) {
            montantMinimeNeg = FAApplication.MONTANT_MINIMENEG_DEFVAL;
        }
        if (JadeStringUtil.isEmpty(montantMinimePos)) {
            montantMinimePos = FAApplication.MONTANT_MINIMEPOS_DEFVAL;
        }
        if (JadeStringUtil.isEmpty(montantMinimeMax)) {
            montantMinimeMax = FAApplication.MONTANT_MINIMEMAX_DEFVAL;
        }
        if ("true".equalsIgnoreCase(FWFindParameter.findParameter(getTransaction(), "1", "FAREFFACTU",
                JACalendar.todayJJsMMsAAAA(), "", 0))) {
            wantReferenceFacture = true;
        }
        rolePersonelle = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
    }

    private boolean isCAContentieux(CACompteAnnexe compteAnnexe) {
        if (compteAnnexe.isASurveiller().booleanValue() || compteAnnexe.isMotifExistant(CACodeSystem.CS_RENTIER)
                || compteAnnexe.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)) {
            return true;
        }
        return false;
    }


    /**
     * @return
     */
    public boolean isImprimable() {
        return imprimable;
    }


    private boolean isSectionContentieux(CASection section) throws CATechnicalException {
        if (section.hasMotifContentieux(CACodeSystem.CS_RENTIER)
                || section.hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE) || section.isSectionAuxPoursuites(true)
                || !JadeStringUtil.isBlankOrZero(section.getIdPlanRecouvrement())) {
            return true;
        }
        return false;
    }

    protected boolean isSectionEchue(CASection section, FAPassage passage) throws Exception {
        // Integer dateFactu = new Integer(passage.getDateFacturation());
        // Integer dateEcheanceSection = new Integer(section.getDateEcheance());
        JADate dateEcheanceSection = new JADate(section.getDateEcheance());
        JADate dateFactu = new JADate(passage.getDateFacturation());
        JACalendar cal = getSession().getApplication().getCalendar(); // new
        // JAGregorianCalendar
        if (cal.compare(dateFactu, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
            String dateExecutionEtape = section._getDateExecutionSommation();
            if (JadeStringUtil.isBlank(dateExecutionEtape)) {
                return false;
            } else {
                JADate dateSommation = new JADate(dateExecutionEtape);
                dateSommation = cal.addDays(dateSommation, 15);
                // dateSommation = CADateUtil.getDateOuvrable(dateSommation);
                return (cal.compare(dateFactu, dateSommation) == JACalendar.COMPARE_FIRSTUPPER);
            }
        }
        return false;

    }

    public boolean isUnificationProcess() {
        return unificationProcess;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        // regarde si le container a encore des factures à imprimer et qu'il n'y
        // ait pas d'erreur
        if (isAborted()) {
            impressionFactureDataSourceProvider.setAbort(true);
            return false;
        } else {
            boolean hasNext = impressionFactureDataSourceProvider.hasNext(factureImpressionNo)
                    && (!(getSession().hasErrors()) || (getMemoryLog().hasErrors()));
            if (hasNext) {
                // initialise le datasource courant
                newCurrentDataSource = impressionFactureDataSourceProvider.getNextDataSource();
                // Regarde si il y a eu des erreurs au chargement et si c'est le
                // cas
                // les spécifie dans le memory log du process sous forme
                // d'avertissement (cf version précédente, c'est comme ça que
                // c'était fait visiblement)
                if (newCurrentDataSource.hasError()) {
                    getMemoryLog().logMessage(newCurrentDataSource.getErrorLog().toString(), FWViewBeanInterface.WARNING,
                            this.getClass().getName());
                }
                totalDataSourceLoadingTime += newCurrentDataSource.getLoadingTime();
            }
            factureImpressionNo++;
            return hasNext;
        }
    }

    /*
     * Insertion des infos pour la publication (GED)
     */
    public void setDocumentInfo() throws FWIException {
        FAApplication app = null;
        IFormatData affilieFormater = null;
        try {
            app = application;
            affilieFormater = app.getAffileFormater();
        } catch (Exception e1) {
            getMemoryLog().logMessage(
                    "",
                    FWMessage.ERREUR,
                    getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE")
                            + newCurrentDataSource.getEnteteFacture().getIdExterneRole());
        }
        try {
            // On rempli le documentInfo avec les infos du document
            TIDocumentInfoHelper.fill(getDocumentInfo(), newCurrentDataSource.getEnteteFacture().getIdTiers(),
                    getSession(), newCurrentDataSource.getEnteteFacture().getIdRole(), newCurrentDataSource
                            .getEnteteFacture().getIdExterneRole(), affilieFormater.unformat(newCurrentDataSource
                            .getEnteteFacture().getIdExterneRole()));
            CADocumentInfoHelper.fill(getDocumentInfo(), newCurrentDataSource.getEnteteFacture().getIdExterneFacture(),
                    newCurrentDataSource.getEnteteFacture().getIdTypeFacture(), newCurrentDataSource.getEnteteFacture()
                            .getPassage().getDateFacturation());
            FADocumentInfoHelper.fill(getDocumentInfo(), newCurrentDataSource.getEnteteFacture().getIdExterneFacture());
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "FAC");
            getDocumentInfo().setDocumentProperty("annee", getAnneeFromEntete(newCurrentDataSource.getEnteteFacture()));

            getDocumentInfo().setDocumentDate(getPassage().getDateFacturation());
            getDocumentInfo().setDocumentTitle(
                    newCurrentDataSource.getEnteteFacture().getDescriptionDecompte(
                            newCurrentDataSource.getEnteteFacture().getISOLangueTiers()));
            if (JadeStringUtil.isEmpty(newCurrentDataSource.getAdressePrincipale())) {
                getDocumentInfo().setRejectDocument(true);
            }
            if (!JANumberFormatter.deQuote(newCurrentDataSource.getEnteteFacture().getTotalFacture()).equals(
                    newCurrentDataSource.getMontantImprimer().toString())
                    && !APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(newCurrentDataSource.getEnteteFacture()
                            .getIdTypeFacture())) {
                getDocumentInfo().setRejectDocument(true);
            }
            getDocumentInfo().setDuplex((getSession().getApplication()).getProperty("gestionVerso").equals("avec"));
            // Numéro inforom selon le type de facture
            if (wantReferenceFacture) {
                int idSousType = Integer.parseInt(newCurrentDataSource.getEnteteFacture().getIdSousType());
                if (((227001 <= idSousType) && (idSousType <= 227012))
                        || ((227040 <= idSousType) && (idSousType <= 227044))
                        || ((227061 <= idSousType) && (idSousType <= 227062))) {
                    if (newCurrentDataSource.getEnteteFacture().getIdRole().equalsIgnoreCase(rolePersonelle)) {
                        // Périodique personelle
                        getDocumentInfo().setDocumentType(
                                FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_PERIODIQUE_PERSONELLE);
                    } else {
                        // Périodique paritaire par défaut
                        getDocumentInfo().setDocumentType(
                                FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_PERIODIQUE_PARITAIRE);
                    }
                } else {
                    if (newCurrentDataSource.getEnteteFacture().getIdRole().equalsIgnoreCase(rolePersonelle)) {
                        // Décompte personelle
                        getDocumentInfo().setDocumentType(
                                FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PERSONELLE);
                    } else {
                        // Décompte paritaire
                        getDocumentInfo().setDocumentType(
                                FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                    }
                }
            } else {
                getDocumentInfo()
                        .setDocumentType(FANewImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
            }
            getDocumentInfo().setDocumentTypeNumber(getDocumentInfo().getDocumentType());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    e.getMessage().toString(),
                    FWMessage.ERREUR,
                    getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO")
                            + newCurrentDataSource.getEnteteFacture().getIdExterneRole());
        }
    }

    /**
     * Sets the newCurrentDataSource.getEnteteFacture()List.
     * 
     * @param entityList
     *            .getEnteteFacture()List The newCurrentDataSource.getEnteteFacture()List to set
     */
    @Override
    public void setEntityList(ArrayList entityList) {
        // initialise the progress bar
        getParent().setProgressScaleValue(entityList.size());
        this.entityList = entityList.iterator();
        enteteFactures = entityList;
    }

    /**
     * @param boolean1
     */
    public void setEnvoyerGed(Boolean boolean1) {
        envoyerGed = boolean1;
    }


    /**
     * @param boolean1
     */
    public void setImprimable(boolean boolean1) {
        imprimable = boolean1;
    }

    /**
     * @param i
     */
    public void setIndex(int i) {
        index = i;
    }

    public void setUnificationProcess(boolean unificationProcess) {
        this.unificationProcess = unificationProcess;
    }

}
