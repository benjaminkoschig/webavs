package ch.globaz.pegasus.factory.pca;

import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;

public class SimplePrestationsAccordeesFactory {
    public static final String ID_TIERS_CONJOINT = "200";
    public static final String ID_TIERS_REQUERANT = "100";

    public static SimplePrestationsAccordees generate(String dateDebut, String dateFin, String idTiersBeneficiaire) {
        SimplePrestationsAccordees accordees = new SimplePrestationsAccordees();
        accordees.setDateDebutDroit(dateDebut);
        accordees.setDateFinDroit(dateFin);
        accordees.setIdTiersBeneficiaire(idTiersBeneficiaire);
        return accordees;
    }

    public static SimplePrestationsAccordees generateForConjoint(String dateDebut, String dateFin) {
        return SimplePrestationsAccordeesFactory.generate(dateDebut, dateFin,
                SimplePrestationsAccordeesFactory.ID_TIERS_CONJOINT);
    }

    public static SimplePrestationsAccordees generateForRequerant(String dateDebut, String dateFin) {
        return SimplePrestationsAccordeesFactory.generate(dateDebut, dateFin,
                SimplePrestationsAccordeesFactory.ID_TIERS_REQUERANT);
    }
}
