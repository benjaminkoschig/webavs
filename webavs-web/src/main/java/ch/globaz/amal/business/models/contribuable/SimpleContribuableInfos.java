/**
 * 
 */
package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleContribuableInfos extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresse1 = null;
    private String adresse2 = null;
    private String adresse3 = null;
    private String casepostale = null;
    private String cDateNaissance = null;
    private String civilite = null;
    private String commune = null;
    private String dateNaissance = null;
    private String idContribuable = null;
    private String idContribuableInfo = null;
    private boolean isTransfered = false;
    private String nnss = null;
    private String nom = null;
    private String nomUpper = null;
    private String npa = null;
    private String numero = null;
    private String numeroContribuableActuel = null;
    private String numeroContribuableActuelFormate = null;
    private String numeroContribuableAncienFormatAncienneValeur = null;
    private String numeroContribuableAncienFormatNouvelleValeur = null;
    private String numeroContribuableNouveauFormatAncienneValeur = null;
    private String numeroContribuableNouveauFormatNouvelleValeur = null;
    private String pDateNaissance = null;
    private String prenom = null;

    private String prenomUpper = null;

    private String rue = null;

    public String getAdresse1() {
        return adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public String getAdresse3() {
        return adresse3;
    }

    public String getCasepostale() {
        return casepostale;
    }

    public String getCDateNaissance() {
        return cDateNaissance;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getCommune() {
        return commune;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return getIdContribuableInfo();
    }

    public String getIdContribuable() {
        return idContribuable;
    }

    public String getIdContribuableInfo() {
        return idContribuableInfo;
    }

    public Boolean getIsTransfered() {
        return isTransfered;
    }

    public String getNnss() {
        return nnss;
    }

    public String getNom() {
        return nom;
    }

    public String getNomUpper() {
        return nomUpper;
    }

    public String getNpa() {
        return npa;
    }

    public String getNumero() {
        return numero;
    }

    public String getNumeroContribuableActuel() {
        return numeroContribuableActuel;
    }

    public String getNumeroContribuableActuelFormate() {
        return numeroContribuableActuelFormate;
    }

    public String getNumeroContribuableAncienFormatAncienneValeur() {
        return numeroContribuableAncienFormatAncienneValeur;
    }

    public String getNumeroContribuableAncienFormatNouvelleValeur() {
        return numeroContribuableAncienFormatNouvelleValeur;
    }

    public String getNumeroContribuableNouveauFormatAncienneValeur() {
        return numeroContribuableNouveauFormatAncienneValeur;
    }

    public String getNumeroContribuableNouveauFormatNouvelleValeur() {
        return numeroContribuableNouveauFormatNouvelleValeur;
    }

    public String getPDateNaissance() {
        return pDateNaissance;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPrenomUpper() {
        return prenomUpper;
    }

    public String getRue() {
        return rue;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    // @Override
    // public boolean isNew() {
    // return JadeStringUtil.isEmpty(this.getId());
    // }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public void setAdresse3(String adresse3) {
        this.adresse3 = adresse3;
    }

    public void setCasepostale(String casepostale) {
        this.casepostale = casepostale;
    }

    public void setCDateNaissance(String cDateNaissance) {
        this.cDateNaissance = cDateNaissance;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        setIdContribuableInfo(id);
    }

    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    public void setIdContribuableInfo(String idContribuableInfo) {
        this.idContribuableInfo = idContribuableInfo;
    }

    public void setIsTransfered(Boolean isTransfered) {
        this.isTransfered = isTransfered;
    }

    public void setNnss(String nnss) {
        this.nnss = nnss;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomUpper(String nomUpper) {
        this.nomUpper = nomUpper;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setNumeroContribuableActuel(String numeroContribuableActuel) {
        this.numeroContribuableActuel = numeroContribuableActuel;
    }

    public void setNumeroContribuableActuelFormate(String numeroContribuableActuelFormate) {
        this.numeroContribuableActuelFormate = numeroContribuableActuelFormate;
    }

    public void setNumeroContribuableAncienFormatAncienneValeur(String numeroContribuableAncienFormatAncienneValeur) {
        this.numeroContribuableAncienFormatAncienneValeur = numeroContribuableAncienFormatAncienneValeur;
    }

    public void setNumeroContribuableAncienFormatNouvelleValeur(String numeroContribuableAncienFormatNouvelleValeur) {
        this.numeroContribuableAncienFormatNouvelleValeur = numeroContribuableAncienFormatNouvelleValeur;
    }

    public void setNumeroContribuableNouveauFormatAncienneValeur(String numeroContribuableNouveauFormatAncienneValeur) {
        this.numeroContribuableNouveauFormatAncienneValeur = numeroContribuableNouveauFormatAncienneValeur;
    }

    public void setNumeroContribuableNouveauFormatNouvelleValeur(String numeroContribuableNouveauFormatNouvelleValeur) {
        this.numeroContribuableNouveauFormatNouvelleValeur = numeroContribuableNouveauFormatNouvelleValeur;
    }

    public void setPDateNaissance(String pDateNaissance) {
        this.pDateNaissance = pDateNaissance;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPrenomUpper(String prenomUpper) {
        this.prenomUpper = prenomUpper;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }
}
