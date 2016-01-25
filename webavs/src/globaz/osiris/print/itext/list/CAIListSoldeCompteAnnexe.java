package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CARoleManager;
import globaz.osiris.db.comptes.CARoleViewBean;
import globaz.osiris.parser.CASelectBlockParser;
import globaz.osiris.translation.CACodeSystem;

/**
 * Date de création : (06.03.2002 11:30:24)
 */
public class CAIListSoldeCompteAnnexe extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0018GCA";
    private static final String TEMPLATE_FILENAME = "CAIListSoldeCA";
    private String forDate = new String();
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private String forSelectionRole = new String();
    private String forSelectionSigne = new String();

    private String forSelectionTri = new String();
    private String idCompteAnnexe = new String();

    /**
     * Commentaire relatif au constructeur CAIListSoldeCompteAnnexe.
     */
    public CAIListSoldeCompteAnnexe() throws Exception {
        this(new globaz.globall.db.BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAIListSoldeCompteAnnexe.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListSoldeCompteAnnexe(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, CAIListSoldeCompteAnnexe.TEMPLATE_FILENAME);
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_LIST_SOLDE"));
    }

    /**
     * Commentaire relatif au constructeur CAIListSoldeCompteAnnexe.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListSoldeCompteAnnexe(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, CAIListSoldeCompteAnnexe.TEMPLATE_FILENAME, session
                .getLabel("TITLE_PRINT_LIST_SOLDE"));
    }

    /**
     * Commentaire relatif au constructeur CAListSoldeCompteAnnexe.
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentTitle
     *            java.lang.String
     */
    public CAIListSoldeCompteAnnexe(globaz.globall.db.BSession session, String filenameRoot, String companyName,
            String documentTitle) throws FWIException {
        this(session, filenameRoot, companyName, documentTitle, null);
    }

    /**
     * Commentaire relatif au constructeur CAIListSoldeCompteAnnexe.
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @param filenameRoot
     *            java.lang.String
     * @param companyName
     *            java.lang.String
     * @param documentTitle
     *            java.lang.String
     * @param applicationId
     *            java.lang.String
     */
    public CAIListSoldeCompteAnnexe(globaz.globall.db.BSession session, String filenameRoot, String companyName,
            String documentTitle, String applicationId) throws FWIException {
        super(session, filenameRoot, companyName);
        super.setDocumentTitle(documentTitle);
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        // Exercice (date)
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        // Titre du document
        if ((getForDate() == null) || (getForDate().trim().length() == 0)) {
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6003"));
        } else {
            super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6003A") + " " + getForDate());
        }
        // Affichage de la sélection du rôle
        CARole tempRole;
        CARoleManager manRole = new CARoleManager();
        manRole.setSession(getSession());
        try {
            manRole.find(getTransaction());
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_ROLE, getSession().getLabel("ROLE") + " :");
            if (!getForSelectionRole().equals(CARoleViewBean.getTousOptions(getSession()))) {
                for (int i = 0; i < manRole.size(); i++) {
                    tempRole = (CARole) manRole.getEntity(i);
                    if (tempRole.getIdRole().equalsIgnoreCase(getForSelectionRole())) {
                        super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_ROLE, tempRole.getDescription());
                    }
                }
            } else {
                super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_ROLE, getSession().getLabel("TOUS"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Affichage de la sélection de tri
        super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_TRI, getSession().getLabel("TRI"));
        if (getForSelectionTri().equalsIgnoreCase("1")) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_TRI, getSession().getLabel("NUMERO"));
        } else {
            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_TRI, getSession().getLabel("NOM"));
        }

        addPrintParametersGenre();
        addPrintParametersCategorie();

        // Affichage de la sélection de choix des signes
        super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_SIGNE, getSession().getLabel("SIGNE") + " :");
        if (getForSelectionSigne().equalsIgnoreCase("1")) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_SIGNE, getSession().getLabel("POSITIF_ET_NEGATIF"));
        } else if (getForSelectionSigne().equalsIgnoreCase("2")) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_SIGNE, getSession().getLabel("POSITIF"));
        } else {
            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_SIGNE, getSession().getLabel("NEGATIF"));
        }
        super._headerText();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
        super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_TOTAUX, getSession().getLabel("TOTAL_DE_CAS"));

    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        super.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("COMPTEANNEXE"));
        super.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("DATE_RADIATION"));
        super.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("SOLDE"));
    }

    /**
     * Si la catégorie est présente ajout des paramètres pour l'impression.
     */
    private void addPrintParametersCategorie() {
        if (!JadeStringUtil.isBlank(getForIdCategorie())) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_CATEGORIE, getSession().getLabel("CATEGORIE")
                    + " :");
            try {
                if (getForIdCategorie().equals(CACompteAnnexeManager.ALL_CATEGORIE)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE,
                            getSession().getLabel(CASelectBlockParser.LABEL_TOUS));
                } else if (getForIdCategorie().equals(CACompteAnnexe.CATEGORIE_COMPTE_STANDARD)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE,
                            getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD));
                } else {
                    FWParametersSystemCodeManager manager = CACodeSystem.getCategories(getSession());

                    for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdCategorie())) {
                            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_CATEGORIE, code
                                    .getCurrentCodeUtilisateur().getLibelle());
                            return;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Si le genre est présente ajout des paramètres pour l'impression.
     */
    private void addPrintParametersGenre() {
        if (!JadeStringUtil.isBlank(getForIdGenreCompte())) {
            super.setParametres(CAIListSoldeCompteAnnexeParam.LABEL_GENRE, getSession().getLabel("GENRE") + " :");
            try {
                if (getForIdGenreCompte().equals(CACompteAnnexe.GENRE_COMPTE_STANDARD)) {
                    super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_GENRE,
                            getSession().getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD));
                } else {
                    FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(getSession());

                    for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
                        FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                        if (code.getIdCode().equals(getForIdGenreCompte())) {
                            super.setParametres(CAIListSoldeCompteAnnexeParam.PARAM_GENRE, code
                                    .getCurrentCodeUtilisateur().getLibelle());
                            return;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Attribution du nom du document
        try {
            getDocumentInfo().setTemplateName(CAIListSoldeCompteAnnexe.TEMPLATE_FILENAME);
            getDocumentInfo().setDocumentTypeNumber(CAIListSoldeCompteAnnexe.NUMERO_REFERENCE_INFOROM);
            setTemplateFile(CAIListSoldeCompteAnnexe.TEMPLATE_FILENAME);

            BManager source = null;

            if ((getForDate() == null) || (getForDate().trim().length() == 0)) {
                source = new CAIListSoldeCompteAnnexe_DS();
                source.setSession(getSession());
                ((CAIListSoldeCompteAnnexe_DS) source).setContext(getParent());
                ((CAIListSoldeCompteAnnexe_DS) source).setForSelectionRole(getForSelectionRole());
                ((CAIListSoldeCompteAnnexe_DS) source).setForSelectionSigne(getForSelectionSigne());
                ((CAIListSoldeCompteAnnexe_DS) source).setForSelectionTri(getForSelectionTri());
                ((CAIListSoldeCompteAnnexe_DS) source).setForIdGenreCompte(getForIdGenreCompte());
                ((CAIListSoldeCompteAnnexe_DS) source).setForIdCategorie(getForIdCategorie());
                super.setDataSource((CAIListSoldeCompteAnnexe_DS) source);
            } else {
                source = new CAIListSoldeCompteAnnexeAtDate_DS();
                source.setSession(getSession());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setContext(getParent());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForSelectionRole(getForSelectionRole());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForSelectionSigne(getForSelectionSigne());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForSelectionTri(getForSelectionTri());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForDate(getForDate());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForIdGenreCompte(getForIdGenreCompte());
                ((CAIListSoldeCompteAnnexeAtDate_DS) source).setForIdCategorie(getForIdCategorie());
                super.setDataSource((CAIListSoldeCompteAnnexeAtDate_DS) source);
            }
            if (getParent() != null) {
                getParent().setProgressScaleValue(source.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.04.2002 10:22:45)
     */
    @Override
    public void beforeExecuteReport() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.api.BIDocument#bindData(String)
     */
    public void bindData(String id) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itext.print.FWIImportManager#_createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        _headerText();
        _tableHeader();
        _summaryText();
        super._getRefParam();
    }

    /**
     * Returns the forDate.
     * 
     * @return String
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 13:22:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:58:41)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionSigne() {
        return forSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 13:44:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:11:32)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdCompteAnnexe() {
        return idCompteAnnexe;
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

    /**
     * Sets the forDate.
     * 
     * @param forDate
     *            The forDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 13:22:43)
     * 
     * @param newForSelectionRole
     *            java.lang.String
     */
    public void setForSelectionRole(java.lang.String newForSelectionRole) {
        forSelectionRole = newForSelectionRole;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (30.04.2002 08:58:41)
     * 
     * @param newForSelectionSigne
     *            java.lang.String
     */
    public void setForSelectionSigne(java.lang.String newForSelectionSigne) {
        forSelectionSigne = newForSelectionSigne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 13:44:09)
     * 
     * @param newFromNumNom
     *            java.lang.String
     */
    public void setForSelectionTri(java.lang.String newForSelectionTri) {
        forSelectionTri = newForSelectionTri;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:11:32)
     * 
     * @param newIdCompteAnnexe
     *            java.lang.String
     */
    public void setIdCompteAnnexe(java.lang.String newIdCompteAnnexe) {
        idCompteAnnexe = newIdCompteAnnexe;
    }

}
