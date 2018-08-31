package ch.globaz.vulpecula.process.communicationsalaires;

import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.BProcessWithContext;

public abstract class AbstractCommunicationSalairesProcess extends BProcessWithContext {

    private Annee annee;

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }
}
