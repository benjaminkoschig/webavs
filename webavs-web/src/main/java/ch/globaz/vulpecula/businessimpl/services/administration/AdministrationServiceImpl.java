package ch.globaz.vulpecula.businessimpl.services.administration;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.vulpecula.business.services.administration.AdministrationService;
import ch.globaz.vulpecula.external.models.AdministrationSearchComplexModel;

public class AdministrationServiceImpl implements AdministrationService {
    /**
     * Opération de recherche sur les personnes
     */
    @Override
    public AdministrationSearchComplexModel find(AdministrationSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException {
        if (searchModel != null) {
            // Case insensitive search
            if (searchModel.getForDesignation1Like() != null) {
                searchModel.setForDesignation1Like(JadeStringUtil.convertSpecialChars(searchModel
                        .getForDesignation1Like().toUpperCase()));
            }
        }
        return (AdministrationSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

}