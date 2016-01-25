package globaz.babel.db.cat;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * utilisé par les interfaces.
 * </p>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTDocumentAPIAdapter
 */
public class CTDocumentAPIAdapterManager extends CTTexteManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forActif = null;
    private String forCsDestinataire = "";
    private String forCsDomaine = "";
    private String forCsTypeDocument = "";
    private Boolean forDefaut = null;
    private String forNom = "";
    private String forNomLike = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
        return CTDocumentAPIAdapter.createFromClause(_getCollection());
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
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer(super._getWhere(statement));

        if (!JadeStringUtil.isEmpty(forCsDomaine)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTTypeDocument.FIELDNAME_CS_DOMAINE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forCsDomaine));
        }

        if (!JadeStringUtil.isEmpty(forCsTypeDocument)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forCsTypeDocument));
        }

        if (!JadeStringUtil.isEmpty(forCsDestinataire)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_CS_DESTINATAIRE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forCsDestinataire));
        }

        if (!JadeStringUtil.isEmpty(forNom)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_NOM);
            whereClause.append("=");
            whereClause.append(_dbWriteString(statement.getTransaction(), forNom));
        }

        if (!JadeStringUtil.isEmpty(forNomLike)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_NOM);
            whereClause.append(" like ");
            whereClause.append(_dbWriteString(statement.getTransaction(), forNomLike + "%"));
        }

        if (forActif != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_ACTIF);
            whereClause.append("=");
            whereClause.append(_dbWriteBoolean(statement.getTransaction(), forActif, BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (forDefaut != null) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_DEFAULT);
            whereClause.append("=");
            whereClause.append(_dbWriteBoolean(statement.getTransaction(), forDefaut, BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        return whereClause.toString();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocumentAPIAdapter();
    }

    /**
     * getter pour l'attribut for actif.
     * 
     * @return la valeur courante de l'attribut for actif
     */
    public Boolean getForActif() {
        return forActif;
    }

    /**
     * getter pour l'attribut for cs destinataire.
     * 
     * @return la valeur courante de l'attribut for cs destinataire
     */
    public String getForCsDestinataire() {
        return forCsDestinataire;
    }

    /**
     * getter pour l'attribut for cs domaine.
     * 
     * @return la valeur courante de l'attribut for cs domaine
     */
    public String getForCsDomaine() {
        return forCsDomaine;
    }

    /**
     * getter pour l'attribut for cs type document.
     * 
     * @return la valeur courante de l'attribut for cs type document
     */
    public String getForCsTypeDocument() {
        return forCsTypeDocument;
    }

    /**
     * getter pour l'attribut for defaut.
     * 
     * @return la valeur courante de l'attribut for defaut
     */
    public Boolean getForDefaut() {
        return forDefaut;
    }

    /**
     * getter pour l'attribut for nom.
     * 
     * @return la valeur courante de l'attribut for nom
     */
    public String getForNom() {
        return forNom;
    }

    public String getForNomLike() {
        return forNomLike;
    }

    /**
     * setter pour l'attribut for actif.
     * 
     * @param forActif
     *            une nouvelle valeur pour cet attribut
     */
    public void setForActif(Boolean forActif) {
        this.forActif = forActif;
    }

    /**
     * setter pour l'attribut for cs destinataire.
     * 
     * @param forCsDestinataire
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsDestinataire(String forCsDestinataire) {
        this.forCsDestinataire = forCsDestinataire;
    }

    /**
     * setter pour l'attribut for cs domaine.
     * 
     * @param forCsDomaine
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsDomaine(String forCsDomaine) {
        this.forCsDomaine = forCsDomaine;
    }

    /**
     * setter pour l'attribut for cs type document.
     * 
     * @param forCsTypeDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsTypeDocument(String forCsTypeDocument) {
        this.forCsTypeDocument = forCsTypeDocument;
    }

    /**
     * setter pour l'attribut for defaut.
     * 
     * @param forDefaut
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDefaut(Boolean forDefaut) {
        this.forDefaut = forDefaut;
    }

    /**
     * setter pour l'attribut for nom.
     * 
     * @param forNom
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNom(String forNom) {
        this.forNom = forNom;
    }

    public void setForNomLike(String forNomLike) {
        this.forNomLike = forNomLike;
    }
}
