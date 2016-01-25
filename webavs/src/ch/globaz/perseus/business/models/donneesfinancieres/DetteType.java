package ch.globaz.perseus.business.models.donneesfinancieres;

public enum DetteType {
    AUTRES_DETTES(202, false),
    DETTES_HYPOTHECAIRES(201, false);

    /**
     * Permet de retrouver une DetteType sur la base de son ID stock� en base de donn�es
     * 
     * @param idType
     *            stock� en base de donn�es
     * @return le type correspondant � l'id pass�
     */
    public static DetteType getTypeFromId(String idType) {
        DetteType[] allValues = DetteType.values();
        for (int i = 0; i < allValues.length; i++) {
            DetteType rt = allValues[i];
            if (rt.id.equals(Integer.valueOf(idType))) {
                return rt;
            }
        }
        return null;
    }

    private Integer id;
    private Boolean useSpecialisation;

    private DetteType(Integer id, Boolean useSpecialisation) {
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
