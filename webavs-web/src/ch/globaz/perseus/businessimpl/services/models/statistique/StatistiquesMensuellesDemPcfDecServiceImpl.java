package ch.globaz.perseus.businessimpl.services.models.statistique;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesDemPcfDecException;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesDemPcfDecSearchModel;
import ch.globaz.perseus.business.services.models.statistique.StatistiquesMensuellesDemPcfDecService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class StatistiquesMensuellesDemPcfDecServiceImpl extends PerseusAbstractServiceImpl implements
        StatistiquesMensuellesDemPcfDecService {

    @Override
    public StatistiquesMensuellesDemPcfDecSearchModel search(StatistiquesMensuellesDemPcfDecSearchModel searchModel)
            throws JadePersistenceException, StatistiquesMensuellesDemPcfDecException {

        if (searchModel == null) {
            throw new StatistiquesMensuellesDemPcfDecException(
                    "Impossible de chercher StatistiquesMensuellesDemPcfDec, le searchmodel passé est null");
        }
        return (StatistiquesMensuellesDemPcfDecSearchModel) JadePersistenceManager.search(searchModel);
    }

}
