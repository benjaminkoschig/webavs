package globaz.pegasus.process.liste;

import globaz.prestation.interfaces.tiers.CommunePolitiqueBean;
import java.math.BigDecimal;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "PEGASUS_LISTE_EXCEL_CP")
public class ContainerByCommunePolitique implements Comparable<ContainerByCommunePolitique> {

    private Montant montantRestitution;
    private Montant montantPaiement;
    private Montant total;
    private CommunePolitiqueBean communePolitique;

    public ContainerByCommunePolitique(CommunePolitiqueBean communePolitique) {
        this.communePolitique = communePolitique;
        montantRestitution = Montant.ZERO;
        montantPaiement = Montant.ZERO;
        total = Montant.ZERO;
    }

    @Column(name = "Restitution", order = 4)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantRestitution() {
        return montantRestitution.getBigDecimalValue();
    }

    @Column(name = "Paiement", order = 3)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantPaiement() {
        return montantPaiement.getBigDecimalValue();
    }

    @Column(name = "commune", order = 1)
    @ColumnStyle(align = Align.CENTER)
    public String getCodeCommunePolitique() {
        return communePolitique.getCode();
    }

    @Column(name = "communeNom", order = 2)
    @ColumnStyle(align = Align.LEFT)
    public String getNomCommunePolitique() {
        return communePolitique.getNom();
    }

    public void setCommunePolitique(CommunePolitiqueBean communePolitique) {
        this.communePolitique = communePolitique;
    }

    @Column(name = "Total", order = 5)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getTotal() {
        return total.getBigDecimalValue();
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
        total = total.add(montant);
    }

    @Override
    public int compareTo(ContainerByCommunePolitique o) {
        return getCodeCommunePolitique().compareToIgnoreCase(o.getCodeCommunePolitique());
    }
}
