/*
 * Créé le 15 fevr. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * @author bsc
 * 
 */

public class REPrestationsDuesManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsType = "";
    private String forCsTypePaiement = "";
    private String forIdPrestationDue = "";
    private String forIdRenteAccordes = "";
    private String forIdsPrestDues = "";
    private String forPeriodePDInMoisAnnee = "";
    private String fromDateDebut = "";
    private String fromDateFin = "";

    private String toDateDebut = "";
    private String toDateFin = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Renvoie la clause WHERE de la requête SQL
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */

    @Override
    protected String _getWhere(BStatement statement) {
        String sqlWhere = "";

        if (!JadeStringUtil.isIntegerEmpty(forIdsPrestDues)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_ID_PRESTATION_DUE + " IN (" + forIdsPrestDues + ")";

        }

        if (!JadeStringUtil.isIntegerEmpty(forPeriodePDInMoisAnnee)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "(" + REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + "<="
                    + _dbWriteNumeric(statement.getTransaction(), forPeriodePDInMoisAnnee) + " AND ((" +

                    REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), forPeriodePDInMoisAnnee) + ") OR ("
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " =0)))";

        }

        if (!JadeStringUtil.isIntegerEmpty(forIdPrestationDue)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_ID_PRESTATION_DUE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdPrestationDue);
        }

        if (!JadeStringUtil.isIntegerEmpty(forIdRenteAccordes)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forIdRenteAccordes);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsType)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_TYPE + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsType);
        }

        if (!JadeStringUtil.isIntegerEmpty(forCsTypePaiement)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_CS_TYPE_PAIEMENT + "="
                    + _dbWriteNumeric(statement.getTransaction(), forCsTypePaiement);
        }

        if (!JadeStringUtil.isIntegerEmpty(fromDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), fromDateDebut);
        }

        if (!JadeStringUtil.isIntegerEmpty(toDateDebut)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + "<="
                    + _dbWriteNumeric(statement.getTransaction(), toDateDebut);
        }

        if (!JadeStringUtil.isIntegerEmpty(fromDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "( " + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), fromDateFin) + " OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " IS NULL" + " OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " = 0) ";
        }

        if (!JadeStringUtil.isIntegerEmpty(toDateFin)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += "( " + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + "<="
                    + _dbWriteNumeric(statement.getTransaction(), toDateFin) + " OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " IS NULL" + " OR "
                    + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " = 0) ";
        }

        return sqlWhere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationDue();
    }

    /**
     * @return the forType
     */
    public String getForCsType() {
        return forCsType;
    }

    /**
     * @return
     */
    public String getForCsTypePaiement() {
        return forCsTypePaiement;
    }

    /**
     * @return the forIdPrestationDue
     */
    public String getForIdPrestationDue() {
        return forIdPrestationDue;
    }

    /**
     * @return the forIdRenteAccordes
     */
    public String getForIdRenteAccordes() {
        return forIdRenteAccordes;
    }

    public String getForIdsPrestDues() {
        return forIdsPrestDues;
    }

    public String getForPeriodePDInMoisAnnee() {
        return forPeriodePDInMoisAnnee;
    }

    /**
     * @return the fromDateDebut
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    public String getFromDateFin() {
        return fromDateFin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestationDue.FIELDNAME_ID_PRESTATION_DUE;
    }

    public String getToDateDebut() {
        return toDateDebut;
    }

    /**
     * @return the toDateFin
     */
    public String getToDateFin() {
        return toDateFin;
    }

    /**
     * @param forType
     *            the forType to set
     */
    public void setForCsType(String forType) {
        forCsType = forType;
    }

    /**
     * @param string
     */
    public void setForCsTypePaiement(String string) {
        forCsTypePaiement = string;
    }

    /**
     * @param forIdPrestationDue
     *            the forIdPrestationDue to set
     */
    public void setForIdPrestationDue(String forIdPrestationDue) {
        this.forIdPrestationDue = forIdPrestationDue;
    }

    /**
     * @param forIdRenteAccordes
     *            the forIdRenteAccordes to set
     */
    public void setForIdRenteAccordes(String forIdRenteAccordes) {
        this.forIdRenteAccordes = forIdRenteAccordes;
    }

    public void setForIdsPrestDues(String forIdsPrestDues) {
        this.forIdsPrestDues = forIdsPrestDues;
    }

    public void setForPeriodePDInMoisAnnee(String forPeriodePDInMoisAnnee) {
        this.forPeriodePDInMoisAnnee = forPeriodePDInMoisAnnee;
    }

    /**
     * @param fromDateDebut
     *            the fromDateDebut to set
     */
    public void setFromDateDebut(String fromDateDebut) {
        this.fromDateDebut = fromDateDebut;
    }

    public void setFromDateFin(String fromDateFin) {
        this.fromDateFin = fromDateFin;
    }

    public void setToDateDebut(String toDateDebut) {
        this.toDateDebut = toDateDebut;
    }

    /**
     * @param toDateFin
     *            the toDateFin to set
     */
    public void setToDateFin(String toDateFin) {
        this.toDateFin = toDateFin;
    }

}
