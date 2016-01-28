package globaz.naos.db.annonceAffilie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFAnnonceAffilieManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffiliationId;
    private String forDateAnnonce;
    private java.lang.String forTraitement;

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFAPREP";
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

        if (!JadeStringUtil.isEmpty(getForTraitement())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPBTRA=" + this._dbWriteNumeric(statement.getTransaction(), getForTraitement());
        }
        if (!JadeStringUtil.isEmpty(getForDateAnnonce())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MPDANN=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateAnnonce());
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
        return new AFAnnonceAffilie();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public void forTraitement(java.lang.String newForTraitement) {
        forTraitement = newForTraitement;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    /**
     * @return
     */
    public String getForDateAnnonce() {
        return forDateAnnonce;
    }

    public java.lang.String getForTraitement() {
        return forTraitement;
    }

    public void setForAffiliationId(java.lang.String newForAffiliationId) {
        forAffiliationId = newForAffiliationId;
    }

    /**
     * @param string
     */
    public void setForDateAnnonce(String string) {
        forDateAnnonce = string;
    }

}
