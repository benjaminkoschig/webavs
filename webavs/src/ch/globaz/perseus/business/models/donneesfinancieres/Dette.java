package ch.globaz.perseus.business.models.donneesfinancieres;

public class Dette extends DonneeFinanciere {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = null;
    private DetteType type = null;

    public Dette() {
        super();
        simpleDonneeFinanciereSpecialisation = new SimpleDonneeFinanciereSpecialisation();
    }

    public Dette(DetteType type) {
        this();
        this.setType(type);
    }

    /**
     * Permet de copier les éléments d'un Revenu dans une autre sans changer d'objet
     * 
     * @param Revenu
     *            à copier dans l'objet
     */
    public void copy(Dette dette) {
        super.copy(dette);
        simpleDonneeFinanciereSpecialisation = dette.getSimpleDonneeFinanciereSpecialisation();
        type = dette.getTypeAsEnum();
        // this.setNew(dette.isNew());
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
    public DetteType getTypeAsEnum() {
        if (type == null) {
            return DetteType.getTypeFromId(getSimpleDonneeFinanciere().getType());
        } else {
            return type;
        }
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
    public void setType(DetteType type) {
        this.type = type;
        getSimpleDonneeFinanciere().setType(type.toString());
    }

    /**
     * @param type
     *            Id type venant de la base de donnée
     */
    public void setType(String idType) {
        getSimpleDonneeFinanciere().setType(idType);
        type = DetteType.getTypeFromId(idType);
    }
}
