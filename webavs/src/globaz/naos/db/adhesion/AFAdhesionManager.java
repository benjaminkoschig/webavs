/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.adhesion;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité Adhésion.
 * 
 * @author sau
 */
public class AFAdhesionManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAdhesionId;
    private java.lang.String forAffiliationId;
    private java.lang.String forDateDebutLower;
    private java.lang.String forDateValeur;
    private java.lang.String forIdTiers;
    private java.lang.String forPlanCaisseId;
    private java.lang.String forTypeAdhesion;

    private String[] forTypeAdhesionList;

    private java.lang.String order = new String();

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFADHEP";
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

        if (!JadeStringUtil.isEmpty(getForAdhesionId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MRIADH=" + this._dbWriteNumeric(statement.getTransaction(), getForAdhesionId());
        }

        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }

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

            sqlWhere += "HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        if (!JadeStringUtil.isEmpty(getForTypeAdhesion())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MRTADH=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeAdhesion());
        }

        if ((forTypeAdhesionList != null) && (forTypeAdhesionList.length > 0)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND (";
            }
            for (int idType = 0; idType < forTypeAdhesionList.length; ++idType) {
                if (idType > 0) {
                    sqlWhere += " OR ";
                }

                sqlWhere += "MRTADH=";
                sqlWhere += this._dbWriteNumeric(statement.getTransaction(), forTypeAdhesionList[idType]);
            }
            sqlWhere += ")";
        }
        if (!JadeStringUtil.isBlankOrZero(forDateValeur)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String date = this._dbWriteDateAMJ(statement.getTransaction(), forDateValeur);
            sqlWhere += " ((" + date + " between MRDDEB and MRDFIN) or (MRDFIN=0 and MRDDEB<= " + date + " ))";
        }
        if (!JadeStringUtil.isBlankOrZero(forDateDebutLower)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String dateDebut = this._dbWriteDateAMJ(statement.getTransaction(), forDateDebutLower);
            sqlWhere += " (MRDDEB<= " + dateDebut + ")";
        }
        if (JadeStringUtil.isEmpty(order)) {
            order = "MRTADH DESC, MRDDEB";
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
        return new AFAdhesion();
    }

    public java.lang.String getForAdhesionId() {
        return forAdhesionId;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForDateDebutLower() {
        return forDateDebutLower;
    }

    public java.lang.String getForDateValeur() {
        return forDateValeur;
    }

    public java.lang.String getForIdTiers() {
        return forIdTiers;
    }

    public java.lang.String getForPlanCaisseId() {
        return forPlanCaisseId;
    }

    public java.lang.String getForTypeAdhesion() {
        return forTypeAdhesion;
    }

    public String[] getForTypeAdhesionList() {
        return forTypeAdhesionList;
    }

    public java.lang.String getOrder() {
        return order;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setForAdhesionId(java.lang.String string) {
        forAdhesionId = string;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    public void setForDateDebutLower(java.lang.String string) {
        forDateDebutLower = string;
    }

    public void setForDateValeur(java.lang.String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    public void setForIdTiers(java.lang.String string) {
        forIdTiers = string;
    }

    public void setForPlanCaisseId(java.lang.String string) {
        forPlanCaisseId = string;
    }

    public void setForTypeAdhesion(java.lang.String string) {
        forTypeAdhesion = string;
    }

    public void setForTypeAdhesionList(String[] strings) {
        forTypeAdhesionList = strings;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }
}
