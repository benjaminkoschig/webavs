package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CATypeOperation;
import globaz.osiris.db.comptes.CATypeOperationManager;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (06.03.2002 11:30:24)
 * 
 * @author: Administrator
 */
public class CAIListTypeOperation extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class CATypeOperationBean {
        String description = null;
        String idTypeOperation = null;
        String nomClasse = null;
        String nomPageDetail = null;

        CATypeOperationBean(CATypeOperation _typeOperation) {
            // COL_1 --> Type
            idTypeOperation = _typeOperation.getIdTypeOperation();
            // COL_2 --> Description
            description = _typeOperation.getDescription();
            // COL_3 --> Nom de la classe
            nomClasse = _typeOperation.getNomClasse();
            // COL_4 --> Nom de la page de d�tail
            nomPageDetail = _typeOperation.getNomPageDetail();
        }

        /**
         * Returns the idTypeOperation.
         * 
         * @return String
         */
        public String getCOL_1() {
            return idTypeOperation;
        }

        /**
         * Returns the description.
         * 
         * @return String
         */
        public String getCOL_2() {
            return description;
        }

        /**
         * Returns the nomClasse.
         * 
         * @return String
         */
        public String getCOL_3() {
            return nomClasse;
        }

        /**
         * Returns the nomPageDetail.
         * 
         * @return String
         */
        public String getCOL_4() {
            return nomPageDetail;
        }

    }

    private static final String NUMERO_REFERENCE_INFOROM = "0143GCA";
    private java.lang.String idTypeOperation = new String();

    private Collection m_container = new ArrayList();

    /**
     * Commentaire relatif au constructeur CAIListTypeOperation.
     */
    public CAIListTypeOperation() throws Exception {
        this(new globaz.globall.db.BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeOperation.
     * 
     * @param session
     *            globaz.globall.db.BProcess
     */
    public CAIListTypeOperation(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListTypeOperation");
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_LIST_TYPE_OPERATION"));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeOperation.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListTypeOperation(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListTypeOperation", session
                .getLabel("TITLE_PRINT_LIST_TYPE_OPERATION"));
    }

    /**
     * Commentaire relatif au constructeur CAIListTypeOperation.
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
    public CAIListTypeOperation(globaz.globall.db.BSession session, String filenameRoot, String companyName,
            String documentTitle) throws FWIException {
        super(session, filenameRoot, companyName);
        super.setDocumentTitle(documentTitle);
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche dans la premi�re page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        // Exercice (date)
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        // Titre du document
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6006"));
        super._headerText();
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche dans la derni�re page Utiliser super.setParametres(Key, Value)
     */
    protected void _summaryText() {
    }

    /**
     * Methode pour ins�rer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        super.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("TYPE"));
        super.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("DESCRIPTION"));
        super.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("NOM_CLASSE"));
        super.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("NOM_PAGE_DETAIL"));
    }

    /**
     * Derni�re m�thode lanc� avant la cr�ation du document par JasperReport Dernier minute pour fournir le nom du
     * rapport � utiliser avec la m�thode setTemplateFile(String) et si n�cessaire le type de document � sortir avec la
     * m�thode setFileType(String [PDF|CSV|HTML|XSL]) par d�faut PDF Date de cr�ation : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Attribution du nom du document
        try {
            getDocumentInfo().setDocumentTypeNumber(CAIListTypeOperation.NUMERO_REFERENCE_INFOROM);
            setTemplateFile("CAIListTypeOperation");
            super.getImporter().setBeanCollectionDataSource(m_container);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.04.2002 10:22:45)
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
        CATypeOperationManager manager = null;
        BTransaction transaction = null;
        BStatement statement = null;
        try {
            manager = new CATypeOperationManager();
            manager.setSession(getSession());
            transaction = super.getTransaction();
            statement = manager.cursorOpen(transaction);
            CATypeOperation entity = null;
            while ((entity = (CATypeOperation) manager.cursorReadNext(statement)) != null) {
                m_container.add(new CATypeOperationBean(entity));
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
     * Cette m�thode retourne l'idTypeOperation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdTypeOperation() {
        return idTypeOperation;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Cette m�thode permet d'attribuer l'idTypeOperation.
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdTypeOperation(java.lang.String newIdTypeOperation) {
        idTypeOperation = newIdTypeOperation;
    }

}
