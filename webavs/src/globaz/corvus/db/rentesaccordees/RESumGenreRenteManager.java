/*
 * Créé le 15 nov. 07
 */
package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.tools.PRDateFormater;

/**
 * @author scr
 * 
 * 
 *         Somme des montants des rentes par genre de rente.
 * 
 */

public class RESumGenreRenteManager extends BManager {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean forIsEnErreur = null;

    private Boolean forIsPrestationBloquee = null;

    private Boolean forIsRetenue = null;

    // format : MM.AAAA
    private String forMMxAAAA = "";

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
         * SELECT sum(ZTMPRE), ZTLCPR FROM CVCIWEB.REPRACC WHERE CVCIWEB.REPRACC.ZTTETA IN (52820002,52820003) AND
         * CVCIWEB.REPRACC.ZTDDDR <= 200808 AND (CVCIWEB.REPRACC.ZTDFDR >= 200808 OR CVCIWEB.REPRACC.ZTDFDR = 0 OR
         * CVCIWEB.REPRACC.ZTDFDR IS NULL) AND (CVCIWEB.REPRACC.ZTBERR = '0' OR CVCIWEB.REPRACC.ZTBERR IS NULL OR
         * CVCIWEB.REPRACC.ZTBERR = '2' OR CVCIWEB.REPRACC.ZTBERR = '') GROUP BY ZTLCPR
         */

        final String TABLE = REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        final String C_CP = REPrestationsAccordees.FIELDNAME_CODE_PRESTATION;
        final String C_ET = REPrestationsAccordees.FIELDNAME_CS_ETAT;
        final String C_DD = REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT;
        final String C_DF = REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT;
        final String C_IERR = REPrestationsAccordees.FIELDNAME_IS_ERREUR;
        final String C_IBLK = REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE;
        final String C_IRET = REPrestationsAccordees.FIELDNAME_IS_RETENUES;
        final String C_MNT = REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION;
        final String col = _getCollection();

        String date_AAAAMM = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(getForMMxAAAA());

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("SUM ( " + C_MNT + ") as " + C_MNT + ", ");
        sql.append(C_CP);
        sql.append(" FROM ");
        sql.append(col + TABLE);
        sql.append(" WHERE ");
        sql.append(col + TABLE + "." + C_ET + " IN (" + IREPrestationAccordee.CS_ETAT_PARTIEL + ", "
                + IREPrestationAccordee.CS_ETAT_VALIDE + ") ");

        sql.append(" AND ").append(col + TABLE + "." + C_DD + " <= " + date_AAAAMM);

        sql.append(" AND (").append(col + TABLE + "." + C_DF + " >= " + date_AAAAMM);
        sql.append(" OR ").append(col + TABLE + "." + C_DF + " = 0 ");
        sql.append(" OR ").append(col + TABLE + "." + C_DF + " IS NULL )");

        if (getForIsEnErreur() != null) {
            if (getForIsEnErreur().booleanValue()) {
                sql.append(" AND " + col + TABLE + "." + C_IERR + " = '1'");
            } else {
                sql.append(" AND (" + col + TABLE + "." + C_IERR + " = '0'");
                sql.append(" OR " + col + TABLE + "." + C_IERR + " = '2'");
                sql.append(" OR " + col + TABLE + "." + C_IERR + " IS NULL");
                sql.append(" OR " + col + TABLE + "." + C_IERR + " = '')");
            }
        }

        if (getForIsRetenue() != null) {
            if (getForIsRetenue().booleanValue()) {
                sql.append(" AND " + col + TABLE + "." + C_IRET + " = '1'");
            } else {
                sql.append(" AND (" + col + TABLE + "." + C_IRET + " = '0'");
                sql.append(" OR " + col + TABLE + "." + C_IRET + " = '2'");
                sql.append(" OR " + col + TABLE + "." + C_IRET + " IS NULL");
                sql.append(" OR " + col + TABLE + "." + C_IRET + " = '')");
            }
        }

        if (getForIsPrestationBloquee() != null) {
            if (getForIsPrestationBloquee().booleanValue()) {
                sql.append(" AND " + col + TABLE + "." + C_IBLK + " = '1'");
            } else {
                sql.append(" AND (" + col + TABLE + "." + C_IBLK + " = '0'");
                sql.append(" OR " + col + TABLE + "." + C_IBLK + " = '2'");
                sql.append(" OR " + col + TABLE + "." + C_IBLK + " IS NULL");
                sql.append(" OR " + col + TABLE + "." + C_IBLK + " = '')");
            }
        }

        sql.append(" GROUP BY " + C_CP);

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        return null;

    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new RESumGenreRente();
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

    public String getForMMxAAAA() {
        return forMMxAAAA;
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

    public void setForMMxAAAA(String forMMxAAAA) {
        this.forMMxAAAA = forMMxAAAA;
    }
}
