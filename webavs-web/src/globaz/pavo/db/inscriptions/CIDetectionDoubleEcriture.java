package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.pavo.db.compte.CICompteIndividuel;

public class CIDetectionDoubleEcriture extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String compteIndividuelId = "";

    String dateNaissance = "";
    String nomPrenom = "";
    String nss = "";
    String sexe = "";

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        CICompteIndividuel ci = new CICompteIndividuel();
        ci.setSession(getSession());
        ci.setCompteIndividuelId(compteIndividuelId);
        ci.retrieve();
        nss = ci.getNumeroAvs();
        nomPrenom = ci.getNomPrenom();
        dateNaissance = ci.getDateNaissance();
        sexe = ci.getSexe();
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return "CIINDIP";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compteIndividuelId = statement.dbReadString("KAIIND");
        // nss = statement.dbReadString("KANAVS");
        // nomPrenom = statement.dbReadString("KALNOM");
        // dateNaissance = statement.dbReadDateAMJ("KADNAI");
        // sexe = statement.dbReadNumeric("KATSEX");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public String getCompteIndividuelId() {
        return compteIndividuelId;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNss() {
        return nss;
    }

    public String getSexe() {
        return sexe;
    }

    public void setCompteIndividuelId(String compteIndividuelId) {
        this.compteIndividuelId = compteIndividuelId;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
