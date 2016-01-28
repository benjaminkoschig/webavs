package globaz.phenix.listes.excel;

import globaz.op.common.exception.OpNodeInstanciationException;

/**
 * fix BZ8420 : le parseur devrait pouvoir ignorer les SsNamedCell double de type <NamedCell
 * ss:Name="_FilterDatabase"/>.
 * 
 * @author cel
 * 
 */
public class CPExcelFilterNotSupportedException extends OpNodeInstanciationException {
    public CPExcelFilterNotSupportedException(String string) {
        super(string);
    }
}
