/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 * 
 *         Nombre de RA à payer :
 * 
 *         Nombre de prestations à payer : RA dans l'état partiel ou validé, inclusent dans la période de pmt.
 * 
 * 
 * 
 * 
 */

public class RECountRentesManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forCsEtatIn = "";

    // format : MM.AAAA
    private String forDatePmt = "";

    private Boolean forIsEnErreur = null;
    private Boolean forIsPrestationBloquee = null;
    private Boolean forIsRetenue = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * selectionne en une seule requete toutes les infos necessaires au bouclement ALFA des caisses horlogeres.
     * 
     * <p>
     * redefini car la requete est un peu compliquee.
     * </p>
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getSql(BStatement statement) {
        /*
         * Exemple :
         * 
         * select REPRACC.YLIRAC from webavs.rereacc WHERE WEBAVS.REPRACC.YLTETA IN (52820003,52820002 ) AND
         * WEBAVS.REPRACC.YLDDDR <= 200802 AND ( WEBAVS.REPRACC.YLDFDR >= 200802 OR (WEBAVS.REPRACC.YLDFDR = 0 OR
         * WEBAVS.REPRACC.YLDFDR IS NULL))
         */

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT ");

        sql.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);
        sql.append(" FROM ");
        sql.append(_getCollection());
        sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

        String sqlWhere = "";

        if (!JadeStringUtil.isBlankOrZero(getForDatePmt())) {

            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += (REPrestationsAccordees.FIELDNAME_CS_ETAT);
            sqlWhere += (" IN (");
            sqlWhere += (getForCsEtatIn());
            sqlWhere += (" ) ");
        }

        if (!JadeStringUtil.isBlankOrZero(getForCsEtatIn())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }

            sqlWhere += (REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
            sqlWhere += (" <= ");
            sqlWhere += (PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePmt()));

            sqlWhere += (" AND (");

            sqlWhere += (REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere += (" >= ");
            sqlWhere += (PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePmt()));
            sqlWhere += (" OR ( ");
            sqlWhere += (REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere += (" = 0 OR ");
            sqlWhere += (REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
            sqlWhere += (" IS NULL ))");

        }

        if (getForIsEnErreur() != null) {
            if (getForIsEnErreur().booleanValue()) {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  ='1' ");
            } else {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '0' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" = '2' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" IS NULL OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '' ");
            }
        }

        if (getForIsRetenue() != null) {
            if (getForIsRetenue().booleanValue()) {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  ='1' ");
            } else {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  = '0' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(" = '2' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(" IS NULL OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  = '' ");
            }
        }

        if (getForIsPrestationBloquee() != null) {
            if (getForIsPrestationBloquee().booleanValue()) {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append("  ='1' ");
            } else {
                sql.append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append("  = '0' OR ").append(_getCollection())
                        .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append("  = '2' OR ")
                        .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                        .append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                        .append(" IS NULL OR ").append(_getCollection())
                        .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                        .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append("  = '' ");
            }
        }

        if (sqlWhere.length() != 0) {
            sql.append(" WHERE ");
            sql.append(sqlWhere);
        }

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        /*
         * Exemple :
         * 
         * select count(*) from webavs.rereacc WHERE WEBAVS.REREACC.YLTETA IN (52820003,52820002 ) AND
         * WEBAVS.REREACC.YLDDDR <= 200802 AND ( WEBAVS.REREACC.YLDFDR >= 200802 OR (WEBAVS.REREACC.YLDFDR = 0 OR
         * WEBAVS.REREACC.YLDFDR IS NULL))
         */

        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT COUNT(*) FROM ");
            sql.append(_getCollection());
            sql.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);

            StringBuffer sqlWhere = new StringBuffer();

            if (!JadeStringUtil.isBlankOrZero(getForCsEtatIn())) {

                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(REPrestationsAccordees.FIELDNAME_CS_ETAT);
                sqlWhere.append(" IN (");
                sqlWhere.append(getForCsEtatIn());
                sqlWhere.append(" ) ");
            }

            if (!JadeStringUtil.isBlankOrZero(getForDatePmt())) {
                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT);
                sqlWhere.append(" <= ");
                sqlWhere.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePmt()));

                sqlWhere.append(" AND (");

                sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
                sqlWhere.append(" >= ");
                sqlWhere.append(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForDatePmt()));
                sqlWhere.append(" OR ( ");
                sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
                sqlWhere.append(" = 0 OR ");
                sqlWhere.append(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT);
                sqlWhere.append(" IS NULL ))");

            }

            if (getForIsEnErreur() != null) {

                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                if (getForIsEnErreur().booleanValue()) {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  ='1') ");
                } else {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '0' OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" = '2' OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append(" IS NULL OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_ERREUR).append("  = '') ");
                }
            }

            if (getForIsRetenue() != null) {

                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                if (getForIsRetenue().booleanValue()) {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  ='1') ");
                } else {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  = '0' OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(" = '2' OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append(" IS NULL OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_RETENUES).append("  = '') ");
                }
            }

            if (getForIsPrestationBloquee() != null) {

                if (sqlWhere.length() != 0) {
                    sqlWhere.append(" AND ");
                }

                if (getForIsPrestationBloquee().booleanValue()) {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append("  ='1') ");
                } else {
                    sqlWhere.append("(").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append("  = '0' OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                            .append("  = '2' OR ").append(_getCollection())
                            .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(".")
                            .append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE).append(" IS NULL OR ")
                            .append(_getCollection()).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                            .append(".").append(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE)
                            .append("  = '') ");
                }
            }

            if (sqlWhere.length() != 0) {
                sql.append(" WHERE ");
                sql.append(sqlWhere);
            }

            return sql.toString();
        } catch (Exception e) {
            JadeLogger.warn(this, "PROBLEM IN FUNCTION _getSqlCount() (" + e.toString() + ")");
            return "";
        }

    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RECountRentes();
    }

    public String getForCsEtatIn() {
        return forCsEtatIn;
    }

    public String getForDatePmt() {
        return forDatePmt;
    }

    public Boolean getForIsEnErreur() {
        return forIsEnErreur;
    }

    public Boolean getForIsPrestationBloquee() {
        return forIsPrestationBloquee;
    }

    public Boolean getForIsRetenue() {
        return forIsRetenue;
    }

    public void setForCsEtatIn(String forCsEtatIn) {
        this.forCsEtatIn = forCsEtatIn;
    }

    public void setForDatePmt(String forDatePmt) {
        this.forDatePmt = forDatePmt;
    }

    public void setForIsEnErreur(Boolean forIsEnErreur) {
        this.forIsEnErreur = forIsEnErreur;
    }

    public void setForIsPrestationBloquee(Boolean forIsPrestationBloquee) {
        this.forIsPrestationBloquee = forIsPrestationBloquee;
    }

    public void setForIsRetenue(Boolean forIsRetenue) {
        this.forIsRetenue = forIsRetenue;
    }
}
