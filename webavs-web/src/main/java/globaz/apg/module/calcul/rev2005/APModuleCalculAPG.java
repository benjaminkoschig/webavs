/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCalculException;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPCalculateur;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.globall.db.BSession;
import globaz.jade.properties.JadePropertiesService;

/**
 * Description : Facade fournissant un point d'entrée pour le calcul des prestation APG selon la révision 1999.
 *
 * @author scr
 *
 *
 */

public class APModuleCalculAPG implements IAPCalculateur {

    IAPReferenceDataPrestation refDataPrestation = null;

    public APModuleCalculAPG() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.module.calcul.IAPCalculateur#calculerPrestation()
     */
    @Override
    public APResultatCalcul calculerPrestation(APBaseCalcul baseCalcul, BSession session) throws Exception {

        APResultatCalcul result = null;
        IAPModuleCalcul alloc = null;

        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(baseCalcul.getTypeAllocation())) {
            alloc = new APModuleCalculAllocMaternite();
        } else if ((IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(baseCalcul.getTypeAllocation())
                && (baseCalcul.getNombreEnfants() == 0)) ||
        // Formation base protection civile
                (IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() == 0))
                || (IAPDroitLAPG.CS_RECRUTEMENT.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() == 0))
                || (IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() == 0))
                || (IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() == 0))) {
            alloc = new APModuleCalculAllocBaseRecrue();
        } else if (IAPDroitLAPG.CS_SOF_EN_SERVICE_LONG.equals(baseCalcul.getTypeAllocation())) {
            alloc = new APModuleCalculAllocCadreServiceLong();
        } else if (IAPDroitLAPG.CS_SERVICE_AVANCEMENT.equals(baseCalcul.getTypeAllocation())) {
            alloc = new APModuleCalculAllocServiceAvancement();
        } else if (IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_COURS_MONITEURS_JEUNES_TIREURS.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_PROTECTION_CIVILE_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_PROTECTION_CIVILE_CADRE_SPECIALISTE.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_PROTECTION_CIVILE_COMMANDANT.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_SERVICE_CIVIL_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_SERVICE_INTERRUPTION_PENDANT_SERVICE_AVANCEMENT
                        .equals(baseCalcul.getTypeAllocation())
                || (IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() > 0))
                || (IAPDroitLAPG.CS_RECRUTEMENT.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() > 0))
                || (IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() > 0))
                || (IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() > 0))
                || (IAPDroitLAPG.CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF.equals(baseCalcul.getTypeAllocation())
                        && (baseCalcul.getNombreEnfants() > 0))) {
            alloc = new APModuleCalculAllocServiceNormal();
        } else if (isTypeAllocationJourIsole(baseCalcul.getTypeAllocation())) {
            APModuleCalculAllocJourIsole allocJoursIsole = new APModuleCalculAllocJourIsole();
            result = allocJoursIsole.calculerMontantAllocation(baseCalcul, refDataPrestation, session);
            return result;
        } else {
            throw new APCalculException("Type d'allocation non géré pour la révision 2005");
        }

        result = alloc.calculerMontantAllocation(baseCalcul, refDataPrestation);
        return result;
    }

    private Boolean isTypeAllocationJourIsole(String csTypeAllocation) {
        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        return (IAPDroitLAPG.CS_DEMENAGEMENT_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_NAISSANCE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_MARIAGE_LPART_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DECES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_JOURNEES_DIVERSES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_CONGE_JEUNESSE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_SERVICE_ETRANGER_CIAB.equals(csTypeAllocation)) && "true".equals(isFerciab);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.apg.module.calcul.IAPCalculateur#setReferenceData(globaz.apg.module
     * .calcul.IAPReferenceDataPrestation)
     */
    @Override
    public void setReferenceData(IAPReferenceDataPrestation referenceData) {
        refDataPrestation = referenceData;
    }

}
