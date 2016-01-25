package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.naos.process.AFImprimerControleProcess;

/**
 * Ce ViewBean va lancer un process pour imprimer les factures car son implémentation est différente des Listes de iText
 * qui héritent directement de FWIAbstractDocumentList dont la classe parente n'est autre que le BProcess
 * 
 * Date de création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class AFImprimerControleViewBean extends AFImprimerControleProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFImprimerControleViewBean() throws java.lang.Exception {
    }

}
