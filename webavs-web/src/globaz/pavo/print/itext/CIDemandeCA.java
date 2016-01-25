package globaz.pavo.print.itext;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pavo.application.CIApplication;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;

public class CIDemandeCA extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // code système du catalogue de texte pour le domaine (même code système
    // pour tous les pdf)
    private static final String CS_CI_DOMAINE = "329000";
    private final static String CS_TYPE_DEMANDE_CA = "330002";
    public final static String JASP_PROP_HEADER_ADRESSE_CAISSE = "header.adresse.caisse.";
    // Constantes
    private final static String NOM_DOC_RECTO = "PAVO_DEMANDE_CA";
    private final static String NOM_DOC_VERSO = "PAVO_DEMANDE_CA_VERSO_";
    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    public static final String formatMessage(String message, Object[] args) {
        StringBuffer buffer = new StringBuffer(message);
        // doubler les guillemets simples si necessaire
        for (int idChar = 0; idChar < buffer.length(); ++idChar) {
            if ((buffer.charAt(idChar) == '\'')
                    && ((idChar == (buffer.length() - 1)) || (buffer.charAt(idChar + 1) != '\''))) {
                buffer.insert(idChar, '\'');
                ++idChar;
            }
        }
        // remplacer les arguments null par chaine vide
        for (int idArg = 0; idArg < args.length; ++idArg) {
            if (args[idArg] == null) {
                args[idArg] = "";
            }
        }
        // remplacer et retourner
        return MessageFormat.format(buffer.toString(), args);
    }

    // code système du catalogue de texte pour le type de document (un code
    // système par pdf)
    protected String cs_typeDocument = "";
    // Champs pour le catalogue de textes
    protected ICTDocument document;
    private CIDemandeCAStruct etudiant = null;
    private int i = 0;
    protected String idDocument = "";
    private String idJournal = null;
    private String libelleJournal = null;

    // ---------------------------------------------------- BABEL
    private ArrayList listeEtudiant = null;
    protected String nomDocument = "";

    protected int numDocument = 0;

    private int size = 0;

    public CIDemandeCA() {
        super();
    }

    public CIDemandeCA(BProcess parent) throws FWIException {
        super(parent, CIApplication.APPLICATION_PAVO_REP, "demandeCA");

    }

    public CIDemandeCA(BSession session) throws FWIException {
        super(session, CIApplication.APPLICATION_PAVO_REP, "demandeCA");
    }

    @Override
    public void afterBuildReport() {
        try {
            JasperPrint verso = getVerso();
            if (verso != null) {
                verso.setName("DemandeCA_" + etudiant.getNumAffilie() + "_Verso");
                super.getDocumentList().add(verso);
            }
        } catch (Exception e) {
            JadeLogger.error(this, "CreationVerso: " + e.getMessage());
        }
    }

    @Override
    public void beforeBuildReport() throws FWIException {
        setTypeDocument(CS_TYPE_DEMANDE_CA);
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        setImpressionParLot(true);
        setTypeDocument(CS_TYPE_DEMANDE_CA);
        super.setTemplateFile(NOM_DOC_RECTO);
    }

    @Override
    public void createDataSource() throws Exception {
        getDocumentInfo().setDocumentTypeNumber("0211CCI");
        beforeExecuteReport();
        pageDemandeCARecto();

    }

    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep) {
        try {
            for (Iterator paraIter = loadCatalogue().getTextes(niveau).iterator(); paraIter.hasNext();) {

                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            out.append(TEXTE_INTROUVABLE);
            getMemoryLog()
                    .logMessage(e.toString(), FWMessage.ERREUR, getSession().getLabel("ERROR_DUMP_TEXT") + niveau);
        }
    }

    protected String formatMessage(StringBuffer message, Object[] args) {
        return formatMessage(message.toString(), args);
    }

    public String getDefaultModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "defaultModel", '\\',
                    '/');
        } catch (Exception e) {
            return "";
        }
    }

    // méthode BABEL pour intégrer le catalogue de text

    public String getIdDocument() {
        return idDocument;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getLibelleJournal() {
        return libelleJournal;
    }

    public ArrayList getListeEtudiant() {
        return listeEtudiant;
    }

    public String getModelPath() {
        try {
            return JadeStringUtil.change(getSession().getApplication().getExternalModelPath() + "pavoRoot\\model",
                    '\\', '/');
        } catch (Exception e) {
            return "";
        }
    }

    public String getNomDocument() {
        return nomDocument;
    }

    public int getSize() {
        return size;
    }

    protected StringBuffer getTexte(int niveau, int position) {
        StringBuffer resString = new StringBuffer("");
        try {
            ICTListeTextes listeTextes = loadCatalogue().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        } catch (Exception e3) {
            getMemoryLog().logMessage(e3.toString(), FWMessage.ERREUR,
                    getSession().getLabel("ERROR_GETTING_LIST_TEXT") + niveau + ":" + position);
        }
        return resString;
    }

    public String getTypeDocument() {
        return cs_typeDocument;
    }

    protected JasperPrint getVerso() throws Exception {
        String langue = etudiant.getLangue();
        String documentKey = "";
        if (!JadeStringUtil.isBlank(langue)) {
            documentKey = NOM_DOC_VERSO + langue;
        } else {
            documentKey = NOM_DOC_VERSO + "FR";
        }
        return getImporter().importReport(documentKey, getImporter().getImportPath());
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    protected ICTDocument loadCatalogue() throws Exception {
        if (document == null) {
            ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            loader.setActif(Boolean.TRUE);
            if (!JadeStringUtil.isBlank(getNomDocument())) {
                loader.setNom(getNomDocument());
            } else if (!JadeStringUtil.isBlank(getIdDocument())) {
                loader.setIdDocument(getIdDocument());
            } else {
                loader.setDefault(Boolean.TRUE);
            }

            loader.setCodeIsoLangue(etudiant.getLangue());
            loader.setCsDomaine(CS_CI_DOMAINE);
            loader.setCsTypeDocument(getTypeDocument());

            ICTDocument[] candidats = loader.load();

            document = candidats[numDocument];
        }

        return document;
    }

    @Override
    public boolean next() throws FWIException {
        if (i < size) {
            etudiant = (CIDemandeCAStruct) listeEtudiant.get(i);
            i++;
            return true;
        } else {
            return false;
        }

    }

    public void pageDemandeCARecto() throws Exception {
        HashMap champs = new HashMap();
        List lignes = new LinkedList();
        String formatAdresse = etudiant.getAdresse();
        // Header
        CaisseHeaderReportBean crBean = new CaisseHeaderReportBean();
        ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                getDocumentInfo(), getSession().getApplication(), etudiant.getLangue());
        crBean.setDate(JACalendar.format(JACalendar.todayJJsMMsAAAA(), etudiant.getLangue()));
        crBean.setAdresse(formatAdresse);
        setTypeDocument(CS_TYPE_DEMANDE_CA);
        setDocumentTitle("DemandeCA_" + etudiant.getNumAffilie());
        caisseReportHelper.addHeaderParameters(this, crBean);
        caisseReportHelper.addSignatureParameters(this, "");
        // Corps du document
        // Titre 1
        StringBuffer titre1 = new StringBuffer("");
        dumpNiveau(1, titre1, "");
        setParametres("P_TEXT_TITRE_1", titre1.toString());
        // Titre 2
        setParametres("P_TEXT_TITRE_2", getTexte(2, 1).toString());
        // politesse
        StringBuffer politesse = new StringBuffer("");
        if (!JadeStringUtil.isBlank(etudiant.getPolitesse())) {
            politesse.append(formatMessage(getTexte(3, 1), new Object[] { etudiant.getPolitesse() }));
        } else {
            politesse.append(getTexte(3, 2).toString());
        }
        setParametres("P_FORM_POLITESSE", politesse + ", ");
        // Corps 1
        setParametres("P_TEXT_CORPS_1", getTexte(4, 1).toString());
        // Corps 2
        setParametres("P_TEXT_CORPS_2", getTexte(5, 1).toString());
        // Corps 3
        StringBuffer corps3 = new StringBuffer("");
        corps3.append(getTexte(6, 1).toString());
        if (!JadeStringUtil.isBlank(etudiant.getPolitesse())) {
            corps3.append(formatMessage(getTexte(7, 1), new Object[] { etudiant.getPolitesse() }));
        } else {
            corps3.append(getTexte(7, 2).toString());
        }
        setParametres("P_TEXT_CORPS_3", corps3.toString());
        // Annexe
        setParametres("P_TEXT_ANNEXE", getTexte(8, 1).toString());
        lignes.add(champs);
        setDataSource(lignes);
        document = null;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setLibelleJournal(String libelleJournal) {
        this.libelleJournal = libelleJournal;
    }

    public void setListeEtudiant(ArrayList listeEtudiant) {
        this.listeEtudiant = listeEtudiant;
    }

    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTypeDocument(String typeDocument) {
        cs_typeDocument = typeDocument;
    }
}
