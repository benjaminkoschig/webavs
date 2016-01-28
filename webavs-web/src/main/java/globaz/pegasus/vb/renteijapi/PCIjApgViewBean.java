package globaz.pegasus.vb.renteijapi;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCIjApgViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        RenteIjApiSearch search = new RenteIjApiSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedIjApg");
        search = PegasusServiceLocator.getDroitService().searchRenteIjApi(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RenteIjApi donnee = (RenteIjApi) it.next();
            if (donnee.getDonneeFinanciere() instanceof IjApg) {
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
