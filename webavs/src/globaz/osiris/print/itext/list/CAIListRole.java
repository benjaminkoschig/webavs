package globaz.osiris.print.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CARoleManager;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Insérez la description du type ici. Date de création : (06.03.2002 11:30:24)
 * 
 * @author: Administrator
 */
public class CAIListRole extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class RoleBean {
        String m_roleDescription = "";
        String m_roleId = "";

        public RoleBean(CARole entity) {
            m_roleId = entity.getIdRole();
            m_roleDescription = entity.getDescription();
        }

        public String getCOL_1() {
            return m_roleId;
        }

        public String getCOL_2() {
            return m_roleDescription;
        }
    }

    private static final String NUMERO_REFERENCE_INFOROM = "0103GCA";
    private java.lang.String idRole = new String();

    private Collection m_container = new ArrayList();

    /**
     * Commentaire relatif au constructeur CAIListRole.
     */
    public CAIListRole() throws Exception {
        this(new globaz.globall.db.BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Commentaire relatif au constructeur CAIListRole.
     * 
     * @param session
     *            globaz.globall.db.BProcess
     */
    public CAIListRole(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "ListRole");
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_LIST_ROLES"));
    }

    /**
     * Commentaire relatif au constructeur CAIListRole.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListRole(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "test", session.getLabel("TITLE_PRINT_LIST_ROLES"));
    }

    /**
     * Commentaire relatif au constructeur CAIListRole.
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
    public CAIListRole(globaz.globall.db.BSession session, String filenameRoot, String companyName, String documentTitle)
            throws FWIException {
        super(session, filenameRoot, "CAIListRole");
        super.setDocumentTitle(documentTitle);
    }

    @Override
    protected void _headerText() {
        // Exercice (date)
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        // Titre du document
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6007"));
        super._headerText();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        super.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("NUMERO"));
        super.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("DESCRIPTION"));
    }

    // Création du paramètre de référence pour les documents de type liste

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */
    @Override
    public void beforeBuildReport() {
        // Attribution du nom du document
        try {
            getDocumentInfo().setDocumentTypeNumber(CAIListRole.NUMERO_REFERENCE_INFOROM);
            setTemplateFile("CAIListRole");
            JRBeanCollectionDataSource newSource = new JRBeanCollectionDataSource(m_container);
            super.setDataSource(newSource);
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
        setIdRole(id);
        super.executeProcess();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.itext.print.FWIImportManager#_createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        CARoleManager manager = null;
        BTransaction transaction = null;
        BStatement statement = null;
        CARole entity = null;
        try {
            manager = new CARoleManager();
            manager.setSession(getSession());
            if (!JadeStringUtil.isBlank(getIdRole())) {
                manager.setFromIdRole(getIdRole());
            }
            transaction = getTransaction();
            statement = manager.cursorOpen(transaction);
            while ((entity = (CARole) manager.cursorReadNext(statement)) != null) {
                m_container.add(new RoleBean(entity));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * Cette méthode retourne l'idRole.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRole() {
        return idRole;
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
     * Cette méthode permet d'attribuer l'idRole.
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdRole(java.lang.String newIdRole) {
        idRole = newIdRole;
    }

}
