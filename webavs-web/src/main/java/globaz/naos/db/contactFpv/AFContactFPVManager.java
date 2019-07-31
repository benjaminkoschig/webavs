/*
 * Créé le 29 jui. 05
 */
package globaz.naos.db.contactFpv;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Le Manager pour l'entité Contact FPV
 */
public class AFContactFPVManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forNumeroAffilier;

    public AFContactFPVManager() {
        super();
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     *
     * @see BManager#_getWhere(BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        // Numero Affilié
        if (!JadeStringUtil.isEmpty(getForNumeroAffilier())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += AFContactFPV.COLUMN_NUMERO_AFF+" = " + this._dbWriteNumeric(statement.getTransaction(), getForNumeroAffilier());
        }
        return sqlWhere;
    }

    /**
     * Crée une nouvelle entité.
     *
     * @see BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFContactFPV();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getForNumeroAffilier() {
        return forNumeroAffilier;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForNumeroAffilier(String string) {
        forNumeroAffilier = string;
    }
}
