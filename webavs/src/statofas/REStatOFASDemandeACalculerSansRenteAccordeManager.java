/*
 * Créé le 28-08-2013 BY RCO
 */
package statofas;

import globaz.corvus.db.demandes.REDemandePrestationJointDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import java.util.Iterator;
import java.util.List;

public class REStatOFASDemandeACalculerSansRenteAccordeManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /*
     * Type de requête : SELECT CCJUWEB.TIPAVSP.HXNAVS AS AVS, CCJUWEB.TITIERP.HTLDE1 AS NOM, CCJUWEB.TITIERP.HTLDE2 AS
     * PRENOM FROM CCJUWEB.REDEVIE INNER JOIN CCJUWEB.REDEREN ON CCJUWEB.REDEVIE.YCIDVI = CCJUWEB.REDEREN.YAIDEM INNER
     * JOIN CCJUWEB.PRDEMAP ON CCJUWEB.REDEREN.YAIMDO = CCJUWEB.PRDEMAP.WAIDEM INNER JOIN CCJUWEB.TITIERP ON
     * CCJUWEB.PRDEMAP.WAITIE = CCJUWEB.TITIERP.HTITIE INNER JOIN CCJUWEB.TIPAVSP ON CCJUWEB.PRDEMAP.WAITIE =
     * CCJUWEB.TIPAVSP.HTITIE WHERE CCJUWEB.REDEVIE.YCBAJR = '1' AND CCJUWEB.REDEREN.YATETA
     * IN(52804001,52804002,52804003) AND CCJUWEB.TIPAVSP.HXNAVS NOT IN ( SELECT CCJUWEB.TIPAVSP.HXNAVS AS AVS FROM
     * CCJUWEB.REDEVIE INNER JOIN CCJUWEB.REDEREN ON CCJUWEB.REDEVIE.YCIDVI = CCJUWEB.REDEREN.YAIDEM INNER JOIN
     * CCJUWEB.PRDEMAP ON CCJUWEB.REDEREN.YAIMDO = CCJUWEB.PRDEMAP.WAIDEM INNER JOIN CCJUWEB.RERECAL ON
     * CCJUWEB.REDEREN.YAIRCA = CCJUWEB.RERECAL.YNIRCA INNER JOIN CCJUWEB.REBACAL ON CCJUWEB.RERECAL.YNIRCA =
     * CCJUWEB.REBACAL.YIIRCA INNER JOIN CCJUWEB.TITIERP ON CCJUWEB.PRDEMAP.WAITIE = CCJUWEB.TITIERP.HTITIE INNER JOIN
     * CCJUWEB.TIPAVSP ON CCJUWEB.PRDEMAP.WAITIE = CCJUWEB.TIPAVSP.HTITIE );
     */
    String AND = " AND ";
    String BETWEEN = " BETWEEN ";

    String COUNT = " COUNT ";

    String EGAL = " = ";

    // ETAT
    // CS :
    // - Enregistré 52804001
    // - Au calcul 52804002
    // - Calculé 52804003
    // - Validé 52804004
    // - Courant validé 52804005
    // - Transféré 52804006
    // - Partiel 52804007
    // Ils sont dans IREDemandeRente

    private String forCsAnneeStatOFAS = null;
    private List<String> forCsEtatDemandeIn = null;
    private String forCsEtatRenteDeVieillesse = null;
    private String forCsEtatRentePrestationAccordee = null;
    private final String FROM = " FROM ";

    private final String INNER_JOIN = " INNER JOIN ";

    private Boolean isEqualRentePrestationAccordee = true;

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

        sql.append("SELECT ").append(SCHEMA).append("TIPAVSP.HXNAVS AS AVS, ").append(SCHEMA)
                .append("TITIERP.HTLDE1 AS NOM, ").append(SCHEMA).append("TITIERP.HTLDE2 AS PRENOM");

        sql.append(FROM).append(SCHEMA).append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE)
                .append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(ON + SCHEMA)
                .append("REDEVIE.YCIDVI = ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE + POINT)
                .append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append(PRDemande.TABLE_NAME).append(ON + SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE + POINT)
                .append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION).append(" = ")
                .append(SCHEMA).append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDDEMANDE).append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("TITIERP").append(ON).append(SCHEMA)
                .append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDTIERS).append(" = ").append(SCHEMA)
                .append("TITIERP.HTITIE").append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("TIPAVSP").append(ON).append(SCHEMA)
                .append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDTIERS).append(" = ").append(SCHEMA)
                .append("TIPAVSP").append(".HTITIE").append(" ");

        if ((forCsEtatDemandeIn != null) || (forCsEtatDemandeIn != null)) {
            sql.append("WHERE ");
        }

        if (forCsEtatRenteDeVieillesse != null) {
            sql.append(SCHEMA).append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE)
                    .append(POINT + REDemandeRenteVieillesse.FIELDNAME_IS_AJOURNEMENT_REQUERANT).append(" = '")
                    .append(forCsEtatRenteDeVieillesse).append("'");
            isWhereExist = true;
        }

        if (forCsEtatDemandeIn != null) {
            if (isWhereExist) {
                sql.append(AND);
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

        sql.append(" AND ").append(SCHEMA).append("TIPAVSP.HXNAVS NOT IN (");

        sql.append("SELECT ").append(SCHEMA).append("TIPAVSP.HXNAVS AS AVS ");

        sql.append(FROM).append(SCHEMA).append(REDemandeRenteVieillesse.TABLE_NAME_DEMANDE_RENTE_VIEILLESSE)
                .append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE).append(ON + SCHEMA)
                .append("REDEVIE.YCIDVI = ").append(SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE + POINT)
                .append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append(PRDemande.TABLE_NAME).append(ON + SCHEMA)
                .append(REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE + POINT)
                .append(REDemandePrestationJointDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION).append(" = ")
                .append(SCHEMA).append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDDEMANDE).append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("RERECAL").append(ON)
                .append(SCHEMA + REDemandePrestationJointDemandeRente.TABLE_NAME_DEMANDE_RENTE)
                .append(POINT + REDemandePrestationJointDemandeRente.FIELDNAME_ID_RENTE_CALCULEE).append(" = ")
                .append(SCHEMA).append("RERECAL.YNIRCA ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("REBACAL").append(ON).append(SCHEMA)
                .append("RERECAL.YNIRCA = ").append(SCHEMA + "REBACAL.YIIRCA ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("TITIERP").append(ON).append(SCHEMA)
                .append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDTIERS).append(" = ").append(SCHEMA)
                .append("TITIERP.HTITIE").append(" ");

        sql.append(INNER_JOIN).append(" ").append(SCHEMA).append("TIPAVSP").append(ON).append(SCHEMA)
                .append(PRDemande.TABLE_NAME + POINT).append(PRDemande.FIELDNAME_IDTIERS).append(" = ").append(SCHEMA)
                .append("TIPAVSP").append(".HTITIE");

        sql.append(")");

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
        return new REStatOFASDemandeACalculer();
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

    public Boolean getIsEqualRentePrestationAccordee() {
        return isEqualRentePrestationAccordee;
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

    public void setIsEqualRentePrestationAccordee(Boolean isEqualRentePrestationAccordee) {
        this.isEqualRentePrestationAccordee = isEqualRentePrestationAccordee;
    }

}
