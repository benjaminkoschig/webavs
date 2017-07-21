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
    private boolean isExtourne;
    private String moisDebut;
    private String moisFin;
    private String montant;
    private String nivSecuAffilie;
    private String nivSecuCI;
    private String nom;
    private String nss;
    private String numeroAffilie;
    private String sexe;
    private String motifSuivi;

    public AFAffilieSoumiLpp() {
        super();
    }

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
        dateFinAffiliation = statement.dbReadString("MADFIN");
        idCompteIndividuel = statement.dbReadNumeric("KAIIND");
        nivSecuCI = statement.dbReadNumeric("KATSEC");
        if (!JadeStringUtil.isBlankOrZero(statement.dbReadNumeric("KBTEXT"))) {
            isExtourne = true;
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not implemented
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not implemented
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

    public String getMotifSuivi() {
        return motifSuivi;
    }

    public void setMotifSuivi(String motifSuivi) {
        this.motifSuivi = motifSuivi;
    }
}
