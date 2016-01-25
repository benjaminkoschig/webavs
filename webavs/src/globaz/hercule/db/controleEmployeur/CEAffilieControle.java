package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hercule.db.couverture.CECouverture;
import globaz.naos.db.affiliation.AFAffiliation;

/**
 * @author SCO
 * @since SCO 9 juin 2010
 */
public class CEAffilieControle extends BEntity {

    private static final long serialVersionUID = -3680409606076970773L;
    public static final String FIELD_IDAFFILIE = "MAIAFF";
    public static final String FIELD_IDTIERS = "HTITIE";
    public static final String FIELD_NUMERO_AFFILIE = "MALNAF";

    private String anneCouverture;
    private String collaboration = "";
    private String criteresEntreprise = "";
    private String dateDebutAffiliation = "";
    private String dateFinAffiliation = "";

    // Fields sur table des controles
    private String dateFinControle;
    private String derniereRevision = "";
    private String idAffilie = "";
    // Fields sur table des notes
    private String idAttributionPts = "";
    // Fields sur la table des couvertures
    private String idCouverture;
    private String idTiers = "";
    private Boolean isModificationUtilisateur = new Boolean(false);

    private String nbrePoints = "";

    private String numeroAffilie = "";
    // Fields sur la table AFPARTP
    private String particulariteDerogation;

    private String qualiteRH = "";

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return null;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idTiers = statement.dbReadNumeric(FIELD_IDTIERS);
        idAffilie = statement.dbReadNumeric(FIELD_IDAFFILIE);
        numeroAffilie = statement.dbReadString(FIELD_NUMERO_AFFILIE);
        dateDebutAffiliation = statement.dbReadDateAMJ(AFAffiliation.FIELDNAME_AFF_DDEBUT);
        dateFinAffiliation = statement.dbReadDateAMJ(AFAffiliation.FIELDNAME_AFF_DFIN);

        // Fields sur la table des notes
        idAttributionPts = statement.dbReadNumeric("MPAPID");
        derniereRevision = statement.dbReadNumeric("MPDREV");
        qualiteRH = statement.dbReadNumeric("MPQURH");
        collaboration = statement.dbReadNumeric("MPCOLL");
        criteresEntreprise = statement.dbReadNumeric("MPCREN");
        isModificationUtilisateur = statement.dbReadBoolean(CEAttributionPts.FIELD_MODIFICATION_UTILISATEUR);
        nbrePoints = statement.dbReadNumeric("MPNBPT");

        // Fields sur la table des controles
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");

        // Fields sur la table des couvertures
        anneCouverture = statement.dbReadString(CECouverture.FIELD_ANNEE);
        idCouverture = statement.dbReadNumeric(CECouverture.FIELD_IDCOUVERTURE);

        // Fields sur la table AFPARTP
        particulariteDerogation = statement.dbReadNumeric("MFMNUM");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getAnneCouverture() {
        return anneCouverture;
    }

    public String getCollaboration() {
        return collaboration;
    }

    public String getCriteresEntreprise() {
        return criteresEntreprise;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinControle() {
        return dateFinControle;
    }

    public String getDerniereRevision() {
        return derniereRevision;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getIdAttributionPts() {
        return idAttributionPts;
    }

    public String getIdCouverture() {
        return idCouverture;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNbrePoints() {
        return nbrePoints;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getParticulariteDerogation() {
        return particulariteDerogation;
    }

    public String getQualiteRH() {
        return qualiteRH;
    }

    public Boolean isModificationUtilisateur() {
        return isModificationUtilisateur;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAnneCouverture(String anneCouverture) {
        this.anneCouverture = anneCouverture;
    }

    public void setCollaboration(String collaboration) {
        this.collaboration = collaboration;
    }

    public void setCriteresEntreprise(String criteresEntreprise) {
        this.criteresEntreprise = criteresEntreprise;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setDerniereRevision(String derniereRevision) {
        this.derniereRevision = derniereRevision;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setIdAttributionPts(String idAttributionPts) {
        this.idAttributionPts = idAttributionPts;
    }

    public void setIdCouverture(String idCouverture) {
        this.idCouverture = idCouverture;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsModificationUtilisateur(Boolean isModificationUtilisateur) {
        this.isModificationUtilisateur = isModificationUtilisateur;
    }

    public void setNbrePoints(String nbrePoints) {
        this.nbrePoints = nbrePoints;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setParticulariteDerogation(String particulariteDerogation) {
        this.particulariteDerogation = particulariteDerogation;
    }

    public void setQualiteRH(String qualiteRH) {
        this.qualiteRH = qualiteRH;
    }

}
