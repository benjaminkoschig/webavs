package ch.globaz.pegasus.business.services.models.annonce.communicationocc;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;

public interface GenerationCommunicationOCCService extends JadeApplicationService {

    public void genereCommunicationOCCSuppression(DecisionSuppression decisionSuppression) throws PrestationException;

    public List<SimpleCommunicationOCC> genereCommunicationOCCValidation(List<DecisionApresCalcul> list,
            List<PcaForDecompte> listAnciennePca) throws DecisionException;
}
