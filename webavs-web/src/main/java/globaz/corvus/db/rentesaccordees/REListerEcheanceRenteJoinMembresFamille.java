package globaz.corvus.db.rentesaccordees;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.db.tiers.ITIPersonneAvsDefTable;
import globaz.pyxis.db.tiers.ITIPersonneDefTable;
import globaz.pyxis.db.tiers.ITITiersDefTable;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;

/**
 * @author PCA
 */
public class REListerEcheanceRenteJoinMembresFamille extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeNationalite = "";
    private String codePrestation = "";
    private String ConjointMFDateNaissance = "";
    private String ConjointTDateNaissance = "";
    private String cs1 = "";
    private String cs2 = "";
    private String cs3 = "";
    private String cs4 = "";
    private String cs5 = "";
    private String csDomaine = "";
    private String csEtatDemandeRente = "";
    private String csSexe = "";
    private String csTypeInfoComplementaire = "";
    private String dateDebutDroit = "";
    private String dateDeces = "";
    private String dateEcheanceRenteAccordee = "";
    private String dateFinDroit = "";
    private String dateNaissance = "";
    private String dateRevocationAjournement = "";
    private String degreInvalidite = "";
    private String etat = "";
    private String fractionRente = "";
    private boolean GenererLettre = false;
    private String idConjoints = "";
    private String idConjointT = "";
    private String idInfoCompta = "";
    private String idMembre = "";
    private String idMembreFamille = "";
    private String idMFConjoint1 = "";
    private String idMFConjoint2 = "";
    private String idRelation = "";
    private String idRenteAccordee = "";
    private String idTiers = "";
    private boolean isDejaTraite = false;
    private Boolean isPrestationBloquee = Boolean.FALSE;
    private boolean ListerInfoEntity = false;
    private String montantPrestation = "";
    /**
     * Ajout de ce champ suite au mandat Inforom 483
     */
    private REMotifEcheance motifEcheance;
    private String motifLettre = "";
    private String motifListe = "";
    private String nom = "";
    private String nss = "";
    private String periodeDateDebut = "";
    private String periodeDateFin = "";
    private String periodeType = "";
    private String prenom = "";
    private String relationDateDebut = "";
    private String relationDateFin = "";
    private String relationType = "";
    private String sexeConjoint = "";
    private boolean hasSameIdAdressePaiement = true;
    private String idTiersAdressePaiement = "";
    private List<REListerEcheanceRenteJoinMembresFamille> listeEcheanceLiees = new ArrayList<REListerEcheanceRenteJoinMembresFamille>();

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
    protected String _getTableName() {
        return null; // PAS DE TABLES !!!
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        setIdRenteAccordee(statement.dbReadNumeric(RERenteAccordee.FIELDNAME_ID_RENTE_ACCORDEE));
        setDateRevocationAjournement(statement.dbReadNumeric(RERenteAccordee.FIELDNAME_DATE_REVOCATION_AJOURNEMENT));
        setCs1(statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_1));
        setCs2(statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_2));
        setCs3(statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_3));
        setCs4(statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_4));
        setCs5(statement.dbReadString(RERenteAccordee.FIELDNAME_CODE_CAS_SPECIAUX_5));

        setEtat(statement.dbReadString(REPrestationsAccordees.FIELDNAME_CS_ETAT));
        setCodePrestation(statement.dbReadString(REPrestationsAccordees.FIELDNAME_CODE_PRESTATION));
        setMontantPrestation(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_MONTANT_PRESTATION));
        setDateDebutDroit(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT));
        setDateFinDroit(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT));
        setFractionRente(statement.dbReadString(REPrestationsAccordees.FIELDNAME_FRACTION_RENTE));
        setDateEcheanceRenteAccordee(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_DATE_ECHEANCE));
        setIdTiers(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_TIERS_BENEFICIAIRE));
        setIdInfoCompta(statement.dbReadNumeric(REPrestationsAccordees.FIELDNAME_ID_INFO_COMPTA));
        setIsPrestationBloquee(statement.dbReadBoolean(REPrestationsAccordees.FIELDNAME_IS_PRESTATION_BLOQUEE));

        setDegreInvalidite(statement.dbReadNumeric(REBasesCalcul.FIELDNAME_DEGRE_INVALIDITE));

        setNom(statement.dbReadString(ITITiersDefTable.DESIGNATION_1));
        setPrenom(statement.dbReadString(ITITiersDefTable.DESIGNATION_2));
        setCodeNationalite(statement.dbReadNumeric(ITITiersDefTable.ID_PAYS));

        setCsSexe(statement.dbReadNumeric(ITIPersonneDefTable.CS_SEXE));
        setNss(statement.dbReadString(ITIPersonneAvsDefTable.NUMERO_AVS_ACTUEL));
        setDateNaissance(statement.dbReadNumeric(ITIPersonneDefTable.DATE_NAISSANCE));
        setDateDeces(statement.dbReadNumeric(ITIPersonneDefTable.DATE_DECES));

        setIdMembre(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_TIERS));
        setIdMembreFamille(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_ID_MEMBRE_FAMILLE));
        setCsDomaine(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_MEMBRE_FAMILLE_BENEFICIAIRE_DOMAINE_APPLICATION));

        setPeriodeType(statement.dbReadNumeric(SFPeriode.FIELD_TYPE));
        setPeriodeDateDebut(statement.dbReadNumeric(SFPeriode.FIELD_DATEDEBUT));
        setPeriodeDateFin(statement.dbReadNumeric(SFPeriode.FIELD_DATEFIN));

        setIdConjoints(statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINTS));
        setIdMFConjoint1(statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINT1));
        setIdMFConjoint2(statement.dbReadNumeric(SFConjoint.FIELD_IDCONJOINT2));

        setIdRelation(statement.dbReadNumeric(SFRelationConjoint.FIELD_IDRELATIONCONJOINT));
        setRelationDateDebut(statement.dbReadNumeric(SFRelationConjoint.FIELD_DATEDEBUT));
        setRelationDateFin(statement.dbReadNumeric(SFRelationConjoint.FIELD_DATEFIN));
        setRelationType(statement.dbReadNumeric(SFRelationConjoint.FIELD_TYPERELATION));

        setIdConjointT(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_ID_TIERS));
        setSexeConjoint(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_CS_SEXE));
        setConjointTDateNaissance(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_TIERS_DATE_NAISSANCE));

        setConjointMFDateNaissance(statement
                .dbReadNumeric(REListerEcheanceRenteJoinMembresFamilleManager.ALIAS_CONJOINT_MEMBRE_FAMILLE_DATE_NAISSANCE));

        setCsTypeInfoComplementaire(statement.dbReadNumeric(REDemandeRente.FIELDNAME_CS_ETAT));

        setCsTypeInfoComplementaire(statement.dbReadNumeric(PRInfoCompl.FIELDNAME_TYPE_INFO_COMPL));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        _addError(statement.getTransaction(), "interdit d'ajouter");
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // ON NE FAIT RIEN
    }

    public String getCodeNationalite() {
        return codeNationalite;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getConjointMFDateNaissance() {
        return ConjointMFDateNaissance;
    }

    public String getConjointTDateNaissance() {
        return ConjointTDateNaissance;
    }

    public String getCs1() {
        return cs1;
    }

    public String getCs2() {
        return cs2;
    }

    public String getCs3() {
        return cs3;
    }

    public String getCs4() {
        return cs4;
    }

    public String getCs5() {
        return cs5;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public String getCsEtatDemandeRente() {
        return csEtatDemandeRente;
    }

    public String getCsSexe() {
        return csSexe;
    }

    public String getCsTypeInfoComplementaire() {
        return csTypeInfoComplementaire;
    }

    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateEcheanceRenteAccordee() {
        return dateEcheanceRenteAccordee;
    }

    public String getDateFinDroit() {
        return dateFinDroit;
    }

    /**
     * La date de naissance à le format original AAAAMMJJ. Elle est formatée au format JJxMMxAAAA avant d'être retournée
     * 
     * @return la date de naissance au format JJxMMxAAAA
     */
    public String getDateNaissance() {
        return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateNaissance);
    }

    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    public String getDegreInvalidite() {
        return degreInvalidite;
    }

    public String getEtat() {
        return etat;
    }

    public String getFractionRente() {
        return fractionRente;
    }

    public String getIdConjoints() {
        return idConjoints;
    }

    public String getIdConjointT() {
        return idConjointT;
    }

    public String getIdInfoCompta() {
        return idInfoCompta;
    }

    public String getIdMembre() {
        return idMembre;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public String getIdMFConjoint1() {
        return idMFConjoint1;
    }

    public String getIdMFConjoint2() {
        return idMFConjoint2;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    public String getMontantPrestation() {
        return montantPrestation;
    }

    /**
     * @return the motifEcheance
     */
    public final REMotifEcheance getMotifEcheance() {
        return motifEcheance;
    }

    public String getMotifLettre() {
        return motifLettre;
    }

    public String getMotifListe() {
        return motifListe;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPeriodeDateDebut() {
        return periodeDateDebut;
    }

    public String getPeriodeDateFin() {
        return periodeDateFin;
    }

    public String getPeriodeType() {
        return periodeType;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRelationDateDebut() {
        return relationDateDebut;
    }

    public String getRelationDateFin() {
        return relationDateFin;
    }

    public String getRelationType() {
        return relationType;
    }

    public String getSexeConjoint() {
        return sexeConjoint;
    }

    public boolean isDejaTraite() {
        return isDejaTraite;
    }

    public boolean isGenererLettre() {
        return GenererLettre;
    }

    public boolean isListerInfoEntity() {
        return ListerInfoEntity;
    }

    public void setCodeNationalite(String codeNationalite) {
        this.codeNationalite = codeNationalite;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setConjointMFDateNaissance(String conjointMFDateNaissance) {
        ConjointMFDateNaissance = conjointMFDateNaissance;
    }

    public void setConjointTDateNaissance(String conjointTDateNaissance) {
        ConjointTDateNaissance = conjointTDateNaissance;
    }

    public void setCs1(String cs1) {
        this.cs1 = cs1;
    }

    public void setCs2(String cs2) {
        this.cs2 = cs2;
    }

    private void setCs3(String cs3) {
        this.cs3 = cs3;
    }

    public void setCs4(String cs4) {
        this.cs4 = cs4;
    }

    public void setCs5(String cs5) {
        this.cs5 = cs5;
    }

    private void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public void setCsEtatDemandeRente(String csEtatDemandeRente) {
        this.csEtatDemandeRente = csEtatDemandeRente;
    }

    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    public void setCsTypeInfoComplementaire(String csTypeInfoComplementaire) {
        this.csTypeInfoComplementaire = csTypeInfoComplementaire;
    }

    public void setDateDebutDroit(String dateDebutDroit) {
        this.dateDebutDroit = dateDebutDroit;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateEcheanceRenteAccordee(String dateEcheanceRenteAccordee) {
        this.dateEcheanceRenteAccordee = dateEcheanceRenteAccordee;
    }

    private void setDateFinDroit(String dateFinDroit) {
        this.dateFinDroit = dateFinDroit;
    }

    /**
     * Date de naissance au format attendu AAAMMJJ
     * 
     * @param dateNaissance
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRevocationAjournement(String dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    public void setDegreInvalidite(String degreInvalidite) {
        this.degreInvalidite = degreInvalidite;
    }

    public void setDejaTraite(boolean isDejaTraite) {
        this.isDejaTraite = isDejaTraite;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setGenererLettre(boolean genererLettre) {
        GenererLettre = genererLettre;
    }

    public void setIdConjoints(String idConjoints) {
        this.idConjoints = idConjoints;
    }

    public void setIdConjointT(String idConjointT) {
        this.idConjointT = idConjointT;
    }

    public void setIdInfoCompta(String idInfoCompta) {
        this.idInfoCompta = idInfoCompta;
    }

    public void setIdMembre(String idMembre) {
        this.idMembre = idMembre;
    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIdMFConjoint1(String idMFConjoint1) {
        this.idMFConjoint1 = idMFConjoint1;
    }

    public void setIdMFConjoint2(String idMFConjoint2) {
        this.idMFConjoint2 = idMFConjoint2;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public void setIdRenteAccordee(String idRenteAccordee) {
        this.idRenteAccordee = idRenteAccordee;
    }

    public void setIdTiers(String idTiersBeneficaire) {
        idTiers = idTiersBeneficaire;
    }

    public void setIsPrestationBloquee(Boolean isPrestationBloquee) {
        this.isPrestationBloquee = isPrestationBloquee;
    }

    public void setListerInfoEntity(boolean listerInfoEntity) {
        ListerInfoEntity = listerInfoEntity;
    }

    public void setMontantPrestation(String montantPrestation) {
        this.montantPrestation = montantPrestation;
    }

    /**
     * @param motifEcheance
     *            the motifEcheance to set
     */
    public final void setMotifEcheance(REMotifEcheance motifEcheance) {
        this.motifEcheance = motifEcheance;
    }

    public void setMotifLettre(String motifLettre) {
        this.motifLettre = motifLettre;
    }

    public void setMotifListe(String motifListe) {
        this.motifListe = motifListe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPeriodeDateDebut(String periodeDateDebut) {
        this.periodeDateDebut = periodeDateDebut;
    }

    public void setPeriodeDateFin(String periodeDateFin) {
        this.periodeDateFin = periodeDateFin;
    }

    public void setPeriodeType(String periodeType) {
        this.periodeType = periodeType;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setRelationDateDebut(String relationDateDebut) {
        this.relationDateDebut = relationDateDebut;
    }

    public void setRelationDateFin(String relationDateFin) {
        this.relationDateFin = relationDateFin;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public void setSexeConjoint(String sexeConjoint) {
        this.sexeConjoint = sexeConjoint;
    }

    public List<REListerEcheanceRenteJoinMembresFamille> getListeEcheanceLiees() {
        return listeEcheanceLiees;
    }

    public void setListeEcheanceLiees(List<REListerEcheanceRenteJoinMembresFamille> listeEcheanceLiees) {
        this.listeEcheanceLiees = listeEcheanceLiees;
    }

    public boolean isHasSameIdAdressePaiement() {
        return hasSameIdAdressePaiement;
    }

    public void setHasSameIdAdressePaiement(boolean hasSameIdAdressePaiement) {
        this.hasSameIdAdressePaiement = hasSameIdAdressePaiement;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiement(String idAdressePaiement) {
        idTiersAdressePaiement = idAdressePaiement;
    }

}
