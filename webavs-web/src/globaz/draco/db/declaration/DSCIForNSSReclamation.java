package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class DSCIForNSSReclamation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee;
    private String extourne;
    private String moisDebut;
    private String moisFin;
    private String montant;
    private String nomPrenom;
    private String numeroAvs;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numeroAvs = statement.dbReadString("KANAVS");
        nomPrenom = statement.dbReadString("KALNOM");
        montant = statement.dbReadNumeric("KBMMON", 2);
        moisDebut = statement.dbReadNumeric("KBNMOD");
        moisFin = statement.dbReadNumeric("KBNMOF");
        extourne = statement.dbReadNumeric("KBTEXT");
        annee = statement.dbReadNumeric("TENANN");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getExtourne() {
        return extourne;
    }

    public String getMoisDebut() {
        return moisDebut;
    }

    public String getMoisFin() {
        return moisFin;
    }

    public String getMontant() {
        return montant;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumeroAvs() {
        return numeroAvs;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setExtourne(String extourne) {
        this.extourne = extourne;
    }

    public void setMoisDebut(String moisDebut) {
        this.moisDebut = moisDebut;
    }

    public void setMoisFin(String moisFin) {
        this.moisFin = moisFin;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setNumeroAvs(String numeroAvs) {
        this.numeroAvs = numeroAvs;
    }

}
