package globaz.osiris.print.itext.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.OsirisDef;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.contentieux.CAParametreEtapeManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (06.03.2002 11:30:24)
 * 
 * @author: Administrator
 */
public class CAIListSequenceContentieux extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean hasNext = true;
    private java.lang.String idParametreEtape = new String();
    private Vector m_container = new Vector();

    /**
     * Commentaire relatif au constructeur CAIListSequenceContentieux.
     */
    public CAIListSequenceContentieux() throws Exception {
        super(new globaz.globall.db.BSession(OsirisDef.DEFAULT_APPLICATION_OSIRIS), CAApplication.DEFAULT_OSIRIS_ROOT,
                "ListSequenceContentieux");
        super.setDocumentTitle(getSession().getLabel("TITLE_LIST_SEQUENCE_CONTENTIEUX"));
    }

    public CAIListSequenceContentieux(BProcess parent) throws FWIException {
        this(parent, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListSequenceContentieux");
        super.setDocumentTitle(getSession().getLabel("TITLE_PRINT_SEQUENCE_CONTENTIEUX"));
    }

    /**
     * Constructor for CAIListSequenceContentieux.
     * 
     * @param parent
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListSequenceContentieux(BProcess parent, String rootApplication, String fileName) throws FWIException {
        super(parent, rootApplication, fileName);
    }

    /**
     * Commentaire relatif au constructeur CAIListSequenceContentieux.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CAIListSequenceContentieux(BSession session) throws FWIException {
        this(session, CAApplication.DEFAULT_OSIRIS_ROOT, "CAIListSequenceContentieux");
        super.setDocumentTitle(session.getLabel("TITLE_PRINT_SEQUENCE_CONTENTIEUX"));
    }

    /**
     * Constructor for CAIListSequenceContentieux.
     * 
     * @param session
     * @param rootApplication
     * @param fileName
     * @throws FWIException
     */
    public CAIListSequenceContentieux(BSession session, String rootApplication, String fileName) throws FWIException {
        super(session, rootApplication, fileName);
    }

    // Création du paramètre de référence pour les documents de type liste
    private String _getRefParam() {
        try {
            SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy ' - ' HH:mm");
            StringBuffer refBuffer = new StringBuffer(getSession().getLabel("REFERENCE") + " ");
            refBuffer.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1));
            refBuffer.append(" (");
            refBuffer.append(formater.format(new Date()));
            refBuffer.append(")");
            refBuffer.append(" - ");
            refBuffer.append(getSession().getUserId());
            return refBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode pour insérer les constantes qui s'affiche dans la première page Utiliser super.setParametres(Key, Value)
     */
    protected void _headerText() {
        this.setParametres(
                FWIImportParametre.PARAM_COMPANYNAME,
                FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                        ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        // Exercice (date)
        super.setParametres(FWIImportParametre.PARAM_EXERCICE, globaz.globall.util.JACalendar.todayJJsMMsAAAA());
        // Titre du document
        super.setDocumentTitle(getSession().getLabel("6008"));

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
        setColumnHeader(1, getSession().getLabel("ORDRE_SEQUENCE"));
        setColumnHeader(2, getSession().getLabel("ETAPE"));
        setColumnHeader(3, getSession().getLabel("DELAI_SEQUENCE"));
        setColumnHeader(4, getSession().getLabel("IMPUTER_TAXE"));
        setColumnHeader(5, getSession().getLabel("NOM_CLASSE_IMPL"));
        setColumnHeader(6, getSession().getLabel("DATEREFERENCE"));
        setColumnHeader(7, getSession().getLabel("MONTANT_LIMITE"));
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
            setTemplateFile("CAIListSequenceContentieux");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Ajout des références en bas de liste
        super.setParametres(FWIImportParametre.PARAM_REFERENCE, _getRefParam());
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
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource ()
     */
    @Override
    public void createDataSource() throws Exception {
        super.setDataSource(m_container);
    }

    /**
     * Cette méthode retourne l'idParametreEtape.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdParametreEtape() {
        return idParametreEtape;
    }

    /**
     * Returns the hasNext.
     * 
     * @return boolean
     */
    public boolean isHasNext() {
        return hasNext;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (!isHasNext()) {
            return false;
        }
        CAParametreEtapeManager manager = null;
        CAIListSequenceContentieuxBean bean = null;
        BTransaction transaction = null;
        BStatement statement = null;
        try {
            manager = new CAParametreEtapeManager();
            manager.setSession(getSession());
            transaction = getTransaction();
            transaction.openTransaction();
            statement = manager.cursorOpen(transaction);
            while ((bean = (CAIListSequenceContentieuxBean) manager.cursorReadNext(statement)) != null) {
                m_container.add(bean);
            }
            return true;
        } catch (Exception e) {
            super._addError(e.getMessage());
            JadeLogger.error(this, e);
            return false;
        } finally {
            try {
                if (manager != null) {
                    try {
                        manager.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
                setHasNext(false);
            } catch (Exception e) {
            } finally {
                if (transaction != null) {
                    try {
                        transaction.closeTransaction();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#returnDocument()
     */
    @Override
    public void returnDocument() throws FWIException {
        super.imprimerListDocument();
    }

    /**
     * Sets the hasNext.
     * 
     * @param hasNext
     *            The hasNext to set
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * Cette méthode permet d'attribuer l'idParametreEtape.
     * 
     * @param newIdParametreEtape
     *            java.lang.String
     */
    public void setIdParametreEtape(java.lang.String newIdParametreEtape) {
        idParametreEtape = newIdParametreEtape;
    }

}
