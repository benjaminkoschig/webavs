package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.musca.process.FAImpressionFactureProcess;

/**
 * Ce ViewBean va lancer un process pour imprimer les factures car son impl�mentation est diff�rente des Listes de iText
 * qui h�ritent directement de FWIAbstractDocumentList dont la classe parente n'est autre que le BProcess Date de
 * cr�ation : (10.03.2003 10:37:34)
 * 
 * @author: btc
 */
public class FAPassageImprimerDecomptesViewBean extends FAImpressionFactureProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.04.2003 15:55:17)
     */
    public FAPassageImprimerDecomptesViewBean() throws java.lang.Exception {
    }

}
