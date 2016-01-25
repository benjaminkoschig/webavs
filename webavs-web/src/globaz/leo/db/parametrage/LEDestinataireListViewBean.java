/*
 * Créé le 6 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.parametrage;

import globaz.envoi.db.parametreEnvoi.access.ENDestinataireDocumentManager;
import globaz.envoi.db.parametreEnvoi.access.IENDefinitionFormuleDefTable;
import globaz.envoi.db.parametreEnvoi.access.IENDestinataireDocumentDefTable;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEDestinataireListViewBean extends ENDestinataireDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String forCsDocument;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String from = super._getFrom(statement);
        from += (" INNER JOIN " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME);
        from += (" ON " + _getCollection() + IENDestinataireDocumentDefTable.TABLE_NAME + "." + IENDestinataireDocumentDefTable.ID_PROVENANCE);
        from += (" = " + _getCollection() + IENDefinitionFormuleDefTable.TABLE_NAME + "." + IENDefinitionFormuleDefTable.ID_DEFINITION_FORMULE);
        return from;
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sql = new StringBuffer(super._getWhere(statement));
        if (getForCsDocument().trim().length() > 0) {
            if (sql.length() > 0) {
                sql.append(" AND ");
            }

            sql.append(IENDefinitionFormuleDefTable.CS_DOCUMENT).append("=")
                    .append(_dbWriteNumeric(statement.getTransaction(), getForCsDocument()));
        }
        return sql.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new LEDestinataireViewBean();
    }

    /**
     * @return
     */
    public String getForCsDocument() {
        return forCsDocument;
    }

    /**
     * @param string
     */
    public void setForCsDocument(String string) {
        forCsDocument = string;
    }

}
