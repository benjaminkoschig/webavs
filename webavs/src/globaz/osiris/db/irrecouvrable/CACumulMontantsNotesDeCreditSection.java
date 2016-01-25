package globaz.osiris.db.irrecouvrable;

import java.math.BigDecimal;

public class CACumulMontantsNotesDeCreditSection {
    private BigDecimal montantSimple;

    private BigDecimal montantSalarie;
    private BigDecimal montantEmployeur;

    public CACumulMontantsNotesDeCreditSection(BigDecimal montantSimple, BigDecimal montantSalarie,
            BigDecimal montantEmployeur) {
        this.montantSimple = montantSimple;
        this.montantSalarie = montantSalarie;
        this.montantEmployeur = montantEmployeur;
    }

    /**
     * @return the montantSimple
     */
    public BigDecimal getMontantSimple() {
        return montantSimple;
    }

    /**
     * @return the montantSalarie
     */
    public BigDecimal getMontantSalarie() {
        return montantSalarie;
    }

    /**
     * @return the montantEmployeur
     */
    public BigDecimal getMontantEmployeur() {
        return montantEmployeur;
    }

}
