/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.couverture;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Couverture.
 * 
 * @author sau
 */
public class AFCouvertureManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAssuranceId;
    private java.lang.String forCouvertureId;
    private java.lang.String forDate;
    private java.lang.String forDateFinPlusGransQue;
    private java.lang.String forPlanCaisseId;

    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFCOUVP";
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

        if (!JadeStringUtil.isEmpty(getForCouvertureId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MTICOU=" + this._dbWriteNumeric(statement.getTransaction(), getForCouvertureId());
        }

        if (!JadeStringUtil.isEmpty(getForPlanCaisseId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MSIPLC=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanCaisseId());
        }

        if (!JadeStringUtil.isEmpty(getForAssuranceId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());
        }

        if (!JadeStringUtil.isEmpty(getForDateFinPlusGransQue())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "( MTDFIN > " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFinPlusGransQue());
            sqlWhere += " OR MTDFIN = 0 )";
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
        return new AFCouverture();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAssuranceId() {
        return forAssuranceId;
    }

    public java.lang.String getForCouvertureId() {
        return forCouvertureId;
    }

    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForDateFinPlusGransQue() {
        return forDateFinPlusGransQue;
    }

    public java.lang.String getForPlanCaisseId() {
        return forPlanCaisseId;
    }

    public java.lang.String getOrder() {
        return order;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAssuranceId(java.lang.String string) {
        forAssuranceId = string;
    }

    public void setForCouvertureId(java.lang.String string) {
        forCouvertureId = string;
    }

    public void setForDate(java.lang.String string) {
        forDate = string;
    }

    public void setForDateFinPlusGransQue(java.lang.String forDateFinPlusGransQue) {
        this.forDateFinPlusGransQue = forDateFinPlusGransQue;
    }

    public void setForPlanCaisseId(java.lang.String string) {
        forPlanCaisseId = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }
}
