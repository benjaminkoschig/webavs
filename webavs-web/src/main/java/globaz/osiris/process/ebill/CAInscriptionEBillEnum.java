package globaz.osiris.process.ebill;

public enum CAInscriptionEBillEnum {

    NUMERO_ADHERENT(0, "numeroAdherent", "Num�ro d'adh�rent"),
    PRENOM(1, "prenom", "Pr�nom"),
    NOM(2, "nom", "Nom"),
    ENTREPRISE(3, "entreprise", "Entreprise"),
    ADRESSE_1(4, "adresse1", "Adresse 1"),
    ADRESSE_2(5, "adresse2", "Adresse 2"),
    NPA(6, "npa", "NPA"),
    LOCALITE(7, "localite", "Localit�"),
    NUMERO_TEL(8, "numeroTel", "Num�ro de t�l�phone"),
    EMAIL(9, "email", "e-mail"),
    NUMERO_AFFILIE(10, "numeroAffilie", "Num�ro d'affili�"),
    ROLE_PARITAIRE(11, "roleParitaire", "R�le paritaire"),
    ROLE_PERSONNEL(12, "rolePersonnel", "R�le personnel"),
    NUM_ADHERENT_BVR(13, "numAdherentBVR", "Num�ro d'adh�rent BVR"),
    NUM_REF_BVR(14, "numeroRefBVR", "Num�ro de r�f�rence BVR"),
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
     * Retourne le type �num�r� {@link CAInscriptionEBillEnum} correspondant au code syst�me pass� en param�tre
     *
     * @param index Le code syst�me � tester
     * @return le type �num�r� {@link CAInscriptionEBillEnum} correspondant au code syst�me pass� en param�tre ou null si pas
     *         trouv�
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
