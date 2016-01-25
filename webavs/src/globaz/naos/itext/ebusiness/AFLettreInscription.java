package globaz.naos.itext.ebusiness;

import globaz.babel.api.ICTDocument;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.CaisseSignatureReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BIEntity;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.itext.AFDocumentItextService;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.tiers.TITiersAdresseManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.util.TIAdresseResolver;
import globaz.webavs.common.ICommonConstantes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.orion.business.models.inscription.InscriptionEbusiness;

public class AFLettreInscription extends FWIDocumentManager {

    private static final long serialVersionUID = -1630527032008937638L;
    private static final String CT_TYPE_INSCRIPTION = "836200";

    public final static String NUMERO_REFERENCE_INFOROM = "0285CAF";

    private final static String P_CONCERNE = "P_CONCERNE";
    private final static String P_CORPS = "P_CORPS";
    private final static String P_PASSWORD = "P_PASSWORD";
    private final static String P_SALUTATIONS = "P_SALUTATIONS";
    private static final String TEMPLATE_FILE_NAME = "NAOS_LETTRE_INSCRIPTION";
    private ICTDocument catalogue = null;
    private Boolean hasNext = true;
    private InscriptionEbusiness inscription = null;
    private String langueDoc = null;
    private TITiersViewBean tiers = null;

    public AFLettreInscription() {
        super();
    }

    public AFLettreInscription(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    @Override
    public void beforeBuildReport() throws FWIException {

        // Gestion du lot "à controler"
        lotSepared();
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
    }

    /**
     * Construit l'adresse du destinataire et ajoute la zone "A l'attention de" en fonction du paramètre
     * 
     * @param attention
     *            set la zone "A l'attention de".
     * @return l'adresse du destinataire
     * @throws Exception
     */
    private String buildAdresseCourrierInscription(String attention) throws Exception {
        TIAdresseDataSource adresseDS = retrieveTiers().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                ICommonConstantes.CS_APPLICATION_COTISATION, getInscription().getNumAffilie(),
                JACalendar.todayJJsMMsAAAA(), true, null);

        if (!retrieveTiers().getPersonnePhysique()) {
            adresseDS.attention = attention;
        }

        return new TIAdresseFormater().format(adresseDS);
    }

    /**
     * Construit le contenu de la lettre à l'aide du catalogue de textes.
     * 
     * @throws Exception
     */
    private void buildDocument() throws Exception {
        super.setTemplateFile(AFLettreInscription.TEMPLATE_FILE_NAME);
        super.setDocumentTitle(getSession().getLabel("TITRE_DOC_LETTRE_INSCRIPTION"));
        initHeader();
        initSignature();

        try {
            // Texte pour le concerne
            this.setParametres(
                    AFLettreInscription.P_CONCERNE,
                    AFDocumentItextService.getTexte(catalogue, 1, new String[] { getInscription().getNumAffilie(),
                            getInscription().getNom(), getInscription().getPrenom() }));
            // Texte pour le corps de texte
            this.setParametres(
                    AFLettreInscription.P_CORPS,
                    AFDocumentItextService.getTexte(catalogue, 2,
                            new String[] { retrieveTiers().getFormulePolitesse(getLangue()),
                                    getInscription().getNumAffilie(), getInscription().getNom(),
                                    getInscription().getPrenom(), getInscription().getRaisonSociale() }));
            // Texte pour la signature
            this.setParametres(AFLettreInscription.P_PASSWORD, getInscription().getPassword());
            // Texte pour les annexes
            this.setParametres(
                    AFLettreInscription.P_SALUTATIONS,
                    AFDocumentItextService.getTexte(catalogue, 3,
                            new String[] { retrieveTiers().getFormulePolitesse(getLangue()) }));
        } catch (Exception e) {
            getMemoryLog().logMessage("", FWMessage.ERREUR, e.getMessage());
        }
    }

    @Override
    public void createDataSource() throws Exception {
        super.getDocumentInfo().setDocumentTypeNumber(AFLettreInscription.NUMERO_REFERENCE_INFOROM);

        // Informations pour la GED
        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                    affilieFormater.unformat(getInscription().getNumAffilie()));

            TIDocumentInfoHelper.fill(getDocumentInfo(), getInscription().getIdTiers(), getSession(),
                    ITIRole.CS_AFFILIE, getInscription().getNumAffilie(),
                    affilieFormater.unformat(getInscription().getNumAffilie()));
        } catch (Exception e) {
            getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", getInscription().getNumAffilie());
        }
        // Chargement du catalogue
        loadCatalogue(AFLettreInscription.CT_TYPE_INSCRIPTION);

        buildDocument();

        ArrayList<Map<String, String>> documents = new ArrayList<Map<String, String>>();
        Map<String, String> nomDoc = new HashMap<String, String>();
        nomDoc.put(AFLettreInscription.TEMPLATE_FILE_NAME,
                getSession().getApplication().getLabel("NAOS_LETTRE_INSCRIPTION", getLangue().toUpperCase()));
        documents.add(nomDoc);

        // On sette le nom dans le dataSource
        this.setDataSource(documents);
    }

    private TIAbstractAdresseData dataSourceAdr(BSession session, String idTiers, String csDomaine, String csType,
            String idExterne, String dateJJsMMsAAAA, boolean herite) throws Exception {
        TIAdresseDataManager mgr = new TITiersAdresseManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForDateEntreDebutEtFin(dateJJsMMsAAAA);
        mgr.changeManagerSize(0);
        if (herite == false) {
            mgr.setForTypeAdresse(csType);
            mgr.setForIdApplication(csDomaine);
            mgr.setForIdExternAvoirAdresse(idExterne);
        }
        mgr.find();
        Collection<BIEntity> col = null;
        if (herite == false) {
            col = new ArrayList<BIEntity>();
            if (mgr.size() == 1) {
                col.add(mgr.getFirstEntity());
            } else if (mgr.size() > 1) {
                // Préventif, ne doit pas arriver !
                throw new Exception("TIAdresseResolver.strAdr, Assert error : Too many records found for " + "idTiers:"
                        + idTiers + ", " + "type:" + csType + ", " + "domaine:" + csDomaine + ", "
                        + "idExterneAvoirAdresse:" + idExterne + ", " + "date:" + dateJJsMMsAAAA);
            }
        } else {
            // On recherche la bonne adresse parmis toutes celles possibles.
            col = TIAdresseResolver.resolveForOneTiers(mgr, csType, csDomaine, idExterne);
        }

        if (col.size() == 1) {
            return (TIAbstractAdresseData) col.iterator().next();
        }

        return null;
    }

    public InscriptionEbusiness getInscription() {
        return inscription;
    }

    /**
     * retourne le code iso de la langue pour ce document.
     * 
     * @return la valeur courante de l'attribut langue
     */
    private String getLangue() {
        if (langueDoc == null) {
            langueDoc = retrieveTiers().getLangueIso();
        }
        if (langueDoc == null) {
            langueDoc = getSession().getIdLangueISO();
        }

        return langueDoc;
    }

    /**
     * Initialise l'entete de la lettre
     * 
     * @throws Exception
     */
    private void initHeader() throws Exception {
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        String attention = AFDocumentItextService.getTexte(catalogue, 9, 1, new String[] { inscription.getPrenom(),
                inscription.getNom() });

        headerBean.setAdresse(buildAdresseCourrierInscription(attention));

        headerBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), getLangue()));
        headerBean.setNoAffilie(getInscription().getNumAffilie());
        headerBean.setNoAvs(retrieveTiers().getNumAvsActuel());
        headerBean.setEmailCollaborateur(" ");
        headerBean.setNomCollaborateur(getSession().getUserFullName());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        headerBean.setUser(getSession().getUserInfo());

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangue().toUpperCase());
        caisseReportHelper.addHeaderParameters(this, headerBean);
    }

    /**
     * Initialise la signature de la lettre
     * 
     * @throws Exception
     */
    private void initSignature() throws Exception {
        // mise en place de la signature
        CaisseSignatureReportBean signBean = new CaisseSignatureReportBean();
        signBean.setService2("");
        signBean.setSignataire2("");

        // on récupère la propriété "signature.nom.caisse" du
        // jasperGlobazProperties
        String caisseSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_CAISSE
                + getLangue().toUpperCase());
        signBean.setSignatureCaisse(caisseSignature);

        // on récupère la propriété "signature.nom.service" du
        // jasperGlobazProperties
        String serviceSignature = getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_SIGN_NOM_SERVICE
                + getLangue().toUpperCase());
        // la méthode _replaceVars permet de remplacer les chaine de type
        // {user.service}
        String serviceSignatureFinal = ACaisseReportHelper._replaceVars(serviceSignature, getSession().getUserId(),
                null);
        signBean.setService(serviceSignatureFinal);
        signBean.setSignataire(getSession().getUserFullName());

        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), getLangue().toUpperCase());
        caisseReportHelper.addSignatureParameters(this, signBean);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Chargement du catalogue
     */
    private void loadCatalogue(String typeDocument) {
        try {
            catalogue = AFDocumentItextService.retrieveCatalogue(getSession(), getLangue(), CodeSystem.DOMAINE,
                    typeDocument);
        } catch (Exception e) {
            getMemoryLog().logMessage(getSession().getLabel("CATALOGUE_INTROUVABLE"), FWMessage.ERREUR,
                    this.getClass().getName());
            abort();
        }
    }

    /**
     * Vérifie les criteres de FWPARP.
     * 
     * @see AFCritereLotSepared
     */
    private void lotSepared() {
        try {
            String criteres = FWFindParameter.findParameter(getTransaction(), "0", "CRITINSCR", "0", "0", 0);

            TIAbstractAdresseData adresseDate = dataSourceAdr(getSession(), inscription.getIdTiers(),
                    ICommonConstantes.CS_APPLICATION_COTISATION, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    getInscription().getNumAffilie(), JACalendar.todayJJsMMsAAAA(), true);

            if (criteres.contains(AFCritereLotSepared.remplaceAdresseDes1.getLabel())
                    && !JadeStringUtil.isBlank(adresseDate.getDesignation1_adr())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.remplaceAdresseDes2.getLabel())
                    && !JadeStringUtil.isBlank(adresseDate.getDesignation2_adr())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.remplaceAdresseDes3.getLabel())
                    && !JadeStringUtil.isBlank(adresseDate.getDesignation3_adr())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.remplaceAdresseDes4.getLabel())
                    && !JadeStringUtil.isBlank(adresseDate.getDesignation4_adr())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.tiersDes3.getLabel())
                    && !JadeStringUtil.isBlank(retrieveTiers().getDesignation3())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.tiersDes4.getLabel())
                    && !JadeStringUtil.isBlank(retrieveTiers().getDesignation4())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

            if (criteres.contains(AFCritereLotSepared.attention.getLabel())
                    && !JadeStringUtil.isBlank(adresseDate.getAttention())) {
                super.getDocumentInfo().setSeparateDocument(true);
                return;
            }

        } catch (Exception e) {
            this._addError(e.toString());
        }
    }

    @Override
    public boolean next() throws FWIException {
        if (hasNext) {
            hasNext = !hasNext;
            return true;
        }
        return false;
    }

    /**
     * Rechercher le tiers fonction de son ID.
     * 
     * @return le tiers
     */
    private TITiersViewBean retrieveTiers() {
        if (JadeStringUtil.isIntegerEmpty(inscription.getIdTiers())) {
            return null;
        }

        if ((tiers == null) || ((tiers != null) && !inscription.getIdTiers().equals(tiers.getIdTiers()))) {

            tiers = new TITiersViewBean();
            tiers.setSession(getSession());
            tiers.setIdTiers(inscription.getIdTiers());
            try {
                tiers.retrieve();
            } catch (Exception e) {
                this._addError(null, e.getMessage());
                tiers = null;
            }
        }
        return tiers;
    }

    /**
     * @param inscription
     *            the inscription to set
     */
    public void setInscription(InscriptionEbusiness inscription) {
        this.inscription = inscription;
    }

}
