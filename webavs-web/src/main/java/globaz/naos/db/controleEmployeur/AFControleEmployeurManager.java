package globaz.naos.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

public class AFControleEmployeurManager extends BManager implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String field = "";
    private java.lang.String forAffiliationId;
    private java.lang.String forAnnee;
    private java.lang.String forControleEmployeurId;
    private boolean forLastControlEffectif;
    private java.lang.String forNotId;
    private java.lang.String forNumAffilie;
    private boolean forNumRapportNonVide;
    private java.lang.String likeNouveauNumRapport;
    private String forDateDebutControle = "";
    private String forDateFinControle = "";

    private java.lang.String orderBy = "";

    @Override
    protected String _getFields(BStatement statement) {
        if (getField().length() != 0) {
            return getField();
        }
        return "*";
    }

    /**
     * Renvoie la clause FROM.
     * 
     * @see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "AFCONTP";
    }

    /**
     * Renvoie la composante de tri de la requête SQL.
     * 
     * @see globaz.globall.db.BManager#_getOrder(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().length() != 0) {
            return getOrderBy();
        } else {
            return "";
        }
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
        if (!JadeStringUtil.isEmpty(getForNumAffilie())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MALNAF=" + this._dbWriteString(statement.getTransaction(), getForNumAffilie());
        }
        if (!JadeStringUtil.isEmpty(getForAnnee())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDDPRE>=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "0101");
            sqlWhere += " AND MDDPRE <=" + this._dbWriteNumeric(statement.getTransaction(), getForAnnee() + "1231");
        }
        if (!JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MAIAFF=" + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId());
        }
        if (!JadeStringUtil.isEmpty(getForDateDebutControle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDDCDE = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebutControle());
        }
        if (!JadeStringUtil.isEmpty(getForDateFinControle())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDDCFI = " + this._dbWriteDateAMJ(statement.getTransaction(), getForDateFinControle());
        }
        if (!JadeStringUtil.isEmpty(getForControleEmployeurId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDICON=" + this._dbWriteNumeric(statement.getTransaction(), getForControleEmployeurId());
        }
        if (getForLastControlEffectif() && !JadeStringUtil.isEmpty(getForAffiliationId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            // (select max(mddeff) from CCJUWEB.AFCONTP where
            // maiaff=affiliationId)
            sqlWhere += "MDDEFF=" + "(SELECT MAX(MDDEFF) FROM " + _getCollection() + "AFCONTP" + " WHERE MAIAFF="
                    + this._dbWriteNumeric(statement.getTransaction(), getForAffiliationId()) + ")";
        }
        if (!JadeStringUtil.isEmpty(getLikeNouveauNumRapport())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDLNRA LIKE '" + getLikeNouveauNumRapport() + "%'";
        }
        if (!JadeStringUtil.isEmpty(getForNotId())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MDICON <> " + this._dbWriteNumeric(statement.getTransaction(), getForNotId());
        }
        if (getForNumRapportNonVide()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(MDLNRA IS NULL OR MDLNRA='0')";
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
        return new AFControleEmployeur();
    }

    /**
     * @return
     */
    public java.lang.String getField() {
        return field;
    }

    /**
     * @return
     */
    public java.lang.String getForAffiliationId() {
        return forAffiliationId;
    }

    /**
     * @return
     */
    public java.lang.String getForAnnee() {
        return forAnnee;
    }

    /**
     * @return
     */
    public String getForDateDebutControle() {
        return forDateDebutControle;
    }

    /**
     * @return
     */
    public String getForDateFinControle() {
        return forDateFinControle;
    }

    /**
     * @return
     */
    public java.lang.String getForControleEmployeurId() {
        return forControleEmployeurId;
    }

    public boolean getForLastControlEffectif() {
        return forLastControlEffectif;
    }

    public java.lang.String getForNotId() {
        return forNotId;
    }

    /**
     * @return
     */
    public java.lang.String getForNumAffilie() {
        return forNumAffilie;
    }

    public boolean getForNumRapportNonVide() {
        return forNumRapportNonVide;
    }

    public java.lang.String getLikeNouveauNumRapport() {
        return likeNouveauNumRapport;
    }

    /**
	 * 
	 */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * @param string
     */
    public void setField(java.lang.String string) {
        field = string;
    }

    public void setForDateDebutControle(String forDateDebutControle) {
        this.forDateDebutControle = forDateDebutControle;
    }

    public void setForDateFinControle(String forDateFinControle) {
        this.forDateFinControle = forDateFinControle;
    }

    /**
     * @param string
     */
    public void setForAffiliationId(java.lang.String string) {
        forAffiliationId = string;
    }

    /**
     * @param string
     */
    public void setForAnnee(java.lang.String string) {
        forAnnee = string;
    }

    /**
     * @param string
     */
    public void setForControleEmployeurId(java.lang.String string) {
        forControleEmployeurId = string;
    }

    public void setForLastControlEffectif(boolean forLastControlEffectif) {
        this.forLastControlEffectif = forLastControlEffectif;
    }

    public void setForNotId(java.lang.String forNotId) {
        this.forNotId = forNotId;
    }

    /**
     * @param string
     */
    public void setForNumAffilie(java.lang.String string) {
        forNumAffilie = string;
    }

    public void setForNumRapportNonVide(boolean forNumRapportNonVide) {
        this.forNumRapportNonVide = forNumRapportNonVide;
    }

    public void setLikeNouveauNumRapport(java.lang.String likeNouveauNumRapport) {
        this.likeNouveauNumRapport = likeNouveauNumRapport;
    }

    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

}
