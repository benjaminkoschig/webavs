package ch.globaz.pegasus.business.models.calcul;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

/**
 * Modèle de recherche pour le calcul mois suivants
 * 
 * @author sce
 * 
 */
public class CalculMoisSuivantSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /* Id du droit */
    private String forIdDroit = null;
    /* liste des id membres famille pour le DF */
    private List forIdMembreFamilleSFIn = null;
    /* id de la version du droit */
    private String forIdVersionDroit = null;

    public String getForIdDroit() {
        return forIdDroit;
    }

    public List getForIdMembreFamilleSFIn() {
        return forIdMembreFamilleSFIn;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdMembreFamilleSFIn(List forIdMembreFamilleSFIn) {
        this.forIdMembreFamilleSFIn = forIdMembreFamilleSFIn;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    @Override
    public Class whichModelClass() {
        return CalculMoisSuivant.class;
    }

}
