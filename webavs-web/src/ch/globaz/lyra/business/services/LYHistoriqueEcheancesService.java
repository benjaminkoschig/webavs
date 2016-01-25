package ch.globaz.lyra.business.services;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.lyra.business.exceptions.LYException;
import ch.globaz.lyra.business.models.historique.LYSimpleHistorique;
import ch.globaz.lyra.business.models.historique.LYSimpleHistoriqueSearchModel;

public interface LYHistoriqueEcheancesService extends JadeApplicationService {

    public void add(LYSimpleHistorique historique) throws LYException, JadePersistenceException;

    public LYSimpleHistorique read(String idHistorique) throws LYException, JadePersistenceException;

    public LYSimpleHistoriqueSearchModel search(LYSimpleHistoriqueSearchModel searchModel) throws LYException,
            JadePersistenceException;
}
