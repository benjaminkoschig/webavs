package globaz.osiris.db.rentes.operation;

import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import java.sql.SQLException;

public abstract class CARentesOperation {

    private String date;

    private String idCompteAnnexe = "0";
    private String idCompteCourant = "0";
    private String idJournal;
    private String idOperation = "0";
    private String idOrdreVersement = "0";
    private String idRubrique = "0";
    private String idSection = "0";
    protected BPreparedStatement insert;
    private String libelle = "";

    private String montant;

    /**
     * Initialise le Prepared Statement servant insert.
     */
    public CARentesOperation(BTransaction transaction) throws Exception {
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
     * Remplit les variables à insérer en base de données.
     * 
     * @throws Exception
     */
    public abstract void fillSpecialVariables(int index) throws Exception;

    /**
     * Efface puis remplit les variables à insérer en base de données.
     * 
     * @throws Exception
     */
    public void fillVariables() throws Exception {
        insert.clearParameters();

        insert.setInt(1, JadeStringUtil.parseInt(getIdOperation(), 0));
        insert.setInt(2, JadeStringUtil.parseInt(getIdCompteAnnexe(), 0));
        insert.setInt(3, JadeStringUtil.parseInt(getIdSection(), 0));
        insert.setInt(4, JadeStringUtil.parseInt(getIdCompteCourant(), 0));
        insert.setInt(5, JadeStringUtil.parseInt(getIdJournal(), 0));
        insert.setInt(6, JadeStringUtil.parseInt(getDate(), 0));

        fillSpecialVariables(7);
    }

    public String getDate() {
        return date;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdOperation() {
        return idOperation;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdSection() {
        return idSection;
    }

    protected abstract String getIdTypeOperation();

    public BPreparedStatement getInsert() {
        return insert;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMontant() {
        return montant;
    }

    /**
     * Contruit la requête INSERT.
     * 
     * @param transaction
     * @return
     */
    private String getPreparedSqlQuery(BTransaction transaction) {
        String sql = "INSERT INTO " + Jade.getInstance().getDefaultJdbcSchema() + "." + CAOperation.TABLE_CAOPERP
                + " (";

        sql += CAOperation.FIELD_IDOPERATION + ", ";
        sql += CAOperation.FIELD_IDCOMPTEANNEXE + ", ";
        sql += CAOperation.FIELD_IDSECTION + ", ";
        sql += CAOperation.FIELD_IDCOMPTECOURANT + ", ";
        sql += CAOperation.FIELD_IDJOURNAL + ", ";
        sql += CAOperation.FIELD_DATE + ", ";

        sql += CAOperation.FIELD_CODEMASTER + ", ";
        sql += CAOperation.FIELD_IDLOG + ", ";
        sql += CAOperation.FIELD_ETAT + ", ";
        sql += CAOperation.FIELD_IDTYPEOPERATION + ", ";
        sql += BSpy.FIELDNAME + ", ";

        sql += getPreparedSqlQuerySpecialFields(transaction);

        sql += ") VALUES (";

        sql += "?, ?, ?, ?, ?, ?, ";
        sql += APIOperation.SINGLE + ", 0, ";
        sql += APIOperation.ETAT_PROVISOIRE + ", ";
        sql += getIdTypeOperation() + ",";
        sql += "'" + new BSpy(transaction.getSession()).getFullData() + "' ,";

        sql += getPreparedSqlQuerySpecialValues(transaction);

        sql += ")";

        return sql;
    }

    /**
     * Return les colonnes à setter pour le type d'opérations.
     * 
     * @param transaction
     * @return
     */
    protected abstract String getPreparedSqlQuerySpecialFields(BTransaction transaction);

    /**
     * Return les valeurs pour les colonnes à setter pour le type d'opérations.
     * 
     * @param transaction
     * @return
     */
    protected abstract String getPreparedSqlQuerySpecialValues(BTransaction transaction);

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdCompteCourant(String idCompteCourant) {
        this.idCompteCourant = idCompteCourant;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdOperation(String idOperation) {
        this.idOperation = idOperation;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setInsert(BPreparedStatement insert) {
        this.insert = insert;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
