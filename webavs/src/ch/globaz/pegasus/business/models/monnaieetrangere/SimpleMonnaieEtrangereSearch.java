package ch.globaz.pegasus.business.models.monnaieetrangere;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * 
 * @author SCE
 * 
 */
public class SimpleMonnaieEtrangereSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Champ de recherche
    private String forCsTypeMonnaie = null; // Code CS de la monnaie
    private String forDateDebutCheckPeriode = null;
    private String forDateFinCheckPeriode = null;
    private String forDateValable = null; // date de validité
    private String forIdMonnaieEtrangere = null;

    /**
     * @return the forCsNomMonnaie
     */
    public String getForCsTypeMonnaie() {
        return forCsTypeMonnaie;
    }

    /**
     * @return the forDateDebut
     */
    public String getForDateDebutCheckPeriode() {
        return forDateDebutCheckPeriode;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFinCheckPeriode() {
        return forDateFinCheckPeriode;
    }

    /**
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    /**
     * @return the forIdMonnaieEtrangere
     */
    public String getForIdMonnaieEtrangere() {
        return forIdMonnaieEtrangere;
    }

    /**
     * @param forCsNomMonnaie
     *            the forCsNomMonnaie to set
     */
    public void setForCsTypeMonnaie(String forCsTypeMonnaie) {
        this.forCsTypeMonnaie = forCsTypeMonnaie;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebutCheckPeriode(String forDateDebutCheckPeriode) {
        this.forDateDebutCheckPeriode = forDateDebutCheckPeriode;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFinCheckPeriode(String forDateFinCheckPeriode) {
        this.forDateFinCheckPeriode = forDateFinCheckPeriode;
    }

    /**
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    /**
     * @param forIdMonnaieEtrangere
     *            the forIdMonnaieEtrangere to set
     */
    public void setForIdMonnaieEtrangere(String forIdMonnaieEtrangere) {
        this.forIdMonnaieEtrangere = forIdMonnaieEtrangere;
    }

    @Override
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return SimpleMonnaieEtrangere.class;

    }

}
