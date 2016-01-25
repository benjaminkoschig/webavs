package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPRevenuCotisationCantonManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forAnneeDebut = "";
    private java.lang.String forCanton = "";
    private String forCodeCanton = "";
    private java.lang.String fromAnneeDebutDesc = "";
    private java.lang.String fromDateDebutActiviteDesc = "";

    private String order = "";

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPRECAP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (!JadeStringUtil.isBlankOrZero(getOrder())) {
            return getOrder();
        }
        return "RCTCAN ASC, RCNAND DESC, RCDACT DESC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCTCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCanton());
        }

        if (getForCodeCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCCCAN=" + this._dbWriteString(statement.getTransaction(), getForCodeCanton());
        }

        // traitement du positionnement
        if (getForAnneeDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCNAND=" + this._dbWriteNumeric(statement.getTransaction(), getForAnneeDebut());
        }

        // traitement du positionnement
        if (getFromAnneeDebutDesc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCNAND<=" + this._dbWriteNumeric(statement.getTransaction(), getFromAnneeDebutDesc());
        }

        // traitement du positionnement
        if (getFromDateDebutActiviteDesc().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "RCDACT<=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebutActiviteDesc());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPRevenuCotisationCanton();
    }

    public java.lang.String getForAnneeDebut() {
        return forAnneeDebut;
    }

    /**
     * Getter
     */
    public java.lang.String getForCanton() {
        return forCanton;
    }

    public String getForCodeCanton() {
        return forCodeCanton;
    }

    public java.lang.String getFromAnneeDebutDesc() {
        return fromAnneeDebutDesc;
    }

    public java.lang.String getFromDateDebutActiviteDesc() {
        return fromDateDebutActiviteDesc;
    }

    public String getOrder() {
        return order;
    }

    public void setForAnneeDebut(java.lang.String newForAnneeRentier) {
        forAnneeDebut = newForAnneeRentier;
    }

    /**
     * Setter
     */
    public void setForCanton(java.lang.String newForIdTableRentier) {
        forCanton = newForIdTableRentier;
    }

    public void setForCodeCanton(String forCodeCanton) {
        this.forCodeCanton = forCodeCanton;
    }

    public void setFromAnneeDebutDesc(java.lang.String newFromAnneeRentier) {
        fromAnneeDebutDesc = newFromAnneeRentier;
    }

    public void setFromDateDebutActiviteDesc(java.lang.String fromDateDebutActiviteDesc) {
        this.fromDateDebutActiviteDesc = fromDateDebutActiviteDesc;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
