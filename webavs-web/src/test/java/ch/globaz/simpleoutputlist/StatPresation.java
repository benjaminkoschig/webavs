package ch.globaz.simpleoutputlist;

import java.math.BigDecimal;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "RENTE")
public class StatPresation {
    private String code;
    private Integer nb;
    private BigDecimal montant;

    @Column(name = "code", order = 2)
    public String getCode() {
        return code;
    }

    @Aggregate(AggregateFunction.SUM)
    @Column(name = "nbPrestation", order = 1)
    public Integer getNb() {
        return nb;
    }

    // @ColumnValueConverter(MontantStringConverter.class)
    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Aggregate(AggregateFunction.SUM)
    @Column(name = "MontantTotal", order = 3)
    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setNb(Integer nb) {
        this.nb = nb;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "StatPresation [code=" + code + ", nb=" + nb + ", montant=" + montant + "]";
    }

}
