/*
 * Cr�� le 13 juil. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.api.ICTDocument;
import globaz.babel.application.CTApplication;
import globaz.babel.db.ICTCompteurBorne;
import globaz.babel.dump.CTInsertQueryBuilder;
import globaz.babel.dump.ICTExportableSQL;
import globaz.babel.utils.CTUserUtils;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import java.math.BigDecimal;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cet entity d�finit un document, c'est-�-dire un groupe {@link globaz.prestation.db.cattxt.CTElement d'elements}.
 * Un document est d'un certain {@link globaz.prestation.db.cattxt.CTTypeDocument type}. Un document poss�de
 * �galement un nom qui permet de le distinguer des autres documents du m�me
 * {@link globaz.prestation.db.cattxt.CTTypeDocument type}.
 * </p>
 * 
 * <p>
 * Un document peut etre desactive. Par d�faut, l'insertion d'un nouveau document pour un type donne et un nom donne
 * entrainera la desactivation de tous les autres documents du meme type et ayant le meme nom. Ce comportement peut etre
 * desactive en utilisant la methode {@link #wantDesactivation(boolean) wantDesactivation}
 * </p>
 * 
 * <p>
 * Un document peut �tre {@link #getDocumentEditable() �ditable}. Dans ce cas, son nom, et son statut actif ou non
 * actif peut �tre modifi� par tout le monde. Dans le cas contraire, seuls les administrateurs peuvent le faire.
 * </p>
 * 
 * <p>
 * Les textes d'un document peuvent �tre {@link #getTextesEditables() �ditables}. Dans ce cas les textes peuvent
 * �tre modifi�s par tout le monde. Dans le cas contraire, seuls les administrateurs peuvent changer les textes.
 * </p>
 * 
 * <p>
 * Pour des raisons de simplicit�, cet entity est toujours charg� en effectuant une jointure avec le type de
 * document qui lui est associ� (relation 1-1).
 * </p>
 * 
 * @author vre
 * @see globaz.prestation.db.cattxt.CTDocumentManager
 */
public class CTDocument extends CTTypeDocument implements ICTCompteurBorne, ICTExportableSQL {

    // ~ Static fields/initializers
    // ---------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_ACTIF = "CBBACT";
    public static final String FIELDNAME_CS_DESTINATAIRE = "CBTDES";
    public static final String FIELDNAME_CS_EDITABLE = "CBTEDI";
    public static final String FIELDNAME_DATE_DESACTIVATION = "CBDDES";
    public static final String FIELDNAME_DEFAULT = "CBBDEF";
    public static final String FIELDNAME_ID_DOCUMENT = "CBIDOC";
    public static final String FIELDNAME_ID_TYPE_DOCUMENT = "CBITYD";
    public static final String FIELDNAME_IS_DOCUMENT_STYLE = "CBBSTY";
    public static final String FIELDNAME_NOM = "CBLNOM";
    public static final String FIELDNAME_PSPY = "PSPY";
    public static final String TABLE_NAME_DOCUMENT = "CTDOCUME";

    // ~ Instance fields
    // --------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFieldsClause(String schema) {
        StringBuffer fieldsClause = new StringBuffer();

        fieldsClause.append(FIELDNAME_ACTIF);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_CS_DOMAINE);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_CS_EDITABLE);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_CS_TYPE_DOCUMENT);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_DATE_DESACTIVATION);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_ID_DOCUMENT);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_ID_GROUPE_ANNEXE);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_CS_DESTINATAIRE);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_DEFAULT);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_IS_DOCUMENT_STYLE);
        fieldsClause.append(",");
        fieldsClause.append(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        fieldsClause.append(",");
        fieldsClause.append(CTTypeDocument.FIELDNAME_BORNE_INFERIEURE);
        fieldsClause.append(",");
        fieldsClause.append(FIELDNAME_NOM);
        fieldsClause.append(",");
        fieldsClause.append(schema);
        fieldsClause.append(TABLE_NAME_DOCUMENT);
        fieldsClause.append(".");
        fieldsClause.append(BSpy.FIELDNAME);

        return fieldsClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static final String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer();

        fromClause.append(schema);
        fromClause.append(TABLE_NAME_DOCUMENT);

        // jointure avec la table des type d'en-tetes de catalogue
        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(TABLE_NAME_TYPE_DOCUMENT);
        fromClause.append(" ON ");
        fromClause.append(FIELDNAME_ID_TYPE_DOCUMENT);
        fromClause.append("=");
        fromClause.append(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);

        return fromClause.toString();
    }

    private Boolean actif = Boolean.TRUE;
    private String csDestinataire = ICTDocument.CS_ASSURE;
    private String csEditable = ICTDocument.CS_EDITABLE;
    private String dateDesactivation = "";
    private Boolean defaut = Boolean.FALSE;
    private boolean desactivation = true;

    private String idDocument = "";

    // ~ Methods
    // ----------------------------------------------------------------------------------------------------------

    private Boolean isStyledDocument = Boolean.FALSE;

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    private String nom = "";

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * retourne vrai.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return true;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * retourne vrai.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return true;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * retourne vrai.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return true;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * retourne faux (on ne veut pas mettre a jour la table des types de documents).
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
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // d�sactivation d'un ancien document s'il existe
        if (desactivation) {
            CTDocumentManager documents = new CTDocumentManager();

            documents.setSession(getSession());
            documents.setForIdTypeDocument(getIdTypeDocument());
            documents.setForCsDestinataire(csDestinataire);
            documents.setForNom(nom);
            documents.setForActif(Boolean.TRUE);
            documents.find();

            for (int idDocument = documents.size(); --idDocument >= 0;) {
                CTDocument document = (CTDocument) documents.get(idDocument);

                if (ICTDocument.CS_EDITABLE.equals(document.getCsEditable()) || isAdministrateur()) {
                    document.setActif(Boolean.FALSE);
                    document.setDateDesactivation(JACalendar.todayJJsMMsAAAA());
                    document.update(transaction);
                } else {
                    _addError(transaction, getSession().getLabel("DOCUMENT_NON_EDITABLE"));
                }
            }
        }

        idDocument = new BigDecimal(_incCounter(transaction, "0", TABLE_NAME_DOCUMENT, getCsTypeDocument(), "0")).add(
                new BigDecimal(getBorneInferieure())).toString();
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

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
        // effacement des elements de ce document
        CTElementManager mgr = new CTElementManager();

        mgr.setSession(getSession());
        mgr.setForIdDocument(idDocument);
        mgr.find(transaction);

        for (int idElement = mgr.size(); --idElement >= 0;) {
            ((BEntity) mgr.get(idElement)).delete(transaction);
        }
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

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
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        // effacement de la date de d�sactivation si le statut est actif
        if (actif.booleanValue()) {
            dateDesactivation = "";
        } else {
            if (JAUtil.isDateEmpty(dateDesactivation)) {
                dateDesactivation = JACalendar.todayJJsMMsAAAA();
            }
        }
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return createFieldsClause(_getCollection());
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_DOCUMENT;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        dateDesactivation = statement.dbReadDateAMJ(FIELDNAME_DATE_DESACTIVATION);
        actif = statement.dbReadBoolean(FIELDNAME_ACTIF);
        idDocument = statement.dbReadNumeric(FIELDNAME_ID_DOCUMENT);
        nom = statement.dbReadString(FIELDNAME_NOM);
        csEditable = statement.dbReadNumeric(FIELDNAME_CS_EDITABLE);
        csDestinataire = statement.dbReadNumeric(FIELDNAME_CS_DESTINATAIRE);
        defaut = statement.dbReadBoolean(FIELDNAME_DEFAULT);
        isStyledDocument = statement.dbReadBoolean(FIELDNAME_IS_DOCUMENT_STYLE);
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), nom, getSession().getLabel("NOM_DOCUMENT_REQUIS"));
        _propertyMandatory(statement.getTransaction(), getIdTypeDocument(),
                getSession().getLabel("TYPE_DOCUMENT_REQUIS"));

        // il ne peut y avoir qu'un seul document par defaut par type de
        // document et par destinataire
        if (defaut.booleanValue()) {
            CTDocumentManager documents = new CTDocumentManager();

            documents.setForIdTypeDocument(getIdTypeDocument());
            documents.setForCsDestinataire(csDestinataire);
            documents.setForDefaut(Boolean.TRUE);
            documents.setNotForIdDocument(idDocument);
            documents.setSession(getSession());

            if (documents.getCount() > 0) {
                _addError(statement.getTransaction(), getSession().getLabel("DOCUMENT_PAR_DEFAUT_UNIQUE"));
            }
        }
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), idDocument));
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_DATE_DESACTIVATION,
                _dbWriteDateAMJ(statement.getTransaction(), dateDesactivation));
        statement.writeField(FIELDNAME_ACTIF,
                _dbWriteBoolean(statement.getTransaction(), actif, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(FIELDNAME_ID_TYPE_DOCUMENT,
                _dbWriteNumeric(statement.getTransaction(), getIdTypeDocument()));
        statement.writeField(FIELDNAME_ID_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), idDocument));
        statement.writeField(FIELDNAME_NOM, _dbWriteString(statement.getTransaction(), nom));
        statement.writeField(FIELDNAME_CS_EDITABLE, _dbWriteNumeric(statement.getTransaction(), csEditable));
        statement.writeField(FIELDNAME_CS_DESTINATAIRE, _dbWriteNumeric(statement.getTransaction(), csDestinataire));
        statement.writeField(FIELDNAME_DEFAULT,
                _dbWriteBoolean(statement.getTransaction(), defaut, BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField(FIELDNAME_IS_DOCUMENT_STYLE,
                _dbWriteBoolean(statement.getTransaction(), isStyledDocument, BConstants.DB_TYPE_BOOLEAN_CHAR));

        // note: tous les champs ajoutés ici doivent être ajoutés dans la
        // méthode export egalement
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.babel.db.ICTExportableSQL#export(java.lang.String, java.lang.String, java.io.Writer)
     */
    @Override
    public void export(CTInsertQueryBuilder query, BTransaction transaction) {
        query.setTableName(TABLE_NAME_DOCUMENT);
        query.setSchema(_getCollection());
        query.setParentIdColName(FIELDNAME_ID_TYPE_DOCUMENT);
        query.setSelfIdColName(FIELDNAME_ID_DOCUMENT);

        query.addCol(FIELDNAME_DATE_DESACTIVATION, _dbWriteDateAMJ(transaction, dateDesactivation));
        query.addCol(FIELDNAME_ACTIF, _dbWriteBoolean(transaction, actif, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_NOM, _dbWriteString(transaction, nom));
        query.addCol(FIELDNAME_CS_EDITABLE, _dbWriteNumeric(transaction, csEditable));
        query.addCol(FIELDNAME_CS_DESTINATAIRE, _dbWriteNumeric(transaction, csDestinataire));
        query.addCol(FIELDNAME_DEFAULT, _dbWriteBoolean(transaction, defaut, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_IS_DOCUMENT_STYLE,
                _dbWriteBoolean(transaction, isStyledDocument, BConstants.DB_TYPE_BOOLEAN_CHAR));
        query.addCol(FIELDNAME_PSPY, _dbWriteSpy(transaction, getSpy()));
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut actif.
     * 
     * @return la valeur courante de l'attribut actif
     */
    public Boolean getActif() {
        return actif;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs destinataire.
     * 
     * @return la valeur courante de l'attribut cs destinataire
     */
    public String getCsDestinataire() {
        return csDestinataire;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut cs editable.
     * 
     * @return la valeur courante de l'attribut cs editable
     */
    public String getCsEditable() {
        return csEditable;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date desactivation.
     * 
     * @return la valeur courante de l'attribut date desactivation
     */
    public String getDateDesactivation() {
        return dateDesactivation;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut defaut.
     * 
     * @return la valeur courante de l'attribut defaut
     */
    public Boolean getDefaut() {
        return defaut;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id document.
     * 
     * @return la valeur courante de l'attribut id document
     */
    public String getIdDocument() {
        return idDocument;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public Boolean getIsStyledDocument() {
        return isStyledDocument;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut id role administrateur.
     * 
     * @return la valeur courante de l'attribut id role administrateur
     */
    public boolean isAdministrateur() {
        try {
            CTApplication application = (CTApplication) GlobazSystem
                    .getApplication(CTApplication.DEFAULT_APPLICATION_BABEL);

            return CTUserUtils.isUtilisateurARole(getSession(), application.getIdRoleAdministrateur());
        } catch (Exception e) {
            return false;
        }
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut desactivation.
     * 
     * @return la valeur courante de l'attribut desactivation
     */
    public boolean isDesactivation() {
        return desactivation;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut actif.
     * 
     * @param actif
     *            une nouvelle valeur pour cet attribut
     */
    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut cs destinataire.
     * 
     * @param csDestinataire
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsDestinataire(String csDestinataire) {
        this.csDestinataire = csDestinataire;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut cs editable.
     * 
     * @param csEditable
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEditable(String csEditable) {
        this.csEditable = csEditable;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut date desactivation.
     * 
     * @param dateDesactivation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDesactivation(String dateDesactivation) {
        this.dateDesactivation = dateDesactivation;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * setter pour l'attribut defaut.
     * 
     * @param defaut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    // ~
    // -----------------------------------------------------------------------------------------------------------------

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
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * @param boolean1
     */
    public void setIsStyledDocument(Boolean boolean1) {
        isStyledDocument = boolean1;
    }

    /**
     * setter pour l'attribut nom.
     * 
     * @param nom
     *            une nouvelle valeur pour cet attribut
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param desactivation
     *            DOCUMENT ME!
     */
    public void wantDesactivation(boolean desactivation) {
        this.desactivation = desactivation;
    }

}
