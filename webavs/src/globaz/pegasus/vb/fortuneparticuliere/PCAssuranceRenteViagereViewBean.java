/**
 * 
 */
package globaz.pegasus.vb.fortuneparticuliere;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCAssuranceRenteViagereViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CS_REQUERANT = "64004001";

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les donn�es financi�res
        FortuneParticuliereSearch search = new FortuneParticuliereSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionnedAssuranceRenteViagere");
        search = PegasusServiceLocator.getDroitService().searchFortuneParticuliere(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            FortuneParticuliere donnee = (FortuneParticuliere) it.next();
            if (donnee.getDonneeFinanciere() instanceof AssuranceRenteViagere) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

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
