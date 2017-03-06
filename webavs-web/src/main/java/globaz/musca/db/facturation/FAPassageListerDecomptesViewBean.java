package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.musca.itext.list.FAListDecompteNew_Doc;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FAPassageListerDecomptesViewBean extends FAListDecompteNew_Doc implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.03.2003 10:55:33)
     */
    public FAPassageListerDecomptesViewBean() throws java.lang.Exception {
    }

    public boolean isXls() {
        return "XLS".equalsIgnoreCase(getOutPutType());
    }

    @Override
    protected boolean _executeProcess() {
        if (isXls()) {
            generateXsl();
        } else {
            super._executeProcess();
        }
        return false;
    }
}
