/*
 * Créé le 6 mars 07
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.inscriptions.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * @author JMC
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CIDeclarationParametrageManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forTypeChamp = "";
    private String forTypeImport = "";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";
        if (getForTypeImport().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KOTDEC=" + _dbWriteNumeric(statement.getTransaction(), getForTypeImport());
        }
        if (getForTypeChamp().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "KOTCHA=" + _dbWriteNumeric(statement.getTransaction(), getForTypeChamp());
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        // TODO Raccord de méthode auto-généré
        return new CIDeclarationParametrage();
    }

    /**
     * @return
     */
    public String getForTypeChamp() {
        return forTypeChamp;
    }

    /**
     * @return
     */
    public String getForTypeImport() {
        return forTypeImport;
    }

    /**
     * @param string
     */
    public void setForTypeChamp(String string) {
        forTypeChamp = string;
    }

    /**
     * @param string
     */
    public void setForTypeImport(String string) {
        forTypeImport = string;
    }

}
