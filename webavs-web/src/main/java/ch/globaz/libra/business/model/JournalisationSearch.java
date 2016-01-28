package ch.globaz.libra.business.model;

public class JournalisationSearch {
    private String forCsDomaine = null;
    private String forCsType = null;
    private String forDateReceptionFinal = null;
    private String forDateReceptionInitial = null;

    private String forIdDomaine = null;
    private String forIdDossier = null;
    private String forIdExtern = null;
    private String forIdGroupe = null;
    private String forIdUtilisateur = null;
    private String forLibelle = null;

    public String getForCsDomaine() {
        return forCsDomaine;
    }

    public String getForCsType() {
        return forCsType;
    }

    /**
     * @return the forDateReceptionFinal
     */
    public String getForDateReceptionFinal() {
        return forDateReceptionFinal;
    }

    /**
     * @return the forDateReceptionInitial
     */
    public String getForDateReceptionInitial() {
        return forDateReceptionInitial;
    }

    public String getForIdDomaine() {
        return forIdDomaine;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdExtern() {
        return forIdExtern;
    }

    public String getForIdGroupe() {
        return forIdGroupe;
    }

    public String getForIdUtilisateur() {
        return forIdUtilisateur;
    }

    /**
     * @return the forLibelle
     */
    public String getForLibelle() {
        return forLibelle;
    }

    public void setForCsDomaine(String forCsDomaine) {
        this.forCsDomaine = forCsDomaine;
    }

    public void setForCsType(String forCsType) {
        this.forCsType = forCsType;
    }

    /**
     * @param forDateReceptionFinal
     *            the forDateReceptionFinal to set
     */
    public void setForDateReceptionFinal(String forDateReceptionFinal) {
        this.forDateReceptionFinal = forDateReceptionFinal;
    }

    /**
     * @param forDateReceptionInitial
     *            the forDateReceptionInitial to set
     */
    public void setForDateReceptionInitial(String forDateReceptionInitial) {
        this.forDateReceptionInitial = forDateReceptionInitial;
    }

    public void setForIdDomaine(String forIdDomaine) {
        this.forIdDomaine = forIdDomaine;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdExtern(String forIdExtern) {
        this.forIdExtern = forIdExtern;
    }

    public void setForIdGroupe(String forIdGroupe) {
        this.forIdGroupe = forIdGroupe;
    }

    public void setForIdUtilisateur(String forIdUtilisateur) {
        this.forIdUtilisateur = forIdUtilisateur;
    }

    /**
     * @param forLibelle
     *            the forLibelle to set
     */
    public void setForLibelle(String forLibelle) {
        this.forLibelle = forLibelle;
    }

}
