package ch.globaz.perseus.business.models.donneesfinancieres;

public class DepenseReconnue extends DonneeFinanciere {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = null;
    private DepenseReconnueType type = null;

    public DepenseReconnue() {
        super();
        simpleDonneeFinanciereSpecialisation = new SimpleDonneeFinanciereSpecialisation();
    }

    public DepenseReconnue(DepenseReconnueType type) {
        this();
        this.setType(type);
    }

    /**
     * Permet de copier les éléments d'un Revenu dans une autre sans changer d'objet
     * 
     * @param Revenu
     *            à copier dans l'objet
     */
    public void copy(DepenseReconnue depenseReconnue) {
        super.copy(depenseReconnue);
        simpleDonneeFinanciereSpecialisation = depenseReconnue.getSimpleDonneeFinanciereSpecialisation();
        type = depenseReconnue.getTypeAsEnum();
        // this.setNew(depenseReconnue.isNew());
    }

    public String getNbPersonnesLogement() {
        return simpleDonneeFinanciereSpecialisation.getNbPersonnesLogement();
    }

    /**
     * @return the penurieLogement
     */
    public Boolean getPenurieLogement() {
        return simpleDonneeFinanciereSpecialisation.getPenurieLogement();
    }

    /**
     * @return the simpleDonneeFinanciereSpecialisation
     */
    public SimpleDonneeFinanciereSpecialisation getSimpleDonneeFinanciereSpecialisation() {
        return simpleDonneeFinanciereSpecialisation;
    }

    /**
     * @return the type
     */
    public String getType() {
        if (type == null) {
            return getSimpleDonneeFinanciere().getType();
        } else {
            return type.toString();
        }
    }

    /**
     * @return the type as an enum
     */
    public DepenseReconnueType getTypeAsEnum() {
        if (type == null) {
            return DepenseReconnueType.getTypeFromId(getSimpleDonneeFinanciere().getType());
        } else {
            return type;
        }
    }

    public void setNbPersonnesLogement(String nbPersonnesLogement) {
        simpleDonneeFinanciereSpecialisation.setNbPersonnesLogement(nbPersonnesLogement);
    }

    /**
     * @param penurieLogement
     *            the penurieLogement to set
     */
    public void setPenurieLogement(Boolean penurieLogement) {
        simpleDonneeFinanciereSpecialisation.setPenurieLogement(penurieLogement);
    }

    /**
     * @param simpleDonneeFinanciereSpecialisation
     *            the simpleDonneeFinanciereSpecialisation to set
     */
    public void setSimpleDonneeFinanciereSpecialisation(
            SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation) {
        this.simpleDonneeFinanciereSpecialisation = simpleDonneeFinanciereSpecialisation;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(DepenseReconnueType type) {
        this.type = type;
        getSimpleDonneeFinanciere().setType(type.toString());
    }

    /**
     * @param type
     *            Id type venant de la base de donnée
     */
    public void setType(String idType) {
        getSimpleDonneeFinanciere().setType(idType);
        type = DepenseReconnueType.getTypeFromId(idType);
    }
}
