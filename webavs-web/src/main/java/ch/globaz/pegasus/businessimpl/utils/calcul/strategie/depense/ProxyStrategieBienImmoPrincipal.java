package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoPrincipal.StrategieBienImmoPrincipal;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoPrincipal.StrategieBienImmoPrincipalVS;
import ch.globaz.pegasus.utils.PCApplicationUtil;

public class ProxyStrategieBienImmoPrincipal extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (PCApplicationUtil.isCantonVS()) {
            return new StrategieBienImmoPrincipalVS().calcule(donnee, context, resultatExistant);
        } else {
            return new StrategieBienImmoPrincipal().calcule(donnee, context, resultatExistant);
        }
    }

}
