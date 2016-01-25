package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;

public class CGExerciceComptableManager extends globaz.globall.db.BManager implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TRI_DATE_DEBUT = "DATEDEBUT";
    public final static String TRI_DATE_DEBUT_DESC = "DATEDEBUT_DESC";
    public final static String TRI_DATE_FIN = "DATEFIN";

    public final static String TRI_DATE_FIN_DESC = "DATEFIN DESC";
    private java.lang.String betweenDateDebutDateFin = "";
    private java.lang.Boolean forExerciceOuvert = null;
    private java.lang.String forIdMandat = new String();
    private java.lang.String fromDateDebut = "";
    private java.lang.String orderBy = "";
    private java.lang.String untilDateDebut = "";
    private java.lang.String untilDateFin = "";

    /**
     * Commentaire relatif au constructeur CGExerciceComptableManager.
     */
    public CGExerciceComptableManager() {
        super();
        wantCallMethodBeforeFind(true);
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CGEXERP";
        String table2 = "CGMANDP";
        return _getCollection() + table1 + " LEFT OUTER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IDMANDAT=" + _getCollection() + table2 + ".IDMANDAT)";
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        return orderBy;
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // traitement du positionnement
        if (getForIdMandat().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += _getCollection() + "CGEXERP.IDMANDAT="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdMandat());
        }

        if (getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEDEBUT>=" + _dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }

        if (getUntilDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEDEBUT<=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateDebut());
        }

        if (getUntilDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "DATEFIN<=" + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateFin());
        }
        if (getBetweenDateDebutDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " DATEDEBUT<=" + _dbWriteDateAMJ(statement.getTransaction(), getBetweenDateDebutDateFin());
            sqlWhere += " AND DATEFIN>=" + _dbWriteDateAMJ(statement.getTransaction(), getBetweenDateDebutDateFin());
        }

        if (getForExerciceOuvert() != null && getForExerciceOuvert().booleanValue()) {
            // ne pas afficher les exercice clôture
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ESTCLOTURE="
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(false), BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExerciceComptable();
    }

    /**
     * Returns the betweenDateDebutDateFin.
     * 
     * @return java.lang.String
     */
    public java.lang.String getBetweenDateDebutDateFin() {
        return betweenDateDebutDateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.10.2002 09:17:59)
     * 
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getForExerciceOuvert() {
        return forExerciceOuvert;
    }

    /**
     * Getter
     */
    public java.lang.String getForIdMandat() {
        return forIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 09:46:44)
     * 
     * @return java.lang.String
     */
    public java.lang.String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 12:01:19)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDateDebut() {
        return untilDateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 14:19:24)
     * 
     * @return java.lang.String
     */
    public java.lang.String getUntilDateFin() {
        return untilDateFin;
    }

    /**
     * Sets the betweenDateDebutDateFin.
     * 
     * @param betweenDateDebutDateFin
     *            The betweenDateDebutDateFin to set
     */
    public void setBetweenDateDebutDateFin(java.lang.String betweenDateDebutDateFin) {
        this.betweenDateDebutDateFin = betweenDateDebutDateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.10.2002 09:17:59)
     * 
     * @param newForExerciceOuvert
     *            java.lang.Boolean
     */
    public void setForExerciceOuvert(java.lang.Boolean newForExerciceOuvert) {
        forExerciceOuvert = newForExerciceOuvert;
    }

    /**
     * Setter
     */
    public void setForIdMandat(java.lang.String newForIdMandat) {
        forIdMandat = newForIdMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.09.2002 09:46:44)
     * 
     * @param newFromDateDebut
     *            java.lang.String
     */
    public void setFromDateDebut(java.lang.String newFromDateDebut) {
        fromDateDebut = newFromDateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 14:20:50)
     * 
     * @param order
     *            java.lang.String
     */
    public void setOrderBy(String order) {
        orderBy = order;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 12:01:19)
     * 
     * @param newUntilDateDebut
     *            java.lang.String
     */
    public void setUntilDateDebut(java.lang.String newUntilDateDebut) {
        untilDateDebut = newUntilDateDebut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.04.2003 14:19:24)
     * 
     * @param newUntilDateFin
     *            java.lang.String
     */
    public void setUntilDateFin(java.lang.String newUntilDateFin) {
        untilDateFin = newUntilDateFin;
    }

}
