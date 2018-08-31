package ch.globaz.vulpecula.business.services.ebusiness;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.vulpecula.business.models.ebusiness.TravailleurEbuSimpleModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurEbuSearchSimpleModel;

/**
 * @author JPA
 * 
 */
public interface NouveauTravailleurServiceCRUD extends
        JadeCrudService<TravailleurEbuSimpleModel, TravailleurEbuSearchSimpleModel> {

    @Override
    int count(TravailleurEbuSearchSimpleModel searchModel) throws JadeApplicationException, JadePersistenceException;

    @Override
    TravailleurEbuSearchSimpleModel search(TravailleurEbuSearchSimpleModel searchModel) throws JadePersistenceException;

}
