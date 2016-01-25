package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;

public class FAOrdreAttribuerManager extends globaz.globall.db.BManager implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdOrdreRegroupement = "";
    private java.lang.String forIdRubrique = new String();
    private String forNature = new String();
    private java.lang.String forNumCaisse = new String();
    private java.lang.String forNumOrdreRegroupement = new String();
    private java.lang.String fromIdExterneRubrique = new String();
    protected java.lang.String order = "EHNORD ASC";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAOrdreAttribuer.TABLE_FIELDS
                + ", FAORDIP.EHIDOR, FAORDIP.EHNORD, FAORDIP.EHLLIF, FAORDIP.EHLLID, "
                + "FAORDIP.EHLLII, FAORDIP.EHIDCA, FAORDIP.EHNCAI, FAORDIP.NATURE, CARUBRP.IDRUBRIQUE, CARUBRP.IDEXTERNE, CARUBRP.IDTRADUCTION";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = _getCollection() + "FAORLIP AS FAORLIP " + "INNER JOIN " + _getCollection()
                + "FAORDIP AS FAORDIP ON (FAORLIP.EHIDOR=FAORDIP.EHIDOR) " + "INNER JOIN " + _getCollection()
                + "CARUBRP AS CARUBRP ON (FAORLIP.IDRUBRIQUE=CARUBRP.IDRUBRIQUE) ";
        return from;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return order;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        if (getForIdOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORLIP.EHIDOR="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdOrdreRegroupement());
        }

        if (getFromIdExterneRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "CARUBRP.IDEXTERNE like "
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterneRubrique() + "%");
        }

        if (getForNumOrdreRegroupement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORDIP.EHNORD="
                    + this._dbWriteNumeric(statement.getTransaction(), getForNumOrdreRegroupement());
        }
        if (getForNumCaisse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORDIP.EHIDCA=" + this._dbWriteNumeric(statement.getTransaction(), getForNumCaisse());
        }
        if (getForIdRubrique().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORLIP.IDRUBRIQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRubrique());
        }
        if (getForNature().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAORDIP.NATURE=" + this._dbWriteNumeric(statement.getTransaction(), getForNature());
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new FAOrdreAttribuer();
    }

    public String getForIdOrdreRegroupement() {
        return forIdOrdreRegroupement;
    }

    /**
     * @return
     */
    public java.lang.String getForIdRubrique() {
        return forIdRubrique;
    }

    public String getForNature() {
        return forNature;
    }

    /**
     * @return
     */
    public java.lang.String getForNumCaisse() {
        return forNumCaisse;
    }

    /**
     * @return
     */
    public java.lang.String getForNumOrdreRegroupement() {
        return forNumOrdreRegroupement;
    }

    /**
     * @return
     */
    public java.lang.String getFromIdExterneRubrique() {
        return fromIdExterneRubrique;
    }

    public void setForIdOrdreRegroupement(String newForIdOrdreRegroupement) {
        forIdOrdreRegroupement = newForIdOrdreRegroupement;
    }

    /**
     * @param string
     */
    public void setForIdRubrique(java.lang.String string) {
        forIdRubrique = string;
    }

    public void setForNature(String forNature) {
        this.forNature = forNature;
    }

    /**
     * @param string
     */
    public void setForNumCaisse(java.lang.String string) {
        forNumCaisse = string;
    }

    /**
     * @param string
     */
    public void setForNumOrdreRegroupement(java.lang.String string) {
        forNumOrdreRegroupement = string;
    }

    /**
     * @param string
     */
    public void setFromIdExterneRubrique(java.lang.String string) {
        fromIdExterneRubrique = string;
    }

}
