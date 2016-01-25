package globaz.naos.db.avisMutation;

public enum AFAvisMotif {
    CREATE_RI_SOC(11),
    CREATE_ASSOCIE(12),
    CREATE_SUCCURSALE(13),
    CREATE_DOMICILE(14),
    UPDATE_RI_SOC(21),
    UPDATE_ASSOCIE(22),
    UPDATE_SUCCURSALE(23),
    UPDATE_DOMICILE(24),
    UPDATE_NEW(20),
    DELETE_RI_SOC(31),
    DELETE_ASSOCIE(32),
    DELETE_SUCCURSALE(33),
    DELETE_DOMICILE(34),
    MOTIF_55(55);

    private String codeMotif;
    private int codeMotifInt;

    private AFAvisMotif(int aCodeMotif) {
        codeMotifInt = aCodeMotif;
        codeMotif = "" + aCodeMotif;
    }

    @Override
    public String toString() {
        return codeMotif;
    }

    public int codeMotifInt() {
        return codeMotifInt;
    }
}
