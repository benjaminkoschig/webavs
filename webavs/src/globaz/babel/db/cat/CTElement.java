/*
 * Cr�� le 13 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.db.ICTCompteurBorne;
import globaz.babel.dump.CTInsertQueryBuilder;
import globaz.babel.dump.ICTExportableSQL;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un �l�ment regroupe des {@link globaz.babel.db.cat.CTTexte textes} s�mantiquement identiques mais �crits dans
 * des langues diff�rentes. Un �l�ment est par exemple un paragraphe ou une ligne d'un
 * {@link globaz.babel.db.cat.CTDocument document}. Chaque �l�ment poss�de une valeur de niveau et une valeur de
 * position. Le niveau repr�sente en gros la section (titre, en-t�te, corps, ...) du document dans lequel
 * l'�l�ment doit appara�tre. La position repr�sente l'ordre dans lequel les �l�ments doivent appara�tre
 * � ce niveau.
 * </p>
 * 
 * @author vre
 * @see globaz.prestation.db.cattxt.CTElementManager
 */
public class CTElement extends BEntity implements ICTCompteurBorne, ICTExportableSQL {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DESCRIPTION = "CCBDES";
    public static final String FIELDNAME_ID_DOCUMENT = "CCIDOC";
    public static final String FIELDNAME_ID_ELEMENT = "CCIELE";
    public static final String FIELDNAME_IS_EDITABLE = "CCBEDI";
    public static final String FIELDNAME_IS_SELECTABLE = "CCBSEL";
    public static final String FIELDNAME_IS_SELECTED_BY_DEFAULT = "CCBSBD";
    public static final String FIELDNAME_NIVEAU = "CCNNIV";
    public static final String FIELDNAME_POSITION = "CCNPOS";
    public static final String FIELDNAME_PSPY = "PSPY";
    public static final String TABLE_NAME_ELEMENTS = "CTELEMEN";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String borneInferieure = "0";
    private String description = "";
    private String idDocument = "";
    private String idElement = "";
    private Boolean isEditable = Boolean.FALSE;
    private Boolean isSelectable = Boolean.FALSE;
    private Boolean isSelectedByDefault = Boolean.FALSE;
    private String niveau = "";
    private String position = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        CTDocument doc = new CTDocument();
        doc.setSession(getSession());
        doc.setIdDocument(getIdDocument());
        doc.retrieve();
        idElement = new BigDecimal(_incCounter(transaction, "0", TABLE_NAME_ELEMENTS, doc.getCsTypeDocument(), "0"))
                .add(new BigDecimal(getBorneInferieure())).toString();
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // effacements des textes pour cet element
        CTTexteManager mgr = new CTTexteManager();

        mgr.setSession(getSession());
        mgr.setForIdElement(idElement);
        mgr.find(transaction);

        for (int idTexte = mgr.size(); --idTexte >= 0;) {
            ((BEntity) mgr.get(idTexte)).delete(transaction);
        }
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_ELEMENTS;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idElement = statement.dbReadNumeric(FIELDNAME_ID_ELEMENT);
        idDocument = statement.dbReadNumeric(FIELDNAME_ID_DOCUMENT);
        niveau = statement.dbReadNumeric(FIELDNAME_NIVEAU);
        position = statement.dbReadNumeric(FIELDNAME_POSITION);
        description = statement.dbReadString(FIELDNAME_DESCRIPTION);
        isSelectable = statement.dbReadBoolean(FIELDNAME_IS_SELECTABLE);
        isSelectedByDefault = statement.dbReadBoolean(FIELDNAME_IS_SELECTED_BY_DEFAULT);
        isEditable = statement.dbReadBoolean(FIELDNAME_IS_EDITABLE);
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), position, getSession().getLabel("POSITION_REQUISE"));
        _propertyMandatory(statement.getTransaction(), niveau, getSession().getLabel("NIVEAU_REQUIS"));
        if (isSelectable.booleanValue() || isEditable.booleanValue()) {
            _propertyMandatory(statement.getTransaction(), description, getSession().getLabel("DESCRIPTION_REQUISE"));
        }
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_ELEMENT, _dbWriteNumeric(statement.getTransaction(), idElement));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_ID_ELEMENT, _dbWriteNumeric(statement.getTransaction(), idElement));
        statement.writeField(FIELDNAME_ID_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), idDocument));
        statement.writeField(FIELDNAME_POSITION, _dbWriteNumeric(statement.getTransaction(), position));
        statement.writeField(FIELDNAME_NIVEAU, _dbWriteNumeric(statement.getTransaction(), niveau));
        statement.writeField(FIELDNAME_DESCRIPTION, _dbWriteString(statement.getTransaction(), description));
        statement.writeField(FIELDNAME_IS_SELECTABLE,
                _dbWriteBoolean(statement.getTransaction(), isSelectable, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(FIELDNAME_IS_SELECTED_BY_DEFAULT,
                _dbWriteBoolean(statement.getTransaction(), isSelectedByDefault, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(FIELDNAME_IS_EDITABLE,
                _dbWriteBoolean(statement.getTransaction(), isEditable, BConstants.DB_TYPE_BOOLEAN_CHAR));
        // note: tous les champs qui sont rajoutés doivent l'être également
        // dans la méthode export()
    }

    /**
     * DOCUMENT ME!
     * 
     * @param query
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     */
    @Override
    public void export(CTInsertQueryBuilder query, BTransaction transaction) {
        query.setTableName(TABLE_NAME_ELEMENTS);
        query.setSchema(_getCollection());
        query.setParentIdColName(FIELDNAME_ID_DOCUMENT);
        query.setSelfIdColName(FIELDNAME_ID_ELEMENT);

        query.addCol(FIELDNAME_POSITION, _dbWriteNumeric(transaction, position));
        query.addCol(FIELDNAME_NIVEAU, _dbWriteNumeric(transaction, niveau));
        query.addCol(FIELDNAME_DESCRIPTION, _dbWriteString(transaction, description));
        query.addCol(FIELDNAME_IS_SELECTABLE,
                _dbWriteBoolean(transaction, isSelectable, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_IS_SELECTED_BY_DEFAULT,
                _dbWriteBoolean(transaction, isSelectedByDefault, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_IS_EDITABLE, _dbWriteBoolean(transaction, isEditable, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_PSPY, _dbWriteSpy(transaction, getSpy()));
    }

    /**
     * @see globaz.babel.db.cat.ICTCompteurBorne#getBorneInferieure()
     */
    @Override
    public String getBorneInferieure() {
        return borneInferieure;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id document.
     * 
     * @return la valeur courante de l'attribut id document
     */
    public String getIdDocument() {
        return idDocument;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id element.
     * 
     * @return la valeur courante de l'attribut id element
     */
    public String getIdElement() {
        return idElement;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public Boolean getIsEditable() {
        return isEditable;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public Boolean getIsSelectable() {
        return isSelectable;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public Boolean getIsSelectedByDefault() {
        return isSelectedByDefault;
    }

    /**
     * getter pour l'attribut niveau.
     * 
     * @return la valeur courante de l'attribut niveau
     */
    public String getNiveau() {
        return niveau;
    }

    /**
     * getter pour l'attribut position.
     * 
     * @return la valeur courante de l'attribut position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @see globaz.babel.db.cat.ICTCompteurBorne#setBorneInferieure(java.lang.String)
     */
    @Override
    public void setBorneInferieure(String borneInferieure) {
        this.borneInferieure = borneInferieure;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut id document.
     * 
     * @param idDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut id element.
     * 
     * @param idElement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdElement(String idElement) {
        this.idElement = idElement;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @param boolean1
     */
    public void setIsEditable(Boolean boolean1) {
        isEditable = boolean1;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @param boolean1
     */
    public void setIsSelectable(Boolean boolean1) {
        isSelectable = boolean1;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @param boolean1
     */
    public void setIsSelectedByDefault(Boolean boolean1) {
        isSelectedByDefault = boolean1;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut niveau.
     * 
     * @param niveau
     *            une nouvelle valeur pour cet attribut
     */
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut position.
     * 
     * @param position
     *            une nouvelle valeur pour cet attribut
     */
    public void setPosition(String position) {
        this.position = position;
    }

}
