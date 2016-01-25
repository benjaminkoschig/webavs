package globaz.helios.db.comptes;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.HashSet;

public class CGPeriodeComptableManager extends BManager implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TRI_DATE_DEBUT_AND_TYPE = CGPeriodeComptable.FIELD_DATEDEBUT + ", "
            + CGPeriodeComptable.FIELD_IDTYPEPERIODE;
    public final static String TRI_DATE_FIN = CGPeriodeComptable.FIELD_DATEFIN;
    public final static String TRI_DATE_FIN_AND_TYPE_DESC = CGPeriodeComptable.FIELD_DATEFIN + " DESC, "
            + CGPeriodeComptable.FIELD_IDTYPEPERIODE + " DESC ";
    public final static String TRI_DATE_FIN_ASC_AND_TYPE_ASC = CGPeriodeComptable.FIELD_DATEFIN + " ASC, "
            + CGPeriodeComptable.FIELD_IDTYPEPERIODE + " ASC ";
    public final static String TRI_DATE_FIN_CODE_DESC = CGPeriodeComptable.FIELD_DATEFIN + " DESC, "
            + CGPeriodeComptable.FIELD_CODE + " DESC ";
    public final static String TRI_DATE_FIN_DESC = CGPeriodeComptable.FIELD_DATEFIN + " DESC ";

    private HashSet exceptIdTypePeriode = null;
    private String forCode = "";
    private String forDateInPeriode = "";

    private String forIdBouclement = new String();;
    private String forIdExerciceComptable = new String();
    private String forIdJournal = new String();
    private String forIdTypePeriode = "";

    private boolean forPeriodeOuverte = false;
    private String fromDateDebut = "";

    private String fromDateFin = "";
    private String notForIdTypePeriode = "";
    private String orderBy = "";
    private String untilDateFin = "";

    /**
     * Commentaire relatif au constructeur CGPeriodeComptableManager.
     */
    public CGPeriodeComptableManager() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + CGPeriodeComptable.TABLE_NAME;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (JadeStringUtil.isBlank(orderBy)) {
            setOrderBy(" " + _getCollection() + CGPeriodeComptable.TABLE_NAME + "." + CGPeriodeComptable.FIELD_DATEFIN
                    + " ASC, " + _getCollection() + CGPeriodeComptable.TABLE_NAME + "."
                    + CGPeriodeComptable.FIELD_DATEDEBUT + " DESC ");
        }

        return orderBy;

    }

    /**
     * Retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        String sqlWhere = "";

        if (getForIdExerciceComptable().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDEXERCOMPTABLE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdExerciceComptable());
        }

        if (getNotForIdTypePeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDTYPEPERIODE + " <> "
                    + _dbWriteNumeric(statement.getTransaction(), getNotForIdTypePeriode());
        }

        if (getForIdTypePeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDTYPEPERIODE + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdTypePeriode());
        }

        if (getExceptIdTypePeriode() != null && getExceptIdTypePeriode().size() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDTYPEPERIODE + " NOT IN (";

            String values[] = (String[]) getExceptIdTypePeriode().toArray(new String[0]);
            for (int i = 0; i < values.length; i++) {
                sqlWhere += ((i != 0) ? "," : "") + _dbWriteNumeric(statement.getTransaction(), values[i]);
            }

            sqlWhere += ")";
        }

        if (getForIdJournal().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDJOURNAL + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdJournal());
        }

        if (getForIdBouclement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_IDBOUCLEMENT + " = "
                    + _dbWriteNumeric(statement.getTransaction(), getForIdBouclement());
        }

        if (getForCode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_CODE + " = "
                    + _dbWriteString(statement.getTransaction(), getForCode());
        }

        if (isForPeriodeOuverte()) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_ESTCOLTURE
                    + " = "
                    + _dbWriteBoolean(statement.getTransaction(), new Boolean(!forPeriodeOuverte),
                            BConstants.DB_TYPE_BOOLEAN_CHAR);
        }

        if (getForDateInPeriode().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_DATEDEBUT + " <= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateInPeriode());
            sqlWhere += " AND ";
            sqlWhere += CGPeriodeComptable.FIELD_DATEFIN + " >= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getForDateInPeriode());

        }

        if (getUntilDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_DATEFIN + " <= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getUntilDateFin());

        }

        if (getFromDateDebut() != null && getFromDateDebut().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_DATEDEBUT + " >= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateDebut());
        }

        if (getFromDateFin() != null && getFromDateFin().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += CGPeriodeComptable.FIELD_DATEFIN + " >= "
                    + _dbWriteDateAMJ(statement.getTransaction(), getFromDateFin());
        }

        return sqlWhere;
    }

    /**
     * new entity
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGPeriodeComptable();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 17:44:24)
     * 
     * @return java.util.HashSet
     */
    public java.util.HashSet getExceptIdTypePeriode() {
        return exceptIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:13:58)
     * 
     * @return String
     */
    public String getForCode() {
        return forCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 15:07:15)
     * 
     * @return String
     */
    public String getForDateInPeriode() {
        return forDateInPeriode;
    }

    public String getForIdBouclement() {
        return forIdBouclement;
    }

    /**
     * Getter
     */
    public String getForIdExerciceComptable() {
        return forIdExerciceComptable;
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:21:55)
     * 
     * @return String
     */
    public String getForIdTypePeriode() {
        return forIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:31:54)
     * 
     * @return String
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * Returns the fromDateFin.
     * 
     * @return String
     */
    public String getFromDateFin() {
        return fromDateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 09:31:37)
     * 
     * @return String
     */
    public String getNotForIdTypePeriode() {
        return notForIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:46:40)
     * 
     * @return String
     */
    public String getUntilDateFin() {
        return untilDateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 14:53:57)
     * 
     * @return boolean
     */
    public boolean isForPeriodeOuverte() {
        return forPeriodeOuverte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.06.2003 17:44:24)
     * 
     * @param newExceptIdTypePeriode
     *            java.util.HashSet
     */
    public void setExceptIdTypePeriode(java.util.HashSet newExceptIdTypePeriode) {
        exceptIdTypePeriode = newExceptIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (27.05.2003 11:13:58)
     * 
     * @param newForCode
     *            String
     */
    public void setForCode(String newForCode) {
        forCode = newForCode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 15:07:15)
     * 
     * @param newForDateInPeriode
     *            String
     */
    public void setForDateInPeriode(String newForDateInPeriode) {
        forDateInPeriode = newForDateInPeriode;
    }

    public void setForIdBouclement(String newForIdBouclement) {
        forIdBouclement = newForIdBouclement;
    }

    /**
     * Setter
     */
    public void setForIdExerciceComptable(String newForIdExerciceComptable) {
        forIdExerciceComptable = newForIdExerciceComptable;
    }

    public void setForIdJournal(String newForIdJournal) {
        forIdJournal = newForIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:21:55)
     * 
     * @param newForIdTypePeriode
     *            String
     */
    public void setForIdTypePeriode(String newForIdTypePeriode) {
        forIdTypePeriode = newForIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 14:53:57)
     * 
     * @param newForPeriodeOuverte
     *            boolean
     */
    public void setForPeriodeOuverte(boolean newForPeriodeOuverte) {
        forPeriodeOuverte = newForPeriodeOuverte;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 13:31:54)
     * 
     * @param newFromDateDebut
     *            String
     */
    public void setFromDateDebut(String newFromDateDebut) {
        fromDateDebut = newFromDateDebut;
    }

    /**
     * Sets the fromDateFin.
     * 
     * @param fromDateFin
     *            The fromDateFin to set
     */
    public void setFromDateFin(String fromDateFin) {
        this.fromDateFin = fromDateFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 09:31:37)
     * 
     * @param newNotForIdTypePeriode
     *            String
     */
    public void setNotForIdTypePeriode(String newNotForIdTypePeriode) {
        notForIdTypePeriode = newNotForIdTypePeriode;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:22:44)
     * 
     * @param newOrderBy
     *            String
     */
    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 15:46:40)
     * 
     * @param newUntilDateFin
     *            String
     */
    public void setUntilDateFin(String newUntilDateFin) {
        untilDateFin = newUntilDateFin;
    }

}
