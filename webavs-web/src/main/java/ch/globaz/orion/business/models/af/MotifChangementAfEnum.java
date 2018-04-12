package ch.globaz.orion.business.models.af;

public enum MotifChangementAfEnum {
    ACCIDENT("DETAIL_RECAP_MOTIF_ACCIDENT", 11030001),
    MALADIE("DETAIL_RECAP_MOTIF_MALADIE", 11030002),
    CONGE_MATERNITE("DETAIL_RECAP_MOTIF_CONGE_MATERNITE", 11030003),
    CONGE_NON_PAYE("DETAIL_RECAP_MOTIF_CONGE_NON_PAYE", 11030004),
    VACANCES("DETAIL_RECAP_MOTIF_VACANCES", 11030005),
    FIN_ACTIVITE("DETAIL_RECAP_MOTIF_FIN_ACTIVITE", 11030006),
    DECES("DETAIL_RECAP_MOTIF_DECES", 11030007);

    private String label;
    private Integer cs;

    private MotifChangementAfEnum(String label, Integer cs) {
        this.label = label;
        this.cs = cs;
    }

    public String getLabel() {
        return label;
    }

    public String getCs() {
        return String.valueOf(cs);
    }
}
