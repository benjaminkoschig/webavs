package globaz.webavs.common.op;

import globaz.op.common.exception.OpNodeInstanciationException;

/**
 * fix BZ8420 : le parseur devrait pouvoir ignorer les SsNamedCell double de type <NamedCell
 * ss:Name="_FilterDatabase"/>.
 * 
 * @author cel
 * 
 */
public class CommonExcelFilterNotSupportedException extends OpNodeInstanciationException {

    private static final long serialVersionUID = 1L;

    public CommonExcelFilterNotSupportedException(String string) {
        super(string);
    }
}
