package globaz.phenix.itext.taxation.definitive;

import ch.globaz.common.listoutput.converterImplemented.MontantConverter;
import ch.globaz.common.listoutput.converterImplemented.PourcentConverter;
import ch.globaz.simpleoutputlist.annotation.ColumnValueConverter;

@ColumnValueConverter({ MontantConverter.class, PourcentConverter.class })
public class TaxationDefinitiveWithoutRadieForList extends TaxationDefinitiveForList {

}
