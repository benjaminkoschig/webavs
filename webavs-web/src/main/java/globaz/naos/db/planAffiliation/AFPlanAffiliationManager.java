/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité PlanAffiliation.
 * 
 * @author sau
 */
public class AFPlanAffiliationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffiliationId;
    private boolean forPlanActif;
    private java.lang.String forPlanAffiliationId;
    private java.lang.String fromLibelle;

    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFPLAFP";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(BStatement statement) {
        return order;
    }

    /**
     * Renvoie la composante de sélection de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isEmpty(getForPlanAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MUIPLA=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getFromLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MULLIB>=" + this._dbWriteString(statement.getTransaction(), getFromLibelle());
        }
        if (isForPlanActif()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MUBINA='2'";
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
        return new AFPlanAffiliation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public java.lang.String getForPlanAffiliationId() {
        return forPlanAffiliationId;
    }

    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    public java.lang.String getOrder() {
        return order;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public boolean isForPlanActif() {
        return forPlanActif;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    public void setForPlanActif(boolean forPlanActif) {
        this.forPlanActif = forPlanActif;
    }

    public void setForPlanAffiliationId(java.lang.String string) {
        forPlanAffiliationId = string;
    }

    public void setFromLibelle(java.lang.String string) {
        fromLibelle = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }
}
