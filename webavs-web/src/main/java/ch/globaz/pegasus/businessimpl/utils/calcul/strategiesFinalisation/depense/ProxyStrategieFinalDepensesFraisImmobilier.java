package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense;

import java.util.Date;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.StrategieCalculFinalisation;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategieFinalDepensesFraisImmobilier.StrategieFinalDepenseFraisImmobilier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.depense.strategieFinalDepensesFraisImmobilier.StrategieFinalDepenseFraisImmobilierVS;
import ch.globaz.pegasus.utils.PCApplicationUtil;

public class ProxyStrategieFinalDepensesFraisImmobilier implements StrategieCalculFinalisation {

    @Override
    public void calcule(TupleDonneeRapport donnee, CalculContext context, Date dateDebut) throws CalculException {

        if (PCApplicationUtil.isCantonVS()) {
            new StrategieFinalDepenseFraisImmobilierVS().calcule(donnee, context, dateDebut);
        } else {
            new StrategieFinalDepenseFraisImmobilier().calcule(donnee, context, dateDebut);
        }

    }

}
