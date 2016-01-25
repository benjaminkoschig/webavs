package globaz.pegasus.vb.renteijapi;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApi;
import ch.globaz.pegasus.business.models.renteijapi.RenteIjApiSearch;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutreRenteViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getTypeEcheance() {
        return EcheanceType.AUTRE_RENTE.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les donn�es financi�res
        RenteIjApiSearch search = new RenteIjApiSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedAutreRente");
        search = PegasusServiceLocator.getDroitService().searchRenteIjApi(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RenteIjApi donnee = (RenteIjApi) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleAutreRente) {
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
