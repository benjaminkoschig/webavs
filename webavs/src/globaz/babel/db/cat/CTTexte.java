/*
 * Cr�� le 13 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.db.ICTCompteurBorne;
import globaz.babel.dump.ICTExportableSQL;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cet entity d�finit la valeur d'un texte. Les textes peuvent contenir des marqueurs du type {0} qui pourront �tre
 * remplac�s � l'ex�cution (java.text).
 * </p>
 * 
 * <p>
 * En raison du couplage assez fort qui existe entre les textes et l'�l�ment qui leur est associ�, les textes sont
 * toujours charg�s avec une jointure avec les �l�ments.
 * </p>
 * 
 * @author vre
 * @see globaz.prestation.db.cattxt.CTTexteManager
 * @see java.text.MessageFormat
 */
public class CTTexte extends CTElement implements ICTCompteurBorne, ICTExportableSQL {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_CODE_ISO_LANGUE = "CDLCIL";
    public static final String FIELDNAME_DESCRIPTION = "CDLDES";
    public static final String FIELDNAME_ID_ELEMENT = "CDIELE";
    public static final String FIELDNAME_ID_TEXTE = "CDITXT";
    public static final String FIELDNAME_PSPY = "PSPY";
    public static final String TABLE_NAME_TEXTES = "CTTEXTES";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_TEXTES);

        // jointure avec la table des elements
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_ELEMENTS);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_ELEMENT);
        fromClause.append("=");
        fromClause.append(CTElement.FIELDNAME_ID_ELEMENT);

        return fromClause.toString();
    }

    private String codeIsoLangue = "";
    private String description = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idTexte = "";

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * retourne faux (on ne veut pas que les �l�ments soient modifi�s).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_autoInherits()
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        CTElement element = new CTElement();
        element.setSession(getSession());
        element.setIdElement(getIdElement());
        element.retrieve();
        CTDocument doc = new CTDocument();
        doc.setSession(getSession());
        doc.setIdDocument(element.getIdDocument());
        doc.retrieve();

        idTexte = new BigDecimal(_incCounter(transaction, "0", TABLE_NAME_TEXTES, doc.getCsTypeDocument(), "0")).add(
                new BigDecimal(getBorneInferieure())).toString();
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // intentionnellement (et obligatoirement) vide:
        // comme on herite de CTElement, si cette methode n'est pas redefinie,
        // on boucle indefiniment
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_TEXTES;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        description = statement.dbReadString(FIELDNAME_DESCRIPTION);
        codeIsoLangue = statement.dbReadString(FIELDNAME_CODE_ISO_LANGUE);
        idTexte = statement.dbReadNumeric(FIELDNAME_ID_TEXTE);
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), codeIsoLangue, getSession().getLabel("LANGUE_REQUISE"));
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_TEXTE, idTexte);
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_CODE_ISO_LANGUE, _dbWriteString(statement.getTransaction(), codeIsoLangue));
        statement.writeField(FIELDNAME_DESCRIPTION, _dbWriteString(statement.getTransaction(), description));
        statement.writeField(FIELDNAME_ID_ELEMENT, _dbWriteNumeric(statement.getTransaction(), getIdElement()));
        statement.writeField(FIELDNAME_ID_TEXTE, _dbWriteNumeric(statement.getTransaction(), idTexte));

        // note: tous les champs qui sont rajoutes doivent l'être également
        // dans la méthode export()
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param query
     *            DOCUMENT ME!
     * @param transaction
     *            DOCUMENT ME!
     */
    @Override
    public void export(globaz.babel.dump.CTInsertQueryBuilder query, globaz.globall.db.BTransaction transaction) {
        query.setTableName(TABLE_NAME_TEXTES);
        query.setSchema(_getCollection());
        query.setParentIdColName(FIELDNAME_ID_ELEMENT);
        query.setSelfIdColName(FIELDNAME_ID_TEXTE);

        query.addCol(FIELDNAME_CODE_ISO_LANGUE, _dbWriteString(transaction, codeIsoLangue));
        query.addCol(FIELDNAME_DESCRIPTION, _dbWriteString(transaction, description));
        query.addCol(FIELDNAME_PSPY, _dbWriteSpy(transaction, getSpy()));
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut code iso langue.
     * 
     * @return la valeur courante de l'attribut code iso langue
     */
    public String getCodeIsoLangue() {
        return codeIsoLangue;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut description.
     * 
     * @return la valeur courante de l'attribut description
     */
    @Override
    public String getDescription() {
        return description;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id texte.
     * 
     * @return la valeur courante de l'attribut id texte
     */
    public String getIdTexte() {
        return idTexte;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut code iso langue.
     * 
     * @param codeIsoLangue
     *            une nouvelle valeur pour cet attribut
     */
    public void setCodeIsoLangue(String codeIsoLangue) {
        this.codeIsoLangue = codeIsoLangue;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut description.
     * 
     * @param description
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    // ~
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut id texte.
     * 
     * @param idTexte
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTexte(String idTexte) {
        this.idTexte = idTexte;
    }
}
