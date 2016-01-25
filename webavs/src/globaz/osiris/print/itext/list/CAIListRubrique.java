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
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Insérez la description du type ici. Date de création : (06.03.2002 11:30:24)
 * 
 * @author: Administrator
 */
public class CAIListRubrique extends CAIListFactory {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class CARubriqueBean {
        String idExterne = null;
        String m_alias = null;
        String m_annee = null;
        String m_compteCourant = null;
        String m_compteur = null;
        String m_description = null;
        String m_nature = null;
        String m_numeroCotti = null;
        String m_secteur = null;
        String m_ventilation = null;

        public CARubriqueBean(CARubrique _rubrique) {
            idExterne = _rubrique.getIdExterne();
            m_description = _rubrique.getDescription();
            m_nature = _rubrique.getCsNatureRubrique().getCurrentCodeUtilisateur().getLibelle();
            if (_rubrique.getEstVentilee().booleanValue()) {
                m_ventilation = "X";
            } else {
                m_ventilation = "";
            }
            if (_rubrique.getTenirCompteur().booleanValue()) {
                m_compteur = "X";
            } else {
                m_compteur = "";
            }
            m_alias = _rubrique.getAlias();
            if (_rubrique.getAnneeCotisation().equals("0")) {
                m_annee = "";
            } else {
                m_annee = _rubrique.getAnneeCotisation();
            }
            m_secteur = _rubrique.getIdSecteur();
            if (_rubrique.getIdContrepartie().equals("0")) {
                m_compteCourant = "";
            } else {
                m_compteCourant = _rubrique.getCompteCourant().getRubrique().getIdExterne();
            }
            if (_rubrique.getNumCompteCG().equals("0")) {
                m_numeroCotti = "";
            } else {
                m_numeroCotti = _rubrique.getNumCompteCG();
            }

        }

        /**
         * Returns the idExterne.
         * 
         * @return String
         */
        public String getCOL_1() {
            return idExterne;
        }

        /**
         * Returns the numeroCotti.
         * 
         * @return String
         */
        public String getCOL_10() {
            return m_numeroCotti;
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
         * Returns the nature.
         * 
         * @return String
         */
        public String getCOL_3() {
            return m_nature;
        }

        /**
         * Returns the ventilation.
         * 
         * @return String
         */
        public String getCOL_4() {
            return m_ventilation;
        }

        /**
         * Returns the compteur.
         * 
         * @return String
         */
        public String getCOL_5() {
            return m_compteur;
        }

        /**
         * Returns the alias.
         * 
         * @return String
         */
        public String getCOL_6() {
            return m_alias;
        }

        /**
         * Returns the annee.
         * 
         * @return String
         */
        public String getCOL_7() {
            return m_annee;
        }

        /**
         * Returns the secteur.
         * 
         * @return String
         */
        public String getCOL_8() {
            return m_secteur;
        }

        /**
         * Returns the compteCourant.
         * 
         * @return String
         */
        public String getCOL_9() {
            return m_compteCourant;
        }

    }

    private static final String NUMERO_REFERENCE_INFOROM = "0017GCA";
    private java.lang.String idRubrique = new String();

    private Collection m_container = new ArrayList();

    /**
     * Commentaire relatif au constructeur CAIListRubrique.
     */
    public CAIListRubrique() throws Exception {
        this(new globaz.globall.db.BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS));
    }

    public CAIListRubrique(BProcess parent) throws FWIException {
        super(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListRubrique");
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_LIST_RUBRIQUE"));
    }

    /**
     * Commentaire relatif au constructeur CAIListRubrique.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListRubrique(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListRubrique");
    }

    /**
     * Commentaire relatif au constructeur CAIListRubrique.
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
    public CAIListRubrique(globaz.globall.db.BSession session, String filenameRoot, String fileName)
            throws FWIException {
        super(session, filenameRoot, fileName);
        super.setDocumentTitle(session.getLabel("TITLE_PRINT_LIST_RUBRIQUE"));
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    @Override
    protected void _headerText() {
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        super.setParametres(FWIImportParametre.PARAM_TITLE, getSession().getLabel("6004"));
        super._headerText();
    }

    /**
     * Methode pour insérer les constantes qui s'affiche le nom des colonnes Utiliser super.setParametres(Key, Value)
     */
    protected void _tableHeader() {
        super.setParametres(FWIImportParametre.getCol(1), getSession().getLabel("NUMERO"));
        super.setParametres(FWIImportParametre.getCol(2), getSession().getLabel("DESCRIPTION"));
        super.setParametres(FWIImportParametre.getCol(3), getSession().getLabel("NATURE_RUBRIQUE"));
        super.setParametres(FWIImportParametre.getCol(4), getSession().getLabel("VENTILATION"));
        super.setParametres(FWIImportParametre.getCol(5), getSession().getLabel("TENIR_COMPTEUR"));
        super.setParametres(FWIImportParametre.getCol(6), getSession().getLabel("ALIAS"));
        super.setParametres(FWIImportParametre.getCol(7), getSession().getLabel("ANNEE_COTISATION"));
        super.setParametres(FWIImportParametre.getCol(8), getSession().getLabel("SECTEUR"));
        super.setParametres(FWIImportParametre.getCol(9), getSession().getLabel("COMPTECOURANT"));
        super.setParametres(FWIImportParametre.getCol(10), getSession().getLabel("NUMERO_COMPTE_CG"));
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
            getDocumentInfo().setDocumentTypeNumber(CAIListRubrique.NUMERO_REFERENCE_INFOROM);
            setTemplateFile("CAIListRubrique");
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
        // CARubriqueManager
        CARubriqueManager manager = null;
        BTransaction transaction = null;
        BStatement statement = null;
        try {
            manager = new CARubriqueManager();
            manager.setSession(getSession());
            manager.setOrderBy(CARubriqueManager.ORDER_IDEXTERNE);
            transaction = super.getTransaction();
            statement = manager.cursorOpen(transaction);
            CARubrique entity = null;
            while ((entity = (CARubrique) manager.cursorReadNext(statement)) != null) {
                m_container.add(new CARubriqueBean(entity));
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
     * Cette méthode retourne l'idRubrique.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRubrique() {
        return idRubrique;
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
     * Cette méthode permet d'attribuer l'idRubrique.
     * 
     * @param newIdRubrique
     *            java.lang.String
     */
    public void setIdRubrique(java.lang.String newIdRubrique) {
        idRubrique = newIdRubrique;
    }

}
