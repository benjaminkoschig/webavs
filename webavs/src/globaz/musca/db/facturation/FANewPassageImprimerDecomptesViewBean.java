package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.musca.process.FANewImpressionFactureProcess;

/**
 * Ce ViewBean va lancer un process pour imprimer les factures car son implémentation est différente des Listes de iText
 * qui héritent directement de FWIAbstractDocumentList dont la classe parente n'est autre que le BProcess Date de
 * création : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FANewPassageImprimerDecomptesViewBean extends FANewImpressionFactureProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Insérez la description de la méthode ici. Date de création : (28.04.2003 15:55:17)
     */
    public FANewPassageImprimerDecomptesViewBean() throws java.lang.Exception {
    }

}
