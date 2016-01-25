package ch.globaz.pegasus.business.services.models.avance;

import globaz.corvus.db.avances.REAvance;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.pegasus.business.exceptions.models.avance.AvanceException;
import ch.globaz.pegasus.business.models.avance.AvanceVo;
import ch.globaz.pegasus.business.models.avance.AvanceVoSearch;

public interface AvanceService extends JadeCrudService<AvanceVo, AvanceVoSearch> {

    @Override
    public int count(AvanceVoSearch search) throws JadeApplicationException, JadePersistenceException;

    @Override
    public AvanceVo create(AvanceVo entity) throws JadeApplicationException, JadePersistenceException;

    public REAvance createReAvance(REAvance avance) throws Exception;

    @Override
    public AvanceVo delete(AvanceVo entity) throws JadeApplicationException, JadePersistenceException;

    public REAvance deleteReAvance(REAvance avance) throws AvanceException;

    public void executer();

    @Override
    public AvanceVo read(String idEntity);

    Object readAvance(String idEntity) throws Exception;

    @Override
    public AvanceVoSearch search(AvanceVoSearch search) throws JadeApplicationException, JadePersistenceException;

    @Override
    public AvanceVo update(AvanceVo entity) throws JadeApplicationException, JadePersistenceException;

    public REAvance updateReAvance(REAvance avance) throws AvanceException;

}
