package globaz.osiris.db.rentes.section;

import globaz.globall.db.BConstants;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import java.sql.SQLException;

public class CARentesSection {

    private String dateCreation;

    private String dateDebutPeriode;
    private String dateEcheance;
    private String dateFinPeriode;
    private String domaine;
    private String idCompteAnnexe = "0";
    private String idExterne;
    private String idJournal = "0";
    private String idSection = "0";
    protected BPreparedStatement insert;
    private String montantBase;
    private String montantPaiment;

    private String typeAdresse;

    /**
     * Initialise le Prepared Statement servant insert.
     */
    public CARentesSection(BTransaction transaction) throws Exception {
        insert = new BPreparedStatement(transaction);
        insert.prepareStatement(getPreparedSqlQuery(transaction));
    }

    /**
     * Termine le prepare statement.
     * 
     * @throws Exception
     */
    public void closeStatement() throws Exception {
        insert.closePreparedStatement();
    }

    /**
     * Exécute la query INSERT.
     * 
     * @return
     * @throws SQLException
     */
    public void executeQuery(BTransaction transaction) throws Exception {
        insert.execute();
    }

    /**
     * Efface puis remplit les variables à insérer en base de données.
     * 
     * @throws Exception
     */
    public void fillVariables() throws Exception {
        insert.clearParameters();

        insert.setInt(1, JadeStringUtil.parseInt(getDomaine(), 0));
        insert.setInt(2, JadeStringUtil.parseInt(getTypeAdresse(), 0));

        insert.setInt(3, JadeStringUtil.parseInt(getIdSection(), 0));
        insert.setInt(4, JadeStringUtil.parseInt(getIdCompteAnnexe(), 0));
        insert.setString(5, getIdExterne());
        insert.setInt(6, JadeStringUtil.parseInt(getDateCreation(), 0));
        insert.setInt(7, JadeStringUtil.parseInt(getDateEcheance(), 0));
        insert.setInt(8, JadeStringUtil.parseInt(getDateDebutPeriode(), 0));
        insert.setInt(9, JadeStringUtil.parseInt(getDateFinPeriode(), 0));
        insert.setDouble(10, JadeStringUtil.parseDouble(getMontantBase(), 0));
        insert.setDouble(11, JadeStringUtil.parseDouble(getMontantPaiment(), 0));
        insert.setInt(12, JadeStringUtil.parseInt(getIdJournal(), 0));
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getDomaine() {
        return domaine;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontantBase() {
        return montantBase;
    }

    public String getMontantPaiment() {
        return montantPaiment;
    }

    /**
     * Contruit la requête INSERT.
     * 
     * @param transaction
     * @return
     */
    private String getPreparedSqlQuery(BTransaction transaction) {
        String sql = "INSERT INTO " + Jade.getInstance().getDefaultJdbcSchema() + "." + CASection.TABLE_CASECTP + " (";

        sql += CASection.FIELD_DOMAINE + ", ";
        sql += CASection.FIELD_TYPEADRESSE + ", ";
        sql += CASection.FIELD_IDSECTION + ", ";
        sql += CASection.FIELD_IDCOMPTEANNEXE + ", ";
        sql += CASection.FIELD_IDEXTERNE + ", ";
        sql += CASection.FIELD_DATESECTION + ", ";
        sql += CASection.FIELD_DATEECHEANCE + ", ";
        sql += CASection.FIELD_DATEDEBUTPERIODE + ", ";
        sql += CASection.FIELD_DATEFINPERIODE + ", ";
        sql += CASection.FIELD_BASE + ", ";
        sql += CASection.FIELD_PMTCMP + ", ";
        sql += CASection.FIELD_IDJOURNAL + ", ";

        sql += CASection.FIELD_IDPLANRECOUVREMENT + ", ";
        sql += CASection.FIELD_IDREMARQUE + ", ";
        sql += CASection.FIELD_IDPOSJOU + ", ";
        sql += CASection.FIELD_IDSEQCON + ", ";
        sql += CASection.FIELD_CONTENTIEUXESTSUS + ", ";
        sql += CASection.FIELD_IDMOTCONSUS + ", ";
        sql += CASection.FIELD_SOLDE + ", ";
        sql += CASection.FIELD_TAXES + ", ";
        sql += CASection.FIELD_FRAIS + ", ";
        sql += CASection.FIELD_INTERETS + ", ";
        sql += CASection.FIELD_IDLASTETAPECTX + ", ";
        sql += CASection.FIELD_DATESUSPENDU + ", ";
        sql += CASection.FIELD_IDSECTIONPRINC + ", ";
        sql += CASection.FIELD_IDMODECOMPENSATION + ", ";
        sql += CASection.FIELD_IDCAISSEPROFESSIONNELE + ", ";
        sql += CASection.FIELD_IDTYPESECTION + ", ";
        sql += CASection.FIELD_CATEGORIESECTION + ", ";
        sql += BSpy.FIELDNAME;

        sql += ") VALUES (";

        sql += "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ";
        sql += "0, 0, 0, " + BConstants.DB_BOOLEAN_TRUE + ", " + BConstants.DB_BOOLEAN_FALSE_DELIMITED
                + ", 0, 0.00, 0.00, 0.00, 0.00, 0, 0, 0, 0, 0, ";
        sql += APISection.ID_TYPE_SECTION_RENTE_AVS_AI + ", ";
        sql += APISection.ID_CATEGORIE_SECTION_PAIEMENT_PRINCIPAL + ", ";
        sql += "'" + new BSpy(transaction.getSession()).getFullData() + "'";

        sql += ")";

        return sql;
    }

    public String getTypeAdresse() {
        return typeAdresse;
    }

    public void setDateCreation(String creationDate) {
        dateCreation = creationDate;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontantBase(String montantBase) {
        this.montantBase = montantBase;
    }

    public void setMontantPaiment(String montantPaiment) {
        this.montantPaiment = montantPaiment;
    }

    public void setTypeAdresse(String typeAdresse) {
        this.typeAdresse = typeAdresse;
    }
}
