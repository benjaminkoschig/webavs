package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SCO
 * @since 7 sept. 2010
 */
public class CEGestionAttributionPts extends BEntity {

    private static final long serialVersionUID = -871607975687718855L;
    private String anneeCouverture = "";
    private String collaboration = "";
    private String collaborationCom = "";
    private String commentaires = "";
    private String criteresEntreprise = "";
    private String criteresEntrepriseCom = "";
    private String dateDebutAffiliation = "";
    private String dateDebutControle = "";
    private String dateEffective = "";
    private String dateFinAffiliation = "";
    private String dateFinControle = "";
    private String derniereRevision = "";
    private String derniereRevisionCom = "";
    private String genreControle = "";
    private String idAttributionPts = "";
    private String idControle = "";
    private String idTiers = "";
    private Boolean isAttributionActive = new Boolean(false);
    private Boolean isModificationUtilisateur = new Boolean(false);
    private String lastModification = "";
    private String lastUser = "";
    private String nbrePoints = "";
    private String nom = "";
    private String numAffilie = "";
    private String numAffilieExterne = "";
    private String brancheEconomique = "";
    private String observations = "";
    private String periodeDebut = "";
    private String periodeFin = "";
    private String qualiteRH = "";
    private String qualiteRHCom = "";
    private String codeNOGA = "";
    private String tempsJour = "";
    private String nomReviseur = "";
    private String codeSuva = "";
    private String libelleSuva;
    private String typeReviseur;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        idAttributionPts = statement.dbReadNumeric("MPAPID");
        numAffilie = statement.dbReadString("MALNAF");
        derniereRevision = statement.dbReadNumeric("MPDREV");
        derniereRevisionCom = statement.dbReadString("MPDREC");
        qualiteRH = statement.dbReadNumeric("MPQURH");
        qualiteRHCom = statement.dbReadString("MPQURC");
        collaboration = statement.dbReadNumeric("MPCOLL");
        collaborationCom = statement.dbReadString("MPCOLC");
        criteresEntreprise = statement.dbReadNumeric("MPCREN");
        criteresEntrepriseCom = statement.dbReadString("MPCREC");
        commentaires = statement.dbReadString("MPCOMM");
        isAttributionActive = statement.dbReadBoolean("CEBAAV");
        nbrePoints = statement.dbReadNumeric("MPNBPT");
        lastUser = statement.dbReadString("MPLUSR");
        lastModification = statement.dbReadString("MPLMOD");
        periodeDebut = statement.dbReadDateAMJ("MPPEDE");
        periodeFin = statement.dbReadDateAMJ("MPPEFI");
        observations = statement.dbReadString("MPOBSE");
        isModificationUtilisateur = statement.dbReadBoolean("CEBMUT");
        _setSpy(statement.dbReadSpy());

        // table de tiers TITIERP
        nom = statement.dbReadString(CEAffilieManager.F_TIER_NOM) + " "
                + statement.dbReadString(CEAffilieManager.F_TIER_COMPLEMENTNOM);

        // Table affilitaiton AFAFFIP
        dateDebutAffiliation = statement.dbReadDateAMJ("MADDEB");
        dateFinAffiliation = statement.dbReadDateAMJ("MADFIN");
        idTiers = statement.dbReadString("HTITIE");
        brancheEconomique = statement.dbReadNumeric("MATBRA");
        codeNOGA = statement.dbReadNumeric("MATCDN");
        numAffilieExterne = statement.dbReadString("MDLNAF");

        // Table controle employeur CECONTP
        dateEffective = statement.dbReadDateAMJ("MDDEFF");
        genreControle = statement.dbReadNumeric("MDTGEN");
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE");
        dateFinControle = statement.dbReadDateAMJ("MDDCFI");
        idControle = statement.dbReadNumeric("MDICON");
        tempsJour = statement.dbReadNumeric("MDNTJO");

        // Table des couvertures CECOUVP
        anneeCouverture = statement.dbReadNumeric("CENANE");

        // Table des reviseurs
        nomReviseur = statement.dbReadString("MILNOM");
        typeReviseur = statement.dbReadNumeric("MITTYR");

        // Code suva
        codeSuva = statement.dbReadString("CODESUVA");
        libelleSuva = statement.dbReadString("LIBELLESUVA");
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey("MPAPID", this._dbWriteNumeric(statement.getTransaction(), getIdAttributionPts(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getAnneeCouverture() {
        return anneeCouverture;
    }

    public String getCollaboration() {
        return collaboration;
    }

    public String getCollaborationCom() {
        return collaborationCom;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public String getCriteresEntreprise() {
        return criteresEntreprise;
    }

    public String getCriteresEntrepriseCom() {
        return criteresEntrepriseCom;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutControle() {
        return dateDebutControle;
    }

    public String getDateEffective() {
        return dateEffective;
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

    public String getDerniereRevisionCom() {
        return derniereRevisionCom;
    }

    public String getGenreControle() {
        return genreControle;
    }

    public String getIdAttributionPts() {
        return idAttributionPts;
    }

    public String getIdControle() {
        return idControle;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsAttributionActive() {
        return isAttributionActive;
    }

    public Boolean getIsModificationUtilisateur() {
        return isModificationUtilisateur;
    }

    public String getLastModification() {
        return lastModification;
    }

    public String getLastUser() {
        return lastUser;
    }

    public String getNbrePoints() {
        return nbrePoints;
    }

    public String getNom() {
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getObservations() {
        return observations;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getQualiteRH() {
        return qualiteRH;
    }

    public String getQualiteRHCom() {
        return qualiteRHCom;
    }

    public boolean isAttributionActive() {
        return isAttributionActive.booleanValue();
    }

    public Boolean isModificationUtilisateur() {
        return isModificationUtilisateur;
    }

    public void setAnneeCouverture(final String anneeCouverture) {
        this.anneeCouverture = anneeCouverture;
    }

    public void setCollaboration(final String collaboration) {
        this.collaboration = collaboration;
    }

    public void setCollaborationCom(final String collaborationCom) {
        this.collaborationCom = collaborationCom;
    }

    public void setCommentaires(final String commentaires) {
        this.commentaires = commentaires;
    }

    public void setCriteresEntreprise(final String criteresEntreprise) {
        this.criteresEntreprise = criteresEntreprise;
    }

    public void setCriteresEntrepriseCom(final String criteresEntrepriseCom) {
        this.criteresEntrepriseCom = criteresEntrepriseCom;
    }

    public void setDateDebutAffiliation(final String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutControle(final String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(final String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateFinAffiliation(final String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinControle(final String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setDerniereRevision(final String derniereRevision) {
        this.derniereRevision = derniereRevision;
    }

    public void setDerniereRevisionCom(final String derniereRevisionCom) {
        this.derniereRevisionCom = derniereRevisionCom;
    }

    public void setGenreControle(final String genreControle) {
        this.genreControle = genreControle;
    }

    public void setIdAttributionPts(final String idAttributionPts) {
        this.idAttributionPts = idAttributionPts;
    }

    public void setIdControle(final String idControle) {
        this.idControle = idControle;
    }

    public void setIdTiers(final String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsAttributionActive(final Boolean isAttributionActive) {
        this.isAttributionActive = isAttributionActive;
    }

    public void setIsModificationUtilisateur(final Boolean isModificationUtilisateur) {
        this.isModificationUtilisateur = isModificationUtilisateur;
    }

    public void setLastModification(final String lastModification) {
        this.lastModification = lastModification;
    }

    public void setLastUser(final String lastUser) {
        this.lastUser = lastUser;
    }

    public void setNbrePoints(final String nbrePoints) {
        this.nbrePoints = nbrePoints;
    }

    public void setNom(final String nom) {
        this.nom = nom;
    }

    public void setNumAffilie(final String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setObservations(final String observations) {
        this.observations = observations;
    }

    public void setPeriodeDebut(final String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(final String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setQualiteRH(final String qualiteRH) {
        this.qualiteRH = qualiteRH;
    }

    public void setQualiteRHCom(final String qualiteRHCom) {
        this.qualiteRHCom = qualiteRHCom;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public void setBrancheEconomique(final String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    /**
     * Getter de codeNOGA
     * 
     * @return the codeNOGA
     */
    public String getCodeNOGA() {
        return codeNOGA;
    }

    /**
     * Setter de codeNOGA
     * 
     * @param codeNOGA the codeNOGA to set
     */
    public void setCodeNOGA(final String codeNOGA) {
        this.codeNOGA = codeNOGA;
    }

    /**
     * Getter de numAffilieExterne
     * 
     * @return the numAffilieExterne
     */
    public String getNumAffilieExterne() {
        return numAffilieExterne;
    }

    /**
     * Setter de numAffilieExterne
     * 
     * @param numAffilieExterne the numAffilieExterne to set
     */
    public void setNumAffilieExterne(final String numAffilieExterne) {
        this.numAffilieExterne = numAffilieExterne;
    }

    /**
     * Getter de tempsJour
     * 
     * @return the tempsJour
     */
    public String getTempsJour() {
        return tempsJour;
    }

    /**
     * Setter de tempsJour
     * 
     * @param tempsJour the tempsJour to set
     */
    public void setTempsJour(final String tempsJour) {
        this.tempsJour = tempsJour;
    }

    /**
     * Getter de nomReviseur
     * 
     * @return the nomReviseur
     */
    public String getNomReviseur() {
        return nomReviseur;
    }

    /**
     * Setter de nomReviseur
     * 
     * @param nomReviseur the nomReviseur to set
     */
    public void setNomReviseur(final String nomReviseur) {
        this.nomReviseur = nomReviseur;
    }

    /**
     * Getter de codeSuva
     * 
     * @return the codeSuva
     */
    public String getCodeSuva() {
        return codeSuva;
    }

    /**
     * Setter de codeSuva
     * 
     * @param codeSuva the codeSuva to set
     */
    public void setCodeSuva(final String codeSuva) {
        this.codeSuva = codeSuva;
    }

    /**
     * Getter de libelleSuva
     * 
     * @return the libelleSuva
     */
    public String getLibelleSuva() {
        return libelleSuva;
    }

    /**
     * Setter de libelleSuva
     * 
     * @param libelleSuva the libelleSuva to set
     */
    public void setLibelleSuva(final String libelleSuva) {
        this.libelleSuva = libelleSuva;
    }

    /**
     * @return the typeReviseur
     */
    public String getTypeReviseur() {
        return typeReviseur;
    }

    /**
     * @param typeReviseur the typeReviseur to set
     */
    public void setTypeReviseur(String typeReviseur) {
        this.typeReviseur = typeReviseur;
    }
}
