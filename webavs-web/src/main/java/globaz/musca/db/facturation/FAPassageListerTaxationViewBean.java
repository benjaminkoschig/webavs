package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.phenix.itext.taxation.definitive.CPListeTaxationDefinitiveXlsPdf;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FAPassageListerTaxationViewBean extends CPListeTaxationDefinitiveXlsPdf implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.03.2003 10:55:33)
     */
    public FAPassageListerTaxationViewBean() throws java.lang.Exception {
        setFromFacturation(true);
    }
}
