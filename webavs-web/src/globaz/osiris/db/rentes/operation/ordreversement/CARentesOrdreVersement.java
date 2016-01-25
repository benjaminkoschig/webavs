package globaz.osiris.db.rentes.operation.ordreversement;

import globaz.globall.db.BConstants;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.ordres.CAOrdreVersement;
import java.sql.SQLException;

public class CARentesOrdreVersement {

    private String idAdressePaiement;

    private String idOrdre;
    private String idOrdreGroupe;
    private String idOrganeExecution;
    protected BPreparedStatement insert;
    private String motif;
    private String natureOrdre;
    private String nomCache;
    private String numeroTransaction;

    /**
     * Initialise le Prepared Statement servant insert.
     */
    public CARentesOrdreVersement(BTransaction transaction) throws Exception {
        insert = new BPreparedStatement(transaction);
        insert.prepareStatement(getPreparedSqlQuery(transaction));
    }

    /**
     * Termine le prepare statement.
     * 
     * @throws Exception
     */
    public void closeQuery() throws Exception {
        insert.closePreparedStatement();
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

        insert.setInt(1, JadeStringUtil.parseInt(getIdAdressePaiement(), 0));
        insert.setInt(2, JadeStringUtil.parseInt(getIdOrdre(), 0));
        insert.setInt(3, JadeStringUtil.parseInt(getIdOrdreGroupe(), 0));
        insert.setInt(4, JadeStringUtil.parseInt(getIdOrganeExecution(), 0));
        insert.setString(5, getMotif());
        insert.setString(6, getNomCache());
        insert.setInt(7, JadeStringUtil.parseInt(getNumeroTransaction(), 0));
        insert.setInt(8, JadeStringUtil.parseInt(getNatureOrdre(), 0));
    }

    public String getIdAdressePaiement() {
        return idAdressePaiement;
    }

    public String getIdOrdre() {
        return idOrdre;
    }

    public String getIdOrdreGroupe() {
        return idOrdreGroupe;
    }

    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    public BPreparedStatement getInsert() {
        return insert;
    }

    public String getMotif() {
        return motif;
    }

    public String getNatureOrdre() {
        return natureOrdre;
    }

    public String getNomCache() {
        return nomCache;
    }

    public String getNumeroTransaction() {
        return numeroTransaction;
    }

    /**
     * Contruit la requête INSERT.
     * 
     * @param transaction
     * @return
     */
    private String getPreparedSqlQuery(BTransaction transaction) {
        String sql = "INSERT INTO " + Jade.getInstance().getDefaultJdbcSchema() + "." + CAOrdreVersement.TABLE_CAOPOVP
                + " (";

        sql += CAOrdreVersement.FIELD_CODEISOMONBON + ", ";
        sql += CAOrdreVersement.FIELD_CODEISOMONDEP + ", ";
        sql += CAOrdreVersement.FIELD_COURSCONVERSION + ", ";
        sql += CAOrdreVersement.FIELD_VALEURCONVERSION + ", ";
        sql += CAOrdreVersement.FIELD_REFERENCEBVR + ", ";
        sql += CAOrdreVersement.FIELD_IDBANQUE + ", ";
        sql += CAOrdreVersement.FIELD_NOCOMPTE + ", ";
        sql += CAOrdreVersement.FIELD_ESTBLOQUE + ", ";
        sql += CAOrdreVersement.FIELD_ESTRETIRE + ", ";
        sql += CAOrdreVersement.FIELD_TYPEVIREMENT + ", ";
        sql += CAOrdreVersement.FIELD_TYPEORDRE + ", ";
        sql += CAOrdreVersement.FIELD_CODEISOPAYS + ", ";

        sql += CAOrdreVersement.FIELD_IDADRESSEPAIEMENT + ", ";
        sql += CAOrdreVersement.FIELD_IDORDRE + ", ";
        sql += CAOrdreVersement.FIELD_IDORDREGROUPE + ", ";
        sql += CAOrdreVersement.FIELD_IDORGANEEXECUTION + ", ";
        sql += CAOrdreVersement.FIELD_MOTIF + ", ";
        sql += CAOrdreVersement.FIELD_NOMCACHE + ", ";
        sql += CAOrdreVersement.FIELD_NUMTRANSACTION + ", ";
        sql += CAOrdreVersement.FIELD_NATUREORDRE + ", ";
        sql += BSpy.FIELDNAME;
        sql += ") VALUES (";

        sql += "'CHF', 'CHF', 0.00, 0, '', 0, '0', '', ";
        sql += BConstants.DB_BOOLEAN_FALSE_DELIMITED + ", ";
        sql += CAOrdreVersement.VIREMENT + ", ";
        sql += "'" + APIOperation.CAOPERATIONORDREVERSEMENT + "', ";
        sql += "'', ";

        sql += "?, ?, ?, ?, ?, ?, ?, ?, ";
        sql += "'" + new BSpy(transaction.getSession()).getFullData() + "'";
        sql += ")";

        return sql;
    }

    public void setIdAdressePaiement(String idAdressePaiement) {
        this.idAdressePaiement = idAdressePaiement;
    }

    public void setIdOrdre(String idOrdre) {
        this.idOrdre = idOrdre;
    }

    public void setIdOrdreGroupe(String idOrdreGroupe) {
        this.idOrdreGroupe = idOrdreGroupe;
    }

    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    public void setInsert(BPreparedStatement insert) {
        this.insert = insert;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setNatureOrdre(String natureOrdre) {
        this.natureOrdre = natureOrdre;
    }

    public void setNomCache(String nomCache) {
        this.nomCache = nomCache;
    }

    public void setNumeroTransaction(String numTransaction) {
        numeroTransaction = numTransaction;
    }
}
