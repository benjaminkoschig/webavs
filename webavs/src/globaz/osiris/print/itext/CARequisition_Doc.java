package globaz.osiris.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.CADocumentInfoHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIScriptDocument;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.admin.user.bean.JadeUserDetail;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.contentieux.CAEcritureCompteCourantCotSoumisInt;
import globaz.osiris.db.contentieux.CAEcritureRubriqueCotSoumisInt;
import globaz.osiris.db.contentieux.CAEvenementContentieux;
import globaz.osiris.db.contentieux.CAEvenementContentieuxForEtapeManager;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CATiersUtil;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

/**
 * Cette Classe permet d'imprimer le document pour une requisition de poursuite
 * 
 * @author sda
 * @author sch
 */
public class CARequisition_Doc extends FWIScriptDocument {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String MSG_ERROR_API_DOCUMENT = "Error while api for document";
    private static final String MSG_ERROR_GETTING_DOCUMENT = "Error while getting document";
    private static final String SAUT_PARAGRAPHE = "\n\n";
    private static final String TEMPLATE_DOCUMENT = "CARequisitionContentieux";
    public static final String USERDETAIL_PHONE = "Phone";
    private static final String ZERO = "0.00";

    private ICTDocument document = null;
    // Variables pour la recherche des textes pour le document
    private ICTDocument[] documents = null;
    private CARequisition requisition = null;

    private Iterator requisitionIt = null;
    private ICTDocument res[] = null;
    private String responsable = null;

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     */
    public CARequisition_Doc() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BProcess
     */
    public CARequisition_Doc(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "RequisitionContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_REQUISITION"));
    }

    /**
     * Commentaire relatif au constructeur CAProcessImprJournalContentieuxCSC.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CARequisition_Doc(BSession session) throws FWIException {
        super(session, CAApplication.DEFAULT_OSIRIS_ROOT, "RequisitionContentieux");
        super.setDocumentTitle(getSession().getLabel("FILENAME_CTX_REQUISITION"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#afterBuildReport ()
     */
    @Override
    public void afterBuildReport() {
        if (!requisition.isModePrevisionnel().booleanValue()) {
            // On sette la date du document
            getDocumentInfo().setDocumentDate(requisition.getDateDocument());
            // On dit qu'au début on ne veut pas envoyer un mail avec le
            // document
            getDocumentInfo().setPublishDocument(false);
            try {
                getSession().setApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS);
            } catch (Exception e2) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_SETTING_APPLICATION"));
            }

            try {
                CAApplication app = (CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS);
                IFormatData affilieFormater = app.getAffileFormater();
                // On rempli le documentInfo avec les infos du document
                TIDocumentInfoHelper.fill(getDocumentInfo(), requisition.getSection().getCompteAnnexe().getIdTiers(),
                        getSession(), requisition.getSection().getCompteAnnexe().getIdRole(), requisition.getSection()
                                .getCompteAnnexe().getIdExterneRole(),
                        affilieFormater.unformat(requisition.getSection().getCompteAnnexe().getIdExterneRole()));
                CADocumentInfoHelper.fill(getDocumentInfo(), requisition.getSection());

            } catch (Exception e1) {
                getMemoryLog().logMessage("", FWMessage.ERREUR, getSession().getLabel("ERROR_UNFORMATING_NUM_AFFILIE"));
            }
            try {
                getDocumentInfo().setDocumentProperty("babel.type.id", "CTX");
                getDocumentInfo().setDocumentType(document.getCsTypeDocument());
                // On dit qu'on veut archiver le document dans la GED
                getDocumentInfo().setArchiveDocument(true);
                getDocumentInfo().setPublishDocument(false);
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR,
                        getSession().getLabel("GED_ERROR_GETTING_TIERS_INFO"));
            }
        }
    }

    /**
     * Après la création de tous les documents Par defaut ne fait rien
     */
    @Override
    public void afterExecuteReport() {
        if (!requisition.isModePrevisionnel().booleanValue()) {
            try {
                JadePublishDocumentInfo info = createDocumentInfo();
                // Envoie un e-mail avec les pdf fusionnés
                info.setArchiveDocument(false);
                info.setPublishDocument(true);
                this.mergePDF(info, false, 200, false, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        APIEtape etape = getEtape();
        if (etape.getTypeEtape().equals(APIEtape.POURSUITE)) {
            getDocumentInfo().setDocumentTypeNumber("0139GCA");
        } else if (etape.getTypeEtape().equals(APIEtape.CONTINUER)) {
            getDocumentInfo().setDocumentTypeNumber("0140GCA");
        } else {
            getDocumentInfo().setDocumentTypeNumber("0141GCA");
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
        setTemplateFile(CARequisition_Doc.TEMPLATE_DOCUMENT);
        setImpressionParLot(true);

        // On va initialiser les documents
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, CARequisition_Doc.MSG_ERROR_API_DOCUMENT);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument ()
     */
    @Override
    public boolean beforePrintDocument() {
        return ((super.size() > 0) && !super.isAborted());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    @Override
    public void bindData(String arg0) throws Exception {
    }

    /**
     * Date de création : (15.05.2003 07:17:28)
     * 
     * @param data
     *            java.lang.Object
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void bindObject(Object data) throws java.lang.Exception {
        requisitionIt = ((Vector) data).iterator();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document <br>
     * 1) addRow (si nécessaire) <br>
     * 2) Appèle des méthodes pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {
        try {
            getDocumentInfo().setTemplateName(CARequisition_Doc.TEMPLATE_DOCUMENT);
            requisition = (CARequisition) requisitionIt.next();
            if (requisition.isModePrevisionnel().booleanValue()) {
                setTailleLot(500);
            } else {
                setTailleLot(1);
            }
            // On récupère les documents du catalogue de textes nécessaires
            documents = getICTDocument();
            initHeader();
            // Paramètres
            super.setParametres(CARequisitionParam.PARAM_TITRE, getTexte(1, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_OFFICE, getTexte(2, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_ADRESSE_DEBITEUR, getAdresseString());
            super.setParametres(CARequisitionParam.PARAM_ADRESSE_CREANCIER, getTexte(3, 3).toString());

            // Récuperer le montant des rubriques de cotisations qui sont
            // soumises à intérêt pour la section en cours
            BigDecimal montant = getMontantEcritureCotRubriqueSoumisInt(getSession(), requisition.getSection()
                    .getIdSection());
            // Récupérer le montant des pmtCmp (standard avec montant < 0, cc
            // débiteur, cc créancier)
            BigDecimal pmtCmpSoumisInt = getPmtCmpEcritureCotRubriqueSoumisInt(getSession(), requisition.getSection()
                    .getIdSection());
            if (pmtCmpSoumisInt.doubleValue() < 0) {
                // On soustrait les paiements effectués
                montant = montant.add(pmtCmpSoumisInt);
            }
            BigDecimal soldeSection = new BigDecimal(requisition.getSection().getSolde());
            BigDecimal montantSansInteret = soldeSection.subtract(montant);
            // On enlève les avances de frais pour les étapes "continuer la
            // poursuite" et "réquisition de vente"
            APIEtape etape = getEtape();
            if (etape.getTypeEtape().equals(APIEtape.VENTE) || etape.getTypeEtape().equals(APIEtape.CONTINUER)) {
                if (!JadeStringUtil.isDecimalEmpty(requisition.getSection().getFrais())) {
                    BigDecimal avanceFrais = new BigDecimal(requisition.getSection().getFrais());

                    // Vérifier que les frais soient positifs
                    if (avanceFrais.signum() == 1) {
                        montantSansInteret = montantSansInteret.subtract(avanceFrais);
                    }
                }
            }

            boolean isNegatif = false;
            try {
                JAUtil.checkNumberPositiveOrZero(montant);
            } catch (Exception e) {
                isNegatif = true;
            }
            if (isNegatif) {
                montantSansInteret = soldeSection;
                montant = new BigDecimal(CARequisition_Doc.ZERO);
            }
            super.setParametres(CARequisitionParam.PARAM_MONTANT, JANumberFormatter.format(montant.toString()) + " "
                    + getLibelleMontantCreance1().toString() + " " + getDateDebutInteretsTardifs(requisition));
            super.setParametres(CARequisitionParam.PARAM_INTERET, JANumberFormatter.format(montantSansInteret) + " "
                    + getLibelleMontantCreance2().toString());
            super.setParametres(CARequisitionParam.PARAM_TITRE_CREANCE, getTexteTitreCreance().toString());
            super.setParametres(CARequisitionParam.PARAM_PS, getTexte(4, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_PRIVILEGE_LEGAL, getTexte(5, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_SIGNATURE, getTexte(6, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_POURSUITE, getTexte(7, 1).toString());
            super.setParametres(CARequisitionParam.PARAM_PARVENUE_OP, getTexte(7, 2).toString());
            // Labels
            super.setParametres(CARequisitionParam.LABEL_DEBITEUR, getTexte(3, 1).toString());
            super.setParametres(CARequisitionParam.LABEL_CREANCIER, getTexte(3, 2).toString());
            super.setParametres(CARequisitionParam.LABEL_MONTANT_CREANCE, getTexte(3, 4).toString());
            super.setParametres(CARequisitionParam.LABEL_TITRE_CREANCE, getTexte(3, 5).toString());
            // super.setParametres(CARequisitionParam.PARAM_DATE,
            // requisition.getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        headerText();
    }

    /**
     * Formate le texte. Ex: Remplace un {0} par la date d'échéance
     * 
     * @param paragraphe
     * @return
     * @throws Exception
     */
    private StringBuffer format(StringBuffer paragraphe) {
        StringBuffer res = new StringBuffer("");
        try {
            for (int i = 0; i < paragraphe.length(); i++) {
                if (paragraphe.charAt(i) != '{') {
                    res.append(paragraphe.charAt(i));
                } else if (paragraphe.charAt(i + 1) == '0') {
                    /**
                     * Va chercher l'office des poursuites correspondant à l'afffilié par liens entre tiers.
                     */
                    res.append(CATiersUtil.getLibelleOfficeDesPoursuitesLong(getSession(), requisition.getTiers(),
                            requisition.getNoCompte()));
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '1') {
                    res.append(requisition.getNoFacture());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '2') {
                    res.append(requisition.getNoCompte());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '3') {
                    res.append(requisition.getDateSection() + ".");
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '4') {
                    CAEvenementContentieux evSommation = getEvenementContentieuxForEtape(getSession(), requisition
                            .getSection().getIdSection(), requisition.getParametreEtape().getIdSequenceContentieux(),
                            APIEtape.SOMMATION);
                    res.append(evSommation.getDateExecution() + ".");
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '5') {
                    res.append(requisition.getTaxe());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '6') {
                    res.append(" " + requisition.getSection().getNumeroPoursuite());
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '7') {
                    APIParametreEtape parEtape = requisition.getParametreEtape();
                    JACalendar dateDeclenchement = new JACalendarGregorian();
                    res.append(dateDeclenchement.addDays(parEtape.getDateDeclenchement(requisition.getSection()), -30));
                    i = i + 2;
                } else if (paragraphe.charAt(i + 1) == '8') {
                    FWParametersUserCode codeSysteme = new FWParametersUserCode();
                    codeSysteme.setSession(getSession());
                    codeSysteme.setIdCodeSysteme(requisition.getAdresseCourrier().getIdCanton());
                    codeSysteme.setIdLangue(transformLangueTiers(requisition.getTiers().getLangueISO()));
                    codeSysteme.retrieve();
                    res.append(codeSysteme.getLibelle());
                    i = i + 2;
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return res;
    }

    /**
     * Retourne l'adresse pour une réquisition.<br>
     * Ne fait aucune distinction entre une personne physique ou morale.
     * <ol>
     * <li>Prend l'adresse du domaine <b>contentieux</b> si elle existe.</li>
     * <li>Prend l'adresse de <b>domicile</b> si pas d'adresse de contentieux.</li>
     * <li>Si aucune autre adresse, prend l'adresse de <b>courrier</b>.</li>
     * </ol>
     * 
     * @return java.lang.String
     * @throws Exception
     */
    private String getAdresseString() throws Exception {
        String adresse = "";

        try {
            if (requisition.getSection().getCompteAnnexe() == null) {
                // appel page neuve
                return "";
            }
            String domaine = requisition.getSection().getCompteAnnexe()._getDefaultDomainFromRole();
            adresse = requisition.getTiers()
                    .getAdresseAsString(getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                            IConstantes.CS_APPLICATION_CONTENTIEUX,
                            requisition.getSection().getCompteAnnexe().getIdExterneRole(),
                            JACalendar.today().toStr("."), false);

            if (JadeStringUtil.isBlank(adresse)) {
                adresse = requisition.getTiers().getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_CONTENTIEUX, "",
                        JACalendar.today().toStr("."), false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = requisition.getTiers().getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT,
                        requisition.getSection().getCompteAnnexe().getIdExterneRole(), JACalendar.today().toStr("."),
                        false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = requisition.getTiers().getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, IConstantes.CS_APPLICATION_DEFAUT, "",
                        JACalendar.today().toStr("."), false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = requisition.getTiers().getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine,
                        requisition.getSection().getCompteAnnexe().getIdExterneRole(), JACalendar.today().toStr("."),
                        false);
            }
            if (JadeStringUtil.isBlank(adresse)) {
                adresse = requisition.getTiers().getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine, "", JACalendar.today().toStr("."), false);
            }

            return adresse;
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return "";
    }

    /**
     * Récupère la date de début des intérêts moratoires tardifs pour une section / facture
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return la date de début des intérêts moratoires tardifs
     * @throws Exception
     */
    private String getDateDebutInteretsTardifs(CARequisition requisition) throws Exception {
        JADate dateDebutInteret = new JADate();

        // Inforom321 : Suppression du test
        // CAAttentePaiementManager manager = this.hasInteretAttentePaiement(requisition.getSession(), requisition
        // .getSession().getCurrentThreadTransaction(), requisition.getSection().getIdSection());
        // if ((manager != null) && !manager.isEmpty()) {
        // CADetailInteretMoratoireManager detailManager = this.getDetailCalculInteret25(requisition.getSession(),
        // requisition.getSession().getCurrentThreadTransaction(), manager);
        //
        // if (!manager.isEmpty()) {
        // dateDebutInteret = new JADate(
        // ((CADetailInteretMoratoire) detailManager.getFirstEntity()).getDateDebut());
        // }
        //
        // } else {
        CAInteretTardif interet = CAInteretTardifFactory.getInteretTardif(requisition.getSection()
                .getCategorieSection());
        if (interet != null) {
            // Y-a-t'il une rubrique soumise à intérêt dans la section ?
            FWCurrency montantSoumis = CAInteretUtil.getMontantSoumisParPlans(getTransaction(), requisition
                    .getSection().getIdSection(), null, null);

            if ((montantSoumis != null) && montantSoumis.isPositive()) {
                interet.setIdSection(requisition.getSection().getIdSection());
                dateDebutInteret = interet.getDateCalculDebutInteret(requisition.getSession(), requisition.getSession()
                        .getCurrentThreadTransaction());
            }
        }
        // }
        return JACalendar.format(dateDebutInteret);
    }

    /**
     * Inforom321 - suppression de la méthode <br>
     * Return le détail du calcul de l'intérêt 25%. Inforom321 : méthode supprimée
     * 
     * @param session
     * @param transaction
     * @param manager
     * @return
     * @throws Exception
     */
    // private CADetailInteretMoratoireManager getDetailCalculInteret25(BSession session, BTransaction transaction,
    // CAAttentePaiementManager manager) throws Exception {
    // CAAttentePaiement interet25 = (CAAttentePaiement) manager.getFirstEntity();
    // CADetailInteretMoratoireManager detailManager = new CADetailInteretMoratoireManager();
    // detailManager.setSession(session);
    // detailManager.setForIdInteretMoratoire(interet25.getIdInteretMoratoire());
    // detailManager.find(transaction);
    //
    // if (manager.hasErrors()) {
    // throw new Exception(manager.getErrors().toString());
    // }
    //
    // return detailManager;
    // }

    /**
     * @return
     */
    private APIEtape getEtape() {
        APIParametreEtape etapePar = requisition.getParametreEtape();
        APIEtape etape = etapePar.getEtape();
        return etape;
    }

    /**
     * Cette méthode permet de récupérer un événement contentieux pour une étape spécifique. <br>
     * Il faut renseigner les informations suivantes.
     * 
     * @param session
     * @param idSection
     * @param idSeqCon
     * @param typeEtape
     * @return CAEvenementContentieux, retourne null en cas d'erreur
     * @throws Exception
     */
    private CAEvenementContentieux getEvenementContentieuxForEtape(BSession session, String idSection, String idSeqCon,
            String typeEtape) throws Exception {
        CAEvenementContentieux evCont = null;
        CAEvenementContentieuxForEtapeManager evEtapeMan = new CAEvenementContentieuxForEtapeManager();
        evEtapeMan.setISession(session);
        evEtapeMan.setForIdSection(idSection);
        evEtapeMan.setForIdSeqCon(idSeqCon);
        evEtapeMan.setForTypeEtape(typeEtape);
        evEtapeMan.find(0);
        if (evEtapeMan.isEmpty()) {
            throw new Exception("Pas de sommation trouvée pour cet id de section" + idSection);
        }
        evCont = (CAEvenementContentieux) evEtapeMan.getFirstEntity();
        return evCont;
    }

    /**
     * Récupère le document permettant d'ajouter les textes au document
     * 
     * @return
     */
    private ICTDocument[] getICTDocument() {
        document.setISession(getSession());
        document.setCsDomaine(CACodeSystem.CS_DOMAINE_CA);
        APIEtape etape = getEtape();
        if (etape.getTypeEtape().equals(APIEtape.POURSUITE)) {
            document.setCsTypeDocument(CACodeSystem.CS_TYPE_REQUISITION_POURSUITE);
        } else if (etape.getTypeEtape().equals(APIEtape.CONTINUER)) {
            document.setCsTypeDocument(CACodeSystem.CS_TYPE_REQUISITION_SAISIE);
        } else {
            document.setCsTypeDocument(CACodeSystem.CS_TYPE_REQUISITION_VENTE);
        }
        document.setDefault(new Boolean(true));
        document.setCodeIsoLangue(requisition.getTiers().getLangueISO());
        document.setActif(new Boolean(true));
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, CARequisition_Doc.MSG_ERROR_GETTING_DOCUMENT);
        }
        return res;
    }

    /**
     * Affiche le texte pour le 1er libellé du montant de la créance
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getLibelleMontantCreance1() throws Exception {
        StringBuffer resString = new StringBuffer("");
        // Si le document est vide, on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On affiche le texte qui est au niveau 9
            try {
                listeTextes = documents[0].getTextes(9);
                resString.append(listeTextes.getTexte(95));
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
    }

    /**
     * Affiche le texte pour le 1er libellé du montant de la créance
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getLibelleMontantCreance2() throws Exception {
        StringBuffer resString = new StringBuffer("");
        // Si le document est vide, on génère une erreur
        if (document == null) {
            getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
        } else {
            ICTListeTextes listeTextes = null;
            // On affiche le texte qui est au niveau 9
            try {
                listeTextes = documents[0].getTextes(9);
                resString.append(listeTextes.getTexte(96));
            } catch (Exception e3) {
                getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                        getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
            }
        }
        return resString;
    }

    /**
     * Cette méthode retourne la somme des écritures (E%) de type cotisation(avec et sans masse) comptabilisées de la
     * section qui sont soumises aux intérêts
     * 
     * @param session
     * @param idSection
     * @return BigDecimal somme des écritures soumises aux intérêts
     */
    private BigDecimal getMontantEcritureCotRubriqueSoumisInt(BSession session, String idSection) throws Exception {
        BigDecimal montantSoumisInt = new BigDecimal(CARequisition_Doc.ZERO);
        CAEcritureRubriqueCotSoumisInt ecMan = new CAEcritureRubriqueCotSoumisInt();
        ecMan.setSession(session);
        ecMan.setForIdSection(idSection);
        ecMan.find(0);
        if (!ecMan.isEmpty()) {
            for (int i = 0; i < ecMan.size(); i++) {
                CAEcriture ec = (CAEcriture) ecMan.getEntity(i);
                montantSoumisInt = montantSoumisInt.add(new BigDecimal(ec.getMontant()));
            }
        }
        return montantSoumisInt;
    }

    /**
     * Cette méthode retourne la somme des pmtCmp (E%) de type cotisation(avec et sans masse) ainsi que les pmtcmp (E%)
     * de type standard qui on un montant < 0 et qui sont comptabilisées de la section qui sont soumises aux intérêts
     * 
     * @param session
     * @param idSection
     * @return BigDecimal somme des écritures soumises aux intérêts
     */
    private BigDecimal getPmtCmpEcritureCotRubriqueSoumisInt(BSession session, String idSection) throws Exception {
        BigDecimal montantSoumisInt = new BigDecimal(CARequisition_Doc.ZERO);
        CAEcritureCompteCourantCotSoumisInt ecMan = new CAEcritureCompteCourantCotSoumisInt();
        ecMan.setSession(session);
        ecMan.setForIdSection(idSection);
        ecMan.find(0);
        if (!ecMan.isEmpty()) {
            for (int i = 0; i < ecMan.size(); i++) {
                CAEcriture ec = (CAEcriture) ecMan.getEntity(i);
                montantSoumisInt = montantSoumisInt.add(new BigDecimal(ec.getMontant()));
            }
        }
        return montantSoumisInt;
    }

    /**
     * Retourne l'alias du responable Date de création : (02.05.2003 11:59:50)
     * 
     * @return java.lang.String
     */
    private String getResponsable() {
        // Si responsable est vide, on charge le responsable
        if (JadeStringUtil.isBlank(responsable)) {
            responsable = "";
            try {
                JadeUser user = JadeAdminServiceLocatorProvider.getLocator().getUserService()
                        .load(getSession().getUserId());
                if (user != null) {
                    responsable = user.getFirstname() + " " + user.getLastname() + "\n";
                    JadeUserDetail detail = null;
                    try {
                        detail = JadeAdminServiceLocatorProvider.getLocator().getUserDetailService()
                                .load(user.getIdUser(), FWSecureConstants.USER_DETAIL_PHONE);
                    } catch (Exception e) {
                        detail = null;
                    }
                    if (detail != null) {
                        responsable += detail.getValue();
                    }
                }
            } catch (Exception e) {
                // si on ne trouve pas de responsable, ce n'est pas grave...
            }
        }
        return responsable;
    }

    /**
     * Récupère tout le texte du haut du document (va prendre les textes du catalogue de texte)
     * 
     * @return
     * @throws Exception
     */
    private StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            if (document == null) {
                getMemoryLog().logMessage(getSession().getLabel("PAS_TEXTE_DEFAUT"), FWMessage.ERREUR, "");
            } else {
                ICTListeTextes listeTextes = documents[0].getTextes(niveau);
                resString.append(listeTextes.getTexte(position));
            }
        } catch (Exception e3) {
            getMemoryLog()
                    .logMessage(e3.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_GETTING_LIST_TEXT"));
        }
        return format(resString);
    }

    /**
     * @return le texte pour le titre de la créance
     */
    private StringBuffer getTexteTitreCreance() {
        StringBuffer res = new StringBuffer("");
        res.append(getTexte(3, 6));
        res.append(CARequisition_Doc.SAUT_PARAGRAPHE);
        res.append(getTexte(3, 7));
        APIEtape etape = getEtape();
        if (!etape.getTypeEtape().equals(APIEtape.VENTE)) {
            res.append(CARequisition_Doc.SAUT_PARAGRAPHE);
            res.append(getTexte(3, 8));
        }
        return res;
    }

    /**
     * Inforom321 : méthode supprimée<br>
     * La section du contentieux en cours contient-elle un intérêt moratoire pour 25% de différence en attente de
     * paiement ?
     * 
     * @param session
     * @param transaction
     * @return
     */
    // private CAAttentePaiementManager hasInteretAttentePaiement(BSession session, BTransaction transaction,
    // String idSection) throws Exception {
    // CAAttentePaiementManager manager = new CAAttentePaiementManager();
    // manager.setSession(session);
    // manager.setForIdSection(idSection);
    // manager.find(transaction, BManager.SIZE_NOLIMIT);
    //
    // if (manager.hasErrors()) {
    // throw new Exception(manager.getErrors().toString());
    // }
    //
    // return manager;
    // }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void headerText() {
        try {
            super.setParametres(FWIImportParametre.PARAM_COMPANYNAME,
                    getSession().getApplication().getLabel("ADR_CAISSE", requisition.getTiers().getLangueISO()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    private void initHeader() throws Exception {
        // Header
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), requisition.getTiers().getLangueISO());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
        headerBean.setAdresse(getAdresseString());
        headerBean.setDate(JACalendar.format(requisition.getDate(), requisition.getTiers().getLangueISO()));
        // headerBean.setNomCollaborateur(_getResponsable());
        caisseReportHelper.addHeaderParameters(this, headerBean);
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
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
        return requisitionIt.hasNext();
    }

    /**
     * Méthode appelé pour lancer l'exportation du document Par défaut ne pas utiliser car déjà implémenté par la
     * superClass Utile si on ne veut pas exporter en fichier temporaire Date de création : (17.02.2003 14:44:15)
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }

    // Transforme le code de la langue du tiers en code utilisé pour la
    // recherche du libellé du code système
    private String transformLangueTiers(String langueTiers) {
        String res = "F";
        if (langueTiers.equals("IT")) {
            res = "I";
        } else if (langueTiers.equals("DE")) {
            res = "D";
        } else if (langueTiers.equals("EN")) {
            res = "E";
        }
        return res;
    }
}