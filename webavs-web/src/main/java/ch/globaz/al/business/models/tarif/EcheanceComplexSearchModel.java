package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche du critère âge pour les échéances des droits
 * 
 * @author PTA
 * 
 */

public class EcheanceComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * recherche sur la capacité d'exercer
     */
    Boolean forCapableExercer = null;
    /**
     * recherche sur la catégorie de résident
     */
    private String forCategorieResident = null;

    /**
     * recherche sur la catégorie de tarif
     */
    private String forCategorieTarif = null;

    /**
     * recherche sur le début de la validité de la prestation
     */
    private String forDebutValiditePrestation = null;

    /**
     * recherche sur la fin de la validité de la prestation
     */
    private String forFinValiditePrestation = null;

    /**
     * recherche sur le type de prestation
     */
    private String forTypePrestation = null;

    /**
     * @return the forCapableExercer
     */
    public Boolean getForCapableExercer() {
        return forCapableExercer;
    }

    /**
     * @return the forCategorieResident
     */
    public String getForCategorieResident() {
        return forCategorieResident;
    }

    /**
     * @return the forCategorieTarif
     */
    public String getForCategorieTarif() {
        return forCategorieTarif;
    }

    /**
     * @return the forDebutValiditePrestation
     */
    public String getForDebutValiditePrestation() {
        return forDebutValiditePrestation;
    }

    /**
     * @return the forFinValiditePrestation
     */
    public String getForFinValiditePrestation() {
        return forFinValiditePrestation;
    }

    /**
     * @return the forTypePrestation
     */
    public String getForTypePrestation() {
        return forTypePrestation;
    }

    /**
     * @param forCapableExercer
     *            the forCapableExercer to set
     */
    public void setForCapableExercer(Boolean forCapableExercer) {
        this.forCapableExercer = forCapableExercer;
    }

    /**
     * @param forCategorieResident
     *            the forCategorieResident to set
     */
    public void setForCategorieResident(String forCategorieResident) {
        this.forCategorieResident = forCategorieResident;
    }

    /**
     * @param forCategorieTarif
     *            the forCategorieTarif to set
     */
    public void setForCategorieTarif(String forCategorieTarif) {
        this.forCategorieTarif = forCategorieTarif;
    }

    /**
     * @param forDebutValiditePrestation
     *            the forDebutValiditePrestation to set
     */
    public void setForDebutValiditePrestation(String forDebutValiditePrestation) {
        this.forDebutValiditePrestation = forDebutValiditePrestation;
    }

    /**
     * @param forFinValiditePrestation
     *            the forFinValiditePrestation to set
     */
    public void setForFinValiditePrestation(String forFinValiditePrestation) {
        this.forFinValiditePrestation = forFinValiditePrestation;
    }

    /**
     * @param forTypePrestation
     *            the forTypePrestation to set
     */
    public void setForTypePrestation(String forTypePrestation) {
        this.forTypePrestation = forTypePrestation;
    }

    @Override
    public Class<EcheanceComplexModel> whichModelClass() {

        return EcheanceComplexModel.class;
    }

}
