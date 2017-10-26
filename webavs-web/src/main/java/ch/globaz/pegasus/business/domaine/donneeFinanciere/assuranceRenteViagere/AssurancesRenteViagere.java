package ch.globaz.pegasus.business.domaine.donneeFinanciere.assuranceRenteViagere;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.revenu.StrategieAssuranceRenteViagere;

public class AssurancesRenteViagere extends DonneesFinancieresList<AssuranceRenteViagere, AssurancesRenteViagere> {

    public AssurancesRenteViagere() {
        super(AssurancesRenteViagere.class);
    }

    public Montant sumWithStrategie() {
        return this.sum(new Each<AssuranceRenteViagere>() {

            @Override
            public Montant getMontant(AssuranceRenteViagere arv) {
                return StrategieAssuranceRenteViagere.calculRevenu(arv.getMontant(), arv.getExcedant());
            }
        });
    }
}
