package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Taux;

public interface Interet {
    public Montant computeInteret(Taux taux);
}
