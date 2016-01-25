package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import java.util.List;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.businessimpl.services.pca.PeriodePca;

public class DataForGenerateOvs {
    private List<SimpleAllocationNoel> allocationsNoel;
    private List<CreanceAccordee> creanciers;
    private List<DetteCompenseCompteAnnexe> dettes;
    private List<SimpleJoursAppoint> joursAppointNew;
    private List<SimpleJoursAppoint> joursAppointReplaced;
    private List<PeriodePca> periodesPca;
    private boolean useAllocationNoel = false;
    private boolean useJourAppoints = false;

    public List<SimpleAllocationNoel> getAllocationsNoel() {
        return allocationsNoel;
    }

    public List<CreanceAccordee> getCreanciers() {
        return creanciers;
    }

    public List<DetteCompenseCompteAnnexe> getDettes() {
        return dettes;
    }

    public List<SimpleJoursAppoint> getJoursAppointNew() {
        return joursAppointNew;
    }

    public List<SimpleJoursAppoint> getJoursAppointReplaced() {
        return joursAppointReplaced;
    }

    public List<PeriodePca> getPeriodesPca() {
        return periodesPca;
    }

    public boolean isUseAllocationNoel() {
        return useAllocationNoel;
    }

    public boolean isUseJourAppoints() {
        return useJourAppoints;
    }

    public void setAllocationsNoel(List<SimpleAllocationNoel> allocationsNoel) {
        this.allocationsNoel = allocationsNoel;
    }

    public void setCreanciers(List<CreanceAccordee> creanciers) {
        this.creanciers = creanciers;
    }

    public void setDettes(List<DetteCompenseCompteAnnexe> dettes) {
        this.dettes = dettes;
    }

    public void setJoursAppointNew(List<SimpleJoursAppoint> joursAppointNew) {
        this.joursAppointNew = joursAppointNew;
    }

    public void setJoursAppointReplaced(List<SimpleJoursAppoint> joursAppointReplaced) {
        this.joursAppointReplaced = joursAppointReplaced;
    }

    public void setPeriodesPca(List<PeriodePca> periodesPca) {
        this.periodesPca = periodesPca;
    }

    public void setUseAllocationNoel(boolean useAllocationNoel) {
        this.useAllocationNoel = useAllocationNoel;
    }

    public void setUseJourAppoints(boolean useJourAppoints) {
        this.useJourAppoints = useJourAppoints;
    }
}
