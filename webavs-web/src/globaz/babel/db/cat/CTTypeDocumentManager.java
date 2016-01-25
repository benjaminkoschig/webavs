/*
 * Créé le 13 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.cat;

import globaz.babel.db.CTAbstractManager;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 * @see globaz.prestation.db.cattxt.CTTypeDocument
 */
public class CTTypeDocumentManager extends CTAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDomaine = "";
    private String forCsTypeDocument = "";
    private String forIdTypeDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer whereClause = new StringBuffer();

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

        if (!JadeStringUtil.isEmpty(forIdTypeDocument)) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append(CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT);
            whereClause.append("=");
            whereClause.append(_dbWriteNumeric(statement.getTransaction(), forIdTypeDocument));
        }

        return whereClause.toString();
    }

    /**
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTTypeDocument();
    }

    /**
     * getter pour l'attribut for cs domaine
     * 
     * @return la valeur courante de l'attribut for cs domaine
     */
    public String getForCsDomaine() {
        return forCsDomaine;
    }

    /**
     * getter pour l'attribut for cs type document
     * 
     * @return la valeur courante de l'attribut for cs type document
     */
    public String getForCsTypeDocument() {
        return forCsTypeDocument;
    }

    /**
     * getter pour l'attribut for id type document
     * 
     * @return la valeur courante de l'attribut for id type document
     */
    public String getForIdTypeDocument() {
        return forIdTypeDocument;
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
        return CTTypeDocument.FIELDNAME_ID_TYPE_DOCUMENT;
    }

    /**
     * setter pour l'attribut for cs domaine
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsDomaine(String string) {
        forCsDomaine = string;
    }

    /**
     * setter pour l'attribut for cs type document
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForCsTypeDocument(String string) {
        forCsTypeDocument = string;
    }

    /**
     * setter pour l'attribut for id type document
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdTypeDocument(String string) {
        forIdTypeDocument = string;
    }
}
