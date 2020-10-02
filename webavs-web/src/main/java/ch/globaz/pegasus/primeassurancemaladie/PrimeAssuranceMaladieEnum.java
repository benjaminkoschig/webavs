package ch.globaz.pegasus.primeassurancemaladie;

public enum PrimeAssuranceMaladieEnum {

    /**
     *
     */
    NSS(0, "NSS", "Numéro NSS"),
    NOM(1, "Nom", "Nom"),
    PRENOM(2, "Prenom", "Prénom"),
    DATE_NAISSANCE(3, "DateNaissance", "Date de naissance"),
    MONTANT(4, "Montant", "Montant"),
    NOM_CAISSE(5, "NomCaisse", "Nom de la caisse maladie");


    private int index;
    private String titre;
    private String description;

    PrimeAssuranceMaladieEnum(int index, String titre, String description) {
        this.index = index;
        this.titre = titre;
        this.description = description;

    }

    public int getIndex() {
        return index;
    }

    public String getTitre() {
        return titre;
    }


    public String getDescription() {
        return description;
    }



    /**
     * Retourne le type énuméré {@link PrimeAssuranceMaladieEnum} correspondant au code système passé en paramètre
     *
     * @param index Le code système à tester
     * @return le type énuméré {@link PrimeAssuranceMaladieEnum} correspondant au code système passé en paramètre ou null si pas
     *         trouvé
     */
    public static PrimeAssuranceMaladieEnum fromIndex(String index) {
        for (PrimeAssuranceMaladieEnum type : PrimeAssuranceMaladieEnum.values()) {
            if (type.getCodeAsString().equals(index)) {
                return type;
            }
        }
        return null;
    }

    private String getCodeAsString() {
        return String.valueOf(index);
    }
}
