/*
 * Créé le 27 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.module.calcul.rev1999;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCalculException;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPCalculateur;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;

/**
 * Description : Facade fournissant un point d'entrée pour le calcul des prestation APG selon la révision 1999.
 * 
 * @author scr
 * 
 * 
 */
public class APModuleCalculAPG implements IAPCalculateur {

    IAPReferenceDataPrestation refDataPrestation = null;

    /**
     * Description Constructeur de la classe
     */
    public APModuleCalculAPG() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.IAPCalculateur#calculerPrestation()
     */
    @Override
    public APResultatCalcul calculerPrestation(APBaseCalcul baseCalcul) throws Exception {

        APResultatCalcul result = null;
        IAPModuleCalcul alloc = null;

        if (IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(baseCalcul.getTypeAllocation())
                && baseCalcul.getNombreEnfants() == 0
                ||
                // Formation base protection civile
                (IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(baseCalcul.getTypeAllocation()) && baseCalcul
                        .getNombreEnfants() == 0)
                || (IAPDroitLAPG.CS_RECRUTEMENT.equals(baseCalcul.getTypeAllocation()) && baseCalcul.getNombreEnfants() == 0)
                || (IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES.equals(baseCalcul.getTypeAllocation()) && baseCalcul
                        .getNombreEnfants() == 0)) {
            alloc = new APModuleCalculAllocBaseRecrue();
        } else if (IAPDroitLAPG.CS_SERVICE_AVANCEMENT.equals(baseCalcul.getTypeAllocation())) {
            alloc = new APModuleCalculAllocServiceAvancement();
        } else if (IAPDroitLAPG.CS_ARMEE_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_COURS_MONITEURS_JEUNES_TIREURS.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_PROTECTION_CIVILE_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS.equals(baseCalcul.getTypeAllocation())
                || IAPDroitLAPG.CS_SERVICE_CIVIL_SERVICE_NORMAL.equals(baseCalcul.getTypeAllocation())
                || (IAPDroitLAPG.CS_SERVICE_EN_QUALITE_DE_RECRUE.equals(baseCalcul.getTypeAllocation()) && baseCalcul
                        .getNombreEnfants() > 0)
                || (IAPDroitLAPG.CS_RECRUTEMENT.equals(baseCalcul.getTypeAllocation()) && baseCalcul.getNombreEnfants() > 0)
                || (IAPDroitLAPG.CS_FORMATION_DE_BASE.equals(baseCalcul.getTypeAllocation()) && baseCalcul
                        .getNombreEnfants() > 0)
                || (IAPDroitLAPG.CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES.equals(baseCalcul.getTypeAllocation()) && baseCalcul
                        .getNombreEnfants() > 0))

        {
            alloc = new APModuleCalculAllocServiceNormal();
        } else {
            throw new APCalculException("Type d'allolcation non géré pour la révision 2005");
        }
        result = alloc.calculerMontantAllocation(baseCalcul, refDataPrestation);
        return result;
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
