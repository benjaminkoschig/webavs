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

    private Montant montantRestitutionPC;
    private Montant montantPaiementPC;
    private Montant montantRestitutionRFM;
    private Montant montantPaiementRFM;
    private Montant totalPC;
    private Montant totalRFM;
    private Montant total;
    private CommunePolitiqueBean communePolitique;

    public ContainerByCommunePolitique(CommunePolitiqueBean communePolitique) {
        this.communePolitique = communePolitique;
        montantRestitutionPC = Montant.ZERO;
        montantPaiementPC = Montant.ZERO;
        montantRestitutionRFM = Montant.ZERO;
        montantPaiementRFM = Montant.ZERO;
        total = Montant.ZERO;
        totalPC = Montant.ZERO;
        totalRFM = Montant.ZERO;
    }

    @Column(name = "RestitutionPc", order = 4)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantRestitutionPC() {
        return montantRestitutionPC.getBigDecimalValue().abs();
    }

    @Column(name = "PaiementPc", order = 3)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantPaiementPC() {
        return montantPaiementPC.getBigDecimalValue().abs();
    }

    @Column(name = "RestitutionRfm", order = 7)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantRestitutionRFM() {
        return montantRestitutionRFM.getBigDecimalValue().abs();
    }

    @Column(name = "PaiementRfm", order = 6)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getMontantPaiementRFM() {
        return montantPaiementRFM.getBigDecimalValue().abs();
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

    // WEBAVS-4858 : POAVS-738-K170622_002-CCVS : Les chiffres qui devraient être en négatifs sortent en positifs
    // -> changement du abs() en negate()
    @Column(name = "Total", order = 9)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getTotal() {
        return total.getBigDecimalValue().negate();
    }

    @Column(name = "TotalPc", order = 5)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getTotalPC() {
        return totalPC.getBigDecimalValue().negate();
    }

    @Column(name = "TotalRfm", order = 8)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    public BigDecimal getTotalRFM() {
        return totalRFM.getBigDecimalValue().negate();
    }

    public void setTotal(Montant total) {
        this.total = total;
    }

    public void addMontantPaiementPC(Montant montant) {
        montantPaiementPC = montantPaiementPC.add(montant);
        totalPC = totalPC.add(montant);
        total = total.add(montant);
    }

    public void addMontantRestitutionPC(Montant montant) {
        montantRestitutionPC = montantRestitutionPC.add(montant);
        totalPC = totalPC.add(montant);
        total = total.add(montant);
    }

    public void addMontantPaiementRFM(Montant montant) {
        montantPaiementRFM = montantPaiementRFM.add(montant);
        totalRFM = totalRFM.add(montant);
        total = total.add(montant);
    }

    public void addMontantRestitutionRFM(Montant montant) {
        montantRestitutionRFM = montantRestitutionRFM.add(montant);
        totalRFM = totalRFM.add(montant);
        total = total.add(montant);
    }

    @Override
    public int compareTo(ContainerByCommunePolitique o) {
        return getCodeCommunePolitique().compareToIgnoreCase(o.getCodeCommunePolitique());
    }
}
