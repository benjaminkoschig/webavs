package globaz.pegasus.vb.habitat;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.utils.PCApplicationUtil;
import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCTaxeJournaliereHomeHandler;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.HabitatSearch;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHome;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCTaxeJournaliereHomeViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getPrix(TaxeJournaliereHome taxeJournaliereHome, BSession objSession) throws PrixChambreException,
            HomeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        return PCTaxeJournaliereHomeHandler.getPrix(taxeJournaliereHome, objSession);

    }
    public boolean isCaisseCCJU(BSession session) throws CalculException {
        return PCApplicationUtil.isCantonJU();
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        HabitatSearch search = new HabitatSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedTaxeJournaliereHome");
        search = PegasusServiceLocator.getDroitService().searchHabitat(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            Habitat donnee = (Habitat) it.next();
            if (donnee.getDonneeFinanciere() instanceof TaxeJournaliereHome) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();
                PCTaxeJournaliereHomeHandler.putPrix(donnee);
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
