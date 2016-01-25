package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Dette {
    public Montant computeDette();

    public Montant computeDetteBrut();
}
