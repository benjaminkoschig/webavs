package globaz.naos.listes.excel;

import globaz.op.common.exception.OpNodeInstanciationException;

/**
 * fix BZ8420 : le parseur devrait pouvoir ignorer les SsNamedCell double de type <NamedCell
 * ss:Name="_FilterDatabase"/>.
 * 
 * @author cel
 * 
 */
public class AFExcelFilterNotSupportedException extends OpNodeInstanciationException {
    public AFExcelFilterNotSupportedException(String string) {
        super(string);
    }
}
