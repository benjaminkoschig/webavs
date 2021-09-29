package ch.globaz.vulpecula.documents.catalog;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.vulpecula.documents.NumeroReferenceFactory;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.external.models.osiris.TypeSection;
import ch.globaz.vulpecula.external.models.pyxis.Adresse;
import ch.globaz.vulpecula.external.models.pyxis.Role;
import globaz.babel.utils.BabelContainer;
import globaz.babel.utils.CatalogueText;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.Serializable;
import java.util.*;

import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import ch.globaz.vulpecula.util.CodeSystemUtil;
import ch.globaz.vulpecula.util.I18NUtil;

/**
 * <p>
 * Classe abstraite permettant la g�n�ration de document iText
 * 
 * <p>
 * Dans le cas o� la gestion de catalogues babel est n�cessaire, il appeler la m�thode .
 * 
 * <p>
 * La classe g�n�rique utilis�e doit �tre serializable.
 * <p>
 * La classe concr�te �tendant cette classe doit impl�menter deux constructeurs obligatoirement :
 * <ul>
 * <li> -> A ne pas utiliser mais n�cessaire pour le serveur de job
 * <li> -> A utiliser pour cr�er un processus
 * </ul>
 * 
 * <p>
 * Dans le cas o� l'on souhaite imprimer plusieurs documents, il s'agit d'ajouter un �tat qui d�finit quel type de
 * document et imprimer. Et d'autre part, il s'agit d'overrider les m�thodes suivantes :
 * <ul>
 * <li>{@link #next()} Afin de d�finir sous quelle condition il y a un document suivant. Ceci implique le plus souvent
 * la gestion d'un status afin de d�finir quel est le document actuellement utilis�.</li>
 * <li>{@link #fillFields()} Afin de g�rer la population des diff�rents types de documents, en basant la population sur
 * l'�tat du process.
 * <li>{@link #beforeBuildReport()} Afin de d�finir quel template est actuellement en utilisatation via
 * {@link #setTemplateFile(String)}
 * <p>
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 4 juin 2014
 * 
 */
public abstract class DocumentManager<T extends Serializable> extends FWIDocumentManager {

    private static final long serialVersionUID = 1L;

    private String numeroInfoRom;
    private String headerName;

    private CatalogueText catalogueText;
    private BabelContainer babelContainer;

    // Ajout pour QR Facture
    protected ReferenceQR qrFacture = null;
    protected TypeSection typeSection = TypeSection.BULLETIN_NEUTRE;

    private T element;

    // Stockage de la datasource de base
    private JRDataSource baseDataSource;
    @SuppressWarnings("rawtypes")
    private Map baseParametres;

    private boolean hasBeenPrinted = false;

    public DocumentManager(final String application, final String applicationRoot) throws Exception {
        super(new BSession(application), applicationRoot, null);
        initCataloguesTextes(getDomaine(), getTypeDocument(), getNomDocumentForCataloguesTextes());
    }

    public DocumentManager(final String application, final String applicationRoot, final T element,
            final String documentName, final String numeroInfoRom) throws Exception {
        super(new BSession(application), applicationRoot, documentName);
        initCataloguesTextes(getDomaine(), getTypeDocument(), getNomDocumentForCataloguesTextes());
        this.element = element;
        this.numeroInfoRom = numeroInfoRom;
    }

    public abstract String getDomaine();

    public abstract String getTypeDocument();

    public abstract String getNomDocumentForCataloguesTextes();

    @Override
    protected void _createDocumentInfo() {
        super._createDocumentInfo();
        getDocumentInfo().setDocumentTypeNumber(numeroInfoRom);
    }

    /**
     * Retourne le template Jasper PRINCIPAL utilis� pour g�n�rer le document. (p.ex : DOCUMENT_VIDE pour
     * DOCUMENT_VIDE.japser)
     * 
     * @return String repr�sentant le fichier jasper pour la g�n�ration du document.
     */
    public abstract String getJasperTemplate();

    /**
     * Retourne une liste des catalogues de textes utilis�s pour la g�n�ration des documents.
     * 
     * @return Liste des catalogues de textes.
     */
    public List<CatalogueText> definirCatalogs() {
        return Collections.emptyList();
    }

    /**
     * Remplissage du formulaire
     * Cette m�thode doit contenir EXCLUSIVEMENT des appels � {@link #setParametres(Object, Object)} et
     *
     */
    public abstract void fillFields() throws Exception;

    /**
     * Retourne un objet de {@link CaisseHeaderReportBean} contenant toutes les informations n�cessaire pour l'ent�te.
     * Voici un exemple d'impl�mentation :
     * 
     * <pre>
     * CaisseHeaderReportBean beanReport = new CaisseHeaderReportBean();
     * beanReport.setAdresse(getAdresseFormate());
     * beanReport.setNomCollaborateur(getSession().getUserFullName());
     * beanReport.setTelCollaborateur(getSession().getUserInfo().getPhone());
     * beanReport.setUser(getSession().getUserInfo());
     * beanReport.setDate(getDateExecution());
     * beanReport.setNoAffilie(getAffilieNumero());
     * beanReport.setConfidentiel(true);
     * return beanReport;
     * </pre>
     * 
     * @return le bean avec les infos n�cessaire pour l'ent�te
     * @throws Exception
     */
    public abstract CaisseHeaderReportBean giveBeanHeader() throws Exception;

    /**
     * @return la langue du destinataire du document
     */
    public abstract CodeLangue getCodeLangue();

    /**
     * Retourne le texte du catalogue d�finit pour une position, un niveau et une langue
     * 
     * @param niveau
     * @param position
     * @return le texte traduit
     * @throws Exception
     */
    public String getTexte(final int niveau, final int position) throws Exception {
        return babelContainer.getTexte(getCatalogueTextForLangue(getCodeLangue()), niveau, position);
    }

    /**
     * @param codeLangue
     * @return le CatalogueText d�finit pour cette langue
     */
    private CatalogueText getCatalogueTextForLangue(CodeLangue codeLangue) {
        CatalogueText temp = new CatalogueText();
        temp.setCodeIsoLangue(codeLangue.getCodeIsoLangue());
        temp.setCsDomaine(catalogueText.getCsDomaine());
        temp.setCsTypeDocument(catalogueText.getCsTypeDocument());
        temp.setNomCatalogue(catalogueText.getNomCatalogue());
        return temp;
    }

    /**
     * Retourne le texte du catalogue d�finit pour une position, un niveau et une langue avec les param�tres remplac�s
     * par le texte correspondant de la Map.
     * 
     * @param niveau : niveau du paragraphe
     * @param position : position du texte
     * @param parametresMap : les param�tres pour le formattage
     * @return Le texte dans la bonne traduction et formatt�
     * @throws Exception
     */
    public String getTexteFormatte(int niveau, int position, Map<String, String> parametresMap) throws Exception {
        return DocumentUtils.formatMessage(getTexte(niveau, position), parametresMap);
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        JadePublishDocumentInfo documentInfo = getDocumentInfo();
        documentInfo.setArchiveDocument(true);
        documentInfo.setPublishDocument(false);
        documentInfo.setDocumentType(getNumeroInfoRom());
        documentInfo.setDocumentTypeNumber(getNumeroInfoRom());
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setTemplateFile(getJasperTemplate());
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * D�finit la casacade d'adresses suivante : Domicile - Courrier.
     * Force l'adresse, le user, le nom du collaborateur et le t�l�phone du bean pass� en param�tre.
     *
     * @param codeLangue : langue du document. obligatoire
     * @throws NullPointerException
     */
    private void initHeader(CodeLangue codeLangue) {
        try {
            if (codeLangue == null) {
                throw new NullPointerException("codeLangue est NULL pour l'ent�te !!");
            }
            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    getDocumentInfo(), getSession().getApplication(), codeLangue.getCodeIsoLangue());

            caisseReportHelper.setHeaderName(headerName);
            caisseReportHelper.addHeaderParameters(this, giveBeanHeader());
        } catch (Exception ex) {
            getSession().addError("Probl�me lors de l'initialisation de la transaction");
        }
    }

    // Initilisation du context, car on lance un process avec l'ancienne persistence
    private JadeThreadContext initThreadContext(BSession session) {
        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles;
        try {
            roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                    .findAllIdRoleForIdUser(session.getUserId());
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void createDataSource() throws Exception {
        // Workaround afin de repartir avec une datasource et des param�tres
        // de base lors de la cr�ation de chaque document.
        // Si cela n'est pas effectu�, il peut arriv� que des documents sortent vides sans
        // aucune erreur affich�e dans la console.
        try {
            if (baseDataSource == null) {
                baseDataSource = (JRDataSource) getDataSource().clone();
                baseParametres = new HashMap(getImporter().getParametre());
            } else {
                setDataSource((JRDataSource) baseDataSource.clone());
                setParametres(new HashMap(baseParametres));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());
            initHeader(getCodeLangue());
            fillFields();
        } catch (Exception e) {
            getSession().addError("Probl�me lors de l'initialisation de la transaction");
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }
    }

    /**
     * Retourne l'�l�ment actuellement utilis� pour g�n�rer le document.
     * 
     * @return T repr�sentant l'objet utilis� pour g�n�rer le document
     */
    public final T getCurrentElement() {
        return element;
    }

    /**
     * <b>UTILISER PAR {@link JadeUtil#dumpFields(Object)}</b><br/>
     * 
     * @return Retourne le num�ro Inforom utilis� pour l'indexation
     */
    public final String getNumeroInfoRom() {
        return numeroInfoRom;
    }

    /**
     * <b>UTILISER PAR {@link JadeUtil#dumpFields(Object)}</b><br/>
     * 
     * @param numeroInfoRom Num�ro Inforom utilis� pour l'indexation
     */
    public final void setNumeroInfoRom(final String numeroInfoRom) {
        this.numeroInfoRom = numeroInfoRom;
    }

    @Override
    public boolean next() throws FWIException {
        if (!hasBeenPrinted) {
            hasBeenPrinted = true;
            return true;
        }
        return false;
    }

    @Override
    protected String getEMailObject() {
        return "";
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    /**
     * @param csDomaine : code syst�me du domaine
     * @param csTypeDocument : code syst�me du type de document
     * @param nomDocument : nom donn� au document dans le catalogue de textes
     * @throws Exception
     */
    protected void initCataloguesTextes(String csDomaine, String csTypeDocument, String nomDocument) throws Exception {
        if (csTypeDocument != null && nomDocument != null && csTypeDocument.length() != 0 && nomDocument.length() != 0) {
            babelContainer = new BabelContainer();

            catalogueText = new CatalogueText();
            catalogueText.setCsDomaine(csDomaine);
            catalogueText.setCsTypeDocument(csTypeDocument);
            catalogueText.setNomCatalogue(nomDocument);

            babelContainer.addCatalogueText(getCatalogueText(CodeLangue.FR));
            babelContainer.addCatalogueText(getCatalogueText(CodeLangue.DE));
            babelContainer.addCatalogueText(getCatalogueText(CodeLangue.IT));

            babelContainer.setSession(getSession());
            babelContainer.load();
        }
    }

    /**
     * @param codeLangue
     * @return le CatalogueText d�finit pour cette langue
     */
    private CatalogueText getCatalogueText(CodeLangue codeLangue) {
        CatalogueText temp = new CatalogueText();
        temp.setCodeIsoLangue(codeLangue.getCodeIsoLangue());
        temp.setCsDomaine(catalogueText.getCsDomaine());
        temp.setCsTypeDocument(catalogueText.getCsTypeDocument());
        temp.setNomCatalogue(catalogueText.getNomCatalogue());
        return temp;
    }

    /**
     * @param headerName the headerName to set
     */
    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getLabel(String label) throws Exception {
        BApplication application = getSession().getApplication();
        String langue = I18NUtil.getLanguesOf(getCodeLangue()).getCodeIso();
        return application.getLabel(label, langue);
    }

    public String getCode(String cs) {
        return CodeSystemUtil.getCodeSysteme(cs, I18NUtil.getLanguesOf(getCodeLangue())).getCode();
    }

    public String getCodeLibelle(String cs) {
        return CodeSystemUtil.getCodeSysteme(cs, I18NUtil.getLanguesOf(getCodeLangue())).getLibelle();
    }
    
    public String getCodeLibelle(String cs, CodeLangue langue) {
        return CodeSystemUtil.getCodeSysteme(cs, I18NUtil.getLanguesOf(langue)).getLibelle();
    }

    public String getDefaultModelPath() {
        try {
            return getSession().getApplication().getExternalModelPath() + "defaultModel";
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE);
        }
    }
    
    /**
     * Permet le formatage des textes du catalogue de texte pour les documents iText.<BR>
     * Il permet de remplacer toutes les occurences {x} par l'argument [x] correspondant.
     * 
     * @param message
     * @param args
     * @return
     */
    public static String formatMessage(String message, String[] args) {

        StringBuilder buffer = new StringBuilder(message);

        // doubler les guillemets simples si necessaire
        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }

        // Remplace les {x} par les args[x]
        StringBuilder messageFormat = new StringBuilder();
        boolean isTexte = true;
        String param = "";
        for (int i = 0; i < buffer.length(); i++) {

            if ((buffer.charAt(i) != '{') && isTexte && (buffer.charAt(i) != '}')) {
                messageFormat.append(buffer.charAt(i));
            } else if (!isTexte && (buffer.charAt(i) != '}')) {
                param += buffer.charAt(i);
            } else if (buffer.charAt(i) == '}') {
                isTexte = true;
                if (!JadeStringUtil.isBlank(param)) {
                    int numArg = Integer.parseInt(param);
                    if (numArg < args.length) {
                        messageFormat.append(args[numArg]);
                    }
                }
            } else if (buffer.charAt(i) == '{') {
                isTexte = false;
                param = "";
            }
        }

        return messageFormat.toString();
    }

    // Initialisation des variables QR
    public void initVariableQR(Decompte decompte) {

        qrFacture.setLangueDoc(getLangue());
        qrFacture.setMonnaie(qrFacture.DEVISE_DEFAUT);

        Montant montant = decompte.getMontantContributionTotal();

        if (montant.isNegative()) {
            qrFacture.setMontant("XXXXXXXXXXXXXXX.XX");
        } else {
            qrFacture.setMontant(Objects.isNull(montant)? "" : montant.toString());
        }


        try {
            qrFacture.recupererIban();

                Adresse adresseDebiteurAsData = decompte.getEmployeur().getAdressePrincipale();
                if (Objects.nonNull(adresseDebiteurAsData)) {
                    qrFacture.setDebfAdressTyp(ReferenceQR.STRUCTURE);
                    qrFacture.setDebfNom(adresseDebiteurAsData.getDescription1());
                    qrFacture.setDebfPays(adresseDebiteurAsData.getIsoPays());
                    qrFacture.setDebfCodePostal(adresseDebiteurAsData.getNpa());
                    qrFacture.setDebfLieu(adresseDebiteurAsData.getLocalite());
                    qrFacture.setDebfRueOuLigneAdresse1(adresseDebiteurAsData.getRue());
                    qrFacture.setDebfNumMaisonOuLigneAdresse2(adresseDebiteurAsData.getRueNumero());
                }

            // Cr�ation du numRef
            NumeroReference numRef = NumeroReferenceFactory.createNumeroReference(Role.AFFILIE_PARITAIRE, decompte
                    .getEmployeur().getAffilieNumero(), typeSection, decompte.getNumeroDecompte());

            qrFacture.genererReference(decompte.getMontantContributionTotal().toString(), numRef.getValue());


            // Il n'existe pas pour l'heure actuel d'adresse de cr�diteur en DB.
            // Elle est r�cup�r�e depuis le catalogue de texte au format Combin�e
            qrFacture.genererCreAdresse();
        } catch (Exception e) {
            getMemoryLog().logMessage(
                    "Erreur lors de recherche des �lements de la QR-Facture : " + e.getMessage(),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
        }


    }

    /**
     * @return La langue du document.
     * Pas d'impl�mentation de langue, return "FR" par d�faut
     *
     */
    protected String getLangue() {
        return getCodeLangue().getCodeIsoLangue().toUpperCase();
    }
}
