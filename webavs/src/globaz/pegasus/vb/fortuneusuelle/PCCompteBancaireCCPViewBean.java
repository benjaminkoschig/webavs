/**
 * 
 */
package globaz.pegasus.vb.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle;
import ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelleSearch;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCCompteBancaireCCPViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Utilise pour faciliter l'affichage des IBAN dans les tableaux
     * 
     * @param iban
     * @return
     */
    public String formatIbanForTD(String iban) {
        String formattedIban = "";
        if (!JadeStringUtil.isEmpty(iban)) {
            formattedIban = JadeStringUtil.change(iban, " ", "&nbsp;");
        }
        return formattedIban;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        FortuneUsuelleSearch search = new FortuneUsuelleSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedCompteBancaireCCP");
        search = PegasusServiceLocator.getDroitService().searchFortuneUsuelle(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            FortuneUsuelle donnee = (FortuneUsuelle) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleCompteBancaireCCP) {
                MembreFamilleEtendu f = donnee.getMembreFamilleEtendu();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }
    }

}
