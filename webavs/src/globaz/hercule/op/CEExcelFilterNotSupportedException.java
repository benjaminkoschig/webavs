package globaz.hercule.op;

import globaz.op.common.exception.OpNodeInstanciationException;

/**
 * fix BZ8420 : le parseur devrait pouvoir ignorer les SsNamedCell double de type <NamedCell
 * ss:Name="_FilterDatabase"/>.
 * 
 * @author cel
 * 
 */
public class CEExcelFilterNotSupportedException extends OpNodeInstanciationException {
    public CEExcelFilterNotSupportedException(String string) {
        super(string);
    }
}
