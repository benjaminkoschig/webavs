package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.Montant;

public class SalaryCaf {
    private final PeriodeSalary periode;
    private final Montant montant;
    private final String canton;

    private SalaryCaf(SalaryCafBuilder builder) {
        periode = builder.periode;
        montant = builder.montant;
        canton = builder.canton;
    }

    public PeriodeSalary getPeriode() {
        return periode;
    }

    public Montant getMontant() {
        return montant;
    }

    public String getCanton() {
        return canton;
    }

    @Override
    public String toString() {
        return "SalaryCaf [periode=" + periode + ", montant=" + montant + ", canton=" + canton + "]";
    }

    public static class SalaryCafBuilder {
        private PeriodeSalary periode;
        private Montant montant;
        private String canton;

        public SalaryCafBuilder periode(PeriodeSalary periode) {
            this.periode = periode;
            return this;
        }

        public SalaryCafBuilder montant(Montant montant) {
            this.montant = montant;
            return this;
        }

        public SalaryCafBuilder canton(String canton) {
            this.canton = canton;
            return this;
        }

        public SalaryCaf build() {
            return new SalaryCaf(this);
        }
    }
}
