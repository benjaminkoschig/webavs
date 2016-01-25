package globaz.apg.enums;

public enum APTypeActiviteProfessionnel {
    ACTIVITE_INDEPENDANT(2),
    ACTIVITE_NON_ACTIF(3),
    ACTIVITE_SALARIE(1),
    ACTIVITE_SALARIE_ET_INDEPENDANT(4);

    private int codeActivite;

    private APTypeActiviteProfessionnel(int codeActivite) {
        this.codeActivite = codeActivite;
    }

    public int getCodeActivite() {
        return codeActivite;
    }

    public String getCodeActiviteAsString() {
        return String.valueOf(codeActivite);
    }
}
