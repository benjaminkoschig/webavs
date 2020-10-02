package globaz.pegasus.vb.assurancemaladie;

import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.assurancemaladie.SimpleSubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * ViewBean pour les rentes avsai AI 6.2010
 * 
 * @author SCE
 */
public class PCSubsideAssuranceMaladieViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getTypeEcheance() {
        return EcheanceType.RENTE_AVS_AI.toString();
    }

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // TODO A remplacer
        // cherche les données financières
        AssuranceMaladieSearch search = new AssuranceMaladieSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedSubsideAssuranceMaladie");
        search = PegasusServiceLocator.getDroitService().searchAssuranceMaladie(search);

        for (Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            AssuranceMaladie donnee = (AssuranceMaladie) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleSubsideAssuranceMaladie) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList<>();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }
    }

    public AssuranceMaladie getDonneesAssuranceMaladie(String idMembre) throws Exception {
        if (donnees.containsKey(idMembre)) {
            return (AssuranceMaladie) donnees.get(idMembre);
        } else {
            return null;
        }
    }
}
