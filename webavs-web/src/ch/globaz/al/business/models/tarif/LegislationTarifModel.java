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
     * Priorité permettant de spécifier l'ordre à utiliser en cas de résultats identique lors de la recherche de tarif
     */
    private String priorite = null;

    /**
     * Type de législation
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    private String typeLegislation = null;

    /**
     * @return priorité permettant de spécifier l'ordre à utiliser en cas de résultats identique lors de la recherche de
     *         tarif
     */
    public String getPriorite() {
        return priorite;
    }

    /**
     * @return le type de législation
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
