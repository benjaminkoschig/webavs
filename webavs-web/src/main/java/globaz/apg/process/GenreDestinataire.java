package globaz.apg.process;

public enum GenreDestinataire {
    EMPLOYEUR(true, false),
    INDEPENDANT(true, true),
    ASSURE(false, false);

    final boolean isEmployeur;
    final boolean isIndependant;

    private GenreDestinataire(final boolean isEmployeur, final boolean isIndependant) {
        this.isEmployeur = isEmployeur;
        this.isIndependant = isIndependant;
    }

    public static GenreDestinataire getDestinataire(final boolean isEmployeur, final boolean isIndependant) {
        for (GenreDestinataire dest : GenreDestinataire.values()) {
            if (dest.isEmployeur == isEmployeur && dest.isIndependant == isIndependant) {
                return dest;
            }
        }

        return null;
    }
}
