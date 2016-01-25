package ch.globaz.perseus.businessimpl.services.models.statistique;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.statistique.StatistiquesMensuellesEnfantsDemandeException;
import ch.globaz.perseus.business.models.statistique.StatistiquesMensuellesEnfantDemandeSearchModel;
import ch.globaz.perseus.business.services.models.statistique.StatistiquesMensuellesEnfantDemandeService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class StatistiquesMensuellesEnfantDemandeServiceImpl extends PerseusAbstractServiceImpl implements
        StatistiquesMensuellesEnfantDemandeService {

    @Override
    public StatistiquesMensuellesEnfantDemandeSearchModel search(
            StatistiquesMensuellesEnfantDemandeSearchModel searchModel) throws JadePersistenceException,
            StatistiquesMensuellesEnfantsDemandeException {

        if (searchModel == null) {
            throw new StatistiquesMensuellesEnfantsDemandeException(
                    "Impossible de chercher StatistiquesMensuellesDemPcfDec, le searchmodel passé est null");
        }

        return (StatistiquesMensuellesEnfantDemandeSearchModel) JadePersistenceManager.search(searchModel);
    }

}
