package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.pavo.util.CIUtil;

public class CIListeInscriptionActif extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String dateNaissance = "";
    private String montant = "";
    private String nom = "";
    // Autres champs nécessaires
    private String numAvs = "";
    private String pays = "";
    private String sexe = "";

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAvs = statement.dbReadString("KANAVS");
        nom = statement.dbReadString("KALNOM");
        dateNaissance = CIUtil.formatDate(statement.dbReadNumeric("KADNAI"));
        sexe = statement.dbReadNumeric("KATSEX");
        pays = statement.dbReadString("KAIPAY");
        annee = statement.dbReadString("KBNANN");
        montant = statement.dbReadString("KBMMON");

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

    public final String createFromClause(String schema) {
        StringBuffer fromClauseBuffer = new StringBuffer();
        String on = " ON ";
        String point = ".";
        String egal = "=";

        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIINDIP");

        // jointure entre table des demandes et table des tiers
        fromClauseBuffer.append(" JOIN ");
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIECRIP");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIINDIP.KAIIND");
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIECRIP.KAIIND");

        fromClauseBuffer.append(" JOIN ");
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIRAOUP");
        fromClauseBuffer.append(on);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIINDIP.KAIIND");
        fromClauseBuffer.append(egal);
        fromClauseBuffer.append(_getCollection());
        fromClauseBuffer.append("CIRAOUP.KAIIND");

        return fromClauseBuffer.toString();
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getMontant() {
        return montant;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getPays() {
        return pays;
    }

    public String getSexe() {
        return sexe;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
