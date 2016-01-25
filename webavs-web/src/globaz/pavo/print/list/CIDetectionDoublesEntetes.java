package globaz.pavo.print.list;

import globaz.framework.printing.itext.dynamique.FWIAbstractDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JAVector;
import com.lowagie.text.DocumentException;

/**
 * La classe <code>FWIAbstractManagerDocumentList</code> est une classe abstraite fournissant le squelette pour g�n�rer
 * un rapport contenant une liste via un manager
 * 
 * @author Emmanuel Fleury
 */
public abstract class CIDetectionDoublesEntetes extends FWIAbstractDocumentList {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee = "";
    // GROUPING
    private JAVector groupProperties = new JAVector();
    private BEntity lastEntity = null;
    private JAVector lastGroupValues = new JAVector();
    // GLOBALL Framework
    private BManager manager = null;

    /**
     * Constructeur du type FWIAbstractManagerDocumentList.
     */
    public CIDetectionDoublesEntetes() {
        super();
    }

    /**
     * Constructeur du type FWIAbstractManagerDocumentList.
     * 
     * @param session
     *            la session utilis�e
     * @param filenameRoot
     *            la racine du nom du document g�n�r�
     * @param companyName
     *            le nom de la soci�t�
     * @param documentTitle
     *            le titre du document
     * @param manager
     *            le manager � utiliser
     */
    public CIDetectionDoublesEntetes(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager) {
        this(session, filenameRoot, companyName, documentTitle, manager, null);
    }

    /**
     * Constructeur du type FWIAbstractManagerDocumentList.
     * 
     * @param session
     *            la session utilis�e
     * @param filenameRoot
     *            la racine du nom du document g�n�r�
     * @param companyName
     *            le nom de la soci�t�
     * @param documentTitle
     *            le titre du document
     * @param manager
     *            le manager � utiliser
     * @param applicationId
     *            l'id de l'application
     */
    public CIDetectionDoublesEntetes(BSession session, String filenameRoot, String companyName, String documentTitle,
            BManager manager, String applicationId) {
        super(session, filenameRoot, companyName, documentTitle, applicationId);
        setManager(manager);
    }

    /**
     * Constructeur du type FWIAbstractManagerDocumentList.
     * 
     * @param filenameRoot
     *            la racine du nom du document g�n�r�
     * @param companyName
     *            le nom de la soci�t�
     * @param documentTitle
     *            le titre du document
     * @param applicationId
     *            l'id de l'application
     */
    public CIDetectionDoublesEntetes(String filenameRoot, String companyName, String documentTitle, String applicationId) {
        super(filenameRoot, companyName, documentTitle, applicationId);
    }

    /**
     * Ajoute une colonne centr�e dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     */
    protected final void _addColumnCenter(String columnName) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, CENTER);
    }

    /**
     * Ajoute une colonne centr�e dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     * @param width
     *            la largeur de la colonne
     */
    protected final void _addColumnCenter(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, CENTER);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * Ajoute une colonne align�e � gauche dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     */
    protected final void _addColumnLeft(String columnName) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, LEFT);
    }

    /**
     * Ajoute une colonne align�e � gauche dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     * @param width
     *            la largeur de la colonne
     */
    protected final void _addColumnLeft(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, LEFT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * Ajoute une colonne align�e � droite dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     */
    protected final void _addColumnRight(String columnName) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, RIGHT);
    }

    /**
     * Ajoute une colonne align�e � droite dans la table de donn�es
     * 
     * @param columnName
     *            le nom de la colonne
     * @param width
     *            la largeur de la colonne
     */
    protected final void _addColumnRight(String columnName, int width) {
        _addDataTableColumn(columnName);
        _setDataTableColumnFormat(_getDataTableColumnCount() - 1);
        _setDataTableColumnAlignment(_getDataTableColumnCount() - 1, RIGHT);
        _setDataTableColumnWidth(_getDataTableColumnCount() - 1, width);
    }

    /**
     * Ex�cute des traitements apr�s insertion d'une ligne de d�tail
     * 
     * @param entity
     *            l'entit� en cours de lecture
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
     * Ex�cute des traitements apr�s insertion d'une ligne de d�tail
     * 
     * @param entity
     *            l'entit� en cours de lecture
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

    /**
     * Ex�cute les d�buts de groupes
     * 
     * @param entity
     *            l'entit� en cours de lecture
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
                            _addDataTableGroupRow();
                        } catch (DocumentException e) {
                            throw new FWIException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Remplit la table de donn�es
     */
    @Override
    protected final void _bindDataTable() throws FWIException {
        getDocumentInfo().setDocumentTypeNumber("0179CCI");
        try {
            _initializeData();
            _getManager().setSession(getSession());
            _getManager().find(BManager.SIZE_NOLIMIT);
            initializeTable();
            _setDataTableModel();
            setProgressScaleValue(_getManager().size());
            for (int i = 0; i < _getManager().size(); i++) {
                BEntity entity = (BEntity) _getManager().getEntity(i);
                _beforeAddRow(entity);
                addRow(entity);
                _addDataTableRow();
                _afterAddRow(entity);
                incProgressCounter();
            }
            _endGrouping(null);
            summary();
            _addDataTableGroupRow();
            if (_getManager().size() == 0) {
                // HACK: comme iText n'ajoute pas la table si elle ne contient
                // pas de ligne de d�tail,
                // on modifie les lignes d'ent�te en lignes de d�tail
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
     * Ex�cute les fins de groupes
     * 
     * @param entity
     *            l'entit� en cours de lecture
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
                            _addDataTableGroupRow();
                        } catch (DocumentException e) {
                            throw new FWIException(e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.04.2002 09:38:14)
     */
    @Override
    public void _executeCleanUp() {
        super._executeCleanUp();
        setManager(null);
        groupProperties = new JAVector();
        lastGroupValues = new JAVector();
        lastEntity = null;
    }

    /**
     * Renvoie la valeur de groupage d'une entit�
     * 
     * @param level
     *            le niveau de rupture
     * @param entity
     *            l'entit� en cours de lecture
     */
    private final Object _getGroupValue(int level, BEntity entity) {
        try {
            Object item = groupProperties.elementAt(level);
            if (entity != null) {
                if (item != null) {
                    // groupage via des m�thodes
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
     * Renvoie la derni�re entit� trait�e
     * 
     * @return la derni�re entit� trait�e, �v. null
     */
    protected final BEntity _getLastEntity() {
        return lastEntity;
    }

    /**
     * Renvoie le manager utilis�
     * 
     * @return le manager
     */
    protected final BManager _getManager() {
        return getManager();
    }

    /**
     * Renvoie la valeur d'une propri�t�
     * 
     * @return la valeur de la propri�t�, �v. null
     * @param entity
     *            l'entit� contenant la propri�t�
     * @param methodName
     *            le nom de la m�thode renvoyant la valeur
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
     * Ajoute un groupage "manuel" (devra �tre trait� dans <code>getGroupValue()</code>)
     */
    protected final void groupManual() {
        groupProperties.add(null);
        lastGroupValues.add(null);
    }

    /**
     * Ajoute une m�thode dans les propri�t�s de groupage
     * 
     * @param methodName
     *            le nom de la m�thode � appeler
     */
    protected final void _groupOnMethod(String methodName) {
        groupProperties.add(methodName);
        lastGroupValues.add(null);
    }

    /**
     * Indique si le rapport contient une table de donn�es
     * 
     * @return true si le rapport contient une table de donn�es, false sinon
     */
    @Override
    protected boolean _hasDataTable() {
        return (_getManager() != null);
    }

    /**
     * Initialise les donn�es du rapport
     */
    private final void _initializeData() {
        groupProperties.clear();
        lastGroupValues.clear();
        lastEntity = null;
    }

    /**
     * Indique si une entit� provoque une rupture
     * 
     * @param level
     *            le niveau de rupture
     * @param entity
     *            l'entit� en cours de lecture
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

    /**
     * Ajoute une ligne dans la table de donn�es (utiliser <code>_addCell(Object)</code>)
     * 
     * @param entity
     *            l'entit� contenant les donn�es
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected abstract void addRow(BEntity entity) throws FWIException;

    /**
     * Effectue les op�rations apr�s ajout d'une ligne de d�tail
     * 
     * @param entity
     *            l'entit� en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void afterAdd(BEntity entity) throws FWIException {
    }

    /**
     * Effectue les op�rations avant ajout d'une ligne de d�tail
     * 
     * @param entity
     *            l'entit� en cours de lecture
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void beforeAdd(BEntity entity) throws FWIException {
    }

    /**
     * Effectue le traitement de d�but de groupage
     * 
     * @param level
     *            le niveau de groupage
     * @param lastEntity
     *            la derni�re entit� ins�r�e
     * @param nextEntity
     *            la prochaine entit� qui sera ins�r�e
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void beginGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
    }

    /**
     * Remplit le footer de la page de garde
     * 
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    @Override
    protected void bindHeaderPageFooter() throws FWIException {
    }

    /**
     * Remplit le footer de page
     * 
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    @Override
    protected void bindPageFooter() throws FWIException {
    }

    /**
     * Effectue le traitement de fin de groupage
     * 
     * @param level
     *            le niveau de groupage
     * @param lastEntity
     *            la derni�re entit� ins�r�e
     * @param nextEntity
     *            la prochaine entit� qui sera ins�r�e
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void endGroup(int level, BEntity lastEntity, BEntity nextEntity) throws FWIException {
    }

    public String getForAnnee() {
        return forAnnee;
    }

    /**
     * Renvoie la valeur de groupage d'une entit� pour un niveau de groupage donn�
     * 
     * @return la valeur de groupage de l'entit�
     * @param level
     *            le niveau de groupage
     * @param entity
     *            l'entit�
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected Object getGroupValue(int level, BEntity entity) throws FWIException {
        return null;
    }

    /**
     * Renvoie le manager utilis�
     * 
     * @return le manager
     */
    public final BManager getManager() {
        return manager;
    }

    public boolean hasEcritureSuperieurMinimun() {
        return true;
    }

    /**
     * Initialise la table des donn�es
     * <p>
     * <u>Utilisation</u>:
     * <ul>
     * <li><code>_addColumn(..)</code> permet de d�clarer les colonnes
     * <li><code>_group...(..)</code> permet de d�clarer les groupages
     * </ul>
     */
    protected abstract void initializeTable();

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    /**
     * D�finit le manager � utiliser
     * 
     * @param manager
     *            le manager
     */
    public final void setManager(BManager manager) {
        this.manager = manager;
    }

    /**
     * R�sum� de la table de donn�es (utiliser <code>_addCell(Object)</code>)
     * 
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    protected void summary() throws FWIException {
    }

}
