package globaz.ij.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CTDocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWITemplateType;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.basseindemnisation.IIJFormulaireIndemnisation;
import globaz.ij.api.codesystem.IIJCatalogueTexte;
import globaz.ij.application.IJApplication;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisation;
import globaz.ij.db.basesindemnisation.IJFormulaireIndemnisationManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.external.IntRole;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.db.tiers.TIAdministrationAdresse;
import java.io.File;
import java.rmi.RemoteException;
import java.text.FieldPosition;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJFormulaires extends FWIDocumentManager {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FICHIER_MODELE = "IJ_ATTESTATION";
    private static final String FICHIER_RESULTAT = "attestation";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    public static final String ORDER_PRINTING_BY = "orderPrintingBy";
    private ICaisseReportHelper caisseHelper;
    private String csTypeIJ = "";
    private String date;
    private String dateRetour;
    private ICTDocument document;
    private ICTDocument documentHelper;

    private IJFormulaireIndemnisation formulaire;

    private Iterator formulaires;
    private String idFormulaire = "";
    private String idPrononce = "";

    private Boolean isSendToGed = Boolean.FALSE;
    private List lignes;

    private Locale locale;
    private Boolean publishDocument = Boolean.FALSE;

    PRTiersWrapper tiers;

    public IJFormulaires() throws FWIException {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe IJDecomptes.
     * 
     * @param parent
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJFormulaires(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, IJFormulaires.FICHIER_RESULTAT);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJDecomptes.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public IJFormulaires(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, IJFormulaires.FICHIER_RESULTAT);
    }

    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
                setSendMailOnError(false);
            } else {
                setSendCompletionMail(true);
                setSendMailOnError(true);
            }

            setControleTransaction(getTransaction() == null);
        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    @Override
    public void afterBuildReport() {
        if (IIJFormulaireIndemnisation.CS_ENVOYE.equals(formulaire.getEtat())) {
            formulaire.incRappel();
        } else {
            formulaire.setEtat(IIJFormulaireIndemnisation.CS_ENVOYE);
        }

        if (JAUtil.isDateEmpty(formulaire.getDateEnvoi())) {
            formulaire.setDateEnvoi(date);
        }

        try {
            formulaire.update(getTransaction());

            super.afterBuildReport();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.AVERTISSEMENT, "IJFormulaires");
            // avertissement donc on n'avorte pas
        }
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        try {
            Map parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            // remplissage de l'entete
            CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();

            // crBean.setNomCollaborateur(getSession().getUserFullName());

            PRTiersWrapper agentExec;
            String adresse;

            try {
                // creation du helper pour les entetes et pieds de page
                caisseHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(getDocumentInfo(),
                        getSession().getApplication(),
                        getSession().getCode(formulaire.loadAgentExecution().getCsLangueAttestation()).toLowerCase());

                // si l'agent d'exécution est un tiers "normal"
                agentExec = PRTiersHelper.getTiersParId(getISession(), formulaire.loadAgentExecution().getIdTiers());

                // sinon c'est une administration
                if (agentExec == null) {
                    agentExec = PRTiersHelper.getAdministrationParId(getISession(), formulaire.loadAgentExecution()
                            .getIdTiers());
                }

                adresse = PRTiersHelper.getAdresseCourrierFormatee(getISession(), formulaire.loadAgentExecution()
                        .getIdTiers(), "", IJApplication.CS_DOMAINE_ADRESSE_IJAI);

            } catch (Exception e) {
                throw new FWIException("impossible de charger le tiers", e);
            }
            String codeIsoLangue = getSession().getCode(formulaire.loadAgentExecution().getCsLangueAttestation());
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);

            crBean.setDate(JACalendar.format(date, codeIsoLangue));

            if (JadeStringUtil.isEmpty(adresse)) {
                getMemoryLog()
                        .logMessage(
                                getSession().getLabel("ADR_COURR_AGENT_ERR") + " - "
                                        + agentExec.getProperty(PRTiersWrapper.PROPERTY_NOM), FWMessage.ERREUR,
                                "IJFormulaires");
                abort();
            }
            crBean.setAdresse(adresse); // l'adresse est celle de l'institution

            if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_NOMCOLABO))) {
                // nom du collaborateur
                crBean.setNomCollaborateur(getSession().getUserFullName());
            }
            // nom du document est celui de l'institution
            setDocumentTitle(agentExec.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " - "
                    + agentExec.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                    + agentExec.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            // creation des parametres pour l'en-tete
            // ---------------------------------------------------------------------
            crBean.setNoAvs(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));

            // Ajoute le libelle CONFIDENTIEL dans l'adresse de l'entete du
            // document
            if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DOC_CONFIDENTIEL))) {

                crBean.setConfidentiel(true);
            }

            caisseHelper.addHeaderParameters(getImporter(), crBean);

            if (!JadeStringUtil.isBlankOrZero(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS))) {
                if ("true".equals(getSession().getApplication().getProperty(IJApplication.PROPERTY_DISPLAY_NIP))) {
                    parametres.put("P_HEADER_NIP_LIB", getSession().getLabel("NIP") + " :");
                    parametres.put("P_HEADER_NIP", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                }
            }

            // le titre
            // ---------------------------------------------------------------------------------------------------
            StringBuffer buffer = new StringBuffer(document.getTextes(1).getTexte(1).getDescription());
            FWMessageFormat message = createMessageFormat(buffer);

            buffer.setLength(0);
            parametres.put(
                    "P_TITRE",
                    message.format(
                            new Object[] {
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase() + " "
                                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM) }, buffer,
                            new FieldPosition(0)).toString());

            // le corps du document
            // ---------------------------------------------------------------------------------------
            buffer.setLength(0);

            // l'intro
            // ------------------------------------------------------------------------------------------------

            ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
            Hashtable params = new Hashtable();
            params.put(ITITiers.FIND_FOR_IDTIERS, agentExec.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            ITITiers[] t = tiersTitre.findTiers(params);
            if ((t != null) && (t.length > 0)) {
                tiersTitre = t[0];
            }
            String titre = tiersTitre.getFormulePolitesse(formulaire.loadAgentExecution().getCsLangueAttestation());

            for (Iterator textes = document.getTextes(2).iterator(); textes.hasNext();) {
                ICTTexte texte = (ICTTexte) textes.next();

                // if (buffer.length() > 0) {
                // buffer.append("\n\n");
                // }

                buffer.append(texte.getDescription());
            }
            buffer = new StringBuffer(PRStringUtils.formatMessage(buffer, titre));

            parametres.put("P_INTRO", buffer.toString());

            // la première ligne
            // --------------------------------------------------------------------------------------
            buffer.setLength(0);
            buffer.append(document.getTextes(3).getTexte(1).getDescription());
            message = createMessageFormat(buffer);

            buffer.setLength(0);
            parametres.put(
                    "P_LI_1",
                    message.format(
                            new Object[] { formulaire.loadBaseIndemnisation().getDateDebutPeriode(),
                                    formulaire.loadBaseIndemnisation().getDateFinPeriode() }, buffer,
                            new FieldPosition(0)).toString());

            parametres.put("P_LI_4", document.getTextes(3).getTexte(2).getDescription());
            parametres.put("P_LI_4_OUI", document.getTextes(3).getTexte(3).getDescription());
            parametres.put("P_LI_4_NON", document.getTextes(3).getTexte(4).getDescription());

            // la seconde ligne
            // ---------------------------------------------------------------------------------------
            parametres.put("P_LI_2", document.getTextes(4).getTexte(1).getDescription());
            parametres.put("P_LI_2_OUI", document.getTextes(4).getTexte(2).getDescription());
            parametres.put("P_LI_2_NON", document.getTextes(4).getTexte(3).getDescription());
            parametres.put("P_LI_2_DU", document.getTextes(4).getTexte(4).getDescription());
            parametres.put("P_LI_2_AU", document.getTextes(4).getTexte(5).getDescription());
            parametres.put("P_LI_2_MOTIF", document.getTextes(4).getTexte(6).getDescription());
            parametres.put("P_LI_2_ARRET", document.getTextes(4).getTexte(7).getDescription());

            parametres.put("P_LI_2_REMARQUE", document.getTextes(4).getTexte(8).getDescription());

            // la 4ème ligne
            // -------------------------------------------------------------------------------------
            parametres.put("P_LI_3", document.getTextes(5).getTexte(1).getDescription());
            parametres.put("P_LI_3_OUI", document.getTextes(5).getTexte(2).getDescription());
            parametres.put("P_LI_3_NON", document.getTextes(5).getTexte(3).getDescription());

            // les paragraphes de fin
            // ---------------------------------------------------------------------------------
            buffer.setLength(0);

            for (Iterator textes = document.getTextes(6).iterator(); textes.hasNext();) {
                ICTTexte texte = (ICTTexte) textes.next();

                // if (buffer.length() > 0) {
                // buffer.append("\n");
                // }

                buffer.append(texte.getDescription());
            }

            // parametres.put("P_OUTRO", buffer.toString());

            completeOutro(parametres, buffer, document.getTextes(6));

            // ajouter la signature
            // ---------------------------------------------------------------------------------------
            try {

                caisseHelper.addSignatureParameters(getImporter());
            } catch (Exception e) {
                throw new FWIException("Impossible de charger le pied de page", e);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJFormulaires");
            abort();
        }
    }

    /**
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        // Effacer les pdf a la fin
        setDeleteOnExit(true);

        try {
            // le modele
            String extensionModelCaisse = getSession().getApplication().getProperty("extensionModelITextCaisse");
            if (!JadeStringUtil.isEmpty(extensionModelCaisse)) {
                setTemplateFile(IJFormulaires.FICHIER_MODELE + extensionModelCaisse);
                FWIImportManager im = getImporter();
                File sourceFile = new File(im.getImportPath() + im.getDocumentTemplate()
                        + FWITemplateType.TEMPLATE_JASPER.toString());
                if ((sourceFile != null) && sourceFile.exists()) {
                    ;
                } else {
                    setTemplateFile(IJFormulaires.FICHIER_MODELE);
                }
            } else {
                setTemplateFile(IJFormulaires.FICHIER_MODELE);
            }
        } catch (Exception e) {
            setTemplateFile(IJFormulaires.FICHIER_MODELE);
        }

        try {

            // langue de l'utilisateur
            locale = new Locale(getSession().getIdLangueISO(), "CH");

            // chargement des formulaires et de l'agent d'execution
            LinkedList list = new LinkedList();

            if (!JadeStringUtil.isIntegerEmpty(idFormulaire)) {
                // on cree un document unique pour un formulaire
                IJFormulaireIndemnisation formulaire = new IJFormulaireIndemnisation();

                formulaire.setIdFormulaireIndemnisation(idFormulaire);
                formulaire.setSession(getSession());
                formulaire.retrieve();

                list.add(formulaire);

            } else {
                // on cree un document pour toutes les bases d'un prononce qui
                // se trouvent dans l'etat ouvert
                // charger les bases dans l'etat ouvert.
                IJBaseIndemnisationManager bases = new IJBaseIndemnisationManager();

                bases.setForIdPrononce(idPrononce);
                bases.setForCsEtat(IIJBaseIndemnisation.CS_OUVERT);
                bases.setSession(getSession());
                bases.find();

                // charger tous les formulaires
                IJFormulaireIndemnisationManager formulairesMgr = new IJFormulaireIndemnisationManager();

                formulairesMgr.setSession(getSession());

                for (int idBase = 0; idBase < bases.size(); ++idBase) {
                    formulairesMgr.setForIdBaseIndemnisation(((IJBaseIndemnisation) bases.get(idBase))
                            .getIdBaseIndemisation());
                    formulairesMgr.find();

                    for (int idFormulaire = 0; idFormulaire < formulairesMgr.size(); ++idFormulaire) {
                        list.add(formulairesMgr.get(idFormulaire));
                    }
                }
            }

            formulaires = list.iterator();

            // la prochaine etape est la methode next()
        } catch (Exception e) {
            getMemoryLog().logMessage("impossible de lancer la generation des decomptes", FWMessage.ERREUR,
                    "IJFormulaires");
            abort();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        // getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
        return super.beforePrintDocument();
    }

    private String buildOrderPrintingByKey(String idTiers, String dateDebutPeriode, JadePublishDocumentInfo docInfo)
            throws Exception {

        String nomAgentExec = "";
        String prenomAgentExec = "";
        String npaAgentExec = "";
        boolean isAdministration = false;
        PRTiersWrapper tierWrapper = null;

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            tierWrapper = PRTiersHelper.getTiersAdresseParId(getSession(), idTiers);

            if (tierWrapper == null) {
                tierWrapper = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
                isAdministration = true;
            }

            if (tierWrapper != null) {
                nomAgentExec = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NOM).toUpperCase();
                prenomAgentExec = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_PRENOM).toUpperCase();
                if (isAdministration) {
                    TIAdministrationAdresse tiAdministrationAdresse = new TIAdministrationAdresse();
                    tiAdministrationAdresse.setSession(getSession());
                    tiAdministrationAdresse.setIdTiers(idTiers);
                    tiAdministrationAdresse.retrieve();

                    if (!tiAdministrationAdresse.isNew()) {
                        npaAgentExec = tiAdministrationAdresse.getNpa()
                                + (tiAdministrationAdresse.getNpa_sup().length() > 1 ? tiAdministrationAdresse
                                        .getNpa_sup() : "0" + tiAdministrationAdresse.getNpa_sup());
                    } else {
                        npaAgentExec = "";
                    }
                } else {
                    npaAgentExec = tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA)
                            + (tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP).length() > 1 ? tierWrapper
                                    .getProperty(PRTiersWrapper.PROPERTY_NPA_SUP) : "0"
                                    + tierWrapper.getProperty(PRTiersWrapper.PROPERTY_NPA_SUP));
                }
            } else {
                System.out.println("Oups pas de tier pour cet id!");
            }
        } else {
            System.out.println("Oups pas d'id tier!");
        }

        nomAgentExec = JadeStringUtil.fillWithSpaces(nomAgentExec, 20);
        prenomAgentExec = JadeStringUtil.fillWithSpaces(prenomAgentExec, 20);
        JADate ddp = new JADate(dateDebutPeriode);

        // System.out.println(nomAgentExec+"_"+prenomAgentExec
        // +"_"+npaAgentExec+"_"+ddp.toStrAMJ()+"-" + docInfo.getDocumentUID());

        return nomAgentExec + "_" + prenomAgentExec + "_" + npaAgentExec + "_" + ddp.toStrAMJ();
    }

    private void chargerCatalogue() throws FWIException {
        try {
            // chargement du catalogue de texte
            if (documentHelper == null) {
                documentHelper = PRBabelHelper.getDocumentHelper(getISession());
                documentHelper.setCsDomaine(IIJCatalogueTexte.CS_IJ);
                documentHelper.setCsTypeDocument(IIJCatalogueTexte.CS_ATTESTATION);
                documentHelper.setDefault(Boolean.TRUE);
                documentHelper.setActif(Boolean.TRUE);
            }

            documentHelper.setCodeIsoLangue(getSession().getCode(
                    formulaire.loadAgentExecution().getCsLangueAttestation()).toLowerCase());

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                getMemoryLog().logMessage("impossible de charger le catalogue de texte", FWMessage.ERREUR,
                        "IJFormulaires");
                abort();
            } else {
                document = documents[0];
            }
        } catch (Exception e) {
            throw new FWIException("impossible de charger le catalogue de texte", e);
        }
    }

    private void completeOutro(Map parametres, StringBuffer buffer, ICTListeTextes textes) throws Exception {
        Object[] arguments = null;

        arguments = completeOutro1(buffer, textes);

        // remplacement
        FWMessageFormat message = createMessageFormat(buffer);
        buffer.setLength(0); // on recycle
        parametres.put("P_OUTRO", message.format(arguments, buffer, new FieldPosition(0)).toString());
        parametres.put("P_DATE", document.getTextes(7).getTexte(1).getDescription());
        parametres.put("P_TIMBRE", document.getTextes(7).getTexte(2).getDescription());
    }

    private Object[] completeOutro1(StringBuffer buffer, ICTListeTextes textes) throws Exception {

        ITITiers tiersTitre = (ITITiers) getSession().getAPIFor(ITITiers.class);
        Hashtable params = new Hashtable();
        params.put(ITITiers.FIND_FOR_IDTIERS, formulaire.loadAgentExecution().getIdTiers());
        ITITiers[] t = tiersTitre.findTiers(params);
        if ((t != null) && (t.length > 0)) {
            tiersTitre = t[0];
        }
        String titre = tiersTitre.getFormulePolitesse(tiersTitre.getLangue());
        if (JadeStringUtil.isEmpty(titre)) {
            titre = getSession().getApplication().getLabel("MADAME_MONSIEUR",
                    getSession().getCode(tiersTitre.getLangue()));
        }

        // Gestion des titres provisoires en attendant la gestion par les tiers
        // !!
        // PRTiersWrapper tiers;
        // tiers = PRTiersHelper.getTiersParId(getISession(),
        // formulaire.loadAgentExecution().getIdTiers());
        // String titre =
        // PRCodeSystem.getLibelleForCS(tiers.getProperty(PRTiersWrapper.PROPERTY_TITRE),
        // getSession());
        //
        // if (JadeStringUtil.isEmpty(titre)) {
        // titre = getSession().getLabel("MADAME_MONSIEUR");
        // }
        // else {
        // titre = titre.substring(0,3);
        //
        // if (titre.startsWith("Mon") || titre.startsWith("Mad") ||
        // titre.startsWith("Mes") || titre.startsWith("Maî") ||
        // titre.startsWith("Doc")){
        // titre =
        // PRCodeSystem.getLibelleForCS(tiers.getProperty(PRTiersWrapper.PROPERTY_TITRE),
        // getSession());
        // }
        // else {
        // titre = getSession().getLabel("MADAME_MONSIEUR");
        // }
        // }

        Object[] arguments = new Object[2];

        if ((null == dateRetour) || "".equals(dateRetour)) {
            arguments[0] = "__________";
        } else {
            arguments[0] = dateRetour;
        }
        arguments[1] = titre;

        return arguments;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public void createDataSource() throws Exception {

        tiers = formulaire.loadBaseIndemnisation().loadPrononce(getTransaction()).loadDemande(getTransaction())
                .loadTiers(); // assure
        createDocInfoIjFormulaires();

        if (lignes == null) {
            lignes = new LinkedList();
            lignes.add("");
        }

        this.setDataSource(lignes);
    }

    /**
     * Set les proprietes du JadePublishDocumentInfo pour archivaghe du document dans la GED
     */
    public void createDocInfoIjFormulaires() {
        JadePublishDocumentInfo docInfo = getDocumentInfo();

        docInfo.setPublishDocument(getPublishDocument().booleanValue());
        docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEMailAddress());
        docInfo.setDocumentTitle(getSession().getLabel("DOC_FORMULAIRE_INDEMNISATION_TITLE"));
        docInfo.setDocumentDate(getDate());

        try {
            String anneeFormulaire = JADate.getYear(getDate()).toString();
            // on set les proprietes du DocInfo pour l'archivage uniquement
            // - pour les client qui possedent une GED

            docInfo.setDocumentType(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
            docInfo.setDocumentTypeNumber(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
            docInfo.setDocumentProperty("annee", anneeFormulaire);

            // BZ 7284
            docInfo.setBarcode(PRStringUtils.replaceString(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    ".", "")
                    + "-"
                    + JadeStringUtil.substring(date, 6)
                    + "-"
                    + IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);

            TIDocumentInfoHelper.fill(docInfo, tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), getSession(),
                    IntRole.ROLE_IJAI, null, null);

            docInfo.setDocumentProperty(CTDocumentInfoHelper.TYPE_DOCUMENT_ID, documentHelper.getCsTypeDocument());

            if (getIsSendToGed().booleanValue()) {
                docInfo.setArchiveDocument(true);
            } else {
                docInfo.setArchiveDocument(false);
            }

            // on ajoute au doc info le critere de tri pour les impressions
            // ORDER_PRINTING_BY
            docInfo.setDocumentProperty(
                    IJFormulaires.ORDER_PRINTING_BY,
                    buildOrderPrintingByKey(formulaire.loadAgentExecution().getIdTiers(), formulaire
                            .loadBaseIndemnisation().getDateDebutPeriode(), docInfo));

        } catch (RemoteException e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJFormulaire afterPrintDocument():" + e.toString(), FWMessage.ERREUR,
                    "IJFormulaire");
        } catch (Exception e) {
            e.printStackTrace();
            getMemoryLog().logMessage("IJFormulaire afterPrintDocument():" + e.toString(), FWMessage.ERREUR,
                    "IJFormulaire");
        }
    }

    private FWMessageFormat createMessageFormat(StringBuffer pattern) {
        // doubler les apostrophes pour eviter que MessageFormat se trompe
        for (int idChar = pattern.length(); --idChar >= 0;) {
            if (pattern.charAt(idChar) == '\'') {
                pattern.insert(idChar, '\'');
            }
        }

        // créer un formatteur pour la langue de la session
        FWMessageFormat retValue = new FWMessageFormat(pattern.toString());

        if (locale == null) {
            locale = new Locale(getSession().getIdLangueISO(), "CH");
        }

        retValue.setLocale(locale);

        return retValue;
    }

    /**
     * getter pour l'attribut cs type IJ.
     * 
     * @return la valeur courante de l'attribut cs type IJ
     */
    public String getCsTypeIJ() {
        return csTypeIJ;
    }

    /**
     * getter pour l'attribut date.
     * 
     * @return la valeur courante de l'attribut date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return
     */
    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * getter pour l'attribut id formulaire.
     * 
     * @return la valeur courante de l'attribut id formulaire
     */
    public String getIdFormulaire() {
        return idFormulaire;
    }

    /**
     * getter pour l'attribut id prononce.
     * 
     * @return la valeur courante de l'attribut id prononce
     */
    public String getIdPrononce() {
        return idPrononce;
    }

    public Boolean getIsSendToGed() {
        return isSendToGed;
    }

    public Boolean getPublishDocument() {
        if (publishDocument == null) {
            return Boolean.FALSE;
        } else {
            return publishDocument;
        }

    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    @Override
    public boolean next() throws FWIException {
        if (formulaires.hasNext()) {
            formulaire = (IJFormulaireIndemnisation) formulaires.next();
            chargerCatalogue();

            return true;
        }

        return false;
    }

    /**
     * setter pour l'attribut cs type IJ.
     * 
     * @param csTypeIJ
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeIJ(String csTypeIJ) {
        this.csTypeIJ = csTypeIJ;
    }

    /**
     * setter pour l'attribut date.
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     * 
     * @throws FWIException
     *             DOCUMENT ME!
     */
    public void setDate(String date) throws FWIException {
        this.date = date;
    }

    /**
     * @param string
     */
    public void setDateRetour(String string) {
        dateRetour = string;
    }

    /**
     * setter pour l'attribut id formulaire.
     * 
     * @param idFormulaire
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdFormulaire(String idFormulaire) {
        this.idFormulaire = idFormulaire;
    }

    /**
     * setter pour l'attribut id prononce.
     * 
     * @param idPrononce
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrononce(String idPrononce) {
        this.idPrononce = idPrononce;
    }

    // public void afterExecuteReport() {
    // JadePublishDocumentInfo docInfo = createDocumentInfo();
    // // on ajoute au doc info le numéro de référence inforom
    // getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.FORMULAIRE_BASE_INDEMNI_IJ);
    //
    // docInfo.setPublishDocument(true);
    // docInfo.setArchiveDocument(false);
    // docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO,
    // getEMailAddress());
    // docInfo.setDocumentTitle(getSession().getLabel("DOC_FORMULAIRE_INDEMNISATION_TITLE"));
    // docInfo.setDocumentDate(getDate());
    //
    // try {
    // // Pour les decomptes definitifs et les client qui possedent une GED
    // if(getIsSendToGed().booleanValue()) {
    // // on genere le doc pour impression (mail) et on set les proprietes
    // DocInfo
    // // on ne supprime pas les documents individuels car on doit les envoies à
    // la GED
    // // on trie les documents sur le critère "orderPrintBy"
    // mergePDF(docInfo, false, 0, false, ORDER_PRINTING_BY);
    // }else{
    // // on genere le doc pour impression (mail) et on set les proprietes
    // DocInfo
    // // on supprime pas les documents individuels car on ne les envoies pasà
    // la GED
    // // on trie les documents sur le critère "orderPrintBy"
    // mergePDF(docInfo, true, 0, false, ORDER_PRINTING_BY);
    // }
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // getMemoryLog().logMessage("IJFormulaire.afterExecuteReport():" +
    // e.toString(), FWMessage.ERREUR, "IJFormulaire");
    // }
    // }

    public void setIsSendToGed(Boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setPublishDocument(Boolean publishDocument) {
        this.publishDocument = publishDocument;
    }

}
