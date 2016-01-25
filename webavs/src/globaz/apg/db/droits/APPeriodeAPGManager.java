/*
 * Créé le 10 mai 05
 */
package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.PRAbstractManager;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPeriodeAPGManager extends PRAbstractManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = "";
    private String forDateFin = "";
    private String forIdDroit = "";
    private String forIdPeriode = "";
    private String forNbrJours = "";
    private String forTypePeriode = "";

    private String fromDateDebut = "";
    private String fromDateFin = "";
    private String fromNbrJours = "";
    private String fromTypePeriode = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        return _getCollection() + APPeriodeAPG.TABLE_NAME;
    }

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

        if (!JadeStringUtil.isEmpty(getForIdPeriode())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_IDPERIODE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdPeriode());
        }

        if (!JAUtil.isDateEmpty(getForDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_DATEDEBUT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForDateDebut());
        }

        if (!JAUtil.isDateEmpty(getForDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_DATEFIN + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForDateFin());
        }

        if (!JadeStringUtil.isIntegerEmpty(getForNbrJours())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_NBRJOURS + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForNbrJours());
        }

        if (!JadeStringUtil.isEmpty(getForTypePeriode())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_TYPEPERIODE + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForTypePeriode());
        }

        if (!JadeStringUtil.isEmpty(getForIdDroit())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_IDDROIT + "="
                    + _dbWriteNumeric(statement.getTransaction(), getForIdDroit());
        }

        if (!JAUtil.isDateEmpty(getFromDateDebut())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_DATEDEBUT + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getFromDateDebut());
        }

        if (!JAUtil.isDateEmpty(getFromDateFin())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_DATEFIN + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getFromDateFin());
        }

        if (!JadeStringUtil.isIntegerEmpty(getFromNbrJours())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_NBRJOURS + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getFromNbrJours());
        }

        if (!JadeStringUtil.isEmpty(getFromTypePeriode())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += APPeriodeAPG.FIELDNAME_TYPEPERIODE + ">="
                    + _dbWriteNumeric(statement.getTransaction(), getFromTypePeriode());
        }

        return sqlWhere;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APPeriodeAPG();
    }

    /**
     * getter pour l'attribut for date debut
     * 
     * @return la valeur courante de l'attribut for date debut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * getter pour l'attribut for date fin
     * 
     * @return la valeur courante de l'attribut for date fin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * getter pour l'attribut for id droit
     * 
     * @return la valeur courante de l'attribut for id droit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * getter pour l'attribut for id periode
     * 
     * @return la valeur courante de l'attribut for id periode
     */
    public String getForIdPeriode() {
        return forIdPeriode;
    }

    /**
     * getter pour l'attribut for nbr jours
     * 
     * @return la valeur courante de l'attribut for nbr jours
     */
    public String getForNbrJours() {
        return forNbrJours;
    }

    /**
     * getter pour l'attribut for type periode
     * 
     * @return la valeur courante de l'attribut for type periode
     */
    public String getForTypePeriode() {
        return forTypePeriode;
    }

    /**
     * getter pour l'attribut from date debut
     * 
     * @return la valeur courante de l'attribut from date debut
     */
    public String getFromDateDebut() {
        return fromDateDebut;
    }

    /**
     * getter pour l'attribut from date fin
     * 
     * @return la valeur courante de l'attribut from date fin
     */
    public String getFromDateFin() {
        return fromDateFin;
    }

    /**
     * getter pour l'attribut from nbr jours
     * 
     * @return la valeur courante de l'attribut from nbr jours
     */
    public String getFromNbrJours() {
        return fromNbrJours;
    }

    /**
     * getter pour l'attribut from type periode
     * 
     * @return la valeur courante de l'attribut from type periode
     */
    public String getFromTypePeriode() {
        return fromTypePeriode;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     * 
     * @return la valeur courante de l'attribut order by defaut
     */
    @Override
    public String getOrderByDefaut() {
        return APPeriodeAPG.FIELDNAME_IDPERIODE;
    }

    /**
     * setter pour l'attribut for date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateDebut(String string) {
        forDateDebut = string;
    }

    /**
     * setter pour l'attribut for date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForDateFin(String string) {
        forDateFin = string;
    }

    /**
     * setter pour l'attribut for id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdDroit(String string) {
        forIdDroit = string;
    }

    /**
     * setter pour l'attribut for id periode
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForIdPeriode(String string) {
        forIdPeriode = string;
    }

    /**
     * setter pour l'attribut for nbr jours
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForNbrJours(String string) {
        forNbrJours = string;
    }

    /**
     * setter pour l'attribut for type periode
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setForTypePeriode(String string) {
        forTypePeriode = string;
    }

    /**
     * setter pour l'attribut from date debut
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateDebut(String string) {
        fromDateDebut = string;
    }

    /**
     * setter pour l'attribut from date fin
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromDateFin(String string) {
        fromDateFin = string;
    }

    /**
     * setter pour l'attribut from nbr jours
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromNbrJours(String string) {
        fromNbrJours = string;
    }

    /**
     * setter pour l'attribut from type periode
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setFromTypePeriode(String string) {
        fromTypePeriode = string;
    }
}
