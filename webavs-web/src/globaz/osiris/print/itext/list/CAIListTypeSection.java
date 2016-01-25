package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CATypeSection;
import globaz.osiris.db.comptes.CATypeSectionManager;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Insérez la description du type ici. Date de création : (06.03.2002 11:30:24)
 * 
 * @author: Administrator
 */
public class CAIListTypeSection extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class CATypeSectionBean {
        String m_description = null;
        String m_idSequenceContentieux = null;
        String m_idTypeSection = null;
        String m_nomClasse = null;
        String m_nomPageDetail = null;

        CATypeSectionBean(CATypeSection _typeSection) {
            // COL_1 --> Numéro
            m_idTypeSection = _typeSection.getIdTypeSection();
            // COL_2 --> Description
            m_description = _typeSection.getDescription();
            // COL_3 --> N° séquence contentieux
            m_idSequenceContentieux = _typeSection.getIdSequenceContentieux();
            // COL_4 --> Nom de la classe d'implémentation
            m_nomClasse = _typeSection.getNomClasse();
            // COL_5 --> Nom de la page du détail
            m_nomPageDetail = _typeSection.getNomPageDetail();
        }

        /**
         * Returns the idTypeSection.
         * 
         * @return String
         */
        public String getCOL_1() {
            return m_idTypeSection;
        }

        /**
         * Returns the description.
         * 
         * @return String
         */
        public String getCOL_2() {
            return m_description;
        }

        /**
         * Returns the idSequenceContentieux.
         * 
         * @return String
         */
        public String getCOL_3() {
            return m_idSequenceContentieux;
        }

        /**
         * Returns the nomClasse.
         * 
         * @return String
         */
        public String getCOL_4() {
            return m_nomClasse;
        }

        /**
         * Returns the nomPageDetail.
         * 
         * @return String
         */
        public String getCOL_5() {
            return m_nomPageDetail;
        }

    }

    private static final String NUMERO_REFERENCE_INFOROM = "0142GCA";
    private java.lang.String idTypeSection = new String();

    private Collection m_container = new ArrayList();

    /**
     * Commentaire relatif au constructeur CAIListTypeSection.
     */
    public CAIListTypeSection() throws Exception {
        this(new globaz.globall.db.BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeSection.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListTypeSection(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListTypeSection");
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_LIST_TYPE_SECTION"));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeSection.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListTypeSection(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListTypeSection", session
                .getLabel("TITLE_PRINT_LIST_TYPE_SECTION"));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeSection.
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
    public CAIListTypeSection(globaz.globall.db.BSession session, String filenameRoot, String companyName,
            String documentTitle) throws FWIException {
        super(session, filenameRoot, companyName);
        super.setDocumentTitle(documentTitle);
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        super._headerText();
        // Exercice (date)
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        // Titre du document
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6005"));

    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la dernière page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        super.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("NUMERO"));
        super.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("DESCRIPTION"));
        super.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("NUMERO_SEQUENCE_CONTENTIEUX"));
        super.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("NOM_CLASSE"));
        super.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("NOM_PAGE_DETAIL"));
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
            getDocumentInfo().setDocumentTypeNumber(CAIListTypeSection.NUMERO_REFERENCE_INFOROM);
            setTemplateFile("CAIListTypeSection");
            super.getImporter().setBeanCollectionDataSource(m_container);
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
        CATypeSectionManager manager = null;
        BTransaction transaction = null;
        BStatement statement = null;
        try {
            manager = new CATypeSectionManager();
            manager.setSession(getSession());
            transaction = super.getTransaction();
            statement = manager.cursorOpen(transaction);
            CATypeSection entity = null;
            while ((entity = (CATypeSection) manager.cursorReadNext(statement)) != null) {
                m_container.add(new CATypeSectionBean(entity));
            }
        } finally {
            try {
                if (manager != null) {
                    manager.cursorClose(statement);
                    statement = null;
                }
            } finally {
                if ((transaction != null) && transaction.isOpened()) {
                    transaction.closeTransaction();
                }
            }
        }
        _headerText();
        _tableHeader();
        _getRefParam();
    }

    /**
     * Cette méthode retourne l'idTypeSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeSection() {
        return idTypeSection;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Cette méthode permet d'attribuer l'idTypeSection.
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdTypeSection(java.lang.String newIdTypeSection) {
        idTypeSection = newIdTypeSection;
    }

}
