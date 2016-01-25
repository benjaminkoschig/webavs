package ch.globaz.pegasus.businessimpl.utils.calcul.strategie;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;

public interface IStrategieDessaisissable {

    float calculeMontantDessaisi(CalculDonneesCC donnee, CalculContext context) throws CalculException;

}
