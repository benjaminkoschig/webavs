package globaz.naos.process.nbrAssures;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Entité représentant le nombre d'assurés pour un affilé, une assurance et une année donnée
 */
public class AFNbrAssures extends BEntity {

    private static final long serialVersionUID = 4035380562739898817L;

    private String numAffilie = "";
    private String nomTiers = "";
    private String prenomTiers = "";
    private String libelleAssurance = "";
    private String annee = "";
    private String nbrAssures = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString("MALNAF");
        nomTiers = statement.dbReadString("HTLDE1");
        prenomTiers = statement.dbReadString("HTLDE2");
        libelleAssurance = statement.dbReadString("LIBELLE_ASS");
        annee = statement.dbReadNumeric("MVNANN");
        nbrAssures = statement.dbReadNumeric("MVNNBR");
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

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @param numAffilie the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @return the nomTiers
     */
    public String getNomTiers() {
        return nomTiers;
    }

    /**
     * @param nomTiers the nomTiers to set
     */
    public void setNomTiers(String nomTiers) {
        this.nomTiers = nomTiers;
    }

    /**
     * @return the prenomTiers
     */
    public String getPrenomTiers() {
        return prenomTiers;
    }

    /**
     * @param prenomTiers the prenomTiers to set
     */
    public void setPrenomTiers(String prenomTiers) {
        this.prenomTiers = prenomTiers;
    }

    /**
     * @return the libelleAssurance
     */
    public String getLibelleAssurance() {
        return libelleAssurance;
    }

    /**
     * @param libelleAssurance the libelleAssurance to set
     */
    public void setLibelleAssurance(String libelleAssurance) {
        this.libelleAssurance = libelleAssurance;
    }

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @return the nbrAssures
     */
    public String getNbrAssures() {
        return nbrAssures;
    }

    /**
     * @param nbrAssures the nbrAssures to set
     */
    public void setNbrAssures(String nbrAssures) {
        this.nbrAssures = nbrAssures;
    }

}
