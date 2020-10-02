package globaz.pegasus.vb.habitat;

import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.habitat.Habitat;
import ch.globaz.pegasus.business.models.habitat.HabitatSearch;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PCSejourMoisPartielHomeViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private VariableMetierSearch variableMetierSearch;

    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        HabitatSearch search = new HabitatSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionedSejourMoisPartielHome");
        search = PegasusServiceLocator.getDroitService().searchHabitat(search);

        for (Iterator it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            Habitat donnee = (Habitat) it.next();
            if (donnee.getDonneeFinanciere() instanceof SejourMoisPartielHome) {
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

    public String getFraisNourriture() throws JadeApplicationServiceNotAvailableException, VariableMetierException, JadePersistenceException {
        VariableMetierSearch vmSearch = new VariableMetierSearch();
        vmSearch.setForCsTypeVariableMetier(IPCVariableMetier.CS_REFORME_REFORME_FRAIS_DE_NOURRITURE);
        vmSearch.setWhereKey("withDateValable");
        vmSearch.setForLangue(BSessionUtil.getSessionFromThreadContext().getIdLangue());
        vmSearch = PegasusServiceLocator.getVariableMetierService().search(vmSearch);
        String frais = "0";
        if (vmSearch.getSize() == 1) {
            VariableMetier var = (((VariableMetier) vmSearch.getSearchResults()[0]));
            frais = var.getSimpleVariableMetier().getMontant();
        } else {
            JadeThread.logError("", "Plusieurs variables métier trouvées");
        }
        return frais;
    }
}
