package globaz.naos.db.controleLpp;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

/**
 * 
 * @author sco
 * @since 08 sept. 2011
 */
public class AFAffilieSoumiLpp extends BEntity {

    private static final long serialVersionUID = -3477643777920963976L;
    private String dateFinAffiliation;
    private String dateFinSuivi;
    private String dateNaissance;
    private String genreEcriture;
    private String idAffilie;
    private String idCompteIndividuel;
    private String idTiers;
    private boolean isExtourne = false;
    private String moisDebut;
    private String moisFin;
    private String montant;
    private String nivSecuAffilie;
    private String nivSecuCI;
    private String nom;
    private String nss;
    private String numeroAffilie;
    private String sexe;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffilie = statement.dbReadNumeric("MAIAFF");
        numeroAffilie = statement.dbReadString("MALNAF");
        nivSecuAffilie = statement.dbReadNumeric("MATSEC");
        nom = statement.dbReadString("KALNOM");
        sexe = statement.dbReadString("KATSEX");
        montant = statement.dbReadString("KBMMON");
        nss = statement.dbReadString("KANAVS");
        dateNaissance = statement.dbReadDateAMJ("KADNAI");
        moisDebut = statement.dbReadString("KBNMOD");
        moisFin = statement.dbReadString("KBNMOF");
        genreEcriture = statement.dbReadString("KBTGEN");
        dateFinSuivi = statement.dbReadString("MYDFIN");
        dateFinAffiliation = statement.dbReadString("MADFIN");
        idCompteIndividuel = statement.dbReadNumeric("KAIIND");
        nivSecuCI = statement.dbReadNumeric("KATSEC");
        if (!JadeStringUtil.isBlankOrZero(statement.dbReadNumeric("KBTEXT"))) {
            isExtourne = true;
        }
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

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinSuivi() {
        return dateFinSuivi;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getGenreEcriture() {
        return genreEcriture;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdCompteIndividuel() {
        return idCompteIndividuel;
    }

    public String getIdTiers() {
        return idTiers;
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

    public String getNivSecuAffilie() {
        return nivSecuAffilie;
    }

    public String getNivSecuCI() {
        return nivSecuCI;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getSexe() {
        return sexe;
    }

    public boolean isExtourne() {
        return isExtourne;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinSuivi(String dateFinSuivi) {
        this.dateFinSuivi = dateFinSuivi;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setExtourne(boolean isExtourne) {
        this.isExtourne = isExtourne;
    }

    public void setGenreEcriture(String genreEcriture) {
        this.genreEcriture = genreEcriture;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdCompteIndividuel(String idCompteIndividuel) {
        this.idCompteIndividuel = idCompteIndividuel;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
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

    public void setNivSecuAffilie(String nivSecuAffilie) {
        this.nivSecuAffilie = nivSecuAffilie;
    }

    public void setNivSecuCI(String nivSecuCI) {
        this.nivSecuCI = nivSecuCI;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
