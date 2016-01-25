package ch.globaz.vulpecula.businessimpl.services.absencesjustifiees;

import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.business.services.absencesjustifiees.AbsenceJustifieeService;
import ch.globaz.vulpecula.business.services.postetravail.PosteTravailService;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.domain.repositories.absencejustifiee.AbsenceJustifieeRepository;

public class AbsenceJustifieeServiceImpl implements AbsenceJustifieeService {
    private AbsenceJustifieeRepository absenceJustifieeRepository;
    private PosteTravailService posteTravailService;

    public AbsenceJustifieeServiceImpl(AbsenceJustifieeRepository absenceJustifieeRepository,
            PosteTravailService posteTravailService) {
        this.absenceJustifieeRepository = absenceJustifieeRepository;
        this.posteTravailService = posteTravailService;
    }

    @Override
    public AbsenceJustifiee create(AbsenceJustifiee absenceJustifiee) throws UnsatisfiedSpecificationException {
        absenceJustifiee.validate();
        // A désactiver pour la reprise
        if (!hasAssuranceActiveForAJ(absenceJustifiee)) {
            throw new UnsatisfiedSpecificationException(SpecificationMessage.PRESTATION_ASSURANCE_NON_ACTIVE);
        }

        if (absenceJustifiee.getEtat() == null) {
            absenceJustifiee.setEtat(Etat.SAISIE);
        }
        absenceJustifiee = absenceJustifieeRepository.create(absenceJustifiee);
        return absenceJustifiee;
    }

    private boolean hasAssuranceActiveForAJ(AbsenceJustifiee absenceJustifiee) {
        return posteTravailService.hasAssuranceActiveForX(absenceJustifiee.getPosteTravail(), absenceJustifiee
                .getPeriode().getDateDebut(), absenceJustifiee.getPeriode().getDateFin(),
                TypePrestation.ABSENCES_JUSTIFIEES);
    }
}
