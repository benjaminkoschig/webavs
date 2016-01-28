package ch.globaz.al.business.models.tarif;

/**
 * classe de legislation du tarif
 * 
 * @author PTA
 * 
 */
public class LegislationTarifModel extends LegislationTarifFkModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Priorit� permettant de sp�cifier l'ordre � utiliser en cas de r�sultats identique lors de la recherche de tarif
     */
    private String priorite = null;

    /**
     * Type de l�gislation
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    private String typeLegislation = null;

    /**
     * @return priorit� permettant de sp�cifier l'ordre � utiliser en cas de r�sultats identique lors de la recherche de
     *         tarif
     */
    public String getPriorite() {
        return priorite;
    }

    /**
     * @return le type de l�gislation
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    public String getTypeLegislation() {
        return typeLegislation;
    }

    /**
     * @param priorite
     *            the priorite to set
     */
    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    /**
     * @param typeLegislation
     *            the typeLegislation to set
     */
    public void setTypeLegislation(String typeLegislation) {
        this.typeLegislation = typeLegislation;
    }
}
