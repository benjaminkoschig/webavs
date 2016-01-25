package ch.globaz.vulpecula.domain.specifications.postetravail;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.utils.ListUtil;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * @author Arnaud Geiser (AGE) | Créé le 18 févr. 2014
 * 
 */
public class PosteTravailOccupationsSansDoublonsSpecification extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail posteTravail) {
        List<Date> datesOccupations = new ArrayList<Date>();
        for (Occupation occupation : posteTravail.getOccupations()) {
            datesOccupations.add(occupation.getDateValidite());
        }

        if (ListUtil.hasDuplicate(datesOccupations)) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_MEME_OCCUPATIONS_DATE);
        }
        return true;
    }
}
