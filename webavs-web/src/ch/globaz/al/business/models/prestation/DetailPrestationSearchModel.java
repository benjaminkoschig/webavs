package ch.globaz.al.business.models.prestation;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * Modèle simple de recherche du détail de prestation
 * 
 * @author PTA
 * 
 */
public class DetailPrestationSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** REcherche sur la ctégorie de tarid */
    private String forCategorieTarif = null;
    /**
     * Recherche sur l'identifiant du détail de la prestation
     */
    private String forIdDetailPrestation = null;
    /**
     * Recherche sur l'identifiant du droit
     */
    private String forIdDroit = null;
    /**
     * Recherche sur l'id de l'en-tête de la prestation
     */
    private String forIdEntetePrestation = null;
    /**
     * Recherche sur la période de validité
     */
    private String forPeriodeDebut = null;
    /**
     * Recherche sur la période de validité
     */
    private String forPeriodeFin = null;

    /** Recherche sur un ou plusieurs types de prestation */
    private Collection<String> inTypePrestation = null;

    /**
     * @return the forIdDetailPrestation
     */
    public String getForIdDetailPrestation() {
        return forIdDetailPrestation;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdEntetePrestation
     */
    public String getForIdEntetePrestation() {
        return forIdEntetePrestation;
    }

    /**
     * @return the forPeriodeDebut
     */
    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    /**
     * @return the forPeriodeFin
     */
    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    public Collection<String> getInTypePrestation() {
        return inTypePrestation;
    }

    /**
     * @param forIdDetailPrestation
     *            the forIdDetailPrestation to set
     */
    public void setForIdDetailPrestation(String forIdDetailPrestation) {
        this.forIdDetailPrestation = forIdDetailPrestation;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdEntetePrestation
     *            the forIdEntetePrestation to set
     */
    public void setForIdEntetePrestation(String forIdEntetePrestation) {
        this.forIdEntetePrestation = forIdEntetePrestation;
    }

    /**
     * @param forPeriodeDebut
     *            the forPeriodeDebut to set
     */
    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    /**
     * @param forPeriodeFin
     *            the forPeriodeFin to set
     */
    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    /**
     * 
     * @param forTypePrestation
     *            types de prestation à rechercher
     */
    public void setInTypePrestation(Collection<String> inTypePrestation) {
        this.inTypePrestation = inTypePrestation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DetailPrestationModel> whichModelClass() {
        return DetailPrestationModel.class;
    }

    public void setForCategorieTarif(String forCategorieTarif) {
        this.forCategorieTarif = forCategorieTarif;
    }

    public String getForCategorieTarif() {
        return forCategorieTarif;
    }

}
