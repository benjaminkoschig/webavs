package ch.globaz.vulpecula.domain.specifications.postetravail;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class PosteTravailTauxOccupationInPeriodeActivite extends AbstractSpecification<PosteTravail> {

    @Override
    public boolean isValid(PosteTravail poste) {
        if (!poste.getOccupations().isEmpty() && poste.getPeriodeActivite() == null) {
            addMessage(SpecificationMessage.POSTE_TRAVAIL_PERIODE_ACTIVITE_NON_SAISIE_OCCUPATIONS);
        }

        Date periodeFin = poste.getFinActivite();
        Date periodeDebut = poste.getDebutActivite();

        for (Occupation occupation : poste.getOccupations()) {
            if (occupation.getDateValidite().before(periodeDebut)
                    || (periodeFin != null && occupation.getDateValidite().after(periodeFin))) {
                addMessage(SpecificationMessage.POSTE_TRAVAIL_OCCUPATION_EN_DEHORS_PERIODE_ACTIVITE,
                        occupation.getDateValiditeAsValue());
            }
        }
        return true;
    }
}
