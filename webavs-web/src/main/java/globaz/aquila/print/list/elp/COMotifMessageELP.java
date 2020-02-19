package globaz.aquila.print.list.elp;

public enum COMotifMessageELP {
    POURSUITE_RADIE("ELP_MOTIF_POURSUITE_RADIE"),
    POURSUITE_CLASSE("ELP_MOTIF_POURSUITE_CLASSE"),
    CDP_NON_NOTIFIE("ELP_MOTIF_CDP_NON_NOTIFIE"),
    CDP_NON_TRAITE("ELP_MOTIF_CDP_NON_TRAITE"),
    PV_NON_TRAITE("ELP_MOTIF_PV_NON_TRAITE"),
    RUBRIQUE_FRAIS_INDEF("ELP_MOTIF_RUBRIQUE_FRAIS_INDEF"),
    ADB_NON_TRAITE("ELP_MOTIF_ADB_NON_TRAITE"),
    REF_INCOMPATIBLE("ELP_MOTIF_REF_INCOMPATIBLE"),
    TYPE_NON_TRAITE("ELP_MOTIF_TYPE_NON_TRAITE"),
    NOM_FICHIER_INCOHERENT("ELP_MOTIF_NOM_FICHIER_INCOHERENT"),
    AUTRE_ERREUR("ELP_MOTIF_AUTRE_ERREUR");

    private String value;

    private COMotifMessageELP(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}