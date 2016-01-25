/*
 * Cr�� le 28-08-2013 BY RCO
 */
package statofas;

import globaz.corvus.db.demandes.REDemandePrestationJointDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import java.util.Iterator;
import java.util.List;

public class REStatOFASManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /*
     * Requ�te du type : SELECT COUNT (ANNEE) As Comptage, ANNEE FROM ( SELECT COUNT (CCJUWEB.REDEVIE.YCBAJR, MIN (
     * TRUNC (CCJUWEB.REPRACC.ZTDDDR, -2)/100) As ANNEE FROM CCJUWEB.REDEVIE INNER JOIN CCJUWEB.REDEREN ON
     * CCJUWEB.REDEVIE.YCIDVI = CCJUWEB.REDEREN.YAIDEM INNER JOIN CCJUWEB.RERECAL ON CCJUWEB.REDEREN.YAIRCA =
     * CCJUWEB.RERECAL.YNIRCA INNER JOIN CCJUWEB.REBACAL ON CCJUWEB.RERECAL.YNIRCA = CCJUWEB.REBACAL.YIIRCA INNER JOIN
     * CCJUWEB.REREACC ON CCJUWEB.REBACAL.YIIBCA = CCJUWEB.REREACC.YLIBAC INNER JOIN CCJUWEB.REPRACC ON
     * CCJUWEB.REREACC.YLIRAC = CCJUWEB.REPRACC.ZTIPRA WHERE CCJUWEB.REDEVIE.YCBAJR = '1' AND (2013>=( TRUNC
     * (CCJUWEB.REPRACC.ZTDDDR, -2)/100) AND ( TRUNC (CCJUWEB.REPRACC.ZTDDDR, -2)/100) >= 2009) AND
     * CCJUWEB.rederen.yateta < 52804004 GROUP BY CCJUWEB.REPRACC.ZTITBE) AS TMP GROUP BY ANNEE;
     */
    String AND = " AND ";
    String BETWEEN = " BETWEEN ";

    String COUNT = " COUNT ";

    String EGAL = " = ";

    // ETAT
    // CS :
    // - Enregistr� 52804001
    // - Au calcul 52804002
    // - Calcul� 52804003
    // - Valid� 52804004
    // - Courant valid� 52804005
    // - Transf�r� 52804006
    // - Partiel 52804007
    // Ils sont dans IREDemandeRente

    private String forCsAnneeStatOFAS = null;
    private List<String> forCsEtatDemandeIn = null;
    private String forCsEtatRenteDeVieillesse = null;
    private String forCsEtatRentePrestationAccordee = null;

    private final String FROM = " FROM ";

    private final String INNER_JOIN = " INNER JOIN ";

    private final String LEFT_JOIN = " LEFT OUTER JOIN ";

    private final String MIN = " MIN ";

    private final String ON = " ON ";

    private final String OR = " OR ";

    private final String POINT = ".";
    private final String TRUNC = " TRUNC ";

    /**
     * selectionne en une seule requete les infos necessaires pour les stat OFAS
     */
    @Override
    protected String _getSql(BStatement statement) {
        boolean isWhereExist = false;

        String tableDemandeRente = _getCollection() + REDemandeRente.TABLE_NAME_DEMANDE_RENTE;

        String SCHEMA = _getCollection();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ").append(COUNT).append("(ANNEE) As Comptage, CAST(ANNEE AS INTEGER)AS ANNEE FROM (");
        sql.append("SELECT ").append(COUNT).append("(").append(_getCollection())
                .append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE).append(POINT)
                .append(REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT).append("), ");

        sql.append(MIN).append("(").append(TRUNC).append("(")
                .append(_getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", -2)/100) As ANNEE ");
        sql.append(FROM).append(SCHEMA).append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE)
                .append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(ON + SCHEMA)
                .append("REDEVIE.YCIDVI = ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE + POINT)
                .append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("RERECAL").append(ON)
                .append(SCHEMA + REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE)
                .append(POINT + REDemandePrestationJointDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append(" = ")
                .append(SCHEMA).append("RERECAL.YNIRCA ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("REBACAL").append(ON).append(SCHEMA)
                .append("RERECAL.YNIRCA = ").append(SCHEMA + "REBACAL.YIIRCA ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("REREACC").append(ON).append(SCHEMA)
                .append("REBACAL.YIIBCA = ").append(SCHEMA + "REREACC.YLIBAC ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA)
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(ON)
                .append(SCHEMA + "REREACC.YLIRAC = ").append(SCHEMA)
                .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES).append(POINT + "ZTIPRA ");

        if ((forCsEtatRentePrestationAccordee != null) || (forCsAnneeStatOFAS != null) || (forCsEtatDemandeIn != null)) {
            sql.append("WHERE ");
        }

        if (forCsEtatRenteDeVieillesse != null) {
            sql.append(SCHEMA).append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE)
                    .append(POINT + REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT).append(" = '")
                    .append(forCsEtatRenteDeVieillesse).append("'");
            isWhereExist = true;
        }

        if (forCsAnneeStatOFAS != null) {
            if (isWhereExist) {
                sql.append(AND);
            }

            sql.append("(").append(this._dbWriteNumeric(null, forCsAnneeStatOFAS)).append(">=").append("(")
                    .append(TRUNC).append("(").append(SCHEMA + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", -2)/100)");

            sql.append(AND).append("(").append(TRUNC).append("(").append(SCHEMA)
                    .append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT + REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT).append(", -2)/100) >= ")
                    .append(String.valueOf(Integer.parseInt(this._dbWriteNumeric(null, forCsAnneeStatOFAS)) - 4))
                    .append(")");

            isWhereExist = true;
        }

        if (forCsEtatDemandeIn != null) {
            if (isWhereExist) {
                sql.append(" AND ");
            }
            sql.append(tableDemandeRente).append(POINT).append(REDemandeRente.FIELDNAME_CS_ETAT).append(" IN(");
            for (Iterator<String> iterator = forCsEtatDemandeIn.iterator(); iterator.hasNext();) {
                String next = iterator.next();
                sql.append(next);
                if (iterator.hasNext()) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }

        if (forCsEtatRentePrestationAccordee != null) {
            sql.append(" AND ").append(SCHEMA).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                    .append(POINT).append(REPrestationsAccordees.FIELDNAME_CS_ETAT).append(" = ");
            sql.append(getForCsEtatRentePrestationAccordee());
        }

        // sql.append(this.AND).append(SCHEMA).append("rederen.yateta < 52804004 ");
        sql.append(" GROUP BY ").append(SCHEMA).append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES)
                .append(POINT + "ZTITBE) AS TMP ");
        sql.append("GROUP BY ANNEE ");
        sql.append("ORDER BY ANNEE DESC");

        return sql.toString();
    }

    @Override
    protected java.lang.String _getSqlCount(BStatement statement) {
        _addError(statement.getTransaction(), "Unsuported function. Use RECountManager.getCount() method instead");
        return null;
    }

    /**
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REStatOFAS();
    }

    public String getForCsAnneeStatOFAS() {
        return forCsAnneeStatOFAS;
    }

    public List<String> getForCsEtatDemandeIn() {
        return forCsEtatDemandeIn;
    }

    public String getForCsEtatRenteDeVieillesse() {
        return forCsEtatRenteDeVieillesse;
    }

    public String getForCsEtatRentePrestationAccordee() {
        return forCsEtatRentePrestationAccordee;
    }

    public void setForCsAnneeStatOFAS(String forCsAnneeStatOFAS) {
        this.forCsAnneeStatOFAS = forCsAnneeStatOFAS;
    }

    public void setForCsEtatDemandeIn(List<String> forCsEtatDemandeIn) {
        this.forCsEtatDemandeIn = forCsEtatDemandeIn;
    }

    public void setForCsEtatRenteDeVieillesse(String forCsEtatRenteDeVieillesse) {
        this.forCsEtatRenteDeVieillesse = forCsEtatRenteDeVieillesse;
    }

    public void setForCsEtatRentePrestationAccordee(String forCsEtatRentePrestationAccordee) {
        this.forCsEtatRentePrestationAccordee = forCsEtatRentePrestationAccordee;
    }

}
