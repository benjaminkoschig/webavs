package ch.globaz.vulpecula.business.models.travailleur;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PersonneEtendueMetierSearchComplexModel extends JadeSearchComplexModel {
    public String getSexeHomme() {
        return sexeHomme;
    }

    public String getSexeFemme() {
        return sexeFemme;
    }

    public String getPersonnePhysique() {
        return personnePhysique;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDesignation1;
    private String forDesignation2;
    private String forDesignation1Like;
    private String forDesignation2Like;
    private String forNumeroAvsActuel;
    private String forIdTiers;
    private String forDateNaissance;
    private String for_personneMorale;
    private String forNoContribuable;
    private String for_isInactif;

    public static final String WHERE_WITHSEXANDPERSON = "sexeObligatoireEtPersonnePhysique";

    private String sexeHomme = "516001";
    private String sexeFemme = "516002";

    private String personnePhysique = "1";

    @Override
    public Class<PersonneEtendueMetierComplexModel> whichModelClass() {
        return PersonneEtendueMetierComplexModel.class;
    }

    /*
     * Getter et Setter
     */
    public String getForDesignation1Like() {
        return forDesignation1Like;
    }

    public void setForDesignation1Like(String forDesignation1Like) {
        this.forDesignation1Like = forDesignation1Like;
    }

    public String getForNumeroAvsActuel() {
        return forNumeroAvsActuel;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroAvsActuel(String forNumeroAvsActuel) {
        this.forNumeroAvsActuel = forNumeroAvsActuel;
    }

    public String getForDesignation2Like() {
        return forDesignation2Like;
    }

    public void setForDesignation2Like(String forDesignation2Like) {
        this.forDesignation2Like = forDesignation2Like;
    }

    public String getForDateNaissance() {
        return forDateNaissance;
    }

    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    public String getForDesignation1() {
        return forDesignation1;
    }

    public void setForDesignation1(String forDesignation1) {
        this.forDesignation1 = forDesignation1;
    }

    public String getForDesignation2() {
        return forDesignation2;
    }

    public void setForDesignation2(String forDesignation2) {
        this.forDesignation2 = forDesignation2;
    }

    public String getFor_personneMorale() {
        return for_personneMorale;
    }

    public void setFor_personneMorale(String forPersonneMorale) {
        for_personneMorale = forPersonneMorale;
    }

    public String getForNoContribuable() {
        return forNoContribuable;
    }

    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    public String getFor_isInactif() {
        return for_isInactif;
    }

    /**
     * Si for_Inactif égal 1 ==> seulement les tiers inactifs<br>
     * Si for_Inactif égal 2 ==> seulement les tiers actifs
     * 
     * @param for_isInactif
     */
    public void setFor_isInactif(String for_isInactif) {
        this.for_isInactif = for_isInactif;
    }
}
