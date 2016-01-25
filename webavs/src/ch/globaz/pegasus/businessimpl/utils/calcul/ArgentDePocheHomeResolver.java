package ch.globaz.pegasus.businessimpl.utils.calcul;

import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;

public class ArgentDePocheHomeResolver {

    enum CS_WITH_CALCUL_MAPPING {
        CS_EMS_NON_MEDICALISE_AGE(IPCTaxeJournaliere.CS_EMS_NON_MEDICALISE_AGE,
                Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE),
        CS_EMS_NON_MEDICALISE_PSY(IPCTaxeJournaliere.CS_EMS_NON_MEDICALISE_PSY,
                Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY),
        CS_ESE_HANDICAP_PHYSIQUE(IPCTaxeJournaliere.CS_ESE_HANDICAP_PHYSIQUE,
                Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE),
        CS_CATEGORIE_CHAMBRE_MEDICALISE(IPCTaxeJournaliere.CS_CATEGORIE_CHAMBRE_MEDICALISE,
                Attribut.CS_ARGENT_POCHE_MEDICALISE),
        CS_CATEGORIE_CHAMBRE_NON_MEDICALISE(IPCTaxeJournaliere.CS_CATEGORIE_CHAMBRE_NON_MEDICALISE,
                Attribut.CS_ARGENT_POCHE_NON_MEDICALISE);

        String cs;
        Attribut attribut;

        CS_WITH_CALCUL_MAPPING(String cs, Attribut attribut) {
            this.cs = cs;
            this.attribut = attribut;
        }
    };

    public static Float getMontantForCalcul(CalculContext context, String csTypeVariableMetier) throws CalculException {

        if (null == context || null == csTypeVariableMetier) {
            throw new CalculException("Null parameter not allowed here [" + ArgentDePocheHomeResolver.class.getName()
                    + "]");
        }

        CS_WITH_CALCUL_MAPPING mapping = getForCs(csTypeVariableMetier);

        if (null == mapping) {
            throw new CalculException("No mapping defined for the cs [" + csTypeVariableMetier + "]. ");
        }

        return Float.parseFloat(((ControlleurVariablesMetier) context.get(mapping.attribut)).getValeurCourante());
    }

    private static CS_WITH_CALCUL_MAPPING getForCs(String cs) {
        for (CS_WITH_CALCUL_MAPPING mapping : CS_WITH_CALCUL_MAPPING.values()) {
            if (mapping.cs.equals(cs)) {
                return mapping;
            }
        }

        return null;
    }

}
