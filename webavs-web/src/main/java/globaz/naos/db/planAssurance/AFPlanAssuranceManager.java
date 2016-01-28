package globaz.naos.db.planAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité PlanAssurance.
 * 
 * @author sau
 */
public class AFPlanAssuranceManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forPlanId;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFPLASP";
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

        // traitement du positionnement
        if (!JadeStringUtil.isEmpty(getForPlanId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MRIPLA=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanId());
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
        return new AFPlanAssurance();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForPlanId() {
        return forPlanId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForPlanId(java.lang.String newForPlanId) {
        forPlanId = newForPlanId;
    }
}
