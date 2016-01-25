package ch.globaz.vulpecula.domain.specifications.caissemaladie;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public class CMPeriodePosteTravailValideSpecification extends AbstractSpecification<AffiliationCaisseMaladie> {

    private PosteTravail poste;

    public CMPeriodePosteTravailValideSpecification(PosteTravail poste) {
        this.poste = poste;
    }

    @Override
    public boolean isValid(AffiliationCaisseMaladie affiliationCaisseMaladie) {
        Periode periodeCaisse = new Periode(affiliationCaisseMaladie.getMoisAnneeDebut(),
                affiliationCaisseMaladie.getMoisAnneeFin());

        // si même mois et même année
        // Dernier jours du mois doit être contenu dans la période du poste
        if (periodeCaisse.isSameMonthAndYear()) {
            if (!poste.getPeriodeActivite().contains(periodeCaisse.getDateDebut().getLastDayOfMonth())) {
                addMessage(SpecificationMessage.CM_PERIODE_POSTE_CM_INVALIDE);
            }
        }

        else {
            if (!poste.getPeriodeActivite().contains(periodeCaisse.getPeriodeDate1DernierJourDate2PremierJour())) {
                addMessage(SpecificationMessage.CM_PERIODE_POSTE_CM_INVALIDE);
            }
        }
        return true;
    }
}
