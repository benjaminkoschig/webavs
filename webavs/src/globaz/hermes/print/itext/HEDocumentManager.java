package globaz.hermes.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.hermes.application.HEApplication;
import globaz.hermes.babel.HECTConstantes;
import globaz.hermes.db.access.HEInfos;
import globaz.hermes.db.access.HEInfosManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * Classe abstraite parente de touts les documents du projet aquila Centralise les fonctionalit�s communes aux documents
 * 
 * @author Alexandre Cuva, 19-aug-2004
 * @version $Id: HEDocumentManager.java,v 1.20 2010/09/02 09:14:49 jmc Exp $
 */
public abstract class HEDocumentManager extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Seuil de l'ex�cution directe ou diff�r�e */
    private static final int JOB_QUEUE_THRESHOLD = 10;

    /** L'annonce de l'assur� */
    protected HEExtraitAnnonceAssureBean annonceAssure = null;

    /** Collection pour un DataSource */
    private ArrayList beanList = new ArrayList();
    /** Entit� courante */
    private Object currentEntity = null;
    /** Liste des entit�s � traiter */
    private ArrayList entityList = new ArrayList();
    /** Document principal */
    private HEDocumentManager firstDocument = null;
    /** It�rateur sur la liste des entit�s */
    private Iterator iEntityList = null;
    /** Langue du document */
    private String langue = "FR";
    private String langueFromEcran = "";
    /** Liste annexes */
    private String listeAnnexes = "";
    /** niveau � transmettre le d�tail des positions -1= aucun */
    private int niveauPourDetail = -1;
    /** PrintCompletionDoc */
    private boolean printCompletionDoc = true;
    /** PrintOutline */
    private boolean printOutline = false;
    /** Map d'une ligne de donn�es */
    private Map row = new HashMap();

    public HEDocumentManager() throws Exception {
        super();
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de probl�me d'initialisaion
     */
    public HEDocumentManager(BSession parent, String fileName) throws FWIException {
        super(parent, HEApplication.DEFAULT_APPLICATION_ROOT, fileName);
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @param fileName
     *            Le nom du fichier
     * @throws FWIException
     *             En cas de probl�me d'initialisaion
     */
    public HEDocumentManager(FWProcess parent, String fileName) throws FWIException {
        super(parent, HEApplication.DEFAULT_APPLICATION_ROOT, fileName);
    }

    /**
     * Retourne un String repr�sentant l'adresse sur plusieurs lignes
     * 
     * @param info
     *            L'info d'o� vient l'adresse
     * @return La repr�sentation de l'adresse
     */
    protected String _getAdresseString(HEInfos adresse) {
        return adresse.getLibInfo();
    }

    /**
     * @return La langue du document
     */
    protected String _getLangue() {
        if (JadeStringUtil.isEmpty(langue)) {
            // TODO : rechercher correctement la langue du tiers
            return "FR";
        } else {
            return langue.toUpperCase();
        }
    }

    /**
     * Retourne un String repr�sentant la valeur de la propri�t�
     * 
     * @param property
     *            Le nom de la propri�t�
     * @param additionalValue
     *            Une valeur additionnelle
     * @return La valeur de la propri�t�
     */
    protected String _getProperty(String property, String additionalValue) {
        String buffer = "";
        if (!JadeStringUtil.isEmpty(property)) {
            buffer = getTemplateProperty(getDocumentInfo(), property + _getLangue());
        }
        if (!JadeStringUtil.isEmpty(additionalValue)) {
            buffer += additionalValue;
        }
        return buffer;
    }

    /**
     * Gestion de l'en-t�te/pied de page/signature
     * 
     * @param adresseDestination
     *            L'adresse du destinataire du document
     * @param annonce
     *            L'annonce de l'assur� concern�e par ce document
     * @param hasHeader
     *            <code>true</code> si le document contient un en-t�te
     * @param hasFooter
     *            <code>true</code> si le document contient un pied de page
     * @param hasSignature
     *            <code>true</code> si le document contient une signature
     * @throws Exception
     *             En cas de probl�me
     */
    protected void _handleHeaders(Object adresseDestination, String numAffilie, boolean hasHeader) throws Exception {
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), _getLangue());
        caisseReportHelper.init(getSession().getApplication(), _getLangue());
        CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();

        // Adresse du destinataire
        if (adresseDestination != null) {
            if (adresseDestination instanceof HEInfos) {
                headerBean.setAdresse(_getAdresseString((HEInfos) adresseDestination));
                // Param�tres relatifs � l'annonce
                headerBean.setNoAffilie("");
            }
            if (adresseDestination instanceof TITiersViewBean) {
                headerBean.setNoAffilie(numAffilie);
                headerBean.setAdresse(((TITiersViewBean) adresseDestination).getAdresseAsString(getDocumentInfo(),
                        IConstantes.CS_AVOIR_ADRESSE_COURRIER, HEApplication.CS_DOMAINE_ADRESSE_CI_ARC));
            }
            if (adresseDestination instanceof TIAdministrationViewBean) {
                headerBean.setNoAffilie("");
                headerBean.setNoAvs("");
                headerBean.setAdresse(((TIAdministrationViewBean) adresseDestination).getAdresseAsString(
                        getDocumentInfo(), IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                        HEApplication.CS_DOMAINE_ADRESSE_CI_ARC));
            }
        } else {
            headerBean.setAdresse("");

        }
        // Date
        headerBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), _getLangue()));
        headerBean.setNomCollaborateur(getSession().getUserFullName());
        headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
        headerBean.setUser(getSession().getUserInfo());
        if (hasHeader) {
            caisseReportHelper.addHeaderParameters(this, headerBean);
        }
    }

    protected void _setLangue(String lge) {
        langue = getSession().getCode(lge);
        ;
    }

    /**
     * Sp�cifie la langue du document en fonction de la langue de l'affili�
     * 
     * @param TITiersViewBean
     *            La langue de l'affili�
     */
    protected void _setLangueFromAffilie(TITiersViewBean affilie) {
        if (affilie != null) {
            langue = getSession().getCode(affilie.getLangue());
        }
    }

    /**
     * Sp�cifie la langue du document en fonction de la langue de l'assur�
     * 
     * @param HEInfos
     *            La langue de l'assur�
     */
    protected void _setLangueFromAssure(HEInfos langueAssure) {
        if (langueAssure != null) {
            langue = getSession().getCode(langueAssure.getLibInfo());
        }
    }

    /**
     * Sp�cifie la langue du document en fonction de la langue d'un tiers
     * 
     * @param tiers
     *            Le tiers
     */
    protected void _setLangueFromTiers(ITITiers tiers) {
        if (tiers != null) {
            langue = tiers.getLangue();
        }
    }

    public void _setLangueWithLangueFromEcran(String theLangueFromEcran) {
        langue = theLangueFromEcran;
    }

    /**
     * Ajoute un groupe d'entit�s � la liste
     * 
     * @param allEntities
     *            Le groupe d'entit�s
     * @return <code>true</code> si l'ajout s'est bien pass�
     */
    public boolean addAllEntities(Collection allEntities) {
        return entityList.addAll(allEntities);
    }

    /**
     * Ajoute une entit� � la liste
     * 
     * @param entity
     *            L'entit�
     * @return <code>true</code> si l'ajout s'est bien pass�
     */
    public boolean addEntity(Object entity) {
        return entityList.add(entity);
    }

    /**
     * Ne fait rien par d�faut
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
    }

    /**
     * Ne fait rien par d�faut
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setDocumentRoot(HEApplication.DEFAULT_APPLICATION_ROOT);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#beforePrintDocument()
     */
    @Override
    public boolean beforePrintDocument() {
        if (firstDocument != null) {
            // Gestion du titre et du nom
            setDocumentTitle(firstDocument.getExporter().getExportFileName());
            getExporter().setExportFileName(firstDocument.getExporter().getExportFileName());
        }
        try {
            if (isPrintOutline()) {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_ALL);
            } else {
                super.getExporter().setExporterOutline(JRExporterParameter.OUTLINE_NONE);
            }
        } catch (Exception e) {
            // pas grave si �a plante!
        }

        return super.beforePrintDocument();
    }

    /**
     * Retourne l'entit� courante
     * 
     * @return L'entit�
     */
    public Object currentEntity() {
        return currentEntity;
    }

    protected String format(String message, String[] args) {
        StringBuffer msgBuf = new StringBuffer();

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public HEExtraitAnnonceAssureBean getAnnonceAssure() {
        return annonceAssure;
    }

    /**
     * @return un JRDataSource de type JRMapCollectionDataSource
     */
    protected JRDataSource getEntityBeanDataSource() {
        return new JRMapCollectionDataSource(beanList);
    }

    protected ICTDocument[] getICTDocument(String isoLangue, String categorie, String nom) {
        ICTDocument res[] = null;
        ICTDocument document = null;
        try {
            document = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, "Error while api for document");
        }
        document.setISession(getSession());
        document.setCsDomaine(HECTConstantes.CS_HE_DOMAINE);
        document.setCsTypeDocument(categorie);
        document.setCodeIsoLangue(isoLangue);
        document.setActif(new Boolean(true));
        document.setNom(nom);
        try {
            res = document.load();
        } catch (Exception e1) {
            getMemoryLog().logMessage(e1.toString(), FWMessage.ERREUR, "Error while getting document");
        }
        return res;
    }

    /**
     * Recherche de l'info de l'assur�
     * 
     * @param String
     *            idArc Id de l'annonce
     * @param String
     *            systemCode
     * @return info de type HEInfos
     */
    public HEInfos getInfoAssure(String idArc, String systemCode) throws Exception {
        HEInfos info = null;
        // Recherche de l'info de l'assur�
        HEInfosManager infosManager = new HEInfosManager();
        infosManager.setSession(getSession());
        infosManager.setForIdArc(idArc);
        infosManager.setForTypeInfo(systemCode);
        infosManager.find();
        if (infosManager.size() > 0) {
            info = ((HEInfos) infosManager.getEntity(0));
            return info;
        }
        return new HEInfos();
    }

    public String getLangueFromEcran() {
        return langueFromEcran;
    }

    /**
     * @return La liste des annexes
     */
    public String getListeAnnexes() {
        return listeAnnexes;
    }

    /** Template properties */
    public abstract int getNbLevel();

    /**
     * @return
     */
    public int getNiveauPourDetail() {
        return niveauPourDetail;
    }

    protected String getNumeroAVSAssure() {
        if (annonceAssure != null) {
            return annonceAssure.getNumeroAvs();
        } else {
            return "";
        }
    }

    public boolean isPrintCompletionDoc() {
        return printCompletionDoc;
    }

    public boolean isPrintOutline() {
        return printOutline;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        if (entityList.size() <= HEDocumentManager.JOB_QUEUE_THRESHOLD) {
            return GlobazJobQueue.READ_SHORT;
        } else {
            return GlobazJobQueue.READ_LONG;
        }
    }

    protected void loadCatTexte(String isoLangueTiers, String categorie, String nomDoc) throws Exception {
        StringBuffer buffer;
        // passe les param�tres n�cessaires
        ICTDocument listeTxt = getICTDocument(isoLangueTiers, categorie, nomDoc)[0];
        if (listeTxt != null) {
            ICTListeTextes crtListe;
            for (int i = 1; i < getNbLevel() + 1; i++) {
                buffer = new StringBuffer();
                try {
                    crtListe = listeTxt.getTextes(i);
                } catch (Exception e) {
                    crtListe = null;
                }
                if (crtListe != null) {
                    for (Iterator titresIter = crtListe.iterator(); titresIter.hasNext();) {
                        if (buffer.length() > 0) {
                            buffer.append("\n\n");
                        }
                        ICTTexte txt = (ICTTexte) titresIter.next();
                        buffer.append(txt.getDescription());
                        if (i == getNiveauPourDetail()) {
                            setPositionToCatTexte(txt.getPosition(), buffer.toString());
                            buffer = new StringBuffer();
                        }
                    }
                    if (i != getNiveauPourDetail()) {
                        setFieldToCatTexte(i, buffer.toString());
                    }
                }
            }
        }

    }

    /**
     * Initialise une nouvelle ligne de donn�e pour la Map JRDataSource
     */
    protected void newLineEntityBean() {
        if (row.size() > 0) {
            beanList.add(row);
        }
        row = new HashMap();
    }

    /**
     * Retourne <code>true</code> si il reste des entit�s � traiter et pr�pare l'entit� courante
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     * @return <code>true</code> si il reste des entit�s
     * @throws FWIException
     *             En cas de probl�me
     */
    @Override
    public boolean next() throws FWIException {
        if (iEntityList == null) {
            iEntityList = entityList.iterator();
        }
        boolean hasNext = iEntityList.hasNext();
        if (hasNext) {
            currentEntity = iEntityList.next();
        }
        return hasNext;
    }

    /**
     * @param user
     *            La nouvelle valeur de la propri�t�
     */
    public void setAnnonceAssure(HEExtraitAnnonceAssureBean bean) {
        annonceAssure = bean;
    }

    /**
     * Set un champ d'information pour la Map JRDataSource
     * 
     * @param name
     * @param obj
     */
    protected void setEntityBean(String name, Object obj) {
        row.put(name, obj);
    }

    public abstract void setFieldToCatTexte(int i, String value) throws Exception;

    /**
     * @param document
     *            Document principal
     */
    public void setFirstDocument(HEDocumentManager document) {
        firstDocument = document;
    }

    public void setLangueFromEcran(String langueFromEcran) {
        this.langueFromEcran = langueFromEcran;
    }

    /**
     * @param liste
     *            des annexes
     */
    public void setListeAnnexes(String string) {
        listeAnnexes = string;
    }

    /**
	 * 
	 */
    protected void setPositionToCatTexte(String position, String texte) {
        // par d�faut la m�thode n'est pas appel�
    }

    public void setPrintCompletionDoc(boolean b) {
        printCompletionDoc = b;
    }

    public void setPrintOutline(boolean b) {
        printOutline = b;
    }
}
