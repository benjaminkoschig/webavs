/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.db.ICTCompteurBorne;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cet entity définit un type de document, c'est-à-dire un couple (domaine d'application <-> type de document). Par
 * exemple, tous les {@link globaz.prestation.db.cattxt.CTDocument document} du type (domaine=APG, type=decision) sont
 * censes se rapporter aux documents de décisions pour les droits APG.
 * </p>
 * 
 * <p>
 * Note: il est interdit d'ajouter, modifier ou effacer un type document. La population de la base doit se faire au
 * moment de sa création dans l'application.
 * </p>
 * 
 * @author vre
 * @see globaz.prestation.db.cattxt.CTTypeDocumentManager
 */
public class CTTypeDocument extends BEntity implements ICTCompteurBorne {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_BORNE_INFERIEURE = "CANBIN";
    public static final String FIELDNAME_CS_DOMAINE = "CATDOM";
    public static final String FIELDNAME_CS_TYPE_DOCUMENT = "CATTYP";
    public static final String FIELDNAME_ID_GROUPE_ANNEXE = "CAIGAN";
    public static final String FIELDNAME_ID_TYPE_DOCUMENT = "CAITYD";
    public static final String TABLE_NAME_TYPE_DOCUMENT = "CTTYPDOC";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String borneInferieure = "0";
    private String csDomaine = "";
    private String csTypeDocument = "";
    private String idGroupeAnnexe = "";
    private String idTypeDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * retourne faux.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowAdd()
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowDelete()
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_allowUpdate()
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME_TYPE_DOCUMENT;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        csDomaine = statement.dbReadNumeric(FIELDNAME_CS_DOMAINE);
        csTypeDocument = statement.dbReadNumeric(FIELDNAME_CS_TYPE_DOCUMENT);
        idTypeDocument = statement.dbReadNumeric(FIELDNAME_ID_TYPE_DOCUMENT);
        idGroupeAnnexe = statement.dbReadNumeric(FIELDNAME_ID_GROUPE_ANNEXE);
        borneInferieure = statement.dbReadNumeric(FIELDNAME_BORNE_INFERIEURE);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_ID_TYPE_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), idTypeDocument));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // on ne devrait arriver ici car l'ajout et la modification sont
        // interdits mais si jamais...
        statement.writeField(FIELDNAME_ID_TYPE_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), idTypeDocument));
        statement.writeField(FIELDNAME_ID_GROUPE_ANNEXE, _dbWriteNumeric(statement.getTransaction(), idGroupeAnnexe));
        statement.writeField(FIELDNAME_CS_DOMAINE, _dbWriteNumeric(statement.getTransaction(), csDomaine));
        statement.writeField(FIELDNAME_CS_TYPE_DOCUMENT, _dbWriteNumeric(statement.getTransaction(), csTypeDocument));
        statement.writeField(FIELDNAME_BORNE_INFERIEURE, _dbWriteNumeric(statement.getTransaction(), borneInferieure));
    }

    /**
     * getter pour l'attribut borne inferieure
     * 
     * @return la valeur courante de l'attribut borne inferieure
     */
    @Override
    public String getBorneInferieure() {
        return borneInferieure;
    }

    /**
     * getter pour l'attribut cs domaine
     * 
     * @return la valeur courante de l'attribut cs domaine
     */
    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * getter pour l'attribut cs type document
     * 
     * @return la valeur courante de l'attribut cs type document
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * @return
     */
    public String getIdGroupeAnnexe() {
        return idGroupeAnnexe;
    }

    /**
     * getter pour l'attribut id type document
     * 
     * @return la valeur courante de l'attribut id type document
     */
    public String getIdTypeDocument() {
        return idTypeDocument;
    }

    /**
     * setter pour l'attribut borne inferieure
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setBorneInferieure(String string) {
        borneInferieure = string;
    }

    /**
     * setter pour l'attribut cs domaine
     * 
     * @param csDomaine
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /**
     * setter pour l'attribut cs type document
     * 
     * @param csTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDocument(String csTypeDocument) {
        this.csTypeDocument = csTypeDocument;
    }

    /**
     * @param string
     */
    public void setIdGroupeAnnexe(String string) {
        idGroupeAnnexe = string;
    }

    /**
     * setter pour l'attribut id type document
     * 
     * @param idTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTypeDocument(String idTypeDocument) {
        this.idTypeDocument = idTypeDocument;
    }

}
