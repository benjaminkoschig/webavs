package ch.globaz.orion.business.domaine.pucs;

import ch.globaz.common.domaine.Montant;

public class SalaryAvs {
    private final PeriodeSalary periode;
    private final Montant montantAvs;
    private final Montant montantAc1;
    private final Montant montantAc2;

    private SalaryAvs(SalaryAvsBuilder builder) {
        periode = builder.periode;
        montantAvs = builder.montantAvs;
        montantAc1 = builder.montantAc1;
        montantAc2 = builder.montantAc2;
    }

    public PeriodeSalary getPeriode() {
        return periode;
    }

    public Montant getMontantAvs() {
        return montantAvs;
    }

    public Montant getMontantAc1() {
        return montantAc1;
    }

    public Montant getMontantAc2() {
        return montantAc2;
    }

    @Override
    public String toString() {
        return "SalaryAvs [periode=" + periode + ", montantAvs=" + montantAvs + ", montantAc1=" + montantAc1
                + ", montantAc2=" + montantAc2 + "]";
    }

    public static class SalaryAvsBuilder {
        private PeriodeSalary periode;
        private Montant montantAvs;
        private Montant montantAc1;
        private Montant montantAc2;

        public SalaryAvsBuilder periode(PeriodeSalary periode) {
            this.periode = periode;
            return this;
        }

        public SalaryAvsBuilder montantAvs(Montant montantAvs) {
            this.montantAvs = montantAvs;
            return this;
        }

        public SalaryAvsBuilder montantAc1(Montant montantAc1) {
            this.montantAc1 = montantAc1;
            return this;
        }

        public SalaryAvsBuilder montantAc2(Montant montantAc2) {
            this.montantAc2 = montantAc2;
            return this;
        }

        public SalaryAvs build() {
            return new SalaryAvs(this);
        }
    }

}
