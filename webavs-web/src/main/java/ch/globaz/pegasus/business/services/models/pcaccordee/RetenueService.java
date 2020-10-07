package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;

public interface RetenueService extends JadeApplicationService {
    public void create(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException;

    public void createWithOutCheck(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException;

    public void delete(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException;

    public PcaRetenue read(String id) throws JadePersistenceException;

    public PcaRetenueSearch search(PcaRetenueSearch search) throws JadePersistenceException;

    public void update(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException;
    public void updateWithoutCheck(PcaRetenue retenue) throws JadePersistenceException, JadeApplicationException;
}
