package ch.globaz.vulpecula.domain.models.prestations;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

public interface PrestationParEmployeur extends Serializable {
    Employeur getEmployeur();
}
