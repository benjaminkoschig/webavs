package ch.globaz.perseus.business.models.donneesfinancieres;

public enum DepenseReconnueType {
    CHARGES_ANNUELLES(302, false),
    COTISATION_NON_ACTIF(303, false),
    FRAIS_ENTRETIENS_IMMEUBLE(304, false),
    FRAIS_REPAS(309, false),
    FRAIS_TRANSPORT(308, false),
    FRAIS_VETEMENTS(310, false),
    INTERETS_HYPOTHECAIRES(305, false),
    LOYER_ANNUEL(301, true),
    PENSION_ALIMENTAIRE_VERSEE(306, false),
    TAILLE_UNITE_ASSISTANCE(307, false);

    /**
     * Permet de retrouver une DepenseReconnueType sur la base de son ID stocké en base de données
     * 
     * @param idType
     *            stocké en base de données
     * @return le type correspondant à l'id passé
     */
    public static DepenseReconnueType getTypeFromId(String idType) {
        DepenseReconnueType[] allValues = DepenseReconnueType.values();
        for (int i = 0; i < allValues.length; i++) {
            DepenseReconnueType rt = allValues[i];
            if (rt.id.equals(Integer.valueOf(idType))) {
                return rt;
            }
        }
        return null;
    }

    private Integer id;
    private Boolean useSpecialisation;

    private DepenseReconnueType(Integer id, Boolean useSpecialisation) {
        this.id = id;
        this.useSpecialisation = useSpecialisation;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the useSpecialisation
     */
    public Boolean getUseSpecialisation() {
        return useSpecialisation;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public Boolean useSpecialisation() {
        return useSpecialisation;
    }

}
