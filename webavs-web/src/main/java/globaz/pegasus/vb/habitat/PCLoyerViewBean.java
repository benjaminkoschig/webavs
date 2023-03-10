package globaz.pegasus.vb.habitat;

import globaz.globall.parameters.FWParametersSystemCode;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.HabitatSearch;
import ch.globaz.pegasus.business.models.habitat.SimpleLoyer;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCLoyerViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les donn?es financi?res
        HabitatSearch search = new HabitatSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedLoyer");
        search = PegasusServiceLocator.getDroitService().searchHabitat(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            Habitat donnee = (Habitat) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleLoyer) {
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

    public String getDescriptionFromCsDeplafonnement(String csDeplafonnement) {
        FWParametersSystemCode csEntity = new FWParametersSystemCode();
        csEntity.setSession(UtilsService.initSession());
        csEntity.setIdCode(csDeplafonnement);
        try {
            csEntity.retrieve();
        } catch (Exception e) {
            return null;
        }

        return csEntity.getCurrentCodeUtilisateur().getLibelle();
    }

    public String getCsRoleFamilleRequerant() {
        return requerant.getSimpleDroitMembreFamille().getCsRoleFamillePC();
    }
}
