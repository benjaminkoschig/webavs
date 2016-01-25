package ch.globaz.vulpecula.domain.models.prestations;

import ch.globaz.vulpecula.domain.models.common.Periode;

public interface PrestationSMAJ extends Prestation {
    void setPeriode(Periode periode);

    Periode getPeriode();
}
