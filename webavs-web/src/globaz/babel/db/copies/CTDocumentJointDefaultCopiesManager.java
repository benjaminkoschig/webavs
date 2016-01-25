/*
 * Créé le 2 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.db.copies;

import globaz.babel.db.cat.CTDocument;
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
public class CTDocumentJointDefaultCopiesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDocument = "";

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

        return sqlWhere.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new CTDocumentJointDefaultCopies();
    }

    /**
     * @return
     */
    public String getForIdDocument() {
        return forIdDocument;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    public String getOrderByDefaut() {
        return CTDefaultCopies.FIELDNAME_DEFAULT_COPIE_ID;
    }

    /**
     * @param string
     */
    public void setForIdDocument(String string) {
        forIdDocument = string;
    }

}