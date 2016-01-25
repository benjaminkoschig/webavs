package globaz.pavo.print.list;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAVector;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import com.lowagie.text.DocumentException;

public class CIImprimerListJournauxPartielsProcess extends FWIAbstractDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    private String forDate = "";
    private String fordateInscription = "";
    private String forIdType = "";
    private String fromUser = "";
    private JAVector groupProperties = new JAVector();

    private BEntity lastEntity = null;
    private JAVector lastGroupValues = new JAVector();
    private String likeIdAffiliation = "";
    private BManager manager = null;

    public CIImprimerListJournauxPartielsProcess() {
        super();
    }

    public CIImprimerListJournauxPartielsProcess(BSession session, String filenameRoot, String companyName,
            String documentTitle, BManager manager, String applicationId) {
        super(session, filenameRoot, companyName, documentTitle, applicationId);
        setManager(manager);
    }

    public CIImprimerListJournauxPartielsProcess(String filenameRoot, String companyName, String documentTitle,
            String applicationId) {
        super(filenameRoot, companyName, documentTitle, applicationId);
    }

    /**
     * Ajoute une colonne alignée à gauche dans la table de données
     * 
     * @param columnName
     *            le nom de la colonne
     */
    protected final void _addColumnLeft(String columnName) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
    }

    /**
     * Ajoute une colonne alignée à gauche dans la table de données
     * 
     * @param columnName
     *            le nom de la colonne
     * @param width
     *            la largeur de la colonne
     */
    protected final void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, FWIAbstractDocumentList.LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * Exécute des traitements après insertion d'une ligne de détail
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    private final void _afterAddRow(BEntity entity) throws FWIException {
        afterAdd(entity);
        for (int level = 0; level < groupProperties.size(); level++) {
            lastGroupValues.setElementAt(_getGroupValue(level, entity), level);
        }
        lastEntity = entity;
    }

    /**
     * Exécute des traitements après insertion d'une ligne de détail
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    private final void _beforeAddRow(BEntity entity) throws FWIException {
        if (_getLastEntity() != null) {
            _endGrouping(entity);
        }
        _beginGrouping(entity);
        beforeAdd(entity);
    }

    @Override
    public void _beforeExecuteReport() {

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));
        _setDocumentTitle(getSession().getLabel("MSG_JOURNAUX_PARTIEL"));

        CIJournalManager manager = (CIJournalManager) getManager();
        manager.setSession(getSession());
        manager.setForIdEtat(CIJournal.CS_PARTIEL);
        manager.setForAnneeCotisation(getForAnnee());
        manager.setForIdTypeInscription(getForIdType());
        manager.setLikeIdAffiliation(getLikeIdAffiliation());
        manager.setForDate(getForDate());
        manager.setFromUser(getFromUser());
        manager.setOrderBy("KCDATE");
        manager.setFordateInscription(getFordateInscription());
    }

    /**
     * Exécute les débuts de groupes
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    private final void _beginGrouping(BEntity entity) throws FWIException {
        int firstLevel = -1;
        for (int level = 0; level < groupProperties.size(); level++) {
            if (_isGroupBreak(level, entity)) {
                firstLevel = level;
                break;
            }
        }
        if (firstLevel != -1) {
            for (int level = 0; level < groupProperties.size(); level++) {
                if (level >= firstLevel) {
                    // rupture
                    beginGroup(level, _getLastEntity(), entity);
                    if (newRow != null) {
                        try {
                            this._addDataTableGroupRow();
                        } catch (DocumentException e) {
                            throw new FWIException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Remplit la table de données
     */
    @Override
    protected final void _bindDataTable() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber("xxxxCCI");
        try {
            _initializeData();
            getManager().setSession(getSession());
            getManager().find(BManager.SIZE_NOLIMIT);
            initializeTable();
            _setDataTableModel();
            setProgressScaleValue(getManager().size());
            for (int i = 0; i < getManager().size(); i++) {
                BEntity entity = (BEntity) getManager().getEntity(i);
                _beforeAddRow(entity);
                addRow(entity);
                this._addDataTableRow();
                _afterAddRow(entity);
                incProgressCounter();
            }
            _endGrouping(null);
            summary();
            this._addDataTableGroupRow();
            if (getManager().size() == 0) {
                // HACK: comme iText n'ajoute pas la table si elle ne contient
                // pas de ligne de détail,
                // on modifie les lignes d'entête en lignes de détail
                _getDataTable().setHeaderRows(0);
                // pushTable();
            }
        } catch (Exception e) {
            if (!(e instanceof FWIException)) {
                throw new FWIException(e);
            }
        }
    }

    /**
     * Exécute les fins de groupes
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    private final void _endGrouping(BEntity entity) throws FWIException {
        int firstLevel = -1;
        for (int level = 0; level < groupProperties.size(); level++) {
            if (_isGroupBreak(level, entity)) {
                firstLevel = level;
                break;
            }
        }
        if (firstLevel != -1) {
            for (int level = groupProperties.size() - 1; level >= 0; level--) {
                if (level >= firstLevel) {
                    // rupture
                    endGroup(level, _getLastEntity(), entity);
                    if (newRow != null) {
                        try {
                            this._addDataTableGroupRow();
                        } catch (DocumentException e) {
                            throw new FWIException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Renvoie la valeur de groupage d'une entité
     * 
     * @param level
     *            le niveau de rupture
     * @param entity
     *            l'entité en cours de lecture
     */
    private final Object _getGroupValue(int level, BEntity entity) {
        try {
            Object item = groupProperties.elementAt(level);
            if (entity != null) {
                if (item != null) {
                    // groupage via des méthodes
                    return _getPropertyValue(entity, (String) item);
                } else {
                    // groupage "manuel"
                    return getGroupValue(level, entity);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Renvoie la dernière entité traitée
     * 
     * @return la dernière entité traitée, év. null
     */
    protected final BEntity _getLastEntity() {
        return lastEntity;
    }

    /**
     * Renvoie la valeur d'une propriété
     * 
     * @return la valeur de la propriété, év. null
     * @param entity
     *            l'entité contenant la propriété
     * @param methodName
     *            le nom de la méthode renvoyant la valeur
     */
    private final Object _getPropertyValue(BEntity entity, String methodName) {
        try {
            java.lang.reflect.Method method = entity.getClass().getMethod(methodName, new Class[] {});
            return method.invoke(entity, new Object[] {});
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Indique si le rapport contient une table de données
     * 
     * @return true si le rapport contient une table de données, false sinon
     */
    @Override
    protected boolean _hasDataTable() {
        return (getManager() != null);
    }

    /**
     * Initialise les données du rapport
     */
    private final void _initializeData() {

        getDocumentInfo().setDocumentTypeNumber("0250CCI");
        groupProperties.clear();
        lastGroupValues.clear();
        lastEntity = null;
    }

    /**
     * Indique si une entité provoque une rupture
     * 
     * @param level
     *            le niveau de rupture
     * @param entity
     *            l'entité en cours de lecture
     */
    protected final boolean _isGroupBreak(int level, BEntity entity) {
        try {
            Object lastValue = lastGroupValues.elementAt(level);
            Object nextValue = _getGroupValue(level, entity);
            boolean groupBreak = false;
            if ((lastValue == null) && (nextValue != null)) {
                groupBreak = true;
            } else if ((lastValue != null) && (nextValue == null)) {
                groupBreak = true;
            } else if ((nextValue != null) && (!nextValue.equals(lastValue))) {
                groupBreak = true;
            }
            return groupBreak;
        } catch (Exception e) {
            return false;
        }
    }

    protected void addRow(BEntity entity) throws FWIException {
        CIJournal journal = (CIJournal) entity;
        _addCell(journal.getDateFormatee());
        _addCell(journal.getIdJournal());
        _addCell(journal.getDescription());
        _addCell(journal.getAnneeCotisation());
        _addCell(journal.getTotalInscritFormat());
        _addCell(journal.getRefExterneFacturation());
    }

    /**
     * Effectue les opérations après ajout d'une ligne de détail
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void afterAdd(BEntity entity) throws FWIException {
    }

    /**
     * Effectue les opérations avant ajout d'une ligne de détail
     * 
     * @param entity
     *            l'entité en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void beforeAdd(BEntity entity) throws FWIException {
    }

    /**
     * Effectue le traitement de début de groupage
     * 
     * @param level
     *            le niveau de groupage
     * @param lastEntity
     *            la dernière entité insérée
     * @param nextEntity
     *            la prochaine entité qui sera insérée
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
    }

    /**
     * Effectue le traitement de fin de groupage
     * 
     * @param level
     *            le niveau de groupage
     * @param lastEntity
     *            la dernière entité insérée
     * @param nextEntity
     *            la prochaine entité qui sera insérée
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void endGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.04.2002 10:06:37)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            StringBuffer buffer = new StringBuffer("L'impression du document '");
            buffer.append(getSession().getLabel("MSG_JOURNAUX_PARTIEL"));
            buffer.append("' s'est terminée en erreur");
            return buffer.toString();
        } else {
            StringBuffer buffer = new StringBuffer("L'impression du document '");
            buffer.append(getSession().getLabel("MSG_JOURNAUX_PARTIEL"));
            buffer.append("' s'est terminée avec succès");
            return buffer.toString();
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForDate() {
        return forDate;
    }

    public String getFordateInscription() {
        return fordateInscription;
    }

    public String getForIdType() {
        return forIdType;
    }

    public String getFromUser() {
        return fromUser;
    }

    /**
     * Renvoie la valeur de groupage d'une entité pour un niveau de groupage donné
     * 
     * @return la valeur de groupage de l'entité
     * @param level
     *            le niveau de groupage
     * @param entity
     *            l'entité
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected Object getGroupValue(int level, BEntity entity) throws FWIException {
        return null;
    }

    public String getLikeIdAffiliation() {
        return likeIdAffiliation;
    }

    public BManager getManager() {
        return manager;
    }

    protected void initializeTable() {
        this._addColumnLeft(getSession().getLabel("TITRE_JOURNAUX_PARTIEL_DATE"), 1000);
        this._addColumnLeft(getSession().getLabel("TITRE_JOURNAUX_PARTIEL_NUMERO"), 1000);
        this._addColumnLeft(getSession().getLabel("TITRE_JOURNAUX_PARTIEL_DESCR"), 7000);
        this._addColumnLeft(getSession().getLabel("DT_ANNEE_COTISATION"), 2000);
        this._addColumnLeft(getSession().getLabel("TITRE_JOURNAUX_PARTIEL_REVENU"), 2000);
        this._addColumnLeft(getSession().getLabel("TITRE_JOURNAUX_PARTIEL_NO_FACTURE"), 2000);
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setFordateInscription(String fordateInscription) {
        this.fordateInscription = fordateInscription;
    }

    public void setForIdType(String forIdType) {
        this.forIdType = forIdType;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setLikeIdAffiliation(String likeIdAffiliation) {
        this.likeIdAffiliation = likeIdAffiliation;
    }

    public void setManager(BManager manager) {
        this.manager = manager;
    }

    /**
     * Résumé de la table de données (utiliser <code>_addCell(Object)</code>)
     * 
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void summary() throws FWIException {
    }
}
