package globaz.naos.db.suiviAssurance;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité SuiviAssurance.
 * 
 * @author administrator
 */
public class AFSuiviAssuranceManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAssuranceId;
    private java.lang.String forIdTiers;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFSUIVP";
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

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (!JadeStringUtil.isEmpty(getForAssuranceId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());
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
        return new AFSuiviAssurance();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAssuranceId() {
        return forAssuranceId;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAssuranceId(java.lang.String newForAssuranceId) {
        forAssuranceId = newForAssuranceId;
    }

    public void setForIdTiers(java.lang.String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }
}
