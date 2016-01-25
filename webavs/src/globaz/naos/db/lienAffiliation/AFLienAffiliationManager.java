/*
 * Created on 28-Jan-05
 */
package globaz.naos.db.lienAffiliation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Le Manager pour l'entité LienAffiliation.
 * 
 * @author sau
 */
public class AFLienAffiliationManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAff_AffiliationId;
    private java.lang.String forAffiliationId;
    private java.lang.String forDate;
    private java.lang.String forLienAffiliationId;
    private java.lang.String forTypeLien;
    private java.lang.String order = new String();

    private Boolean wantLienInverse = new Boolean(false);

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFLIENP";
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

        if (!JadeStringUtil.isEmpty(getForLienAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MWILIE=" + this._dbWriteNumeric(statement.getTransaction(), getForLienAffiliationId());
        }

        if (!JadeStringUtil.isEmpty(getForTypeLien())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MWTLIE=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeLien());
        }

        // Cherche parent->enfant et enfant->parent
        // l'id de l'affiliant doit être renseigné
        if (getWantLienInverse().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
        }
        if (getWantLienInverse().booleanValue() && !JadeStringUtil.isEmpty(getForAffiliationId())) {
            sqlWhere += "(MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId())
                    + " OR AFA_MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId()) + ")";

            // l'affiliant est renseigné
            if (!JadeStringUtil.isEmpty(getForAff_AffiliationId())) {
                sqlWhere += "(MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAff_AffiliationId())
                        + " OR AFA_MAIAFF="
                        + this._dbWriteNumeric(statement.getTransaction(), getForAff_AffiliationId()) + ")";
            }

            // Les resultats sont trié pour l'affilié principal
            String preOrder = "MAIAFF ASC";
            // puis par les autres critères de tri s'il sont renseignés
            if (JadeStringUtil.isEmpty(order)) {
                order = preOrder;
            } else {
                order = preOrder + ", " + order;
            }

        } else {
            // Cherche normalle parent->enfant
            if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
            }

            if (!JadeStringUtil.isEmpty(getForAff_AffiliationId())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }

                sqlWhere += "AFA_MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAff_AffiliationId());
            }
        }
        // recherche des liens actifs pour une date donnée
        if (!JadeStringUtil.isEmpty(forDate)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "MWDDEB<=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate)
                    + " AND (MWDFIN=0 OR MWDFIN>=" + this._dbWriteDateAMJ(statement.getTransaction(), forDate) + ")";
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
        return new AFLienAffiliation();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public java.lang.String getForAff_AffiliationId() {
        return forAff_AffiliationId;
    }

    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    /**
     * @return
     */
    public java.lang.String getForDate() {
        return forDate;
    }

    public java.lang.String getForLienAffiliationId() {
        return forLienAffiliationId;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public String getForTypeLien() {
        return forTypeLien;
    }

    public java.lang.String getOrder() {
        return order;
    }

    public Boolean getWantLienInverse() {
        return wantLienInverse;
    }

    public void setForAff_AffiliationId(java.lang.String string) {
        forAff_AffiliationId = string;
    }

    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    /**
     * @param string
     */
    public void setForDate(java.lang.String string) {
        forDate = string;
    }

    public void setForLienAffiliationId(java.lang.String string) {
        forLienAffiliationId = string;
    }

    public void setForTypeLien(String string) {
        forTypeLien = string;
    }

    public void setOrder(java.lang.String string) {
        order = string;
    }

    public void setWantLienInverse(Boolean boolean1) {
        wantLienInverse = boolean1;
    }

}
