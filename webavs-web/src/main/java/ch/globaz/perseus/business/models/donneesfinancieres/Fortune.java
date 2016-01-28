package ch.globaz.perseus.business.models.donneesfinancieres;

public class Fortune extends DonneeFinanciere {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDonneeFinanciereSpecialisation simpleDonneeFinanciereSpecialisation = null;
    private FortuneType type = null;

    public Fortune() {
        super();
        simpleDonneeFinanciereSpecialisation = new SimpleDonneeFinanciereSpecialisation();
    }

    public Fortune(FortuneType type) {
        this();
        this.setType(type);
    }

    /**
     * Permet de copier les éléments d'un Revenu dans une autre sans changer d'objet
     * 
     * @param Revenu
     *            à copier dans l'objet
     */
    public void copy(Fortune fortune) {
        super.copy(fortune);
        simpleDonneeFinanciereSpecialisation = fortune.getSimpleDonneeFinanciereSpecialisation();
        type = fortune.getTypeAsEnum();
        // this.setNew(fortune.isNew());
    }

    public String getDateCession() {
        return simpleDonneeFinanciereSpecialisation.getDateCession();
    }

    public String getNomHoirie() {
        return simpleDonneeFinanciereSpecialisation.getNomHoirie();
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
    public FortuneType getTypeAsEnum() {
        if (type == null) {
            return FortuneType.getTypeFromId(getSimpleDonneeFinanciere().getType());
        } else {
            return type;
        }
    }

    public void setDateCession(String dateCession) {
        simpleDonneeFinanciereSpecialisation.setDateCession(dateCession);
    }

    public void setNomHoirie(String nomHoirie) {
        simpleDonneeFinanciereSpecialisation.setNomHoirie(nomHoirie);
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
    public void setType(FortuneType type) {
        this.type = type;
        getSimpleDonneeFinanciere().setType(type.toString());
    }

    /**
     * @param type
     *            Id type venant de la base de donnée
     */
    public void setType(String idType) {
        getSimpleDonneeFinanciere().setType(idType);
        type = FortuneType.getTypeFromId(idType);
    }
}
