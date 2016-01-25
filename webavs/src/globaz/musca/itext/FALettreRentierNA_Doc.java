package globaz.musca.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.service.JadeUserService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.util.FAUtil;
import globaz.naos.application.AFApplication;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.db.tiers.TITiers;
import java.io.IOException;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Le document imprime les zones de facture selon les paramètres suivants: _modeRecouvrement : aucun, bvr,
 * remboursement, recouvrement direct _critereDecompte : interne, positif, note de credit, decompte zéro Date de
 * création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class FALettreRentierNA_Doc extends FAImpressionFacturation {

    private static final long serialVersionUID = 7902546287990826383L;
    public final static String NUM_REF_INFOROM_LETTRE_RENTIER_NA = "0181CFA";
    protected final static String TEMPLATE_FILENAME = "MUSCA_LETTRE_RENTIERNA";
    protected java.lang.String adresseDomicile;
    protected java.lang.String adressePrincipalePaiement;
    private int compt = 0;
    ICTDocument[] document = null;
    private String idPassage = null;
    private String libelleInteret;
    private FAEnteteFactureManager manager;
    private String signataire1 = new String();
    private String signataire2 = new String();
    private BStatement statement = null;
    private TITiers tiers = null;

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:56:39)
     */
    public FALettreRentierNA_Doc() throws Exception {
        this(new BSession(FAApplication.DEFAULT_APPLICATION_MUSCA));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FALettreRentierNA_Doc(BProcess parent) throws java.lang.Exception {
        super(parent, FAApplication.APPLICATION_MUSCA_REP, "LETTRERENTIERNA");
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_LETTRE_RENTIERNA"));
        super.setSendCompletionMail(false);

    };

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public FALettreRentierNA_Doc(BSession session) throws java.lang.Exception {
        super(session, FAApplication.APPLICATION_MUSCA_REP, "LETTRERENTIERNA");
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_LETTRE_RENTIERNA"));
        super.setSendCompletionMail(false);
    }

    /**
     * Insert the method's description here. Creation date: (05.06.2003 08:55:49)
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText(CaisseHeaderReportBean headerBean) {

        try {
            document = getICTDocument();
            JadeUser user = null;
            JadeUserService service = JadeAdminServiceLocatorProvider.getLocator().getUserService();

            if (passage != null) {
                user = service.loadForVisa(passage.getPersonneRef());
            }
            // adresse du tiers
            headerBean.setAdresse(adressePrincipalePaiement);

            // texte de la date
            if (getPassage() != null) {
                headerBean.setDate(JACalendar.format(getPassage().getDateFacturation(), entity.getISOLangueTiers()));
            }

            // numéro AVS
            if ("".equals(getEntity().getNumeroAVSTiers(getTransaction()))) {
                headerBean.setNoAvs(" "); // PO 10337
            } else {
                headerBean.setNoAvs(getEntity().getNumeroAVSTiers(getTransaction()));
            }

            if (user != null) {
                headerBean.setNomCollaborateur(user.getFirstname() + " " + user.getLastname());
                headerBean.setTelCollaborateur(user.getPhone());
                headerBean.setUser(user);
            }

            // No affilié
            headerBean.setNoAffilie(getEntity().getIdExterneRole());

            // Partie concerne du document
            super.setParametres(FWIImportParametre.PARAM_TITLE, getTexte(1, document));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: " + e.getMessage());

        }

    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _letterBody() {

        try {
            tiers = getTiers();
            // On set le texte du haut du document
            super.setParametres(
                    FADetailInteretMoratoire_Param.P_TITLE2,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(2, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));
            super.setParametres(
                    FADetailInteretMoratoire_Param.PARAM_TEXTTOP,
                    FWMessageFormat.format(FAUtil.prepareQuotes(getTexte(3, document), false),
                            FAUtil.prepareQuotes(tiers.getFormulePolitesse(tiers.getLangue()), false)));

        } catch (Exception e) {
            this._addError("Erreur lors de la création du Header du détail d'un intérêt: " + e.getMessage());
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:33)
     */
    protected void _summaryText() {
        try {
            super.setParametres(FADetailInteretMoratoire_Param.PARAM_TEXTSIGN, getTexte(4, document));
            super.setParametres(FADetailInteretMoratoire_Param.PARAM_TEXTBOTTOM, getTexte(5, document));
        } catch (Exception e) {
            this._addError("Erreur lors de la création du text du détail d'un intérêt: " + e.getMessage());
        }

    }

    /**
     * Retourne la décision ou null en cas d'exception Insérez la description de la méthode ici. Date de création :
     * (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        super.setSendMailOnError(false);
        super.setDocumentTitle(entity.getIdExterneRole() + " - " + entity.getNomTiers());
    }

    @Override
    public final void beforeExecuteReport() {
        setImpressionParLot(true);
        setTailleLot(500);
        manager = new FAEnteteFactureManager();
        manager.setSession(getSession());
        manager.setForEstRentierNa(new Boolean(true));
        manager.setForIdPassage(getIdPassage());
        // manager.find();
        try {
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
        } finally {
        }

        // Initialise le document pour le catalogue de texte
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2003 14:07:16)
     */
    @Override
    public void createDataSource() throws Exception {

        if (getPassage() == null && !JadeStringUtil.isBlankOrZero(entity.getIdPassage())) {
            try {
                passage = new FAPassage();
                passage.setISession(getSession());
                passage.setIdPassage(entity.getIdPassage());
                passage.retrieve(getTransaction());
            } catch (Exception e) {
                throw new Exception("Impossible d'extraire le passage de facturation");
            }
        }

        // En tout premier lieu, il est nécessaire de renseigner le docInfo !
        fillDocInfo();

        FAUtil.fillDocInfoWithPassageInfo(getDocumentInfo(), getPassage());

        super.setTemplateFile(FALettreRentierNA_Doc.TEMPLATE_FILENAME);

        // initialiser les variables d'aide
        adressePrincipalePaiement = entity.getAdressePrincipale(getTransaction(), globaz.globall.util.JACalendar
                .today().toStr("."));
        adresseDomicile = entity.getAdresseDomicile(getTransaction(), JACalendar.today().toStr("."));

        // Vérifier l'id de l'entête
        if (JadeStringUtil.isIntegerEmpty(entity.getIdEntete())) {
            return;
        }

        // Get Parameters
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), entity.getISOLangueTiers());

        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        _headerText(headerBean);
        _letterBody();
        _summaryText();

        caisseReportHelper.addHeaderParameters(this, headerBean);

    }

    private void fillDocInfo() {
        getDocumentInfo().setDocumentTypeNumber(FALettreRentierNA_Doc.NUM_REF_INFOROM_LETTRE_RENTIER_NA);
        String numAff = getEntity().getIdExterneRole();
        String idTiers = getEntity().getIdTiers();
        getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", affilieFormater.unformat(numAff));
            TIDocumentInfoHelper.fill(getDocumentInfo(), idTiers, getSession(), ITIRole.CS_AFFILIE, numAff,
                    affilieFormater.unformat(numAff));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
        }
        if (getEntity().isNonImprimable().booleanValue()) {
            getDocumentInfo().setSeparateDocument(true);
        }
        if (getPassage() != null) {
            getDocumentInfo().setDocumentProperty("document.date", getPassage().getDateFacturation());
        }

        // Pour la mise en GED unitaire
        if (isMettreEnGed()) {
            getDocumentInfo().setPublishDocument(false);
            getDocumentInfo().setArchiveDocument(true);
        }
    }

    private boolean isMettreEnGed() {
        try {
            return "true".equals(getSession().getApplication().getProperty(FAApplication.PROPERTY_MISE_EN_GED));
        } catch (Exception e) {
            JadeLogger.error(this, "Unabled to retrieve properties '" + FAApplication.PROPERTY_MISE_EN_GED + "'");
            return false;
        }
    }

    // Retourne le document à utiliser
    private ICTDocument[] getICTDocument() {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWViewBeanInterface.ERROR, "Error while api for document");
        }
        // On charge le document
        document.setISession(getSession());
        document.setCsDomaine(FAImpressionFacturation.DOMAINE_FACTURATION);
        document.setCsTypeDocument(FAImpressionFacturation.TYPE_LETTRE);
        document.setCodeIsoLangue(entity.getISOLangueTiers());
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWViewBeanInterface.ERROR, "Error while getting document");
        }
        return res;
    }

    public String getIdPassage() {
        return idPassage;
    }

    /**
     * @return
     */
    public String getLibelleInteret() {
        return libelleInteret;
    }

    public String getSignataire1() {
        return signataire1;
    }

    public String getSignataire2() {
        return signataire2;
    }

    // Retourne le texte du niveau entré en paramètres
    private String getTexte(int niveau, ICTDocument[] document) throws Exception {
        String resString = "";
        ICTTexte texte = null;
        // Si le document est null, on retourne un message d'erreur
        if (document == null) {
            getMemoryLog().logMessage("Il n'y a pas de document par défaut", FWViewBeanInterface.ERROR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On charge la liste de textes du niveau donné
            try {
                listeTextes = document[0].getTextes(niveau);
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWViewBeanInterface.ERROR,
                        "Error while getting listes de textes");
            }
            // S'il n'y a pas de texte on retourne une erreur
            if (listeTextes == null) {
                getMemoryLog().logMessage("Il n'y a pas de texte", FWViewBeanInterface.ERROR, "");
            } else {
                // Dans le cas ou on a un texte, on va parcourir toutes les
                // positions de ce texte
                for (int i = 0; i < listeTextes.size(); i++) {
                    // On charge le texte de la position donnée
                    texte = listeTextes.getTexte(i + 1);
                    // On regarde si c'est la dernière position du niveau
                    if (i + 1 < listeTextes.size()) {
                        resString = resString.concat(texte.getDescription() + "\n\n");
                    } else {
                        // Dans le cas ou c'est la dernière position du niveau,
                        // on n'ajoute aucun retour à la ligne ni paragraphe
                        resString = resString.concat(texte.getDescription());
                    }
                }
            }
        }
        return resString;
    }

    public TITiers getTiers() {
        try {
            if ((entity != null) && !JadeStringUtil.isBlankOrZero(entity.getIdTiers())) {
                tiers = new TITiers();
                tiers.setSession(entity.getSession());
                tiers.setIdTiers(entity.getIdTiers());
                tiers.retrieve();
            }
        } catch (Exception e) {
            this._addError("Erreur lors du retrieve du tiers pour la lettre pour rentier: " + e.getMessage());
        }
        return tiers;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        try {

            if (((entity = (FAEnteteFacture) manager.cursorReadNext(statement)) != null) && (!entity.isNew())) {
                compt++;
                setProgressDescription(entity.getIdExterneRole() + " <br>" + compt + "/" + manager.size() + "<br>");
                if (isAborted()) {
                    setProgressDescription("Traitement interrompu<br> sur l'affilié : " + entity.getIdExterneRole()
                            + " <br>" + compt + "/" + manager.size() + "<br>");
                    if ((getParent() != null) && getParent().isAborted()) {
                        getParent().setProcessDescription(
                                "Traitement interrompu<br> sur l'affilié : " + entity.getIdExterneRole() + " <br>"
                                        + compt + "/" + manager.size() + "<br>");
                    }
                    return false;
                } else {
                    // ---------------------------------------------------------------------
                    factureImpressionNo++;
                    // ---------------------------------------------------------------------
                    return true;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getTransaction(),
                    "Exception dans la méthode next de la class CPImpressionCommunicationRetourDetailFiscVD_Doc.java");
            throw new FWIException(e);
        }
        return false;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    /**
     * @param string
     */
    public void setLibelleInteret(String string) {
        libelleInteret = string;
    }

    public void setSignataire1(String signataire1) {
        this.signataire1 = signataire1;
    }

    public void setSignataire2(String signataire2) {
        this.signataire2 = signataire2;
    }

    @Override
    public boolean beforePrintDocument() {
        super.DocumentSort();

        JadePublishDocumentInfo docInfo = createDocumentInfo();
        docInfo.setDocumentTypeNumber(FALettreRentierNA_Doc.NUM_REF_INFOROM_LETTRE_RENTIER_NA);
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        docInfo.setDocumentSubject(getSubject());
        docInfo.setDocumentNotes(getSubjectDetail());

        try {
            this.mergePDF(docInfo, !isMettreEnGed(), 0, true, null, null);
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        return false;
    }

    @Override
    public void afterBuildReport() {
        try {
            returnDocument();
            super.registerAttachedDocument(getDocumentInfo(), getExporter().getExportNewFilePath());
            deleteAllDocument();
        } catch (IOException e) {
            JadeLogger.error(this, e);
        } catch (FWIException e) {
            JadeLogger.error(this, e);
        }
        super.afterBuildReport();
    }

}
