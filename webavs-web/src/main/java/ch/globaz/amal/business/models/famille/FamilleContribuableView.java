package ch.globaz.amal.business.models.famille;

/**
 *
 */
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author DHI
 *
 */
public class FamilleContribuableView extends JadeComplexModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String anneeHistorique = null;
    private Boolean codeActif = null;
    private String codeTraitement = null;
    private String codeTraitementDossier = null;
    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String codeTraitementDossierCodeAJAX = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String codeTraitementDossierLibelleAJAX = null;
    private String dateEnvoi = null;
    private String dateNaissance = null;
    private String dateNaissanceFamille = null;
    private String dateRecepDemande = null;
    private String debutDroit = null;
    private String detailFamilleId = null;
    private String familleId = null;
    private String finDefinitive = null;
    private String finDroit = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String idDetailFamilleAJAX = null;
    private Boolean isContribuable = null;
    private Boolean isContribuableActif = null;
    private String montantContribution = null;
    /**
     *
     * Utilisé pour afficher le montant total dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String montantTotalSubsideAJAX = null;
    private String noModeles = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesAbreviationAJAX = null;
    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesCodeAJAX = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesLibelleAJAX = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesTemporaireAbreviationAJAX = null;
    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesTemporaireCodeAJAX = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String noModelesTemporaireLibelleAJAX = null;
    private String nomPrenom = null;

    private String numAvsActuel = null;

    private String numContribuableActuel = null;

    private String pereMereEnfant = null;

    private Boolean refus = null;

    private String sexe = null;

    private String supplExtra = null;
    private String typeDemande = null;
    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String typeDemandeCodeAJAX = null;
    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    private String typeDemandeLibelleAJAX = null;

    public FamilleContribuableView() {
        super();
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the codeTraitement
     */
    public String getCodeTraitement() {
        return codeTraitement;
    }

    /**
     * @return the codeTraitementDossier
     */
    public String getCodeTraitementDossier() {
        return codeTraitementDossier;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getCodeTraitementDossierCodeAJAX() {
        return codeTraitementDossierCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getCodeTraitementDossierLibelleAJAX() {
        return codeTraitementDossierLibelleAJAX;
    }

    /**
     * @return the dateEnvoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the dateNaissanceFamille
     */
    public String getDateNaissanceFamille() {
        return dateNaissanceFamille;
    }

    /**
     * @return the dateRecepDemande
     */
    public String getDateRecepDemande() {
        return dateRecepDemande;
    }

    /**
     * @return the debutDroit
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * @return the detailFamilleId
     */
    public String getDetailFamilleId() {
        return detailFamilleId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    public String getFamilleId() {
        return familleId;
    }

    /**
     * @return the finDefinitive
     */
    public String getFinDefinitive() {
        return finDefinitive;
    }

    /**
     * @return the finDroit
     */
    public String getFinDroit() {
        return finDroit;
    }

    @Override
    public String getId() {
        return familleId;
    }

    public String getIdDetailFamilleAJAX() {
        return idDetailFamilleAJAX;
    }

    public Boolean getIsContribuable() {
        return isContribuable;
    }

    public Boolean getIsContribuableActif() {
        return isContribuableActif;
    }

    /**
     * @return the montantContribution
     */
    public String getMontantContribution() {
        return montantContribution;
    }

    /**
     *
     * Utilisé pour afficher le montant total dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getMontantTotalSubsideAJAX() {
        return montantTotalSubsideAJAX;
    }

    /**
     * @return the noModeles
     */
    public String getNoModeles() {
        return noModeles;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getNoModelesAbreviationAJAX() {
        return noModelesAbreviationAJAX;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getNoModelesCodeAJAX() {
        return noModelesCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getNoModelesLibelleAJAX() {
        return noModelesLibelleAJAX;
    }

    public String getNoModelesTemporaireAbreviationAJAX() {
        return noModelesTemporaireAbreviationAJAX;
    }

    public String getNoModelesTemporaireCodeAJAX() {
        return noModelesTemporaireCodeAJAX;
    }

    public String getNoModelesTemporaireLibelleAJAX() {
        return noModelesTemporaireLibelleAJAX;
    }

    /**
     * @return the nomPrenom
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return the numAvsActuel
     */
    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    /**
     * @return the numContribuableActuel
     */
    public String getNumContribuableActuel() {
        return numContribuableActuel;
    }

    /**
     * @return the pereMereEnfant
     */
    public String getPereMereEnfant() {
        return pereMereEnfant;
    }

    /**
     * @return the refus
     */
    public Boolean getRefus() {
        return refus;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the supplExtra
     */
    public String getSupplExtra() {
        return supplExtra;
    }

    /**
     * @return the typeDemande
     */
    public String getTypeDemande() {
        return typeDemande;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getTypeDemandeCodeAJAX() {
        return typeDemandeCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public String getTypeDemandeLibelleAJAX() {
        return typeDemandeLibelleAJAX;
    }

    public boolean isCodeActif() {
        return codeActif;
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    public void setCodeActif(boolean codeActif) {
        this.codeActif = codeActif;
    }

    /**
     * @param codeTraitement
     *            the codeTraitement to set
     */
    public void setCodeTraitement(String codeTraitement) {
        this.codeTraitement = codeTraitement;
    }

    /**
     * @param codeTraitementDossier
     *            the codeTraitementDossier to set
     */
    public void setCodeTraitementDossier(String codeTraitementDossier) {
        this.codeTraitementDossier = codeTraitementDossier;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setCodeTraitementDossierCodeAJAX(String codeTraitementDossierCodeAJAX) {
        this.codeTraitementDossierCodeAJAX = codeTraitementDossierCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setCodeTraitementDossierLibelleAJAX(String codeTraitementDossierLibelleAJAX) {
        this.codeTraitementDossierLibelleAJAX = codeTraitementDossierLibelleAJAX;
    }

    /**
     * @param dateEnvoi
     *            the dateEnvoi to set
     */
    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param dateNaissanceFamille
     *            the dateNaissanceFamille to set
     */
    public void setDateNaissanceFamille(String dateNaissanceFamille) {
        this.dateNaissanceFamille = dateNaissanceFamille;
    }

    /**
     * @param dateRecepDemande
     *            the dateRecepDemande to set
     */
    public void setDateRecepDemande(String dateRecepDemande) {
        this.dateRecepDemande = dateRecepDemande;
    }

    /**
     * @param debutDroit
     *            the debutDroit to set
     */
    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    /**
     * @param detailFamilleId
     *            the detailFamilleId to set
     */
    public void setDetailFamilleId(String detailFamilleId) {
        this.detailFamilleId = detailFamilleId;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    public void setFamilleId(String id) {
        familleId = id;

    }

    /**
     * @param finDefinitive
     *            the finDefinitive to set
     */
    public void setFinDefinitive(String finDefinitive) {
        this.finDefinitive = finDefinitive;
    }

    /**
     * @param finDroit
     *            the finDroit to set
     */
    public void setFinDroit(String finDroit) {
        this.finDroit = finDroit;
    }

    @Override
    public void setId(String id) {
        familleId = id;
    }

    public void setIdDetailFamilleAJAX(String idDetailFamilleAJAX) {
        this.idDetailFamilleAJAX = idDetailFamilleAJAX;
    }

    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    public void setIsContribuableActif(Boolean isContribuableActif) {
        this.isContribuableActif = isContribuableActif;
    }

    /**
     * @param montantContribution
     *            the montantContribution to set
     */
    public void setMontantContribution(String montantContribution) {
        this.montantContribution = montantContribution;
    }

    /**
     *
     * Utilisé pour afficher le montant total dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setMontantTotalSubsideAJAX(String montantTotalSubsideAJAX) {
        this.montantTotalSubsideAJAX = montantTotalSubsideAJAX;
    }

    /**
     * @param noModeles
     *            the noModeles to set
     */
    public void setNoModeles(String noModeles) {
        this.noModeles = noModeles;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setNoModelesAbreviationAJAX(String noModelesAbreviationAJAX) {
        this.noModelesAbreviationAJAX = noModelesAbreviationAJAX;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setNoModelesCodeAJAX(String noModelesCodeAJAX) {
        this.noModelesCodeAJAX = noModelesCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setNoModelesLibelleAJAX(String noModelesLibelleAJAX) {
        this.noModelesLibelleAJAX = noModelesLibelleAJAX;
    }

    public void setNoModelesTemporaireAbreviationAJAX(String noModelesTemporaireAbreviationAJAX) {
        this.noModelesTemporaireAbreviationAJAX = noModelesTemporaireAbreviationAJAX;
    }

    public void setNoModelesTemporaireCodeAJAX(String noModelesTemporaireCodeAJAX) {
        this.noModelesTemporaireCodeAJAX = noModelesTemporaireCodeAJAX;
    }

    public void setNoModelesTemporaireLibelleAJAX(String noModelesTemporaireLibelleAJAX) {
        this.noModelesTemporaireLibelleAJAX = noModelesTemporaireLibelleAJAX;
    }

    /**
     * @param nomPrenom
     *            the nomPrenom to set
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * @param numAvsActuel
     *            the numAvsActuel to set
     */
    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    /**
     * @param numContribuableActuel
     *            the numContribuableActuel to set
     */
    public void setNumContribuableActuel(String numContribuableActuel) {
        this.numContribuableActuel = numContribuableActuel;
    }

    /**
     * @param pereMereEnfant
     *            the pereMereEnfant to set
     */
    public void setPereMereEnfant(String pereMereEnfant) {
        this.pereMereEnfant = pereMereEnfant;
    }

    /**
     * @param refus
     *            the refus to set
     */
    public void setRefus(Boolean refus) {
        this.refus = refus;
    }

    /**
     * @param sexe
     *            the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    /**
     * @param supplExtra
     *            the supplExtra to set
     */
    public void setSupplExtra(String supplExtra) {
        this.supplExtra = supplExtra;
    }

    /**
     * @param typeDemande
     *            the typeDemande to set
     */
    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    /**
     *
     * Utilisé pour afficher le code du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setTypeDemandeCodeAJAX(String typeDemandeCodeAJAX) {
        this.typeDemandeCodeAJAX = typeDemandeCodeAJAX;
    }

    /**
     *
     * Utilisé pour afficher le libelle du CS dans le tableau AJAX contribuableRevenu.jsp
     * </br>
     * </br>
     * <b>CE CHAMP N'EXISTE PAS EN DB !!!</b>
     */
    public void setTypeDemandeLibelleAJAX(String typeDemandeLibelleAJAX) {
        this.typeDemandeLibelleAJAX = typeDemandeLibelleAJAX;
    }

}
