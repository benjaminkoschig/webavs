/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul.interfaces;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.globall.db.BSession;

/**
 * Description Interface définissant les méthodes communes au calcul des prestations
 *
 * @author scr
 *
 */
public interface IAPCalculateur {

    public APResultatCalcul calculerPrestation(APBaseCalcul baseCalcul, BSession session) throws Exception;

    public void setReferenceData(IAPReferenceDataPrestation referenceData);
}
