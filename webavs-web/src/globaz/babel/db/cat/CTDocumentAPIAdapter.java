package globaz.babel.db.cat;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * La classe utilisée par le helper de l'interface ICTDocument de Babel.
 * </p>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTDocumentAPIAdapter
 */
public class CTDocumentAPIAdapter extends CTTexte {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @param schema
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String createFromClause(String schema) {
        StringBuffer fromClause = new StringBuffer(CTTexte.createFromClause(schema));

        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(CTDocument.TABLE_NAME_DOCUMENT);
        fromClause.append(" ON ");
        fromClause.append(CTElement.FIELDNAME_ID_DOCUMENT);
        fromClause.append("=");
        fromClause.append(CTDocument.FIELDNAME_ID_DOCUMENT);

        fromClause.append(" INNER JOIN ");
        fromClause.append(schema);
        fromClause.append(CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT);
        fromClause.append(" ON ");
        fromClause.append(CTDocument.FIELDNAME_ID_TYPE_DOCUMENT);
        fromClause.append("=");
        fromClause.append(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);

        return fromClause.toString();
    }

    private Boolean actif = Boolean.TRUE;
    private String csDomaine = "";
    private String csTypeDocument = "";
    private String dateDesactivation = "";
    private Boolean isStyledDocument = Boolean.FALSE;
    private String noCaisse = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String nom = "";

    /**
     * retourne faux.
     * 
     * @return false
     */
    @Override
    protected boolean _allowAdd() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return false
     */
    @Override
    protected boolean _allowDelete() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return false
     */
    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /**
     * retourne faux.
     * 
     * @return false.
     */
    @Override
    protected boolean _autoInherits() {
        return false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return createFromClause(_getCollection());
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return super._getTableName();
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);
        csDomaine = statement.dbReadNumeric(CTTypeDocument.FIELDNAME_CS_DOMAINE);
        csTypeDocument = statement.dbReadNumeric(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
        actif = statement.dbReadBoolean(CTDocument.FIELDNAME_ACTIF);
        dateDesactivation = statement.dbReadDateAMJ(CTDocument.FIELDNAME_DATE_DESACTIVATION);
        nom = statement.dbReadString(CTDocument.FIELDNAME_NOM);
        isStyledDocument = statement.dbReadBoolean(CTDocument.FIELDNAME_IS_DOCUMENT_STYLE);
        noCaisse = CaisseHelperFactory.getInstance().getNoCaisse(
                statement.getTransaction().getSession().getApplication());

    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nope
    }

    /**
     * getter pour l'attribut actif.
     * 
     * @return la valeur courante de l'attribut actif
     */
    public Boolean getActif() {
        return actif;
    }

    /**
     * getter pour l'attribut cs domaine.
     * 
     * @return la valeur courante de l'attribut cs domaine
     */
    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * getter pour l'attribut cs type document.
     * 
     * @return la valeur courante de l'attribut cs type document
     */
    public String getCsTypeDocument() {
        return csTypeDocument;
    }

    /**
     * getter pour l'attribut date desactivation.
     * 
     * @return la valeur courante de l'attribut date desactivation
     */
    public String getDateDesactivation() {
        return dateDesactivation;
    }

    /**
     * @return
     */
    public Boolean getIsStyledDocument() {
        return isStyledDocument;
    }

    /**
     * @return
     */
    public String getNoCaisse() {
        return noCaisse;
    }

    /**
     * getter pour l'attribut nom.
     * 
     * @return la valeur courante de l'attribut nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idDocument
     *            DOCUMENT ME!
     * @param csDomaine
     *            DOCUMENT ME!
     * @param csTypeDocument
     *            DOCUMENT ME!
     * @param csDestinataire
     *            DOCUMENT ME!
     * @param defaut
     *            DOCUMENT ME!
     * @param actif
     *            DOCUMENT ME!
     * @param nom
     *            DOCUMENT ME!
     * @param codeIsoLangue
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public BManager load(String idDocument, String csDomaine, String csTypeDocument, String csDestinataire,
            String defaut, String actif, String nom, String nomLike, String codeIsoLangue) throws Exception {
        CTDocumentAPIAdapterManager mgr = new CTDocumentAPIAdapterManager();

        mgr.setSession(getSession());
        mgr.setForIdDocument(idDocument);
        mgr.setForCsDomaine(csDomaine);
        mgr.setForCsTypeDocument(csTypeDocument);
        mgr.setForCsDestinataire(csDestinataire);
        mgr.setForDefaut("".equals(defaut) ? null : Boolean.valueOf(defaut));
        mgr.setForActif("".equals(actif) ? null : Boolean.valueOf(actif));
        mgr.setForCodeIsoLangue(codeIsoLangue);
        mgr.setForNom(nom);
        mgr.setForNomLike(nomLike);
        mgr.find(BManager.SIZE_NOLIMIT);

        return mgr;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idDocument
     *            DOCUMENT ME!
     * @param csDomaine
     *            DOCUMENT ME!
     * @param csTypeDocument
     *            DOCUMENT ME!
     * @param csDestinataire
     *            DOCUMENT ME!
     * @param defaut
     *            DOCUMENT ME!
     * @param actif
     *            DOCUMENT ME!
     * @param nom
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public BManager loadListeNoms(String idDocument, String csDomaine, String csTypeDocument, String csDestinataire,
            String defaut, String actif, String nom) throws Exception {
        CTDocumentManager mgr = new CTDocumentManager();

        mgr.setSession(getSession());
        mgr.setForIdDocument(idDocument);
        mgr.setForCsDomaine(csDomaine);
        mgr.setForCsTypeDocument(csTypeDocument);
        mgr.setForCsDestinataire(csDestinataire);
        mgr.setForDefaut("".equals(defaut) ? null : Boolean.valueOf(defaut));
        mgr.setForActif("".equals(actif) ? null : Boolean.valueOf(actif));
        mgr.setForNom(nom);
        mgr.find();

        return mgr;
    }

    /**
     * setter pour l'attribut actif.
     * 
     * @param actif
     *            une nouvelle valeur pour cet attribut
     */
    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    /**
     * setter pour l'attribut cs domaine.
     * 
     * @param csDomaine
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /**
     * setter pour l'attribut cs type document.
     * 
     * @param csTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsTypeDocument(String csTypeDocument) {
        this.csTypeDocument = csTypeDocument;
    }

    /**
     * setter pour l'attribut date desactivation.
     * 
     * @param dateDesactivation
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDesactivation(String dateDesactivation) {
        this.dateDesactivation = dateDesactivation;
    }

    /**
     * @param boolean1
     */
    public void setIsStyledDocument(Boolean boolean1) {
        isStyledDocument = boolean1;
    }

    /**
     * @param string
     */
    public void setNoCaisse(String string) {
        noCaisse = string;
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

}
