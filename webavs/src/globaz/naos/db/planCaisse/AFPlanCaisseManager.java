/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.planCaisse;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

/**
 * Le Manager pour l'entité PlanCaisse.
 * 
 * @author sau
 */
public class AFPlanCaisseManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forIdTiers;
    private java.lang.String forNumeroCaisse;
    private java.lang.String forPlanCaisseId;
    private java.lang.String forTypeAffiliation;
    private java.lang.String fromLibelle;
    private java.lang.String fromNumeroCaisse;

    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        String sqlFrom = null;

        // if (! JadeStringUtil.isEmpty(getForNumeroCaisse())) {
        sqlFrom = _getCollection() + "AFPLCAP INNER JOIN " + _getCollection() + "TIADMIP ON " + _getCollection()
                + "TIADMIP.HTITIE=" + _getCollection() + "AFPLCAP.HTITIE";
        // } else {
        // sqlFrom = _getCollection() + "AFPLCAP";
        // }
        return sqlFrom;
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

        if (!JadeStringUtil.isEmpty(getForPlanCaisseId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MSIPLC=" + this._dbWriteNumeric(statement.getTransaction(), getForPlanCaisseId());
        }

        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += _getCollection() + "AFPLCAP.HTITIE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (!JadeStringUtil.isEmpty(getFromLibelle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            String langue = getSession().getIdLangueISO();

            if (JACalendar.LANGUAGE_DE.equals(langue)) {
                sqlWhere += "MSLLIA LIKE " + this._dbWriteString(statement.getTransaction(), getFromLibelle() + "%");

            } else if (JACalendar.LANGUAGE_IT.equals(langue)) {
                sqlWhere += "MSLLII LIKE " + this._dbWriteString(statement.getTransaction(), getFromLibelle() + "%");
            } else {
                sqlWhere += "MSLLIB LIKE " + this._dbWriteString(statement.getTransaction(), getFromLibelle() + "%");
            }

        }

        if (!JadeStringUtil.isEmpty(getForNumeroCaisse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TIADMIP.HBCADM ="
                    + this._dbWriteString(statement.getTransaction(), getForNumeroCaisse());
        }

        if (!JadeStringUtil.isEmpty(getFromNumeroCaisse())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "TIADMIP.HBCADM <> '' and CAST(";
            sqlWhere += _getCollection() + "TIADMIP.HBCADM AS INTEGER) >="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromNumeroCaisse());
        }
        if (!JadeStringUtil.isEmpty(getForTypeAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String types = "0,";
            if ((CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equals(getForTypeAffiliation()))
                    || (CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(getForTypeAffiliation()))) {
                // on recherche les plans employeur ET indépendants
                types += CodeSystem.TYPE_AFFILI_EMPLOY + "," + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ","
                        + CodeSystem.TYPE_AFFILI_INDEP;
            } else {
                types += getForTypeAffiliation();
            }
            sqlWhere += "MSTTAF in (" + types + ")";
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
        return new AFPlanCaisse();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForNumeroCaisse() {
        return forNumeroCaisse;
    }

    public java.lang.String getForPlanCaisseId() {
        return forPlanCaisseId;
    }

    public java.lang.String getForTypeAffiliation() {
        return forTypeAffiliation;
    }

    public java.lang.String getFromLibelle() {
        return fromLibelle;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public java.lang.String getFromNumeroCaisse() {
        return fromNumeroCaisse;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public void setForIdTiers(java.lang.String string) {
        forIdTiers = string;
    }

    public void setForNumeroCaisse(java.lang.String string) {
        forNumeroCaisse = string;
    }

    public void setForPlanCaisseId(java.lang.String string) {
        forPlanCaisseId = string;
    }

    public void setForTypeAffiliation(java.lang.String forTypeAffiliation) {
        this.forTypeAffiliation = forTypeAffiliation;
    }

    public void setFromLibelle(java.lang.String string) {
        fromLibelle = string;
    }

    public void setFromNumeroCaisse(java.lang.String fromNumeroCaisse) {
        this.fromNumeroCaisse = fromNumeroCaisse;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }
}
