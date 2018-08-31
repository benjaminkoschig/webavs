package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.vulpecula.ws.bean.StatusAnnonceTravailleurEbu;

/**
 * @since eBMS 1.0
 */
public class TravailleurEbuSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 6158599981518446200L;

    private String id;

    private String idTravailleur;
    private String idPosteTravail;
    private String nom;
    private String prenom;
    private String adresseTitre;
    private String adresseDescription1;
    private String adresseDescription2;
    private String adresseRue;
    private String adresseRuenumero;
    private String adresseNpa;
    private String adresseLocalite;
    private String adresseCasePostale;
    private String adressePays;
    private String telephone;
    private String nationalite;
    private String csEtatCivil;
    private String dateNaissance;
    private String nss;
    private String permisSejourCategorie;
    private String permisSejourNumero;
    private String nombreEnfant;
    private String compteBancaireNom;
    private String compteBancaireLocalite;
    private String compteBancaireIban;
    private String compteBancaireLocaliteId;
    private String numeroEntreprise;
    private String nomEntreprise;
    private String profession;
    private String dateDebutActivite;
    private String dateFinActivite;
    private String sexe;
    private String tauxActivite;
    private String dateTauxActivite;
    private Boolean dureeContratDeterminee = false;
    private String dureeContratDateFin;
    private String csCodeProfessionnel;
    private String contratCollectifAssureur;
    private Boolean contratCollectifAssuranceCombinee = false;
    private String dateInscription;
    private String convention;
    private Boolean traite = false;
    private String nomUpper;
    private String prenomUpper;
    private String idEmployeur;
    private String salaire;
    private String typeSalaire;
    private String idDecompte;
    private String correlationId;
    private String posteCorrelationId;
    private Boolean modification = false;
    private String tiersStatus;
    private String posteStatus;
    private String travailleurStatus;
    private String banqueStatus;
    private String status;

    // private Boolean isNewAnnonce;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getIdDecompte() {
        return idDecompte;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public Boolean getTraite() {
        return status.equals(StatusAnnonceTravailleurEbu.TRAITE.getValue())
                || status.equals(StatusAnnonceTravailleurEbu.REFUSE.getValue())
                || status.equals(StatusAnnonceTravailleurEbu.AUTO.getValue());
    }

    public void setTraite(Boolean traite) {
        this.traite = traite;
    }

    public String getNomUpper() {
        return nomUpper;
    }

    public String getDateDebutActivite() {
        return dateDebutActivite;
    }

    public void setDateDebutActivite(String dateDebutActivite) {
        this.dateDebutActivite = dateDebutActivite;
    }

    public String getDateFinActivite() {
        return dateFinActivite;
    }

    public void setDateFinActivite(String dateFinActivite) {
        this.dateFinActivite = dateFinActivite;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setNomUpper(String nomUpper) {
        this.nomUpper = nomUpper;
    }

    public String getPrenomUpper() {
        return prenomUpper;
    }

    public void setPrenomUpper(String prenomUpper) {
        this.prenomUpper = prenomUpper;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public String getTypeSalaire() {
        return typeSalaire;
    }

    public void setTypeSalaire(String typeSalaire) {
        this.typeSalaire = typeSalaire;
    }

    /**
     * @return the idTravailleur
     */
    public String getIdTravailleur() {
        return idTravailleur;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return the adresseTitre
     */
    public String getAdresseTitre() {
        return adresseTitre;
    }

    /**
     * @return the adresseDescription1
     */
    public String getAdresseDescription1() {
        return adresseDescription1;
    }

    /**
     * @return the adresseDescription2
     */
    public String getAdresseDescription2() {
        return adresseDescription2;
    }

    /**
     * @return the adresseRue
     */
    public String getAdresseRue() {
        return adresseRue;
    }

    /**
     * @return the adresseRuenumero
     */
    public String getAdresseRuenumero() {
        return adresseRuenumero;
    }

    /**
     * @return the adresseNpa
     */
    public String getAdresseNpa() {
        return adresseNpa;
    }

    /**
     * @return the adresseLocalite
     */
    public String getAdresseLocalite() {
        return adresseLocalite;
    }

    /**
     * @return the adresseCasePostale
     */
    public String getAdresseCasePostale() {
        return adresseCasePostale;
    }

    /**
     * @return the adressePays
     */
    public String getAdressePays() {
        return adressePays;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @return the nationalite
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return the csEtatCivil
     */
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * @return the permisSejourCategorie
     */
    public String getPermisSejourCategorie() {
        return permisSejourCategorie;
    }

    /**
     * @return the permisSejourNumero
     */
    public String getPermisSejourNumero() {
        return permisSejourNumero;
    }

    /**
     * @return the nombreEnfant
     */
    public String getNombreEnfant() {
        return nombreEnfant;
    }

    /**
     * @return the compteBancaireNom
     */
    public String getCompteBancaireNom() {
        return compteBancaireNom;
    }

    /**
     * @return the compteBancaireLocalite
     */
    public String getCompteBancaireLocalite() {
        return compteBancaireLocalite;
    }

    /**
     * @return the compteBancaireIban
     */
    public String getCompteBancaireIban() {
        return compteBancaireIban;
    }

    /**
     * @return the numeroEntreprise
     */
    public String getNumeroEntreprise() {
        return numeroEntreprise;
    }

    /**
     * @return the nomEntreprise
     */
    public String getNomEntreprise() {
        return nomEntreprise;
    }

    /**
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @return the tauxActivite
     */
    public String getTauxActivite() {
        return tauxActivite;
    }

    /**
     * @return the dateTauxActivite
     */
    public String getDateTauxActivite() {
        return dateTauxActivite;
    }

    /**
     * @return the dureeContratDeterminee
     */
    public Boolean getDureeContratDeterminee() {
        return dureeContratDeterminee;
    }

    /**
     * @return the dureeContratDateFin
     */
    public String getDureeContratDateFin() {
        return dureeContratDateFin;
    }

    /**
     * @return the csCodeProfessionnel
     */
    public String getCsCodeProfessionnel() {
        return csCodeProfessionnel;
    }

    /**
     * @return the contratCollectifAssureur
     */
    public String getContratCollectifAssureur() {
        return contratCollectifAssureur;
    }

    /**
     * @return the contratCollectifAssuranceCombinee
     */
    public Boolean getContratCollectifAssuranceCombinee() {
        return contratCollectifAssuranceCombinee;
    }

    /**
     * @return the dateInscription
     */
    public String getDateInscription() {
        return dateInscription;
    }

    /**
     * @return the convention
     */
    public String getConvention() {
        return convention;
    }

    /**
     * @param idTravailleur the idTravailleur to set
     */
    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @param adresseTitre the adresseTitre to set
     */
    public void setAdresseTitre(String adresseTitre) {
        this.adresseTitre = adresseTitre;
    }

    /**
     * @param adresseDescription1 the adresseDescription1 to set
     */
    public void setAdresseDescription1(String adresseDescription1) {
        this.adresseDescription1 = adresseDescription1;
    }

    /**
     * @param adresseDescription2 the adresseDescription2 to set
     */
    public void setAdresseDescription2(String adresseDescription2) {
        this.adresseDescription2 = adresseDescription2;
    }

    /**
     * @param adresseRue the adresseRue to set
     */
    public void setAdresseRue(String adresseRue) {
        this.adresseRue = adresseRue;
    }

    /**
     * @param adresseRuenumero the adresseRuenumero to set
     */
    public void setAdresseRuenumero(String adresseRuenumero) {
        this.adresseRuenumero = adresseRuenumero;
    }

    /**
     * @param adresseNpa the adresseNpa to set
     */
    public void setAdresseNpa(String adresseNpa) {
        this.adresseNpa = adresseNpa;
    }

    /**
     * @param adresseLocalite the adresseLocalite to set
     */
    public void setAdresseLocalite(String adresseLocalite) {
        this.adresseLocalite = adresseLocalite;
    }

    /**
     * @param adresseCasePostale the adresseCasePostale to set
     */
    public void setAdresseCasePostale(String adresseCasePostale) {
        this.adresseCasePostale = adresseCasePostale;
    }

    /**
     * @param adressePays the adressePays to set
     */
    public void setAdressePays(String adressePays) {
        this.adressePays = adressePays;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @param nationalite the nationalite to set
     */
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    /**
     * @param csEtatCivil the csEtatCivil to set
     */
    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    /**
     * @param dateNaissance the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param nss the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * @param permisSejourCategorie the permisSejourCategorie to set
     */
    public void setPermisSejourCategorie(String permisSejourCategorie) {
        this.permisSejourCategorie = permisSejourCategorie;
    }

    /**
     * @param permisSejourNumero the permisSejourNumero to set
     */
    public void setPermisSejourNumero(String permisSejourNumero) {
        this.permisSejourNumero = permisSejourNumero;
    }

    /**
     * @param nombreEnfant the nombreEnfant to set
     */
    public void setNombreEnfant(String nombreEnfant) {
        this.nombreEnfant = nombreEnfant;
    }

    /**
     * @param compteBancaireNom the compteBancaireNom to set
     */
    public void setCompteBancaireNom(String compteBancaireNom) {
        this.compteBancaireNom = compteBancaireNom;
    }

    /**
     * @param compteBancaireLocalite the compteBancaireLocalite to set
     */
    public void setCompteBancaireLocalite(String compteBancaireLocalite) {
        this.compteBancaireLocalite = compteBancaireLocalite;
    }

    /**
     * @param compteBancaireIban the compteBancaireIban to set
     */
    public void setCompteBancaireIban(String compteBancaireIban) {
        this.compteBancaireIban = compteBancaireIban;
    }

    /**
     * @param numeroEntreprise the numeroEntreprise to set
     */
    public void setNumeroEntreprise(String numeroEntreprise) {
        this.numeroEntreprise = numeroEntreprise;
    }

    /**
     * @param nomEntreprise the nomEntreprise to set
     */
    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    /**
     * @param profession the profession to set
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    /**
     * @param tauxActivite the tauxActivite to set
     */
    public void setTauxActivite(String tauxActivite) {
        this.tauxActivite = tauxActivite;
    }

    /**
     * @param dateTauxActivite the dateTauxActivite to set
     */
    public void setDateTauxActivite(String dateTauxActivite) {
        this.dateTauxActivite = dateTauxActivite;
    }

    /**
     * @param dureeContratDeterminee the dureeContratDeterminee to set
     */
    public void setDureeContratDeterminee(Boolean dureeContratDeterminee) {
        this.dureeContratDeterminee = dureeContratDeterminee;
    }

    /**
     * @param dureeContratDateFin the dureeContratDateFin to set
     */
    public void setDureeContratDateFin(String dureeContratDateFin) {
        this.dureeContratDateFin = dureeContratDateFin;
    }

    /**
     * @param csCodeProfessionnel the csCodeProfessionnel to set
     */
    public void setCsCodeProfessionnel(String csCodeProfessionnel) {
        this.csCodeProfessionnel = csCodeProfessionnel;
    }

    /**
     * @param contratCollectifAssureur the contratCollectifAssureur to set
     */
    public void setContratCollectifAssureur(String contratCollectifAssureur) {
        this.contratCollectifAssureur = contratCollectifAssureur;
    }

    /**
     * @param contratCollectifAssuranceCombinee the contratCollectifAssuranceCombinee to set
     */
    public void setContratCollectifAssuranceCombinee(Boolean contratCollectifAssuranceCombinee) {
        this.contratCollectifAssuranceCombinee = contratCollectifAssuranceCombinee;
    }

    /**
     * @param dateInscription the dateInscription to set
     */
    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    /**
     * @param convention the convention to set
     */
    public void setConvention(String convention) {
        this.convention = convention;
    }

    public Boolean getModification() {
        return modification;
    }

    public void setModification(Boolean modification) {
        this.modification = modification;
    }

    public String getTiersStatus() {
        return tiersStatus;
    }

    public void setTiersStatus(String tiersStatus) {
        this.tiersStatus = tiersStatus;
    }

    public String getPosteStatus() {
        return posteStatus;
    }

    public void setPosteStatus(String posteStatus) {
        this.posteStatus = posteStatus;
    }

    public String getTravailleurStatus() {
        return travailleurStatus;
    }

    public void setTravailleurStatus(String travailleurStatus) {
        this.travailleurStatus = travailleurStatus;
    }

    public String getBanqueStatus() {
        return banqueStatus;
    }

    public void setBanqueStatus(String banqueStatus) {
        this.banqueStatus = banqueStatus;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getCompteBancaireLocaliteId() {
        return compteBancaireLocaliteId;
    }

    public void setCompteBancaireLocaliteId(String compteBancaireLocaliteId) {
        this.compteBancaireLocaliteId = compteBancaireLocaliteId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // public Boolean getIsNewAnnonce() {
    // return isNewAnnonce;
    // }
    //
    // public void setIsNewAnnonce(Boolean isNewAnnonce) {
    // this.isNewAnnonce = isNewAnnonce;
    // }

}
