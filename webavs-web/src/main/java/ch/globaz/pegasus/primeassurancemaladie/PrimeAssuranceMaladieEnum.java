package ch.globaz.pegasus.primeassurancemaladie;

public enum PrimeAssuranceMaladieEnum {

    /**
     *
     */
    NSS(0, "NSS", "Num�ro NSS"),
    NOM(1, "Nom", "Nom"),
    PRENOM(2, "Prenom", "Pr�nom"),
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
     * Retourne le type �num�r� {@link PrimeAssuranceMaladieEnum} correspondant au code syst�me pass� en param�tre
     *
     * @param index Le code syst�me � tester
     * @return le type �num�r� {@link PrimeAssuranceMaladieEnum} correspondant au code syst�me pass� en param�tre ou null si pas
     *         trouv�
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
