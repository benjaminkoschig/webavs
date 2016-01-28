package ch.globaz.perseus.business.services.models.statistique;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesEnfantsDemandeException;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesEnfantDemandeSearchModel;

public interface StatistiquesMensuellesEnfantDemandeService extends JadeApplicationService {
    public StatistiquesMensuellesEnfantDemandeSearchModel search(
            StatistiquesMensuellesEnfantDemandeSearchModel searchModel) throws JadePersistenceException,
            StatistiquesMensuellesEnfantsDemandeException;
}