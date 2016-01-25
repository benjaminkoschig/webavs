/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.annexes;

import globaz.babel.db.cat.CTDocument;
import globaz.babel.db.cat.CTTypeDocument;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CTDocumentJointDefaultAnnexesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsDestinataireDocument = "";
    private String forCsDomaineDocument = "";
    private String forCsTypeDocument = "";
    private String forIdDocument = "";
    private String forNomDocument = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();
        String schema = _getCollection();

        if (!JadeStringUtil.isBlank(forIdDocument)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTDocument.TABLE_NAME_DOCUMENT + "." + CTDocument.FIELDNAME_ID_DOCUMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdDocument));
        }

        if (!JadeStringUtil.isBlank(forCsTypeDocument)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT + "."
                    + CTTypeDocument.FIELDNAME_CS_TYPE_DOCUMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypeDocument));
        }

        if (!JadeStringUtil.isBlank(forCsDomaineDocument)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTTypeDocument.TABLE_NAME_TYPE_DOCUMENT + "."
                    + CTTypeDocument.FIELDNAME_CS_DOMAINE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsDomaineDocument));
        }

        if (!JadeStringUtil.isBlank(forNomDocument)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTDocument.TABLE_NAME_DOCUMENT + "." + CTDocument.FIELDNAME_NOM + "="
                    + _dbWriteString(statement.getTransaction(), forNomDocument));
        }

        if (!JadeStringUtil.isBlank(forCsDestinataireDocument)) {
            if (sqlWhere.length() != 0) {
                sqlWhere.append(" AND ");
            }

            sqlWhere.append(schema + CTDocument.TABLE_NAME_DOCUMENT + "." + CTDocument.FIELDNAME_CS_DESTINATAIRE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsDestinataireDocument));
        }

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocumentJointDefaultAnnexes();
    }

    /**
     * @return
     */
    public String getForCsDestinataireDocument() {
        return forCsDestinataireDocument;
    }

    /**
     * @return
     */
    public String getForCsDomaineDocument() {
        return forCsDomaineDocument;
    }

    /**
     * @return
     */
    public String getForCsTypeDocument() {
        return forCsTypeDocument;
    }

    /**
     * @return
     */
    public String getForIdDocument() {
        return forIdDocument;
    }

    /**
     * @return
     */
    public String getForNomDocument() {
        return forNomDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    public String getOrderByDefaut() {
        return CTDefaultAnnexes.FIELDNAME_DEFAULT_ANNEXES_ID;
    }

    /**
     * @param string
     */
    public void setForCsDestinataireDocument(String string) {
        forCsDestinataireDocument = string;
    }

    /**
     * @param string
     */
    public void setForCsDomaineDocument(String string) {
        forCsDomaineDocument = string;
    }

    /**
     * @param string
     */
    public void setForCsTypeDocument(String string) {
        forCsTypeDocument = string;
    }

    /**
     * @param string
     */
    public void setForIdDocument(String string) {
        forIdDocument = string;
    }

    /**
     * @param string
     */
    public void setForNomDocument(String string) {
        forNomDocument = string;
    }

}