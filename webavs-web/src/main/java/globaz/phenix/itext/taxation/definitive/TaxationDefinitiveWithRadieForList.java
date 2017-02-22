package globaz.phenix.itext.taxation.definitive;

import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.common.listoutput.converterImplemented.PourcentConverter;
import ch.globaz.simpleoutputlist.annotation.Column;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;
import ch.globaz.simpleoutputlist.annotation.style.Align;
import ch.globaz.simpleoutputlist.annotation.style.ColumnStyle;

@ColumnValueConverter({ MontantConverter.class, PourcentConverter.class })
public class TaxationDefinitiveWithRadieForList extends TaxationDefinitiveForList {
    private String radie;

    public TaxationDefinitiveWithRadieForList(String radiation) {
        radie = radiation;
    }

    @ColumnStyle(align = Align.LEFT)
    @Column(name = "listeTaxDefRadie", order = 12)
    public String getRadie() {
        return radie;
    }

    public void setRadie(String radie) {
        this.radie = radie;
    }
}
