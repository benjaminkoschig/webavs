package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;

public interface Each<T> {
    public Montant getMontant(T donnneeFianciere);
}
