package ch.globaz.simpleoutputlist;

import java.math.BigDecimal;
import ch.globaz.common.listoutput.converterImplemented.CodeSystemeConverter;
import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.simpleoutputlist.annotation.Aggregate;
import ch.globaz.simpleoutputlist.annotation.AggregateFunction;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "RENTE")
public class StatPresation {
    private String code;
    private Integer nb;
    private BigDecimal montant;

    @Column(name = "TATA")
    public String getPRo() {
        return code;
    }

    @Column(name = "nbPrestation", order = 3)
    @Aggregate(AggregateFunction.SUM)
    public Integer getNb() {
        return nb;
    }

    @ColumnValueConverter(CodeSystemeConverter.class)
    public String codeS() {
        return null;
    }

    @ColumnStyle(align = Align.RIGHT, format = "#,##0.00")
    @Column(name = "MontantTotal", order = 2)
    @Aggregate(AggregateFunction.SUM)
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
