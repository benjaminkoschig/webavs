package globaz.corvus.db.rentesaccordees;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.prestations.REPrestations;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BStatement;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.IPRTiers;
import globaz.prestation.tools.PRDateFormater;

public class REDecisionJointDemandeRente extends REDecisionEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String createFromClause(String schema) {
        StringBuilder from = new StringBuilder();

        String innerJoin = " INNER JOIN ";
        String on = " ON ";
        String point = ".";
        String egal = "=";

        from.append(schema);
        from.append(REDecisionEntity.TABLE_NAME_DECISIONS);

        // jointure entre table des décisions et demande Rente
        from.append(innerJoin);
        from.append(schema);
        from.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        from.append(on);
        from.append(schema);
        from.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        from.append(point);
        from.append(REDecisionEntity.FIELDNAME_ID_DEMANDE_RENTE);
        from.append(egal);
        from.append(schema);
        from.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        from.append(point);
        from.append(REDemandeRente.FIELDNAME_ID_DEMANDE_RENTE);

        // jointure entre table demande Rente et table demande prestation
        from.append(innerJoin);
        from.append(schema);
        from.append(PRDemande.TABLE_NAME);
        from.append(on);
        from.append(schema);
        from.append(REDemandeRente.TABLE_NAME_DEMANDE_RENTE);
        from.append(point);
        from.append(REDemandeRente.FIELDNAME_ID_DEMANDE_PRESTATION);
        from.append(egal);
        from.append(schema);
        from.append(PRDemande.TABLE_NAME);
        from.append(point);
        from.append(PRDemande.FIELDNAME_IDDEMANDE);

        // jointure entre table des decisions et table des numeros AVS
        from.append(innerJoin);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_AVS);
        from.append(on);
        from.append(schema);
        from.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        from.append(point);
        from.append(REDecisionEntity.FIELDNAME_ID_TIERS_BENEF_PRINCIPAL);
        from.append(egal);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_AVS);
        from.append(point);
        from.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // jointure entre table des des numeros AVS et table des personne
        from.append(innerJoin);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_PERSONNE);
        from.append(on);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_AVS);
        from.append(point);
        from.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
        from.append(egal);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_PERSONNE);
        from.append(point);
        from.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // jointure entre table des personne et table des tiers
        from.append(innerJoin);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_TIERS);
        from.append(on);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_PERSONNE);
        from.append(point);
        from.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
        from.append(egal);
        from.append(schema);
        from.append(IPRConstantesExternes.TABLE_TIERS);
        from.append(point);
        from.append(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);

        // pour la recherche sur le no AVS
        from.append(innerJoin);
        from.append(schema);
        from.append(IPRTiers.TABLE_AVS_HIST);
        from.append(" AS ");
        from.append(IPRTiers.TABLE_AVS_HIST);
        from.append(on);
        from.append(schema + IPRTiers.TABLE_AVS);
        from.append(point);
        from.append(IPRTiers.FIELD_TI_IDTIERS);
        from.append(egal);
        from.append(IPRTiers.TABLE_AVS_HIST);
        from.append(point);
        from.append(IPRTiers.FIELD_TI_IDTIERS);

        // jointure entre table des prestation et table des décisions
        from.append(innerJoin);
        from.append(schema);
        from.append(REPrestations.TABLE_NAME_PRESTATION);
        from.append(on);
        from.append(schema);
        from.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        from.append(point);
        from.append(REDecisionEntity.FIELDNAME_ID_DECISION);
        from.append(egal);
        from.append(schema);
        from.append(REPrestations.TABLE_NAME_PRESTATION);
        from.append(point);
        from.append(REPrestations.FIELDNAME_ID_DECISION);

        // jointure entre table des décisions et validation décisions
        from.append(innerJoin);
        from.append(schema);
        from.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        from.append(on);
        from.append(schema);
        from.append(REDecisionEntity.TABLE_NAME_DECISIONS);
        from.append(point);
        from.append(REDecisionEntity.FIELDNAME_ID_DECISION);
        from.append(egal);
        from.append(schema);
        from.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        from.append(point);
        from.append(REValidationDecisions.FIELDNAME_ID_DECISION);

        // jointure entre table validation décisions et table préstation dues
        from.append(innerJoin);
        from.append(schema);
        from.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        from.append(on);
        from.append(schema);
        from.append(REValidationDecisions.TABLE_NAME_VALIDATION_DECISION);
        from.append(point);
        from.append(REValidationDecisions.FIELDNAME_ID_PRESTATION_DUE);
        from.append(egal);
        from.append(schema);
        from.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        from.append(point);
        from.append(REPrestationDue.FIELDNAME_ID_PRESTATION_DUE);

        // jointure entre table préstations dues et table rentes accordées
        from.append(innerJoin);
        from.append(schema);
        from.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        from.append(on);
        from.append(schema);
        from.append(REPrestationDue.TABLE_NAME_PRESTATIONS_DUES);
        from.append(point);
        from.append(REPrestationDue.FIELDNAME_ID_RENTE_ACCORDEE);
        from.append(egal);
        from.append(schema);
        from.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        from.append(point);
        from.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);

        // jointure entre table rentes accordées et prestations accordees
        from.append(innerJoin);
        from.append(schema);
        from.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        from.append(on);
        from.append(schema);
        from.append(RERenteAccordee.TABLE_NAME_RENTE_ACCORDEE);
        from.append(point);
        from.append(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE);
        from.append(egal);
        from.append(schema);
        from.append(REPrestationsAccordees.TABLE_NAME_PRESTATIONS_ACCORDEES);
        from.append(point);
        from.append(REPrestationsAccordees.FIELDNAME_ID_PRESTATION_ACCORDEE);

        return from.toString();
    }

    private String csNationaliteRequerant = "";
    private String csSexeRequerant = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String dateNaissanceRequerant = "";
    private String genrePrestation = "";
    private String idLot = "";
    private String idPrestation = "";
    private String idTierRequerant = "";
    private String montantPrestation = "";
    private String nomRequerant = "";
    private String nssRequerant = "";
    private int numDateDebutDroit;
    private String prenomRequerant = "";

    @Override
    protected boolean _autoInherits() {
        return false;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return REDecisionJointDemandeRente.createFromClause(_getCollection());
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        super._readProperties(statement);

        idTierRequerant = statement.dbReadNumeric(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_ID_TIERS_TI);
        genrePrestation = statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION);
        nssRequerant = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_AVS_NUM_AVS);
        nomRequerant = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM);
        prenomRequerant = statement.dbReadString(IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM);
        dateNaissanceRequerant = statement.dbReadDateAMJ(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_DATENAISSANCE);
        csSexeRequerant = statement.dbReadNumeric(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_SEXE);
        csNationaliteRequerant = statement.dbReadNumeric(IPRConstantesExternes.FIELDNAME_TABLE_PERSONNE_NATIONALITE);
        dateDebutDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        dateFinDroit = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement
                .dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        numDateDebutDroit = new Integer(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT))
                .intValue();
        idPrestation = statement.dbReadNumeric(REPrestations.FIELDNAME_ID_PRESTATION);
        montantPrestation = statement.dbReadNumeric(REPrestations.FIELDNAME_MONTANT_PRESTATION);
        idLot = statement.dbReadNumeric(REPrestations.FIELDNAME_ID_LOT);
    }

    public String getCsNationaliteRequerant() {
        return csNationaliteRequerant;
    }

    public String getCsSexeRequerant() {
        return csSexeRequerant;
    }

    public String getDateDebutDroit() {
        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(getCsTypeDecision())) {
            return getDecisionDepuis();
        }
        return dateDebutDroit;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    public String getDateNaissanceRequerant() {
        return dateNaissanceRequerant;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdTierRequerant() {
        return idTierRequerant;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public String getNssRequerant() {
        return nssRequerant;
    }

    public int getNumDateDebutDroit() {
        return numDateDebutDroit;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    public void setCsNationaliteRequerant(String csNationaliteRequerant) {
        this.csNationaliteRequerant = csNationaliteRequerant;
    }

    public void setCsSexeRequerant(String csSexeRequerant) {
        this.csSexeRequerant = csSexeRequerant;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    public void setDateNaissanceRequerant(String dateNaissanceRequerant) {
        this.dateNaissanceRequerant = dateNaissanceRequerant;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdTierRequerant(String idTierRequerant) {
        this.idTierRequerant = idTierRequerant;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setNssRequerant(String nssRequerant) {
        this.nssRequerant = nssRequerant;
    }

    public void setNumDateDebutDroit(int numDateDebutDroit) {
        this.numDateDebutDroit = numDateDebutDroit;
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }
}
