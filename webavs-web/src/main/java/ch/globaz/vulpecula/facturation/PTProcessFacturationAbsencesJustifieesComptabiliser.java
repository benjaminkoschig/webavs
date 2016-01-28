package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.prestations.Etat;

/**
 * Processus de comptabilisation des absences justifiées
 */
public final class PTProcessFacturationAbsencesJustifieesComptabiliser extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    public PTProcessFacturationAbsencesJustifieesComptabiliser() {
        super();
    }

    public PTProcessFacturationAbsencesJustifieesComptabiliser(final BProcess parent) {
        super(parent);
    }

    @Override
    protected void clean() {
    }

    @Override
    protected boolean launch() {
        List<AbsenceJustifiee> listeAbsences = VulpeculaRepositoryLocator.getAbsenceJustifieeRepository()
                .findDecomptesForFacturation(getPassage().getId());
        for (AbsenceJustifiee absence : listeAbsences) {
            majEtatAbsence(absence);
        }
        return true;
    }

    private void majEtatAbsence(final AbsenceJustifiee absence) {
        absence.setEtat(Etat.COMPTABILISEE);
        VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(absence);
    }
}
