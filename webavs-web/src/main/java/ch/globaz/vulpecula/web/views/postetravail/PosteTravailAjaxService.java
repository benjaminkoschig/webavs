package ch.globaz.vulpecula.web.views.postetravail;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

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

    public Boolean getIsPosteReactivable(final String dateDebutActivite, final String dateFinActivite) {
        if (dateFinActivite == null) {
            return true;
        }

        int monthBetween = JadeDateUtil.getNbMonthsBetween(dateFinActivite, dateDebutActivite);

        // le poste n'est pas ré-activable si inactif depuis plus de 9 mois
        return monthBetween <= 9;
    }

    public Boolean getExistPostePourQualif(final String idTravailleur, final String idEmployeur,
            final String valueQualif) {

        PosteTravail posteTravail = null;

        if (idTravailleur != null && idEmployeur != null && valueQualif != null) {

            List<Qualification> listeQualif = new ArrayList<Qualification>();
            listeQualif.add(Qualification.fromValue(valueQualif));

            posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository()
                    .findByTravailleurEmployeurEtQualification(idTravailleur, idEmployeur, listeQualif);
        }

        return posteTravail != null;
    }
}
