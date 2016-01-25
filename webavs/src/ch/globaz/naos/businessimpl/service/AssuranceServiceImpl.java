package ch.globaz.naos.businessimpl.service;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.service.AssuranceService;

public class AssuranceServiceImpl implements AssuranceService {

    @Override
    public String getAssuranceLibelle(String idAssurance, String langue) throws JadePersistenceException,
            JadeApplicationException {
        AssuranceSimpleModel assurance = read(idAssurance);
        if ("FR".equals(langue)) {
            return assurance.getAssuranceLibelleFr();
        } else if ("DE".equals(langue)) {
            return assurance.getAssuranceLibelleAl();
        } else if ("IT".equals(langue)) {
            return assurance.getAssuranceLibelleIt();
        } else {
            return assurance.getAssuranceLibelleFr();
        }
    }

    @Override
    public AssuranceSimpleModel read(String idAssurance) throws JadePersistenceException, JadeApplicationException {
        AssuranceSimpleModel assurance = new AssuranceSimpleModel();
        assurance.setId(idAssurance);
        return (AssuranceSimpleModel) JadePersistenceManager.read(assurance);
    }

}
