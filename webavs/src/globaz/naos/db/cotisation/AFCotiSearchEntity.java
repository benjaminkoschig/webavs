package globaz.naos.db.cotisation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class AFCotiSearchEntity extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // plan d'affiliation
    private Boolean blocageEnvoiPlanAffiliation = new Boolean(false); // blocage
    private String brancheEconomique = ""; // raison sociale
    private String codeCaisseMetier = ""; // code de la caisse
    private String dateDebutAdhesion = ""; // date de début de l'adhésion

    private String dateDebutAffiliation = ""; // date debut affiliation
    private String dateDebutCotisation = ""; // date de début de la cotisation
    private String dateFinAdhesion = ""; // date de fin de l'adhésion
    private String dateFinAffiliation = ""; // date fin affiliation
    private String dateFinCotisation = ""; // date de fin de la cotisation
    private String designation1 = ""; // nom de l'affilié
    private String designation1CaisseMetier = ""; // nom de la caisse
    private String designation2 = ""; // nom de l'affilié
    private String designation2CaisseMetier = ""; // nom de la caisse
    private String designation3 = ""; // nom de l'affilié
    private String designation3CaisseMetier = ""; // nom de la caisse
    private String designation4 = ""; // nom de l'affilié

    private String designation4CaisseMetier = ""; // nom de la caisse
    // d'affiliation
    private String domaineCourrierPlanAffiliation = ""; // domaine de courrier
    // du plan d'affiliation
    private String domaineRecouvrementPlanAffiliation = ""; // domaine de
    // recouvrement du
    // plan
    // d'affiliation
    private String domaineRemboursementPlanAffiliation = ""; // domaine de
    // remboursement
    // du plan
    // d'affiliation

    private String genreAssurance = ""; // genre de l'assurance
    private String idAdhesion = ""; // type d'adhésion
    private String idAffiliation = ""; // id affiliation
    private String idCotisation = ""; // id de la cotisation
    private String idPlanAffiliation = "";

    private String idTiers = ""; // id du tiers
    // paritaire) aux acomptes
    // (=2)
    private Boolean impressionReleve = new Boolean(false); // impression
    private String libelleAssuranceDe = ""; // libelle assurance (allemand)
    private String libelleAssuranceFr = ""; // libelle assurance (français)
    private String libelleAssuranceIt = ""; // libelle assurance (italien)

    private String libelleFacturePlanAffiliation = ""; // libelle facture du
    private String libellePlanAffiliation = ""; // libelle du plan d'affiliation
    private String masseAnnuelleCotisation = ""; // masse annuelle de la
    // cotisation
    private String montantAnnuelCotisation = ""; // montant annuel de la
    // cotisation
    private String montantMensuelCotisation = ""; // montant mensuel de la
    // la cotisation
    private String montantSemestrielCotisation = ""; // montant semestriel de la
    // cotisation
    private String montantTrimestrielCotisation = ""; // montant trimestriel de
    private String motifFinAffiliation = ""; // motif de fin d'affiliation
    private String motifFinCotisation = ""; // motif de fin de la cotisation
    private String numeroAffilie = ""; // numero affilié
    // automatique des
    // relevés à blanc
    private String periodiciteAffiliation = ""; // periodicite de l'affiliation

    private String periodiciteCotisation = ""; // periodicite de la cotisation
    // envoi
    // du
    // plan
    // d'affiliation
    private Boolean planAffiliationInactif = new Boolean(false); // plan
    // d'affiliation
    // actif /
    // inactif
    private String raisonSociale = ""; // raison sociale
    private Boolean releve = new Boolean(false); // au relevé (=1 seul.

    // cotisation
    private String tauxAssuranceIdCotisation = ""; // taux spécifique à la
    // cotisation
    private String typeAdhesion = ""; // type d'adhésion
    private String typeAdressePlanAffiliation = ""; // type d'adresse du plan
    private String typeAffiliation = ""; // type affiliation

    private String typeAssurance = ""; // type de l'assurance

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idTiers = statement.dbReadNumeric("afaffipHTITIE");
        idAffiliation = statement.dbReadNumeric("afaffipMAIAFF");
        numeroAffilie = statement.dbReadString("afaffipMALNAF");
        libelleAssuranceFr = statement.dbReadString("afassupMBLLIF");

        designation1 = statement.dbReadString("affHTLDE1"); // nom de l'affilié
        designation2 = statement.dbReadString("affHTLDE2"); // nom de l'affilié
        designation3 = statement.dbReadString("affHTLDE3"); // nom de l'affilié
        designation4 = statement.dbReadString("affHTLDE4"); // nom de l'affilié

        idTiers = statement.dbReadNumeric("afaffipHTITIE"); // id du tiers
        idAffiliation = statement.dbReadNumeric("afaffipMAIAFF"); // id
        // affiliation
        numeroAffilie = statement.dbReadString("afaffipMALNAF"); // numero
        // affilié
        dateDebutAffiliation = statement.dbReadDateAMJ("afaffipMADDEB"); // date
        // debut
        // affiliation
        dateFinAffiliation = statement.dbReadDateAMJ("afaffipMADFIN"); // date
        // fin
        // affiliation
        typeAffiliation = statement.dbReadNumeric("afaffipMATTAF"); // type
        // affiliation
        motifFinAffiliation = statement.dbReadNumeric("afaffipMATMOT"); // motif
        // de
        // fin
        // d'affiliation
        releve = statement.dbReadBoolean("afaffipMABREP"); // au relevé (=1
        // seul. paritaire)
        // aux acomptes(=2)
        impressionReleve = statement.dbReadBoolean("afaffipMABEAA"); // impression
        // automatique
        // des
        // relevés
        // à
        // blanc
        periodiciteAffiliation = statement.dbReadNumeric("afaffipMATPER"); // periodicite
        // de
        // l'affiliation
        raisonSociale = statement.dbReadString("afaffipMADESL"); // raison
        // sociale
        // l'affiliation
        brancheEconomique = statement.dbReadNumeric("afaffipMATBRA"); // branche
        // économique

        libellePlanAffiliation = statement.dbReadString("afplafpMULLIB"); // libelle
        // du
        // plan
        // d'affiliation
        libelleFacturePlanAffiliation = statement.dbReadString("afplafpMULFAC"); // libelle
        // facture
        // du
        // plan
        // d'affiliation
        blocageEnvoiPlanAffiliation = statement.dbReadBoolean("afplafpMUBBLO"); // blocage
        // envoi
        // du
        // plan
        // d'affiliation
        planAffiliationInactif = statement.dbReadBoolean("afplafpMUBINA"); // plan
        // d'affiliation
        // actif
        // /
        // inactif

        idPlanAffiliation = statement.dbReadNumeric("afplafpMUIPLA"); // type
        // d'adresse
        // du
        // plan
        // d'affiliation
        typeAdressePlanAffiliation = statement.dbReadNumeric("afplafpHETTAD"); // type
        // d'adresse
        // du
        // plan
        // d'affiliation
        domaineCourrierPlanAffiliation = statement.dbReadNumeric("afplafpHFIAPP"); // domaine
        // de
        // courrier
        // du
        // plan
        // d'affiliation
        domaineRecouvrementPlanAffiliation = statement.dbReadNumeric("afplafpHFIAPL"); // domaine
        // de
        // recouvrement
        // du
        // plan
        // d'affiliation
        domaineRemboursementPlanAffiliation = statement.dbReadNumeric("afplafpHFIAPR"); // domaine
        // de
        // remboursement
        // du
        // plan
        // d'affiliation

        libelleAssuranceIt = statement.dbReadString("afassupMBLLII"); // libelle
        // assurance
        // (italien)
        libelleAssuranceFr = statement.dbReadString("afassupMBLLIF"); // libelle
        // assurance
        // (français)
        libelleAssuranceDe = statement.dbReadString("afassupMBLLID"); // libelle
        // assurance
        // (allemand)
        typeAssurance = statement.dbReadNumeric("afassupMBTTYP"); // type de
        // l'assurance
        genreAssurance = statement.dbReadNumeric("afassupMBTGEN"); // genre de
        // l'assurance

        idCotisation = statement.dbReadNumeric("afcotipMEICOT"); // id de la
        // cotisation
        dateDebutCotisation = statement.dbReadDateAMJ("afcotipMEDDEB"); // date
        // de
        // début
        // de la
        // cotisation
        dateFinCotisation = statement.dbReadDateAMJ("afcotipMEDFIN"); // date de
        // fin
        // de la
        // cotisation
        motifFinCotisation = statement.dbReadNumeric("afcotipMETMOT"); // motif
        // de
        // fin
        // de la
        // cotisation
        periodiciteCotisation = statement.dbReadNumeric("afcotipMETPER"); // periodicite
        // de
        // la
        // cotisation
        masseAnnuelleCotisation = statement.dbReadNumeric("afcotipMEMMAP"); // masse
        // annuelle
        // de
        // la
        // cotisation
        montantTrimestrielCotisation = statement.dbReadNumeric("afcotipMEMTRI"); // montant
        // trimestriel
        // de
        // la
        // cotisation
        montantSemestrielCotisation = statement.dbReadNumeric("afcotipMEMSEM"); // montant
        // semestriel
        // de
        // la
        // cotisation
        montantAnnuelCotisation = statement.dbReadNumeric("afcotipMEMANN"); // montant
        // annuel
        // de
        // la
        // cotisation
        montantMensuelCotisation = statement.dbReadNumeric("afcotipMEMMEN"); // montant
        // mensuel
        // de
        // la
        // cotisation
        tauxAssuranceIdCotisation = statement.dbReadNumeric("afcotipMCITAU"); // taux
        // spécifique
        // à
        // la
        // cotisation
        idAdhesion = statement.dbReadNumeric("afadhepMRIADH"); // type
        // d'adhésion
        typeAdhesion = statement.dbReadNumeric("afadhepMRTADH"); // type
        // d'adhésion
        dateDebutAdhesion = statement.dbReadDateAMJ("afadhepMRDDEB"); // date de
        // début
        // de
        // l'adhésion
        dateFinAdhesion = statement.dbReadDateAMJ("afadhepMRDFIN"); // date de
        // fin de
        // l'adhésion

        designation1CaisseMetier = statement.dbReadString("caisseHTLDE1"); // nom
        // de
        // la
        // caisse
        designation2CaisseMetier = statement.dbReadString("caisseHTLDE2"); // nom
        // de
        // la
        // caisse
        designation3CaisseMetier = statement.dbReadString("caisseHTLDE3"); // nom
        // de
        // la
        // caisse
        designation4CaisseMetier = statement.dbReadString("caisseHTLDE4"); // nom
        // de
        // la
        // caisse

        codeCaisseMetier = statement.dbReadString("tiadmipHBCADM"); // code de
        // la caisse

    }

    /*
     * Not used
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(_getBaseTable() + "HTITIE",
                this._dbWriteNumeric(statement.getTransaction(), getIdTiers(), ""));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public Boolean getBlocageEnvoiPlanAffiliation() {
        return blocageEnvoiPlanAffiliation;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public String getCodeCaisseMetier() {
        return codeCaisseMetier;
    }

    public String getDateDebutAdhesion() {
        return dateDebutAdhesion;
    }

    public String getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public String getDateDebutCotisation() {
        return dateDebutCotisation;
    }

    public String getDateFinAdhesion() {
        return dateFinAdhesion;
    }

    public String getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public String getDateFinCotisation() {
        return dateFinCotisation;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation1CaisseMetier() {
        return designation1CaisseMetier;
    }

    public String getDesignation2() {
        return designation2;
    }

    public String getDesignation2CaisseMetier() {
        return designation2CaisseMetier;
    }

    public String getDesignation3() {
        return designation3;
    }

    public String getDesignation3CaisseMetier() {
        return designation3CaisseMetier;
    }

    public String getDesignation4() {
        return designation4;
    }

    public String getDesignation4CaisseMetier() {
        return designation4CaisseMetier;
    }

    public String getDomaineCourrierPlanAffiliation() {
        return domaineCourrierPlanAffiliation;
    }

    public String getDomaineRecouvrementPlanAffiliation() {
        return domaineRecouvrementPlanAffiliation;
    }

    public String getDomaineRemboursementPlanAffiliation() {
        return domaineRemboursementPlanAffiliation;
    }

    public String getGenreAssurance() {
        return genreAssurance;
    }

    public String getIdAdhesion() {
        return idAdhesion;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCotisation() {
        return idCotisation;
    }

    public String getIdPlanAffiliation() {
        return idPlanAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getImpressionReleve() {
        return impressionReleve;
    }

    public String getLibelleAssuranceDe() {
        return libelleAssuranceDe;
    }

    public String getLibelleAssuranceFr() {
        return libelleAssuranceFr;
    }

    public String getLibelleAssuranceIt() {
        return libelleAssuranceIt;
    }

    public String getLibelleFacturePlanAffiliation() {
        return libelleFacturePlanAffiliation;
    }

    public String getLibellePlanAffiliation() {
        return libellePlanAffiliation;
    }

    public String getMasseAnnuelleCotisation() {
        return masseAnnuelleCotisation;
    }

    public String getMontantAnnuelCotisation() {
        return montantAnnuelCotisation;
    }

    public String getMontantMensuelCotisation() {
        return montantMensuelCotisation;
    }

    public String getMontantSemestrielCotisation() {
        return montantSemestrielCotisation;
    }

    public String getMontantTrimestrielCotisation() {
        return montantTrimestrielCotisation;
    }

    public String getMotifFinAffiliation() {
        return motifFinAffiliation;
    }

    public String getMotifFinCotisation() {
        return motifFinCotisation;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getPeriodiciteAffiliation() {
        return periodiciteAffiliation;
    }

    public String getPeriodiciteCotisation() {
        return periodiciteCotisation;
    }

    public Boolean getPlanAffiliationInactif() {
        return planAffiliationInactif;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public Boolean getReleve() {
        return releve;
    }

    public String getTauxAssuranceIdCotisation() {
        return tauxAssuranceIdCotisation;
    }

    public String getTypeAdhesion() {
        return typeAdhesion;
    }

    public String getTypeAdressePlanAffiliation() {
        return typeAdressePlanAffiliation;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public String getTypeAssurance() {
        return typeAssurance;
    }

    public void setBlocageEnvoiPlanAffiliation(Boolean blocageEnvoiPlanAffiliation) {
        this.blocageEnvoiPlanAffiliation = blocageEnvoiPlanAffiliation;
    }

    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    public void setCodeCaisseMetier(String codeCaisseMetier) {
        this.codeCaisseMetier = codeCaisseMetier;
    }

    public void setDateDebutAdhesion(String dateDebutAdhesion) {
        this.dateDebutAdhesion = dateDebutAdhesion;
    }

    public void setDateDebutAffiliation(String dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutCotisation(String dateDebutCotisation) {
        this.dateDebutCotisation = dateDebutCotisation;
    }

    public void setDateFinAdhesion(String dateFinAdhesion) {
        this.dateFinAdhesion = dateFinAdhesion;
    }

    public void setDateFinAffiliation(String dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinCotisation(String dateFinCotisation) {
        this.dateFinCotisation = dateFinCotisation;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation1CaisseMetier(String designation1CaisseMetier) {
        this.designation1CaisseMetier = designation1CaisseMetier;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    public void setDesignation2CaisseMetier(String designation2Caissemetier) {
        designation2CaisseMetier = designation2Caissemetier;
    }

    public void setDesignation3(String designation3) {
        this.designation3 = designation3;
    }

    public void setDesignation3CaisseMetier(String designation3CaisseMetier) {
        this.designation3CaisseMetier = designation3CaisseMetier;
    }

    public void setDesignation4(String designation4) {
        this.designation4 = designation4;
    }

    public void setDesignation4Caissemetier(String designation4Caissemetier) {
        designation4CaisseMetier = designation4CaisseMetier;
    }

    public void setDomaineCourrierPlanAffiliation(String domaineCourrierPlanAffiliation) {
        this.domaineCourrierPlanAffiliation = domaineCourrierPlanAffiliation;
    }

    public void setDomaineRecouvrementPlanAffiliation(String domaineRecouvrementPlanAffiliation) {
        this.domaineRecouvrementPlanAffiliation = domaineRecouvrementPlanAffiliation;
    }

    public void setDomaineRemboursementPlanAffiliation(String domaineRemboursementPlanAffiliation) {
        this.domaineRemboursementPlanAffiliation = domaineRemboursementPlanAffiliation;
    }

    public void setGenreAssurance(String genreAssurance) {
        this.genreAssurance = genreAssurance;
    }

    public void setIdAdhesion(String idAdhesion) {
        this.idAdhesion = idAdhesion;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdCotisation(String idCotisation) {
        this.idCotisation = idCotisation;
    }

    public void setIdPlanAffiliation(String idPlanAffiliation) {
        this.idPlanAffiliation = idPlanAffiliation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setImpressionReleve(Boolean impressionReleve) {
        this.impressionReleve = impressionReleve;
    }

    public void setLibelleAssuranceDe(String libelleAssuranceDe) {
        this.libelleAssuranceDe = libelleAssuranceDe;
    }

    public void setLibelleAssuranceFr(String libelleAssuranceFr) {
        this.libelleAssuranceFr = libelleAssuranceFr;
    }

    public void setLibelleAssuranceIt(String libelleAssuranceIt) {
        this.libelleAssuranceIt = libelleAssuranceIt;
    }

    public void setLibelleFacturePlanAffiliation(String libelleFacturePlanAffiliation) {
        this.libelleFacturePlanAffiliation = libelleFacturePlanAffiliation;
    }

    public void setLibellePlanAffiliation(String libellePlanAffiliation) {
        this.libellePlanAffiliation = libellePlanAffiliation;
    }

    public void setMasseAnnuelleCotisation(String masseAnnuelleCotisation) {
        this.masseAnnuelleCotisation = masseAnnuelleCotisation;
    }

    public void setMontantAnnuelCotisation(String montantAnnuelCotisation) {
        this.montantAnnuelCotisation = montantAnnuelCotisation;
    }

    public void setMontantMensuelCotisation(String montantMensuelCotisation) {
        this.montantMensuelCotisation = montantMensuelCotisation;
    }

    public void setMontantSemestrielCotisation(String montantSemestrielCotisation) {
        this.montantSemestrielCotisation = montantSemestrielCotisation;
    }

    public void setMontantTrimestrielCotisation(String montantTrimestrielCotisation) {
        this.montantTrimestrielCotisation = montantTrimestrielCotisation;
    }

    public void setMotifFinAffiliation(String motifFinAffiliation) {
        this.motifFinAffiliation = motifFinAffiliation;
    }

    public void setMotifFinCotisation(String motifFinCotisation) {
        this.motifFinCotisation = motifFinCotisation;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setPeriodiciteAffiliation(String periodiciteAffiliation) {
        this.periodiciteAffiliation = periodiciteAffiliation;
    }

    public void setPeriodiciteCotisation(String periodiciteCotisation) {
        this.periodiciteCotisation = periodiciteCotisation;
    }

    public void setPlanAffiliationInactif(Boolean planAffiliationInactif) {
        this.planAffiliationInactif = planAffiliationInactif;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setReleve(Boolean releve) {
        this.releve = releve;
    }

    public void setTauxAssuranceIdCotisation(String tauxAssuranceIdCotisation) {
        this.tauxAssuranceIdCotisation = tauxAssuranceIdCotisation;
    }

    public void setTypeAdhesion(String typeAdhesion) {
        this.typeAdhesion = typeAdhesion;
    }

    public void setTypeAdressePlanAffiliation(String typeAdressePlanAffiliation) {
        this.typeAdressePlanAffiliation = typeAdressePlanAffiliation;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public void setTypeAssurance(String typeAssurance) {
        this.typeAssurance = typeAssurance;
    }

}
