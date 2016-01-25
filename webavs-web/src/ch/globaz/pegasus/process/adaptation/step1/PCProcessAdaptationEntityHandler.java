package ch.globaz.pegasus.process.adaptation.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.List;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInjectable;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.adaptation.AdaptationEntityHandlerAbstract;

public class PCProcessAdaptationEntityHandler extends AdaptationEntityHandlerAbstract implements
        JadeProcessEntityInjectable {

    public PCProcessAdaptationEntityHandler(DonneesHorsDroitsProvider containerGlobal) {
        setContainerGlobal(containerGlobal);
    }

    @Override
    public String getCsModification() {
        return IPCDroits.CS_MOTIF_DROIT_ADAPTATION;
    }

    @Override
    public boolean hasRetroactif() {
        return false;
    }

    @Override
    public void onInjectable(JadeProcessEntity convert, JadeProcessEntityStateEnum csEtat)
            throws JadeApplicationException, JadePersistenceException {
        setCurrentEntity(convert);
        if (csEtat.equals(JadeProcessEntityStateEnum.INJECT)) {
            fillDroitToUpdate();
        }
    }

    @Override
    public void runFunction(PCAIdMembreFamilleRetenuSearch pcAIdMembreFamilleRetenuSearch, Droit droitACalculer,
            List<String> listeMb) throws Exception {
        // this.adaptationDesPSAL(pcAIdMembreFamilleRetenuSearch, droitACalculer, listeMb);
    }
}
