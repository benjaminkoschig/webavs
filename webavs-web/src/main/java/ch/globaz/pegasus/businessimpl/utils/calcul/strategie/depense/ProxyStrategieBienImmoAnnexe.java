package ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense;

import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoAnnexe.StrategieBienImmoAnnexe;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.depense.strategieBienImmoAnnexe.StrategieBienImmoAnnexeVS;
import ch.globaz.pegasus.utils.PCApplicationUtil;

public class ProxyStrategieBienImmoAnnexe extends StrategieCalculDepense {

    @Override
    protected TupleDonneeRapport calculeDepense(CalculDonneesCC donnee, CalculContext context,
            TupleDonneeRapport resultatExistant) throws CalculException {

        if (PCApplicationUtil.isCantonVS()) {
            return new StrategieBienImmoAnnexeVS().calcule(donnee, context, resultatExistant);
        } else {
            return new StrategieBienImmoAnnexe().calcule(donnee, context, resultatExistant);
        }
    }

}
