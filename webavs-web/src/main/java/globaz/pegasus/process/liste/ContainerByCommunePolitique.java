package globaz.pegasus.process.liste;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter(MontantConverter.class)
@Translater(value = LabelTranslater.class, identifier = "PEGASUS_LISTE_EXCEL_CP")
public class ContainerByCommunePolitique implements Comparable<ContainerByCommunePolitique> {

    private Montant montantRestitution;
    private Montant montantPaiement;
    private Montant total;
    private String communePolitique;

    public ContainerByCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
        montantRestitution = Montant.ZERO;
        montantPaiement = Montant.ZERO;
        total = Montant.ZERO;
    }

    @Column(name = "Restitution", order = 3)
    @ColumnStyle(align = Align.RIGHT)
    @Aggregate(AggregateFunction.SUM)
    public Montant getMontantRestitution() {
        return montantRestitution;
    }

    @Column(name = "Paiement", order = 2)
    @ColumnStyle(align = Align.RIGHT)
    @Aggregate(AggregateFunction.SUM)
    public Montant getMontantPaiement() {
        return montantPaiement;
    }

    @Column(name = "commune", order = 1)
    @ColumnStyle(align = Align.CENTER)
    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    @Column(name = "Total", order = 4)
    @ColumnStyle(align = Align.RIGHT)
    @Aggregate(AggregateFunction.SUM)
    public Montant getTotal() {
        return total;
    }

    public void setTotal(Montant total) {
        this.total = total;
    }

    public void addMontantPaiement(Montant montant) {
        montantPaiement = montantPaiement.add(montant);
        total = total.add(montant);
    }

    public void addMontantRestitution(Montant montant) {
        montantRestitution = montantRestitution.add(montant);
        total = total.substract(montant);
    }

    @Override
    public int compareTo(ContainerByCommunePolitique o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }
}
