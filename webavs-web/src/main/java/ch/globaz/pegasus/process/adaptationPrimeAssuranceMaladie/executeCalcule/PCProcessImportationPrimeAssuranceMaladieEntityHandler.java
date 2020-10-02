package ch.globaz.pegasus.process.adaptationPrimeAssuranceMaladie.executeCalcule;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.adaptation.AdaptationEntityHandlerAbstract;

import java.util.List;

public class PCProcessImportationPrimeAssuranceMaladieEntityHandler extends AdaptationEntityHandlerAbstract {

    public PCProcessImportationPrimeAssuranceMaladieEntityHandler(DonneesHorsDroitsProvider containerGlobal) {
        setContainerGlobal(containerGlobal);
    }

    @Override
    public String getCsModification() {
        return IPCDroits.CS_MOTIF_DROIT_ADAPTATION_HOME;
    }

    @Override
    public boolean hasRetroactif() {
        return true;
    }

    @Override
    public void runFunction(PCAIdMembreFamilleRetenuSearch pcAIdMembreFamilleRetenuSearch, Droit droitACalculer,
            List<String> listeMb) throws Exception {
    }
}
