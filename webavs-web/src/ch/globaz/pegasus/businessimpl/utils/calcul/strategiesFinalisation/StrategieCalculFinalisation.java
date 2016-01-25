package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.util.Date;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public interface StrategieCalculFinalisation {

    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException;

}
