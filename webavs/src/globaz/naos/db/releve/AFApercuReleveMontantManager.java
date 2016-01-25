/*
 * Créé le 29 jui. 05
 */
package globaz.naos.db.releve;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Le Manager pour l'entité Relevé Montant.
 * 
 * @author sau 29 jun. 05 13:43:01
 */
public class AFApercuReleveMontantManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdReleve;

    public AFApercuReleveMontantManager() {
        super();
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFREVMP";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // Releve Id
        if (!JadeStringUtil.isEmpty(getForIdReleve())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MMIREL = " + this._dbWriteNumeric(statement.getTransaction(), getForIdReleve());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFApercuReleveMontant();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForIdReleve() {
        return forIdReleve;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForIdReleve(String string) {
        forIdReleve = string;
    }
}
