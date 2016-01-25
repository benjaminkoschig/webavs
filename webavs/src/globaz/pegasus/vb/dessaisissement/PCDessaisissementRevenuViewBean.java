/**
 * 
 */
package globaz.pegasus.vb.dessaisissement;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAutoSearch;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCDessaisissementRevenuViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map donneesAuto = new HashMap();

    public List getDonneesAuto(String idMembre) throws Exception {
        if (donneesAuto.containsKey(idMembre)) {
            return (List) donneesAuto.get(idMembre);
        } else {
            return new ArrayList();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les dessaisissements de fortune manuels
        DessaisissementRevenuSearch search = new DessaisissementRevenuSearch();
        search.setWhereKey("forVersioned");
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search = PegasusServiceLocator.getDroitService().searchDessaisissementRevenu(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            DessaisissementRevenu donnee = (DessaisissementRevenu) it.next();
            DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

            List donneesMembre = (List) donnees.get(f.getId());
            if (donneesMembre == null) {
                donneesMembre = new ArrayList();
                donnees.put(f.getId(), donneesMembre);
            }
            donneesMembre.add(donnee);
        }

        // cherche les dessaisissements de fortune automatiques
        DessaisissementRevenuAutoSearch searchAuto = new DessaisissementRevenuAutoSearch();
        searchAuto.setWhereKey("forVersioned");
        searchAuto.setForIdDroit(getId());
        searchAuto.setForNumeroVersion(getNoVersion());
        searchAuto = PegasusServiceLocator.getDroitService().searchDessaisissementRevenuAuto(searchAuto);

        for (Iterator it = Arrays.asList(searchAuto.getSearchResults()).iterator(); it.hasNext();) {
            DessaisissementRevenuAuto donnee = (DessaisissementRevenuAuto) it.next();
            DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

            List donneesMembre = (List) donneesAuto.get(f.getId());
            if (donneesMembre == null) {
                donneesMembre = new ArrayList();
                donneesAuto.put(f.getId(), donneesMembre);
            }
            donneesMembre.add(donnee);
        }
    }

}
