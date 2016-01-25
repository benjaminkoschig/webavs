package globaz.naos.db.listeAgenceCommunale;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class AFListeAgenceCommunaleManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String forDateValeur = new String();
    private java.lang.String forIdTiersAgence = new String();
    private Boolean forIdTiersAgenceVide = new Boolean(false);
    private String notInTypeAffiliation = "";
    protected java.lang.String order = "AFAFFIP.MALNAF ASC";

    /** Permet de filtrer les plans selon les droits accordés à l'utilisateur */
    private Boolean wantFilterByPlanFacturation = new Boolean(false);

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return AFListeAgenceCommunale.TABLE_FIELDS;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + "AFAFFIP AS AFAFFIP" + " INNER JOIN " + _getCollection()
                + "TITIERP TITIERP ON (AFAFFIP.HTITIE=TITIERP.HTITIE)" + " LEFT JOIN " + _getCollection()
                + "TICTIEP TICTIEP ON (TITIERP.HTITIE=TICTIEP.HTITIP AND TICTIEP.HGTTLI IN (507007,507008))"
                + " LEFT JOIN " + _getCollection() + "TITIERP TITIER2 ON (TICTIEP.HTITIE=TITIER2.HTITIE)"
                + " LEFT JOIN " + _getCollection() + "TIADMIP TIADMIP ON (TIADMIP.HTITIE=TITIER2.HTITIE)";
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
        // String sqlWhere = " AFAFFIP.MATTAF <> 804007 AND AFAFFIP.MABTRA=2";
        String sqlWhere = "MABTRA<>1";

        if (!JadeStringUtil.isBlankOrZero(getForDateValeur())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            String date = this._dbWriteDateAMJ(statement.getTransaction(), forDateValeur);
            sqlWhere += "((" + date + " between MADDEB and MADFIN) or (MADFIN=0 and MADDEB<= " + date + " ))";
        }

        if (!JadeStringUtil.isBlankOrZero(getForIdTiersAgence())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TITIER2.HTITIE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiersAgence());
        }
        if (getForIdTiersAgenceVide().booleanValue()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(TITIER2.HTITIE IS NULL OR TITIER2.HTITIE=0)";
        }

        if (!JadeStringUtil.isBlankOrZero(getNotInTypeAffiliation())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "MATTAF not in (" + getNotInTypeAffiliation() + ")";
        }
        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new AFListeAgenceCommunale();
    }

    public java.lang.String getForDateValeur() {
        return forDateValeur;
    }

    public java.lang.String getForIdTiersAgence() {
        return forIdTiersAgence;
    }

    public Boolean getForIdTiersAgenceVide() {
        return forIdTiersAgenceVide;
    }

    public String getNotInTypeAffiliation() {
        return notInTypeAffiliation;
    }

    /**
     * Permet de filtrer les plans selon les droits accordés à l'utilisateur
     */
    public Boolean getWantFilterByPlanFacturation() {
        return wantFilterByPlanFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleDe() {
        order = "LIBELLEDE";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleFr() {
        order = "LIBELLEFR";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.05.2002 13:28:35)
     * 
     * @param newOrder
     *            java.lang.String
     */
    public void orderByLibelleIt() {
        order = "LIBELLEIT";
    }

    public void setForDateValeur(java.lang.String forDateValeur) {
        this.forDateValeur = forDateValeur;
    }

    public void setForIdTiersAgence(java.lang.String forIdTiersAgence) {
        this.forIdTiersAgence = forIdTiersAgence;
    }

    public void setForIdTiersAgenceVide(Boolean forIdTiersAgenceVide) {
        this.forIdTiersAgenceVide = forIdTiersAgenceVide;
    }

    public void setNotInTypeAffiliation(String notInTypeAffiliation) {
        this.notInTypeAffiliation = notInTypeAffiliation;
    }

    /**
     * Permet de filtrer les plans selon les droits accordés à l'utilisateur
     */
    public void setWantFilterByPlanFacturation(Boolean boolean1) {
        wantFilterByPlanFacturation = boolean1;
    }

}
