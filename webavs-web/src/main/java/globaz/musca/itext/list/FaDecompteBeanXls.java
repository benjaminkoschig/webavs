package globaz.musca.itext.list;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.MontantConverterToDouble;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverterToDouble.class })
class FaDecompteBeanXls {
    private String debiteur;
    private String decompte;
    private Montant montant;
    private String addressPaiement;
    private String recRem;
    private String imprimable;

    @Column(name = "DEBITEUR", order = 1)
    public String getDebiteur() {
        return debiteur;
    }

    @Column(name = "DECOMPTE", order = 2)
    public String getDecompte() {
        return decompte;
    }

    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    @Column(name = "MONTANT", order = 3)
    public Montant getMontant() {
        return montant;
    }

    @Column(name = "ADRPAIEM", order = 4)
    public String getAddressPaiement() {
        return addressPaiement;
    }

    @Column(name = "REMARQUE", order = 5)
    public String getRecRem() {
        return recRem;
    }

    @Column(name = "IMPRESSION", order = 6)
    public String getImprimable() {
        return imprimable;
    }

    public void setDebiteur(String debiteur) {
        this.debiteur = debiteur;
    }

    public void setDecompte(String decompte) {
        this.decompte = decompte;
    }

    public void setMontant(Montant montant) {
        this.montant = montant;
    }

    public void setAddressPaiement(String addressPaiement) {
        this.addressPaiement = addressPaiement;
    }

    public void setRecRem(String recRem) {
        this.recRem = recRem;
    }

    public void setImprimable(String imprimable) {
        this.imprimable = imprimable;
    }

}
