package ch.globaz.naos.businessimpl.service;

import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.naos.business.model.AssuranceSearchSimpleModel;
import ch.globaz.naos.exception.MajorationFraisAdminException;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.service.AssuranceService;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssuranceServiceImpl implements AssuranceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssuranceServiceImpl.class);

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

    @Override
    public List<String> getIdsAssurancesTous(BSession session) throws MajorationFraisAdminException {
        try {
            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = AFAffiliationUtil.initContext(session);
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            AssuranceSearchSimpleModel searchSimpleModel = new AssuranceSearchSimpleModel();
            List<AssuranceSimpleModel> simpleModel = RepositoryJade.searchForAndFetch(searchSimpleModel);

            if (simpleModel.size() > 0) {
                return simpleModel.stream()
                        .map(AssuranceSimpleModel::getAssuranceId)
                        .collect(Collectors.toList());
            }

            } catch (Exception e) {
            String message = "Error durant la recherche de tous les ids d'assurances.";
                LOGGER.error(message, e);
                throw new MajorationFraisAdminException(message, e);
            } finally {
                JadeThreadActivator.stopUsingContext(Thread.currentThread());
            }

        return new ArrayList<>();
    }

}
