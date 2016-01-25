package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFLineCi extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String genreEcriture;
    private String idCompteIndividuel;
    private String moisDebut;
    private String moisFin;
    private String montant;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        moisDebut = statement.dbReadString("KBNMOD");
        moisFin = statement.dbReadString("KBNMOF");
        genreEcriture = statement.dbReadString("KBTGEN");
        idCompteIndividuel = statement.dbReadNumeric("KAIIND");
        montant = statement.dbReadString("KBMMON");
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

    public String getGenreEcriture() {
        return genreEcriture;
    }

    public String getIdCompteIndividuel() {
        return idCompteIndividuel;
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

    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    public void setIdCompteIndividuel(String idCompteIndividuel) {
        this.idCompteIndividuel = idCompteIndividuel;
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

}
