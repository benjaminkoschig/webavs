package globaz.naos.db.fact;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Facturation.
 * 
 * @author administrator
 */
public class AFFactManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffiliationId;
    private java.lang.String forCotisationId;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFFACTP";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return "";
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getForCotisationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MEICOT=" + this._dbWriteNumeric(statement.getTransaction(), getForCotisationId());
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
        return new AFFact();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getForCotisationId() {
        return forCotisationId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    public void setForCotisationId(java.lang.String newForCotisationId) {
        forCotisationId = newForCotisationId;
    }
}
