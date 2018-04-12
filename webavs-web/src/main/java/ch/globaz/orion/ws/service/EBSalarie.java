package ch.globaz.orion.ws.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class EBSalarie extends BEntity {
    private static final long serialVersionUID = 35891211217588599L;
    private String nss;
    private String nomPrenom;
    private String dateNaissance;
    private Integer sexeCs;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    protected String _getTableName() {
        // not implemented
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nss = statement.dbReadString("nss");
        nomPrenom = statement.dbReadString("nom_Prenom");
        dateNaissance = statement.dbReadDateAMJ("date_Naissance");
        sexeCs = Integer.valueOf(statement.dbReadNumeric("sexe"));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // not implemented
    }

    public Integer getSexeCs() {
        return sexeCs;
    }

    public void setSexeCs(Integer sexeCs) {
        this.sexeCs = sexeCs;
    }

}
