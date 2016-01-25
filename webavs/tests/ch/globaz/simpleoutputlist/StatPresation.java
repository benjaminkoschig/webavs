package ch.globaz.simpleoutputlist;

import ch.globaz.common.listoutput.converterImplemented.LabelTranslater;
import ch.globaz.common.listoutput.converterImplemented.MontantStringConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.Translater;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@Translater(value = LabelTranslater.class, identifier = "RENTE")
public class StatPresation {
    private String code;
    private String nb;
    private String montant;

    @Column(name = "code", order = 2)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "nbPrestation", order = 1)
    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    @ColumnValueConverter(MontantStringConverter.class)
    @ColumnStyle(align = Align.RIGHT)
    @Column(name = "MontantTotal", order = 3)
    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "StatPresation [code=" + code + ", nb=" + nb + ", montant=" + montant + "]";
    }

}
