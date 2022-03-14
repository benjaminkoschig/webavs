package ch.globaz.eform.business.validator;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.search.GFEFormSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GFEFormValidator {
    public static boolean isExists(String id) {
        GFEFormSearch gfeFormSearch = new GFEFormSearch();
        gfeFormSearch.setByMessageId(id);

        try {
            gfeFormSearch = GFEFormServiceLocator.getGFEFormService().search(gfeFormSearch);
            return gfeFormSearch.getSearchResults().length > 0;
        } catch (JadePersistenceException | JadeApplicationServiceNotAvailableException e) {
            LOG.error("Une erreur c'est produite pour la recherche du gfeFormModel id :" + id, e);
        }

        return false;
    }
}
