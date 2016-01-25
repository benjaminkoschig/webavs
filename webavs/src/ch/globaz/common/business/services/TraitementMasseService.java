package ch.globaz.common.business.services;

import globaz.jade.client.util.JadeProgressBarModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.common.businessimpl.models.UnitTask;

// TODO la place de ce service serait plutôt dans l'impl car c'est un service
// technique qu'on ne va pas publier

public interface TraitementMasseService extends JadeApplicationService {

    public <T extends UnitTask> void traiter(JadeProgressBarModel jadeProgressBarModel, List<T> unitTasks)
            throws Exception;

}