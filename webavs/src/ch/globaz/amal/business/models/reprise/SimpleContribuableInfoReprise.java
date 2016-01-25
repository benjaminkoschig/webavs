/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleContribuableInfoReprise extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String casePostale = null;
    private String civilite = null;
    private String commune = null;
    private String dateNaissanceActuelle = null;
    private String dateNaissanceCommune = null;
    private String dateNaissancePrecedente = null;
    private String idContribuable = null;
    private String idContribuableInfo = null;
    private String ligneAdresse1 = null;
    private String ligneAdresse2 = null;
    private String ligneAdresse3 = null;
    private String nnss = null;
    private String nomDeFamille = null;
    private String nomDeFamilleMajuscules = null;
    private String nomDeRue = null;
    private String numeroContribuableActuel = null;
    private String numeroContribuableActuelFormate = null;
    private String numeroContribuableAncienFormatAncienneValeur = null;
    private String numeroContribuableAncienFormatNouvelleValeur = null;
    private String numeroContribuableNouveauFormatAncienneValeur = null;
    private String numeroContribuableNouveauFormatNouvelleValeur = null;
    private String numeroDeRue = null;
    private String numeroPostal = null;
    private String prenom = null;
    private String prenomMajuscules = null;

    /**
	 * 
	 */
    public SimpleContribuableInfoReprise() {
        super();
    }

    /**
     * @return the casePostale
     */
    public String getCasePostale() {
        return casePostale;
    }

    /**
     * @return the civilite
     */
    public String getCivilite() {
        return civilite;
    }

    /**
     * @return the commune
     */
    public String getCommune() {
        return commune;
    }

    /**
     * @return the dateNaissanceActuelle
     */
    public String getDateNaissanceActuelle() {
        return dateNaissanceActuelle;
    }

    /**
     * @return the dateNaissanceCommune
     */
    public String getDateNaissanceCommune() {
        return dateNaissanceCommune;
    }

    /**
     * @return the dateNaissancePrecedente
     */
    public String getDateNaissancePrecedente() {
        return dateNaissancePrecedente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idContribuableInfo;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idContribuableInfo
     */
    public String getIdContribuableInfo() {
        return idContribuableInfo;
    }

    /**
     * @return the ligneAdresse1
     */
    public String getLigneAdresse1() {
        return ligneAdresse1;
    }

    /**
     * @return the ligneAdresse2
     */
    public String getLigneAdresse2() {
        return ligneAdresse2;
    }

    /**
     * @return the ligneAdresse3
     */
    public String getLigneAdresse3() {
        return ligneAdresse3;
    }

    /**
     * @return the nnss
     */
    public String getNnss() {
        return nnss;
    }

    /**
     * @return the nomDeFamille
     */
    public String getNomDeFamille() {
        return nomDeFamille;
    }

    /**
     * @return the nomDeFamilleMajuscules
     */
    public String getNomDeFamilleMajuscules() {
        return nomDeFamilleMajuscules;
    }

    /**
     * @return the nomDeRue
     */
    public String getNomDeRue() {
        return nomDeRue;
    }

    /**
     * @return the numeroContribuableActuel
     */
    public String getNumeroContribuableActuel() {
        return numeroContribuableActuel;
    }

    /**
     * @return the numeroContribuableActuelFormate
     */
    public String getNumeroContribuableActuelFormate() {
        return numeroContribuableActuelFormate;
    }

    /**
     * @return the numeroContribuableAncienFormatAncienneValeur
     */
    public String getNumeroContribuableAncienFormatAncienneValeur() {
        return numeroContribuableAncienFormatAncienneValeur;
    }

    /**
     * @return the numeroContribuableAncienFormatNouvelleValeur
     */
    public String getNumeroContribuableAncienFormatNouvelleValeur() {
        return numeroContribuableAncienFormatNouvelleValeur;
    }

    /**
     * @return the numeroContribuableNouveauFormatAncienneValeur
     */
    public String getNumeroContribuableNouveauFormatAncienneValeur() {
        return numeroContribuableNouveauFormatAncienneValeur;
    }

    /**
     * @return the numeroContribuableNouveauFormatNouvelleValeur
     */
    public String getNumeroContribuableNouveauFormatNouvelleValeur() {
        return numeroContribuableNouveauFormatNouvelleValeur;
    }

    /**
     * @return the numeroDeRue
     */
    public String getNumeroDeRue() {
        return numeroDeRue;
    }

    /**
     * @return the numeroPostal
     */
    public String getNumeroPostal() {
        return numeroPostal;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return the prenomMajuscules
     */
    public String getPrenomMajuscules() {
        return prenomMajuscules;
    }

    /**
     * @param casePostale
     *            the casePostale to set
     */
    public void setCasePostale(String casePostale) {
        this.casePostale = casePostale;
    }

    /**
     * @param civilite
     *            the civilite to set
     */
    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    /**
     * @param commune
     *            the commune to set
     */
    public void setCommune(String commune) {
        this.commune = commune;
    }

    /**
     * @param dateNaissanceActuelle
     *            the dateNaissanceActuelle to set
     */
    public void setDateNaissanceActuelle(String dateNaissanceActuelle) {
        this.dateNaissanceActuelle = dateNaissanceActuelle;
    }

    /**
     * @param dateNaissanceCommune
     *            the dateNaissanceCommune to set
     */
    public void setDateNaissanceCommune(String dateNaissanceCommune) {
        this.dateNaissanceCommune = dateNaissanceCommune;
    }

    /**
     * @param dateNaissancePrecedente
     *            the dateNaissancePrecedente to set
     */
    public void setDateNaissancePrecedente(String dateNaissancePrecedente) {
        this.dateNaissancePrecedente = dateNaissancePrecedente;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idContribuableInfo = id;
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idContribuableInfo
     *            the idContribuableInfo to set
     */
    public void setIdContribuableInfo(String idContribuableInfo) {
        this.idContribuableInfo = idContribuableInfo;
    }

    /**
     * @param ligneAdresse1
     *            the ligneAdresse1 to set
     */
    public void setLigneAdresse1(String ligneAdresse1) {
        this.ligneAdresse1 = ligneAdresse1;
    }

    /**
     * @param ligneAdresse2
     *            the ligneAdresse2 to set
     */
    public void setLigneAdresse2(String ligneAdresse2) {
        this.ligneAdresse2 = ligneAdresse2;
    }

    /**
     * @param ligneAdresse3
     *            the ligneAdresse3 to set
     */
    public void setLigneAdresse3(String ligneAdresse3) {
        this.ligneAdresse3 = ligneAdresse3;
    }

    /**
     * @param nnss
     *            the nnss to set
     */
    public void setNnss(String nnss) {
        this.nnss = nnss;
    }

    /**
     * @param nomDeFamille
     *            the nomDeFamille to set
     */
    public void setNomDeFamille(String nomDeFamille) {
        this.nomDeFamille = nomDeFamille;
    }

    /**
     * @param nomDeFamilleMajuscules
     *            the nomDeFamilleMajuscules to set
     */
    public void setNomDeFamilleMajuscules(String nomDeFamilleMajuscules) {
        this.nomDeFamilleMajuscules = nomDeFamilleMajuscules;
    }

    /**
     * @param nomDeRue
     *            the nomDeRue to set
     */
    public void setNomDeRue(String nomDeRue) {
        this.nomDeRue = nomDeRue;
    }

    /**
     * @param numeroContribuableActuel
     *            the numeroContribuableActuel to set
     */
    public void setNumeroContribuableActuel(String numeroContribuableActuel) {
        this.numeroContribuableActuel = numeroContribuableActuel;
    }

    /**
     * @param numeroContribuableActuelFormate
     *            the numeroContribuableActuelFormate to set
     */
    public void setNumeroContribuableActuelFormate(String numeroContribuableActuelFormate) {
        this.numeroContribuableActuelFormate = numeroContribuableActuelFormate;
    }

    /**
     * @param numeroContribuableAncienFormatAncienneValeur
     *            the numeroContribuableAncienFormatAncienneValeur to set
     */
    public void setNumeroContribuableAncienFormatAncienneValeur(String numeroContribuableAncienFormatAncienneValeur) {
        this.numeroContribuableAncienFormatAncienneValeur = numeroContribuableAncienFormatAncienneValeur;
    }

    /**
     * @param numeroContribuableAncienFormatNouvelleValeur
     *            the numeroContribuableAncienFormatNouvelleValeur to set
     */
    public void setNumeroContribuableAncienFormatNouvelleValeur(String numeroContribuableAncienFormatNouvelleValeur) {
        this.numeroContribuableAncienFormatNouvelleValeur = numeroContribuableAncienFormatNouvelleValeur;
    }

    /**
     * @param numeroContribuableNouveauFormatAncienneValeur
     *            the numeroContribuableNouveauFormatAncienneValeur to set
     */
    public void setNumeroContribuableNouveauFormatAncienneValeur(String numeroContribuableNouveauFormatAncienneValeur) {
        this.numeroContribuableNouveauFormatAncienneValeur = numeroContribuableNouveauFormatAncienneValeur;
    }

    /**
     * @param numeroContribuableNouveauFormatNouvelleValeur
     *            the numeroContribuableNouveauFormatNouvelleValeur to set
     */
    public void setNumeroContribuableNouveauFormatNouvelleValeur(String numeroContribuableNouveauFormatNouvelleValeur) {
        this.numeroContribuableNouveauFormatNouvelleValeur = numeroContribuableNouveauFormatNouvelleValeur;
    }

    /**
     * @param numeroDeRue
     *            the numeroDeRue to set
     */
    public void setNumeroDeRue(String numeroDeRue) {
        this.numeroDeRue = numeroDeRue;
    }

    /**
     * @param numeroPostal
     *            the numeroPostal to set
     */
    public void setNumeroPostal(String numeroPostal) {
        this.numeroPostal = numeroPostal;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @param prenomMajuscules
     *            the prenomMajuscules to set
     */
    public void setPrenomMajuscules(String prenomMajuscules) {
        this.prenomMajuscules = prenomMajuscules;
    }

}
