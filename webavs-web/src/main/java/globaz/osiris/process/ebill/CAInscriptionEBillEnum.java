package globaz.osiris.process.ebill;

public enum CAInscriptionEBillEnum {

    NUMERO_ADHERENT(0, "numeroAdherent", "Numéro d'adhérent"),
    PRENOM(1, "prenom", "Prénom"),
    NOM(2, "nom", "Nom"),
    ENTREPRISE(3, "entreprise", "Entreprise"),
    ADRESSE_1(4, "adresse1", "Adresse 1"),
    ADRESSE_2(5, "adresse2", "Adresse 2"),
    NPA(6, "npa", "NPA"),
    LOCALITE(7, "localite", "Localité"),
    NUMERO_TEL(8, "numeroTel", "Numéro de téléphone"),
    EMAIL(9, "email", "e-mail"),
    NUMERO_AFFILIE(10, "numeroAffilie", "Numéro d'affilié"),
    ROLE_PARITAIRE(11, "roleParitaire", "Rôle paritaire"),
    ROLE_PERSONNEL(12, "rolePersonnel", "Rôle personnel"),
    NUM_ADHERENT_BVR(13, "numAdherentBVR", "Numéro d'adhérent BVR"),
    NUM_REF_BVR(14, "numeroRefBVR", "Numéro de référence BVR"),
    STATUS(15, "statut", "Statut");


    private int index;
    private String titre;
    private String description;

    CAInscriptionEBillEnum(int index, String titre, String description) {
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
     * Retourne le type énuméré {@link CAInscriptionEBillEnum} correspondant au code système passé en paramètre
     *
     * @param index Le code système à tester
     * @return le type énuméré {@link CAInscriptionEBillEnum} correspondant au code système passé en paramètre ou null si pas
     *         trouvé
     */
    public static CAInscriptionEBillEnum fromIndex(String index) {
        for (CAInscriptionEBillEnum type : CAInscriptionEBillEnum.values()) {
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
