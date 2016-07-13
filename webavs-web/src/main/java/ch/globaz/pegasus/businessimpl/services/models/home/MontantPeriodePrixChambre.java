package ch.globaz.pegasus.businessimpl.services.models.home;

public class MontantPeriodePrixChambre {

    public enum TypeMontant {
        PRIX_JOURNALIER,
        MONTANT_PLAFOND_ADMIS;
    }

    private String montant;
    private String dateDebutPeriode;

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    private String dateFinPeriode;
    private TypeMontant typeMontant;

    private MontantPeriodePrixChambre(String montant, String dateDebutPeriode, String dateFinPeriode,
            TypeMontant typeMontant) {
        this.montant = montant;
        this.dateDebutPeriode = dateDebutPeriode;
        this.dateFinPeriode = dateFinPeriode;
        this.typeMontant = typeMontant;
    }

    public static MontantPeriodePrixChambre forPrixJournalier(String montant, String dateDebutPeriode,
            String dateFinPeriode) {
        MontantPeriodePrixChambre montantPrixChambre = new MontantPeriodePrixChambre(montant, dateDebutPeriode,
                dateFinPeriode, TypeMontant.PRIX_JOURNALIER);
        return montantPrixChambre;
    }

    public MontantPeriodePrixChambre getForNewDatePeriode(String dateDebut) {
        return new MontantPeriodePrixChambre(montant, dateDebut, null, typeMontant);
    }

    public TypeMontant getTypeMontant() {
        return typeMontant;
    }

    public void setTypeMontant(TypeMontant typeMontant) {
        this.typeMontant = typeMontant;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    @Override
    public String toString() {
        return "MontantPeriodePrixChambre [montant=" + montant + ", dateDebutPeriode=" + dateDebutPeriode
                + ", typeMontant=" + typeMontant + "]";
    }

    public static MontantPeriodePrixChambre forPrixPlafondAdmis(String montant, String dateDebutPeriode,
            String dateFinPeriode) {
        MontantPeriodePrixChambre montantPrixChambre = new MontantPeriodePrixChambre(montant, dateDebutPeriode,
                dateFinPeriode, TypeMontant.MONTANT_PLAFOND_ADMIS);
        return montantPrixChambre;
    }

    public String getMontant() {
        return montant;
    }

    public TypeMontant getType() {
        return typeMontant;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public boolean isFromType(TypeMontant type) {
        return typeMontant == type;
    }

}
