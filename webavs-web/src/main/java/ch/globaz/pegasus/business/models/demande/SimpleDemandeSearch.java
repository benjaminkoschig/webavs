/**
 * 
 */
package ch.globaz.pegasus.business.models.demande;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

/**
 * @author ECO
 * 
 */
public class SimpleDemandeSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String CHECK_SUPERPOSIZION_DATES = "checkSuperpositionDates";

    private List<String> forCsEtatDemandeIN = null;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forIdDossier = null;
    private String forNotIdDemande = null;

    /**
     * @return the forCsEtatDemandeIN
     */
    public List<String> getForCsEtatDemandeIN() {
        return forCsEtatDemandeIN;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForNotIdDemande() {
        return forNotIdDemande;
    }

    /**
     * @param forCsEtatDemandeIN
     *            the forCsEtatDemandeIN to set
     */
    public void setForCsEtatDemandeIN(List<String> forCsEtatDemandeIN) {
        this.forCsEtatDemandeIN = forCsEtatDemandeIN;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForNotIdDemande(String forNotIdDemande) {
        this.forNotIdDemande = forNotIdDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleDemande> whichModelClass() {
        return SimpleDemande.class;
    }

}
