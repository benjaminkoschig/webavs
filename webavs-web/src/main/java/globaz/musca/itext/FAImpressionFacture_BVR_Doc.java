package globaz.musca.itext;

import ch.globaz.common.document.reference.ReferenceQR;
import globaz.aquila.print.CODecisionFPV;
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
import globaz.framework.util.FWMessageFormat;
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
import globaz.musca.api.musca.PaireIdExterneEBill;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.*;
import globaz.musca.itext.impfactbvrutil.FAImpFactDataSourceProviderFactory;
import globaz.musca.itext.impfactbvrutil.FAImpFactPropertiesProvider;
import globaz.musca.itext.impfactbvrutil.IFAImpFactDataSourceProvider;
import globaz.musca.process.FAGenericProcess;
import globaz.musca.process.FAImpressionFactureProcess;
import globaz.musca.process.FAImpressionFactureProcessSansLSVRemb;
import globaz.musca.util.FAUtil;
import globaz.naos.util.AFIDEUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.exceptions.CATechnicalException;
import globaz.osiris.translation.CACodeSystem;
import globaz.webavs.common.CommonProperties;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignField;

import java.util.*;

/**
 * Le document imprime les zones de facture selon les param?tres suivants: _modeRecouvrement : aucun, bvr,
 * remboursement, recouvrement direct _critereDecompte : interne, positif, note de credit, decompte z?ro Date de
 * cr?ation : (26.02.2003 16:54:19)
 *
 * @author: Administrator
 */
public class FAImpressionFacture_BVR_Doc extends FAImpressionFacturation {

    private static final long serialVersionUID = 423467248804364021L;
    private final static String CODEDECOMPTESALAIRE13 = "13";
    private final static String CODEDECOMPTESALAIRE14 = "14";
    private final static String CODEDECOMPTESALAIRE30 = "30";
    private final static String CODEDECOMPTESALAIRE33 = "33";
    private final static String HEADER_FILENAME = "header.filename.bvr";
    private final static String HEADER_FILENAME_EBU = "header.filename.bvr.ebu";
    public final static String NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE = "0099CFA"; // Utilis? par d?faut
    public final static String NUM_INFOROM_FACTURE_DECOMPTE_PERSONELLE = "0290CFA";
    public final static String NUM_INFOROM_FACTURE_PERIODIQUE = "0293CFA";
    public final static String NUM_INFOROM_FACTURE_PERIODIQUE_PARITAIRE = "0288CFA";
    public final static String NUM_INFOROM_FACTURE_PERIODIQUE_PERSONELLE = "0289CFA";
    public final static String TEMPLATE_FILENAME = "MUSCA_BVR_1_QR";
    public final static String TEMPLATE_FILENAME_BVR_NEUTRE = "MUSCA_BVR_NEUTRE_QR";
    public final static String TEMPLATE_FILENAME4DECSAL = "MUSCA_BVR4DECSAL_QR"; // Template
    private static String TEMPLATE_NAME = "MUSCA_BVR_NEUTRE_QR"; // Par d?faut
    private Boolean isEbusiness = false;
    public Map<PaireIdExterneEBill, List<Map>> lignesFacture = new LinkedHashMap();
    public Map<PaireIdExterneEBill, String> referencesFacture = new LinkedHashMap();

    public static String getTemplateFilename(FAEnteteFacture entFacture) {
        if (FAImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE13.equalsIgnoreCase(entFacture.getIdExterneFacture()
                .substring(4, 6))
                || FAImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE14.equalsIgnoreCase(entFacture.getIdExterneFacture()
                .substring(4, 6))
                || FAImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE33.equalsIgnoreCase(entFacture.getIdExterneFacture()
                .substring(4, 6))
                || FAImpressionFacture_BVR_Doc.CODEDECOMPTESALAIRE30.equalsIgnoreCase(entFacture.getIdExterneFacture()
                .substring(4, 6))) {
            setJasperTemplate(FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL);
            return FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL; // Template pour d?claration de
            // salaire
        } else if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(entFacture.getIdTypeFacture())) {
            setJasperTemplate(FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME_BVR_NEUTRE);
            return FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME_BVR_NEUTRE; // Template pour bulletin
            // neutre
        } else {
            setJasperTemplate(FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME);
            return FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME;
        }
    }

    private FAApplication application = null;
    private long buildReportStartTime = 0;


    private int countFactures = 0;

    private ArrayList enteteFactures = null;
    private Boolean envoyerGed = new Boolean(false);
    // Va contenir les donn?es ? utiliser dans le cadre de la g?n?ration des
    // documents
    private IFAImpFactDataSourceProvider impressionFactureDataSourceProvider = null;
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

    public FAImpressionFacture_BVR_Doc() throws Exception {
        this(new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    public FAImpressionFacture_BVR_Doc(BSession session) throws java.lang.Exception {
        super(session, FAApplication.APPLICATION_MUSCA_REP, "CaisseDeCompensation");
        setDocumentTitle(getSession().getLabel("TITRE_DOC_FACTURE"));
        init(session);
    }

    public FAImpressionFacture_BVR_Doc(FAImpressionFactureProcess parent) throws java.lang.Exception {
        super(parent, FAApplication.APPLICATION_MUSCA_REP, parent.getFactureType());
        super.setDocumentTitle(parent.getFactureType());
        setSendMailOnError(false); // le processus n'enverra jamais de mail et
        // son parent s'en charge
        setEnvoyerGed(parent.getEnvoyerGed());
        setImprimable(parent.isImprimable());
        setUnificationProcess(parent.isUnificationProcess());
        init(parent.getSession());
    }

    public FAImpressionFacture_BVR_Doc(FAImpressionFactureProcessSansLSVRemb parent) throws java.lang.Exception {
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
            if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture()
                    .getIdTypeFacture())) {
                getBvr().setBVRNeutre(currentDataSource.getEnteteFacture(), isFactureAvecMontantMinime(),
                        reporterMontant);
                textMontant = getBvr().getTextBulletinNeutre(getSession(),
                        currentDataSource.getEnteteFacture().getISOLangueTiers());
                super.setParametres(FAImpressionFacture_Param.P_REMARQUE,
                        FWMessageFormat.format(FWMessageFormat.prepareQuotes(textMontant, false), getDateEcheance()));
            } else {
                getBvr().setBVR(currentDataSource.getEnteteFacture(), isFactureAvecMontantMinime(), reporterMontant);
            }

            // Modification suite ? QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");
            super.setParametres(COParameter.P_SUBREPORT_QR_CURRENT_PAGE, getImporter().getImportPath() + "BVR_TEMPLATE_CURRENT_PAGE.jasper");

            super.setParametres(FWIImportParametre.PARAM_REFERENCE + "_X", CODecisionFPV.REFERENCE_NON_FACTURABLE_DEFAUT);
            super.setParametres(COParameter.P_OCR + "_X", CODecisionFPV.OCRB_DEFAUT);
            super.setParametres(COParameter.P_FRANC + "_X", CODecisionFPV.MONTANT_DEFAUT);
            super.setParametres(COParameter.P_CENTIME + "_X", CODecisionFPV.CENT_DEFAUT);

            super.setParametres(FAImpressionFacture_Param.P_ADRESSE,
                    getBvr().getAdresse(currentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(FAImpressionFacture_Param.P_ADRESSECOPY,
                    getBvr().getAdresse(currentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(FAImpressionFacture_Param.P_COMPTE, getBvr().getNumeroCC()); // num?ro
            // CC
            // TODO-VYJ cette variable semble pas ?tre utilis?e
            // numeroCompte = getBvr().getNoAdherent(); // Num?ro d'adh?rent
            super.setParametres(FAImpressionFacture_Param.P_VERSE, getBvr().getLigneReference() + "\n"
                    + adresseDebiteur);
            super.setParametres(FAImpressionFacture_Param.P_PAR, adresseDebiteur);

            super.setParametres(FWIImportParametre.PARAM_REFERENCE, getBvr().getLigneReference());
            super.setParametres(FAImpressionFacture_Param.P_OCR, getBvr().getOcrb());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche du texte sur le bvr: "
                            + currentDataSource.getEnteteFacture().getIdExterneRole() + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }

        // ?tirer le montant avec deux espace blanc entre 2 chiffres
        if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture().getIdTypeFacture())) {
            super.setParametres(FAImpressionFacture_Param.P_FRANC, " ");
            super.setParametres(FAImpressionFacture_Param.P_CENTIME, " ");
        } else if (!(tmpCurrency.isNegative() || tmpCurrency.isZero() || isFactureAvecMontantMinime() || (isFactureMontantReport() && modeReporterMontantMinime))) {
            if (currentDataSource.getEnteteFacture().getIdModeRecouvrement()
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

            // Si aucune date n'est renseing?e, on prend la date de facturation
            if (JAUtil.isDateEmpty(dateImpression2)) {
                dateImpression2 = getPassage().getDateFacturation();
            }
            // texte de la date
            headerBean.setDate(JACalendar.format(dateImpression2, currentDataSource.getEnteteFacture()
                    .getISOLangueTiers()));

            // adresse du tiers
            headerBean.setAdresse(currentDataSource.getAdressePrincipale());

            if (wantReferenceFacture) {
                JadeUser userRef = null;
                if (!JadeStringUtil.isEmpty(currentDataSource.getEnteteFacture().getReferenceFacture())) {

                    JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();
                    userRef = service.load(currentDataSource.getEnteteFacture().getReferenceFacture());
                }
                setJadeUser(userRef);
            }
            headerBean.setUser(getJadeUser());
            if (getJadeUser() != null) {
                headerBean.setNomCollaborateur(getJadeUser().getFirstname() + " " + getJadeUser().getLastname());
                headerBean.setTelCollaborateur(getJadeUser().getPhone());
            } else {
                headerBean.setNomCollaborateur("");
                headerBean.setTelCollaborateur("");
            }

            // num?ro AVS
            headerBean.setNoAvs("");
            if (!CaisseHelperFactory.CS_AFFILIE_PARITAIRE.equals(currentDataSource.getEnteteFacture().getIdRole())) {
                if (!"".equals(currentDataSource.getEnteteFacture().getNumeroAVSTiers(getTransaction()))) {
                    headerBean.setNoAvs(currentDataSource.getEnteteFacture().getNumeroAVSTiers(getTransaction()));
                }
            }

            // No affili?
            headerBean.setNoAffilie(currentDataSource.getEnteteFacture().getIdExterneRole());

            AFIDEUtil.addNumeroIDEInDoc(getSession(), headerBean, currentDataSource.getEnteteFacture().getIdTiers(),
                    currentDataSource.getEnteteFacture().getIdExterneRole(), currentDataSource.getEnteteFacture()
                            .getIdExterneFacture(), currentDataSource.getEnteteFacture().getIdRole());

            headerBean.setConfidentiel(true);

            // description du d?compte
            super.setParametres(FAImpressionFacturation_Param.getParamP(5), currentDataSource.getEnteteFacture()
                    .getDescriptionDecompte(currentDataSource.getEnteteFacture().getISOLangueTiers()));

            // Num?ro de page
            super.setParametres(FAImpressionFacture_Param.P_FACTURE_IMPNO, currentDataSource.getEnteteFacture()
                    .getIdPassage() + "-" + new Integer(getFactureImpressionNo()));
            if (!currentDataSource.getEnteteFacture().getLibelle().equals(null)) {
                super.setParametres(FAImpressionFacture_Param.P_LIBELLE, currentDataSource.getEnteteFacture()
                        .getLibelle());
            }

            super.setParametres(
                    FAImpressionFacture_Param.P_TEXT2,
                    getSession().getApplication().getLabel("FACREPORT",
                            currentDataSource.getEnteteFacture().getISOLangueTiers()));
            super.setParametres(
                    FAImpressionFacture_Param.P_TEXT1,
                    getSession().getApplication().getLabel("FACREPORTMONT",
                            currentDataSource.getEnteteFacture().getISOLangueTiers()));

        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Les param?tres de l'objet peuvent ne pas avoir ?t? mis correctement"
                            + currentDataSource.getEnteteFacture().getIdExterneRole(), FWMessage.AVERTISSEMENT,
                    headerBean.getClass().getName());
        }
    }


    protected void _summaryText() {
        String dateEcheance = "";
        int idSousType = 0;
        int troisDernierChiffre = 0;
        boolean isInteret = false;
        try {
            APISectionDescriptor sectionDescriptor = application.getSectionDescriptor(getSession());

            // initialiser le section descriptor avec les param?tres de l'ent?te
            // de facture
            sectionDescriptor.setSection(currentDataSource.getEnteteFacture().getIdExterneFacture(), currentDataSource
                            .getEnteteFacture().getIdTypeFacture(), currentDataSource.getEnteteFacture().getIdSousType(),
                    passage.getDateFacturation(), "", "");

            // la date d'?ch?ance est calcul?e par le sectionDescriptor
            dateEcheance = sectionDescriptor.getDateEcheanceFacturation();

            // On r?cup?re le type de d?compte d'une facture PO 5025
            idSousType = JadeStringUtil.parseInt(currentDataSource.getEnteteFacture().getIdSousType().substring(4, 6),
                    0);
            // On r?cup?re les 3 derniers chiffres du num?ro de facture pour savoir si c'est des int?r?ts tardifs
            // PO 5222
            troisDernierChiffre = JadeStringUtil.parseInt(currentDataSource.getEnteteFacture().getIdExterneFacture()
                    .substring(6, 9), 0);
            if (troisDernierChiffre == 900) {
                isInteret = true;
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }

        try {
            if (!APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture()
                    .getIdTypeFacture())) {
                // textMontant = getBvr().getTextBVRNeutre(getSession(),
                // currentDataSource.getEnteteFacture().getISOLangueTiers());
                // super.setParametres(FAImpressionFacture_Param.P_REMARQUE,
                // textMontant);
                // }else{
                // Texte montant NEGATIF
                // /////////////////////////////////
                if ("-".equalsIgnoreCase(currentDataSource.getEnteteFacture().getTotalFacture().substring(0, 1))) {

                    // cas pour montant minime n?gatif
                    if (isFactureAvecMontantMinime()) {
                        textMontant = application.getLabel("FACTEXT_MINIMENEG", currentDataSource.getEnteteFacture()
                                .getISOLangueTiers());
                    }
                    // Cas n?gatif retenu manuellement
                    else if (FAEnteteFacture.CS_MODE_RETENU.equalsIgnoreCase(currentDataSource.getEnteteFacture()
                            .getIdModeRecouvrement())) {
                        textMontant = application.getLabel("FACTEXT_RETENUNEG", currentDataSource.getEnteteFacture()
                                .getISOLangueTiers());
                    }
                    // Cas n?gatif retenu pour cause de contentieux
                    else if (FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE.equalsIgnoreCase(currentDataSource
                            .getEnteteFacture().getIdModeRecouvrement())) {
                        textMontant = application.getLabel("FACTEXT_CONTENTIEUX", currentDataSource.getEnteteFacture()
                                .getISOLangueTiers());
                    } else {
                        // pas d'adresse de paiement
                        if (JadeStringUtil.isBlankOrZero(currentDataSource.getEnteteFacture().getIdAdressePaiement())) {
                            textMontant = application.getLabel("FACTEXT_COMPTE_MANQUANT", currentDataSource
                                    .getEnteteFacture().getISOLangueTiers());
                        } else {
                            textMontant = application.getLabel("FACTEXT_NCR", currentDataSource.getEnteteFacture()
                                    .getISOLangueTiers())
                                    + "\n"
                                    + application.getLabel("FACTEXT_NCR2", currentDataSource.getEnteteFacture()
                                    .getISOLangueTiers());
                        }
                    }

                }
                // Texte montant POSITIF
                // /////////////////////////////////

                // cas pour montant minime positif
                else {
                    if ((new FWCurrency(currentDataSource.getEnteteFacture().getTotalFacture()).isZero())) {
                        textMontant = application.getLabel("FACTEXT_TOTAL_NOCOMMENT", currentDataSource
                                .getEnteteFacture().getISOLangueTiers());
                    } else if (isFactureAvecMontantMinime()) {
                        textMontant = application.getLabel("FACTEXT_MINIMEPOS", currentDataSource.getEnteteFacture()
                                .getISOLangueTiers());
                    } else if (isModeReporterMontantMinimal()
                            && !new FWCurrency(currentDataSource.getEnteteFacture().getTotalFacture()).isZero()) // Dans
                    // le
                    // mode
                    // de
                    // repport
                    // du
                    // montant
                    {
                        if (isFactureMontantReport() && modeReporterMontantMinime) {
                            try {
                                // Si l'affili? a un compte annexe on lui dit
                                // que le
                                // montant est report?
                                // if (_getCompteAnnexe().isNew() ||
                                // JadeStringUtil.isIntegerEmpty(_getCompteAnnexe().getRole().getDateFin()))
                                // {
                                textMontant = getSession().getApplication().getLabel("FACTEXT_MINIMEPOS_REPORTE",
                                        currentDataSource.getEnteteFacture().getISOLangueTiers());
                                // }
                            } catch (Exception e1) {
                                getMemoryLog().logMessage(e1.getMessage(), FWMessage.ERREUR, this.getClass().getName());
                            }
                        } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equalsIgnoreCase(currentDataSource
                                .getEnteteFacture().getIdModeRecouvrement())) {
                            textMontant = application.getLabel("FACTEXT_RECOUVREMENT_DIRECT", currentDataSource
                                    .getEnteteFacture().getISOLangueTiers())
                                    + " " + dateEcheance;
                        } else {
                            if (currentDataSource.getEnteteFacture().getIdSousType().equals("227030")) {
                                textMontant = application.getLabel("FACTEXT1_BIS", currentDataSource.getEnteteFacture()
                                        .getISOLangueTiers())
                                        + application.getLabel(dateEcheance, currentDataSource.getEnteteFacture()
                                        .getISOLangueTiers());
                            } else {
                                if ("true".equals(((BApplication) application).getProperty("texteMontantDifferent"))) {
                                    if ((((idSousType >= 1) && (idSousType <= 12))
                                            || ((idSousType >= 40) && (idSousType <= 46)) || ((idSousType > 60) && (idSousType < 63)))
                                            && !isInteret) {
                                        textMontant = application.getLabel("FACTEXT_3", currentDataSource
                                                .getEnteteFacture().getISOLangueTiers());
                                    } else {
                                        textMontant = application.getLabel("FACTEXT_1", currentDataSource
                                                .getEnteteFacture().getISOLangueTiers())
                                                + " "
                                                + dateEcheance
                                                + " "
                                                + application.getLabel("FACTEXT_2", currentDataSource
                                                .getEnteteFacture().getISOLangueTiers());
                                    }
                                } else {
                                    textMontant = application.getLabel("FACTEXT_1", currentDataSource
                                            .getEnteteFacture().getISOLangueTiers())
                                            + " "
                                            + dateEcheance
                                            + " "
                                            + application.getLabel("FACTEXT_2", currentDataSource.getEnteteFacture()
                                            .getISOLangueTiers());
                                }
                                // en allemand, le texte de fin suit la date
                            }
                        }
                    } else if (FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equalsIgnoreCase(currentDataSource
                            .getEnteteFacture().getIdModeRecouvrement())) {
                        textMontant = application.getLabel("FACTEXT_RECOUVREMENT_DIRECT", currentDataSource
                                .getEnteteFacture().getISOLangueTiers())
                                + " " + dateEcheance;
                    } else if (!new FWCurrency(currentDataSource.getEnteteFacture().getTotalFacture()).isZero()) {
                        if (currentDataSource.getEnteteFacture().getIdSousType().equals("227030")) {
                            textMontant = application.getLabel("FACTEXT1_BIS", currentDataSource.getEnteteFacture()
                                    .getISOLangueTiers())
                                    + application.getLabel(dateEcheance, currentDataSource.getEnteteFacture()
                                    .getISOLangueTiers());
                        } else {
                            if ("true".equals(((BApplication) application).getProperty("texteMontantDifferent"))) {
                                if ((((idSousType >= 1) && (idSousType <= 12))
                                        || ((idSousType >= 40) && (idSousType <= 46)) || ((idSousType > 60) && (idSousType < 63)))
                                        & !isInteret) {
                                    textMontant = application.getLabel("FACTEXT_3", currentDataSource
                                            .getEnteteFacture().getISOLangueTiers());
                                } else {
                                    textMontant = application.getLabel("FACTEXT_1", currentDataSource
                                            .getEnteteFacture().getISOLangueTiers())
                                            + " "
                                            + dateEcheance
                                            + " "
                                            + application.getLabel("FACTEXT_2", currentDataSource.getEnteteFacture()
                                            .getISOLangueTiers());
                                }
                            } else {
                                textMontant = application.getLabel("FACTEXT_1", currentDataSource.getEnteteFacture()
                                        .getISOLangueTiers())
                                        + " "
                                        + dateEcheance
                                        + " "
                                        + application.getLabel("FACTEXT_2", currentDataSource.getEnteteFacture()
                                        .getISOLangueTiers());
                            }
                        }
                    }
                }

                super.setParametres(FAImpressionFacture_Param.P_REMARQUE, currentDataSource.getEnteteFacture()
                        .getRemarque(getTransaction()));

                super.setParametres(FAImpressionFacture_Param.P_TEXT, textMontant);
            }

            super.setParametres(FAImpressionFacture_Param.P_ADR_CAISSE,
                    application.getLabel("ADR_CAISSE", currentDataSource.getEnteteFacture().getISOLangueTiers()));
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    protected void _tableHeader() {
        String langueIso = currentDataSource.getEnteteFacture().getISOLangueTiers();
        setColumnHeader(1, application.getLabel("FACLIBELLE", langueIso));
        setColumnHeader(2, application.getLabel("FACBASE", langueIso));
        setColumnHeader(3, application.getLabel("FACACOMPTE", langueIso));
        setColumnHeader(4, application.getLabel("FACSOLDE", langueIso));
        setColumnHeader(5, application.getLabel("FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("FACPERIODE", langueIso));

    }

    protected void _tableHeader4DecSal() {
        String langueIso = currentDataSource.getEnteteFacture().getISOLangueTiers();
        setColumnHeader(1, application.getLabel("FACLIBELLE", langueIso));
        setColumnHeader(2, application.getLabel("FACBASE", langueIso));
        setColumnHeader(3, application.getLabel("FACACOMPTE", langueIso));
        setColumnHeader(5, application.getLabel("FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("FACEFFECTIF", langueIso));
    }

    protected void _tableHeaderBvrNeutre() {
        String langueIso = currentDataSource.getEnteteFacture().getISOLangueTiers();
        setColumnHeader(1, application.getLabel("FACLIBELLE", langueIso));
        setColumnHeader(2, application.getLabel("FACMASSE", langueIso));
        setColumnHeader(5, application.getLabel("FACTAUX", langueIso));
        setColumnHeader(6, application.getLabel("FACMONTANT", langueIso));
        setColumnHeader(7, application.getLabel("FACPERIODE", langueIso));
    }

    @Override
    public void afterBuildReport() {
        try {
            FAModulePassageManager modulePassageManager = new FAModulePassageManager();
            modulePassageManager.setSession(getSession());
            modulePassageManager.setForIdPassage(passage.getId());
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
            boolean hasPassageModuleFacturationDecisionCAP_CGAS = modulePassageManager.getCount() >= 1;

            modulePassageManager = new FAModulePassageManager();
            modulePassageManager.setSession(getSession());
            modulePassageManager.setForIdPassage(passage.getId());
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_CAP_CGAS);
            hasPassageModuleFacturationDecisionCAP_CGAS = hasPassageModuleFacturationDecisionCAP_CGAS
                    || (modulePassageManager.getCount() >= 1);

            if (!hasPassageModuleFacturationDecisionCAP_CGAS) {
                if (((BApplication) application).getProperty("gestionVerso").equals("avec")) {
                    JasperPrint verso = null;
                    verso = getVerso();
                    if (verso != null) {
                        verso.setName(currentDataSource.getEnteteFacture().getIdExterneRole() + " - " + index
                                + " - 2_VERSO");
                        index++;
                        super.getDocumentList().add(verso);
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, "Erreur pendant la lecture des properties l'impression du verso");
        }
        totalBuildReport += (System.currentTimeMillis() - buildReportStartTime);
    }

    @Override
    public void afterExecuteReport() {
        if (FAImpFactPropertiesProvider.IS_DEBUG_MODE) {
            JadeLogger.info(this, "----> Temps total de l'ex?cution du reporting : "
                    + (System.currentTimeMillis() - startTime));
            JadeLogger.info(this, "----> Temps moyen d'ex?cution de la m?thode <createDataSource> : "
                    + (totalTimeDataSource / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen d'ex?cution du build report : "
                    + (totalBuildReport / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen d'ex?cution du print document : "
                    + (totalPrintDocumentTime / factureImpressionNo));
            JadeLogger.info(this, "----> Temps moyen du chargement des donn?es (FAImpressionFactureDatasource) :"
                    + totalDataSourceLoadingTime / factureImpressionNo);
        }
        super.afterExecuteReport();
    }

    @Override
    public void afterPrintDocument() {
        totalPrintDocumentTime += (System.currentTimeMillis() - printDocumentStartTime);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        super.setDocumentTitle(currentDataSource.getEnteteFacture().getIdExterneRole() + " - " + index + " - " + "1_"
                + currentDataSource.getEnteteFacture().getNomTiers());
        buildReportStartTime = System.currentTimeMillis();
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTailleLot(1);
        startTime = System.currentTimeMillis();
        // R?cup?re le provider des donn?es charg?es de mani?re asynchrone ou
        // synchrone
        impressionFactureDataSourceProvider = FAImpFactDataSourceProviderFactory.newInstance(application, getSession(),
                passage, enteteFactures);
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
        if ((!montantFacCur.isZero()) && (montantFacCur.compareTo(new FWCurrency(montantMinimeNeg)) >= 0)
                && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) <= 0)) {
            setFactureAvecMontantMinime(true);
        } else {
            setFactureAvecMontantMinime(false);
        }
        if ((!montantFacCur.isZero()) && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) > 0)
                && (montantFacCur.compareTo(new FWCurrency(montantMinimeMax)) <= 0)) {
            setFactureMontantReport(true);
        } else {
            setFactureMontantReport(false);
        }
    }

    /**
     * @param list
     * @param libelle
     * @param montantInitial
     * @param montantDejaFacture
     * @param masseFacture
     * @param taux
     * @param montant
     * @param dateDebut
     * @param dateFin
     * @param index
     */
    private void _addLine(List list, String libelle, Double montantInitial, Double montantDejaFacture, Double masseFacture, String taux, Double montant, String dateDebut, String dateFin,
                          Integer index) {
        Map m = new HashMap();
        m.put("COL_1", libelle);
        m.put("COL_2", montantInitial);
        m.put("COL_3", montantDejaFacture);
        m.put("COL_4", masseFacture);
        m.put("COL_5", taux);
        m.put("COL_6", montant);
        m.put("COL_7_DEBUT", dateDebut);
        m.put("COL_7_FIN", dateFin);
        m.put("COL_ID", index);
        list.add(m);
    }

    @Override
    public void createDataSource() throws Exception {

        // en premier lieu, renseigner le docInfo
        fillDocumentInfo();

        FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

        super.setParametres(FAImpressionFacture_Param.P_HEADER_EACH_PAGE, new Boolean(wantHeaderOnEachPage()));
        long currentTime = System.currentTimeMillis();
        // incrementer le conteur le la progress bar
        getParent().setProgressCounter(countFactures++);
        super.setTemplateFile(FAImpressionFacture_BVR_Doc.getTemplateFilename(currentDataSource.getEnteteFacture()));
        // V?rifier l'id de l'ent?te
        if (JadeStringUtil.isIntegerEmpty(currentDataSource.getEnteteFacture().getIdEntete())) {
            return;
        }

        // Prepare la map des lignes de factures eBill si propri?t? eBillMusca est active et si compte annexe de la facture inscrit ? eBill
        boolean eBillMuscaActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillMuscaActifEtDansListeCaisses(getSession());
        if (eBillMuscaActif) {
            CACompteAnnexe compteAnnexe = FAGenericProcess.getCompteAnnexe(currentDataSource.getEnteteFacture(), getSession(), getTransaction());
            if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.getEBillAccountID())) {
                //Extrait les lignes dans une liste
                List data = buildLignes(); // lignes Factures
                // Met les lignes trouv?es dans une hashMap identifi? de mani?re unique par une pair d'id
                lignesFacture.put(new PaireIdExterneEBill(currentDataSource.getEnteteFacture().getIdExterneRole(), currentDataSource.getEnteteFacture().getIdExterneFacture()), data);
            }
        }

        super.setDataSource(currentDataSource.getImpressionFacture_DS());
        super.setParametres(FAImpressionFacture_Param.P_TOTAL_ROW, currentDataSource.getNbDeLigne());

        // controller si la facture contient un montant minime
        checkFactureAvecMontantMinime(currentDataSource.getEnteteFacture());

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), application, currentDataSource.getEnteteFacture().getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setUser(getSession().getUserInfo());
        _headerText(headerBean);
        if (FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME4DECSAL.equalsIgnoreCase(FAImpressionFacture_BVR_Doc
                .getTemplateFilename(currentDataSource.getEnteteFacture()))) {
            _tableHeader4DecSal();
        } else if (FAImpressionFacture_BVR_Doc.TEMPLATE_FILENAME_BVR_NEUTRE
                .equalsIgnoreCase(FAImpressionFacture_BVR_Doc.getTemplateFilename(currentDataSource.getEnteteFacture()))) {
            _tableHeaderBvrNeutre();
        } else {
            _tableHeader();
        }
        _summaryText();


        // Init montant facture
        // initier les variables pour le montant

        initCommonVar();

        if (ch.globaz.common.properties.CommonProperties.QR_FACTURE.getBooleanValue()) {
            // -- QR
            qrFacture = new ReferenceQR();
            qrFacture.setSession(getSession());

            qrFacture.setQrNeutre(APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture().getIdTypeFacture()));
            qrFacture.setMontantMinimeOuMontantReporter(this.isFactureAvecMontantMinime() || (this.isFactureMontantReport() && modeReporterMontantMinime));
            qrFacture.setRecouvrementDirect(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT.equals(currentDataSource.getEnteteFacture().getIdModeRecouvrement()));

            if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture().getIdTypeFacture())) {
                textMontant = qrFacture.getTextBulletinNeutre(getSession(),
                        currentDataSource.getEnteteFacture().getISOLangueTiers());
                super.setParametres(FAImpressionFacture_Param.P_REMARQUE,
                        FWMessageFormat.format(FWMessageFormat.prepareQuotes(textMontant, false), getDateEcheance()));
            }

            // Initialisation des variables du document
            initVariableQR(currentDataSource.getEnteteFacture().getISOLangueTiers());

            // G?n?ration du document QR
            qrFacture.initQR(this, qrFactures);
            referencesFacture.put(new PaireIdExterneEBill(currentDataSource.getEnteteFacture().getIdExterneRole(), currentDataSource.getEnteteFacture().getIdExterneFacture()), qrFacture.getReference());
        } else {
            // BVR
            _bvrText();
            referencesFacture.put(new PaireIdExterneEBill(currentDataSource.getEnteteFacture().getIdExterneRole(), currentDataSource.getEnteteFacture().getIdExterneFacture()), getBvr().getRefNoSpace());
        }

        caisseReportHelper.addHeaderParameters(this, headerBean);

        if (isEbusiness) {
            getImporter().getParametre().put(
                    ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                    ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                            + getTemplateProperty(getDocumentInfo(), FAImpressionFacture_BVR_Doc.HEADER_FILENAME_EBU));
        } else {
            // Impl?menter la m?me fonctionalit? des signatures pour les ent?tes
            // sp?cifiques aux documents et aux caisses...
            if ("" != getTemplateProperty(getDocumentInfo(), FAImpressionFacture_BVR_Doc.HEADER_FILENAME)) {

                getImporter().getParametre().put(
                        ICaisseReportHelper.PARAM_SUBREPORT_HEADER,
                        ((ACaisseReportHelper) caisseReportHelper).getDefaultModelPath() + "/"
                                + getTemplateProperty(getDocumentInfo(), FAImpressionFacture_BVR_Doc.HEADER_FILENAME));
                // pour la CICICAM, l'ent?te des BVRs n'a que la date, faire en
                // sorte que pour les autres caisses,
                // il y aie aussi le "lieu,le "...
            }
        }

        totalTimeDataSource += (System.currentTimeMillis() - currentTime);
    }

    private List buildLignes() throws CloneNotSupportedException, net.sf.jasperreports.engine.JRException {
        List list = new ArrayList();
        FAImpressionFacture_DS faImpressionFacture_ds = (FAImpressionFacture_DS) currentDataSource.getImpressionFacture_DS().clone();
        for (int i = 0; i < currentDataSource.getNbDeLigne(); i++) {
            faImpressionFacture_ds.next();
            JRDesignField field = new JRDesignField();

            field.setName("COL_ID");
            Integer index = (Integer) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            if (((FAAfact) faImpressionFacture_ds.getEnCours().get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION)
                    || ((FAAfact) faImpressionFacture_ds.getEnCours().get(0)).getIdTypeAfact().equals(FAAfact.CS_AFACT_COMPENSATION_INTERNE)) {
                field.setName("COL_1B");
            } else {
                field.setName("COL_1");
            }

            String libelleCourant = (String) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_2");
            Double montantInitial = (Double) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_3");
            Double montantDejaFacture = (Double) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_4");
            Double masseFacture = (Double) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_5");
            String taux = (String) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_6");
            Double montant = (Double) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_7_DEBUT");
            String dateDebut = (String) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            field.setName("COL_7_FIN");
            String dateFin = (String) faImpressionFacture_ds.getFieldValueSansModifEtatObject(field);

            _addLine(list, libelleCourant, montantInitial, montantDejaFacture, masseFacture, taux, montant, dateDebut, dateFin, index);

        }

        faImpressionFacture_ds.resetStatus();

        return list;
    }

    private void fillDocumentInfo() throws Exception {
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
     * @throws Exception
     */
    protected String getDateEcheance() throws Exception {
        APISectionDescriptor sectionDescriptor = application.getSectionDescriptor(getSession());
        sectionDescriptor.setSection(currentDataSource.getEnteteFacture().getIdExterneFacture(), currentDataSource
                .getEnteteFacture().getIdTypeFacture(), currentDataSource.getEnteteFacture().getIdSousType(), passage
                .getDateFacturation(), "", "");
        return sectionDescriptor.getDateEcheanceFacturation();
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
                    "Erreur pendant la lecture des properties du num?ro de caisse pour l'impression du verso");
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
        String langue = currentDataSource.getEnteteFacture().getISOLangueTiers();
        String documentKey = "";
        JasperPrint doc = null;
        String sousType = currentDataSource.getEnteteFacture().getIdSousType();
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
            } else if (((BApplication) application).getProperty("gestionVersoDecompte").equals("decision")) {
                documentKey = "MUSCA_FACTURE_VERSO_DEC_" + getDecisionProVersoPourCaisse(langue);
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
            // Recherche verso par d?faut
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
     * M?thode d'initialisation du processus de g?n?ration/impression des document
     *
     * @param session La session en cours
     * @throws Exception Lev?e en cas de probl?me dans la r?cup?ration de l'instance application <code>FAApplication</code>
     *                   via la session
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
        // regarde si le container a encore des factures ? imprimer et qu'il n'y
        // ait pas d'erreur
        if (isAborted()) {
            impressionFactureDataSourceProvider.setAbort(true);
            return false;
        } else {
            boolean hasNext = impressionFactureDataSourceProvider.hasNext(factureImpressionNo)
                    && (!(getSession().hasErrors()) || (getMemoryLog().hasErrors()));
            if (hasNext) {
                // initialise le datasource courant
                currentDataSource = impressionFactureDataSourceProvider.getNextDataSource();
                // Regarde si il y a eu des erreurs au chargement et si c'est le
                // cas
                // les sp?cifie dans le memory log du process sous forme
                // d'avertissement (cf version pr?c?dente, c'est comme ?a que
                // c'?tait fait visiblement)
                if (currentDataSource.hasError()) {
                    getMemoryLog().logMessage(currentDataSource.getErrorLog().toString(), FWViewBeanInterface.WARNING,
                            this.getClass().getName());
                }
                totalDataSourceLoadingTime += currentDataSource.getLoadingTime();
            }
            factureImpressionNo++;
            return hasNext;
        }
    }

    /*
     * Insertion des infos pour la publication (GED)
     */
    private void setDocumentInfo() throws FWIException {
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
                            + currentDataSource.getEnteteFacture().getIdExterneRole());
        }
        try {
            // On rempli le documentInfo avec les infos du document
            TIDocumentInfoHelper.fill(getDocumentInfo(), currentDataSource.getEnteteFacture().getIdTiers(),
                    getSession(), currentDataSource.getEnteteFacture().getIdRole(), currentDataSource
                            .getEnteteFacture().getIdExterneRole(), affilieFormater.unformat(currentDataSource
                            .getEnteteFacture().getIdExterneRole()));
            CADocumentInfoHelper.fill(getDocumentInfo(), currentDataSource.getEnteteFacture().getIdExterneFacture(),
                    currentDataSource.getEnteteFacture().getIdTypeFacture(), currentDataSource.getEnteteFacture()
                            .getPassage().getDateFacturation());
            FADocumentInfoHelper.fill(getDocumentInfo(), currentDataSource.getEnteteFacture().getIdExterneFacture());
            getDocumentInfo().setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, "FAC");

            getDocumentInfo().setDocumentProperty("annee", getAnneeFromEntete(currentDataSource.getEnteteFacture()));

            getDocumentInfo().setDocumentDate(getPassage().getDateFacturation());
            getDocumentInfo().setDocumentTitle(
                    currentDataSource.getEnteteFacture().getDescriptionDecompte(
                            currentDataSource.getEnteteFacture().getISOLangueTiers()));
            if (JadeStringUtil.isEmpty(currentDataSource.getAdressePrincipale())) {
                getDocumentInfo().setRejectDocument(true);
            }
            if (!JANumberFormatter.deQuote(currentDataSource.getEnteteFacture().getTotalFacture()).equals(
                    currentDataSource.getMontantImprimer().toString())
                    && !APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(currentDataSource.getEnteteFacture()
                    .getIdTypeFacture())) {
                getDocumentInfo().setRejectDocument(true);
            }

            FAModulePassageManager modulePassageManager = new FAModulePassageManager();
            modulePassageManager.setSession(getSession());
            modulePassageManager.setForIdPassage(passage.getId());
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_DECISION_CAP_CGAS);
            boolean hasPassageModuleFacturationDecisionCAP_CGAS = modulePassageManager.getCount() >= 1;

            modulePassageManager = new FAModulePassageManager();
            modulePassageManager.setSession(getSession());
            modulePassageManager.setForIdPassage(passage.getId());
            modulePassageManager.setForIdTypeModule(FAModuleFacturation.CS_MODULE_PERIODIQUE_CAP_CGAS);
            hasPassageModuleFacturationDecisionCAP_CGAS = hasPassageModuleFacturationDecisionCAP_CGAS
                    || (modulePassageManager.getCount() >= 1);

            if (!hasPassageModuleFacturationDecisionCAP_CGAS) {
                getDocumentInfo().setDuplex((getSession().getApplication()).getProperty("gestionVerso").equals("avec"));
            }
            // Num?ro inforom selon le type de facture
            if (wantReferenceFacture) {
                int idSousType = Integer.parseInt(currentDataSource.getEnteteFacture().getIdSousType());
                String isInteret = JadeStringUtil.substring(currentDataSource.getEnteteFacture().getIdExterneFacture(),
                        6, 1);
                if ((((227001 <= idSousType) && (idSousType <= 227012))
                        || ((227040 <= idSousType) && (idSousType <= 227047)) || ((227061 <= idSousType) && (idSousType <= 227062)))
                        && !isInteret.equals("9")) {
                    if (getSession().getApplication().getProperty("plusieursTypeAffilie").toLowerCase().equals("true")) {
                        if (currentDataSource.getEnteteFacture().getIdRole().equalsIgnoreCase(rolePersonelle)) {
                            // P?riodique personelle
                            getDocumentInfo().setDocumentType(
                                    FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_PERIODIQUE_PERSONELLE);
                        } else {
                            // P?riodique paritaire
                            getDocumentInfo().setDocumentType(
                                    FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_PERIODIQUE_PARITAIRE);
                        }
                    } else {
                        // P?riodique
                        getDocumentInfo().setDocumentType(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_PERIODIQUE);
                    }
                } else {
                    if (currentDataSource.getEnteteFacture().getIdRole().equalsIgnoreCase(rolePersonelle)) {
                        // D?compte personelle
                        getDocumentInfo().setDocumentType(
                                FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PERSONELLE);
                    } else {
                        // D?compte paritaire
                        getDocumentInfo().setDocumentType(
                                FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
                    }
                }
            } else {
                getDocumentInfo().setDocumentType(FAImpressionFacture_BVR_Doc.NUM_INFOROM_FACTURE_DECOMPTE_PARITAIRE);
            }
            getDocumentInfo().setDocumentTypeNumber(getDocumentInfo().getDocumentType());
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    e.getMessage().toString(),
                    FWMessage.ERREUR,
                    getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO")
                            + currentDataSource.getEnteteFacture().getIdExterneRole());
        }
    }

    /**
     * Sets the currentDataSource.getEnteteFacture()List.
     *
     * @param entityList .getEnteteFacture()List The currentDataSource.getEnteteFacture()List to set
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

    public Boolean getIsEbusiness() {
        return isEbusiness;
    }

    public void setIsEbusiness(Boolean isEbusiness) {
        this.isEbusiness = isEbusiness;
    }

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
    }

    public static void setJasperTemplate(String templateName) {
        TEMPLATE_NAME = templateName;
    }

    public Map<PaireIdExterneEBill, List<Map>> getLignesFacture() {
        return lignesFacture;
    }

    public Map<PaireIdExterneEBill, String> getReferencesFacture() {
        return referencesFacture;
    }
}
