/*
 * Créé le 14 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.db.CTAbstractManager;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Un manager pour charger les catalogues de texte.
 * </p>
 * 
 * @author vre
 * @see globaz.babel.db.cat.CTElement
 */
public class CTElementManager extends CTAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forEditable = Boolean.FALSE;
    private String forIdDocument = "";
    private String forIdElement = "";
    private String forNiveau = "";
    private String forPosition = "";
    private Boolean forSelectable = Boolean.FALSE;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

        if (!JadeStringUtil.isEmpty(forIdElement)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_ID_ELEMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdElement));
        }

        if (!JadeStringUtil.isEmpty(forIdDocument)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_ID_DOCUMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdDocument));
        }

        if (!JadeStringUtil.isEmpty(forNiveau)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_NIVEAU);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forNiveau));
        }

        if (!JadeStringUtil.isEmpty(forPosition)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_POSITION);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forPosition));
        }

        if (forSelectable.booleanValue()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_IS_SELECTABLE);
            whereClause.append("=");
            whereClause.append(_dbWriteBoolean(statement.getTransaction(), forSelectable,
                    BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        if (forEditable.booleanValue()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTElement.FIELDNAME_IS_EDITABLE);
            whereClause.append("=");
            whereClause
                    .append(_dbWriteBoolean(statement.getTransaction(), forEditable, BConstants.DB_TYPE_BOOLEAN_CHAR));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTElement();
    }

    /**
     * @return
     */
    public Boolean getForEditable() {
        return forEditable;
    }

    /**
     * getter pour l'attribut for id document
     * 
     * @return la valeur courante de l'attribut for id document
     */
    public String getForIdDocument() {
        return forIdDocument;
    }

    /**
     * getter pour l'attribut for id element
     * 
     * @return la valeur courante de l'attribut for id element
     */
    public String getForIdElement() {
        return forIdElement;
    }

    /**
     * getter pour l'attribut for niveau
     * 
     * @return la valeur courante de l'attribut for niveau
     */
    public String getForNiveau() {
        return forNiveau;
    }

    /**
     * getter pour l'attribut for position
     * 
     * @return la valeur courante de l'attribut for position
     */
    public String getForPosition() {
        return forPosition;
    }

    /**
     * @return
     */
    public Boolean getForSelectable() {
        return forSelectable;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.babel.db.CTAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return CTElement.FIELDNAME_ID_ELEMENT;
    }

    /**
     * setter pour l'attribut for id document
     * 
     * @param forIdDocument
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDocument(String forIdDocument) {
        this.forIdDocument = forIdDocument;
    }

    /**
     * setter pour l'attribut for id element
     * 
     * @param forIdElement
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdElement(String forIdElement) {
        this.forIdElement = forIdElement;
    }

    /**
     * setter pour l'attribut for niveau
     * 
     * @param forNiveau
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNiveau(String forNiveau) {
        this.forNiveau = forNiveau;
    }

    /**
     * setter pour l'attribut for position
     * 
     * @param forPosition
     *            une nouvelle valeur pour cet attribut
     */
    public void setForPosition(String forPosition) {
        this.forPosition = forPosition;
    }

    /**
     * @param boolean1
     */
    public void setForSelectable(Boolean boolean1) {
        forSelectable = boolean1;
    }

}
