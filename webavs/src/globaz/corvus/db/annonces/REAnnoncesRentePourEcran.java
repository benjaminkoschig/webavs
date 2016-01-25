package globaz.corvus.db.annonces;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.db.tiers.ITITiersDefTable;

public class REAnnoncesRentePourEcran extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeApplication;
    private String codePrestation;
    private String csEtatAnnonce;
    private String csTraitement;
    private String dateDebutDroit;
    private String dateFinDroit;
    private String idAnnonce;
    private String moisRapport;
    private String montant;
    private PRTiersWrapper tiers;

    @Override
    protected String _getTableName() {
        return REAnnonceHeader.TABLE_NAME_ANNONCE_HEADER;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        codeApplication = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_CODE_APPLICATION);
        codePrestation = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_GENRE_PRESTATION);
        csEtatAnnonce = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_ETAT);
        csTraitement = statement.dbReadNumeric(REAnnonceRente.FIELDNAME_CS_TRAITEMENT);
        dateDebutDroit = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_DEBUT_DROIT);
        dateFinDroit = statement.dbReadString(REAnnoncesAbstractLevel1A.FIELDNAME_FIN_DROIT);
        idAnnonce = statement.dbReadNumeric(REAnnonceHeader.FIELDNAME_ID_ANNONCE);
        moisRapport = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_MOIS_RAPPORT);
        montant = statement.dbReadNumeric(REAnnoncesAbstractLevel1A.FIELDNAME_MENSUALITE_PRESTATIONS_FR);
        tiers = PRTiersHelper.getTiersParId(getSession(), statement.dbReadNumeric(ITITiersDefTable.ID_TIERS));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(REAnnonceHeader.FIELDNAME_ID_ANNONCE,
                this._dbWriteNumeric(statement.getTransaction(), idAnnonce, "idAnnonce"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("Entité de visualisation, impossible de sauver en base de données");
    }

    public String getCodeApplication() {
        return codeApplication;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getCsEtatAnnonce() {
        return csEtatAnnonce;
    }

    public String getCsTraitement() {
        return csTraitement;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public String getMontant() {
        return montant;
    }

    public PRTiersWrapper getTiers() {
        return tiers;
    }

    public void setCodeApplication(String codeApplication) {
        this.codeApplication = codeApplication;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setCsEtatAnnonce(String csEtatAnnonce) {
        this.csEtatAnnonce = csEtatAnnonce;
    }

    public void setCsTraitement(String csTraitement) {
        this.csTraitement = csTraitement;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setTiers(PRTiersWrapper tiers) {
        this.tiers = tiers;
    }
}
