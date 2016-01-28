package ch.globaz.perseus.business.services.models.retenue;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.retenue.Retenue;
import ch.globaz.perseus.business.models.retenue.RetenueSearchModel;

public interface RetenueService extends JadeApplicationService {

    public int count(RetenueSearchModel search) throws RetenueException, JadePersistenceException;

    public Retenue create(Retenue retenue) throws RetenueException, JadePersistenceException;

    public Retenue delete(Retenue retenue) throws RetenueException, JadePersistenceException;

    public int deleteForPCFAccordee(String idPCFAccordee) throws RetenueException, JadePersistenceException;

    public Retenue read(String idRetenue) throws RetenueException, JadePersistenceException;

    public RetenueSearchModel search(RetenueSearchModel retenueSearch) throws JadePersistenceException,
            RetenueException;

    public Retenue update(Retenue retenue) throws RetenueException, JadePersistenceException;

}
