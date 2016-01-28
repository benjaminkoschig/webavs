/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.cat;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTDocument
 */
public class CTDocumentManager extends CTTypeDocumentManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forActif = null;
    private String forCsDestinataire = "";
    private Boolean forDefaut = null;
    private String forIdDocument = "";
    private String forNom = "";
    private String notForIdDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return CTDocument.createFieldsClause(_getCollection());
    }

    /**
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return CTDocument.createFromClause(_getCollection());
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

        if (!JadeStringUtil.isEmpty(forNom)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_NOM);
            whereClause.append("=");
            whereClause.append(_dbWriteString(statement.getTransaction(), forNom));
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

        if (!JadeStringUtil.isEmpty(forIdDocument)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_ID_DOCUMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdDocument));
        }

        if (!JadeStringUtil.isEmpty(forCsDestinataire)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_CS_DESTINATAIRE);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forCsDestinataire));
        }

        if (!JadeStringUtil.isEmpty(notForIdDocument)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTDocument.FIELDNAME_ID_DOCUMENT);
            whereClause.append("<>");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), notForIdDocument));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocument();
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
     * getter pour l'attribut for defaut.
     * 
     * @return la valeur courante de l'attribut for defaut
     */
    public Boolean getForDefaut() {
        return forDefaut;
    }

    /**
     * getter pour l'attribut for id document.
     * 
     * @return la valeur courante de l'attribut for id document
     */
    public String getForIdDocument() {
        return forIdDocument;
    }

    /**
     * getter pour l'attribut for nom.
     * 
     * @return la valeur courante de l'attribut for nom
     */
    public String getForNom() {
        return forNom;
    }

    /**
     * getter pour l'attribut not for id document.
     * 
     * @return la valeur courante de l'attribut not for id document
     */
    public String getNotForIdDocument() {
        return notForIdDocument;
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
     * setter pour l'attribut for defaut.
     * 
     * @param forDefaut
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDefaut(Boolean forDefaut) {
        this.forDefaut = forDefaut;
    }

    /**
     * setter pour l'attribut for id document.
     * 
     * @param forIdDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDocument(String forIdDocument) {
        this.forIdDocument = forIdDocument;
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

    /**
     * setter pour l'attribut not for id document.
     * 
     * @param notForIdDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setNotForIdDocument(String notForIdDocument) {
        this.notForIdDocument = notForIdDocument;
    }
}
