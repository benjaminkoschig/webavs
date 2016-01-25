package globaz.pegasus.vb.revenusdepenses;

import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.revenusdepenses.CotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepensesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCCotisationsPsalViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String CS_REQUERANT = "64004001";

    public String getTypeEcheance() {
        return EcheanceType.COTISATION_PSAL.toString();
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
        RevenusDepensesSearch search = new RevenusDepensesSearch();
        search.setForNumeroVersion(getNoVersion());
        search.setForIdDroit(getId());
        search.setWhereKey("forVersionedCotisationsPSAL");
        search = PegasusServiceLocator.getDroitService().searchRevenusDepenses(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RevenusDepenses donnee = (RevenusDepenses) it.next();
            if (donnee.getDonneeFinanciere() instanceof CotisationsPsal) {
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
