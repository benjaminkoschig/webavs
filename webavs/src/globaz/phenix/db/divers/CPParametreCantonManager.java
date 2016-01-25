package globaz.phenix.db.divers;

import globaz.jade.client.util.JadeStringUtil;

public class CPParametreCantonManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCanton = "";
    private java.lang.String forCodeParametre = "";
    private java.lang.String forDateDebut = "";
    private java.lang.String forExceptIdParamatre = "";
    private java.lang.String forGenreAffilie = "";
    private java.lang.String forTypeParametre = "";
    private java.lang.String fromDateDebut = "";

    private String order = "";

    private Boolean wantControleDateZero = Boolean.FALSE;

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "CPCANTOP";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (!JadeStringUtil.isBlankOrZero(getOrder())) {
            return getOrder();
        }
        return "SPCCAN ASC";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForCanton().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPCCAN=" + this._dbWriteNumeric(statement.getTransaction(), getForCanton());
        }

        // traitement du positionnement
        if (getForCodeParametre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPCPAR=" + this._dbWriteNumeric(statement.getTransaction(), getForCodeParametre());
        }

        // traitement du positionnement
        if (getForTypeParametre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPTPAR=" + this._dbWriteNumeric(statement.getTransaction(), getForTypeParametre());
        }

        // traitement du positionnement
        if (getForGenreAffilie().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPTGAF=" + this._dbWriteNumeric(statement.getTransaction(), getForGenreAffilie());
        }

        // traitement du positionnement pour un type de parametre (Ex: mode envoi sedex, calcul AF)
        if (getForExceptIdParamatre().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPINDE <>" + this._dbWriteNumeric(statement.getTransaction(), getForExceptIdParamatre());
        }

        // traitement du positionnement pour un type de parametre (Ex: mode envoi sedex, calcul AF)
        if ((getForDateDebut().length() != 0) || getWantControleDateZero()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPDDEB=" + this._dbWriteDateAMJ(statement.getTransaction(), getForDateDebut());
        }

        // traitement du positionnement pour un type de parametre (Ex: mode envoi sedex, calcul AF)
        if (getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "SPDDEB <=" + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CPParametreCanton();
    }

    public String getForCanton() {
        return forCanton;
    }

    public java.lang.String getForCodeParametre() {
        return forCodeParametre;
    }

    public java.lang.String getForDateDebut() {
        return forDateDebut;
    }

    public java.lang.String getForExceptIdParamatre() {
        return forExceptIdParamatre;
    }

    public java.lang.String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public java.lang.String getForTypeParametre() {
        return forTypeParametre;
    }

    public java.lang.String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getOrder() {
        return order;
    }

    public Boolean getWantControleDateZero() {
        return wantControleDateZero;
    }

    public void setForCanton(String forCodeCanton) {
        forCanton = forCodeCanton;
    }

    public void setForCodeParametre(java.lang.String newFromAnneeRentier) {
        forCodeParametre = newFromAnneeRentier;
    }

    public void setForDateDebut(java.lang.String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForExceptIdParamatre(java.lang.String forExceptIdParamatre) {
        this.forExceptIdParamatre = forExceptIdParamatre;
    }

    public void setForGenreAffilie(java.lang.String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForTypeParametre(java.lang.String forTypeParametre) {
        this.forTypeParametre = forTypeParametre;
    }

    public void setFromDateDebut(java.lang.String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setOrderByDateDesc() {
        setOrder("SPDDEB DESC");
    }

    public void setWantControleDateZero(Boolean wantControleDateZero) {
        this.wantControleDateZero = wantControleDateZero;
    }
}
