package globaz.cygnus.db.dossiers;

import globaz.cygnus.db.contributions.RFContributionsAssistanceAI;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.demandes.PRDemande;
import globaz.pyxis.db.tiers.ITIHistoriqueAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.List;

/**
 * <p>
 * Entité servant à l'affichage de l'écran PRF002 (Dossier RFM).<br/>
 * Ajouté dans le mandat InfoRom D0034, car ce mandat ajoute une nouvelle table (contributions d'assistance AI) et une
 * recherche dans cette table était nécessaire pour une information dans l'écran.
 * </p>
 * <p>
 * Cette entité est chargé, lors de la requête lancée par le manager, une fois par contribution d'assistance AI, puis
 * est regroupée par tiers dans la méthode _afterFind du manager. Il ne faut donc pas utiliser cette entité pour faire
 * un {@link #retrieve()}, mais la charger depuis le manager.
 * </p>
 * 
 * @author PBA
 */
public class RFDossierJointTiers extends BEntity implements Comparable<RFDossierJointTiers> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_USERNAME_GESTIONNAIRE = "KUSER";
    public static final String FIELD_VISA_GESTIONNAIRE = "FVISA";
    public static final String TABLE_GESTIONNAIRE = "FWSUSRP";

    private String csCanton;
    private String csEtatDossier;
    private String csNationalite;
    private String csSexe;
    private String dateDebutPeriodeCAAI;
    private String dateDebutPeriodeDossier;
    private String dateDeces;
    private String dateFinPeriodeCAAI;
    private String dateFinPeriodeDossier;
    private String dateNaissance;
    private String idContributionAssistanceAI;
    private String idDemandePrestation;
    private String idDossier;
    private String idGestionnaire;
    private String idTiers;
    private String nom;
    private String nss;
    private List<RFPeriodeCAAIWrapper> periodesCAAI;
    private String prenom;
    private String visaGestionnaire;

    public RFDossierJointTiers() {
        super();

        csCanton = "";
        csEtatDossier = "";
        csNationalite = "";
        csSexe = "";
        dateDebutPeriodeCAAI = "";
        dateDebutPeriodeDossier = "";
        dateDeces = "";
        dateFinPeriodeCAAI = "";
        dateFinPeriodeDossier = "";
        dateNaissance = "";
        idContributionAssistanceAI = "";
        idDemandePrestation = "";
        idDossier = "";
        idTiers = "";
        nom = "";
        idGestionnaire = "";
        nss = "";
        periodesCAAI = null;
        prenom = "";
        visaGestionnaire = "";
    }

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String schema = _getCollection();
        String tableDossier = schema + RFDossier.TABLE_NAME;
        String tableDemandePrestation = schema + PRDemande.TABLE_NAME;
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = schema + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = schema + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableGestionnaire = schema + RFDossierJointTiers.TABLE_GESTIONNAIRE;
        String tableContributionAssistanceAI = schema + RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;

        sql.append("DISTINCT ");

        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_1).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.DESIGNATION_2).append(",");
        sql.append(tableTiers).append(".").append(ITITiersDefTable.ID_PAYS).append(",");

        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_DECES).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.DATE_NAISSANCE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_SEXE).append(",");
        sql.append(tablePersonne).append(".").append(ITIPersonneDefTable.CS_CANTON).append(",");

        sql.append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL).append(",");

        sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_DOSSIER).append(",");
        sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_CS_ETAT_DOSSIER).append(",");
        sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_DATE_DEBUT).append(",");
        sql.append(tableDossier).append(".").append(RFDossier.FIELDNAME_DATE_FIN).append(",");

        sql.append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE).append(",");

        sql.append(tableGestionnaire).append(".").append(RFDossierJointTiers.FIELD_USERNAME_GESTIONNAIRE).append(",");
        sql.append(tableGestionnaire).append(".").append(RFDossierJointTiers.FIELD_VISA_GESTIONNAIRE).append(",");

        sql.append(tableContributionAssistanceAI).append(".")
                .append(RFContributionsAssistanceAI.ID_CONTRIBUTION_ASSISTANCE_AI).append(",");
        sql.append(tableContributionAssistanceAI).append(".").append(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE)
                .append(",");
        sql.append(tableContributionAssistanceAI).append(".").append(RFContributionsAssistanceAI.DATE_FIN_PERIODE);

        return sql.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String schema = _getCollection();
        String tableDossier = schema + RFDossier.TABLE_NAME;
        String tableDemandePrestation = schema + PRDemande.TABLE_NAME;
        String tableTiers = schema + ITITiersDefTable.TABLE_NAME;
        String tablePersonne = schema + ITIPersonneDefTable.TABLE_NAME;
        String tablePersonneAvs = schema + ITIPersonneAvsDefTable.TABLE_NAME;
        String tableHistoriqueAvs = schema + ITIHistoriqueAvsDefTable.TABLE_NAME;
        String tableGestionnaire = schema + RFDossierJointTiers.TABLE_GESTIONNAIRE;
        String tableContributionAssistanceAI = schema + RFContributionsAssistanceAI.TABLE_CONTRIBUTION_ASSISTANCE_AI;

        sql.append(tableDossier);

        sql.append(" INNER JOIN ").append(tableDemandePrestation);
        sql.append(" ON ").append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_PRDEM).append("=")
                .append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDDEMANDE);

        sql.append(" INNER JOIN ").append(tableTiers);
        sql.append(" ON ").append(tableDemandePrestation).append(".").append(PRDemande.FIELDNAME_IDTIERS).append("=")
                .append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonne);
        sql.append(" ON ").append(tableTiers).append(".").append(ITITiersDefTable.ID_TIERS).append("=")
                .append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tablePersonneAvs);
        sql.append(" ON ").append(tablePersonne).append(".").append(ITIPersonneDefTable.ID_TIERS).append("=")
                .append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS);

        sql.append(" INNER JOIN ").append(tableHistoriqueAvs);
        sql.append(" ON ").append(tablePersonneAvs).append(".").append(ITIPersonneAvsDefTable.ID_TIERS).append("=")
                .append(tableHistoriqueAvs).append(".").append(ITIHistoriqueAvsDefTable.ID_TIERS);

        sql.append(" LEFT OUTER JOIN ").append(tableGestionnaire);
        sql.append(" ON ").append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_GESTIONNAIRE).append("=")
                .append(tableGestionnaire).append(".").append(RFDossierJointTiers.FIELD_USERNAME_GESTIONNAIRE);

        sql.append(" LEFT OUTER JOIN ").append(tableContributionAssistanceAI);
        sql.append(" ON ").append(tableDossier).append(".").append(RFDossier.FIELDNAME_ID_DOSSIER).append("=")
                .append(tableContributionAssistanceAI).append(".").append(RFContributionsAssistanceAI.ID_DOSSIER_RFM);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return RFDossier.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric(ITITiersDefTable.ID_TIERS);
        nom = statement.dbReadString(ITITiersDefTable.DESIGNATION_1);
        prenom = statement.dbReadString(ITITiersDefTable.DESIGNATION_2);
        csNationalite = statement.dbReadNumeric(ITITiersDefTable.ID_PAYS);

        dateDeces = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_DECES);
        dateNaissance = statement.dbReadDateAMJ(ITIPersonneDefTable.DATE_NAISSANCE);
        csSexe = statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE);
        csCanton = statement.dbReadNumeric(ITIPersonneDefTable.CS_CANTON);

        nss = statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL);

        idDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_ID_DOSSIER);
        csEtatDossier = statement.dbReadNumeric(RFDossier.FIELDNAME_CS_ETAT_DOSSIER);
        dateDebutPeriodeDossier = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_DEBUT);
        dateFinPeriodeDossier = statement.dbReadDateAMJ(RFDossier.FIELDNAME_DATE_FIN);

        idDemandePrestation = statement.dbReadNumeric(PRDemande.FIELDNAME_IDDEMANDE);

        idGestionnaire = statement.dbReadString(RFDossierJointTiers.FIELD_USERNAME_GESTIONNAIRE);
        visaGestionnaire = statement.dbReadString(RFDossierJointTiers.FIELD_VISA_GESTIONNAIRE);

        idContributionAssistanceAI = statement.dbReadNumeric(RFContributionsAssistanceAI.ID_CONTRIBUTION_ASSISTANCE_AI);
        dateDebutPeriodeCAAI = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_DEBUT_PERIODE);
        dateFinPeriodeCAAI = statement.dbReadDateAMJ(RFContributionsAssistanceAI.DATE_FIN_PERIODE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        throw new Exception(getSession().getLabel("ERROR_ENTITE_NON_ENREGISTRABLE"));
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RFDossier.FIELDNAME_ID_DOSSIER,
                this._dbWriteNumeric(statement.getTransaction(), getIdDossier()));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new Exception(getSession().getLabel("ERROR_ENTITE_NON_ENREGISTRABLE"));
    }

    @Override
    public int compareTo(RFDossierJointTiers o) {
        int compareNom = JadeStringUtil.convertSpecialChars(o.getNom()).compareTo(
                JadeStringUtil.convertSpecialChars(getNom()));
        if (compareNom != 0) {
            return -1 * compareNom;
        }

        int comparePrenom = JadeStringUtil.convertSpecialChars(o.getPrenom()).compareTo(
                JadeStringUtil.convertSpecialChars(getPrenom()));
        if (comparePrenom != 0) {
            return -1 * comparePrenom;
        }

        int compareIdossier = JadeStringUtil.convertSpecialChars(o.getIdDossier()).compareTo(
                JadeStringUtil.convertSpecialChars(getIdDossier()));
        if (compareIdossier != 0) {
            return -1 * compareIdossier;
        }

        return getIdTiers().compareTo(o.getIdTiers());
    }

    public String getCsCanton() {
        return csCanton;
    }

    public String getCsEtatDossier() {
        return csEtatDossier;
    }

    public String getCsNationalite() {
        return csNationalite;
    }

    public String getCsSexe() {
        return csSexe;
    }

    /**
     * Visibilité de méthode réduite, afin que seul le manager puisse l'utiliser (après transformation par le manager,
     * utilisez {@link #getPeriodesCAAI()} pour récupérer les périodes)
     * 
     * @return
     */
    String getDateDebutPeriodeCAAI() {
        return dateDebutPeriodeCAAI;
    }

    public String getDateDebutPeriodeDossier() {
        return dateDebutPeriodeDossier;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * Visibilité de méthode réduite, afin que seul le manager puisse l'utiliser (après transformation par le manager,
     * utilisez {@link #getPeriodesCAAI()} pour récupérer les périodes)
     * 
     * @return
     */
    String getDateFinPeriodeCAAI() {
        return dateFinPeriodeCAAI;
    }

    public String getDateFinPeriodeDossier() {
        return dateFinPeriodeDossier;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public RFPeriodeCAAIWrapper getDernierePeriodeCAAI() {
        if ((periodesCAAI == null) || (periodesCAAI.size() == 0)) {
            return null;
        }
        return periodesCAAI.get(periodesCAAI.size() - 1);
    }

    public String getIdContributionAssistanceAI() {
        return idContributionAssistanceAI;
    }

    public String getIdDemandePrestation() {
        return idDemandePrestation;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    /**
     * Retourne la liste des périodes de contribution d'assistance AI liées à ce dossier RFM
     * 
     * @return
     */
    public List<RFPeriodeCAAIWrapper> getPeriodesCAAI() {
        return periodesCAAI;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getVisaGestionnaire() {
        return visaGestionnaire;
    }

    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    public void setCsEtatDossier(String csEtatDossier) {
        this.csEtatDossier = csEtatDossier;
    }

    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    /**
     * Visibilité de méthode réduite, afin que seul le manager puisse l'utiliser
     * 
     * @param dateDebutPeriodeCAAI
     */
    public void setDateDebutPeriodeCAAI(String dateDebutPeriodeCAAI) {
        this.dateDebutPeriodeCAAI = dateDebutPeriodeCAAI;
    }

    public void setDateDebutPeriodeDossier(String dateDebutPeriodeDossier) {
        this.dateDebutPeriodeDossier = dateDebutPeriodeDossier;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    /**
     * Visibilité de méthode réduite, afin que seul le manager puisse l'utiliser
     * 
     * @param dateFinPeriodeCAAI
     */
    public void setDateFinPeriodeCAAI(String dateFinPeriodeCAAI) {
        this.dateFinPeriodeCAAI = dateFinPeriodeCAAI;
    }

    public void setDateFinPeriodeDossier(String dateFinPeriodeDossier) {
        this.dateFinPeriodeDossier = dateFinPeriodeDossier;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Visibilité de méthode réduite, afin que seul le manager puisse l'utiliser
     * 
     * @param idContributionAssistanceAI
     */
    void setIdContributionAssistanceAI(String idContributionAssistanceAI) {
        this.idContributionAssistanceAI = idContributionAssistanceAI;
    }

    public void setIdDemandePrestation(String idDemandePrestation) {
        this.idDemandePrestation = idDemandePrestation;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPeriodesCAAI(List<RFPeriodeCAAIWrapper> periodesCAAI) {
        this.periodesCAAI = periodesCAAI;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setVisaGestionnaire(String visaGestionnaire) {
        this.visaGestionnaire = visaGestionnaire;
    }
}
