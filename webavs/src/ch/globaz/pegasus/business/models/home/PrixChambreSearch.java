package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PrixChambreSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebut = null;
    private String forDateFin = null;
    private String forDateValable = null;
    private String forHomeTypeAdresse = null;
    private String forIdHome = null;
    private String forIdTypeChambre = null;

    /**
     * @return the forDateDebut
     */
    public String getForDateDebut() {
        return forDateDebut;
    }

    /**
     * @return the forDateFin
     */
    public String getForDateFin() {
        return forDateFin;
    }

    /**
     * retourne la condition de recherche sur la periode de validite de prixChambre
     * 
     * @return the forDateValable
     */
    public String getForDateValable() {
        return forDateValable;
    }

    public String getForHomeTypeAdresse() {
        return forHomeTypeAdresse;
    }

    /**
     * retourne la condition de recherche sur l'id home pour lequel ce prix de chambre est valable
     * 
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * retourne la condition de recherche sur l'id typeChambre pour lequel ce prix de chambre est valable
     * 
     * @return the forIdTypeChambre
     */
    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    /**
     * @param forDateDebut
     *            the forDateDebut to set
     */
    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    /**
     * @param forDateFin
     *            the forDateFin to set
     */
    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    /**
     * définit la condition de recherche sur la periode de validite de prixChambre
     * 
     * @param forDateValable
     *            the forDateValable to set
     */
    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForHomeTypeAdresse(String forHomeTypeAdresse) {
        this.forHomeTypeAdresse = forHomeTypeAdresse;
    }

    /**
     * définit la condition de recherche sur l'id home pour lequel ce prix de chambre est valable
     * 
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    /**
     * définit la condition de recherche sur l'id typeChambre pour lequel ce prix de chambre est valable
     * 
     * @param forIdTypeChambre
     *            the forIdTypeChambre to set
     */
    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    @Override
    public Class whichModelClass() {
        return PrixChambre.class;
    }

}
