package globaz.hercule.db.controleEmployeur;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hercule.service.CEControleEmployeurService;
import globaz.hercule.utils.CEUtils;
import globaz.jade.log.JadeLogger;

/**
 * @author SCO
 * @since 18 oct. 2010
 */
public class CEListeControleEmployeur extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String changeType;
    private String dateDebutAffiliation;
    private String dateDebutControle;
    private String dateEffective;
    private String dateFinAffiliation;
    private String dateFinControle;
    private String datePrevue;
    private String designation1;
    private String designation2;
    private String genreControle;
    private String idAffiliation;
    private String idControleEmployeur;
    private String idTiers;
    private Boolean isControleActif = new Boolean(false);
    private String numAffilie;
    private String numNouveauRapport;
    private String visaResiveur;

    public String _getNomAffilie() {
        return CEUtils.formatNomTiers(designation1, designation2);
    }

    /**
     * Permet de récupérer le prochain controle
     * 
     * @return
     */
    public String _getProchainControle() {
        String prochainControle = null;

        try {
            prochainControle = CEControleEmployeurService.findAnneeCouvertureActiveByNumAffilie(getSession(),
                    getNumAffilie());
        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve annee couverture for affilie : " + getNumAffilie(), e);
        }

        return prochainControle;
    }

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
        idAffiliation = statement.dbReadNumeric("MAIAFF"); // Id affiliation
        numAffilie = statement.dbReadString("MALNAF"); // Numéro d'affilié
        dateDebutAffiliation = statement.dbReadString("MADDEB"); // Debut
        // d'affiliation
        dateFinAffiliation = statement.dbReadString("MADFIN"); // Fin
        // d'affiliation
        idTiers = statement.dbReadNumeric("HTITIE"); // Id tiers
        designation1 = statement.dbReadString("HTLDE1"); // Description 1 du
        // tiers
        designation2 = statement.dbReadString("HTLDE2"); // Description 2 du
        // tiers
        idControleEmployeur = statement.dbReadNumeric("MDICON"); // id du
        // controle
        dateEffective = statement.dbReadDateAMJ("MDDEFF"); // DAte effective
        dateDebutControle = statement.dbReadDateAMJ("MDDCDE"); // Date debut
        // controle
        dateFinControle = statement.dbReadDateAMJ("MDDCFI"); // Date fin de
        // controle
        datePrevue = statement.dbReadDateAMJ("MDDPRE"); // Date prévu
        numNouveauRapport = statement.dbReadString("MDLNRA"); // Nouveau num
        // rapport
        genreControle = statement.dbReadNumeric("MDTGEN"); // Genre de controle
        visaResiveur = statement.dbReadString("MILVIS"); // Nom réviseur
        isControleActif = statement.dbReadBoolean("MDBFDR"); // Controle actif
        changeType = statement.dbReadNumeric("CHANGETYPE");
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
        statement.writeKey("MDICON", this._dbWriteNumeric(statement.getTransaction(), getIdControleEmployeur(), ""));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getChangeType() {
        return changeType;
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

    public String getDatePrevue() {
        return datePrevue;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getGenreControle() {
        return genreControle;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdControleEmployeur() {
        return idControleEmployeur;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsControleActif() {
        return isControleActif;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumNouveauRapport() {
        return numNouveauRapport;
    }

    public String getVisaResiveur() {
        return visaResiveur;
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public boolean isControleActif() {
        return isControleActif.booleanValue();
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutControle(String dateDebutControle) {
        this.dateDebutControle = dateDebutControle;
    }

    public void setDateEffective(String dateEffective) {
        this.dateEffective = dateEffective;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinControle(String dateFinControle) {
        this.dateFinControle = dateFinControle;
    }

    public void setDatePrevue(String datePrevue) {
        this.datePrevue = datePrevue;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public void setGenreControle(String genreControle) {
        this.genreControle = genreControle;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdControleEmployeur(String idControleEmployeur) {
        this.idControleEmployeur = idControleEmployeur;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsControleActif(Boolean isControleActif) {
        this.isControleActif = isControleActif;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumNouveauRapport(String numNouveauRapport) {
        this.numNouveauRapport = numNouveauRapport;
    }

    public void setVisaResiveur(String visaResiveur) {
        this.visaResiveur = visaResiveur;
    }

}
