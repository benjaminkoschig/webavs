package ch.globaz.perseus.business.services.models.statistique;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesDemPcfDecException;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDecSearchModel;

public interface StatistiquesMensuellesDemPcfDecService extends JadeApplicationService {

    public StatistiquesMensuellesDemPcfDecSearchModel search(StatistiquesMensuellesDemPcfDecSearchModel searchModel)
            throws JadePersistenceException, StatistiquesMensuellesDemPcfDecException;

}
