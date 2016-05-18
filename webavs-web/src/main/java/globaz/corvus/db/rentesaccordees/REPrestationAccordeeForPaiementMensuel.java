package globaz.corvus.db.rentesaccordees;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.adressepaiement.ITIAvoirPaiementDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;

/**
 * @author BSC
 */
public class REPrestationAccordeeForPaiementMensuel extends REPrestationsAccordees {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_RENTES = "idAP1";
    public static final String ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_STANDARD = "idAP2";

    public static final String ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES = "ap1";
    public static final String ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD = "ap2";

    public static final String FIELD_ID_ADR_PMT = "hciaiu";
    public static final String ID_ADR_PMT_RENTE = "idadressepmtrente";
    public static final String ID_ADR_PMT_STD = "idadressepmtstd";

    private String idAdrPmtRente = "";
    private String idAdrPmtStd = "";
    private String idCompteAnnexe = "";
    private String idDomaineApplication = "";
    private String idInfoCompta = "";
    private String idTiersAdressePmt = "";

    @Override
    protected String _getFields(BStatement statement) {

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableInfoCompta = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;

        StringBuilder sql = new StringBuilder();
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE)
                .append(",");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION)
                .append(",");

        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE)
                .append(",");

        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT)
                .append(",");
        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION)
                .append(",");
        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE)
                .append(",");

        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT).append(" AS ")
                .append(REPrestationAccordeeForPaiementMensuel.ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_RENTES)
                .append(",");

        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.ID_ADRESSE_PAIEMENT).append(" AS ")
                .append(REPrestationAccordeeForPaiementMensuel.ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_STANDARD);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {

        StringBuilder sql = new StringBuilder();

        String tablePrestationAccordee = _getCollection() + REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES;
        String tableInfoCompta = _getCollection() + REInformationsComptabilite.TABLE_NAME_INFO_COMPTA;
        String tableTiers = _getCollection() + ITITiersDefTable.TABLE_NAME;
        String tableAvoirAdressePaiement = _getCollection() + ITIAvoirPaiementDefTable.TABLE_NAME;

        String todayAAAAMMJJ = "";
        try {
            todayAAAAMMJJ = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(JACalendar.todayJJsMMsAAAA());
        } catch (JAException e) {
            e.printStackTrace();
        }

        sql.append(tablePrestationAccordee);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableInfoCompta);
        sql.append(" ON ");
        sql.append(tablePrestationAccordee).append(".").append(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA);
        sql.append("=");
        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableTiers);
        sql.append(" ON ");
        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        sql.append("=");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableAvoirAdressePaiement).append(" AS ")
                .append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES);
        sql.append(" ON (");

        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        sql.append("=");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.ID_TIERS);

        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.ID_APPLICATION);
        sql.append("=");
        sql.append(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);

        sql.append(" AND ((");
        sql.append(todayAAAAMMJJ);
        sql.append(" BETWEEN ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_DEBUT_RELATION);
        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_FIN_RELATION);
        sql.append(") OR ( ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_FIN_RELATION);
        sql.append("=0");
        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_RENTES).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_DEBUT_RELATION);
        sql.append("<=");
        sql.append(todayAAAAMMJJ);
        sql.append("))");
        sql.append(" AND ap1.hcidex = '') ");

        sql.append(" LEFT OUTER JOIN ");
        sql.append(tableAvoirAdressePaiement).append(" AS ")
                .append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD);
        sql.append(" ON (");

        sql.append(tableInfoCompta).append(".").append(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        sql.append("=");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.ID_TIERS);

        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.ID_APPLICATION);
        sql.append("=");
        sql.append(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_DEFAULT);

        sql.append(" AND (( ");
        sql.append(todayAAAAMMJJ);
        sql.append(" BETWEEN ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_DEBUT_RELATION);
        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_FIN_RELATION);
        sql.append(") OR ( ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_FIN_RELATION);
        sql.append("=0");
        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.DATE_DEBUT_RELATION);
        sql.append("<=");
        sql.append(todayAAAAMMJJ);
        sql.append("))");

        sql.append(" AND ");
        sql.append(REPrestationAccordeeForPaiementMensuel.ALIAS_TABLE_AVOIR_ADRESSE_PAIEMENT_STANDARD).append(".")
                .append(ITIAvoirPaiementDefTable.ID_EXTERNE);
        sql.append("=''");

        sql.append(")");

        return sql.toString();
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idInfoCompta = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_INFO_COMPTA);
        idTiersAdressePmt = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT);
        idDomaineApplication = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_DOMAINE_APPLICATION);
        idCompteAnnexe = statement.dbReadNumeric(REInformationsComptabilite.FIELDNAME_ID_COMPTE_ANNEXE);
        idAdrPmtRente = statement
                .dbReadNumeric(REPrestationAccordeeForPaiementMensuel.ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_RENTES);
        idAdrPmtStd = statement
                .dbReadNumeric(REPrestationAccordeeForPaiementMensuel.ALIAS_FIELD_ID_AVOIR_ADRESSE_PAIEMENT_STANDARD);
    }

    @Override
    public String getCollection() {
        return _getCollection();
    }

    public String getIdAdrPmtRente() {
        return idAdrPmtRente;
    }

    public String getIdAdrPmtStd() {
        return idAdrPmtStd;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    @Override
    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdTiersAdressePmt() {
        return idTiersAdressePmt;
    }

    public void setIdAdrPmtRente(String idAdrPmtRente) {
        this.idAdrPmtRente = idAdrPmtRente;
    }

    public void setIdAdrPmtStd(String idAdrPmtStd) {
        this.idAdrPmtStd = idAdrPmtStd;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    @Override
    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdTiersAdressePmt(String idTiersAdressePmt) {
        this.idTiersAdressePmt = idTiersAdressePmt;
    }

}
