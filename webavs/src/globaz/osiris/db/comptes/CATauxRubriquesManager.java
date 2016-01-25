package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 10:58:38)
 * 
 * @author: Administrator
 */
public class CATauxRubriquesManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_BY_CAISSE_PROF_ASC_DATE_DESC = "3";
    public static final String ORDER_BY_DATE_ASC = "2";
    public static final String ORDER_BY_DATE_DESC = "1";
    public static final String ORDER_BY_IDEXTERNE_ASC_DATE_ASC = "4";
    private String forDate = "";
    private String forIdCaisseProf = "";
    private String forIdExterne = "";
    private String forIdRubrique = "";
    private String fromDate = "";

    private String fromIdExterne = "";
    private String likeIdExterne = "";
    private String orderBy = "";
    private String untilDate = "";

    /*
     * Renvoie la clause FROM @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        // Récupération du CI, du tiers/partenaire
        // Obligation de renommée la table CIINDIP pour requête sur le
        // partenaire
        String joinStr = " inner join " + _getCollection() + "CARUBRP on " + _getCollection() + "CARUBRP.IDRUBRIQUE="
                + _getCollection() + "CATAUXP.AGID";

        return _getCollection() + "CATAUXP" + joinStr;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().equals(CATauxRubriquesManager.ORDER_BY_DATE_DESC)) {
            return _getCollection() + "CATAUXP.ANDATE DESC";
        } else if (getOrderBy().equals(CATauxRubriquesManager.ORDER_BY_DATE_ASC)) {
            return _getCollection() + "CATAUXP.ANDATE ASC";
        } else if (getOrderBy().equals(CATauxRubriquesManager.ORDER_BY_CAISSE_PROF_ASC_DATE_DESC)) {
            return _getCollection() + "CATAUXP.IDCAIPRO ASC, " + _getCollection() + "CATAUXP.ANDATE DESC";
        } else if (getOrderBy().equals(CATauxRubriquesManager.ORDER_BY_IDEXTERNE_ASC_DATE_ASC)) {
            return _getCollection() + "CARUBRP.IDEXTERNE ASC, " + _getCollection() + "CATAUXP.ANDATE ASC";
        } else {
            return "";
        }
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CATAUXP.AGID="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }

        // traitement du positionnement
        if (getUntilDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ANDATE<=" + this._dbWriteDateAMJ(statement.getTransaction(), getUntilDate());
        }
        if (getForIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE=" + this._dbWriteString(statement.getTransaction(), getForIdExterne());
        }
        if (getFromIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE>=" + this._dbWriteString(statement.getTransaction(), getFromIdExterne());
        }
        if (getLikeIdExterne().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDEXTERNE LIKE " + this._dbWriteString(statement.getTransaction(), getForIdExterne() + "%");
        }
        if (getFromDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ANDATE>=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDate());
        }
        if (getForDate().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ANDATE=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDate());
        }
        if (getForIdCaisseProf().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "IDCAIPRO=" + this._dbWriteNumeric(statement.getTransaction(), getForIdCaisseProf());
        }
        if (getOrderBy().length() != 0) {
            _getOrder(statement);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CATauxRubriques();
    }

    /**
     * @return the fromDate
     */
    public String getForDate() {
        return forDate;
    }

    public String getForIdCaisseProf() {
        return forIdCaisseProf;
    }

    /**
     * @return
     */
    public String getForIdExterne() {
        return forIdExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.02.2002 12:05:45)
     * 
     * @return java.lang.String
     */
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @return the fromIdExterne
     */
    public String getFromIdExterne() {
        return fromIdExterne;
    }

    public String getLikeIdExterne() {
        return likeIdExterne;
    }

    public String getOrderBy() {
        return orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2002 09:00:04)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDate() {
        return untilDate;
    }

    /**
     * @param fromDate
     *            the fromDate to set
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForIdCaisseProf(String forIdCaisseProf) {
        this.forIdCaisseProf = forIdCaisseProf;
    }

    /**
     * @param string
     */
    public void setForIdExterne(String string) {
        forIdExterne = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.02.2002 12:05:45)
     * 
     * @param newForIdRubrique
     *            java.lang.String
     */
    public void setForIdRubrique(java.lang.String newForIdRubrique) {
        forIdRubrique = newForIdRubrique;
    }

    /**
     * @param fromDate
     *            the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @param fromIdExterne
     *            the fromIdExterne to set
     */
    public void setFromIdExterne(String fromIdExterne) {
        this.fromIdExterne = fromIdExterne;
    }

    public void setLikeIdExterne(String likeIdExterne) {
        this.likeIdExterne = likeIdExterne;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2002 09:00:04)
     * 
     * @param newUntilDate
     *            java.lang.String
     */
    public void setUntilDate(java.lang.String newUntilDate) {
        untilDate = newUntilDate;
    }

}
