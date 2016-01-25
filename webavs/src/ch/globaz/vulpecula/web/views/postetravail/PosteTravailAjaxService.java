package ch.globaz.vulpecula.web.views.postetravail;

import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class PosteTravailAjaxService {

    public List<String[]> getTauxListParPoste(String idPosteTravail) throws JadePersistenceException {
        List<Occupation> occupations = VulpeculaRepositoryLocator.getOccupationRepository()
                .findOccupationsByIdPosteTravail(idPosteTravail);
        List<String[]> tauxList = new ArrayList<String[]>();

        for (Occupation occupation : occupations) {
            String[] elements = new String[2];
            elements[0] = String.valueOf(occupation.getTaux().getValue());
            elements[1] = occupation.getDateValidite().getSwissValue();
            tauxList.add(elements);
        }
        return tauxList;
    }
}
