package ch.globaz.perseus.business.models.donneesfinancieres;

public enum FortuneType {
    AUTRE_BIEN(109, false),
    AUTRES_IMMEUBLES(102, false),
    BIENS_ETRANGERS(103, false),
    CESSION(108, true),
    FORTUNE_ENFANT(110, false),
    HOIRIE(107, true),
    IMMEUBLE_HABITE(101, false),
    LIQUIDITE(104, false),
    RACHAT_ASSURANCE_VIE(106, false);

    /**
     * Permet de retrouver une FortuneType sur la base de son ID stocké en base de données
     * 
     * @param idType
     *            stocké en base de données
     * @return le type correspondant à l'id passé
     */
    public static FortuneType getTypeFromId(String idType) {
        FortuneType[] allValues = FortuneType.values();
        for (int i = 0; i < allValues.length; i++) {
            FortuneType rt = allValues[i];
            if (rt.id.equals(Integer.valueOf(idType))) {
                return rt;
            }
        }
        return null;
    }

    private Integer id;
    private Boolean useSpecialisation;

    private FortuneType(Integer id, Boolean useSpecialisation) {
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
