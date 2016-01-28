/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.nombreAssures;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité NombreAssures.
 * 
 * @author sau
 */
public class AFNombreAssuresManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAffiliationId;
    private String forAnnee;
    private java.lang.String forAssuranceId;
    private java.lang.String forNbrAssuresId;
    private java.lang.String fromAnnee;

    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFNASSP";
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

        if (!JadeStringUtil.isEmpty(getForNbrAssuresId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MVINAS=" + this._dbWriteNumeric(statement.getTransaction(), getForNbrAssuresId());
        }

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getForAssuranceId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MBIASS=" + this._dbWriteNumeric(statement.getTransaction(), getForAssuranceId());
        }

        if (!JadeStringUtil.isEmpty(getFromAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MVNANN>=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnnee());
        }

        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MVNANN=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee());
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
        return new AFNombreAssures();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public java.lang.String getForAssuranceId() {
        return forAssuranceId;
    }

    public java.lang.String getForNbrAssuresId() {
        return forNbrAssuresId;
    }

    public java.lang.String getFromAnnee() {
        return fromAnnee;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getOrder() {
        return order;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    public void setForAnnee(String string) {
        forAnnee = string;
    }

    public void setForAssuranceId(java.lang.String string) {
        forAssuranceId = string;
    }

    public void setForNbrAssuresId(java.lang.String string) {
        forNbrAssuresId = string;
    }

    public void setFromAnnee(java.lang.String string) {
        fromAnnee = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

}
