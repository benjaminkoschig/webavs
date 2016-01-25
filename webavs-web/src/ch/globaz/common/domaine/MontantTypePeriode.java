package ch.globaz.common.domaine;

enum MontantTypePeriode {
    ANNUEL,
    MENSUEL,
    JOURNALIER,
    SANS_PERIODE;

    public boolean isMensuel() {
        return equals(MENSUEL);
    }

    public boolean isAnnuel() {
        return equals(ANNUEL);
    }

    public boolean isSansPeriode() {
        return equals(SANS_PERIODE);
    }

    public boolean isJournalier() {
        return equals(JOURNALIER);
    }
}
