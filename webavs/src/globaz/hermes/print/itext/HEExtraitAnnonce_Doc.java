package globaz.hermes.print.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.commons.nss.NSUtil;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEImpressionciListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceException;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.print.itext.util.HEExtraitComparatorCaisse;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.pavo.application.CIApplication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 
 * 
 */
public class HEExtraitAnnonce_Doc extends FWIDocumentManager {

    public static final String KEY_SEPARATOR = "_";
    private static final String NUMERO_INFOROM = "0009CCI";
    private static final long serialVersionUID = -1282911277808204703L;

    /**
     * Main execution pour lancement en mode batch des impressions
     * 
     * @param String
     *            [] args -> -d AAAAMMDD -> -r numero de reference -> false delete on exit non
     */
    public static void main(String[] args) {
        HEExtraitAnnonce_Doc process = null;

        boolean deleteOnExit = true;
        /*
         * boolean isNotBatch = true;
         */
        try {
            if (args.length != 3) {
                throw new Exception(
                        "Usage : java globaz.hermes.process.HEExtraitAnnonceProcess <date jjmmaaaa> <uid> <pwd>");
            }
            BSession session = new BSession("HERMES");
            // Jade.getInstance().setHomeDir(((HEApplication)
            // session.getApplication()).getProperty("zas.home.dir"));
            System.out.println("Home dir " + Jade.getInstance().getHomeDir());
            session.connect(args[1], args[2]);
            // session.connect("globazd", "ssiiadm");
            /** FIN JADE * */
            process = new HEExtraitAnnonce_Doc(session);
            HEApplication application = (HEApplication) session.getApplication();
            // for(int i=0; i<5; i++) {
            // process.setReferenceUnique("52455");
            // }
            process.setForDate(args[0]);
            process.setDeleteOnExit(deleteOnExit);
            process.setEMailAddress(application.getProperty("zas.user.email"));
            process.execute();
            System.out.println("Programme terminé ! Copier le fichier PDF avant de presser <Enter>....");
            System.in.read();
            System.out.println("Arrêt du programme lancé !");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            System.exit(0);
        }
    }

    protected HEExtraitAnnonceBean _currExtrait = null;
    protected Iterator _docIterator = null;
    // private String referenceUnique = "";
    private String _forDate = null;
    private String emailSubjectError = "";
    //
    private String emailSubjectOK = "";
    private String forNumCaisse = "";
    private String fromDate = null;
    private boolean impressionBatch = false;
    private boolean isArchivage = false;
    private boolean isManuelPrint = false;
    private String langue = "FR";
    private String langueFromEcran = "";
    private String lastRefUnique = "";
    protected HEExtraitAnnonceAssureBean m_container = null;
    protected String m_lastCodeApplication = "";
    /**
     * key = N° AVS obj = m_container
     */
    protected TreeMap m_mainContainer = new TreeMap(new HEExtraitComparatorCaisse());
    protected String[] motifs = { "71", "81", "78", "85" };
    private String nomPrenomMailSubject = "";
    private String numCaisseCI = "-";
    private String numeroAVS = "";
    private String numeroAVS20 = "";

    private boolean premierPassage = true;
    private TreeSet referenceUniqueList = new TreeSet();
    protected Vector referenceUniqueVectorList = new Vector();
    public String service;
    private String sexe = "";
    private String untilDate = null;

    protected String userId = "";

    private String utilisateur = "";

    /**
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public HEExtraitAnnonce_Doc() throws FWIException {
        super();
    }

    public HEExtraitAnnonce_Doc(BSession session) throws FWIException {
        this(session, CIApplication.APPLICATION_PAVO_REP, session.getLabel("HERMES_00036"));
    }

    /**
     * Constructor for HEExtraitAnnonce_Doc.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    protected HEExtraitAnnonce_Doc(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    /**
     * Constructor for HEExtraitAnnonce_Doc.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public HEExtraitAnnonce_Doc(FWProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    protected String _getCode1ou2(HEOutputAnnonceViewBean entity)
            throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_1_OU_2);
    }

    protected String _getCodeApplication(HEOutputAnnonceViewBean entity)
            throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_APPLICATION);
    }

    protected String _getCodeEnregistrement(HEOutputAnnonceViewBean entity)
            throws globaz.hermes.db.gestion.HEOutputAnnonceException {
        return entity.getField(IHEAnnoncesViewBean.CODE_ENREGISTREMENT);
    }

    /**
     * Commentaire relatif à la méthode _headerText.
     */
    protected void _headerText() throws Exception {
        if (m_container.m_isExtraitComplementaire) {
            super.setParametres(HEExtraitAnnonce_Param.P_HEAD_1,
                    "Nachtrags IK-Auszug\nExtrait de CI additionnel\nEstratto del CI aggiunto");
        } else {
            super.setParametres(HEExtraitAnnonce_Param.P_HEAD_1,
                    "Auszug aus dem individuellen Konto\nExtrait du compte individuel\nEstratto del conto individuale");
        }
        super.setParametres(HEExtraitAnnonce_Param.P_HEAD_2, m_container.m_nomPrenom);
        super.setParametres(HEExtraitAnnonce_Param.P_HEAD_3, m_container.m_numeroAvs);

        super.setParametres(HEExtraitAnnonce_Param.P_HEAD_4,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + "DE") + "\n"
                        + getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + "FR")
                        + "\n"
                        + getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_NOM_CAISSE + "IT"));
        super.setParametres(HEExtraitAnnonce_Param.P_HEAD_5, m_container.m_dateNaissance);
        super.setParametres(HEExtraitAnnonce_Param.P_HEAD_6, m_container.m_etatOrigine);
        super.setParametres(HEExtraitAnnonce_Param.P_MOTIF,
                getSession().getApplication().getLabel("HERMES_10031", getLangue()));
        // super.setParametres(HEExtraitAnnonce_Param.P_MOTIF2,
        // "Keine Buchung für diese Ausgleichskasse vorhanden\nAucune écriture reçue pour cette caisse");
    }

    // Création du paramètre de référence pour les documents de type liste
    protected void _setRefParam() {
        try {
            // ALD modif : du 24 mars 2004
            // String text = getSession().getUserId() + " / " +
            // m_container.getMotif() + " / ";
            StringBuffer text = new StringBuffer(m_container.getUtilisateur().toLowerCase());
            text.append(" / ");
            text.append(m_container.getMotif());
            text.append(" / ");
            text.append(m_container.getReferenceInterne().trim());
            if (!JadeStringUtil.isEmpty(m_container.getNumeroAnnonce())) {
                text.append("/");
                text.append(m_container.getNumeroAnnonce().trim());
            }
            if (!JadeStringUtil.isBlank(getService())) {
                super.setParametres(HEExtraitAnnonce_Param.P_REFERENCE, text.toString() + "/" + getService());
            } else {
                super.setParametres(HEExtraitAnnonce_Param.P_REFERENCE, text.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:33)
     */
    protected void _summaryText() {
        // JADate today = JACalendar.today();
        // String todayString = JACalendar.format(today,
        // JACalendar.FORMAT_DDsMMsYYYY);
        String isoCode = getSession().getIdLangueISO().toUpperCase();
        super.setParametres(HEExtraitAnnonce_Param.L_1, "Abrechnungsnummer\nNuméro d'affilié\nNumero di affiliato");
        super.setParametres(HEExtraitAnnonce_Param.L_2, "Einkommenscode\nCode revenu\nCodice reddito");
        super.setParametres(HEExtraitAnnonce_Param.L_3,
                "Bruchteil der Betreuungsgutschrift\nPart aux bonifications d'assistance\nParte degli accrediti d'assistenza");
        super.setParametres(HEExtraitAnnonce_Param.L_4,
                "Beitragsmonate (Beginn/Ende)\nMois de cotisation (début/fin)\nMesi di contribuzione (inizio/fine)");
        super.setParametres(HEExtraitAnnonce_Param.L_5, "Beitragsjahr\nAnnée de cotisation\nAnno di contribuzione");
        super.setParametres(HEExtraitAnnonce_Param.L_6, "Einkommen\nRevenu\nReddito");
        super.setParametres(HEExtraitAnnonce_Param.L_7,
                "Beachten Sie das beigelegte Merkblatt\nVoir le mémento annexé\nVedasi il promemoria allegato");
        super.setParametres(HEExtraitAnnonce_Param.P_SUM_LIEU_DATE,
                getTemplateProperty(getDocumentInfo(), ACaisseReportHelper.JASP_PROP_HEADER_PREFIXE_DATE + isoCode)
                        + " " + m_container.m_date);
        _setRefParam();
    }

    /**
     * Commentaire relatif à la méthode _tableHeader.
     */
    protected void _tableHeader() {
        super.setParametres(HEExtraitAnnonce_Param.L_8,
                "Arbeitgeber oder Einkommensart\nEmployeurs ou genre de revenu\nDatori di lavoro o genere del reddito");
        super.setParametres(HEExtraitAnnonce_Param.L_9, "Heimatstaat/Etat d'origine/Stato d'origine:");
        super.setParametres(HEExtraitAnnonce_Param.L_10, "Kassen-Nr.\nN° caisse\nN° cassa");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 08:04:09)
     */
    @Override
    protected void _validate() {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("HERMES_00001"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("MSG_EMAIL_INV"));
            }
        }
        setControleTransaction(true);
        if (!getSession().hasErrors() && !isImpressionBatch()) {
            setSendCompletionMail(true);
        }
    }

    public boolean addEntity(HEOutputAnnonceViewBean entity, String userId) {
        try {
            String key = getKey(entity);
            String numRef = entity.getRefUnique();
            String numeroAssure = entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
            String codeApplication = _getCodeApplication(entity);
            String codeEnregistrement = _getCodeEnregistrement(entity);
            String code = _getCode1ou2(entity);
            if ((m_container = (HEExtraitAnnonceAssureBean) m_mainContainer.get(key)) == null) {
                if (isSpecialMotif(entity)) {
                    m_container = (HEExtraitAnnonceAssureBean) m_mainContainer.get(numRef + "_" + numeroAssure + "_00");
                    if (m_container != null) {
                        m_container = (HEExtraitAnnonceAssureBean) m_container.clone();
                        m_container.clearExtrait();
                    }
                }
                if (m_container == null) {
                    m_container = new HEExtraitAnnonceAssureBean(); // Cette
                }
                // clef
                // n'existe
                // pas
                m_container.setReferenceUnique(numRef);
            }
            if (codeApplication.equals("38")) {
                if (code.equals("1")) { // Code type 1
                    _currExtrait = new HEExtraitAnnonceBean(entity, userId);
                    _currExtrait.setLangue(getLangue());
                } else { // Code type 2
                    _currExtrait.setTextRevenu(entity.getField(IHEAnnoncesViewBean.PARTIE_INFORMATION));
                }
            } else if (codeApplication.equals("39")) {
                m_container.setReferenceInterne(entity
                        .getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE_COMMETTANTE));
                m_container.setNumeroCaisseCI(StringUtils.formatCaisseAgence(
                        entity.getField(IHEAnnoncesViewBean.NUMERO_CAISSE__CI),
                        entity.getField(IHEAnnoncesViewBean.NUMERO_AGENCE_CI)));
                if (codeEnregistrement.equals("001")) {
                    if (JadeStringUtil.toIntMIN(entity.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI)) < 1) {
                        _currExtrait = new HEExtraitAnnonceBean();
                        _currExtrait.setNumeroCaisse(entity.getNumeroCaisse());
                        _currExtrait.setLangue(getLangue());
                    }
                    m_container.setExtraitComplementaire(entity.getField(IHEAnnoncesViewBean.CI_ADDITIONNEL)
                            .equals("1"));
                } else {
                    m_container.setNomPrenom(entity.getField(IHEAnnoncesViewBean.ETAT_NOMINATIF));
                    // modif NNSS
                    m_container.setNumeroAvs(numeroAssure, entity.getNumeroAvsNNSS());
                    m_container.setMotif(entity.getMotif());
                    m_container.setUtilisateur(getUtilisateur());
                }
            } else if (codeApplication.equals("20") && (codeEnregistrement.equals("01"))) {
                m_container.setHeader(entity);
                m_container.setIdAnnonce11(entity.getRefUnique());
                setNumeroAVS20(entity.getNumeroAVS());
                setSexe(entity.getField(IHEAnnoncesViewBean.SEXE));
                _currExtrait = null;
            }
            // ajouter la référence dans le container afin del'imprimer sur
            // l'extrait
            m_container.setNumeroAnnonce(numRef);
            // Sauvegarde des modifications
            if (_currExtrait != null) {
                m_container.setExtrait(_currExtrait);
            }
            if (m_container != null) {
                m_mainContainer.put(key, m_container);
            }
            m_lastCodeApplication = getSignature(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addReferenceUnique(String refUnique) {
        referenceUniqueVectorList.add(refUnique);
    }

    @Override
    public void afterPrintDocument() {
        try {
            super.afterPrintDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_FORMATTE,
                m_container.getNumeroAvs());
        getDocumentInfo().setDocumentProperty(TIDocumentInfoHelper.TIERS_NUMERO_AVS_NON_FORMATTE,
                JadeStringUtil.removeChar(m_container.getNumeroAvs(), '.'));
        getDocumentInfo().setDocumentProperty("user.origine.demande", m_container.getUtilisateur());
    }

    @Override
    public void beforeBuildReport() throws FWIException {
    }

    @Override
    public void beforeExecuteReport() {
        try {
            setTailleLot(0);
            setImpressionParLot(true);
            setDocumentRoot(CIApplication.APPLICATION_PAVO_REP);
            getExporter().setExportFileName(getSession().getLabel("HERMES_00036"));
            if (!JadeStringUtil.isEmpty(getForDate())) {
                // Impression par date
                // Recherche des Extraits CI Rassemblements
                try {
                    ImprimerRassemblements();
                } catch (JAException e) {
                    e.printStackTrace();
                }
                // Recherche des Extraits CI de Compléments
                try {
                    ImprimerComplement();
                } catch (JAException e) {
                    e.printStackTrace();
                }
            }
            // Creer la liste des collections
            Vector docList = new Vector();
            for (Iterator it = referenceUniqueList.iterator(); it.hasNext();) {
                imprimerReference(((HEAnnoncesViewBean) it.next()).getRefUnique(), userId);
                // docList.addAll((new TreeMap(m_mainContainer)).values());
                docList.addAll(m_mainContainer.values());
            }
            // si la liste des références est données sous une forme de vector
            // dans le cas ou PAVO désire imprimer des 95
            for (Iterator it = referenceUniqueVectorList.iterator(); it.hasNext();) {
                imprimerReference((String) it.next(), userId);
                // docList.addAll((new TreeMap(m_mainContainer)).values());
                docList.addAll(m_mainContainer.values());
            }
            // Make ready for the first document
            _docIterator = docList.iterator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean beforePrintDocument() {
        return ((super.getDocumentList().size() > 0) && !isAborted());
    }

    public void bindData(int id) throws Exception {
        super.execute();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     * @deprecated
     */
    @Deprecated
    public void bindData(String id) throws Exception {
    }

    /**
     * Cette méthode permet de contrôler si la période insérée pour l'extrait d'annonce est valide
     * 
     * @param listExtraitAnnonce
     * @param extraitAnnonce
     */
    private boolean checkPeriode(final HEExtraitAnnonceBean extraitAnnonce) {
        boolean isOk = true;
        // tester si on a bien une année de cotisation
        if (JadeStringUtil.isBlankOrZero(extraitAnnonce.getAnneeCotti())) {
            isOk = false;

            // permet d'avoir les numéros de caisse dans le cas ou on n'a pas d'inscriptions
            if (!JadeStringUtil.isBlankOrZero(extraitAnnonce.getNumeroCaisse())) {
                isOk = true;
            }
        } else {
            // si j'ai spécifié une date de début et que l'année de coti n'est pas juste
            if (!JadeStringUtil.isBlankOrZero(getFromDate())
                    && (Integer.valueOf(extraitAnnonce.getAnneeCotti()) < Integer.valueOf(getFromDate()))) {
                isOk = false;
            }
            // si j'ai spécifié une date de fin et que l'année de coti n'est pas juste
            if (!JadeStringUtil.isBlankOrZero(getUntilDate())
                    && (Integer.valueOf(extraitAnnonce.getAnneeCotti()) > Integer.valueOf(getUntilDate()))) {
                isOk = false;
            }
        }
        return isOk;
    }

    @Override
    public void createDataSource() throws Exception {
        if (getDocumentInfo() != null) {
            getDocumentInfo().setDocumentTypeNumber(HEExtraitAnnonce_Doc.NUMERO_INFOROM);
        }
        super.setTemplateFile("PAVO_EXTR_CI_MASTER");
        super.getImporter().setDocumentTemplate("PAVO_EXTR_CI_MASTER");
        setDocumentTitle(m_container.getNumeroAvs() + " (" + m_container.getUtilisateur().toLowerCase() + ")");

        if (isPremierPassage()) {
            setNomPrenomMailSubject(m_container.m_nomPrenom.trim());
            setPremierPassage(false);
        }

        _headerText();
        _tableHeader();
        _summaryText();

        // On contrôle si on a une période donnée
        if (!JadeStringUtil.isBlankOrZero(getFromDate()) || !JadeStringUtil.isBlankOrZero(getUntilDate())) {
            List<HEExtraitAnnonceBean> listExtraitAnnonce = new ArrayList<HEExtraitAnnonceBean>();

            Iterator<HEExtraitAnnonceBean> it = m_container.getCollection().iterator();
            while (it.hasNext()) {
                HEExtraitAnnonceBean extraitAnnonce = it.next();
                // créer l'extrait d'annonce en fonction de l'année passée
                if (checkPeriode(extraitAnnonce)) {
                    listExtraitAnnonce.add(extraitAnnonce);
                }
            }
            // créer le doc dans le cas ou on a minimum 1 inscription sinon on ne fait rien
            if (listExtraitAnnonce.size() > 0) {
                super.setDataSource(listExtraitAnnonce);
                super.setParametres(HEExtraitAnnonce_Param.getSubReport(), new JRBeanCollectionDataSource(
                        listExtraitAnnonce));
            }

        } else {
            // comportement normal
            super.setDataSource(m_container.getCollection());
            super.setParametres(HEExtraitAnnonce_Param.getSubReport(),
                    new JRBeanCollectionDataSource(m_container.getCollection()));
        }
    }

    @Override
    protected String getEMailObject() {
        // le titre du mail est toujours le même quelque soit si l'impresssion
        // est ok ou non
        if (JadeStringUtil.isEmpty(getEmailSubjectOK())) {
            if (isManuelPrint()) {
                // il n'y a qu'un seul extrait dans le document
                if (!JadeStringUtil.isBlank(getService())) {
                    return getSession().getLabel("HERMES_10030") + " " + NSUtil.formatAVSUnknown(numeroAVS) + "/"
                            + getNomPrenomMailSubject() + "/" + getService();
                } else {
                    return getSession().getLabel("HERMES_10030") + " " + NSUtil.formatAVSUnknown(numeroAVS) + "/"
                            + getNomPrenomMailSubject();
                }

            } else {
                if (!JadeStringUtil.isBlank(getService())) {
                    return getSession().getLabel("HERMES_00034") + " / " + JACalendar.todayJJsMMsAAAA() + "/"
                            + getService();
                } else {
                    return getSession().getLabel("HERMES_00034") + " / " + JACalendar.todayJJsMMsAAAA();
                }
            }
        } else {
            return getEmailSubjectOK();
        }
    }

    /**
     * Returns the emailSubjectError.
     * 
     * @return String
     */
    public String getEmailSubjectError() {
        return emailSubjectError;
    }

    /**
     * Returns the emailSubjectOK.
     * 
     * @return String
     */
    public String getEmailSubjectOK() {
        return emailSubjectOK;
    }

    /**
     * Returns the dateLot.
     * 
     * @return JADate
     */
    public String getForDate() {
        return _forDate;
    }

    /**
     * Returns the forNumCaisse.
     * 
     * @return String
     */
    public String getForNumCaisse() {
        return forNumCaisse;
    }

    public String getFromDate() {
        return fromDate;
    }

    public boolean getIsArchivage() {
        return isArchivage();
    }

    private String getKey(HEOutputAnnonceViewBean entity) throws HEOutputAnnonceException {
        String numRef = entity.getRefUnique();
        String numeroAVS = entity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE);
        String motif = entity.getMotif();
        String caisse = entity.getNumeroCaisse();
        // Attention, si on change cette méthode de référencement, il faut aussi
        // changer dans
        // la classe HEExtraitComparatorCaisse !
        String theKey = numRef + HEExtraitAnnonce_Doc.KEY_SEPARATOR + numeroAVS + HEExtraitAnnonce_Doc.KEY_SEPARATOR;
        if (isSpecialMotif(entity)) {
            return theKey + motif + HEExtraitAnnonce_Doc.KEY_SEPARATOR + caisse;
        } else {
            return theKey + "00";
        }
    }

    /**
     * @return
     */
    public String getLangue() {
        return langue;
    }

    public String getLangueFromEcran() {
        return langueFromEcran;
    }

    /**
     * @return
     */
    public String getLastRefUnique() {
        return lastRefUnique;
    }

    private HEExtraitAnnonceAssureBean getNextDoc() {
        HEExtraitAnnonceAssureBean bean = null;
        while (_docIterator.hasNext()) {
            bean = (HEExtraitAnnonceAssureBean) _docIterator.next();
            if (bean.getCollection().size() > 0) {
                return bean;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public String getNomPrenomMailSubject() {
        return nomPrenomMailSubject;
    }

    /**
     * Returns the numCaisseCI.
     * 
     * @return String
     */
    public String getNumCaisseCI() {
        return numCaisseCI;
    }

    /**
     * @return
     */
    public String getNumeroAVS20() {
        return numeroAVS20;
    }

    public String getReferenceUnique() {
        return referenceUniqueVectorList.get(0).toString();
    }

    /**
     * Returns the referenceUniqueList.
     * 
     * @return Vector
     */
    public TreeSet getReferenceUniqueList() {
        return referenceUniqueList;
    }

    public Vector getReferenceUniqueVector() {
        return referenceUniqueVectorList;
    }

    public String getService() {
        return service;
    }

    /**
     * @return
     */
    public String getSexe() {
        return sexe;
    }

    protected String getSignature(HEOutputAnnonceViewBean entity) throws HEOutputAnnonceException {
        String codeApplication = _getCodeApplication(entity);
        String codeEnregistrement = _getCodeEnregistrement(entity);
        String code = _getCode1ou2(entity);
        if (codeApplication.equals("38")) {
            return codeApplication + code;
        } else {
            return codeApplication + codeEnregistrement;
        }
    }

    public String getUntilDate() {
        return untilDate;
    }

    /**
     * Returns the userId.
     * 
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the utilisateur.
     * 
     * @return String
     */
    public String getUtilisateur() {
        return utilisateur;
    }

    protected void ImprimerComplement() throws JAException {

        HEOutputAnnonceListViewBean manager = new HEOutputAnnonceListViewBean();
        manager.setSession(getSession());
        manager.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        manager.setForCodeApplicationLike("39001");
        //
        // certain manager ne convertise pas les date
        if (JadeStringUtil.isDigit(getForDate())) {
            manager.setForDate(getForDate());
        } else {
            manager.setForDate(JACalendar.format(getForDate(), JACalendar.FORMAT_YYYYMMDD));
        }
        try {
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            HEOutputAnnonceViewBean entity = null;
            for (int i = 0; i < manager.size(); i++) {
                entity = (HEOutputAnnonceViewBean) manager.getEntity(i);
                if (entity.getChampEnregistrement().substring(91, 92).equals("1")) {
                    referenceUniqueList.add(entity);
                }
            }
        } catch (Exception e) {
            super._addError("L'impression des Extraits Compléments du " + manager.getForDate() + " : " + e.getMessage());
            super.setMsgType(super.WARNING);
            super.setMessage("L'impression des Extraits Compléments du " + manager.getForDate() + " : "
                    + e.getMessage());
            throw new JAException("L'impression des Extraits Compléments du " + manager.getForDate() + " : "
                    + e.getMessage());
        } finally {
            manager = null;
        }
    }

    protected void ImprimerRassemblements() throws JAException {

        HEImpressionciListViewBean manager = new HEImpressionciListViewBean();
        manager.setSession(getSession());
        manager.setForDate(getForDate());
        manager.setIsArchivage(String.valueOf(isArchivage()));
        try {
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            for (int i = 0; i < manager.size(); i++) {
                referenceUniqueList.add(manager.getEntity(i));
            }
        } catch (Exception e) {
            super._addError("L'impression des Extraits Rassemblé du " + manager.getForDate() + " : " + e.getMessage());
            super.setMsgType(super.WARNING);
            super.setMessage("L'impression des Extraits Rassemblé du " + manager.getForDate() + " : " + e.getMessage());
            throw new JAException("L'impression des Extraits Rassemblé du " + manager.getForDate() + " : "
                    + e.getMessage());
        } finally {
            manager = null;
        }
    }

    /**
     * Imprimer le document d'une référence unique
     * 
     * @param String
     *            ref Reference Unique
     * @return true si le document a été créer sinon false;
     */
    protected void imprimerReference(String ref, String userId) throws JAException {

        HEOutputAnnonceListViewBean manager = null;
        m_mainContainer = new TreeMap(new HEExtraitComparatorCaisse());
        m_container = null;
        _currExtrait = null;
        try {
            manager = new HEOutputAnnonceListViewBean();
            manager.setSession(getSession());
            manager.setForRefUnique(ref);
            // Information de base
            manager.setOrder("RNIANN");
            // On recherche d'abord les informations de base -> 2001
            manager.setLikeEnregistrement("2001");
            // on spécificie si les données sont dans l'archivage
            manager.setIsArchivage(isArchivage());

            // seulement si service <> vide et qu'il y a qqch dans paramétrage
            if (!JadeStringUtil.isBlank(getService())) {
                manager.setForService(getService());
            }

            // statement = manager.cursorOpen(super.getTransaction());
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
            if ((manager.size() > 0) || JadeStringUtil.isBlank(getService())) {
                HEOutputAnnonceViewBean entity = null;
                for (int i = 0; i < manager.size(); i++) {
                    entity = (HEOutputAnnonceViewBean) manager.getEntity(i);
                    addEntity(entity, userId);
                    // on mémorise l'utilisateur et le numéro AVS pour cette
                    // annonce
                    numeroAVS = entity.getNumeroAVS();

                    if (JadeStringUtil.isEmpty(userId)) {
                        utilisateur = entity.getUtilisateur();
                    } else {
                        utilisateur = userId;
                    }
                }
                // On recherche les codeApplication 38 et 39
                manager.setLikeEnregistrement("3");
                manager.setForService("");
                // si une caisse est spécifiée
                if (getForNumCaisse().length() > 0) {
                    manager.setForNumCaisse(getForNumCaisse());
                }
                manager.find(getTransaction(), BManager.SIZE_NOLIMIT);
                for (int i = 0; i < manager.size(); i++) {
                    addEntity((HEOutputAnnonceViewBean) manager.getEntity(i), userId);
                }
            }
            // creation du dernier document
        } catch (Exception e) {
            super._addError("Le document avec la reference '" + ref + "' n'a pas pu être créer : " + e.getMessage());
            super.setMsgType(super.ERROR);
            super.setMessage("Le document avec la reference '" + ref + "' n'a pas pu être créer : " + e.getMessage());
            throw new JAException("Le document avec la reference '" + ref + "' n'a pas pu être créer : "
                    + e.getMessage());
        }
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    public boolean isImpressionBatch() {
        return impressionBatch;
    }

    /**
     * @return
     */
    public boolean isManuelPrint() {
        return isManuelPrint;
    }

    /**
     * @return
     */
    public boolean isPremierPassage() {
        return premierPassage;
    }

    protected boolean isSpecialMotif(HEOutputAnnonceViewBean entity) throws HEOutputAnnonceException {
        String motif = entity.getMotif();
        return (Arrays.asList(motifs).contains(motif));
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    @Override
    public boolean next() throws FWIException {
        return ((m_container = getNextDoc()) != null);
    }

    @Override
    public void returnDocument() throws FWIException {
        super.getExporter().setExportApplicationRoot(HEApplication.DEFAULT_APPLICATION_ROOT);
        super.imprimerListDocument();
    }

    /**
     * Sets the emailSubjectError.
     * 
     * @param emailSubjectError
     *            The emailSubjectError to set
     */
    public void setEmailSubjectError(String emailSubjectError) {
        this.emailSubjectError = emailSubjectError;
    }

    /**
     * Sets the emailSubjectOK.
     * 
     * @param emailSubjectOK
     *            The emailSubjectOK to set
     */
    public void setEmailSubjectOK(String emailSubjectOK) {
        this.emailSubjectOK = emailSubjectOK;
    }

    /**
     * Sets the dateLot.
     * 
     * @param dateLot
     *            The dateLot to set
     */
    public void setForDate(String dateLot) {
        _forDate = dateLot;
    }

    /**
     * Sets the forNumCaisse.
     * 
     * @param forNumCaisse
     *            The forNumCaisse to set
     */
    public void setForNumCaisse(String forNumCaisse) {
        this.forNumCaisse = forNumCaisse;
    }

    /**
     * Set la date de début
     * 
     * @param fromDate
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setImpressionBatch(boolean impressionBatch) {
        this.impressionBatch = impressionBatch;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * @param string
     */
    public void setLangue(String string) {
        langue = string;
    }

    public void setLangueFromEcran(String langueFromEcran) {
        this.langueFromEcran = langueFromEcran;
    }

    /**
     * @param string
     */
    public void setLastRefUnique(String string) {
        lastRefUnique = string;
    }

    /**
     * @param b
     */
    public void setManuelPrint(boolean b) {
        isManuelPrint = b;
    }

    /**
     * @param string
     */
    public void setNomPrenomMailSubject(String string) {
        nomPrenomMailSubject = string;
    }

    /**
     * @param string
     */
    public void setNumeroAVS20(String string) {
        numeroAVS20 = string;
    }

    /**
     * @param b
     */
    public void setPremierPassage(boolean b) {
        premierPassage = b;
    }

    public void setReferenceUnique(HEAnnoncesViewBean o) {
        referenceUniqueList.add(o);
    }

    /**
     * Sets the referenceUniqueList.
     * 
     * @param referenceUniqueList
     *            The referenceUniqueList to set
     */
    public void setReferenceUniqueList(TreeSet referenceUniqueList) {
        this.referenceUniqueList = referenceUniqueList;
    }

    public void setReferenceUniqueVector(Vector v) {
        referenceUniqueVectorList = v;
    }

    public void setService(String newService) {
        service = newService;
    }

    /**
     * @param string
     */
    public void setSexe(String string) {
        sexe = string;
    }

    /**
     * set la date de fin de période
     * 
     * @param untilDate
     */
    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    /**
     * Sets the userId.
     * 
     * @param userId
     *            The userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Sets the utilisateur.
     * 
     * @param utilisateur
     *            The utilisateur to set
     */
    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

}
