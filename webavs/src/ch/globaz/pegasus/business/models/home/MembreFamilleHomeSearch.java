package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class MembreFamilleHomeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDate = "";
    private String forIdTiers = "";
    private String forIdVersionDroit = "";

    /**
     * retourne la condition de recherche sur la période du type de home
     * 
     * @return forDate
     */
    public String getForDate() {
        return forDate;
    }

    /**
     * retourne la condition de recherche sur l'idTiers
     * 
     * @return forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * retourne la condition de recherche sur l'idVersionDroit
     * 
     * @return forIdVersionDroit
     */
    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    /**
     * définit la condition de recherche sur la période du type de home
     * 
     * @param forDate
     */
    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    /**
     * définit la condition de recherche sur l'idTiers
     * 
     * @param forIdTiers
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * définit la condition de recherche sur l'idVersionDroit
     * 
     * @param forIdVersionDroit
     */
    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class whichModelClass() {
        return MembreFamilleHome.class;
    }

}
