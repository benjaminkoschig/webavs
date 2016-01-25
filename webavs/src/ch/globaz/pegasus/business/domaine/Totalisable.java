package ch.globaz.pegasus.business.domaine;

import java.math.BigDecimal;

/**
 * Ajout du comportement permettant de retourner un montant
 */
public interface Totalisable {

    public BigDecimal getMontant();
}
